package SpringAPIStudy.bookstore.app.auth.config;

import SpringAPIStudy.bookstore.app.auth.service.CustomOAuth2Service;
import SpringAPIStudy.bookstore.app.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2Service customOAuth2Service;
    private final OAuth2SuccessHandler successHandler;
    private final TokenService tokenService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                    .cors().and() //cors활성화
                    .csrf().disable() //session안 쓸 것
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션 관리x
                .and()
                    .authorizeRequests() //아래부터 인증 절차 설정하겠다
                    .antMatchers("/api/v1/token/**").permitAll()
                    .anyRequest().authenticated() //그외는 인증된 사용자만 접근 가능
                //.antMatchers("/login").permitAll() //누구에게나 허용
                //.antMatchers("/user").hasRole("USER") //USER권한이 있을 때만 /user접근 가능
                .and()
                    .oauth2Login().loginPage("/api/v1/token/expired")
                    .successHandler(successHandler)
                //.exceptionHandling().accessDeniedHandler("/accessDenied")
                //.and()
                //.logout()//.logoutUrl("/logout")
                //.logoutSuccessUrl("/").permitAll() //logout시 root로 이동
                //.and()
                    .userInfoEndpoint() //oauth로그인 성공 후 설정 시작
                    .userService(customOAuth2Service); //custom한 oauthservice연결

        http.addFilterBefore(new JwtAuthFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
        //SpringSecurity의 UsernamePasswordAuthFilter가 실행되기 전 JwtAuthFilter먼저 실행
    }
}
