package customer.statement.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import customer.statement.processor.request.CustomerRecordValidationRequest;
import customer.statement.processor.response.CustomerRecordValidationResponse;
import customer.statement.processor.response.ValidationResultCode;
import customer.statement.processor.utils.TestFileEnum;
import customer.statement.processor.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CustomerStatementProcessorServiceApplication.class)
class CustomerStatementProcessorServiceApplicationIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    private ResponseEntity<CustomerRecordValidationResponse> resp = null;


    @Test
    void testWhenRequestIsInvalidThenReturnBadRequest() throws Exception {
        HttpEntity<CustomerRecordValidationRequest> entity = createTestRecord(TestFileEnum.TEST_BAD_REQUEST);
        resp = getResponse(entity);
        assertEquals(ValidationResultCode.BAD_REQUEST, resp.getBody().getResult());
    }


    @Test
    void testWhenNoDuplicateReferenceAndCorrectBalance() throws Exception {
        HttpEntity<CustomerRecordValidationRequest> entity = createTestRecord(TestFileEnum.TEST_VALID_REQUEST);
        resp = getResponse(entity);
        assertEquals(ValidationResultCode.SUCCESSFUL, resp.getBody().getResult());
    }

    @Test
    void testWhenDuplicateReferenceAndCorrectBalance() throws Exception {
        HttpEntity<CustomerRecordValidationRequest> entity = createTestRecord(TestFileEnum.TEST_DUPLICATE_REFERENCE_REQUEST);
        resp = getResponse(entity);
        assertEquals(ValidationResultCode.DUPLICATE_REFERENCE, resp.getBody().getResult());

    }

    @Test
    void testWhenDuplicateReferenceAndInCorrectBalance() throws Exception {
        HttpEntity<CustomerRecordValidationRequest> entity = createTestRecord(TestFileEnum.TEST_DUPLICATE_REF_INCORRECT_BALANCE_REQUEST);
        resp = getResponse(entity);
        assertEquals(ValidationResultCode.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE, resp.getBody().getResult());
    }

    @Test
    void testWhenNoDuplicateRefAndInCorrectBalance() throws Exception {
        HttpEntity<CustomerRecordValidationRequest> entity = createTestRecord(TestFileEnum.TEST_INCORRECT_BALANCE_REQUEST);
        resp = getResponse(entity);
        assertEquals(ValidationResultCode.INCORRECT_END_BALANCE, resp.getBody().getResult());
    }

    @Test
    void testWhenRequestIsEmptyThenReturnEmptyRequest() throws Exception {
        HttpEntity<CustomerRecordValidationRequest> entity = createTestRecord(TestFileEnum.TEST_EMPTY_REQUEST);
        resp = getResponse(entity);
        assertEquals(ValidationResultCode.BAD_REQUEST, resp.getBody().getResult());
    }


    private ResponseEntity<CustomerRecordValidationResponse> getResponse(HttpEntity<CustomerRecordValidationRequest> entity) {
        final String END_POINT = "http://localhost:" + port + "/customerRecords";
        ResponseEntity<CustomerRecordValidationResponse> response = restTemplate.exchange(END_POINT, HttpMethod.POST, entity, CustomerRecordValidationResponse.class);
        return response;
    }

    private HttpEntity<CustomerRecordValidationRequest> createTestRecord(TestFileEnum testFile) throws IOException {
        CustomerRecordValidationRequest request = mapper.readValue(TestUtils.readFile(testFile.getFileName()), CustomerRecordValidationRequest.class);
        HttpEntity<CustomerRecordValidationRequest> entity = new HttpEntity<>(request);
        return entity;
    }
}