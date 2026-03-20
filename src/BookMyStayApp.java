import java.util.*;

/**
 * Book My Stay - Hotel Booking System
 *
 * UC10: Cancellation & Rollback (Integrated)
 *
 * @author Vikki
 * @version 10.1
 */
public class BookMyStayApp {

    // ================= INVENTORY =================
    static class RoomInventory {
        private HashMap<String, Integer> availability = new HashMap<>();

        public RoomInventory() {
            availability.put("Single Room", 1);
            availability.put("Double Room", 1);
        }

        public int getAvailability(String type) {
            return availability.getOrDefault(type, 0);
        }

        public void decrease(String type) {
            availability.put(type, availability.get(type) - 1);
        }

        public void increase(String type) {
            availability.put(type, availability.get(type) + 1);
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

    // ================= HISTORY =================
    static class BookingHistory {
        private List<Reservation> history = new ArrayList<>();

        public void add(Reservation r) { history.add(r); }

        public Reservation findById(String id) {
            for (Reservation r : history) {
                if (r.getReservationId().equals(id)) return r;
            }
            return null;
        }

        public void remove(Reservation r) {
            history.remove(r);
        }
    }

    // ================= BOOKING SERVICE =================
    static class BookingService {

        private HashMap<String, Set<String>> allocated = new HashMap<>();

        public void book(Reservation r, RoomInventory inventory, BookingHistory history) {

            String type = r.getRoomType();

            if (inventory.getAvailability(type) <= 0) {
                System.out.println("Booking Failed → No availability for " + type);
                return;
            }

            String id = generateId(type);

            allocated.putIfAbsent(type, new HashSet<>());
            allocated.get(type).add(id);

            inventory.decrease(type);

            r.setReservationId(id);
            history.add(r);

            System.out.println("CONFIRMED → " + r.getGuestName() + " | " + id);
        }

        private String generateId(String type) {
            return type.substring(0, 2).toUpperCase() + "-" +
                    UUID.randomUUID().toString().substring(0, 5);
        }
    }

    // ================= CANCELLATION SERVICE =================
    static class CancellationService {

        private Stack<String> rollbackStack = new Stack<>();

        public void cancel(String reservationId,
                           BookingHistory history,
                           RoomInventory inventory) {

            Reservation r = history.findById(reservationId);

            // ✅ Validation
            if (r == null) {
                System.out.println("CANCEL FAILED → Reservation not found: " + reservationId);
                return;
            }

            String type = r.getRoomType();

            // ✅ LIFO rollback tracking
            rollbackStack.push(reservationId);

            // ✅ Restore inventory
            inventory.increase(type);

            // ✅ Remove booking
            history.remove(r);

            System.out.println("CANCELLED → " + reservationId);
        }

        public void showRollbackStack() {
            System.out.println("\nRollback Stack (LIFO): " + rollbackStack);
        }
    }

    // ================= MAIN =================
    public static void main(String[] args) {

        System.out.println("=== Book My Stay v10.1 ===");

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        BookingService bookingService = new BookingService();
        CancellationService cancelService = new CancellationService();

        // BOOKINGS
        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Double Room");

        bookingService.book(r1, inventory, history);
        bookingService.book(r2, inventory, history);

        // STORE IDs
        String id1 = r1.getReservationId();
        String id2 = r2.getReservationId();

        // CANCELLATIONS
        cancelService.cancel(id1, history, inventory); // valid
        cancelService.cancel("INVALID-ID", history, inventory); // invalid

        cancelService.showRollbackStack();

        System.out.println("\nApplication Finished.");
    }
}