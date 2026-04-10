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

import java.time.LocalDate;
import java.util.List;

// WEEK 9 - JavaFX: TableView, TextArea, Button, VBox, HBox
// WEEK 5 - I/O: saveBillToFile triggers FileWriter/BufferedWriter
public class BillingScreen {

    private TableView<Booking> tableView;
    private TextArea billArea;
    private Label messageLabel;
    private Label totalRevenueLabel;
    private ObservableList<Booking> billingData;
    private BillingManager billingManager = new BillingManager();

    public VBox getView() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f0f4ff;");

        Label heading = new Label("Billing & Receipts");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a237e;");

        // WEEK 9 - HBox for table and bill display side by side
        HBox mainContent = new HBox(15);
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        // Left: Table of completed bookings
        VBox tableSection = buildTableSection();
        HBox.setHgrow(tableSection, Priority.ALWAYS);

        // Right: Bill display area
        VBox billSection = buildBillSection();
        billSection.setPrefWidth(380);

        mainContent.getChildren().addAll(tableSection, billSection);

        HBox buttonRow = buildButtonRow();

        // Total revenue label
        totalRevenueLabel = new Label("Today's Revenue: Rs. 0.00");
        totalRevenueLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2e7d32; " +
                                   "-fx-background-color: #e8f5e9; -fx-padding: 10; " +
                                   "-fx-background-radius: 8; -fx-border-color: #a5d6a7; -fx-border-radius: 8;");

        messageLabel = new Label("");
        messageLabel.setStyle("-fx-font-size: 13px;");

        root.getChildren().addAll(heading, mainContent, buttonRow, totalRevenueLabel, messageLabel);
        loadCompletedBookings();
        return root;
    }

    private VBox buildTableSection() {
        Label title = new Label("Completed Transactions");
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3949ab;");

        tableView = buildTable();
        VBox.setVgrow(tableView, Priority.ALWAYS);

        // WEEK 9 - Event: clicking a row shows bill
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) showBillDetails(selected);
        });

        VBox box = new VBox(10, title, tableView);
        box.setStyle("-fx-background-color: white; -fx-background-radius: 8; " +
                     "-fx-border-color: #c5cae9; -fx-border-radius: 8; -fx-padding: 10;");
        VBox.setVgrow(box, Priority.ALWAYS);
        return box;
    }

    private VBox buildBillSection() {
        Label title = new Label("Bill Preview");
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3949ab;");

        // WEEK 9 - TextArea component for displaying bill
        billArea = new TextArea();
        billArea.setEditable(false);
        billArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");
        billArea.setPrefHeight(350);
        billArea.setPromptText("Select a transaction to view its bill...");
        VBox.setVgrow(billArea, Priority.ALWAYS);

        VBox box = new VBox(10, title, billArea);
        box.setStyle("-fx-background-color: white; -fx-background-radius: 8; " +
                     "-fx-border-color: #c5cae9; -fx-border-radius: 8; -fx-padding: 10;");
        return box;
    }

    // WEEK 9 - TableView with Transaction ID, Customer ID, Date columns
    private TableView<Booking> buildTable() {
        TableView<Booking> table = new TableView<>();
        table.setStyle("-fx-background-color: white;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Booking, Integer> txnCol = new TableColumn<>("Txn ID");
        txnCol.setCellValueFactory(new PropertyValueFactory<>("bookingId"));

        TableColumn<Booking, String> custIdCol = new TableColumn<>("Cust. ID");
        custIdCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                String.valueOf(data.getValue().getCustomer().getCustomerId())));

        TableColumn<Booking, String> custCol = new TableColumn<>("Customer");
        custCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getCustomer().getCustomerName()));

        TableColumn<Booking, String> roomCol = new TableColumn<>("Room");
        roomCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                String.valueOf(data.getValue().getRoom().getRoomNumber())));

        TableColumn<Booking, String> dateCol = new TableColumn<>("Billing Date");
        dateCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(
                data.getValue().getCheckOutDate() != null
                    ? data.getValue().getCheckOutDate().toString()
                    : "N/A"));

        TableColumn<Booking, Double> billCol = new TableColumn<>("Total (Rs.)");
        billCol.setCellValueFactory(new PropertyValueFactory<>("totalBill"));

        table.getColumns().addAll(txnCol, custIdCol, custCol, roomCol, dateCol, billCol);
        billingData = FXCollections.observableArrayList();
        table.setItems(billingData);
        return table;
    }

    // WEEK 9 - HBox for buttons
    private HBox buildButtonRow() {
        Button saveBillBtn = new Button("💾 Save Bill to File");
        Button printAllBtn = new Button("📄 View All Bills");

        saveBillBtn.setStyle("-fx-background-color: #1565c0; -fx-text-fill: white; " +
                             "-fx-font-weight: bold; -fx-cursor: hand;");
        printAllBtn.setStyle("-fx-background-color: #4a148c; -fx-text-fill: white; -fx-cursor: hand;");

        // WEEK 5 - triggers FileWriter saving
        saveBillBtn.setOnAction(e -> handleSaveBill());
        printAllBtn.setOnAction(e -> loadCompletedBookings());

        HBox box = new HBox(10, saveBillBtn, printAllBtn);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    private void showBillDetails(Booking booking) {
        billArea.setText(billingManager.generateBill(booking));
    }

    // WEEK 5 - Event: Save bill via FileWriter (Character Stream)
    private void handleSaveBill() {
        Booking selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Please select a transaction to save bill.", false);
            return;
        }
        billingManager.saveBillToFile(selected); // WEEK 5 - FileWriter/BufferedWriter
        showMessage("Bill saved to bills/ folder for Transaction #" + selected.getBookingId(), true);
    }

    private void loadCompletedBookings() {
        List<Booking> completed = MainApp.bookingRepository.getCompletedBookings();
        billingData.setAll(completed);
        updateTodayRevenue(completed);
    }

    // Calculate and display today's total revenue
    private void updateTodayRevenue(List<Booking> completed) {
        LocalDate today = LocalDate.now();
        double todayTotal = 0.0;
        for (Booking b : completed) {
            if (b.getCheckOutDate() != null && b.getCheckOutDate().equals(today)) {
                todayTotal += b.getTotalBill();
            }
        }
        totalRevenueLabel.setText(String.format("Today's Revenue: Rs. %.2f", todayTotal));
    }

    public void refresh() {
        loadCompletedBookings();
    }

    private void showMessage(String msg, boolean success) {
        messageLabel.setText(msg);
        messageLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " +
                              (success ? "#2e7d32" : "#c62828") + ";");
    }
}