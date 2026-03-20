import java.util.*;

/**
 * Book My Stay - Hotel Booking System
 *
 * Use Case 1 → 8 Implementation
 *
 * @author Vikki
 * @version 8.1
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
            System.out.println(getRoomType() + " | Beds: " + getBeds() + " | ₹" + getPrice());
        }
    }

    static class DoubleRoom extends Room {
        public DoubleRoom() { super("Double Room", 2, 2000); }
        public void displayRoomDetails() {
            System.out.println(getRoomType() + " | Beds: " + getBeds() + " | ₹" + getPrice());
        }
    }

    static class SuiteRoom extends Room {
        public SuiteRoom() { super("Suite Room", 3, 5000); }
        public void displayRoomDetails() {
            System.out.println(getRoomType() + " | Beds: " + getBeds() + " | ₹" + getPrice());
        }
    }

    // ================= INVENTORY =================
    static class RoomInventory {
        private HashMap<String, Integer> availability = new HashMap<>();

        public RoomInventory() {
            availability.put("Single Room", 2);
            availability.put("Double Room", 1);
            availability.put("Suite Room", 1);
        }

        public int getAvailability(String type) {
            return availability.getOrDefault(type, 0);
        }

        public void decreaseAvailability(String type) {
            availability.put(type, availability.get(type) - 1);
        }
    }

    // ================= RESERVATION =================
    static class Reservation {
        private String guestName;
        private String roomType;
        private String reservationId;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }

        public void setReservationId(String id) { this.reservationId = id; }
        public String getReservationId() { return reservationId; }
    }

    // ================= QUEUE =================
    static class BookingQueue {
        private Queue<Reservation> queue = new LinkedList<>();

        public void addRequest(Reservation r) { queue.offer(r); }
        public Reservation getNext() { return queue.poll(); }
        public boolean isEmpty() { return queue.isEmpty(); }
    }

    // ================= HISTORY =================
    static class BookingHistory {
        private List<Reservation> history = new ArrayList<>();

        public void add(Reservation r) {
            history.add(r);
        }

        public List<Reservation> getAll() {
            return history;
        }
    }

    // ================= REPORT SERVICE =================
    static class BookingReportService {

        public void showAllBookings(List<Reservation> history) {

            System.out.println("\n--- Booking History ---");

            for (Reservation r : history) {
                System.out.println(
                        r.getReservationId() + " | " +
                                r.getGuestName() + " | " +
                                r.getRoomType()
                );
            }
        }

        public void showSummary(List<Reservation> history) {

            HashMap<String, Integer> summary = new HashMap<>();

            for (Reservation r : history) {
                summary.put(r.getRoomType(),
                        summary.getOrDefault(r.getRoomType(), 0) + 1);
            }

            System.out.println("\n--- Booking Summary ---");

            for (String type : summary.keySet()) {
                System.out.println(type + ": " + summary.get(type));
            }
        }
    }

    // ================= BOOKING SERVICE =================
    static class BookingService {

        private HashMap<String, Set<String>> allocated = new HashMap<>();

        public void process(BookingQueue queue,
                            RoomInventory inventory,
                            BookingHistory history) {

            while (!queue.isEmpty()) {

                Reservation r = queue.getNext();
                String type = r.getRoomType();

                if (inventory.getAvailability(type) > 0) {

                    String id = generateId(type);

                    allocated.putIfAbsent(type, new HashSet<>());

                    if (!allocated.get(type).contains(id)) {

                        allocated.get(type).add(id);
                        inventory.decreaseAvailability(type);

                        r.setReservationId(id);

                        history.add(r); // 🔥 UC8

                        System.out.println("CONFIRMED → " +
                                r.getGuestName() + " | " + id);
                    }

                } else {
                    System.out.println("FAILED → " + r.getGuestName());
                }
            }
        }

        private String generateId(String type) {
            return type.substring(0, 2).toUpperCase() + "-" +
                    UUID.randomUUID().toString().substring(0, 5);
        }
    }

    // ================= MAIN =================
    public static void main(String[] args) {

        System.out.println("=== Book My Stay v8.1 ===");

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();
        BookingHistory history = new BookingHistory();

        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room"));
        queue.addRequest(new Reservation("David", "Suite Room"));

        BookingService service = new BookingService();
        service.process(queue, inventory, history);

        // ================= REPORT =================
        BookingReportService report = new BookingReportService();

        report.showAllBookings(history.getAll());
        report.showSummary(history.getAll());

        System.out.println("\nApplication Finished.");
    }
}