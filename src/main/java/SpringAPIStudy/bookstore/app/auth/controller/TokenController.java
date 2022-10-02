package SpringAPIStudy.bookstore.app.auth.controller;

import SpringAPIStudy.bookstore.app.auth.entity.Token;
import SpringAPIStudy.bookstore.app.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class TokenController {
    private final TokenService tokenService;

    @GetMapping("/api/v1/test")
    public String index() {
        return "Hello world";
    }

    @GetMapping("/api/v1/token/expired")
    public String expired() {
        throw new RuntimeException();
    }

    @GetMapping("/api/v1/token/refresh")
    public String refreshAuth(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Refresh");

        if (token != null && tokenService.verifyToken(token)) {
            String email = tokenService.getUid(token);
            Token newToken = tokenService.generateToken(email, "USER");

            response.addHeader("Auth", newToken.getToken());
            response.addHeader("Refresh", newToken.getRefreshToken());
            response.setContentType("application/json;charset=UTF-8");

            return "HAPPY NEW TOKEN";
        }
        throw new RuntimeException();
    }
}