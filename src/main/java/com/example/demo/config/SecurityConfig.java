package com.example.demo.config;

import com.example.demo.config.FiltersJwt.JwtAuthenticationFilter;
import com.example.demo.config.FiltersJwt.JwtAuthorizationFilter;
import com.example.demo.entities.AppUser;
import com.example.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
   @Autowired
    private AccountService accountService;

    @Override
    // (2) check the Authentication by Service
    public void configure(AuthenticationManagerBuilder authBuilder) throws Exception {
        // using our custom service
        authBuilder.userDetailsService(new UserDetailsService() {
            // when user write his authentication call  loadUserByUsername and give me the userName
            @Override
            public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

                AppUser appUser=accountService.loadUserByUserName(userName);
                // Class Spring Scurity

                Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();

                appUser.getAppRoles().forEach(appRole -> {
                    grantedAuthorities.add( new SimpleGrantedAuthority(appRole.getRolename()));
                });
                return new User(appUser.getUsername() , appUser.getPassword(),grantedAuthorities);
            }
        });

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        // disable check for CSRF attack -- >> Spring hide code for inputs Forms   **stateFull mode
         http.csrf().disable();

         //** using sessions in mode stateLess
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
      // add intepcerture  for all client request and check it by these Jwt Filters
        http.addFilter( new JwtAuthenticationFilter(authenticationManagerBean()));
        http.addFilterBefore( new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        // check user roles
        //http.authorizeRequests().antMatchers(HttpMethod.POST ,"/users/**").hasAnyAuthority("ADMIN");
       // http.authorizeRequests().antMatchers(HttpMethod.GET ,"/users/**").hasAnyAuthority("USER");



        // disable check for  iframe in html page    // just for H2 console and  refreshToken and  login
        http.headers().frameOptions().disable();

        http.authorizeRequests().antMatchers("/h2-console/**","/refreshToken/**","/login/**").permitAll();

        // allow access to all resources without authenticated
       //  http.authorizeRequests().anyRequest().permitAll();

        // required an  login form access
         //http.formLogin();



        // allow access to all resources with authenticated
        http.authorizeRequests().anyRequest().authenticated();


    }

     @Bean
     @Override  // need to inject in to Filter Class
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
