package account.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.YearMonth;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank
    @Email(message = "Email is not valid", regexp = "^[A-z0-9._%+-]+(@acme.com)$")
    @JsonProperty("employee")
    private String email;

    @JsonFormat(pattern = "MM-yyyy")
    private YearMonth period;

    @Min(0)
    private Long salary;

}
