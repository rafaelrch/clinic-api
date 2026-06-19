package com.clinicapi.infra.security;

import com.clinicapi.domain.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String tokenJWT = recoverToken(request);

        if(tokenJWT != null){
            String subject = tokenService.getSubject(tokenJWT); //  Validar o token e devolve o login do dono. Se o token for inválido, dispara exception
            // obs: "adicionar handler pra JWTVerificationException retornando 401"

            UserDetails user = userRepository.findByLogin(subject); // Busca o usuário no banco. Por isso o UserRepository.findByLogin()

            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            /* Cria um objeto de autenticação com os seguintes parametros
                • user -> o principal(quem é)
                • null -> as credentials(não precisa, ja validamos via JWT)
                • user.getAuthorities() -> as permissões(["ROLE_ADMIN"], etc)
             */
            SecurityContextHolder.getContext().setAuthentication(authentication); // Cola o "post-it". A partir desse momento, o Spring sabe quem é o usuário.
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader == null){
            return null;
        }
        return authorizationHeader.replace("Bearer ", "");
    }
}