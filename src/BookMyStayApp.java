import java.util.*;

/**
 * Book My Stay - Hotel Booking System
 *
 * Use Case 1 → 9 Implementation
 *
 * @author Vikki
 * @version 9.1
 */
public class BookMyStayApp {

    // ================= CUSTOM EXCEPTIONS =================
    static class InvalidRoomTypeException extends Exception {
        public InvalidRoomTypeException(String message) {
            super(message);
        }
    }

    static class NoAvailabilityException extends Exception {
        public NoAvailabilityException(String message) {
            super(message);
        }
    }

    // ================= VALIDATOR =================
    static class BookingValidator {

        private static final Set<String> validRoomTypes = new HashSet<>(
                Arrays.asList("Single Room", "Double Room", "Suite Room")
        );

        public static void validate(String roomType, int availability)
                throws InvalidRoomTypeException, NoAvailabilityException {

            // Validate room type
            if (!validRoomTypes.contains(roomType)) {
                throw new InvalidRoomTypeException(
                        "Invalid Room Type: " + roomType
                );
            }

            // Validate availability
            if (availability <= 0) {
                throw new NoAvailabilityException(
                        "No rooms available for: " + roomType
                );
            }
        }
    }

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
    }

    static class SingleRoom extends Room {
        public SingleRoom() { super("Single Room", 1, 1000); }
    }

    static class DoubleRoom extends Room {
        public DoubleRoom() { super("Double Room", 2, 2000); }
    }

    static class SuiteRoom extends Room {
        public SuiteRoom() { super("Suite Room", 3, 5000); }
    }

    // ================= INVENTORY =================
    static class RoomInventory {
        private HashMap<String, Integer> availability = new HashMap<>();

        public RoomInventory() {
            availability.put("Single Room", 1);
            availability.put("Double Room", 1);
            availability.put("Suite Room", 0);
        }

        public int getAvailability(String type) {
            return availability.getOrDefault(type, 0);
        }

        public void decreaseAvailability(String type) {
            int current = availability.get(type);

            if (current <= 0) {
                throw new IllegalStateException("Inventory went negative!");
            }

            availability.put(type, current - 1);
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

        public void add(Reservation r) { history.add(r); }
        public List<Reservation> getAll() { return history; }
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

                try {
                    // 🔥 VALIDATION (Fail Fast)
                    BookingValidator.validate(type, inventory.getAvailability(type));

                    String id = generateId(type);

                    allocated.putIfAbsent(type, new HashSet<>());

                    if (!allocated.get(type).contains(id)) {

                        allocated.get(type).add(id);
                        inventory.decreaseAvailability(type);

                        r.setReservationId(id);
                        history.add(r);

                        System.out.println("CONFIRMED → " +
                                r.getGuestName() + " | " + id);
                    }

                } catch (InvalidRoomTypeException | NoAvailabilityException e) {

                    System.out.println("ERROR → " + r.getGuestName()
                            + " | " + e.getMessage());

                } catch (Exception e) {
                    System.out.println("SYSTEM ERROR → " + e.getMessage());
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

        System.out.println("=== Book My Stay v9.1 ===");

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();
        BookingHistory history = new BookingHistory();

        // Valid + Invalid inputs
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Suite Room")); // no availability
        queue.addRequest(new Reservation("Charlie", "Deluxe Room")); // invalid
        queue.addRequest(new Reservation("David", "Double Room"));

        BookingService service = new BookingService();
        service.process(queue, inventory, history);

        System.out.println("\nApplication Finished Safely.");
    }
}