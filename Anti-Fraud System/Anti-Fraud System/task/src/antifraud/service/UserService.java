package antifraud.service;

import antifraud.dto.Operation;
import antifraud.dto.Role;
import antifraud.dto.request.AccessRequest;
import antifraud.dto.response.AccessResponse;
import antifraud.dto.response.DeleteResponse;
import antifraud.dto.request.RoleRequest;
import antifraud.entity.User;
import antifraud.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private PasswordEncoder encoder;

    private final List<Role> roles = List.of(Role.MERCHANT, Role.ADMINISTRATOR, Role.SUPPORT);

    public User signUp(User user) {
        if (userRepository.existsUserByUsernameIgnoreCase(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        if (userRepository.count() == 0) {
            user.setRole(Role.ADMINISTRATOR);
        } else {
            user.setRole(Role.MERCHANT);
        }

        user.setPassword(encoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository
                .findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAllByOrderByIdAsc();
    }

    public DeleteResponse deleteUser(String username) {
        User user = getUserByUsername(username);
        userRepository.delete(user);

        return new DeleteResponse(username, "Deleted successfully!");
    }

    public User modifyRole(RoleRequest roleRequest) {
        User user = getUserByUsername(roleRequest.getUsername());
        isValidateUpdate(roleRequest, user);

        user.setRole(roleRequest.getRole());
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email not found"));
    }

    private void isValidateUpdate(RoleRequest roleRequest, User user) {
        if (!roles.contains(roleRequest.getRole()) || roleRequest.getRole() == Role.ADMINISTRATOR) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else if (user.getRole() == roleRequest.getRole()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    public AccessResponse modifyAccess(AccessRequest accessRequest) {
        User user = getUserByUsername(accessRequest.getUsername());
        if (user.getRole() == Role.ADMINISTRATOR) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (accessRequest.getOperation() == Operation.LOCK) {
            user.setAccountNonLocked(false);
            return new AccessResponse(String.format("User %s locked!", accessRequest.getUsername()));
        } else {
            user.setAccountNonLocked(true);
            return new AccessResponse(String.format("User %s unlocked!", accessRequest.getUsername()));
        }
    }
}
