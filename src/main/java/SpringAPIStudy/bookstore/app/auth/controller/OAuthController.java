package SpringAPIStudy.bookstore.app.auth.controller;

import SpringAPIStudy.bookstore.app.auth.dto.AuthRequest;
import SpringAPIStudy.bookstore.app.auth.dto.AuthResponse;
import SpringAPIStudy.bookstore.app.auth.service.CustomOAuth2Service;
import SpringAPIStudy.bookstore.app.common.dto.ApiResponse;
import SpringAPIStudy.bookstore.app.enums.Social;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
/*
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class OAuthController {

    private final CustomOAuth2Service customOAuth2Service;

    @GetMapping(value = "/{social}/login")
    public ApiResponse<AuthResponse> socialAuthRequest(@PathVariable("social")Social social,
                                           @Valid @RequestBody AuthRequest authRequest) {
        customOAuth2Service.loadUser((new OAuth2UserRequest(
                ClientRegistration.withRegistrationId(String.valueOf(authRequest.getSocial())).build(),
                OAuth2AccessToken.
                        authRequest.getAuthToken())));
        return ApiResponse.success(AuthResponse.builder()
                .userId()
                .nickname()
                .accessToken()
                .refreshToken()
                .build()
        );
    }



}
*/