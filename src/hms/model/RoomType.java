package hms.model;
// WEEK 2 - Enumeration
public enum RoomType {
    SINGLE("Single", 1000.0),
    DOUBLE("Double", 2000.0),
    DELUXE("Deluxe", 3500.0);

    private final String label;
    private final double pricePerDay;

    RoomType(String label, double pricePerDay) {
        this.label = label;
        this.pricePerDay = pricePerDay;
    }

    public String getLabel() {
        return label;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    @Override
    public String toString() {
        return label;
    }
}