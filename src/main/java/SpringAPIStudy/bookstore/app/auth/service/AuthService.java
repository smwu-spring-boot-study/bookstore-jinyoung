package SpringAPIStudy.bookstore.app.auth.service;

import SpringAPIStudy.bookstore.app.auth.config.jwt.JwtTokenProvider;
import SpringAPIStudy.bookstore.app.auth.config.jwt.JwtValidation;
import SpringAPIStudy.bookstore.app.auth.dto.RefreshRequest;
import SpringAPIStudy.bookstore.app.auth.dto.Token;
import SpringAPIStudy.bookstore.app.auth.dto.CustomUserDetails;
import SpringAPIStudy.bookstore.app.user.entity.User;
import SpringAPIStudy.bookstore.app.user.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.NoSuchElementException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    public Token refreshToken(RefreshRequest refreshRequest) { //access토큰 만료 시 클라가 요청

        final String oldAccessToken = refreshRequest.getAccessToken();
        final String oldRefreshToken = refreshRequest.getRefreshToken();

        //1. Validate Refresh Token
        if (!JwtValidation.validateToken(oldRefreshToken)) { //둘 다 만료 -> 재로그인 필요
            throw new JwtException("JWT Expired");
        }

        //2. 유저 정보 얻기
        Authentication authentication = tokenProvider.getAuthentication(oldAccessToken);
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        String socialId = user.getSocialId();

        //3. Refresh Token DB와 Match
        String savedToken = userRepository.getRefreshTokenBySocialId(socialId);

        if (!savedToken.equals(oldRefreshToken)) {
            throw new JwtException("Refresh Token Not Matched");
        }

        //4. JWT 갱신
        Token token = tokenProvider.generateToken(authentication);
        return token;

    }

    public Long getUserId(String socialId) {

        User user = userRepository.findBysocialId(socialId)
                .orElseThrow(()->{ throw new NoSuchElementException("User Not Found");});

        return user.getId();

    }
}
