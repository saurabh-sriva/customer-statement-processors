package customer.statement.processor.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@Table(name = "customer_record", schema = "customer_statement_schema")
@Entity
public class CustomerRecord {
    @Id
    @Column(name = "transaction_reference", unique = true, nullable = false)
    Long transactionReference;

    @Column(name = "account_number", nullable = false, length = 50)
    String accountNumber;

    @Column(name = "start_balance", nullable = false)
    BigDecimal startBalance;

    @Column(name = "mutation", nullable = false)
    BigDecimal mutation;

    @Column(name = "description", nullable = true, length = 250)
    String description;

    @Column(name = "end_balance", nullable = false)
    BigDecimal endBalance;

    public CustomerRecord() {

    }
}