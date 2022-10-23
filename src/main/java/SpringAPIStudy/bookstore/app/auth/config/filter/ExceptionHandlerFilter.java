package SpringAPIStudy.bookstore.app.auth.config.filter;

import SpringAPIStudy.bookstore.app.common.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        } catch (JwtException e){
            log.error("[ExceptionHandlerFilter] JwtException");
            setExceptionResponse(response, ApiResponse.unAuthorized(e));
        } catch (Exception e){
            log.error("[ExceptionHandlerFilter] Exception");
            setExceptionResponse(response, ApiResponse.internalServerError(e));
        }
    }

    private void setExceptionResponse(HttpServletResponse response, ApiResponse<Exception> apiResponse) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        var writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(apiResponse));
        writer.flush();
    }

}
