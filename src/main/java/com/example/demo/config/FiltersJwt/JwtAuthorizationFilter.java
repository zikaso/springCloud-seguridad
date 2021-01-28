package com.example.demo.config.FiltersJwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.config.JwtUuil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    //this method execute  with  every client  request
    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain filterChain) throws ServletException, IOException {


        if(httpRequest.getServletPath().equals("/refreshToken") ||  httpRequest.getServletPath().equals("/login")){
            // go to the next Filter to get JWT
            filterChain.doFilter(httpRequest , httpResponse);
        }else
        {

          // ex: visa de client
           String  authorizationToken=httpRequest.getHeader(JwtUuil.AUTH_HEDER);

          // System.out.println(" ***********Authorization Filter **************");
          //System.out.println("jwt:"+authorizationToken );

            if(authorizationToken !=null && authorizationToken.startsWith("Bearer "))
            {
                     try {

                         String jwt=authorizationToken.substring(7);

                         Algorithm  algorithm= Algorithm.HMAC256(JwtUuil.SECRET);
                         JWTVerifier jwtVerifier= JWT.require(algorithm).build();
                         //after dycripte jwt we get  (username, rols,......) if there is no issue with JWT
                         DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
                         String username=decodedJWT.getSubject();
                         String[] roles=decodedJWT.getClaim("roles").asArray(String.class);
                         // Convert String roles to GrantAuthority
                         Collection<GrantedAuthority> authorities= new ArrayList<>();
                         for (String r:roles) {
                             authorities.add( new SimpleGrantedAuthority(r));
                         }

                         UsernamePasswordAuthenticationToken authenticationToken =
                                 new UsernamePasswordAuthenticationToken(username,null,authorities);

                        //  ADD to Spring Security   the user and his roles
                         SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                        // if it's oki -> go to the next Filter
                         filterChain.doFilter(httpRequest , httpResponse);

                       }catch (Exception e){
                         httpResponse.setHeader("error-message",e.getMessage());
                         httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                     }

            }
            else {

               // System.out.println(" ***********Authorization Filter invalid **************");
                    // go to the next Filter to get JWT
                  filterChain.doFilter(httpRequest , httpResponse);
            }

        }

    }
}
