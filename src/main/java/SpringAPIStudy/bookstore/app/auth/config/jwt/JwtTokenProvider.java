package SpringAPIStudy.bookstore.app.auth.config.jwt;

import SpringAPIStudy.bookstore.app.auth.dto.CustomUserDetails;
import SpringAPIStudy.bookstore.app.auth.dto.Token;
import SpringAPIStudy.bookstore.app.user.repository.UserRepository;
import io.jsonwebtoken.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtTokenProvider {

    private final UserRepository userRepository;
    private final RedisTemplate redisTemplate;
    @Value("${jwt.secret}")
    private String secretKey = "MyNickNameisErjuerAndNameisMinsu";

    private final long tokenPeriod = 1000L * 60L * 60L; //60분 후 만료
    private final long refreshPeriod = 1000L * 60L * 60L * 24L * 30L *3L; //3주

    @PostConstruct //의존성 주입 후 실행
    protected void init() { //secretKey를 BASE64 encoding
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }


    @Transactional
    public Token generateToken(Authentication authentication) {

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        String socialId = user.getSocialId();
        String nickname = user.getNickname();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Date now = new Date();

         Token token = new Token(
                nickname,
                Jwts.builder()
                        .setSubject(socialId)
                        .claim("nickname", nickname)
                        .claim("role", role)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + tokenPeriod))
                        .signWith(SignatureAlgorithm.HS256, secretKey) //암호화 알고리즘, secretKey세팅
                        .compact(),
                Jwts.builder()
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + refreshPeriod))
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact());

        saveRefreshToken(authentication, token.getRefreshToken());
        return token;
    }

    @Transactional
    private void saveRefreshToken(Authentication authentication, String refreshToken) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        String socialId = user.getName();

        userRepository.updateRefreshToken(socialId, refreshToken);
    }

    //AccessToken 검사 정보로 Authentication 객체 생성
    public Authentication getAuthentication(String accessToken) { //filter에서 인증 성공 시 SecurityContext에 저장할 Authentication 생성
        Claims claims = JwtValidation.parseClaims(accessToken);

        log.info("[getAuthentication] 토큰 인증 정보 조회 시작");
        if (claims.get("role") == null) { throw new JwtException("AccessToken Parse Failed"); } //access대신 refresh 넣었을 때 대비

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("role").toString().split(","))
                        .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        String nickname = claims.get("nickname", String.class);

        CustomUserDetails principal = new CustomUserDetails(claims.getSubject(), nickname, authorities);
        log.info("[getAuthentication] 토큰 인증 정보 조회 완료, UserDetails socialId : {}", principal.getUsername());
        log.info("[getAuthentication] 토큰 인증 정보 조회 완료, UserDetails authorities : {}", authorities);
        log.info("[getAuthentication] 토큰 인증 정보 조회 완료, UserDetails nickname : {}", nickname);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public Boolean checkBlackList(String accessToken) {
        //redis 뒤져서 accesstoken 존재하면 로그아웃/탈퇴한 유저임 -> 해당 accessToken으로 요청할 수 없음(false)
        String inBlackList = (String) redisTemplate.opsForValue().get(accessToken);
        if(ObjectUtils.isEmpty(inBlackList)) { return true; }
        else { return false; }
    }

    public Long getExpiration(String accessToken) { // accessToken 남은 유효시간
        Date expiration = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken).getBody().getExpiration();
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    public String getSocialId(String token) {
        log.info("[getSocialId] 토큰 기반 회원 socialId 추출");
        String socialId = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        log.info("[getSocialId] 완료, socialId : {}", socialId);
        return socialId;
    }


    //Client의 request 헤더 값으로 받은 토큰 값 리턴
    public String resolveToken(HttpServletRequest request) {
        log.info("[resolveToken] Http Header에서 Token 추출");
        return request.getHeader("Authorization");
    }


}
