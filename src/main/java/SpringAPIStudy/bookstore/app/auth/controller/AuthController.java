package SpringAPIStudy.bookstore.app.auth.controller;

import SpringAPIStudy.bookstore.app.auth.dto.RefreshRequest;
import SpringAPIStudy.bookstore.app.auth.dto.Token;
import SpringAPIStudy.bookstore.app.auth.service.AuthService;
import SpringAPIStudy.bookstore.app.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

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
    public ResponseEntity<Token> refreshToken(HttpServletRequest request) {
        final RefreshRequest refreshHeader = refreshHeader(request);
        return ApiResponse.success(authService.refreshToken(refreshHeader));
    }

    //refresh시 accesstoken, refreshtoken을 전달받음
    private RefreshRequest refreshHeader(HttpServletRequest request) {
        String accessToken = request.getHeader("Access");
        String refreshToken = request.getHeader("Refresh");
        return RefreshRequest.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}