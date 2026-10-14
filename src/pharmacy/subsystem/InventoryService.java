package pharmacy.subsystem;

import pharmacy.exception.PharmacyException;

import java.util.HashMap;
import java.util.Map;

public class InventoryService {

    private final Map<String, Integer> stock = new HashMap<>();

    public InventoryService() {
        stock.put("Paracetamol", 50);
        stock.put("Amoxicillin", 30);
    }

    public boolean isAvailable(String medication, int quantity) {
        return stock.getOrDefault(medication, 0) >= quantity;
    }

    public void reserve(String medication, int quantity) {
        requirePositive(quantity);
        int available = stock.getOrDefault(medication, 0);
        if (available < quantity) {
            throw new PharmacyException("Not enough stock: " + medication
                    + " (available " + available + ", needed " + quantity + ")");
        }
        stock.put(medication, available - quantity);
    }

    public void release(String medication, int quantity) {
        requirePositive(quantity);
        stock.merge(medication, quantity, Integer::sum);
    }

    public void addStock(String medication, int quantity) {
        if (medication == null || medication.isBlank()) {
            throw new PharmacyException("Medication not specified");
        }
        requirePositive(quantity);
        stock.merge(medication, quantity, Integer::sum);
    }

    public Map<String, Integer> getStockSnapshot() {
        return Map.copyOf(stock);
    }

    private void requirePositive(int quantity) {
        if (quantity <= 0) {
            throw new PharmacyException("Quantity must be greater than 0");
        }
    }
}
