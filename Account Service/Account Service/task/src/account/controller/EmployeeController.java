package account.controller;

import account.entity.User;
import account.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.Calendar;

@RestController
@RequestMapping("/api/empl")
@Validated
public class EmployeeController {
    PaymentService paymentService;

    @Autowired
    public EmployeeController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ACCOUNTANT')")
    @GetMapping(value = "/payment", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getPayment(@RequestParam(required = false) @DateTimeFormat(pattern = "MM-yyyy") Calendar period, @AuthenticationPrincipal User user) {
        if (period == null) {
            return paymentService.getAllPayments(user);
        }
        return paymentService.getPayment(calendarToYearMonth(period), user);
    }

    private YearMonth calendarToYearMonth(Calendar calendar) {
        return YearMonth.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
    }
}
