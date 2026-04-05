package com.hotel.ui;

import com.hotel.MainApp;
import com.hotel.model.Room;
import com.hotel.model.RoomType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

// WEEK 9 - JavaFX: TableView, GridPane, VBox, HBox, ComboBox, Button, Label, TextField
public class RoomScreen {

    private TableView<Room> tableView;
    private TextField roomNumberField;
    private ComboBox<RoomType> roomTypeCombo;
    private Label messageLabel;
    private ObservableList<Room> roomData;
    private boolean showOnlyAvailable = false;

    public VBox getView() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f0f4ff;");

        Label heading = new Label("Room Management");
        heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a237e;");

        // WEEK 9 - GridPane for form layout
        GridPane form = buildAddRoomForm();

        // WEEK 9 - HBox for buttons
        HBox buttonRow = buildButtonRow();

        messageLabel = new Label("");
        messageLabel.setStyle("-fx-font-size: 13px;");

        // WEEK 9 - TableView to display rooms
        tableView = buildRoomTable();
        VBox.setVgrow(tableView, Priority.ALWAYS);

        root.getChildren().addAll(heading, form, buttonRow, messageLabel, tableView);
        loadAllRooms();
        return root;
    }

    // WEEK 9 - GridPane layout for the Add Room form
    private GridPane buildAddRoomForm() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));
        grid.setStyle("-fx-background-color: white; -fx-background-radius: 8; " +
                      "-fx-border-color: #c5cae9; -fx-border-radius: 8;");

        Label formTitle = new Label("Add New Room");
        formTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3949ab;");
        grid.add(formTitle, 0, 0, 2, 1);

        // WEEK 9 - Label and TextField
        grid.add(new Label("Room Number:"), 0, 1);
        roomNumberField = new TextField();
        roomNumberField.setPromptText("e.g. 103");
        roomNumberField.setPrefWidth(150);
        grid.add(roomNumberField, 1, 1);

        grid.add(new Label("Room Type:"), 2, 1);
        // WEEK 9 - ComboBox with Enum values (Week 2 - Enum)
        roomTypeCombo = new ComboBox<>(FXCollections.observableArrayList(RoomType.values()));
        roomTypeCombo.setPromptText("Select type");
        roomTypeCombo.setPrefWidth(150);
        grid.add(roomTypeCombo, 3, 1);

        Button addBtn = new Button("➕ Add Room");
        addBtn.setStyle("-fx-background-color: #3949ab; -fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-cursor: hand;");
        // WEEK 9 - Event handling
        addBtn.setOnAction(e -> handleAddRoom());
        grid.add(addBtn, 4, 1);

        return grid;
    }

    // WEEK 9 - HBox for filter buttons
    private HBox buildButtonRow() {
        Button showAllBtn = new Button("📋 Show All Rooms");
        Button showAvailableBtn = new Button("✅ Show Available Only");
        Button deleteBtn = new Button("🗑 Delete Selected");

        showAllBtn.setStyle("-fx-background-color: #5c6bc0; -fx-text-fill: white; -fx-cursor: hand;");
        showAvailableBtn.setStyle("-fx-background-color: #43a047; -fx-text-fill: white; -fx-cursor: hand;");
        deleteBtn.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-cursor: hand;");

        // WEEK 9 - Event handling for buttons
        showAllBtn.setOnAction(e -> { showOnlyAvailable = false; loadAllRooms(); });
        showAvailableBtn.setOnAction(e -> { showOnlyAvailable = true; loadAvailableRooms(); });
        deleteBtn.setOnAction(e -> handleDeleteRoom());

        HBox box = new HBox(10, showAllBtn, showAvailableBtn, deleteBtn);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    // WEEK 9 - TableView with columns
    private TableView<Room> buildRoomTable() {
        TableView<Room> table = new TableView<>();
        table.setStyle("-fx-background-color: white;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Room, Integer> numCol = new TableColumn<>("Room No.");
        numCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));

        TableColumn<Room, String> typeCol = new TableColumn<>("Room Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("roomType"));

        TableColumn<Room, Double> priceCol = new TableColumn<>("Price/Day (Rs.)");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("pricePerDay"));

        TableColumn<Room, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("availabilityStatus"));

        // Color cells based on availability
        statusCol.setCellFactory(col -> new TableCell<Room, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("Available".equals(item)) {
                        setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #c62828; -fx-font-weight: bold;");
                    }
                }
            }
        });

        table.getColumns().addAll(numCol, typeCol, priceCol, statusCol);
        roomData = FXCollections.observableArrayList();
        table.setItems(roomData);
        return table;
    }

    // WEEK 9 - Event handling: Add room button
    private void handleAddRoom() {
        String roomNumStr = roomNumberField.getText().trim();
        RoomType selectedType = roomTypeCombo.getValue();

        if (roomNumStr.isEmpty() || selectedType == null) {
            showMessage("Please fill all fields.", false);
            return;
        }

        try {
            // WEEK 2 - Integer.parseInt is a Wrapper class method
            int roomNum = Integer.parseInt(roomNumStr);

            if (MainApp.roomRepository.roomExists(roomNum)) {
                showMessage("Room " + roomNum + " already exists!", false);
                return;
            }

            Room newRoom = new Room(roomNum, selectedType);
            MainApp.roomRepository.add(newRoom);
            MainApp.fileStorage.saveRooms(MainApp.roomRepository.getAll()); // WEEK 6 - Save

            showMessage("Room " + roomNum + " added successfully!", true);
            clearFields();
            loadAllRooms();

        } catch (NumberFormatException ex) {
            showMessage("Room number must be a valid integer.", false);
        }
    }

    private void handleDeleteRoom() {
        Room selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showMessage("Please select a room to delete.", false);
            return;
        }
        if (!selected.isAvailable()) {
            showMessage("Cannot delete an occupied room!", false);
            return;
        }
        MainApp.roomRepository.remove(selected);
        MainApp.fileStorage.saveRooms(MainApp.roomRepository.getAll());
        showMessage("Room " + selected.getRoomNumber() + " deleted.", true);
        loadAllRooms();
    }

    private void loadAllRooms() {
        List<Room> rooms = MainApp.roomRepository.getAll();
        roomData.setAll(rooms);
    }

    private void loadAvailableRooms() {
        List<Room> available = MainApp.roomRepository.getAvailableRooms();
        roomData.setAll(available);
    }

    public void refresh() {
        if (showOnlyAvailable) loadAvailableRooms();
        else loadAllRooms();
    }

    private void clearFields() {
        roomNumberField.clear();
        roomTypeCombo.setValue(null);
    }

    private void showMessage(String msg, boolean success) {
        messageLabel.setText(msg);
        messageLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " +
                              (success ? "#2e7d32" : "#c62828") + ";");
    }
}
