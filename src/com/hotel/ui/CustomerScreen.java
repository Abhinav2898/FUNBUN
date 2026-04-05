package com.hotel.ui;

import com.hotel.MainApp;
import com.hotel.model.Customer;
import com.hotel.model.Room;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.util.List;

// WEEK 9 - JavaFX: Label, TextField, Button, ComboBox, TableView, GridPane, VBox, HBox
public class CustomerScreen {

    private TableView<Customer> tableView;
    private TextField nameField;
    private TextField contactField;
    private ComboBox<Room> roomCombo;
    private Label messageLabel;
    private ObservableList<Customer> customerData;

    public VBox getView() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f0f4ff;");

        Label heading = new Label("Customer Management");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a237e;");

        GridPane form = buildForm();
        HBox buttonRow = buildButtonRow();

        messageLabel = new Label("");
        messageLabel.setStyle("-fx-font-size: 13px;");

        tableView = buildTable();
        VBox.setVgrow(tableView, Priority.ALWAYS);

        root.getChildren().addAll(heading, form, buttonRow, messageLabel, tableView);
        loadCustomers();
        return root;
    }

    // WEEK 9 - GridPane for customer form
    private GridPane buildForm() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
        grid.setStyle("-fx-background-color: white; -fx-background-radius: 8; " +
                      "-fx-border-color: #c5cae9; -fx-border-radius: 8;");

        Label title = new Label("Add New Customer");
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3949ab;");
        grid.add(title, 0, 0, 4, 1);

        // WEEK 9 - Label + TextField components
        grid.add(new Label("Customer Name:"), 0, 1);
        nameField = new TextField();
        nameField.setPromptText("Full name");
        nameField.setPrefWidth(160);
        grid.add(nameField, 1, 1);

        grid.add(new Label("Contact No.:"), 2, 1);
        contactField = new TextField();
        contactField.setPromptText("10-digit number");
        contactField.setPrefWidth(160);
        grid.add(contactField, 3, 1);

        grid.add(new Label("Select Room:"), 0, 2);
        // WEEK 9 - ComboBox showing available rooms
        roomCombo = new ComboBox<>();
        roomCombo.setPromptText("Choose room");
        roomCombo.setPrefWidth(160);
        grid.add(roomCombo, 1, 2);

        Button refreshRoomsBtn = new Button("🔄 Refresh Rooms");
        refreshRoomsBtn.setStyle("-fx-background-color: #7986cb; -fx-text-fill: white; -fx-cursor: hand;");
        refreshRoomsBtn.setOnAction(e -> loadAvailableRooms());
        grid.add(refreshRoomsBtn, 2, 2);

        loadAvailableRooms();
        return grid;
    }

    // WEEK 9 - HBox for action buttons
    private HBox buildButtonRow() {
        Button addBtn = new Button("➕ Add Customer");
        Button clearBtn = new Button("🔄 Clear Fields");

        addBtn.setStyle("-fx-background-color: #3949ab; -fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-cursor: hand;");
        clearBtn.setStyle("-fx-background-color: #757575; -fx-text-fill: white; -fx-cursor: hand;");

        // WEEK 9 - Event handling
        addBtn.setOnAction(e -> handleAddCustomer());
        clearBtn.setOnAction(e -> clearFields());

        HBox box = new HBox(10, addBtn, clearBtn);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    // WEEK 9 - TableView for customer list
    private TableView<Customer> buildTable() {
        TableView<Customer> table = new TableView<>();
        table.setStyle("-fx-background-color: white;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Customer, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        TableColumn<Customer, String> nameCol = new TableColumn<>("Customer Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        TableColumn<Customer, String> contactCol = new TableColumn<>("Contact Number");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));

        TableColumn<Customer, Integer> roomCol = new TableColumn<>("Assigned Room");
        roomCol.setCellValueFactory(new PropertyValueFactory<>("assignedRoomNumber"));

        table.getColumns().addAll(idCol, nameCol, contactCol, roomCol);
        customerData = FXCollections.observableArrayList();
        table.setItems(customerData);
        return table;
    }

    // WEEK 9 - Event: Add customer with validation
    private void handleAddCustomer() {
        String name = nameField.getText().trim();
        String contact = contactField.getText().trim();
        Room selectedRoom = roomCombo.getValue();

        if (name.isEmpty() || contact.isEmpty() || selectedRoom == null) {
            showMessage("Please fill all fields and select a room.", false);
            return;
        }

        if (!contact.matches("\\d{10}")) {
            showMessage("Contact number must be exactly 10 digits.", false);
            return;
        }

        if (!selectedRoom.isAvailable()) {
            showMessage("Selected room is no longer available!", false);
            loadAvailableRooms();
            return;
        }

        int newId = MainApp.customerRepository.getNextId();
        Customer customer = new Customer(newId, name, contact, selectedRoom.getRoomNumber());
        MainApp.customerRepository.add(customer);

        // Mark the room as occupied to fix the old room-double-assignment bug
        MainApp.roomRepository.markRoomOccupied(selectedRoom.getRoomNumber());

        // WEEK 6 - Save updated customers and rooms
        MainApp.fileStorage.saveCustomers(MainApp.customerRepository.getAll());
        MainApp.fileStorage.saveRooms(MainApp.roomRepository.getAll());

        showMessage("Customer '" + name + "' added successfully! (ID: " + newId + ")", true);
        clearFields();
        loadCustomers();
        loadAvailableRooms(); // Refresh dropdown immediately
    }

    private void loadCustomers() {
        List<Customer> customers = MainApp.customerRepository.getAllCustomers();
        customerData.setAll(customers);
    }

    private void loadAvailableRooms() {
        List<Room> available = MainApp.roomRepository.getAvailableRooms();
        roomCombo.setItems(FXCollections.observableArrayList(available));
    }

    private void clearFields() {
        nameField.clear();
        contactField.clear();
        roomCombo.setValue(null);
        messageLabel.setText("");
    }

    private void showMessage(String msg, boolean success) {
        messageLabel.setText(msg);
        messageLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " +
                              (success ? "#2e7d32" : "#c62828") + ";");
    }
}
