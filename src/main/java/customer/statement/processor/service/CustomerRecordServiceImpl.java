package customer.statement.processor.service;

import java.util.Collections;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import customer.statement.processor.dto.CustomerRecordDTO;
import customer.statement.processor.model.CustomerRecord;
import customer.statement.processor.repository.CustomerRecordRepository;
import customer.statement.processor.response.CustomerRecordValidationResponse;
import customer.statement.processor.response.ErrorRecord;
import customer.statement.processor.response.ValidationResultCode;

@Service
@Transactional
public class CustomerRecordServiceImpl implements CustomerRecordService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerRecordServiceImpl.class);
    private static final String CUSTOMER_RECORD_DUPLICATE_REFERENCE = "Customer Record has duplicate reference and correct balance";
    private static final String CUSTOMER_RECORD_INCORRECT_END_BALANCE = "Customer Record has duplicate reference and Incorrect balance";
    private static final String CUSTOMER_RECORD_DUPLICATE_REFERENCE_INCORRECT_END_BALANCE = "Customer Record has duplicate reference and In correct balance ";
    private static final String CUSTOMER_RECORD_SUCCESSFUL = "Customer Record has no duplicate reference and correct end balance";

    @Autowired
    private CustomerRecordRepository customerRecordRepository;

    /**
     * This method is used to save the Customer Record in database
     */
    @Override
    public void saveCustomerRecord(CustomerRecordDTO customerRecordDTO) {

        CustomerRecord customerRecord = getCustomerRecord(customerRecordDTO);
        customerRecordRepository.save(customerRecord);
    }

    /**
     * This method is used to Validate the customer record.
     */
    @Override
    public CustomerRecordValidationResponse validateRecord(CustomerRecordDTO customerRecordDTO) {

        boolean isReferenceDuplicate = isDuplicate(customerRecordDTO);
        boolean isBalanceCorrect = isValidMutation(customerRecordDTO);

        if (isReferenceDuplicate && isBalanceCorrect) {

            return createCustomerRecordValidationResponse(customerRecordDTO, CUSTOMER_RECORD_DUPLICATE_REFERENCE,
                    ValidationResultCode.DUPLICATE_REFERENCE);

        } else if (!isReferenceDuplicate && !isBalanceCorrect) {

            return createCustomerRecordValidationResponse(customerRecordDTO, CUSTOMER_RECORD_INCORRECT_END_BALANCE,
                    ValidationResultCode.INCORRECT_END_BALANCE);

        } else if (isReferenceDuplicate) {

            return createCustomerRecordValidationResponse(customerRecordDTO,
                    CUSTOMER_RECORD_DUPLICATE_REFERENCE_INCORRECT_END_BALANCE,
                    ValidationResultCode.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE);
        }

        logger.info(CUSTOMER_RECORD_SUCCESSFUL);
        return new CustomerRecordValidationResponse(ValidationResultCode.SUCCESSFUL, Collections.emptyList());
    }

    private boolean isDuplicate(final CustomerRecordDTO customerRecordDTO) {

        return customerRecordRepository.findById(customerRecordDTO.getTransactionReference()).isPresent();
    }

    private boolean isValidMutation(final CustomerRecordDTO customerRecordDTO) {

        return customerRecordDTO.getEndBalance()
                .compareTo(customerRecordDTO.getStartBalance().add(customerRecordDTO.getMutation())) == 0;
    }

    private CustomerRecordValidationResponse createCustomerRecordValidationResponse(
            final CustomerRecordDTO customerRecordDTO, final String loggerMessage,
            final ValidationResultCode validationResultCode) {

        ErrorRecord record = ErrorRecord.buildFrom(customerRecordDTO);
        logger.info(loggerMessage);
        return new CustomerRecordValidationResponse(validationResultCode, Collections.singletonList(record));
    }

    private CustomerRecord getCustomerRecord(CustomerRecordDTO customerRecordDTO) {
        return new ModelMapper().map(customerRecordDTO, CustomerRecord.class);
    }
}
