package com.example.advancedjava.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.example.advancedjava.service.JwtService;
import com.example.advancedjava.service.MyUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthFilters extends OncePerRequestFilter {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    JwtService jwtService;

    @Autowired
    MyUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUserName(token);
        }

        if (username != null) {
            if (username.equalsIgnoreCase("A001")) {
                // Create an authentication token for the ADMIN user
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        null,
                        null,
                        List.of(new SimpleGrantedAuthority("ADMIN")));
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("Authentication set for ADMIN user: A001");
            } else if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("Authentication set for user: {}", username);
                }
            }
        }

        LocalDateTime ldt = LocalDateTime.now();

    
        // Execute the filter chain with the wrapped request and response
        filterChain.doFilter(wrappedRequest, wrappedResponse);

        // Extract and log request and response bodies
        String requestBody = getRequestBody(wrappedRequest);

        requestBody = requestBody.replaceAll("\n", "");
        requestBody = requestBody.replaceAll(" ", "");
        logRequest(wrappedRequest, requestBody);

        String timeTaken = String.valueOf(Duration.between(ldt, LocalDateTime.now()).getNano());
        String responseBody = getResponseBody(wrappedResponse);
        logResponse(wrappedRequest, wrappedResponse, responseBody, timeTaken);

        // Copy the response content back to the original response
        wrappedResponse.copyBodyToResponse();
    }

    private void logResponse(HttpServletRequest request, HttpServletResponse response, String body, String timeTaken) {
        String data = String.format("Status: %s, Requested URL: %s, Response Body: %s, Time Taken: %s ms\n",
                response.getStatus(),
                request.getRequestURL(), body, timeTaken);
        log.info(data);
    }

    private void logRequest(HttpServletRequest request, String body) {

        String data = String.format("Method: %s, Requested URL: %s: Request Body: %s \n", request.getMethod(),
                request.getRequestURL(), body);
        log.info(data);

    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        byte[] buf = request.getContentAsByteArray();
        return buf.length > 0 ? new String(buf, StandardCharsets.UTF_8) : "";
    }

    private String getResponseBody(ContentCachingResponseWrapper response) {
        byte[] buf = response.getContentAsByteArray();
        return buf.length > 0 ? new String(buf, StandardCharsets.UTF_8) : "";
    }

}
