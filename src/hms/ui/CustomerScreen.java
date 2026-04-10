package hms.ui;
import hms.model.*;
import hms.repository.*;
import hms.billing.*;
import hms.MainApp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.util.List;

// WEEK 9 - JavaFX: Label, TextField, Button, TableView, GridPane, VBox, HBox
public class CustomerScreen {

    private TableView<Customer> tableView;
    private TextField nameField;
    private TextField contactField;
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

    // WEEK 9 - GridPane for customer form (no room selection — rooms are assigned via Bookings)
    private GridPane buildForm() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
        grid.setStyle("-fx-background-color: white; -fx-background-radius: 8; " +
                      "-fx-border-color: #c5cae9; -fx-border-radius: 8;");

        Label title = new Label("Register New Customer");
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3949ab;");
        grid.add(title, 0, 0, 4, 1);

        // WEEK 9 - Label + TextField components
        grid.add(new Label("Customer Name:"), 0, 1);
        nameField = new TextField();
        nameField.setPromptText("Full name");
        nameField.setPrefWidth(200);
        grid.add(nameField, 1, 1);

        grid.add(new Label("Contact No.:"), 2, 1);
        contactField = new TextField();
        contactField.setPromptText("10-digit number");
        contactField.setPrefWidth(200);
        grid.add(contactField, 3, 1);

        Button addBtn = new Button("➕ Register Customer");
        addBtn.setStyle("-fx-background-color: #3949ab; -fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-cursor: hand;");
        addBtn.setOnAction(e -> handleAddCustomer());
        grid.add(addBtn, 4, 1);

        return grid;
    }

    // WEEK 9 - HBox for action buttons
    private HBox buildButtonRow() {
        Button clearBtn = new Button("🔄 Clear Fields");
        Button deleteBtn = new Button("🗑 Delete Selected");

        clearBtn.setStyle("-fx-background-color: #757575; -fx-text-fill: white; -fx-cursor: hand;");
        deleteBtn.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-cursor: hand;");

        // WEEK 9 - Event handling
        clearBtn.setOnAction(e -> clearFields());
        deleteBtn.setOnAction(e -> handleDeleteCustomer());

        HBox box = new HBox(10, clearBtn, deleteBtn);
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

        table.getColumns().addAll(idCol, nameCol, contactCol);
        customerData = FXCollections.observableArrayList();
        table.setItems(customerData);
        return table;
    }

    // WEEK 9 - Event: Add customer with validation (no room assignment)
    private void handleAddCustomer() {
        String name = nameField.getText().trim();
        String contact = contactField.getText().trim();

        if (name.isEmpty() || contact.isEmpty()) {
            showMessage("Please fill in both name and contact number.", false);
            return;
        }

        // Name must contain only letters and spaces (no numbers allowed)
        if (!name.matches("[a-zA-Z ]+")) {
            showMessage("Customer name must contain only letters (no numbers).", false);
            return;
        }

        if (!contact.matches("\\d{10}")) {
            showMessage("Contact number must be exactly 10 digits.", false);
            return;
        }

        int newId = MainApp.customerRepository.getNextId();
        Customer customer = new Customer(newId, name, contact);
        MainApp.customerRepository.add(customer);

        // WEEK 6 - Save updated customers
        MainApp.fileStorage.saveCustomers(MainApp.customerRepository.getAll());

        showMessage("Customer '" + name + "' registered successfully! (ID: " + newId + ")", true);
        clearFields();
        loadCustomers();
    }

    private void handleDeleteCustomer() {
        Customer selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Please select a customer to delete.", false);
            return;
        }
        MainApp.customerRepository.remove(selected);
        MainApp.fileStorage.saveCustomers(MainApp.customerRepository.getAll());
        showMessage("Customer '" + selected.getCustomerName() + "' deleted.", true);
        loadCustomers();
    }

    private void loadCustomers() {
        List<Customer> customers = MainApp.customerRepository.getAllCustomers();
        customerData.setAll(customers);
    }

    private void clearFields() {
        nameField.clear();
        contactField.clear();
        messageLabel.setText("");
    }

    private void showMessage(String msg, boolean success) {
        messageLabel.setText(msg);
        messageLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " +
                              (success ? "#2e7d32" : "#c62828") + ";");
    }
}