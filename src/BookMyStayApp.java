import java.io.*;
import java.util.*;

/**
 * Book My Stay - Hotel Booking System
 *
 * UC12: Data Persistence & Recovery
 *
 * @author Vikki
 * @version 12.0
 */
public class BookMyStayApp {

    // ================= INVENTORY =================
    static class RoomInventory implements Serializable {
        private HashMap<String, Integer> availability = new HashMap<>();

        public RoomInventory() {
            availability.put("Single Room", 2);
            availability.put("Double Room", 1);
        }

        public HashMap<String, Integer> getAvailability() {
            return availability;
        }

        public void show() {
            System.out.println("Inventory: " + availability);
        }
    }

    // ================= RESERVATION =================
    static class Reservation implements Serializable {
        String guestName;
        String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        @Override
        public String toString() {
            return guestName + " → " + roomType;
        }
    }

    // ================= BOOKING HISTORY =================
    static class BookingHistory implements Serializable {
        List<Reservation> history = new ArrayList<>();

        public void add(Reservation r) {
            history.add(r);
        }

        public void show() {
            System.out.println("Booking History:");
            for (Reservation r : history) {
                System.out.println(r);
            }
        }
    }

    // ================= PERSISTENCE SERVICE =================
    static class PersistenceService {

        private static final String FILE_NAME = "hotel_data.ser";

        // SAVE
        public void save(RoomInventory inventory, BookingHistory history) {
            try (ObjectOutputStream oos =
                         new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

                oos.writeObject(inventory);
                oos.writeObject(history);

                System.out.println("✅ Data saved successfully.");

            } catch (IOException e) {
                System.out.println("❌ Error saving data: " + e.getMessage());
            }
        }

        // LOAD
        public Object[] load() {
            try (ObjectInputStream ois =
                         new ObjectInputStream(new FileInputStream(FILE_NAME))) {

                RoomInventory inventory = (RoomInventory) ois.readObject();
                BookingHistory history = (BookingHistory) ois.readObject();

                System.out.println("✅ Data loaded successfully.");

                return new Object[]{inventory, history};

            } catch (FileNotFoundException e) {
                System.out.println("⚠ No previous data found. Starting fresh.");
            } catch (Exception e) {
                System.out.println("❌ Error loading data. Starting fresh.");
            }

            return null;
        }
    }

    // ================= MAIN =================
    public static void main(String[] args) {

        System.out.println("=== Book My Stay v12.0 ===");

        PersistenceService persistence = new PersistenceService();

        RoomInventory inventory;
        BookingHistory history;

        // 🔄 LOAD EXISTING DATA
        Object[] data = persistence.load();

        if (data != null) {
            inventory = (RoomInventory) data[0];
            history = (BookingHistory) data[1];
        } else {
            inventory = new RoomInventory();
            history = new BookingHistory();
        }

        // 🔥 SIMULATE BOOKINGS
        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Double Room");

        history.add(r1);
        history.add(r2);

        // SHOW CURRENT STATE
        inventory.show();
        history.show();

        // 💾 SAVE BEFORE EXIT
        persistence.save(inventory, history);

        System.out.println("Application Finished.");
    }
}