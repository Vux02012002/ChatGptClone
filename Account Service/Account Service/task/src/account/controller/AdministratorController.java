package account.controller;

import account.entity.User;
import account.model.SuccessStatus;
import account.model.UserUpdatedRoleResponse;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdministratorController {
    UserService userService;

    @Autowired
    public AdministratorController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping(value ="/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping(value = "user/{email}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SuccessStatus deleteUser(@PathVariable String email) {
        return userService.deleteUser(email);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping(value = "/user/role", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User updateRole(@RequestBody @Valid UserUpdatedRoleResponse userUpdatedRole) {
        return userService.updateRole(userUpdatedRole);
    }
}
