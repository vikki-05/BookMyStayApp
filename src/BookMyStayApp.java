import java.util.HashMap;

/**
 * Book My Stay - Hotel Booking System
 *
 * Use Case 1 + 2 + 3 Implementation
 *
 * @author Vikki
 * @version 3.1
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

        public String getRoomType() {
            return roomType;
        }

        public int getBeds() {
            return beds;
        }

        public double getPrice() {
            return price;
        }

        public abstract void displayRoomDetails();
    }

    // ================= ROOM TYPES =================
    static class SingleRoom extends Room {
        public SingleRoom() {
            super("Single Room", 1, 1000);
        }

        public void displayRoomDetails() {
            System.out.println("Room Type: " + getRoomType());
            System.out.println("Beds: " + getBeds());
            System.out.println("Price: ₹" + getPrice());
        }
    }

    static class DoubleRoom extends Room {
        public DoubleRoom() {
            super("Double Room", 2, 2000);
        }

        public void displayRoomDetails() {
            System.out.println("Room Type: " + getRoomType());
            System.out.println("Beds: " + getBeds());
            System.out.println("Price: ₹" + getPrice());
        }
    }

    static class SuiteRoom extends Room {
        public SuiteRoom() {
            super("Suite Room", 3, 5000);
        }

        public void displayRoomDetails() {
            System.out.println("Room Type: " + getRoomType());
            System.out.println("Beds: " + getBeds());
            System.out.println("Price: ₹" + getPrice());
        }
    }

    // ================= INVENTORY CLASS =================
    static class RoomInventory {

        private HashMap<String, Integer> availability;

        // Constructor → initialize inventory
        public RoomInventory() {
            availability = new HashMap<>();

            availability.put("Single Room", 5);
            availability.put("Double Room", 3);
            availability.put("Suite Room", 2);
        }

        // Get availability
        public int getAvailability(String roomType) {
            return availability.getOrDefault(roomType, 0);
        }

        // Update availability
        public void updateAvailability(String roomType, int newCount) {
            availability.put(roomType, newCount);
        }

        // Display all inventory
        public void displayInventory() {
            System.out.println("\n--- Centralized Room Inventory ---\n");

            for (String roomType : availability.keySet()) {
                System.out.println(roomType + " Available: " + availability.get(roomType));
            }
        }
    }

    // ================= MAIN METHOD =================
    public static void main(String[] args) {

        String appName = "Book My Stay";
        String version = "v3.1";

        System.out.println("=================================");
        System.out.println(" Welcome to " + appName);
        System.out.println(" Version: " + version);
        System.out.println("=================================");

        // Room Objects (Polymorphism)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        System.out.println("\n--- Room Details ---\n");

        single.displayRoomDetails();
        System.out.println("---------------------");

        doubleRoom.displayRoomDetails();
        System.out.println("---------------------");

        suite.displayRoomDetails();
        System.out.println("---------------------");

        // ================= UC3 LOGIC =================
        RoomInventory inventory = new RoomInventory();

        inventory.displayInventory();

        // Example update (controlled update)
        inventory.updateAvailability("Single Room", 4);

        System.out.println("\nAfter Update:");
        inventory.displayInventory();

        System.out.println("\nApplication Finished.");
    }
}