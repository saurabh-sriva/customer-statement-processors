package customer.statement.processor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import customer.statement.processor.model.CustomerRecord;

@Repository
public interface CustomerRecordRepository extends JpaRepository<CustomerRecord, Long> {

}
