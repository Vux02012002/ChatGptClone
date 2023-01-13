package account.controller;

import account.entity.User;
import account.model.PasswordDTO;
import account.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User signUp(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PostMapping(value = "/changepass", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changePassword(
            @RequestBody @Valid PasswordDTO passwordDTO,
            @AuthenticationPrincipal User user
    ) {
        String newPassword = passwordDTO.getNew_password();
        return userService.changePassword(newPassword, user);
    }
}
