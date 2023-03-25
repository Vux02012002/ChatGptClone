package antifraud.service;

import antifraud.dto.Transaction;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    public Transaction checkAmount(Transaction transaction) {
        transaction.calculateResult();
        return transaction;
    }
}
