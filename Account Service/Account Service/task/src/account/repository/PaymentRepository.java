package account.repository;

import account.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByEmailIgnoreCaseAndPeriod(String employee, YearMonth period);

    Optional<Payment> findByEmailIgnoreCaseAndPeriod(String email, YearMonth period);

    List<Payment> findAllByEmailIgnoreCaseOrderByPeriodDesc(String email);
}
