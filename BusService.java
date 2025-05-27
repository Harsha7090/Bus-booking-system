package bus.com.bus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BusService {
    private List<Route> routes;
    private List<Bus> buses;

    public BusService() {
        this.routes = new ArrayList<>();
        this.buses = new ArrayList<>();
        loadRoutes();
    }

    private void addRouteWithReverse(String from, String to, List<String> via, double distance, double duration) {
        Route forward = new Route(from, to, via, distance, duration);
        List<String> reverseVia = new ArrayList<>(via);
        Collections.reverse(reverseVia);
        Route reverse = new Route(to, from, reverseVia, distance, duration);

        routes.add(forward);
        routes.add(reverse);

        buses.add(new Bus("BUS" + (buses.size() + 1), "AC", forward, 40));
        buses.add(new Bus("BUS" + (buses.size() + 1), "Sleeper", forward, 30));
        buses.add(new Bus("BUS" + (buses.size() + 1), "Non-AC", forward, 35));

        buses.add(new Bus("BUS" + (buses.size() + 1), "AC", reverse, 40));
        buses.add(new Bus("BUS" + (buses.size() + 1), "Sleeper", reverse, 30));
        buses.add(new Bus("BUS" + (buses.size() + 1), "Non-AC", reverse, 35));
    }

    public void loadRoutes() {
        // Example route loading; replace with actual data
        addRouteWithReverse("Bangalore", "Chennai", List.of("Vellore", "Krishnagiri"), 350, 6);
        addRouteWithReverse("Mumbai", "Pune", List.of("Lonavala"), 150, 3);
    }
    public Set<String> getPickupCities() {
        Set<String> pickups = new HashSet<>();
        for (Route route : routes) {
            pickups.add(route.getFrom());  // Use getFrom() if that exists in Route
        }
        return pickups;
    }


    public List<String> getPickupCities() {
        Set<String> cities = new HashSet<>();
        for (Route route : routes) {
            cities.add(route.getSource());
        }
        return new ArrayList<>(cities);
    }

    public List<String> getDestinationsFrom(String pickup) {
        Set<String> destinations = new HashSet<>();
        for (Route route : routes) {
            if (route.getSource().equalsIgnoreCase(pickup)) {
                destinations.add(route.getDestination());
            }
        }
        return new ArrayList<>(destinations);
    }

    public List<Bus> findMatchingBuses(String pickup, String destination, String type) {
        List<Bus> matchingBuses = new ArrayList<>();

        for (Bus bus : buses) {
            Route route = bus.getRoute();
            if (route.getSource().equalsIgnoreCase(pickup) &&
                route.getDestination().equalsIgnoreCase(destination) &&
                bus.getType().equalsIgnoreCase(type)) {
                matchingBuses.add(bus);
            }
        }

        return matchingBuses;
    }
    public Bus getBusById(String busId) {
        for (Bus b : buses) {
            if (b.getBusId().equalsIgnoreCase(busId)) {
                return b;
            }
        }
        return null;
    }


    public boolean bookSeats(String busId, List<Integer> seats) {
        for (Bus bus : buses) {
            if (bus.getBusId().equalsIgnoreCase(busId)) {
                return bus.bookSpecificSeats(seats);
            }
        }
        return false;
    }

    public boolean cancelSeats(String busId, List<Integer> seats) {
        for (Bus bus : buses) {
            if (bus.getBusId().equalsIgnoreCase(busId)) {
                return bus.cancelSpecificSeats(seats);
            }
        }
        return false;
    }
}
