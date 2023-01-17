package account.service;

import account.entity.Group;
import account.entity.User;
import account.model.Operation;
import account.model.SuccessStatus;
import account.model.UserUpdatedRoleResponse;
import account.repository.GroupRepository;
import account.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
    private static int count = 1;
    UserRepository userRepository;
    PasswordEncoder encoder;
    GroupRepository groupRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.groupRepository = groupRepository;
    }

    public User addUser(User user) {
        if (isPasswordInHackerDatabase(user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is in the hacker's database!");
        }
        if (userRepository.existsUserByUsernameIgnoreCase(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exist!");
        }

        Group group;
        if (count == 1) {
            group = groupRepository
                    .findByCodeContainsIgnoreCase("ROLE_ADMINISTRATOR")
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        } else {
            group = groupRepository
                    .findByCodeContainsIgnoreCase("ROLE_USER")
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        }

        user.getUserGroups().add(group);
        user.setUsername(user.getUsername().toLowerCase());
        user.setPassword(encoder.encode(user.getPassword()));
        count++;

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
    }

    public boolean isPasswordInHackerDatabase(String password) {
        List<String> hackerPasswords = List.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
                "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
                "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");
        return hackerPasswords.contains(password);
    }

    public ResponseEntity<String> changePassword(String newPassword, User user) {

        if (isPasswordInHackerDatabase(newPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password is in the hacker's database!");
        } else if (encoder.matches(newPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The passwords must be different!");
        }

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);

        String body = String.format("""
                {
                  "email": "%s",
                  "status": "The password has been updated successfully"
                }""", user.getUsername());

        return ResponseEntity.ok(body);
    }

    public List<User> getUsers() {
        return userRepository.findAllByOrderByIdAsc();
    }

    public SuccessStatus deleteUser(String email) {
        User user = getUserByUsername(email);

        if (containsAdministratorRole(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
        }

        userRepository.delete(user);
        return new SuccessStatus(email, "Deleted successfully!");
    }

    public User updateGroup(UserUpdatedRoleResponse userUpdatedRole) {
        User user = getUserByUsername(userUpdatedRole.getUser());
        Group group = groupRepository
                .findByCodeContainsIgnoreCase(userUpdatedRole.getRole())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!"));

        isValidUpdate(userUpdatedRole, user, group);

        if (userUpdatedRole.getOperation().equals(Operation.GRANT)) {
            group.setCode("ROLE_" + userUpdatedRole.getRole());
            user.getUserGroups().add(group);
        } else if (userUpdatedRole.getOperation().equals(Operation.REMOVE)) {
            user.getUserGroups().remove(group);
        }

        return userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository
                .findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
    }

    private boolean containsAdministratorRole(User user) {
        return user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR"));
    }

    private void isValidUpdate(UserUpdatedRoleResponse userUpdatedRole, User user, Group group) {
        if (userUpdatedRole.getOperation().equals(Operation.GRANT)) {
            if (userUpdatedRole.getRole().contains("ADMINISTRATOR") || containsAdministratorRole(user)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user cannot combine administrative and business roles!");
            }
        } else if (userUpdatedRole.getOperation().equals(Operation.REMOVE)) {
            if (!user.getUserGroups().contains(group)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not have a role!");
            } else if (userUpdatedRole.getRole().equalsIgnoreCase("ADMINISTRATOR")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't remove ADMINISTRATOR role!");
            } else if (user.getAuthorities().size() == 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user must have at least one role!");
            }
        }
    }
}
