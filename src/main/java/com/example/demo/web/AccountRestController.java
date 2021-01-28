package com.example.demo.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.entities.AppRole;
import com.example.demo.entities.AppUser;
import com.example.demo.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AccountRestController {

    private AccountService accountService;
    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

     @GetMapping("/users")
     @PostAuthorize("hasAuthority('ADMIN')")
     public List<AppUser> appUserList()
     {
        return  accountService.USER_LIST(); }


     @PostMapping("/users")
     @PostAuthorize("hasAuthority('USER')")
     public  AppUser saveUser( @RequestBody AppUser appUser){
           return accountService.addNewUser(appUser);
    }

    @PostAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/roles")
    public  AppRole saveRole( @RequestBody AppRole appRole){
        return accountService.addNewRole(appRole);
    }


    @PostAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/addRoleToUser")
    void   addRoleToUser ( @RequestBody RoleUserFrom  roleUserFrom ){
          accountService.addRoleToUser(roleUserFrom.getUserName() , roleUserFrom.getRoleName());
    }

  @GetMapping("/refreshToken")
  public  void refreshToken(HttpServletRequest request , HttpServletResponse response) throws IOException {


      String  authorizationToken=request.getHeader("Authorization");
      // System.out.println(" ***********Authorization Filter **************");
      //System.out.println("jwt:"+authorizationToken );
      if(authorizationToken !=null && authorizationToken.startsWith("Bearer "))
      {
          try {

              String jwt=authorizationToken.substring(7);

              Algorithm algorithm= Algorithm.HMAC256("ZakSecrit010");
              JWTVerifier jwtVerifier= JWT.require(algorithm).build();
              //after dycripte jwt we get  (username, rols,......) if there is no issue with JWT
              DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
              String username=decodedJWT.getSubject();
              // when we got user name we can check in Black list Service
              //we load user and his roles in case one of the roles changes
              AppUser appUser=accountService.loadUserByUserName(username);

             //-----------------------------------------------------------------
              // Create JWT Token =Header+Payload+Signature
              String JwtAccessToken= JWT.create()
                      .withSubject(appUser.getUsername())
                      .withExpiresAt( new Date(System.currentTimeMillis()+(5*60*1000)))  // Expiration in 5 minuts
                      .withIssuer(request.getRequestURI())  // severU rl
                      .withClaim("roles" ,appUser.getAppRoles().stream().map(r ->r.getRolename()).collect(Collectors.toList())) // convert getAuthority to String list by Streams
                      .sign(algorithm); // Signature


              Map<String,String> idToken= new HashMap<>();
              idToken.put("access-token",JwtAccessToken);
              idToken.put("refresh-token",authorizationToken);

              //sand the JWT Token generated(access & refresh) to Client by Http Body
              response.setContentType("application/json");
              new ObjectMapper().writeValue(response.getOutputStream() , idToken);


          }catch (Exception e){
              response.setHeader("error-message",e.getMessage());
              response.sendError(HttpServletResponse.SC_FORBIDDEN);
          }

      }else {
           new RuntimeException("Refresh Token Required ");
      }



  }


}
@Data
class RoleUserFrom{
  private  String userName;
  private  String roleName;
}