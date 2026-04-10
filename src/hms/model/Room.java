package hms.model;
import java.io.Serializable;

// WEEK 6 - Serializable for file persistence
// WEEK 2 - Wrapper classes used in getters
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    private int roomNumber;
    private RoomType roomType;
    private double pricePerDay;
    private boolean isAvailable;

    public Room(int roomNumber, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        // WEEK 2 - Autoboxing: double primitive auto-wrapped
        this.pricePerDay = roomType.getPricePerDay();
        this.isAvailable = true;
    }

    // WEEK 2 - Wrapper: Integer.valueOf used when displaying in TableView
    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    // WEEK 2 - Autoboxing: double auto-wrapped to Double when needed
    public double getPricePerDay() {
        return pricePerDay;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getAvailabilityStatus() {
        return isAvailable ? "Available" : "Occupied";
    }

    @Override
    public String toString() {
        return "Room " + roomNumber + " [" + roomType.getLabel() + "] - Rs." + pricePerDay + "/day - " + getAvailabilityStatus();
    }
}