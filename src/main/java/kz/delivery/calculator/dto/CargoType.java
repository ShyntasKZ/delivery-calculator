package kz.delivery.calculator.dto;

public enum CargoType {
    STANDARD(0.0),
    FRAGILE(0.10),
    OVERSIZED(0.25);

    private final double surchargeRate;

    CargoType(double surchargeRate) {
        this.surchargeRate = surchargeRate;
    }

    public double getSurchargeRate() {
        return surchargeRate;
    }
}
