import java.util.*;

/**
 * Book My Stay - Hotel Booking System
 *
 * Use Case 1 → 6 Implementation
 *
 * @author Vikki
 * @version 6.1
 */
public class BookMyStayApp {

    // ================= ROOM =================
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
            availability.put("Single Room", 2);
            availability.put("Double Room", 1);
            availability.put("Suite Room", 1);
        }

        public int getAvailability(String roomType) {
            return availability.getOrDefault(roomType, 0);
        }

        public void decreaseAvailability(String roomType) {
            availability.put(roomType, availability.get(roomType) - 1);
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
    }

    // ================= BOOKING QUEUE =================
    static class BookingQueue {
        private Queue<Reservation> queue = new LinkedList<>();

        public void addRequest(Reservation r) {
            queue.offer(r);
        }

        public Reservation getNextRequest() {
            return queue.poll(); // FIFO
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    // ================= BOOKING SERVICE =================
    static class BookingService {

        // Track allocated rooms
        private HashMap<String, Set<String>> allocatedRooms = new HashMap<>();

        // Process queue
        public void processBookings(BookingQueue queue, RoomInventory inventory) {

            System.out.println("\n--- Processing Bookings ---\n");

            while (!queue.isEmpty()) {

                Reservation req = queue.getNextRequest();
                String roomType = req.getRoomType();

                int available = inventory.getAvailability(roomType);

                if (available > 0) {

                    // Generate unique room ID
                    String roomId = generateRoomId(roomType);

                    // Ensure uniqueness
                    allocatedRooms.putIfAbsent(roomType, new HashSet<>());
                    Set<String> roomSet = allocatedRooms.get(roomType);

                    if (!roomSet.contains(roomId)) {

                        roomSet.add(roomId);

                        // Update inventory (atomic step)
                        inventory.decreaseAvailability(roomType);

                        System.out.println("Booking Confirmed!");
                        System.out.println("Guest: " + req.getGuestName());
                        System.out.println("Room Type: " + roomType);
                        System.out.println("Room ID: " + roomId);
                        System.out.println("--------------------------");

                    }

                } else {
                    System.out.println("Booking Failed (No Availability): " + req.getGuestName());
                }
            }
        }

        // Generate unique ID
        private String generateRoomId(String roomType) {
            return roomType.substring(0, 2).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 5);
        }
    }

    // ================= MAIN =================
    public static void main(String[] args) {

        System.out.println("=== Book My Stay v6.1 ===");

        // Inventory
        RoomInventory inventory = new RoomInventory();

        // Queue
        BookingQueue queue = new BookingQueue();

        // Add requests
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // should fail

        queue.addRequest(new Reservation("David", "Double Room"));
        queue.addRequest(new Reservation("Eve", "Suite Room"));

        // Process bookings
        BookingService service = new BookingService();
        service.processBookings(queue, inventory);

        System.out.println("\nApplication Finished.");
    }
}