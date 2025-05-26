Name:MALA HARSHAVARDHAN KUMAR
Company:GLOBAL QUEST TECHNOLOGIES
Domain:CORE JAVA
Project Name:  Bus-booking-system
**This Java-based console application simulates a Bus Booking System, allowing users to:**

View available pickup cities

Choose a destination city from that pickup

See available bus routes and types

Book seats on a selected bus

🔧** **Core Components****
1. **Class**: Route
Represents a travel route from one city to another.

**Attributes**: pickup, destination, viaPoints, distanceKm, durationHr, and a routeName for readability.

**toString()**: Returns a human-readable representation of the route.

2. **Class**: Bus
Represents a single bus on a specific route.

**Attributes**: busId, type (AC, Sleeper, Non-AC), route, totalSeats, and bookedSeatNumbers.

**Methods:**

isSeatAvailable(seatNumber)

bookSpecificSeats(seats)

getAvailableSeats()

calculateFare(seats)

toString()

3.**Class**: BusService
Manages the collection of buses and routes.

Attributes:

**buses**: List of all buses

**routes**: List of all available routes

**Methods:**

loadRoutesAndBuses(): Initializes cities, routes, and buses

listPickupCities(): Shows cities with outgoing routes

listDestinationsFrom(pickup): Shows valid destinations from a selected pickup

listRoutes(pickup, destination): Lists all routes between two cities

bookBus(...): Handles the booking logic

4.** **Class****: BusBookingApp (Main Class)
Handles user interaction via the console.

Presents a menu:

View pickup cities

Book a bus

Exit

Based on user input, it calls relevant methods in BusService.

🧠** **Functional Features*******
Bidirectional routing (e.g., Bangalore ↔ Hyderabad)

Multiple route options between cities

Real-time seat availability

Fare calculation based on bus type and distance

Route names automatically generated with via points

User-friendly prompts and validations

🗺️ **Example Cities Included**
Pickup/Destination Cities: Bangalore, Hyderabad, Chennai, Vizag

Each city pair has predefined routes with intermediate stops (via points)

💰**Fare Structure (per km)***:
AC: ₹2.0

Sleeper: ₹1.5

Non-AC: ₹1.0

✅ **Benefits**
Simple and extensible

Easy to add more cities, buses, or route variations

Interactive and menu-driven

📦 **Use Case Example**:
A user wants to travel from Bangalore to Hyderabad, selects AC bus, books 2 seats, and gets fare and seat confirmation.

**Source code:**
package busbooking.java;



import java.util.*;

class Route {
    String pickup;
    String destination;
    List<String> viaPoints;
    double distanceKm;
    double durationHr;
    String routeName;

    Route(String pickup, String destination, List<String> viaPoints, double distanceKm, double durationHr) {
        this.pickup = pickup;
        this.destination = destination;
        this.viaPoints = viaPoints;
        this.distanceKm = distanceKm;
        this.durationHr = durationHr;
        this.routeName = pickup + " → " + String.join(" → ", viaPoints) + " → " + destination;
    }

    @Override
    public String toString() {
        return routeName + " | Distance: " + distanceKm + " km | Duration: " + durationHr + " hrs";
    }
}

class Bus {
    String busId;
    String type;
    Route route;
    int totalSeats;
    Set<Integer> bookedSeatNumbers;

    Bus(String busId, String type, Route route, int totalSeats) {
        this.busId = busId;
        this.type = type;
        this.route = route;
        this.totalSeats = totalSeats;
        this.bookedSeatNumbers = new HashSet<>();
    }

    boolean isSeatAvailable(int seatNumber) {
        return seatNumber >= 1 && seatNumber <= totalSeats && !bookedSeatNumbers.contains(seatNumber);
    }

    boolean bookSpecificSeats(List<Integer> seatsToBook) {
        for (int seat : seatsToBook) {
            if (!isSeatAvailable(seat)) return false;
        }
        bookedSeatNumbers.addAll(seatsToBook);
        return true;
    }

    List<Integer> getAvailableSeats() {
        List<Integer> available = new ArrayList<>();
        for (int i = 1; i <= totalSeats; i++) {
            if (!bookedSeatNumbers.contains(i)) {
                available.add(i);
            }
        }
        return available;
    }

    double calculateFare(int seats) {
        double ratePerKm;
        switch (type.toLowerCase()) {
            case "ac": ratePerKm = 2.0; break;
            case "sleeper": ratePerKm = 1.5; break;
            default: ratePerKm = 1.0; break;
        }
        return seats * route.distanceKm * ratePerKm;
    }

    @Override
    public String toString() {
        return "Bus ID: " + busId + " | Type: " + type + " | " + route +
               " | Seats Booked: " + bookedSeatNumbers.size() + "/" + totalSeats;
    }
}

class BusService {
    List<Bus> buses = new ArrayList<>();
    List<Route> routes = new ArrayList<>();

    void addRouteWithReverse(String from, String to, List<String> via, double distance, double duration) {
        Route forward = new Route(from, to, via, distance, duration);
        List<String> reverseVia = new ArrayList<>(via);
        Collections.reverse(reverseVia);
        Route reverse = new Route(to, from, reverseVia, distance, duration);

        routes.add(forward);
        routes.add(reverse);

        buses.add(new Bus("BUS-" + buses.size() + 1, "AC", forward, 50));
        buses.add(new Bus("BUS-" + buses.size() + 1, "Sleeper", forward, 40));
        buses.add(new Bus("BUS-" + buses.size() + 1, "Non-AC", forward, 45));

        buses.add(new Bus("BUS-" + buses.size() + 1, "AC", reverse, 50));
        buses.add(new Bus("BUS-" + buses.size() + 1, "Sleeper", reverse, 40));
        buses.add(new Bus("BUS-" + buses.size() + 1, "Non-AC", reverse, 45));
    }

    void loadRoutes() {
        addRouteWithReverse("Bangalore", "Hyderabad", Arrays.asList("Chikkabalapura", "Kurnool"), 600, 8.0);
        addRouteWithReverse("Bangalore", "Chennai", Arrays.asList("Krishnagiri", "Vellore"), 340, 6.0);
        addRouteWithReverse("Hyderabad", "Vizag", Arrays.asList("Vijayawada", "Rajahmundry"), 620, 9.5);
        addRouteWithReverse("Chennai", "Vizag", Arrays.asList("Ongole", "Vijayawada"), 770, 11.0);
    }

    Set<String> getPickupCities() {
        Set<String> pickups = new HashSet<>();
        for (Route r : routes) {
            pickups.add(r.pickup);
        }
        return pickups;
    }

    Set<String> getDestinationsFrom(String pickup) {
        Set<String> destinations = new HashSet<>();
        for (Route r : routes) {
            if (r.pickup.equalsIgnoreCase(pickup)) {
                destinations.add(r.destination);
            }
        }
        return destinations;
    }

    void listRoutes(String pickup, String destination) {
        System.out.println("🚍 Routes from " + pickup + " to " + destination + ":");
        boolean found = false;
        for (Route r : routes) {
            if (r.pickup.equalsIgnoreCase(pickup) && r.destination.equalsIgnoreCase(destination)) {
                System.out.println("- " + r);
                found = true;
            }
        }
        if (!found) System.out.println("❌ No routes available.");
    }

    void bookBus(String pickup, String destination, String type, int seats, Scanner scanner) {
        List<Bus> matching = new ArrayList<>();
        for (Bus b : buses) {
            if (b.route.pickup.equalsIgnoreCase(pickup) &&
                b.route.destination.equalsIgnoreCase(destination) &&
                b.type.equalsIgnoreCase(type)) {
                matching.add(b);
            }
        }

        if (matching.isEmpty()) {
            System.out.println("❌ No matching buses.");
            return;
        }

        for (int i = 0; i < matching.size(); i++) {
            Bus b = matching.get(i);
            System.out.printf("%d. %s | Available Seats: %d | Fare: ₹%.2f\n",
                              i + 1, b.busId, b.getAvailableSeats().size(), b.calculateFare(seats));
        }

        System.out.print("Select Bus Number: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1 || choice > matching.size()) {
            System.out.println("❌ Invalid choice.");
            return;
        }

        Bus selected = matching.get(choice - 1);
        List<Integer> available = selected.getAvailableSeats();
        if (available.size() < seats) {
            System.out.println("❌ Not enough seats. Available: " + available);
            return;
        }

        System.out.println("Available Seats: " + available);
        System.out.print("Enter seat numbers (space-separated): ");
        String[] seatStrs = scanner.nextLine().split("\\s+");
        List<Integer> seatNums = new ArrayList<>();
        for (String s : seatStrs) seatNums.add(Integer.parseInt(s));

        if (seatNums.size() != seats) {
            System.out.println("❌ Number of seats mismatch.");
            return;
        }

        if (!selected.bookSpecificSeats(seatNums)) {
            System.out.println("❌ Some seats already booked or invalid.");
            return;
        }

        System.out.println("\n✅ Booking Confirmed!");
        System.out.println("Bus: " + selected.busId + " | Route: " + selected.route.routeName);
        System.out.println("Seats: " + seatNums + " | Fare: ₹" + selected.calculateFare(seats));
    }
}

public class BusBookingApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BusService service = new BusService();
        service.loadRoutes();

        while (true) {
            System.out.println("\n=== BUS BOOKING SYSTEM ===");
            System.out.println("1. View Pickup Cities");
            System.out.println("2. Book a Bus");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            int ch = scanner.nextInt();
            scanner.nextLine();

            if (ch == 1) {
                Set<String> pickups = service.getPickupCities();
                System.out.println("📍 Available Pickup Cities: " + pickups);
            } else if (ch == 2) {
                System.out.println("📍 Available Pickup Cities: " + service.getPickupCities());
                System.out.print("Enter Pickup Location: ");
                String pickup = scanner.nextLine();

                Set<String> destinations = service.getDestinationsFrom(pickup);
                if (destinations.isEmpty()) {
                    System.out.println("❌ No destinations from " + pickup);
                    continue;
                }

                System.out.println("📍 Available Destinations from " + pickup + ": " + destinations);
                System.out.print("Enter Destination Location: ");
                String drop = scanner.nextLine();

                service.listRoutes(pickup, drop);
                System.out.print("Enter Bus Type (AC / Sleeper / Non-AC): ");
                String type = scanner.nextLine();
                System.out.print("Enter number of seats: ");
                int seats = scanner.nextInt();
                scanner.nextLine();

                service.bookBus(pickup, drop, type, seats, scanner);
            } else if (ch == 3) {
                System.out.println("👋 Thank you for using the system!");
                break;
            } else {
                System.out.println("❌ Invalid choice.");
            }
        }

        scanner.close();
    }
}

