package account.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmployeeDTO {
    private String name;
    private String lastname;
    @JsonFormat(pattern = "MMMM-yyyy")
    private YearMonth period;
    private String salary;
}
