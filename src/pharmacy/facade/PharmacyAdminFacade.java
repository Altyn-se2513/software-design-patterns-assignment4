package pharmacy.facade;

import pharmacy.exception.PharmacyException;
import pharmacy.subsystem.InventoryService;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class PharmacyAdminFacade {

    private final InventoryService inventory;

    public PharmacyAdminFacade(InventoryService inventory) {
        this.inventory = inventory;
    }

    public void restock(String medication, int quantity) {
        inventory.addStock(medication, quantity);
    }

    public Map<String, Integer> lowStockReport(int threshold) {
        if (threshold <= 0) {
            throw new PharmacyException("Threshold must be greater than 0");
        }
        Map<String, Integer> result = new TreeMap<>();
        inventory.getStockSnapshot().forEach((medication, quantity) -> {
            if (quantity < threshold) {
                result.put(medication, quantity);
            }
        });
        return Collections.unmodifiableMap(result);
    }

    public Map<String, Integer> currentStock() {
        return Collections.unmodifiableMap(new TreeMap<>(inventory.getStockSnapshot()));
    }
}
