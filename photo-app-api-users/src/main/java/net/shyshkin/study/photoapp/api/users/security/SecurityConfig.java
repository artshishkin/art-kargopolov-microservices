package net.shyshkin.study.photoapp.api.users.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.photoapp.api.users.services.UserService;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.InetAddress;

@Slf4j
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final Environment environment;
    private final ServerProperties serverProperties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String gatewayIp = environment.getProperty("gateway.ip", "127.0.0.1");
        if ("all_subnet".equals(gatewayIp)) {
//            String myIpAddress = environment.getProperty("server.address");
            InetAddress address = serverProperties.getAddress();
            if (address == null) {
                gatewayIp = "0.0.0.0/0";
            } else {
                String myIpAddress = address.getHostAddress();
                int lastDotIndex = myIpAddress.lastIndexOf(".");
                gatewayIp = myIpAddress.substring(0, lastDotIndex) + ".0/24";
            }
        }
        log.debug("GateWay IP for security {}", gatewayIp);

        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/actuator/health").permitAll()
//                .antMatchers("/**").permitAll()
                .antMatchers(HttpMethod.POST, "/users").hasIpAddress(gatewayIp)
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(appAuthenticationFilter())
                .addFilter(new JwtAuthorizationFilter(this.authenticationManager(), environment));

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
