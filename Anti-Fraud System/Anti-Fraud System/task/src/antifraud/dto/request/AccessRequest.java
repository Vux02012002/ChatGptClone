package antifraud.dto.request;

import antifraud.dto.Operation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccessRequest {
    private String username;
    private Operation operation;
}
