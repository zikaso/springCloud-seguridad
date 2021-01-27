package com.example.demo.service;

import com.example.demo.entities.AppRole;
import com.example.demo.entities.AppUser;
import com.example.demo.repositorys.AppRoleRepo;
import com.example.demo.repositorys.AppUserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional

public class AccountServiceImpl implements AccountService {

    private AppUserRepo appUserRepo;
    private AppRoleRepo appRoleRepo;
    private PasswordEncoder passwordEncoder;


    public AccountServiceImpl(AppUserRepo appUserRepo, AppRoleRepo appRoleRepo, PasswordEncoder passwordEncoder) {
        this.appUserRepo = appUserRepo;
        this.appRoleRepo = appRoleRepo;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public AppUser addNewUser(AppUser appUser) {

        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));

        return appUserRepo.save(appUser);
    }

    public AppRole addNewRole(AppRole appRole)
    {
        return appRoleRepo.save(appRole);
    }

    @Override
    public void addRoleToUser(String userName, String RoleName)
    {
       AppUser appUser=appUserRepo.findByUsername(userName);
       AppRole appRole=appRoleRepo.findByRolename(RoleName);
       appUser.getAppRoles().add(appRole);
    }

    @Override
    public AppUser loadUserByUserName(String userName) {

        return appUserRepo.findByUsername(userName);
    }

    @Override
    public List<AppUser> USER_LIST() {

        return appUserRepo.findAll();
    }
}
