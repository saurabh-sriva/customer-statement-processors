package customer.statement.processor.service;

import customer.statement.processor.dto.CustomerRecordDTO;
import customer.statement.processor.response.CustomerRecordValidationResponse;
import customer.statement.processor.response.ValidationResultCode;
import customer.statement.processor.utils.CustomerRecordProcessorUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class CustomerRecordServiceImplTest {


    @InjectMocks
    private CustomerRecordServiceImpl service;

    @Mock
    private CustomerRecordProcessorUtils customerRecordProcessorUtils;


    @Test
    void testWhenThereAreNoDuplicateRefAndCorrectBalanceThenReturnSuccess() {

        CustomerRecordDTO record1 = new CustomerRecordDTO(123L, "Account2", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(195.0));
        CustomerRecordDTO record2 = new CustomerRecordDTO(124L, "Account2", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(195.0));
        List<CustomerRecordDTO> records = new ArrayList<>();
        records.add(record1);
        records.add(record2);
        CustomerRecordValidationResponse resp = service.validateRecords(records);
        assertEquals(ValidationResultCode.SUCCESSFUL, resp.getResult());
    }

    @Test
    void testWhenThereAreDuplicateRefAndCorrectBalanceThenReturnDuplicateRef() {
        CustomerRecordDTO record1 = new CustomerRecordDTO(123L, "Account2", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(195.0));
        CustomerRecordDTO record2 = new CustomerRecordDTO(123L, "Account2", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(195.0));
        List<CustomerRecordDTO> records = new ArrayList<>();
        records.add(record1);
        records.add(record2);
        CustomerRecordValidationResponse resp = service.validateRecords(records);
        assertEquals(ValidationResultCode.DUPLICATE_REFERENCE, resp.getResult());


    }

    @Test
    void testWhenThereAreNoDuplicateRefAndInCorrectBalanceThenReturnIncorrectEndBalance() {
        CustomerRecordDTO record1 = new CustomerRecordDTO(123L, "Account2", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(195.0));
        CustomerRecordDTO record2 = new CustomerRecordDTO(124L, "Account3", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(190.0));
        List<CustomerRecordDTO> records = new ArrayList<>();
        records.add(record1);
        records.add(record2);
        CustomerRecordValidationResponse resp = service.validateRecords(records);
        assertEquals(ValidationResultCode.INCORRECT_END_BALANCE, resp.getResult());

    }

    @Test
    void testWhenThereAreDuplicateRefAndInCorrectBalanceThenReturnDuplicateRefAndIncorrectEndBalance() {
        CustomerRecordDTO record1 = new CustomerRecordDTO(123L, "Account2", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(195.0));
        CustomerRecordDTO record2 = new CustomerRecordDTO(123L, "Account2", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(190.0));
        List<CustomerRecordDTO> records = new ArrayList<>();
        records.add(record1);
        records.add(record2);
        CustomerRecordValidationResponse resp = service.validateRecords(records);
        assertEquals(ValidationResultCode.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE, resp.getResult());
    }

}
