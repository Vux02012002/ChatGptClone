package account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserUpdatedRoleResponse {
    private String user;
    private String role;
    private String operation;
}
