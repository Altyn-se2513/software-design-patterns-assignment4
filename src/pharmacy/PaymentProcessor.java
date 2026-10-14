package pharmacy.subsystem;

import pharmacy.exception.PharmacyException;
import pharmacy.model.PaymentMethod;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentProcessor {

    public String charge(PaymentMethod method, BigDecimal amount) {
        if (method == null) {
            throw new PharmacyException("Payment method not specified");
        }
        if (amount == null || amount.signum() <= 0) {
            throw new PharmacyException("Amount must be greater than 0");
        }
        String transactionId = "PAY-" + UUID.randomUUID().toString().substring(0, 8);
        System.out.println("Payment " + amount + " (" + method + "), transaction " + transactionId);
        return transactionId;
    }

    public void refund(String transactionId) {
        System.out.println("Refund for transaction " + transactionId);
    }
}
