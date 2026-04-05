package com.hotel.billing;

import com.hotel.model.Booking;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// WEEK 5 - Character Streams: FileWriter, BufferedWriter
// WEEK 2 - Wrapper classes: Double, Integer used for formatting
public class BillingManager {

    private static final String BILLS_DIR = "bills/";
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // WEEK 2 - Wrapper classes for type conversion
    public double calculateTotal(int days, double pricePerDay) {
        // WEEK 2 - Integer.valueOf wraps int, Double.valueOf wraps double
        Integer daysWrapped = Integer.valueOf(days);
        Double priceWrapped = Double.valueOf(pricePerDay);
        return daysWrapped * priceWrapped; // Autoboxing/Unboxing happens here
    }

    // Generates bill as a formatted String
    public String generateBill(Booking booking) {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("       GRAND HOTEL - BILL RECEIPT       \n");
        sb.append("========================================\n");
        sb.append("Booking ID   : ").append(booking.getBookingId()).append("\n");
        sb.append("Customer     : ").append(booking.getCustomer().getCustomerName()).append("\n");
        sb.append("Contact      : ").append(booking.getCustomer().getContactNumber()).append("\n");
        sb.append("Room No.     : ").append(booking.getRoom().getRoomNumber()).append("\n");
        sb.append("Room Type    : ").append(booking.getRoom().getRoomType().getLabel()).append("\n");
        sb.append("Check-In     : ").append(booking.getCheckInDate().format(DATE_FMT)).append("\n");
        sb.append("Check-Out    : ");

        LocalDate checkOut = booking.getCheckOutDate() != null ? booking.getCheckOutDate() : LocalDate.now();
        sb.append(checkOut.format(DATE_FMT)).append("\n");

        long days = java.time.temporal.ChronoUnit.DAYS.between(booking.getCheckInDate(), checkOut);
        if (days <= 0) days = 1;

        sb.append("Days Stayed  : ").append(days).append("\n");
        // WEEK 2 - Double.valueOf used for explicit wrapping
        sb.append("Rate/Day     : Rs.").append(Double.valueOf(booking.getRoom().getPricePerDay())).append("\n");
        sb.append("----------------------------------------\n");
        sb.append("TOTAL BILL   : Rs.").append(String.format("%.2f", booking.getTotalBill())).append("\n");
        sb.append("========================================\n");
        sb.append("     Thank you for staying with us!     \n");
        sb.append("========================================\n");

        return sb.toString();
    }

    // WEEK 5 - FileWriter + BufferedWriter = Character Streams
    public void saveBillToFile(Booking booking) {
        String fileName = BILLS_DIR + "Bill_Booking" + booking.getBookingId() +
                          "_" + booking.getCustomer().getCustomerName().replaceAll(" ", "_") + ".txt";
        try {
            new File(BILLS_DIR).mkdirs();
            // WEEK 5 - FileWriter is a Character Stream (writes characters, not bytes)
            FileWriter fw = new FileWriter(fileName);
            // WEEK 5 - BufferedWriter wraps FileWriter for efficient writing
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(generateBill(booking));
            bw.newLine();
            bw.write("Generated on: " + LocalDate.now().format(DATE_FMT));

            bw.close(); // WEEK 5 - Always close streams
            fw.close();

            System.out.println("Bill saved to: " + fileName);
        } catch (IOException e) {
            System.err.println("Error saving bill: " + e.getMessage());
        }
    }

    public void printBillSummary(Booking booking) {
        System.out.println(generateBill(booking));
    }
}
