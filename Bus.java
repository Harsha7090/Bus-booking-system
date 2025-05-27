package bus.com.bus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Bus {
    private String busId;
    private String type;
    private Route route;
    private int totalSeats;
    private Set<Integer> bookedSeats;
    private double farePerSeat;  // Add this field

    public Bus(String busId, String type, Route route, int totalSeats) {
        this.busId = busId;
        this.type = type;
        this.route = route;
        this.totalSeats = totalSeats;
        this.bookedSeats = new HashSet<>();
        this.farePerSeat = 100.0; // example fixed fare per seat; you can set dynamically
    }

    public int getAvailableSeats() {
        return totalSeats - bookedSeats.size();
    }
    public List<Integer> getAvailableSeatNumbers() {
        List<Integer> available = new ArrayList<>();
        for (int i = 1; i <= totalSeats; i++) {
            if (!bookedSeats.contains(i)) {
                available.add(i);
            }
        }
        return available;
    }


    public double calculateFare(int seatCount) {
        return seatCount * farePerSeat;
    }

    public boolean bookSeats(List<Integer> seats) {
        for (int seat : seats) {
            if (bookedSeats.contains(seat) || seat < 1 || seat > totalSeats) {
                return false;
            }
        }
        bookedSeats.addAll(seats);
        return true;
    }

    public boolean cancelSeats(List<Integer> seats) {
        if (!bookedSeats.containsAll(seats)) {
            return false;
        }
        bookedSeats.removeAll(seats);
        return true;
    }

    // If you want to keep these as aliases to bookSeats/cancelSeats:
    public boolean bookSpecificSeats(List<Integer> seatsToBook) {
        return bookSeats(seatsToBook);
    }

    public boolean cancelSpecificSeats(List<Integer> seatsToCancel) {
        return cancelSeats(seatsToCancel);
    }

    // Removed getRouteName(), because routeName is not defined here
    // If you want routeName, use route.toString() or add a method in Route

    public Route getRoute() {
        return route;
    }

    public String getType() {
        return type;
    }

    public String getBusId() {
        return busId;
    }
}
