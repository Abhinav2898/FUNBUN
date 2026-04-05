# Grand Hotel Management System
### Java Mini Project | All Weeks Covered

---

## Project Structure

```
HotelManagementSystem/
├── .vscode/
│   ├── settings.json       <- VS Code Java config
│   ├── launch.json         <- Run configuration
│   └── tasks.json          <- Compile & Run tasks
├── src/
│   ├── styles.css          <- JavaFX CSS styling
│   └── com/hotel/
│       ├── MainApp.java
│       ├── model/
│       │   ├── RoomType.java
│       │   ├── Room.java
│       │   ├── Customer.java
│       │   └── Booking.java
│       ├── repository/
│       │   ├── Repository.java
│       │   ├── RoomRepository.java
│       │   ├── CustomerRepository.java
│       │   └── BookingRepository.java
│       ├── storage/
│       │   └── FileStorage.java
│       ├── billing/
│       │   └── BillingManager.java
│       └── ui/
│           ├── MainDashboard.java
│           ├── RoomScreen.java
│           ├── CustomerScreen.java
│           ├── BookingScreen.java
│           └── BillingScreen.java
├── bin/                    <- Compiled .class files go here (auto-created)
├── lib/                    <- PASTE YOUR JAVAFX .jar FILES HERE
├── data/                   <- Auto-created: stores .dat files (serialized data)
└── bills/                  <- Auto-created: stores bill .txt files
```

---

## Setup Instructions

### Step 1: Add JavaFX Libraries
1. Download JavaFX SDK from: https://gluonhq.com/products/javafx/
2. Extract it
3. Copy ALL `.jar` files from the `lib/` folder of JavaFX SDK
4. Paste them into this project's `lib/` folder

### Step 2: Open in VS Code
1. Open VS Code
2. File → Open Folder → Select `HotelManagementSystem`
3. Make sure "Extension Pack for Java" is installed in VS Code

### Step 3: Compile & Run
**Option A - Using VS Code Tasks:**
- Press `Ctrl + Shift + B` → Select "Compile"
- Then press `Ctrl + Shift + P` → "Run Task" → "Run"

**Option B - Using Terminal:**
```bash
# Compile
javac --module-path lib --add-modules javafx.controls,javafx.fxml -d bin -cp bin src/com/hotel/model/RoomType.java src/com/hotel/model/Room.java src/com/hotel/model/Customer.java src/com/hotel/model/Booking.java src/com/hotel/repository/Repository.java src/com/hotel/repository/RoomRepository.java src/com/hotel/repository/CustomerRepository.java src/com/hotel/repository/BookingRepository.java src/com/hotel/storage/FileStorage.java src/com/hotel/billing/BillingManager.java src/com/hotel/ui/RoomScreen.java src/com/hotel/ui/CustomerScreen.java src/com/hotel/ui/BookingScreen.java src/com/hotel/ui/BillingScreen.java src/com/hotel/ui/MainDashboard.java src/com/hotel/MainApp.java

# Run
java --module-path lib --add-modules javafx.controls,javafx.fxml -cp bin com.hotel.MainApp
```

**Option C - Using VS Code Run Button:**
- Open `MainApp.java`
- Click the ▶ Run button at the top right

---

## Syllabus Coverage

| Week | Topic | Where Used |
|------|-------|-----------|
| Week 2 | Wrapper Classes, Enum, Autoboxing | RoomType.java, Booking.java, BillingManager.java |
| Week 3 | Multithreading Basics | FileStorage.java → loadAllDataInBackground() |
| Week 4 | Synchronization | FileStorage.java → all synchronized save/load methods |
| Week 5 | I/O Streams (Character) | BillingManager.java → FileWriter + BufferedWriter |
| Week 6 | Serialization/Deserialization | FileStorage.java → ObjectOutputStream/InputStream |
| Week 7 | Generics | Repository.java → Repository\<T\> |
| Week 8 | Collections / List Interface | All Repositories → ArrayList, List |
| Week 9 | JavaFX GUI | All UI classes |

---

## Features (Full Marks Coverage)

1. **Basic Lab 9 features** — 5 marks
   - Room Management (Add, View, Filter)
   - Customer Management
   - Booking & Checkout
   - TableView, GridPane, VBox, HBox, ComboBox, Button, Label, TextField

2. **Permanent Storage (Files + Serialization)** — Weeks 5 & 6
   - Rooms, Customers, Bookings saved to `.dat` files
   - Data loads automatically on app start

3. **Screen Design with Styles & Layouts** — Week 9
   - CSS styling in `styles.css`
   - BorderPane + TabPane for multi-screen navigation
   - Different colors, fonts, and layouts per screen

4. **Billing Management** — Weeks 2 & 5
   - Auto-calculates total bill on checkout
   - Saves bill receipt to `bills/` as `.txt` file
   - Bill preview in the Billing screen

5. **Background Threading + Synchronization** — Weeks 3 & 4
   - Loading spinner while data loads in background Thread
   - All file operations use `synchronized` methods

---

## Data Persistence
- All data is automatically saved to the `data/` folder
- Files: `rooms.dat`, `customers.dat`, `bookings.dat`
- Data is restored every time you restart the app

## Bills
- Bills are saved to the `bills/` folder as `.txt` files
- Format: `Bill_Booking1_CustomerName.txt`
