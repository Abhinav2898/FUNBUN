package hms.model;
import java.io.Serializable;

// WEEK 6 - Serializable for persistent storage
public class Customer implements Serializable {

    private static final long serialVersionUID = 6L; // Updated after removing room field

    private int customerId;
    private String customerName;
    private String contactNumber;

    public Customer(int customerId, String customerName, String contactNumber) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.contactNumber = contactNumber;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    @Override
    public String toString() {
        return customerName + " (ID: " + customerId + ")";
    }
}