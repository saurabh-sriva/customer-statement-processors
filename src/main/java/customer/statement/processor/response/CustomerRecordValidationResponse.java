package customer.statement.processor.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public final class CustomerRecordValidationResponse {

    private ValidationResultCode result;
    private List<ErrorRecord> errorRecords;
}
