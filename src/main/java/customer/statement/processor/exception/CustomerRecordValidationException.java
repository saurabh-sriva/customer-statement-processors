package customer.statement.processor.exception;

import java.util.ArrayList;
import java.util.Collection;

import customer.statement.processor.response.ErrorRecord;
import customer.statement.processor.response.ValidationResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerRecordValidationException extends RuntimeException {
    private static final long serialVersionUID = -3099995269290364878L;
    private final ValidationResultCode code;
    private final Collection<ErrorRecord> errorRecords;

    public CustomerRecordValidationException(ValidationResultCode code) {
        this(code, new ArrayList<ErrorRecord>());
    }
}
