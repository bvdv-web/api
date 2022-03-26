package com.bvdv.bvdvapi.config;

import com.bvdv.bvdvapi.config.jwt.AuthEntryPointJwt;
import com.bvdv.bvdvapi.config.jwt.JwtTokenAuthenticationFilter;
import com.bvdv.bvdvapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public JwtTokenAuthenticationFilter authenticationJwtTokenFilter() {
        return new JwtTokenAuthenticationFilter();
    }

    //
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
//                .antMatchers(HttpMethod.POST, "/auth/**").permitAll()
                // .antMatchers(HttpMethod.GET, "/user/support-version").permitAll()
                // .antMatchers(HttpMethod.GET, "/users/all", "/users/", "users").hasAnyRole("admin", "ADMIN", "administrator", "Admin")
//                .antMatchers(HttpMethod.GET, "/books", "/books/*").authenticated()
//                .antMatchers("/books", "/books/*").authenticated()
//                .antMatchers("/users", "/user", "/users/*", "/user/*").authenticated()
//                .antMatchers("/Files", "/Files/**").authenticated()
//                .antMatchers("/roles", "/roles/**", "/role", "/role/**").authenticated()
//                .antMatchers("/file-folder", "/file-folder/**").authenticated()

                .anyRequest().permitAll();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    }
}