package customer.statement.processor.request;

import java.util.List;

import javax.validation.Valid;

import customer.statement.processor.dto.CustomerRecordDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class CustomerRecordValidationRequest {
    @Valid
    private List<CustomerRecordDTO> records;
}
