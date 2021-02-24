package net.shyshkin.study.photoapp.api.users.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.shyshkin.study.photoapp.api.users.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${gateway.ip}")
    private String gatewayIp;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final Environment environment;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/**").hasIpAddress(gatewayIp)
                .and()
                .addFilter(appAuthenticationFilter());

        //        h2 console config
        http.headers().frameOptions().sameOrigin();
        //or just disable them
//        http.headers().frameOptions().disable();
    }

    private AppAuthenticationFilter appAuthenticationFilter() throws Exception {
        AppAuthenticationFilter authFilter = new AppAuthenticationFilter(objectMapper, environment, userService);
        authFilter.setAuthenticationManager(this.authenticationManager());
        authFilter.setFilterProcessesUrl(environment.getRequiredProperty("login.url.path"));
        return authFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

}
