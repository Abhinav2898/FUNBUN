package com.hotel.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// WEEK 6 - Serializable
// WEEK 2 - Wrapper classes for bill calculation
public class Booking implements Serializable {

    private static final long serialVersionUID = 3L;

    private int bookingId;
    private Customer customer;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalBill;
    private boolean isActive;

    public Booking(int bookingId, Customer customer, Room room, LocalDate checkInDate) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = null;
        this.totalBill = 0.0;
        this.isActive = true;
    }

    // WEEK 2 - Double.valueOf and Integer parseInt used for type conversion
    public double calculateBill(LocalDate checkOut) {
        long days = ChronoUnit.DAYS.between(checkInDate, checkOut);
        if (days <= 0) days = 1; // minimum 1 day
        // WEEK 2 - Autoboxing: long auto-converted, Double.valueOf wraps result
        double price = Double.valueOf(room.getPricePerDay());
        return days * price;
    }

    public void checkout(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
        this.totalBill = calculateBill(checkOutDate);
        this.isActive = false;
        this.room.setAvailable(true);
    }

    public int getBookingId() { return bookingId; }
    public Customer getCustomer() { return customer; }
    public Room getRoom() { return room; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public double getTotalBill() { return totalBill; }
    public boolean isActive() { return isActive; }

    public String getStatus() {
        return isActive ? "Active" : "Checked Out";
    }

    @Override
    public String toString() {
        return "Booking #" + bookingId + " | " + customer.getCustomerName() +
               " | Room " + room.getRoomNumber() + " | " + getStatus();
    }
}
