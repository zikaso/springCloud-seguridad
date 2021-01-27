package com.example.demo.repositorys;

import com.example.demo.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepo extends JpaRepository<AppRole , Long> {

    AppRole findByRolename( String rolename);

}
