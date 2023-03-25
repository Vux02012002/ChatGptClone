package dartsgame.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@AllArgsConstructor
//@NoArgsConstructor
@Data
public class StatusResponse {
    private String status;

    public StatusResponse(String status) {
        this.status = status;
    }
}
