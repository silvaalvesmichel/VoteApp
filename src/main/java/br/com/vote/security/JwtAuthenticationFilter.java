package br.com.vote.security;

import br.com.vote.resources.dto.response.JwtResponse;
import br.com.vote.service.dto.CredentialDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private  JwtUtil jwtUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {

        try{
            CredentialDTO credentialDTO = new ObjectMapper()
                    .readValue(req.getInputStream(), CredentialDTO.class);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(credentialDTO.getEmail(), credentialDTO.getPassword(), new ArrayList<>());

            return authenticationManager.authenticate(authToken);
        }catch(IOException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public void successfulAuthentication(HttpServletRequest req,
                                         HttpServletResponse res,
                                         FilterChain chain,
                                         Authentication auth) throws IOException, ServletException {

        String username = ((UserSS) auth.getPrincipal()).getUsername();
        String token = jwtUtil.generateToken(username);
        res.addHeader("Authorization", "Bearer " + token);
        UserSS user = ((UserSS) auth.getPrincipal());
        JwtResponse response = JwtResponse.builder()
                .token(token)
                .user(UserSS.builder().id(user.getId()).email(user.getEmail()).build())
                .build();
        Gson gson = new Gson();
        String json = gson.toJson(response);
        res.getWriter().write(json);

    }

    private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
                throws IOException, ServletException {
            response.setStatus(401);
            response.setContentType("application/json");
            response.getWriter().append(json());
        }

        private String json() {
            long date = new Date().getTime();
            return "{\"timestamp\": " + date + ", "
                    + "\"status\": 401, "
                    + "\"error\": \"Não autorizado\", "
                    + "\"message\": \"Email ou senha inválidos\", "
                    + "\"path\": \"/login\"}";
        }
    }
}