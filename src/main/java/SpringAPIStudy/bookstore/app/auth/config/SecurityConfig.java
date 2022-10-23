package SpringAPIStudy.bookstore.app.auth.config;

import SpringAPIStudy.bookstore.app.auth.config.filter.ExceptionHandlerFilter;
import SpringAPIStudy.bookstore.app.auth.config.filter.JwtAuthFilter;
import SpringAPIStudy.bookstore.app.auth.config.handler.CustomAccessDeniedHandler;
import SpringAPIStudy.bookstore.app.auth.config.handler.CustomAuthenticationEntryPoint;
import SpringAPIStudy.bookstore.app.auth.config.handler.OAuth2SuccessHandler;
import SpringAPIStudy.bookstore.app.auth.config.jwt.*;
import SpringAPIStudy.bookstore.app.auth.service.CustomOAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2Service customOAuth2Service;
    private final OAuth2SuccessHandler successHandler;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private final ExceptionHandlerFilter exceptionHandlerFilter;

    @Override
    public void configure(WebSecurity webSecurity) throws Exception{ //스프링 시큐리티(httpSecurity인증,인가) 적용 전
        webSecurity.ignoring().antMatchers("/h2-console/**", "/favicon.ico", "/v2/api-docs", "/configuration/**",
                 "/webjars/**", "/swagger*/**", "/h2-console/**");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http    .headers() // 아래에 X-Frame-Option 헤더 설정을 위해 headers() 작성
                .frameOptions().sameOrigin() // 동일 도메인에서는 iframe 접근 가능하도록 X-Frame-Options을 smaeOrigin()으로 설정
                .and().cors().and()
                .httpBasic().disable()
                    .csrf().disable() //session안 쓸 것
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션 관리x
                .and()
                    .authorizeRequests() //아래부터 인증 절차 설정하겠다
                    .antMatchers(HttpMethod.OPTIONS).permitAll()
                    .antMatchers("/oauth2/**").permitAll()
                    .antMatchers("/api/v1/auth/**").permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                     //.antMatchers(HttpMethod.GET, "/product/**").permitAll()
                    .anyRequest().authenticated(); //그외는 인증된 사용자만 접근 가능

        http
                .oauth2Login()
                .userInfoEndpoint() //oauth로그인 성공 후 설정 시작
                .userService(customOAuth2Service) //custom한 oauthservice연결 -> oAuth2User반환
                .and().successHandler(successHandler)
                .permitAll()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)//인증 과정에서 오류 발생
                .accessDeniedHandler(customAccessDeniedHandler); //권한 확인 과정에서 예외 발생 시 전달
                //.logout()//.logoutUrl("/logout")
                //.logoutSuccessUrl("/").permitAll() //logout시 root로 이동


        http.addFilterBefore(new JwtAuthFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(exceptionHandlerFilter, JwtAuthFilter.class);
        //SpringSecurity의 UsernamePasswordAuthFilter가 실행되기 전 JwtAuthFilter먼저 실행
        //http.addFilterAfter(new JwtAuthFilter(jwtTokenProvider), LogoutFilter.class);
    }

}
