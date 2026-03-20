/**
 * Book My Stay - Hotel Booking System
 *
 * Use Case 1 + Use Case 2 Implementation
 *
 * @author Vikki
 * @version 2.1
 */
public class BookMyStayApp {

    // ================= ABSTRACT CLASS =================
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

    // ================= SINGLE ROOM =================
    static class SingleRoom extends Room {

        public SingleRoom() {
            super("Single Room", 1, 1000);
        }

        @Override
        public void displayRoomDetails() {
            System.out.println("Room Type: " + getRoomType());
            System.out.println("Beds: " + getBeds());
            System.out.println("Price: ₹" + getPrice());
        }
    }

    // ================= DOUBLE ROOM =================
    static class DoubleRoom extends Room {

        public DoubleRoom() {
            super("Double Room", 2, 2000);
        }

        @Override
        public void displayRoomDetails() {
            System.out.println("Room Type: " + getRoomType());
            System.out.println("Beds: " + getBeds());
            System.out.println("Price: ₹" + getPrice());
        }
    }

    // ================= SUITE ROOM =================
    static class SuiteRoom extends Room {

        public SuiteRoom() {
            super("Suite Room", 3, 5000);
        }

        @Override
        public void displayRoomDetails() {
            System.out.println("Room Type: " + getRoomType());
            System.out.println("Beds: " + getBeds());
            System.out.println("Price: ₹" + getPrice());
        }
    }

    // ================= MAIN METHOD =================
    public static void main(String[] args) {

        String appName = "Book My Stay";
        String version = "v2.1";

        System.out.println("=================================");
        System.out.println(" Welcome to " + appName);
        System.out.println(" Version: " + version);
        System.out.println("=================================");

        // Room Objects (Polymorphism)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static Availability (NO Data Structures)
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        System.out.println("\n--- Room Details & Availability ---\n");

        single.displayRoomDetails();
        System.out.println("Available: " + singleAvailable);
        System.out.println("---------------------------------");

        doubleRoom.displayRoomDetails();
        System.out.println("Available: " + doubleAvailable);
        System.out.println("---------------------------------");

        suite.displayRoomDetails();
        System.out.println("Available: " + suiteAvailable);
        System.out.println("---------------------------------");

        System.out.println("Application Finished.");
    }
}