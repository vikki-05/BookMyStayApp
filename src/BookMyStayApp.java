import java.util.HashMap;

/**
 * Book My Stay - Hotel Booking System
 *
 * Use Case 1,2,3,4 Implementation
 *
 * @author Vikki
 * @version 4.1
 */
public class BookMyStayApp {

    // ================= ABSTRACT ROOM =================
    abstract static class Room {
        private String roomType;
        private int beds;
        private double price;

        public Room(String roomType, int beds, double price) {
            this.roomType = roomType;
            this.beds = beds;
            this.price = price;
        }

        public String getRoomType() { return roomType; }
        public int getBeds() { return beds; }
        public double getPrice() { return price; }

        public abstract void displayRoomDetails();
    }

    // ================= ROOM TYPES =================
    static class SingleRoom extends Room {
        public SingleRoom() { super("Single Room", 1, 1000); }
        public void displayRoomDetails() {
            System.out.println("Room Type: " + getRoomType());
            System.out.println("Beds: " + getBeds());
            System.out.println("Price: ₹" + getPrice());
        }
    }

    static class DoubleRoom extends Room {
        public DoubleRoom() { super("Double Room", 2, 2000); }
        public void displayRoomDetails() {
            System.out.println("Room Type: " + getRoomType());
            System.out.println("Beds: " + getBeds());
            System.out.println("Price: ₹" + getPrice());
        }
    }

    static class SuiteRoom extends Room {
        public SuiteRoom() { super("Suite Room", 3, 5000); }
        public void displayRoomDetails() {
            System.out.println("Room Type: " + getRoomType());
            System.out.println("Beds: " + getBeds());
            System.out.println("Price: ₹" + getPrice());
        }
    }

    // ================= INVENTORY =================
    static class RoomInventory {

        private HashMap<String, Integer> availability;

        public RoomInventory() {
            availability = new HashMap<>();
            availability.put("Single Room", 5);
            availability.put("Double Room", 3);
            availability.put("Suite Room", 2);
        }

        public int getAvailability(String roomType) {
            return availability.getOrDefault(roomType, 0);
        }

        public void updateAvailability(String roomType, int newCount) {
            availability.put(roomType, newCount);
        }

        public HashMap<String, Integer> getAllAvailability() {
            return availability; // read-only usage expected
        }
    }

    // ================= SEARCH SERVICE =================
    static class RoomSearchService {

        public void searchAvailableRooms(RoomInventory inventory, Room[] rooms) {

            System.out.println("\n--- Available Rooms (Search Result) ---\n");

            for (Room room : rooms) {

                int available = inventory.getAvailability(room.getRoomType());

                //  Validation (filter unavailable rooms)
                if (available > 0) {

                    room.displayRoomDetails();
                    System.out.println("Available: " + available);
                    System.out.println("---------------------------");
                }
            }
        }
    }

    // ================= MAIN =================
    public static void main(String[] args) {

        String appName = "Book My Stay";
        String version = "v4.1";

        System.out.println("=================================");
        System.out.println(" Welcome to " + appName);
        System.out.println(" Version: " + version);
        System.out.println("=================================");

        // Room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        Room[] rooms = {single, doubleRoom, suite};

        // Inventory (central state)
        RoomInventory inventory = new RoomInventory();

        // Search Service (READ ONLY)
        RoomSearchService searchService = new RoomSearchService();

        // Perform search (NO STATE CHANGE)
        searchService.searchAvailableRooms(inventory, rooms);

        System.out.println("\nApplication Finished.");
    }
}