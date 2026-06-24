package com.quiz.platform.shared.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.platform.shared.dtos.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWT authentication filter for validating and processing JWT tokens.
 * Extracts tokens from requests and sets up security context.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String USER_ID_CONTEXT = "userId";

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (jwt != null && !jwt.isEmpty()) {
                authenticateRequest(request, jwt);
            }
            chain.doFilter(request, response);
        } catch (JwtException e) {
            handleJwtException(request, response, e);
        } finally {
            ThreadContext.remove(USER_ID_CONTEXT);
            ThreadContext.clearAll();
        }
    }

    private void authenticateRequest(HttpServletRequest request, String jwt) {
        validateToken(jwt);
        TokenPayload tokenPayload = jwtUtil.extractTokenPayload(jwt);
        UserInfo userInfo = new UserInfo(tokenPayload);

        ThreadContext.put(USER_ID_CONTEXT, userInfo.getUserId());
        setupSecurityContext(request, userInfo);
    }

    private void validateToken(String jwt) {
        if (!jwtUtil.validateToken(jwt)) {
            log.info("Invalid token: {}", jwt);
            throw new SecurityException("Invalid token submitted");
        }
    }

    private void setupSecurityContext(HttpServletRequest request, UserInfo userInfo) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userInfo, null, userInfo.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTH_HEADER);
        if (bearerToken != null && !bearerToken.isEmpty() && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private void handleJwtException(HttpServletRequest request, HttpServletResponse response, JwtException e)
            throws IOException {
        String message;
        int errorCode = 403;

        if (e instanceof ExpiredJwtException) {
            message = "User access token expired";
            errorCode = 401;
            log.error("ExpiredJwtException {} returning error code 401: {}", request.getRequestURI(), e.getMessage());
        } else {
            message = e.getMessage() == null ? "Invalid JWT token submitted" : e.getMessage();
            log.error("JwtException {} returning error code 403: {}", request.getRequestURI(), e.getMessage());
        }

        createErrorResponse(errorCode, message, response);
    }

    private void createErrorResponse(int errorCode, String errorMessage, HttpServletResponse response)
            throws IOException {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setSuccess(false);
        apiResponse.setMessage(errorMessage);
        apiResponse.setErrorCode(String.valueOf(errorCode));

        response.setStatus(errorCode);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
