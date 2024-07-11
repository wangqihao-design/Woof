package org.lls.woof.woofauth.config;




import org.lls.woof.woofauth.dao.UserDao;
import org.lls.woof.woofauth.service.JwtAuthenticationFilter;
import org.lls.woof.woofauth.service.RedisBlackListFilter;
import org.lls.woof.woofauth.service.impl.RedisTokenService;
import org.lls.woof.woofauth.utils.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final JwtUtils jwtUtils;

    private final UserDetailsService userDetailsService;

    private final RedisTokenService redisTokenService;

    private final UserDao userDao;

    public SecurityConfig(@Lazy JwtUtils jwtUtils, @Lazy UserDetailsService userDetailsService, @Lazy RedisTokenService redisTokenService, @Lazy UserDao userDao) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.redisTokenService = redisTokenService;
        this.userDao = userDao;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter(), AnonymousAuthenticationFilter.class)
                .addFilterBefore(redisBlackListFilter(), JwtAuthenticationFilter.class)
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/api/auth/**")
                                .permitAll()
                                .anyRequest().authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .httpBasic(withDefaults());
        return http.build();
    }


    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtils, userDetailsService);
    }

    @Bean
    public RedisBlackListFilter redisBlackListFilter() {
        return new RedisBlackListFilter(redisTokenService, jwtUtils, userDao);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}
