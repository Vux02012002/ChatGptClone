package account.service;

import account.entity.User;
import account.model.SuccessStatus;
import account.model.UserUpdatedRoleResponse;
import account.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    UserRepository userRepository;
    PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public User addUser(User user) {
        if (isPasswordInHackerDatabase(user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is in the hacker's database!");
        }
        if (userRepository.existsUserByUsernameIgnoreCase(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exist!");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public boolean isPasswordInHackerDatabase(String password) {
        List<String> hackerPasswords = List.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
                "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
                "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");
        return hackerPasswords.contains(password);
    }

    public ResponseEntity<SuccessStatus> changePassword(String newPassword, User user) {

        if (isPasswordInHackerDatabase(newPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is in the hacker's database!");
        } else if (encoder.matches(newPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The passwords must be different!");
        }

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok(new SuccessStatus(user.getUsername().toLowerCase(), "Updated successfully!"));
    }

    public List<User> getUsers() {
        return userRepository.findAllByOrderByIdAsc();
    }

    public SuccessStatus deleteUser(String email) {
        User user = userRepository
                .findUserByUsernameIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ADMINISTRATOR"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
        }

        userRepository.delete(user);
        return new SuccessStatus(email, "Deleted successfully!");
    }


    public User updateRole(UserUpdatedRoleResponse userUpdatedRole) {
        User user = userRepository
                .findUserByUsernameIgnoreCase(userUpdatedRole.getUser())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (userUpdatedRole.getOperation().equals("GRANT")) {
            user.setAuthorities(userUpdatedRole.getRole());
        } else if (userUpdatedRole.getOperation().equals("REMOVE")) {
            if (!user.getAuthorities().contains(new SimpleGrantedAuthority(userUpdatedRole.getRole()))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not have a role!");
            }

            user.getAuthorities().remove(new SimpleGrantedAuthority(userUpdatedRole.getRole()));
        }

        return userRepository.save(user);
    }
}
