package com.MindHub.HomeBanking.configurations;





import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@EnableWebSecurity
@Configuration
public class WebAuthorization {



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/rest/**").denyAll()
                .antMatchers("/web/index.html","/web/register.html", "/web/javascript/**","/web/styles/**","/web/images/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients").permitAll()
                .antMatchers("/rest/**", "/h2-console/**", "/manager.html", "/web/settings.html", "/web/create-loan.html").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST,"/api/loans/create").hasAuthority("ADMIN")
                .antMatchers("/web/accounts.html", "/web/account.html", "/web/cards.html", "/404.html","/web/create-cards.html","/web/settings.html").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/clients/current/**").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.PATCH,"/api/clients/current/password").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.PATCH,"/api/clients/current/cards").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.PATCH,"/api/clients/current/accounts/disabled").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/transactions").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/transactions/pdf").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/clients/current/transactions/payments").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/loans").hasAuthority("CLIENT");



        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");

        
        // turn off checking for CSRF tokens
        http.csrf().disable();

        //disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response
        // http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        http.exceptionHandling().authenticationEntryPoint((request, response, auth) -> response.sendRedirect("/web/index.html"));

        http.exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler());

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));


        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }



}
