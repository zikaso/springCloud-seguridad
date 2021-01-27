package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class AppUser {

    @Id @GeneratedValue
    private  Long id;
    private  String  username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private  String  password;
    @ManyToMany(fetch =FetchType.EAGER )
    private Collection<AppRole> appRoles = new ArrayList<>();


}
