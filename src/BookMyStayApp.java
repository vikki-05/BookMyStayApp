import java.util.*;

/**
 * Book My Stay - Hotel Booking System
 *
 * UC11: Concurrent Booking Simulation (Thread Safety)
 *
 * @author Vikki
 * @version 11.0
 */
public class BookMyStayApp {

    // ================= INVENTORY =================
    static class RoomInventory {
        private HashMap<String, Integer> availability = new HashMap<>();

        public RoomInventory() {
            availability.put("Single Room", 1);
        }

        // 🔒 synchronized → critical section
        public synchronized boolean bookRoom(String type) {

            int available = availability.getOrDefault(type, 0);

            if (available > 0) {
                System.out.println(Thread.currentThread().getName() +
                        " BOOKED " + type);

                availability.put(type, available - 1);
                return true;
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " FAILED (No availability)");

                return false;
            }
        }

        public void showAvailability() {
            System.out.println("Final Inventory: " + availability);
        }
    }

    // ================= RESERVATION =================
    static class Reservation {
        String guestName;
        String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }
    }

    // ================= SHARED QUEUE =================
    static class BookingQueue {
        private Queue<Reservation> queue = new LinkedList<>();

        public synchronized void addRequest(Reservation r) {
            queue.add(r);
        }

        public synchronized Reservation getRequest() {
            return queue.poll();
        }
    }

    // ================= THREAD (PROCESSOR) =================
    static class BookingProcessor extends Thread {

        private BookingQueue queue;
        private RoomInventory inventory;

        public BookingProcessor(String name, BookingQueue queue, RoomInventory inventory) {
            super(name);
            this.queue = queue;
            this.inventory = inventory;
        }

        @Override
        public void run() {

            Reservation r;

            while ((r = queue.getRequest()) != null) {

                // 🔒 critical section handled inside inventory
                inventory.bookRoom(r.roomType);

                try {
                    Thread.sleep(100); // simulate delay
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ================= MAIN =================
    public static void main(String[] args) {

        System.out.println("=== Book My Stay v11.0 ===");

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        // 🔥 Multiple guests (simultaneous requests)
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room"));

        // 🔥 Multiple threads
        Thread t1 = new BookingProcessor("Thread-1", queue, inventory);
        Thread t2 = new BookingProcessor("Thread-2", queue, inventory);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        inventory.showAvailability();

        System.out.println("Application Finished.");
    }
}