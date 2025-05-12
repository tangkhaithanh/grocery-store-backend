package org.api.grocerystorebackend.security;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.api.grocerystorebackend.utils.JwtProperties;
import org.api.grocerystorebackend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtFilter extends GenericFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AccountDetailsService accountDetailsService;

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authHeader = httpRequest.getHeader(jwtProperties.getHeader());

        String email = null;
        String token = null;

        if (authHeader != null && authHeader.startsWith(jwtProperties.getPrefix())) {
            token = authHeader.substring(jwtProperties.getPrefix().length());
            email = jwtUtil.extractEmail(token);
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = accountDetailsService.loadUserByUsername(email);
            if (jwtUtil.validateToken(token, email)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        chain.doFilter(request, response);
    }
}
