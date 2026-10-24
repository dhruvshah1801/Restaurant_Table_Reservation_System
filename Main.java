import java.time.LocalDate;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Restaurant Table Reservation System
 * DAA concepts: linear search, sorting, greedy table selection, conflict detection.
 * Storage is in-memory only. Data resets when the application exits.
 */
public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final ReservationSystem SYSTEM = new ReservationSystem();

    public static void main(String[] args) {
        SYSTEM.addTable(new RestaurantTable(1, 2));
        SYSTEM.addTable(new RestaurantTable(2, 2));
        SYSTEM.addTable(new RestaurantTable(3, 4));
        SYSTEM.addTable(new RestaurantTable(4, 4));
        SYSTEM.addTable(new RestaurantTable(5, 6));
        SYSTEM.addTable(new RestaurantTable(6, 8));

        System.out.println("========================================");
        System.out.println("  RESTAURANT TABLE RESERVATION SYSTEM");
        System.out.println("========================================");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> SYSTEM.displayTables();
                case 2 -> makeReservation();
                case 3 -> searchReservation();
                case 4 -> cancelReservation();
                case 5 -> SYSTEM.displayReservations();
                case 6 -> findSuitableTables();
                case 0 -> {
                    running = false;
                    System.out.println("Thank you. Goodbye!");
                }
                default -> System.out.println("Invalid choice. Please select 0-6.");
            }
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("\n1. View all tables");
        System.out.println("2. Make a reservation");
        System.out.println("3. Search reservation by ID");
        System.out.println("4. Cancel reservation");
        System.out.println("5. View all reservations");
        System.out.println("6. Find suitable tables");
        System.out.println("0. Exit");
    }

    private static void makeReservation() {
        String name = readNonEmpty("Customer name: ");
        int guests = readInt("Number of guests: ");
        if (guests <= 0) {
            System.out.println("Guest count must be greater than zero.");
            return;
        }

        LocalDate date = readDate("Date (YYYY-MM-DD): ");
        LocalTime time = readTime("Start time (HH:MM, 24-hour): ");
        int duration = readInt("Duration in minutes (e.g. 90): ");
        if (duration <= 0 || duration > 720) {
            System.out.println("Duration must be between 1 and 720 minutes.");
            return;
        }

        Reservation result = SYSTEM.reserve(name, guests, date, time, duration);
        if (result == null) {
            System.out.println("No suitable table is available for that time slot.");
        } else {
            System.out.println("Reservation successful!");
            System.out.println(result);
        }
    }

    private static void searchReservation() {
        int id = readInt("Reservation ID: ");
        Reservation reservation = SYSTEM.findReservation(id);
        if (reservation == null) {
            System.out.println("Reservation not found.");
        } else {
            System.out.println(reservation);
        }
    }

    private static void cancelReservation() {
        int id = readInt("Reservation ID to cancel: ");
        if (SYSTEM.cancel(id)) {
            System.out.println("Reservation cancelled successfully.");
        } else {
            System.out.println("Active reservation not found.");
        }
    }

    private static void findSuitableTables() {
        int guests = readInt("Number of guests: ");
        LocalDate date = readDate("Date (YYYY-MM-DD): ");
        LocalTime time = readTime("Start time (HH:MM, 24-hour): ");
        int duration = readInt("Duration in minutes: ");
        if (guests <= 0 || duration <= 0 || duration > 720) {
            System.out.println("Guest count and duration must be valid positive values.");
            return;
        }
        List<RestaurantTable> available =
                SYSTEM.findAvailableTables(guests, date, time, duration);
        if (available.isEmpty()) {
            System.out.println("No suitable tables available.");
        } else {
            System.out.println("Suitable available tables (smallest capacity first):");
            for (RestaurantTable table : available) {
                System.out.println(table);
            }
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = SCANNER.nextLine().trim();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = SCANNER.nextLine().trim();
            if (!value.isEmpty()) return value;
            System.out.println("This field cannot be empty.");
        }
    }

    private static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalDate.parse(SCANNER.nextLine().trim());
            } catch (DateTimeParseException ex) {
                System.out.println("Invalid date. Use YYYY-MM-DD.");
            }
        }
    }

    private static LocalTime readTime(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalTime.parse(SCANNER.nextLine().trim());
            } catch (DateTimeParseException ex) {
                System.out.println("Invalid time. Use HH:MM, e.g. 19:30.");
            }
        }
    }
}

class RestaurantTable {
    private final int tableId;
    private final int capacity;

    public RestaurantTable(int tableId, int capacity) {
        this.tableId = tableId;
        this.capacity = capacity;
    }

    public int getTableId() { return tableId; }
    public int getCapacity() { return capacity; }

    @Override
    public String toString() {
        return "Table " + tableId + " | Capacity: " + capacity;
    }
}

class Reservation {
    private final int reservationId;
    private final String customerName;
    private final int guests;
    private final int tableId;
    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private boolean active;

    public Reservation(int reservationId, String customerName, int guests, int tableId,
                       LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.guests = guests;
        this.tableId = tableId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.active = true;
    }

    public int getReservationId() { return reservationId; }
    public int getTableId() { return tableId; }
    public LocalDate getDate() { return date; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public boolean isActive() { return active; }
    public void cancel() { active = false; }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId
                + "\nCustomer: " + customerName
                + "\nGuests: " + guests
                + "\nTable: " + tableId
                + "\nDate: " + date
                + "\nTime: " + startTime + " - " + endTime
                + "\nStatus: " + (active ? "ACTIVE" : "CANCELLED");
    }
}

class ReservationSystem {
    private final List<RestaurantTable> tables = new ArrayList<>();
    private final Map<Integer, Reservation> reservations = new LinkedHashMap<>();
    private int nextReservationId = 1001;

    public void addTable(RestaurantTable table) {
        tables.add(table);
    }

    public void displayTables() {
        System.out.println("\nRestaurant tables:");
        for (RestaurantTable table : tables) {
            System.out.println(table);
        }
    }

    public void displayReservations() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations have been created.");
            return;
        }
        for (Reservation reservation : reservations.values()) {
            System.out.println("------------------------------");
            System.out.println(reservation);
        }
    }

    /** Linear search by reservation ID: O(R) in the general list-based model. */
    public Reservation findReservation(int id) {
        for (Reservation reservation : reservations.values()) {
            if (reservation.getReservationId() == id) return reservation;
        }
        return null;
    }

    /**
     * Greedy choice: among available tables that fit the group, choose the
     * smallest capacity to avoid wasting seats.
     */
    public Reservation reserve(String name, int guests, LocalDate date,
                               LocalTime start, int durationMinutes) {
        if (name == null || name.trim().isEmpty() || guests <= 0
                || date == null || start == null || durationMinutes <= 0) {
            return null;
        }

        List<RestaurantTable> candidates =
                findAvailableTables(guests, date, start, durationMinutes);
        if (candidates.isEmpty()) return null;

        RestaurantTable chosen = candidates.get(0);
        LocalTime end = start.plusMinutes(durationMinutes);
        Reservation reservation = new Reservation(nextReservationId++, name.trim(),
                guests, chosen.getTableId(), date, start, end);
        reservations.put(reservation.getReservationId(), reservation);
        return reservation;
    }

    /** Returns suitable tables ordered by increasing capacity, then table ID. */
    public List<RestaurantTable> findAvailableTables(int guests, LocalDate date,
                                                      LocalTime start, int durationMinutes) {
        List<RestaurantTable> available = new ArrayList<>();
        if (guests <= 0 || date == null || start == null || durationMinutes <= 0) {
            return available;
        }
        LocalTime end;
        try {
            end = start.plusMinutes(durationMinutes);
        } catch (DateTimeException ex) {
            return available;
        }

        for (RestaurantTable table : tables) {
            if (table.getCapacity() >= guests
                    && isTableAvailable(table.getTableId(), date, start, end)) {
                available.add(table);
            }
        }

        available.sort(Comparator.comparingInt(RestaurantTable::getCapacity)
                .thenComparingInt(RestaurantTable::getTableId));
        return available;
    }

    /**
     * Two half-open intervals overlap exactly when startA < endB and startB < endA.
     * Therefore a booking ending at 19:00 does not conflict with one starting at 19:00.
     */
    private boolean isTableAvailable(int tableId, LocalDate date,
                                     LocalTime requestedStart, LocalTime requestedEnd) {
        for (Reservation existing : reservations.values()) {
            if (!existing.isActive() || existing.getTableId() != tableId
                    || !existing.getDate().equals(date)) {
                continue;
            }
            boolean overlaps = requestedStart.isBefore(existing.getEndTime())
                    && existing.getStartTime().isBefore(requestedEnd);
            if (overlaps) return false;
        }
        return true;
    }

    public boolean cancel(int id) {
        Reservation reservation = findReservation(id);
        if (reservation == null || !reservation.isActive()) return false;
        reservation.cancel();
        return true;
    }
}
