package pharmacy;

import pharmacy.facade.PharmacyAdminFacade;
import pharmacy.facade.PharmacyFacade;
import pharmacy.facade.PharmacySystem;
import pharmacy.model.OrderResult;
import pharmacy.model.Patient;
import pharmacy.model.PaymentMethod;
import pharmacy.model.Prescription;

public class Main {

    public static void main(String[] args) {
        PharmacySystem pharmacy = PharmacySystem.createDefault();
        PharmacyFacade orders = pharmacy.orders();
        PharmacyAdminFacade admin = pharmacy.admin();

        Patient ivan = new Patient("P1", "Ivan", "+77001234567");
        Patient aigerim = new Patient("P2", "Aigerim", null);

        section("1. Successful order");
        show(orders.processPrescription(
                new Prescription("R1", ivan, "Paracetamol", 2, "LIC-123"), PaymentMethod.CARD));

        section("2. No doctor license -> rejection at validation step");
        show(orders.processPrescription(
                new Prescription("R2", ivan, "Paracetamol", 1, ""), PaymentMethod.CARD));

        section("3. Not enough stock -> rejection at inventory step");
        show(orders.processPrescription(
                new Prescription("R3", ivan, "Amoxicillin", 100, "LIC-123"), PaymentMethod.CASH));

        section("4. Payment error -> reservation returned to inventory (rollback)");
        System.out.println("Inventory before: " + admin.currentStock());
        show(orders.processPrescription(
                new Prescription("R4", ivan, "Amoxicillin", 5, "LIC-123"), null));
        System.out.println("Inventory after:  " + admin.currentStock());

        section("5. No phone -> order is still issued, SMS is not critical");
        show(orders.processPrescription(
                new Prescription("R5", aigerim, "Amoxicillin", 3, "LIC-777"), PaymentMethod.QR));

        section("6. Admin facade: report and restock (shared inventory)");
        System.out.println("Low stock (<40): " + admin.lowStockReport(40));
        admin.restock("Amoxicillin", 50);
        System.out.println("After restock:   " + admin.currentStock());
        System.out.println("Low stock (<40): " + admin.lowStockReport(40));
    }

    private static void section(String title) {
        System.out.println();
        System.out.println("=== " + title + " ===");
    }

    private static void show(OrderResult result) {
        System.out.println("-> " + result.orderId() + " | "
                + (result.success() ? "SUCCESS" : "REJECTED") + " | " + result.message());
    }
}
