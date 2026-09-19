package com.clinicapi.infra.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.clinicapi.domain.user.UserRepository;
import com.clinicapi.infra.exceptions.StandardError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String tokenJWT = recoverToken(request);

        if (tokenJWT != null) {
            try {
                String subject = tokenService.getSubject(tokenJWT);

                UserDetails user = userRepository.findByLogin(subject);

                // Token assinado corretamente, mas o usuário não existe mais no banco.
                // Também é 401: a credencial não identifica ninguém válido.
                if (user == null) {
                    writeUnauthorized(request, response, "User not found for the provided token");
                    return;
                }

                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (JWTVerificationException e) {
                writeUnauthorized(request, response, e.getMessage());
                return; // encerra a requisição aqui: nada de doFilter
            }
        }

        filterChain.doFilter(request, response);
    }

    private void writeUnauthorized(HttpServletRequest request, HttpServletResponse response, String message)
            throws IOException {

        HttpStatus status = HttpStatus.UNAUTHORIZED;

        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Unauthorized",
                message,
                request.getRequestURI()
        );

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), error);
    }

    private String recoverToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        // Sem header ou sem o prefixo correto: não há token a validar.
        // A requisição segue sem autenticação e o SecurityConfig decide se ela passa.
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        return authorizationHeader.substring(7).trim();
    }
}