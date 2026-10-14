package pharmacy.facade;

import pharmacy.exception.PharmacyException;
import pharmacy.model.OrderResult;
import pharmacy.model.PaymentMethod;
import pharmacy.model.Prescription;
import pharmacy.subsystem.InventoryService;
import pharmacy.subsystem.LabelPrinter;
import pharmacy.subsystem.NotificationService;
import pharmacy.subsystem.PaymentProcessor;
import pharmacy.subsystem.PrescriptionValidator;
import pharmacy.subsystem.PricingService;

import java.math.BigDecimal;
import java.util.UUID;

public class PharmacyFacade {

    private final PrescriptionValidator validator;
    private final InventoryService inventory;
    private final PricingService pricing;
    private final PaymentProcessor payment;
    private final LabelPrinter printer;
    private final NotificationService notifier;

    public PharmacyFacade(PrescriptionValidator validator,
                          InventoryService inventory,
                          PricingService pricing,
                          PaymentProcessor payment,
                          LabelPrinter printer,
                          NotificationService notifier) {
        this.validator = validator;
        this.inventory = inventory;
        this.pricing = pricing;
        this.payment = payment;
        this.printer = printer;
        this.notifier = notifier;
    }

    public OrderResult processPrescription(Prescription prescription, PaymentMethod paymentMethod) {
        String orderId = generateOrderId();
        boolean reserved = false;
        String transactionId = null;

        try {
            validator.validate(prescription);

            BigDecimal total = pricing.calculateTotal(
                    prescription.medication(), prescription.quantity());

            inventory.reserve(prescription.medication(), prescription.quantity());
            reserved = true;

            transactionId = payment.charge(paymentMethod, total);

            printer.printLabel(prescription, orderId);

            boolean notified = tryNotifyPatient(prescription, orderId);
            String smsNote = notified ? "" : " (SMS not sent)";
            return OrderResult.success(orderId, "Ready for pickup. Total: " + total + smsNote);

        } catch (PharmacyException e) {
            rollback(prescription, reserved, transactionId);
            return OrderResult.failure(orderId, e.getMessage());

        } catch (RuntimeException e) {
            rollback(prescription, reserved, transactionId);
            return OrderResult.failure(orderId, "internal system error");
        }
    }

    private boolean tryNotifyPatient(Prescription prescription, String orderId) {
        try {
            notifier.notifyReady(prescription.patient(), orderId);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    private void rollback(Prescription prescription, boolean reserved, String transactionId) {
        if (transactionId != null) {
            try {
                payment.refund(transactionId);
            } catch (RuntimeException e) {
                System.err.println("Refund failed: " + e.getMessage());
            }
        }
        if (reserved) {
            try {
                inventory.release(prescription.medication(), prescription.quantity());
            } catch (RuntimeException e) {
                System.err.println("Stock release failed: " + e.getMessage());
            }
        }
    }

    private String generateOrderId() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8);
    }
}