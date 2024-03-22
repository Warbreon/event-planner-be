package com.cognizant.EventPlanner.security.jwt;

import com.cognizant.EventPlanner.util.ErrorResponseEntityUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    private static final int BEARER_PREFIX_LENGTH = 7;
    private static final String BEARER_PREFIX = "Bearer ";

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        if (requestTokenHeader == null || !requestTokenHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = requestTokenHeader.substring(BEARER_PREFIX_LENGTH);

        if (!processTokenAuthentication(jwtToken, request, response)) {
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean processTokenAuthentication(String jwtToken, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String email = jwtTokenUtil.getEmailFromToken(jwtToken);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

                if (jwtTokenUtil.validateToken(jwtToken)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    return true;
                }
            }
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired", e);
            ErrorResponseEntityUtil.writeErrorResponse(response, HttpStatus.UNAUTHORIZED, "JWT token is expired", request.getRequestURI());
        } catch (JwtException e) {
            logger.error("JWT token validation failed", e);
            ErrorResponseEntityUtil.writeErrorResponse(response, HttpStatus.BAD_REQUEST, "JWT token validation failed", request.getRequestURI());
        } catch (Exception e) {
            logger.error("Unexpected error during JWT token validation", e);
            ErrorResponseEntityUtil.writeErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error during JWT token validation", request.getRequestURI());
        }

        return false;
    }

}
