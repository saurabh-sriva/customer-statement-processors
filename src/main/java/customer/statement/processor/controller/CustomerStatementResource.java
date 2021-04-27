package customer.statement.processor.controller;

import customer.statement.processor.dto.CustomerRecordDTO;
import customer.statement.processor.request.CustomerRecordValidationRequest;
import customer.statement.processor.response.CustomerRecordValidationResponse;
import customer.statement.processor.service.CustomerRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CustomerStatementResource {

    @Autowired
    private CustomerRecordService customerRecordService;

    @PostMapping(value = "/customerRecords", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerRecordValidationResponse> validateCustomerRecords(@RequestBody @Valid CustomerRecordValidationRequest request) {

        List<CustomerRecordDTO> customerRecord = request.getRecords();
        CustomerRecordValidationResponse response = customerRecordService.validateRecords(customerRecord);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}