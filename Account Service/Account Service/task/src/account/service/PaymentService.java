package account.service;

import account.model.EmployeeDTO;
import account.model.ErrorResponse;
import account.entity.Payment;
import account.entity.User;
import account.repository.PaymentRepository;
import account.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    PaymentRepository paymentRepository;
    UserRepository userRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ErrorResponse addPayments(List<Payment> payments) {
        for (Payment payment : payments) {
            if (!userRepository.existsUserByUsernameIgnoreCase(payment.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee with specified email not found");
            }

            if (paymentRepository.existsByEmailIgnoreCaseAndPeriod(payment.getEmail(), payment.getPeriod())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Record already created");
            }

            userRepository
                    .findUserByUsernameIgnoreCase(payment.getEmail())
                    .map(user -> {
                        user.getPayments().add(payment);
                        return paymentRepository.save(payment);
                    })
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        }

        return new ErrorResponse("Added successfully!");
    }

    @Transactional
    public ErrorResponse updatePayments(Payment newPayment) {
            Payment oldPayment = paymentRepository
                    .findByEmailIgnoreCaseAndPeriod(newPayment.getEmail(), newPayment.getPeriod())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Record with the specified period not found"));

            oldPayment.setSalary(newPayment.getSalary());
            paymentRepository.save(oldPayment);

            return new ErrorResponse("Updated successfully!");
    }

    public List<EmployeeDTO> getAllPayments(User user) {
        List<Payment> payments = paymentRepository.findAllByEmailIgnoreCaseOrderByPeriodDesc(user.getUsername());

        return payments.stream()
                .map(payment -> EmployeeDTO
                        .builder()
                        .name(user.getName())
                        .lastname(user.getLastName())
                        .period(payment.getPeriod())
                        .salary(String.format("%d dollar(s) %d cent(s)", payment.getSalary() / 100, payment.getSalary() % 100))
                        .build()
                )
                .collect(Collectors.toList());
    }

    public EmployeeDTO getPayment(YearMonth period, User user) {
        Payment payment = paymentRepository.findByEmailIgnoreCaseAndPeriod(user.getUsername(), period)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Record with the specified period not found"));

        return EmployeeDTO
                .builder()
                .name(user.getName())
                .lastname(user.getLastName())
                .period(payment.getPeriod())
                .salary(String.format("%d dollar(s) %d cent(s)", payment.getSalary() / 100, payment.getSalary() % 100))
                .build();
    }
}
