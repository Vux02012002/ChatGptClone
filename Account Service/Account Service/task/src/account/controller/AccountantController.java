package account.controller;

import account.model.ErrorResponse;
import account.entity.Payment;
import account.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/acct")
@Validated
public class AccountantController {
    PaymentService paymentService;

    @Autowired
    public AccountantController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(value = "/payments", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ErrorResponse addPayments(@RequestBody List<@Valid Payment> payments) {
        return paymentService.addPayments(payments);
    }

    @PutMapping(value = "/payments", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ErrorResponse updatePayments(@RequestBody @Valid Payment payment) {
        return paymentService.updatePayments(payment);
    }
}
