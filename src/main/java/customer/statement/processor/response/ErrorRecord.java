package customer.statement.processor.response;

import lombok.Data;

@Data
public final class ErrorRecord {

    private long reference;
    private String accountNumber;
}