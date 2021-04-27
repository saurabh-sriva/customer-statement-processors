package customer.statement.processor.request;

import customer.statement.processor.dto.CustomerRecordDTO;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class CustomerRecordValidationRequest {
    @Valid
    private List<CustomerRecordDTO> records;
}
