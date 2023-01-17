package account.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "groups")
public class Group implements Comparable<Group> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    private String name;

    @ManyToMany(mappedBy = "userGroups")
    private Set<User> users;

    public Group(String code) {
        this.code = code;
    }

    @Override
    public int compareTo(Group o) {
        return this.getCode().compareTo(o.getCode());
    }
}
