package SpringAPIStudy.bookstore.app.auth.config.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class JwtValidation {

    @Value("${jwt.secret}")
    private static String secretKey = "MyNickNameisErjuerAndNameisMinsu";

    public static boolean validateToken(String token) { //받은 토큰으로 클레임의 유효기간 체크, 유효 시 true 리턴
        log.info("[validateToken] 토큰 유효성 검사");
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token);
            return !claims.getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (MalformedJwtException e) {
            throw new JwtException("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            throw new JwtException("JWT Expired");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            throw new JwtException("JWT token compact of handler are invalid.");
        }
    }

    // Access Token 만료시 갱신때 사용할 정보를 얻기 위해 Claim 리턴
    public static Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (Exception e) {
            throw new JwtException("AccessToken Parse Failed");
        }
    }

}