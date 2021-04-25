package customer.statement.processor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import customer.statement.processor.dto.CustomerRecordDTO;
import customer.statement.processor.model.CustomerRecord;
import customer.statement.processor.repository.CustomerRecordRepository;
import customer.statement.processor.response.CustomerRecordValidationResponse;
import customer.statement.processor.response.ValidationResultCode;

@ExtendWith(MockitoExtension.class)
class CustomerRecordServiceImplTest {

    @Mock
    private CustomerRecordRepository repo;

    @InjectMocks
    private CustomerRecordServiceImpl service;

    @Test
    void testWhenThereAreNoDuplicateRefAndCorrectBalanceThenReturnSuccess() {
        when(repo.findById(123L)).thenReturn(Optional.empty());
        CustomerRecordDTO record = new CustomerRecordDTO(123L, "Account2", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(195.0));
        CustomerRecordValidationResponse resp = service.validateRecord(record);
        assertEquals(ValidationResultCode.SUCCESSFUL, resp.getResult());
    }

    @Test
    void testWhenThereAreDuplicateRefAndCorrectBalanceThenReturnDuplicateRef() {
        CustomerRecord existingRecord = new CustomerRecord(12L, "Account2", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(195.0));
        when(repo.findById(12L)).thenReturn(Optional.of(existingRecord));
        CustomerRecordDTO record = new CustomerRecordDTO(12L, "Account2", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(195.0));

        CustomerRecordValidationResponse resp = service.validateRecord(record);
        assertEquals(ValidationResultCode.DUPLICATE_REFERENCE, resp.getResult());
    }

    @Test
    void testWhenThereAreNoDuplicateRefAndInCorrectBalanceThenReturnIncorrectEndBalance() {
        when(repo.findById(2L)).thenReturn(Optional.empty());
        CustomerRecordDTO record = new CustomerRecordDTO(2L, "Account2", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(190.0));

        CustomerRecordValidationResponse resp = service.validateRecord(record);
        assertEquals(ValidationResultCode.INCORRECT_END_BALANCE, resp.getResult());
    }

    @Test
    void testWhenThereAreDuplicateRefAndInCorrectBalanceThenReturnDuplicateRefAndIncorrectEndBalance() {
        CustomerRecord existingRecord = new CustomerRecord(4L, "Account2", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(195.0));
        when(repo.findById(4L)).thenReturn(Optional.of(existingRecord));
        CustomerRecordDTO record = new CustomerRecordDTO(4L, "Account2", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(190.0));
        CustomerRecordValidationResponse resp = service.validateRecord(record);
        assertEquals(ValidationResultCode.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE, resp.getResult());
    }

}
