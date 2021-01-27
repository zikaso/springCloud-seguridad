package com.example.demo.repositorys;

import com.example.demo.entities.AppUser;
import org.springframework.data.jpa.mapping.JpaPersistentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepo extends JpaRepository<AppUser , Long> {

    AppUser findByUsername( String username);
}
