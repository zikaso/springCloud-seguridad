package com.example.demo.web;

import com.example.demo.entities.AppRole;
import com.example.demo.entities.AppUser;
import com.example.demo.service.AccountService;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountRestController {

    private AccountService accountService;
    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

     @GetMapping("/users")
     public List<AppUser> appUserList(){
        return  accountService.USER_LIST();
    }
     @PostMapping("/users")
    public  AppUser saveUser( @RequestBody AppUser appUser){
           return accountService.addNewUser(appUser);
    }

    @PostMapping("/roles")
    public  AppRole saveRolz( @RequestBody AppRole appRole){
        return accountService.addNewRole(appRole);
    }

    @PostMapping("/addRoleToUser")
    void   addRoleToUser ( @RequestBody RoleUserFrom  roleUserFrom ){
         accountService.addRoleToUser(roleUserFrom.getUserName() , roleUserFrom.getRoleName());
    }

}
@Data
class RoleUserFrom{
  private  String userName;
  private  String roleName;
}