package org.mukulphougat.userindexerservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class GatewayTrustedFilter extends OncePerRequestFilter {

    private static final String GATEWAY_SECRET = "123e4567-e89b-12d3-a456-426614174000";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String secret = request.getHeader("X-Gateway-Secret");
        if (!GATEWAY_SECRET.equals(secret)) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Forbidden");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
