package com.hn.api.diary.config;

import com.hn.api.diary.filter.AccessFilter;
import com.hn.api.diary.filter.SignInFilter;
import com.hn.api.diary.entity.User;
import com.hn.api.diary.exception.AccessDeniedHandler;
import com.hn.api.diary.exception.InvalidValue;
import com.hn.api.diary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@EnableWebSecurity(debug = false)
@Configuration
public class SecurityConfig {

    private final UserRepository userRepository;

    //    @Bean
//    public CorsConfigurationSource corsConfigurationSource(){
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
//        config.setAllowedMethods(Arrays.asList("*"));
//        config.setExposedHeaders(Arrays.asList("Authorization"));
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/favicon.ico"))
                .requestMatchers(AntPathRequestMatcher.antMatcher("/error"))
                .requestMatchers(toH2Console());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                // Security - CorsConfigurationSource 대신 MVC - addCorsMappings 사용.
                // 공식 문서에 .cors(cors -> cors.disable()) 로 통합 설정을 비활성화하고,
                // CorsConfigurationSource 써도 되는데 설정이 안 먹음.
                .cors(withDefaults())
                .authorizeHttpRequests((req) ->
                        req
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/signUp")).permitAll()

                                .requestMatchers(AntPathRequestMatcher.antMatcher("/user")).hasAnyRole("USER", "ADMIN")
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/admin")).hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                // accessFilter에서 /signIn 요청은 authFilter로 계속 진행되도록 설정.
                // authFilter에서 계속 진행되도록 설정하지 않아서
                // UsernamePasswordAuthenticationFilter 로직은 실행되지 않는다.
                .addFilterBefore(signInFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(accessFilter(), SignInFilter.class)
                .exceptionHandling(handling -> {
                    handling
                            .accessDeniedHandler(accessDeniedHandler());
                });

        return http.build();
    }

    @Bean
    public AccessFilter accessFilter(){
        return new AccessFilter();
    }

    @Bean
    public SignInFilter signInFilter(){
        SignInFilter signInFilter = new SignInFilter();
        signInFilter.setAuthenticationManager(authenticationManager());
        return signInFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService());
        return new ProviderManager(provider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                User user = userRepository.findByEmail(email)
                        .orElseThrow(InvalidValue::new);

                UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRole())
                        .build();

                return userDetails;
            }
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler();
    }
}
