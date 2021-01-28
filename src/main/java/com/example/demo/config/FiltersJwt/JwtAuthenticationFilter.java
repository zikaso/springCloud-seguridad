package com.example.demo.config.FiltersJwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.config.JwtUuil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class JwtAuthenticationFilter  extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    // (1) when the user try to login
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
       String userName=request.getParameter("username");
       String password=request.getParameter("password");
        System.out.println("** attemptAuthentication >>> "+userName+ " password "+ password );

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName ,password );
        // this trigger the authentication process by call  loadUserByUsername in  SecurityConfig calss
        //And check the login and password from data base by the custom service  in (2)  Config -> AuthenticationManagerBuilder
        return authenticationManager.authenticate(authenticationToken);
    }


    @Override
    // (3) after checking  the login success AuthenticationManagerBuilder SecurityConfig calss
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        System.out.println(" **** ok successfulAuthentication ****");

       // spring object -> return user authentified with name and his roles
        User user= (User) authResult.getPrincipal();

        // generate JWT by auth.JWT maven

        // and also and algorithm fo make signature RSA
        Algorithm  algorithm= Algorithm.HMAC256(JwtUuil.SECRET);

        // Create JWT Token =Header+Payload+Signature
    String JwtAccessToken= JWT.create()
            .withSubject(user.getUsername())
            .withExpiresAt( new Date(System.currentTimeMillis()+JwtUuil.EXPARE_ACCESS_TOKEN))  // Expiration in 5 minuts
            .withIssuer(request.getRequestURI())  // severU rl
            .withClaim("roles" ,user.getAuthorities().stream().map(aga ->aga.getAuthority()).collect(Collectors.toList())) // convert getAuthority to String list by Streams
            .sign(algorithm); // Signature

          // sand  the JWT generated to Client by Http Header
          // response.setHeader("Authorization", JwtAccessToken);
          // after user sand a request to  ex:http://localhost:8085/login   first time

//--------------------------------------------
        // Create JWT Token  de Refrichement in case something (password ,.....)change regarding Auth1
        // when the  JwtAccessToken expired and instead of  the user sand login name and password
        // he should just sand  JwtRefreshToken below  and this Filter sand him a new  JwtAccessToken
        // after make sure tha the  last JwtAccessToken is expired by checking the <<black list>>

        String JwtRefreshToken= JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt( new Date(System.currentTimeMillis()+JwtUuil.EXPARE_REFRESH_TOKEN))  // Expiration in 60 minuts
                .withIssuer(request.getRequestURL().toString())  // severU rl
                .sign(algorithm); // Signature

        Map<String,String> idToken= new HashMap<>();
        idToken.put("access-token",JwtAccessToken);
        idToken.put("refresh-token",JwtRefreshToken);

        //sand the JWT Token generated(access & refresh) to Client by Http Body
            response.setContentType("application/json");
          new ObjectMapper().writeValue(response.getOutputStream() , idToken);

    }
}
