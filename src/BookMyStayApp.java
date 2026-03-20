import java.util.*;

/**
 * Book My Stay - Hotel Booking System
 *
 * Use Case 1 → 5 Implementation
 *
 * @author Vikki
 * @version 5.1
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
    }

    // ================= SEARCH SERVICE =================
    static class RoomSearchService {
        public void searchAvailableRooms(RoomInventory inventory, Room[] rooms) {

            System.out.println("\n--- Available Rooms ---\n");

            for (Room room : rooms) {
                int available = inventory.getAvailability(room.getRoomType());

                if (available > 0) {
                    room.displayRoomDetails();
                    System.out.println("Available: " + available);
                    System.out.println("----------------------");
                }
            }
        }
    }

    // ================= RESERVATION =================
    static class Reservation {
        private String guestName;
        private String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }

        public void displayReservation() {
            System.out.println("Guest: " + guestName + " | Requested: " + roomType);
        }
    }

    // ================= BOOKING QUEUE =================
    static class BookingQueue {

        private Queue<Reservation> queue;

        public BookingQueue() {
            queue = new LinkedList<>();
        }

        // Add booking request
        public void addRequest(Reservation reservation) {
            queue.offer(reservation);
            System.out.println("Request Added: " + reservation.getGuestName());
        }

        // Display queue (FIFO order)
        public void displayQueue() {
            System.out.println("\n--- Booking Request Queue (FIFO) ---\n");

            for (Reservation r : queue) {
                r.displayReservation();
            }
        }
    }

    // ================= MAIN =================
    public static void main(String[] args) {

        String appName = "Book My Stay";
        String version = "v5.1";

        System.out.println("=================================");
        System.out.println(" Welcome to " + appName);
        System.out.println(" Version: " + version);
        System.out.println("=================================");

        // Rooms
        Room[] rooms = {
                new SingleRoom(),
                new DoubleRoom(),
                new SuiteRoom()
        };

        // Inventory
        RoomInventory inventory = new RoomInventory();

        // Search (read-only)
        RoomSearchService search = new RoomSearchService();
        search.searchAvailableRooms(inventory, rooms);

        // ================= UC5 LOGIC =================

        BookingQueue bookingQueue = new BookingQueue();

        // Simulating booking requests
        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room"));

        // Display queue
        bookingQueue.displayQueue();

        System.out.println("\nApplication Finished.");
    }
}