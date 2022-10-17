package SpringAPIStudy.bookstore.app.auth.config;

import SpringAPIStudy.bookstore.app.auth.config.jwt.CustomAccessDeniedHandler;
import SpringAPIStudy.bookstore.app.auth.config.jwt.CustomAuthenticationEntryPoint;
import SpringAPIStudy.bookstore.app.auth.config.jwt.JwtAuthFilter;
import SpringAPIStudy.bookstore.app.auth.config.jwt.JwtTokenProvider;
import SpringAPIStudy.bookstore.app.auth.service.CustomOAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http    .cors().and()
                .httpBasic().disable()
                    .csrf().disable() //session안 쓸 것
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션 관리x
                .and()
                    .authorizeRequests() //아래부터 인증 절차 설정하겠다
                    .antMatchers(HttpMethod.OPTIONS).permitAll()
                    .antMatchers("/oauth2/**", "/api/v1/auth/**").permitAll()
                    .antMatchers("/admin/**").hasAnyRole("ADMIN")
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
        //SpringSecurity의 UsernamePasswordAuthFilter가 실행되기 전 JwtAuthFilter먼저 실행
        //http.addFilterAfter(new JwtAuthFilter(jwtTokenProvider), LogoutFilter.class);
    }

    @Override
    public void configure(WebSecurity webSecurity){ //스프링 시큐리티(httpSecurity인증,인가) 적용 전
        webSecurity.ignoring().antMatchers("/h2-console/**", "/favicon.ico", "/v2/api-docs", "/configuration/**", "/swagger-resources/**",
                "/swagger-ui.html", "/webjars/**", "/swagger/**", "/h2-console/**");

    }
}
