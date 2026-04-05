package com.hotel.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

// WEEK 9 - JavaFX: BorderPane, TabPane, multiple screens
public class MainDashboard {

    private Stage stage;
    private TabPane tabPane;
    private RoomScreen roomScreen;
    private CustomerScreen customerScreen;
    private BookingScreen bookingScreen;
    private BillingScreen billingScreen;

    public MainDashboard(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        // WEEK 9 - BorderPane layout for top nav + center content
        BorderPane root = new BorderPane();
        root.setTop(buildNavBar());
        root.setCenter(buildTabPane());
        root.setStyle("-fx-background-color: #f0f4ff;");

        Scene scene = new Scene(root, 1000, 680);

        stage.setScene(scene);
        stage.setTitle("Grand Hotel");
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    // WEEK 9 - HBox layout for the navigation header
    private HBox buildNavBar() {
        Label hotelName = new Label("🏨 GRAND HOTEL");
        hotelName.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; " +
                           "-fx-text-fill: white; -fx-padding: 5 0 5 0;");

        HBox nav = new HBox(hotelName);
        nav.setAlignment(Pos.CENTER_LEFT);
        nav.setPadding(new Insets(15, 25, 15, 25));
        nav.setStyle("-fx-background-color: linear-gradient(to right, #1a237e, #283593);");
        return nav;
    }

    // WEEK 9 - TabPane for screen switching (multiple layouts)
    private TabPane buildTabPane() {
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-background-color: #f0f4ff;");

        // Create screens
        roomScreen = new RoomScreen();
        customerScreen = new CustomerScreen();
        bookingScreen = new BookingScreen();
        billingScreen = new BillingScreen();

        // WEEK 9 - Tab for each screen
        Tab roomTab = new Tab("🛏 Rooms", roomScreen.getView());
        Tab customerTab = new Tab("👤 Customers", customerScreen.getView());
        Tab bookingTab = new Tab("📋 Bookings", bookingScreen.getView());
        Tab billingTab = new Tab("💰 Billing", billingScreen.getView());

        tabPane.getTabs().addAll(roomTab, customerTab, bookingTab, billingTab);

        // Refresh booking screen when switching to it
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == bookingTab) bookingScreen.refresh();
            if (newTab == billingTab) billingScreen.refresh();
            if (newTab == roomTab) roomScreen.refresh();
        });

        return tabPane;
    }


}
