package SpringAPIStudy.bookstore.app.auth.config.filter;

import SpringAPIStudy.bookstore.app.auth.config.jwt.JwtTokenProvider;
import SpringAPIStudy.bookstore.app.auth.config.jwt.JwtValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class JwtAuthFilter extends OncePerRequestFilter {//jwtTokenProvider를 통해 httpRequest에서 토큰 추출, 유효성 검사 후 SecurityContext에 Auth 추가

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                 FilterChain chain) throws IOException, ServletException {

        final String token = jwtTokenProvider.resolveToken(request); //request Header 통해 accessToken받음
        log.info("[doFilterInternal] token값 추출 완료. token : {}", token);

        if (token != null) {
            if (JwtValidation.validateToken(token)) { //jwt 유효성 검사 통과
                //jwt인증 성공 시 SecurityContext에 해당 userDetails, 권한 정보 저장
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("[doFilterInternal] {}의 인증 정보 저장", auth.getName());
            }
        }
        chain.doFilter(request, response);
    }

}
