package com.hotel.storage;

import com.hotel.model.Booking;
import com.hotel.model.Customer;
import com.hotel.model.Room;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// WEEK 3 - Multithreading: loadAllDataInBackground uses Thread
// WEEK 4 - Synchronization: all save/load methods are synchronized
// WEEK 6 - Serialization: ObjectOutputStream / ObjectInputStream
public class FileStorage {

    private static final String DATA_DIR = "data/";
    private static final String ROOMS_FILE = DATA_DIR + "rooms.dat";
    private static final String CUSTOMERS_FILE = DATA_DIR + "customers.dat";
    private static final String BOOKINGS_FILE = DATA_DIR + "bookings.dat";

    // WEEK 4 - synchronized: prevents concurrent read/write corruption
    public synchronized void saveRooms(List<Room> rooms) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ROOMS_FILE))) {
            // WEEK 6 - Serialization: writeObject saves full object graph
            oos.writeObject(new ArrayList<>(rooms));
        } catch (IOException e) {
            System.err.println("Error saving rooms: " + e.getMessage());
        }
    }

    // WEEK 4 - synchronized
    // WEEK 6 - Deserialization: readObject restores object from file
    @SuppressWarnings("unchecked")
    public synchronized List<Room> loadRooms() {
        File file = new File(ROOMS_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(ROOMS_FILE))) {
            return (List<Room>) ois.readObject(); // WEEK 6 - Deserialization
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading rooms: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // WEEK 4 - synchronized
    public synchronized void saveCustomers(List<Customer> customers) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(CUSTOMERS_FILE))) {
            oos.writeObject(new ArrayList<>(customers));
        } catch (IOException e) {
            System.err.println("Error saving customers: " + e.getMessage());
        }
    }

    // WEEK 4 - synchronized
    @SuppressWarnings("unchecked")
    public synchronized List<Customer> loadCustomers() {
        File file = new File(CUSTOMERS_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(CUSTOMERS_FILE))) {
            return (List<Customer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading customers: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // WEEK 4 - synchronized
    public synchronized void saveBookings(List<Booking> bookings) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(BOOKINGS_FILE))) {
            oos.writeObject(new ArrayList<>(bookings));
        } catch (IOException e) {
            System.err.println("Error saving bookings: " + e.getMessage());
        }
    }

    // WEEK 4 - synchronized
    @SuppressWarnings("unchecked")
    public synchronized List<Booking> loadBookings() {
        File file = new File(BOOKINGS_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(BOOKINGS_FILE))) {
            return (List<Booking>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading bookings: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // WEEK 3 - Multithreading: runs file loading in background Thread
    // so the JavaFX UI does not freeze during startup
    public void loadAllDataInBackground(Runnable onComplete) {
        // WEEK 3 - Creating and starting a Thread
        Thread loaderThread = new Thread(() -> {
            // Background work: load all data files
            System.out.println("Background thread loading data...");
            loadRooms();
            loadCustomers();
            loadBookings();
            System.out.println("Background thread done.");

            // After loading, run the completion callback on JavaFX thread
            if (onComplete != null) {
                javafx.application.Platform.runLater(onComplete);
            }
        });

        loaderThread.setDaemon(true); // WEEK 3 - Daemon thread (background)
        loaderThread.setName("DataLoader-Thread"); // WEEK 3 - Named thread
        loaderThread.start(); // WEEK 3 - Starting the thread
    }

    // Ensure data directory exists
    public void ensureDirectories() {
        new File(DATA_DIR).mkdirs();
        new File("bills").mkdirs();
    }
}
