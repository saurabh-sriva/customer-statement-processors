package customer.statement.processor.service;

import customer.statement.processor.dto.CustomerRecordDTO;
import customer.statement.processor.response.CustomerRecordValidationResponse;

public interface CustomerRecordService {

    public void saveCustomerRecord(CustomerRecordDTO customerRecord);

    public CustomerRecordValidationResponse validateRecord(CustomerRecordDTO record);
}
