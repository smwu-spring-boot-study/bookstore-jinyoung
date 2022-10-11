package SpringAPIStudy.bookstore.app.auth.config;

import SpringAPIStudy.bookstore.app.auth.config.jwt.JwtTokenProvider;


import SpringAPIStudy.bookstore.app.auth.config.jwt.Token;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component //로그인 완료 시 진입
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // JWT 생성
        Token token = tokenProvider.generateToken(authentication);
        writeTokenResponse(response, token);

    }

    private void writeTokenResponse(HttpServletResponse response, Token token)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        response.addHeader("Access", token.getToken());
        response.addHeader("Refresh", token.getRefreshToken());
        response.setContentType("application/json;charset=UTF-8");

        var writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(token));
        writer.flush();
    }
}
