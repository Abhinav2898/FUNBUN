package com.hotel;

import com.hotel.model.Room;
import com.hotel.model.RoomType;
import com.hotel.repository.BookingRepository;
import com.hotel.repository.CustomerRepository;
import com.hotel.repository.RoomRepository;
import com.hotel.storage.FileStorage;
import com.hotel.ui.MainDashboard;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

// WEEK 3 - Thread: Background loading with ProgressIndicator
// WEEK 9 - JavaFX Application entry point
public class MainApp extends Application {

    // Shared repositories (accessible by all screens)
    public static RoomRepository roomRepository = new RoomRepository();
    public static CustomerRepository customerRepository = new CustomerRepository();
    public static BookingRepository bookingRepository = new BookingRepository();
    public static FileStorage fileStorage = new FileStorage();

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        fileStorage.ensureDirectories();

        // WEEK 3 - Show loading screen while background thread loads data
        showSplashScreen(stage);
    }

    // WEEK 3 - Multithreading: splash shown while data loads in background Thread
    private void showSplashScreen(Stage stage) {
        // JavaFX loading UI
        ProgressIndicator progress = new ProgressIndicator();
        progress.setPrefSize(60, 60);

        Label title = new Label("GRAND HOTEL");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #1a237e;");

        Label subtitle = new Label(""); // Removed Management System text
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");

        Label loadingLabel = new Label("Loading data, please wait...");
        loadingLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #888;");

        VBox splashRoot = new VBox(15, title, subtitle, progress, loadingLabel);
        splashRoot.setAlignment(Pos.CENTER);
        splashRoot.setStyle("-fx-background-color: #f0f4ff; -fx-padding: 60;");

        Scene splashScene = new Scene(splashRoot, 500, 300);
        stage.setScene(splashScene);
        stage.setTitle("Grand Hotel - Loading...");
        stage.show();

        // WEEK 3 - Background Thread for loading file data
        fileStorage.loadAllDataInBackground(() -> {
            // This callback runs on JavaFX thread after background Thread finishes
            loadDataIntoRepositories();
            launchMainDashboard(stage);
        });
    }

    private void loadDataIntoRepositories() {
        // Load from files into repositories
        // WEEK 6 - Deserialized data restored into repositories
        roomRepository.setAll(fileStorage.loadRooms());
        customerRepository.setAll(fileStorage.loadCustomers());
        bookingRepository.setAll(fileStorage.loadBookings());

        // If no rooms exist yet, add default rooms
        if (roomRepository.getCount() == 0) {
            addDefaultRooms();
        }
    }

    private void addDefaultRooms() {
        // Add some default rooms on first run
        roomRepository.add(new Room(101, RoomType.SINGLE));
        roomRepository.add(new Room(102, RoomType.SINGLE));
        roomRepository.add(new Room(201, RoomType.DOUBLE));
        roomRepository.add(new Room(202, RoomType.DOUBLE));
        roomRepository.add(new Room(301, RoomType.DELUXE));
        roomRepository.add(new Room(302, RoomType.DELUXE));
        // Save defaults to file
        fileStorage.saveRooms(roomRepository.getAll());
    }

    private void launchMainDashboard(Stage stage) {
        // WEEK 9 - Launch the main JavaFX dashboard
        MainDashboard dashboard = new MainDashboard(stage);
        dashboard.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
