package com.example.demo.service;

import com.example.demo.entities.AppRole;
import com.example.demo.entities.AppUser;

import java.util.List;

public interface AccountService {

    AppUser addNewUser( AppUser appUser);
    AppRole addNewRole( AppRole appRole);
    void addRoleToUser( String userName, String RoleName);
    AppUser loadUserByUserName( String userName);
    List<AppUser> USER_LIST();


}
