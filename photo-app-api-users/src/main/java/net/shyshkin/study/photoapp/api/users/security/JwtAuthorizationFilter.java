package net.shyshkin.study.photoapp.api.users.security;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final Environment environment;

    @Value("${authorization.token.header.prefix:Bearer }")
    private String bearerValue;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, Environment environment) {
        super(authenticationManager);
        this.environment = environment;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String authorizationHeader = request.getHeader(environment.getProperty("authorization.token.header.name", HttpHeaders.AUTHORIZATION));

        Optional
                .ofNullable(authorizationHeader)
                .filter(auth -> auth.startsWith(bearerValue))
                .map(this::getAuthentication)
                .ifPresent(token -> SecurityContextHolder.getContext().setAuthentication(token));

        chain.doFilter(request, response);

    }

    private UsernamePasswordAuthenticationToken getAuthentication(String authorizationHeader) {
        String jwtToken = authorizationHeader.replaceFirst(bearerValue, "").trim();

        String userId = null;
        try {
            userId = Jwts
                    .parser()
                    .setSigningKey(environment.getRequiredProperty("token.secret"))
                    .parseClaimsJws(jwtToken)
                    .getBody()
                    .getSubject();
        } catch (Exception exception) {
            log.error(exception.getLocalizedMessage());
            return null;
        }
        if (userId == null) return null;
        return new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
    }
}
