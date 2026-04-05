package com.hotel.ui;

import com.hotel.MainApp;
import com.hotel.billing.BillingManager;
import com.hotel.model.Booking;
import com.hotel.model.Customer;
import com.hotel.model.Room;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.util.List;

// WEEK 9 - JavaFX: DatePicker, ComboBox, TableView, Button, GridPane, VBox, HBox
public class BookingScreen {

    private TableView<Booking> tableView;
    private ComboBox<Customer> customerCombo;
    private ComboBox<Room> roomCombo;
    private DatePicker checkInDatePicker;
    private Label messageLabel;
    private ObservableList<Booking> bookingData;
    private BillingManager billingManager = new BillingManager();

    public VBox getView() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f0f4ff;");

        Label heading = new Label("Booking & Checkout");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a237e;");

        GridPane form = buildBookingForm();
        HBox buttonRow = buildButtonRow();

        messageLabel = new Label("");
        messageLabel.setStyle("-fx-font-size: 13px;");

        tableView = buildBookingTable();
        VBox.setVgrow(tableView, Priority.ALWAYS);

        root.getChildren().addAll(heading, form, buttonRow, messageLabel, tableView);
        loadBookings();
        return root;
    }

    // WEEK 9 - GridPane for booking form
    private GridPane buildBookingForm() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
        grid.setStyle("-fx-background-color: white; -fx-background-radius: 8; " +
                      "-fx-border-color: #c5cae9; -fx-border-radius: 8;");

        Label title = new Label("New Booking");
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3949ab;");
        grid.add(title, 0, 0, 4, 1);

        // WEEK 9 - ComboBox for customer selection
        grid.add(new Label("Customer:"), 0, 1);
        customerCombo = new ComboBox<>();
        customerCombo.setPromptText("Select customer");
        customerCombo.setPrefWidth(200);
        grid.add(customerCombo, 1, 1);

        // WEEK 9 - ComboBox for room selection
        grid.add(new Label("Available Room:"), 2, 1);
        roomCombo = new ComboBox<>();
        roomCombo.setPromptText("Select room");
        roomCombo.setPrefWidth(200);
        grid.add(roomCombo, 3, 1);

        // WEEK 9 - DatePicker component
        grid.add(new Label("Check-In Date:"), 0, 2);
        checkInDatePicker = new DatePicker(LocalDate.now());
        grid.add(checkInDatePicker, 1, 2);

        Button refreshBtn = new Button("🔄 Refresh");
        refreshBtn.setStyle("-fx-background-color: #7986cb; -fx-text-fill: white; -fx-cursor: hand;");
        refreshBtn.setOnAction(e -> loadCombos());
        grid.add(refreshBtn, 2, 2);

        loadCombos();
        return grid;
    }

    // WEEK 9 - HBox for action buttons
    private HBox buildButtonRow() {
        Button bookBtn = new Button("📋 Book Room");
        Button checkoutBtn = new Button("🚪 Checkout");
        Button viewActiveBtn = new Button("✅ Active Bookings");
        Button viewAllBtn = new Button("📜 All Bookings");

        bookBtn.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white; " +
                         "-fx-font-weight: bold; -fx-cursor: hand;");
        checkoutBtn.setStyle("-fx-background-color: #e65100; -fx-text-fill: white; " +
                             "-fx-font-weight: bold; -fx-cursor: hand;");
        viewActiveBtn.setStyle("-fx-background-color: #1565c0; -fx-text-fill: white; -fx-cursor: hand;");
        viewAllBtn.setStyle("-fx-background-color: #4a148c; -fx-text-fill: white; -fx-cursor: hand;");

        // WEEK 9 - Event handling
        bookBtn.setOnAction(e -> handleBooking());
        checkoutBtn.setOnAction(e -> handleCheckout());
        viewActiveBtn.setOnAction(e -> loadActiveBookings());
        viewAllBtn.setOnAction(e -> loadBookings());

        HBox box = new HBox(10, bookBtn, checkoutBtn, viewActiveBtn, viewAllBtn);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    // WEEK 9 - TableView for bookings
    private TableView<Booking> buildBookingTable() {
        TableView<Booking> table = new TableView<>();
        table.setStyle("-fx-background-color: white;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Booking, Integer> idCol = new TableColumn<>("Booking ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("bookingId"));

        TableColumn<Booking, String> custCol = new TableColumn<>("Customer");
        custCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getCustomer().getCustomerName()));

        TableColumn<Booking, String> roomCol = new TableColumn<>("Room No.");
        roomCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                String.valueOf(data.getValue().getRoom().getRoomNumber())));

        TableColumn<Booking, LocalDate> checkInCol = new TableColumn<>("Check-In");
        checkInCol.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));

        TableColumn<Booking, LocalDate> checkOutCol = new TableColumn<>("Check-Out");
        checkOutCol.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));

        TableColumn<Booking, Double> billCol = new TableColumn<>("Total Bill (Rs.)");
        billCol.setCellValueFactory(new PropertyValueFactory<>("totalBill"));

        TableColumn<Booking, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        statusCol.setCellFactory(col -> new TableCell<Booking, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) { setText(null); setStyle(""); }
                else {
                    setText(item);
                    setStyle("Active".equals(item)
                        ? "-fx-text-fill: #2e7d32; -fx-font-weight: bold;"
                        : "-fx-text-fill: #c62828; -fx-font-weight: bold;");
                }
            }
        });

        table.getColumns().addAll(idCol, custCol, roomCol, checkInCol, checkOutCol, billCol, statusCol);
        bookingData = FXCollections.observableArrayList();
        table.setItems(bookingData);
        return table;
    }

    // WEEK 9 - Event: Book a room
    private void handleBooking() {
        Customer customer = customerCombo.getValue();
        Room room = roomCombo.getValue();
        LocalDate checkIn = checkInDatePicker.getValue();

        if (customer == null || room == null || checkIn == null) {
            showMessage("Please select customer, room, and check-in date.", false);
            return;
        }

        if (!room.isAvailable()) {
            showMessage("Room " + room.getRoomNumber() + " is already occupied!", false);
            loadCombos();
            return;
        }

        // Book the room
        int newId = MainApp.bookingRepository.getNextId();
        Booking booking = new Booking(newId, customer, room, checkIn);
        room.setAvailable(false);
        MainApp.roomRepository.markRoomOccupied(room.getRoomNumber());

        MainApp.bookingRepository.add(booking);

        // WEEK 6 - Save all updated data
        MainApp.fileStorage.saveBookings(MainApp.bookingRepository.getAll());
        MainApp.fileStorage.saveRooms(MainApp.roomRepository.getAll());

        showMessage("Room " + room.getRoomNumber() + " booked successfully for " +
                    customer.getCustomerName() + "! Booking ID: " + newId, true);
        loadBookings();
        loadCombos();
    }

    // WEEK 9 - Event: Checkout
    private void handleCheckout() {
        Booking selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Please select a booking to checkout.", false);
            return;
        }
        if (!selected.isActive()) {
            showMessage("This booking is already checked out.", false);
            return;
        }

        LocalDate checkOut = LocalDate.now();
        selected.checkout(checkOut);

        // Generate and save bill (WEEK 5 - FileWriter)
        billingManager.saveBillToFile(selected);

        // WEEK 6 - Save updated data
        MainApp.fileStorage.saveBookings(MainApp.bookingRepository.getAll());
        MainApp.fileStorage.saveRooms(MainApp.roomRepository.getAll());

        // WEEK 9 - Alert dialog for bill summary
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Checkout Successful");
        alert.setHeaderText("Bill Generated for " + selected.getCustomer().getCustomerName());
        alert.setContentText(billingManager.generateBill(selected));
        alert.getDialogPane().setPrefWidth(450);
        alert.showAndWait();

        showMessage("Checkout complete! Bill saved to bills/ folder.", true);
        loadBookings();
        loadCombos();
    }

    private void loadCombos() {
        customerCombo.setItems(FXCollections.observableArrayList(
            MainApp.customerRepository.getAllCustomers()));
        roomCombo.setItems(FXCollections.observableArrayList(
            MainApp.roomRepository.getAvailableRooms()));
    }

    private void loadBookings() {
        bookingData.setAll(MainApp.bookingRepository.getAll());
    }

    private void loadActiveBookings() {
        bookingData.setAll(MainApp.bookingRepository.getActiveBookings());
    }

    public void refresh() {
        loadCombos();
        loadBookings();
    }

    private void showMessage(String msg, boolean success) {
        messageLabel.setText(msg);
        messageLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " +
                              (success ? "#2e7d32" : "#c62828") + ";");
    }
}
