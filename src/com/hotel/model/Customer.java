package com.hotel.model;

import java.io.Serializable;

// WEEK 6 - Serializable for persistent storage
public class Customer implements Serializable {

    private static final long serialVersionUID = 5L; // Changed ID again after model update

    private int customerId;
    private String customerName;
    private String contactNumber;
    private int assignedRoomNumber;

    public Customer(int customerId, String customerName, String contactNumber, int assignedRoomNumber) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.contactNumber = contactNumber;
        this.assignedRoomNumber = assignedRoomNumber;
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

    public int getAssignedRoomNumber() {
        return assignedRoomNumber;
    }

    public void setAssignedRoomNumber(int roomNumber) {
        this.assignedRoomNumber = roomNumber;
    }

    @Override
    public String toString() {
        return customerName + " (Room " + assignedRoomNumber + ")";
    }
}
