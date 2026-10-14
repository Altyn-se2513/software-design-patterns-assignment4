package pharmacy.model;

public record OrderResult(String orderId, boolean success, String message) {

    public static OrderResult success(String orderId, String message) {
        return new OrderResult(orderId, true, message);
    }

    public static OrderResult failure(String orderId, String reason) {
        return new OrderResult(orderId, false, "Error: " + reason);
    }
}
