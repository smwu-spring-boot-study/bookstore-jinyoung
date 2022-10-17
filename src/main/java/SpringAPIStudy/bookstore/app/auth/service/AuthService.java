package SpringAPIStudy.bookstore.app.auth.service;

import SpringAPIStudy.bookstore.app.auth.config.jwt.JwtTokenProvider;
import SpringAPIStudy.bookstore.app.auth.dto.RefreshRequest;
import SpringAPIStudy.bookstore.app.auth.dto.Token;
import SpringAPIStudy.bookstore.app.auth.dto.CustomUserDetails;
import SpringAPIStudy.bookstore.app.auth.entity.User;
import SpringAPIStudy.bookstore.app.auth.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    public Token refreshToken(RefreshRequest refreshRequest) {

        final String oldAccessToken = refreshRequest.getAccessToken();
        final String oldRefreshToken = refreshRequest.getRefreshToken();

        //1. Validate Refresh Token
        if (!tokenProvider.validateToken(oldRefreshToken)) {
            throw new RuntimeException("Not Validated Refresh Token");
        }

        //2. 유저 정보 얻기
        Authentication authentication = tokenProvider.getAuthentication(oldAccessToken);
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        String socialId = user.getSocialId();

        //3. Refresh Token DB와 Match
        String savedToken = userRepository.getRefreshTokenBySocialId(socialId);

        if (!savedToken.equals(oldRefreshToken)) {
            throw new RuntimeException("Not Matched Refresh Token");
        }

        //4. JWT 갱신
        Token token = tokenProvider.generateToken(authentication);
        return token;

    }

    public Long getUserId(String socialId) {

        User user = userRepository.findBysocialId(socialId)
                .orElseThrow(()->{
                    throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다.");}
                );

        return user.getId();


    }
}
