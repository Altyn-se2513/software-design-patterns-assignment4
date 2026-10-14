package pharmacy.model;

public record Prescription(
        String id,
        Patient patient,
        String medication,
        int quantity,
        String doctorLicense
) {}
