package customer.statement.processor.utils;


import customer.statement.processor.dto.CustomerRecordDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class CustomerRecordProcessorUtilsTest {

    @Test
    void testWhenCustomerStatementHasDuplicatesRecords() {
        CustomerRecordDTO record1 = new CustomerRecordDTO(123L, "Account2", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(195.0));
        CustomerRecordDTO record2 = new CustomerRecordDTO(123L, "Account2", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(195.0));
        List<CustomerRecordDTO> records = new ArrayList<>();
        records.add(record1);
        records.add(record2);

        Set<CustomerRecordDTO> customerStmtDuplicateRec = CustomerRecordProcessorUtils.getCustomerStatementDuplicatesRecords(records);
        assertTrue(!CollectionUtils.isEmpty(customerStmtDuplicateRec));
    }

    @Test
    void testWhenCustomerStatementHasNoDuplicatesRecords() {
        CustomerRecordDTO record1 = new CustomerRecordDTO(123L, "Account2", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(195.0));
        CustomerRecordDTO record2 = new CustomerRecordDTO(124L, "Account2", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(195.0));
        List<CustomerRecordDTO> records = new ArrayList<>();
        records.add(record1);
        records.add(record2);

        Set<CustomerRecordDTO> customerStmtDuplicateRec = CustomerRecordProcessorUtils.getCustomerStatementDuplicatesRecords(records);
        assertTrue(CollectionUtils.isEmpty(customerStmtDuplicateRec));
    }

    @Test
    void testWhenCustomerStatementHasMoreDuplicatesRecords() {
        CustomerRecordDTO record1 = new CustomerRecordDTO(123L, "Account2", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(195.0));
        CustomerRecordDTO record2 = new CustomerRecordDTO(123L, "Account2", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(195.0));
        CustomerRecordDTO record3 = new CustomerRecordDTO(123L, "Account2", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(195.0));
        List<CustomerRecordDTO> records = new ArrayList<>();
        records.add(record1);
        records.add(record2);
        records.add(record3);

        Set<CustomerRecordDTO> customerStmtDuplicateRec = CustomerRecordProcessorUtils.getCustomerStatementDuplicatesRecords(records);
        assertEquals(1, customerStmtDuplicateRec.size());
    }


    @Test
    void testWhenCustomerStatementHasIncorrectBalanceRecords() {
        CustomerRecordDTO record1 = new CustomerRecordDTO(123L, "Account2", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(195.0));
        CustomerRecordDTO record2 = new CustomerRecordDTO(124L, "Account3", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(190.0));
        List<CustomerRecordDTO> records = new ArrayList<>();
        records.add(record1);
        records.add(record2);

        Set<CustomerRecordDTO> incorrectBalanceRecords = CustomerRecordProcessorUtils.getIncorrectBalanceRecords(records);
        assertTrue(!CollectionUtils.isEmpty(incorrectBalanceRecords));

    }

    @Test
    void testWhenCustomerStatementHasCorrectBalanceRecords() {
        CustomerRecordDTO record1 = new CustomerRecordDTO(123L, "Account2", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(195.0));
        CustomerRecordDTO record2 = new CustomerRecordDTO(124L, "Account3", new BigDecimal(200.0), new BigDecimal(-5.0),
                "Sample description", new BigDecimal(195.0));
        List<CustomerRecordDTO> records = new ArrayList<>();
        records.add(record1);
        records.add(record2);

        Set<CustomerRecordDTO> incorrectBalanceRecords = CustomerRecordProcessorUtils.getIncorrectBalanceRecords(records);
        assertTrue(CollectionUtils.isEmpty(incorrectBalanceRecords));

    }


}
