package SpringAPIStudy.bookstore.app.auth.config.jwt;

import SpringAPIStudy.bookstore.app.auth.dto.CustomUserDetails;
import SpringAPIStudy.bookstore.app.auth.respository.UserRepository;
import io.jsonwebtoken.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class JwtTokenProvider {

    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secretKey = "MyNickNameisErjuerAndNameisMinsu";

    private final long tokenPeriod = 1000L * 60L * 60L; //60분 후 만료
    private final long refreshPeriod = 1000L * 60L * 60L * 24L * 30L *3L; //3주

    @PostConstruct //의존성 주입 후 실행
    protected void init() { //secretKey를 BASE64 encoding
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }


    public Token generateToken(Authentication authentication) {

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        String socialId = user.getSocialId();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Date now = new Date();

         Token token = new Token(
                Jwts.builder()
                        .setSubject(socialId)
                        .claim("role", role)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + tokenPeriod))
                        .signWith(SignatureAlgorithm.HS256, secretKey) //암호화 알고리즘, secretKey세팅
                        .compact(),
                Jwts.builder()
                        .setSubject(socialId)
                        .claim("role", role)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + refreshPeriod))
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact());

        saveRefreshToken(authentication, token.getRefreshToken());
        return token;
    }

    private void saveRefreshToken(Authentication authentication, String refreshToken) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        String socialId = user.getName();

        userRepository.updateRefreshToken(socialId, refreshToken);
    }

    //AccessToken 검사 정보로 Authentication 객체 생성
    public Authentication getAuthentication(String accessToken) { //filter에서 인증 성공 시 SecurityContext에 저장할 Authentication 생성
        Claims claims = parseClaims(accessToken);

        log.info("[getAuthentication] 토큰 인증 정보 조회 시작");
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("role").toString().split(","))
                        .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        CustomUserDetails principal = new CustomUserDetails(claims.getSubject(), "", authorities);
        log.info("[getAuthentication] 토큰 인증 정보 조회 완료, UserDetails socialId : {}", principal.getUsername());
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public String getSocialId(String token) {
        log.info("[getsocialId] 토큰 기반 회원 socialId 추출");
        String info = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        log.info("[getsocialId] 완료, info : {}", info);
        return info;
    }

    //Client의 request 헤더 값으로 받은 토큰 값 리턴
    public String resolveToken(HttpServletRequest request) {
        log.info("[resolveToken] Http Header에서 Token 추출");
        return request.getHeader("Auth");
    }

    public boolean validateToken(String token) { //받은 토큰으로 클레임의 유효기간 체크, 유효 시 true 리턴
        log.info("[validateToken] 토큰 유효성 검사");
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return !claims.getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (ExpiredJwtException  e) {
            log.info("[validateToken] JWT 만료");
        } catch (IllegalStateException  e) {
            log.info("[validateToken] 잘못된 JWT 토큰");
        } catch (Exception e) {
            log.info("[validateToken] 토큰 유효성 검사 예외 발생");
        }
        return false;
    }

    // Access Token 만료시 갱신때 사용할 정보를 얻기 위해 Claim 리턴
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
