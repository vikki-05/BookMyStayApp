import java.util.*;

/**
 * Book My Stay - Hotel Booking System
 *
 * Use Case 1 → 7 Implementation
 *
 * @author Vikki
 * @version 7.1
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
    }

    // ================= BOOKING QUEUE =================
    static class BookingQueue {
        private Queue<Reservation> queue = new LinkedList<>();

        public void addRequest(Reservation r) {
            queue.offer(r);
        }

        public Reservation getNextRequest() {
            return queue.poll();
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    // ================= BOOKING SERVICE =================
    static class BookingService {

        private HashMap<String, Set<String>> allocatedRooms = new HashMap<>();

        public void processBookings(BookingQueue queue, RoomInventory inventory) {

            System.out.println("\n--- Processing Bookings ---\n");

            while (!queue.isEmpty()) {

                Reservation req = queue.getNextRequest();
                String roomType = req.getRoomType();

                int available = inventory.getAvailability(roomType);

                if (available > 0) {

                    String roomId = generateRoomId(roomType);

                    allocatedRooms.putIfAbsent(roomType, new HashSet<>());
                    Set<String> roomSet = allocatedRooms.get(roomType);

                    if (!roomSet.contains(roomId)) {

                        roomSet.add(roomId);
                        inventory.decreaseAvailability(roomType);

                        System.out.println("Booking Confirmed!");
                        System.out.println("Guest: " + req.getGuestName());
                        System.out.println("Room Type: " + roomType);
                        System.out.println("Reservation ID: " + roomId);
                        System.out.println("--------------------------");
                    }

                } else {
                    System.out.println("Booking Failed (No Availability): " + req.getGuestName());
                }
            }
        }

        private String generateRoomId(String roomType) {
            return roomType.substring(0, 2).toUpperCase() + "-" +
                    UUID.randomUUID().toString().substring(0, 5);
        }
    }

    // ================= ADD-ON SERVICE =================
    static class AddOnService {
        private String serviceName;
        private double price;

        public AddOnService(String serviceName, double price) {
            this.serviceName = serviceName;
            this.price = price;
        }

        public String getServiceName() { return serviceName; }
        public double getPrice() { return price; }
    }

    // ================= ADD-ON MANAGER =================
    static class AddOnServiceManager {

        private HashMap<String, List<AddOnService>> serviceMap = new HashMap<>();

        public void addService(String reservationId, AddOnService service) {
            serviceMap.putIfAbsent(reservationId, new ArrayList<>());
            serviceMap.get(reservationId).add(service);

            System.out.println("Added Service: " + service.getServiceName() +
                    " to " + reservationId);
        }

        public double calculateTotalCost(String reservationId) {
            double total = 0;

            List<AddOnService> services = serviceMap.get(reservationId);

            if (services != null) {
                for (AddOnService s : services) {
                    total += s.getPrice();
                }
            }

            return total;
        }

        public void displayServices(String reservationId) {
            System.out.println("\n--- Services for " + reservationId + " ---");

            List<AddOnService> services = serviceMap.get(reservationId);

            if (services != null) {
                for (AddOnService s : services) {
                    System.out.println(s.getServiceName() + " - ₹" + s.getPrice());
                }
            } else {
                System.out.println("No services added.");
            }
        }
    }

    // ================= MAIN =================
    public static void main(String[] args) {

        System.out.println("=== Book My Stay v7.1 ===");

        // Rooms
        Room[] rooms = {
                new SingleRoom(),
                new DoubleRoom(),
                new SuiteRoom()
        };

        // Inventory
        RoomInventory inventory = new RoomInventory();

        // Search
        RoomSearchService search = new RoomSearchService();
        search.searchAvailableRooms(inventory, rooms);

        // Booking Queue
        BookingQueue queue = new BookingQueue();

        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // fail
        queue.addRequest(new Reservation("David", "Double Room"));

        // Booking Service
        BookingService service = new BookingService();
        service.processBookings(queue, inventory);

        // ================= UC7 =================
        AddOnServiceManager addOnManager = new AddOnServiceManager();

        String res1 = "SI-11111";
        String res2 = "DO-22222";

        addOnManager.addService(res1, new AddOnService("Breakfast", 500));
        addOnManager.addService(res1, new AddOnService("Airport Pickup", 1000));

        addOnManager.addService(res2, new AddOnService("Extra Bed", 700));

        addOnManager.displayServices(res1);
        System.out.println("Total Cost: ₹" + addOnManager.calculateTotalCost(res1));

        addOnManager.displayServices(res2);
        System.out.println("Total Cost: ₹" + addOnManager.calculateTotalCost(res2));

        System.out.println("\nApplication Finished.");
    }
}