package customer.statement.processor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRecordDTO {

    @NotNull
    private Long transactionReference;

    @NotNull
    @Size(max = 50)
    private String accountNumber;

    @NotNull
    private BigDecimal startBalance;

    @NotNull
    private BigDecimal mutation;

    @Size(max = 250)
    private String description;

    @NotNull
    private BigDecimal endBalance;

}
