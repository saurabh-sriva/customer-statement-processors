package customer.statement.processor.service;

import customer.statement.processor.dto.CustomerRecordDTO;
import customer.statement.processor.response.CustomerRecordValidationResponse;

import java.util.List;

public interface CustomerRecordService {

    public CustomerRecordValidationResponse validateRecords(List<CustomerRecordDTO> records);
}
