package customer.statement.processor.exception;

import customer.statement.processor.response.ErrorRecord;
import customer.statement.processor.response.ValidationResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CustomerRecordValidationException extends RuntimeException {
    private static final long serialVersionUID = -3099995269290364878L;
    private final ValidationResultCode code;
    private final transient List<ErrorRecord> errorRecords;
}
