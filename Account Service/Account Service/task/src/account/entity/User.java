package account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;
import org.hibernate.annotations.SortNatural;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    @JsonProperty("lastname")
    private String lastName;

    @Email(message = "Email is not valid", regexp = "^[A-z0-9._%+-]+(@acme.com)$")
    @NotEmpty
    @JsonProperty("email")
    @Column(name = "email", nullable = false, unique = true)
    private String username;

    @NotEmpty
    @Size(min = 12, message = "The password length must be at least 12 chars!")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @OneToMany
    @JoinColumn(name = "user_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Payment> payments = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "user_groups",
            joinColumns =@JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"
            ))
    private Set<Group> userGroups= new HashSet<>();

    @JsonProperty(value = "roles", access = JsonProperty.Access.READ_ONLY)
    public List<String> getRoles() {
        Set<Group> userGroups = this.getUserGroups();
        List<String> authorities = new ArrayList<>(userGroups.size());
        userGroups
                .stream()
                .sorted()
                .forEach(userGroup -> authorities.add(userGroup.getCode().toUpperCase()));
        return authorities;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Group> userGroups = this.getUserGroups();
        Collection<GrantedAuthority> authorities = new ArrayList<>(userGroups.size());

        for(Group userGroup : userGroups){
            authorities.add(new SimpleGrantedAuthority(userGroup.getCode().toUpperCase()));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
