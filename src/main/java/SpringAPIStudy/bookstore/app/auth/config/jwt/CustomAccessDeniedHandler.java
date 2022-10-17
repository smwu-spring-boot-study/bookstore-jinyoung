package SpringAPIStudy.bookstore.app.auth.config.jwt;

import SpringAPIStudy.bookstore.app.auth.dto.AuthResponse;
import SpringAPIStudy.bookstore.app.common.dto.ApiResponse;
import SpringAPIStudy.bookstore.app.common.dto.ErrorResponse;
import SpringAPIStudy.bookstore.app.common.dto.ResponseData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler { //권한 없는 리소스에 접근 시

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ObjectMapper objectMapper = new ObjectMapper();
        log.info("[handle] 권한 없는 요청입니다.");

        ErrorResponse entryPointErrorResponse = new ErrorResponse();
        entryPointErrorResponse.setStatus(HttpStatus.FORBIDDEN);
        entryPointErrorResponse.setMsg("권한 없는 요청입니다.");

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        //response.getWriter().write(objectMapper.writeValueAsString(entryPointErrorResponse));
        var writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(entryPointErrorResponse));
        writer.flush();
    }
}
