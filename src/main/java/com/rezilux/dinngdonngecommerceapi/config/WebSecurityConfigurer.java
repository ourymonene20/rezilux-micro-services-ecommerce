package com.rezilux.dinngdonngecommerceapi.config;


import com.rezilux.dinngdonngecommerceapi.security.CustomUserDetailService;
import com.rezilux.dinngdonngecommerceapi.security.JwtAuthenticationEntryPoint;
import com.rezilux.dinngdonngecommerceapi.security.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

   /* @Autowired
    public void init(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.
                userDetailsService(userDetailsService()).
                passwordEncoder(encoder());
    }*/


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling().authenticationEntryPoint(entryPoint())
            .and().addFilterAfter(tokenFilter(), UsernamePasswordAuthenticationFilter.class)
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/api/authenticate").permitAll()
            .antMatchers(HttpMethod.POST, "/api/inscription").permitAll()
            .antMatchers(HttpMethod.POST, "/api/forgot-password").permitAll()
            .antMatchers(HttpMethod.POST, "/api/retrieve-email").permitAll()
            .antMatchers(HttpMethod.POST, "/api/change-password").permitAll()
            .antMatchers(HttpMethod.POST, "/api/account").permitAll()
            .antMatchers(HttpMethod.GET, "/api/categories").permitAll()
            .antMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
            .antMatchers(HttpMethod.GET, "/api/merchants").permitAll()
            .antMatchers(HttpMethod.GET, "/api/merchants/**").permitAll()
            .antMatchers(HttpMethod.GET, "/api/articles").permitAll()
            .antMatchers(HttpMethod.GET, "/api/articles/**").permitAll()
            .antMatchers(HttpMethod.POST, "/api/forgot-password-mobile").permitAll()
            .antMatchers(HttpMethod.POST, "/api/code-verification-mobile").permitAll()
            .antMatchers( "/api/account/**").permitAll()
            .antMatchers("/socket/**").permitAll()
            .antMatchers("/actuator/**").permitAll()
            // .antMatchers("/api/_search/**").permitAll()
            .antMatchers("/assets/**").permitAll()
            .anyRequest().
            authenticated();
            
    }
    

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService()).passwordEncoder(encoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }

    @Bean
    UserDetailsService userDetailService() {
        return new CustomUserDetailService();
    }

    @Bean
    BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtAuthenticationEntryPoint entryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    @Bean
    JwtAuthenticationTokenFilter tokenFilter() {

        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(86400L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
