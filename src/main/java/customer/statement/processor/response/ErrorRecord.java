package customer.statement.processor.response;

import customer.statement.processor.dto.CustomerRecordDTO;
import lombok.Data;

@Data
public final class ErrorRecord {

    private long reference;
    private String accountNumber;


    public static ErrorRecord buildFrom(CustomerRecordDTO record) {
        ErrorRecord rec = new ErrorRecord();
        rec.setAccountNumber(record.getAccountNumber());
        rec.setReference(record.getTransactionReference());
        return rec;
    }
}