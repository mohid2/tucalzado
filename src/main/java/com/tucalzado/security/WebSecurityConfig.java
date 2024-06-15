package com.tucalzado.security;


import com.tucalzado.service.impl.OAuth2UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final OAuth2UserServiceImpl oAuth2UserService;

    String[] resources = new String[]{"assets/css/**", "assets/js/**", "assets/img/**", "assets/fonts/**", "assets/vendor/**","assets/webfonts/**"};

    public WebSecurityConfig(OAuth2UserServiceImpl oAuth2UserService) {
        this.oAuth2UserService = oAuth2UserService;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz->
                        authz.requestMatchers(resources).permitAll()
                                .requestMatchers("/","/tienda","/producto/**","/registro","/iniciar-sesion","/sobre-nosotros",
                                        "/contacto","/error/**","/auth/check","/cargar-productos/**").permitAll()
                                .requestMatchers("/agregar/**").hasRole("ADMIN")
                                .anyRequest().authenticated())
                .formLogin((form) -> form
                        .loginPage("/iniciar-sesion")
                        .defaultSuccessUrl("/")
                        .loginProcessingUrl("/login")
                        .failureUrl("/iniciar-sesion?error=true")
                        .passwordParameter("password")
                        .usernameParameter("username")
                        .permitAll()
                ).oauth2Login(oauth2 -> oauth2
                        .loginPage("/iniciar-sesion")
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(oAuth2UserService))
                        .defaultSuccessUrl("/")
                        .failureUrl("/iniciar-sesion?error=true"))
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/cerrar-sesion"))
                        .logoutSuccessUrl("/")
                        .permitAll());
        return http.build();

    }

}
