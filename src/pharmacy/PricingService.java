package pharmacy.subsystem;

import pharmacy.exception.PharmacyException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PricingService {

    private final Map<String, BigDecimal> prices = new HashMap<>();

    public PricingService() {
        prices.put("Paracetamol", new BigDecimal("5.00"));
        prices.put("Amoxicillin", new BigDecimal("12.00"));
    }

    public BigDecimal calculateTotal(String medication, int quantity) {
        BigDecimal price = prices.get(medication);
        if (price == null) {
            throw new PharmacyException("No price for: " + medication);
        }
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
