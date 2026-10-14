package pharmacy.subsystem;

import pharmacy.model.Prescription;

public class LabelPrinter {

    public void printLabel(Prescription prescription, String orderId) {
        System.out.println(
                "Label " + orderId + ": " +
                prescription.medication() + " x" +
                prescription.quantity() + " for " +
                prescription.patient().name()
        );
    }
}