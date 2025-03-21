package demo.config;


import demo.models.UserRoles;
import demo.repositories.PassengerRepository;
import demo.services.impl.AppUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class AppSecurityConfiguration {
    private PassengerRepository passengerRepository;
    public AppSecurityConfiguration(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityContextRepository securityContextRepository) throws Exception {
        http
                .authorizeHttpRequests(
                        authorizeHttpRequests ->
                                authorizeHttpRequests
                                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                                        .permitAll()
                                        .requestMatchers("/favicon.ico").permitAll()
                                        .requestMatchers("/error").permitAll()
                                        .requestMatchers("/", "/users/login", "/users/register", "/users/login-error", "/tour", "/city", "/city/{id}", "/tour/{id}", "/tour/find", "/tour/top", "/trip/soon").permitAll()
                                        .requestMatchers("/users/profile", "/users/edit").authenticated()
                                        .requestMatchers("/bus", "/driver").hasRole(UserRoles.ADMIN.name())
                                        .anyRequest().authenticated())
                .formLogin((formLogin) ->
                                formLogin.
                                        loginPage("/users/login").
                                        usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY).
                                        passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY).
                                        defaultSuccessUrl("/").
                                        failureForwardUrl("/users/login-error"))
                .logout((logout) ->
                        logout.logoutUrl("/users/logout").
                                logoutSuccessUrl("/").
                                invalidateHttpSession(true))
                .securityContext(
                        securityContext -> securityContext.
                                securityContextRepository(securityContextRepository));
        return http.build();
    }
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        );
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsService userDetailsService() { return new AppUserDetailsService(passengerRepository); }
}
