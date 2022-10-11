package SpringAPIStudy.bookstore.app.auth.service;

import SpringAPIStudy.bookstore.app.auth.config.jwt.JwtTokenProvider;
import SpringAPIStudy.bookstore.app.auth.config.jwt.Token;
import SpringAPIStudy.bookstore.app.auth.dto.CustomUserDetails;
import SpringAPIStudy.bookstore.app.auth.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    public String refreshToken(HttpServletRequest request, HttpServletResponse response,
                               String oldAccessToken, String oldRefreshToken) {

        //1. Validation Refresh Token
        if (!tokenProvider.validateToken(oldRefreshToken)) {
            throw new RuntimeException("Not Validated Refresh Token");
        }

        //2. 유저 정보 얻기
        Authentication authentication = tokenProvider.getAuthentication(oldAccessToken);
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        String socialId = user.getSocialId();

        //3. Match Refresh Token
        String savedToken = userRepository.getRefreshTokenBySocialId(socialId);

        if (!savedToken.equals(oldRefreshToken)) {
            throw new RuntimeException("Not Matched Refresh Token");
        }

        //4. JWT 갱신
        Token token =  tokenProvider.generateToken(authentication);
        String accessToken = token.getToken();
        return accessToken;

    }
}
