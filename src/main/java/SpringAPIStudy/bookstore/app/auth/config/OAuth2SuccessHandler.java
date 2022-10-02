package SpringAPIStudy.bookstore.app.auth.config;

import SpringAPIStudy.bookstore.app.auth.dto.UserRequestMapper;
import SpringAPIStudy.bookstore.app.auth.dto.UserDto;
import SpringAPIStudy.bookstore.app.auth.entity.Token;
import SpringAPIStudy.bookstore.app.auth.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
    private final TokenService tokenService;
    private final UserRequestMapper userRequestMapper;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        UserDto userDto = userRequestMapper.toDto(oAuth2User);

        //최초 로그인이면 회원가입 처리함

        log.info("Principle에서 꺼낸 OAuth2User = {}", oAuth2User);

        log.info("토큰 발행 시작");
        Token token = tokenService.generateToken(userDto.getEmail(), "USER"); //AccessToken, RefreshToken발급
        log.info("{}", token);


        /*String targetUrl;
        targetUrl = UriComponentsBuilder.fromUriString("/api/v1/home") //token포함해 redirect
                .queryParam("token", "token")
                .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
*/

        writeTokenResponse(response, token);

    }

    private void writeTokenResponse(HttpServletResponse response, Token token)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        response.addHeader("Auth", token.getToken());
        response.addHeader("Refresh", token.getRefreshToken());
        response.setContentType("application/json;charset=UTF-8");

        var writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(token));
        writer.flush();
    }
}
