package customer.statement.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import customer.statement.processor.controller.CustomerStatementResource;
import customer.statement.processor.dto.CustomerRecordDTO;
import customer.statement.processor.model.CustomerRecord;
import customer.statement.processor.repository.CustomerRecordRepository;
import customer.statement.processor.request.CustomerRecordValidationRequest;
import customer.statement.processor.response.BatchCustomerRecordValidationResponse;
import customer.statement.processor.response.CustomerRecordValidationResponse;
import customer.statement.processor.response.ValidationResultCode;
import customer.statement.processor.service.CustomerRecordServiceImpl;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CustomerStatementProcessorServiceApplicationTests.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration
@ContextConfiguration(classes = {CustomerStatementResource.class, CustomerRecordServiceImpl.class})
class CustomerStatementProcessorServiceApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(CustomerStatementProcessorServiceApplicationTests.class);
    private static final String EXCEPTION_MESSAGE_IN_METHOD_ASJSONSTRING = "Exception occured in method asJsonString";
    private static final String EXCEPTION_MESSAGE_IN_METHOD_ASOBJECT = "Exception occured in method asObject";
    private static final String DB_CLEANUP = "DB Cleanup";

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    private BatchCustomerRecordValidationResponse resp = null;

    @Autowired
    private CustomerRecordRepository repo;

    @AfterEach
    void dbClean() {
        logger.info(DB_CLEANUP);
        repo.deleteAll();
    }

    private void assertAllInOrder(BatchCustomerRecordValidationResponse resp, ValidationResultCode... codes) {
        assertTrue(resp.getResponses().size() == codes.length);
        for (int index = 0; index < codes.length; index++) {
            CustomerRecordValidationResponse r = resp.getResponses().get(index);
            assertTrue(r.getResult() == codes[index]);
        }
    }

    @Test
    void testWhenRequestIsInvalidThenReturnBadRequest() throws Exception {

        int responseStatus = getBadRequestResponseStatus(Arrays.asList(createTestRecord()));

        assertEquals(HttpStatus.BAD_REQUEST.value(), responseStatus);

    }

    @Test
    @Rollback(false)
    void testSaveCustomerRecords() throws Exception {
        CustomerRecordDTO record = createTestRecord().withEndBalance(new BigDecimal(195.0));
        CustomerRecord customerRecord = new ModelMapper().map(record, CustomerRecord.class);
        CustomerRecord saveCustomerRecord = repo.save(customerRecord);
        assertNotNull(saveCustomerRecord);
    }

    @Test
    void testWhenNoDuplicateReferenceAndCorrectBalance() throws Exception {
        CustomerRecordDTO record = createTestRecord().withEndBalance(new BigDecimal(195.0));
        resp = getResponse(Arrays.asList(record));
        assertAllInOrder(resp, ValidationResultCode.SUCCESSFUL);
    }

    @Test
    void testWhenDuplicateReferenceAndCorrectBalance() throws Exception {
        CustomerRecordDTO record = createTestRecord().withEndBalance(new BigDecimal(195.0));
        CustomerRecordDTO duplicateRecord = createTestRecord().withEndBalance(new BigDecimal(195.0));
        resp = getResponse(Arrays.asList(record, duplicateRecord));

        assertAllInOrder(resp, ValidationResultCode.SUCCESSFUL, ValidationResultCode.DUPLICATE_REFERENCE);
    }

    @Test
    void testWhenDuplicateReferenceAndInCorrectBalance() throws Exception {
        CustomerRecordDTO record = createTestRecord().withEndBalance(new BigDecimal(195.0));
        CustomerRecordDTO recordWithDupRefAndIncorrectBalance = createTestRecord()
                .withEndBalance(new BigDecimal(200.0));
        resp = getResponse(Arrays.asList(record, recordWithDupRefAndIncorrectBalance));
        assertAllInOrder(resp, ValidationResultCode.SUCCESSFUL,
                ValidationResultCode.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE);
    }

    @Test
    void testWhenNoDuplicateRefAndInCorrectBalance() throws Exception {
        CustomerRecordDTO recordWithUniqueRefAIncorrectBalance = createTestRecord()
                .withEndBalance(new BigDecimal(200.0));
        resp = getResponse(Arrays.asList(recordWithUniqueRefAIncorrectBalance));
        assertAllInOrder(resp, ValidationResultCode.INCORRECT_END_BALANCE);
    }

    private BatchCustomerRecordValidationResponse getResponse(List<CustomerRecordDTO> records) throws Exception {
        final String END_POINT = "http://localhost:" + port + "/customerRecords";
        CustomerRecordValidationRequest req = new CustomerRecordValidationRequest();
        req.setRecords(records);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(END_POINT).content(asJsonString(req))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();
        String jsonString = result.getResponse().getContentAsString();
        return asObject(jsonString, BatchCustomerRecordValidationResponse.class);

    }

    private String asJsonString(final Object obj) {

        try {

            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;

        } catch (Exception e) {
            logger.error(EXCEPTION_MESSAGE_IN_METHOD_ASJSONSTRING, e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private BatchCustomerRecordValidationResponse asObject(final String jsonString,
                                                           Class<BatchCustomerRecordValidationResponse> targetClass) {

        try {

            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonString, targetClass);

        } catch (Exception e) {
            logger.error(EXCEPTION_MESSAGE_IN_METHOD_ASOBJECT, e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private int getBadRequestResponseStatus(List<CustomerRecordDTO> records) throws Exception {

        final String END_POINT = "http://localhost:" + port + "/customerRecords";
        CustomerRecordValidationRequest req = new CustomerRecordValidationRequest();
        req.setRecords(records);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(END_POINT).content(asJsonString(req))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andReturn();

        return result.getResponse().getStatus();

    }

    private CustomerRecordDTO createTestRecord() {
        return new CustomerRecordDTO(123L, "Account2", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", null);
    }

}