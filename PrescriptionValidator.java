package pharmacy.subsystem;

import pharmacy.exception.PharmacyException;
import pharmacy.model.Prescription;

public class PrescriptionValidator {

    public void validate(Prescription prescription) {
        if (prescription == null) {
            throw new PharmacyException("Prescription is missing");
        }
        if (prescription.patient() == null) {
            throw new PharmacyException("Patient not specified");
        }
        if (prescription.medication() == null || prescription.medication().isBlank()) {
            throw new PharmacyException("Medication not specified");
        }
        if (prescription.doctorLicense() == null || prescription.doctorLicense().isBlank()) {
            throw new PharmacyException("No doctor license");
        }
        if (prescription.quantity() <= 0) {
            throw new PharmacyException("Invalid medication quantity");
        }
    }
}