package customer.statement.processor.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import customer.statement.processor.dto.CustomerRecordDTO;
import customer.statement.processor.request.CustomerRecordValidationRequest;
import customer.statement.processor.response.BatchCustomerRecordValidationResponse;
import customer.statement.processor.response.CustomerRecordValidationResponse;
import customer.statement.processor.response.ValidationResultCode;
import customer.statement.processor.service.CustomerRecordService;

@RestController
public class CustomerStatementResource {

    @Autowired
    private CustomerRecordService customerRecordService;

    @PostMapping(value = "/customerRecords", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> saveCustomerRecord(@RequestBody @Valid CustomerRecordValidationRequest request) {

        List<CustomerRecordValidationResponse> respList = new ArrayList<CustomerRecordValidationResponse>();
        List<CustomerRecordDTO> customerRecord = request.getRecords();

        for (CustomerRecordDTO record : customerRecord) {

            CustomerRecordValidationResponse response = customerRecordService.validateRecord(record);

            if (response.getResult() == ValidationResultCode.SUCCESSFUL) {
                customerRecordService.saveCustomerRecord(record);
            }

            respList.add(response);
        }

        return new ResponseEntity<Object>(new BatchCustomerRecordValidationResponse(respList), HttpStatus.OK);
    }
}