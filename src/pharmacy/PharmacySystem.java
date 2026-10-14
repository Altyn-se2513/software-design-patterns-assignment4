package pharmacy.facade;

import pharmacy.subsystem.InventoryService;
import pharmacy.subsystem.LabelPrinter;
import pharmacy.subsystem.NotificationService;
import pharmacy.subsystem.PaymentProcessor;
import pharmacy.subsystem.PrescriptionValidator;
import pharmacy.subsystem.PricingService;

public final class PharmacySystem {

    private final PharmacyFacade orders;
    private final PharmacyAdminFacade admin;

    private PharmacySystem(PharmacyFacade orders, PharmacyAdminFacade admin) {
        this.orders = orders;
        this.admin = admin;
    }

    public static PharmacySystem createDefault() {
        InventoryService sharedInventory = new InventoryService();

        PharmacyFacade orders = new PharmacyFacade(
                new PrescriptionValidator(),
                sharedInventory,
                new PricingService(),
                new PaymentProcessor(),
                new LabelPrinter(),
                new NotificationService()
        );
        PharmacyAdminFacade admin = new PharmacyAdminFacade(sharedInventory);

        return new PharmacySystem(orders, admin);
    }

    public PharmacyFacade orders() {
        return orders;
    }

    public PharmacyAdminFacade admin() {
        return admin;
    }
}
