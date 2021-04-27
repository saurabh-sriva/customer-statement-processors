package customer.statement.processor.utils;

import customer.statement.processor.dto.CustomerRecordDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CustomerStatementUtils {

    public Set<CustomerRecordDTO> getCustomerStatementDuplicatesRecords(final List<CustomerRecordDTO> customerStatements) {
        return customerStatements.stream()
                .collect(Collectors.groupingBy(CustomerRecordDTO::getTransactionReference)).entrySet().stream()
                .filter(statement -> statement.getValue().size() > 1).flatMap(d -> d.getValue().stream())
                .collect(Collectors.toSet());
    }

    public Set<CustomerRecordDTO> getIncorrectBalanceRecords(final List<CustomerRecordDTO> customerStatements) {

        return customerStatements.stream().map(this::validateBalance).filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private CustomerRecordDTO validateBalance(final CustomerRecordDTO customerRecordDTO) {
        if (customerRecordDTO.getEndBalance()
                .compareTo(customerRecordDTO.getStartBalance().add(customerRecordDTO.getMutation())) == 0) {
            return null;
        }
        return customerRecordDTO;
    }

}
