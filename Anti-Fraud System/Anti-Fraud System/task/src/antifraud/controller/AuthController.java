package antifraud.controller;

import antifraud.dto.request.AccessRequest;
import antifraud.dto.response.AccessResponse;
import antifraud.dto.response.DeleteResponse;
import antifraud.dto.request.RoleRequest;
import antifraud.entity.User;
import antifraud.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private UserService userService;

    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> signUp(@RequestBody @Valid User user) {
        return new ResponseEntity<>(userService.signUp(user), HttpStatus.CREATED);
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getListOfUser() {
        return userService.getAllUsers();
    }

    @DeleteMapping(value = "/user/{username}")
    public DeleteResponse deleteUser(@PathVariable String username) {
        return userService.deleteUser(username);
    }

    @PutMapping(value = "/role", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User modifyRole(@RequestBody RoleRequest roleRequest) {
        return userService.modifyRole(roleRequest);
    }

    @PutMapping(value = "/access", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AccessResponse modifyAccess(@RequestBody AccessRequest accessRequest) {
        return userService.modifyAccess(accessRequest);
    }
}
