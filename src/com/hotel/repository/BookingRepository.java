package com.hotel.repository;

import com.hotel.model.Booking;
import java.util.ArrayList;
import java.util.List;

// WEEK 7 - Extends Generic Repository<Booking>
public class BookingRepository extends Repository<Booking> {

    public Booking getBookingById(int id) {
        for (Booking b : items) {
            if (b.getBookingId() == id) return b;
        }
        return null;
    }

    // WEEK 8 - List operations
    public List<Booking> getActiveBookings() {
        List<Booking> active = new ArrayList<>();
        for (Booking b : items) {
            if (b.isActive()) active.add(b);
        }
        return active;
    }

    public List<Booking> getCompletedBookings() {
        List<Booking> completed = new ArrayList<>();
        for (Booking b : items) {
            if (!b.isActive()) completed.add(b);
        }
        return completed;
    }

    public Booking getActiveBookingByRoom(int roomNumber) {
        for (Booking b : items) {
            if (b.isActive() && b.getRoom().getRoomNumber() == roomNumber) {
                return b;
            }
        }
        return null;
    }

    public int getNextId() {
        int maxId = 0;
        for (Booking b : items) {
            if (b.getBookingId() > maxId) maxId = b.getBookingId();
        }
        return maxId + 1;
    }
}
