package customer.statement.processor.service;

import customer.statement.processor.dto.CustomerRecordDTO;
import customer.statement.processor.exception.CustomerRecordValidationException;
import customer.statement.processor.response.CustomerRecordValidationResponse;
import customer.statement.processor.response.ErrorRecord;
import customer.statement.processor.response.ValidationResultCode;
import customer.statement.processor.utils.CustomerStatementUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class CustomerRecordServiceImpl implements CustomerRecordService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerRecordServiceImpl.class);
    private static final String CUSTOMER_RECORD_DUPLICATE_REFERENCE = "Customer Record has duplicate reference and correct balance";
    private static final String CUSTOMER_RECORD_INCORRECT_END_BALANCE = "Customer Record has duplicate reference and Incorrect balance";
    private static final String CUSTOMER_RECORD_DUPLICATE_REFERENCE_INCORRECT_END_BALANCE = "Customer Record has duplicate reference and In correct balance ";

    @Autowired
    private CustomerStatementUtils customerStatementUtils;

    /**
     * This method is used to Validate the customer record.
     */
    @Override
    public CustomerRecordValidationResponse validateRecords(final List<CustomerRecordDTO> records) {

        if (CollectionUtils.isEmpty(records)) {
            throw new CustomerRecordValidationException(ValidationResultCode.BAD_REQUEST, new ArrayList<>());
        }
        final Set<CustomerRecordDTO> customerStmtDuplicateRec = customerStatementUtils.getCustomerStatementDuplicatesRecords(records);
        final Set<CustomerRecordDTO> incorrectBalanceRecords = customerStatementUtils.getIncorrectBalanceRecords(records);

        if (CollectionUtils.isEmpty(customerStmtDuplicateRec) && CollectionUtils.isEmpty(incorrectBalanceRecords)) {

            return new CustomerRecordValidationResponse(ValidationResultCode.SUCCESSFUL, Collections.emptyList());

        } else if (!CollectionUtils.isEmpty(customerStmtDuplicateRec) && !CollectionUtils.isEmpty(incorrectBalanceRecords)) {

            final List<ErrorRecord> errorRecords = createErrorRecords(customerStmtDuplicateRec);
            errorRecords.addAll(createErrorRecords(incorrectBalanceRecords));
            return createCustomerRecordValidationResponse(errorRecords, CUSTOMER_RECORD_DUPLICATE_REFERENCE_INCORRECT_END_BALANCE, ValidationResultCode.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE);

        } else if (!CollectionUtils.isEmpty(customerStmtDuplicateRec)) {

            final List<ErrorRecord> errorRecords = createErrorRecords(customerStmtDuplicateRec);
            return createCustomerRecordValidationResponse(errorRecords, CUSTOMER_RECORD_DUPLICATE_REFERENCE, ValidationResultCode.DUPLICATE_REFERENCE);

        } else { // handle incorrectBalanceRecords in this last branch.

            final List<ErrorRecord> errorRecords = createErrorRecords(incorrectBalanceRecords);
            return createCustomerRecordValidationResponse(errorRecords, CUSTOMER_RECORD_INCORRECT_END_BALANCE, ValidationResultCode.INCORRECT_END_BALANCE);
        }


    }


    private List<ErrorRecord> createErrorRecords(Set<CustomerRecordDTO> records) {

        final List<ErrorRecord> errorRecords = new ArrayList<>();
        ErrorRecord errorRecord = new ErrorRecord();

        for (final CustomerRecordDTO rec : records) {
            errorRecord.setAccountNumber(rec.getAccountNumber());
            errorRecord.setReference(rec.getTransactionReference());
            errorRecords.add(errorRecord);
        }

        return errorRecords;
    }

    private CustomerRecordValidationResponse createCustomerRecordValidationResponse(
            final List<ErrorRecord> records, final String loggerMessage,
            final ValidationResultCode validationResultCode) {
        logger.info(loggerMessage);
        return new CustomerRecordValidationResponse(validationResultCode, records);
    }

}
