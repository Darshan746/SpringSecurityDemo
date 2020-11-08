package com.example.demo.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class JwtUserNameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    //@Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    public JwtUserNameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request
                                                , HttpServletResponse response) throws AuthenticationException{
        try {
            UserNameAndPasswordAuthenticationRequest inputRequest = new ObjectMapper()
                    .readValue(request.getInputStream(),
                            UserNameAndPasswordAuthenticationRequest.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(inputRequest.getUsername(), inputRequest.getPassword());
           Authentication authentication1 =  authenticationManager.authenticate(authentication);
            return authentication1;

        }   catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    /**
     * This Method will invoke only if above method is passed
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request
                                            , HttpServletResponse response
                                            , FilterChain chain
                                            , Authentication authResult) throws IOException, ServletException {

        String key  = "SecureSecureSecureSecureSecureSecureSecureSecureSecureSecureSecureSecureSecureSecureSecureSecureSecureSecureSecureSecureSecureSecure"; //this can be anything to build the 3rd part of the awt token
       String token =  Jwts.builder()
                .setSubject(authResult.getName()) // this will set the First part of the token. This authResult would get from the previous method call  result
                . claim("authorities", authResult.getAuthorities())  // this will set the First part of the token
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
                .signWith(Keys.hmacShaKeyFor(key.getBytes()))
                .compact();//this will build the entire

        // now we have to send the token to the client back for that we have to add the token to the response header

        response.addHeader("Authorization", "Bearer "+token);
        chain.doFilter(request,response);

    }
}
