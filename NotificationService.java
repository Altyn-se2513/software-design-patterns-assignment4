package pharmacy.subsystem;

import pharmacy.exception.PharmacyException;
import pharmacy.model.Patient;

public class NotificationService {

    public void notifyReady(Patient patient, String orderId) {
        if (patient.phone() == null || patient.phone().isBlank()) {
            throw new PharmacyException("Patient has no phone number");
        }
        System.out.println("SMS to " + patient.phone() + ": order " + orderId + " is ready");
    }
}