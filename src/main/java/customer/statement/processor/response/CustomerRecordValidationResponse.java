package customer.statement.processor.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
public final class CustomerRecordValidationResponse {

    private ValidationResultCode result;
    private Collection<ErrorRecord> errorRecords;

    public CustomerRecordValidationResponse() {
    }
}
