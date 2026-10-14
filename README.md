# Assignment #4 - Facade Pattern: Pharmacy System

## Overview
The client wants one thing: "fill this prescription and take my payment." Behind this, there are six subsystem classes. The PharmacyFacade hides them behind a single method: processPrescription.

## Project Structure
``` 
src/pharmacy/
  Main.java
  facade/
    PharmacyFacade.java
    PharmacyAdminFacade.java
    PharmacySystem.java
  subsystem/
    PrescriptionValidator.java
    InventoryService.java
    PricingService.java
    PaymentProcessor.java
    LabelPrinter.java
    NotificationService.java
  model/
    Patient.java
    Prescription.java
    PaymentMethod.java
    OrderResult.java
  exception/
    PharmacyException.java
```

## Order Flow and Rollback
The facade calls subsystems in this exact order:
1. `PrescriptionValidator.validate` — prescription, patient, medication, license, quantity.
2. `PricingService.calculateTotal` — calculates the price (no side effects).
3. `InventoryService.reserve` — reserves the stock (`reserved = true`).
4. `PaymentProcessor.charge` — charges the money (saves `transactionId`).
5. `LabelPrinter.printLabel` — prints the label.
6. `NotificationService.notifyReady` — sends SMS (non-critical).

Why stock before payment?
We never charge for goods we might not have.

Rollback: If steps 3–5 fail, the facade compensates: it refunds the payment (if `transactionId` exists) and releases the reserved stock back to the inventory.

Why SMS doesn't cancel the order: A failed SMS shouldn't cancel a paid, packed order. The `tryNotifyPatient()` method catches the exception and returns a success result with a note `(SMS not sent)`.

## Clean Code Principles Applied
1. Meaningful names — processPrescription, reserve, release, addStock, lowStockReport.
2. Single Responsibility — the facade only coordinates; each subsystem owns one job.
3. No leaking of subsystem details — the client sees only Prescription, PaymentMethod, and OrderResult.
4. Small, focused methods — rollback, tryNotifyPatient, generateOrderId.
5. Proper error handling — catches domain and runtime exceptions, compensation on failure.
6. Type safety — PaymentMethod enum instead of strings, BigDecimal instead of double for money.
7. Dependency Injection — subsystems are passed into the facade constructor.
8. Encapsulation — getStockSnapshot returns an immutable copy.

## How to Run
Java 17 or newer is required.

```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out pharmacy.Main
```
