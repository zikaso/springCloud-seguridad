package com.example.demo;

import com.example.demo.entities.AppRole;
import com.example.demo.entities.AppUser;
import com.example.demo.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class SpringCloudSeguridadApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudSeguridadApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }


    @Bean
    CommandLineRunner start(AccountService accountService){
       return  args -> {
           accountService.addNewRole( new AppRole(null , "USER"));
           accountService.addNewRole( new AppRole(null , "ADMIN"));
           accountService.addNewRole( new AppRole(null , "PERSONNA-MANAGER"));
           accountService.addNewRole( new AppRole(null , "PRODUCTO-MANAGER"));
           accountService.addNewRole( new AppRole(null , "FACTORA-MANAGER"));

           accountService.addNewUser( new AppUser(null,"user","user", new ArrayList<>()));
           accountService.addNewUser( new AppUser(null,"admin","admin", new ArrayList<>()));
           accountService.addNewUser( new AppUser(null,"admin1","admin1", new ArrayList<>()));
           accountService.addNewUser( new AppUser(null,"admin2","admin2", new ArrayList<>()));
           accountService.addNewUser( new AppUser(null,"admin3","admin3", new ArrayList<>()));


           accountService.addRoleToUser("user" ,"USER");
           accountService.addRoleToUser("user" ,"PERSONNA-MANAGER");

           accountService.addRoleToUser("admin" ,"USER");
           accountService.addRoleToUser("admin" ,"ADMIN");
           accountService.addRoleToUser("admin" ,"PERSONNA-MANAGER");
           accountService.addRoleToUser("admin" ,"PRODUCTO-MANAGER");
           accountService.addRoleToUser("admin" ,"FACTORA-MANAGER");

           accountService.addRoleToUser("admin1" ,"PERSONNA-MANAGER");
           accountService.addRoleToUser("admin2" ,"PRODUCTO-MANAGER");
           accountService.addRoleToUser("admin3" ,"FACTORA-MANAGER");




       };
    }
}
