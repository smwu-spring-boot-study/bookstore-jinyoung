package SpringAPIStudy.bookstore.app.auth.config.jwt;

import SpringAPIStudy.bookstore.app.common.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint { //인증 실패

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        log.info("[commence] 인증 실패로 response.sendError 발생");

        ErrorResponse entryPointErrorResponse = new ErrorResponse();
        entryPointErrorResponse.setStatus(HttpStatus.UNAUTHORIZED);
        entryPointErrorResponse.setMsg("인증이 실패했습니다.");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        var writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(entryPointErrorResponse));
        writer.flush();
        //response.getWriter().write(objectMapper.writeValueAsString(entryPointErrorResponse));
        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED);

    }
}
