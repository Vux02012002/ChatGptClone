package antifraud.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transaction {
    @Min(1)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private long amount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ResultValue result;

    public void calculateResult() {
        if (amount <= 200) {
            result = ResultValue.ALLOWED;
        } else if (amount <= 1500) {
            result = ResultValue.MANUAL_PROCESSING;
        } else {
            result = ResultValue.PROHIBITED;
        }
    }
}
