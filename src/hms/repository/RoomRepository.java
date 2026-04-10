package hms.repository;
import hms.model.*;
import hms.storage.*;


import java.util.ArrayList;
import java.util.List;

// WEEK 7 - Extends Generic Repository<Room>
// WEEK 8 - Uses Collections / List Interface methods
public class RoomRepository extends Repository<Room> {

    // WEEK 8 - Using List and iteration
    public List<Room> getAvailableRooms() {
        List<Room> available = new ArrayList<>();
        for (Room room : items) {  // WEEK 8 - enhanced for loop on List
            if (room.isAvailable()) {
                available.add(room);
            }
        }
        return available;
    }

    public Room getRoomByNumber(int roomNumber) {
        for (Room room : items) {
            // WEEK 2 - Integer comparison using wrapper
            if (Integer.compare(room.getRoomNumber(), roomNumber) == 0) {
                return room;
            }
        }
        return null;
    }

    public void markRoomOccupied(int roomNumber) {
        Room room = getRoomByNumber(roomNumber);
        if (room != null) {
            room.setAvailable(false);
        }
    }

    public void markRoomAvailable(int roomNumber) {
        Room room = getRoomByNumber(roomNumber);
        if (room != null) {
            room.setAvailable(true);
        }
    }

    public boolean roomExists(int roomNumber) {
        return getRoomByNumber(roomNumber) != null;
    }
}