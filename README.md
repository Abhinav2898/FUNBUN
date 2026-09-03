# Hotel Management System

A JavaFX-based desktop application for managing hotel rooms, customers, bookings, and billing through a centralized management interface.

## Features

- Room management and availability tracking
- Customer registration and management
- Hotel booking and checkout management
- Automatic bill generation
- Booking history and customer records
- Room type and pricing management
- Persistent local data storage

## Tech Stack

Java 23 | JavaFX 23 | Maven | OOP | File I/O | Serialization

## Architecture

```
UI Layer → Repository Layer → File Storage
                 ↓
            Model Classes
                 ↓
          Billing Manager
```

## Main Components

- Customer management
- Room management
- Booking management
- Billing system
- File-based persistence
- JavaFX dashboard

## How to Run

```bash
mvn clean javafx:run
```
