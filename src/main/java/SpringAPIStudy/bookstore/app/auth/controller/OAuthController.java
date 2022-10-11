package SpringAPIStudy.bookstore.app.auth.controller;

import SpringAPIStudy.bookstore.app.auth.dto.AuthRequest;
import SpringAPIStudy.bookstore.app.auth.dto.AuthResponse;
import SpringAPIStudy.bookstore.app.auth.enums.Social;
import SpringAPIStudy.bookstore.app.auth.service.AuthService;
import SpringAPIStudy.bookstore.app.auth.service.CustomOAuth2Service;
import SpringAPIStudy.bookstore.app.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class OAuthController {

    private final AuthService authService;
    //private final CustomOAuth2Service customOAuth2Service;

    /*@GetMapping(value = "/{social}/login")
    public ApiResponse<AuthResponse> socialAuthRequest(@PathVariable("social") Social social,
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
    }*/

    @PostMapping("/refresh")
    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response,
                                       @RequestBody String accessToken, @RequestBody String refreshToken) {
        return ResponseEntity.ok().body(authService.refreshToken(request, response, accessToken, refreshToken));
    }

}