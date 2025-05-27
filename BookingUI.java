package bus.com.bus;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BookingUI extends JFrame {
    private BusService service;

    private JComboBox<String> pickupCombo;
    private JComboBox<String> destinationCombo;
    private JComboBox<String> typeCombo;
    private JTextField seatsField;
    private JButton bookButton;
    private JButton cancelButton;
    private JTextArea outputArea;

    public BookingUI() {
        service = new BusService();
        service.loadRoutes();

        setTitle("Bus Booking System");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pickupCombo = new JComboBox<>(service.getPickupCities().toArray(new String[0]));
        destinationCombo = new JComboBox<>();
        typeCombo = new JComboBox<>(new String[]{"AC", "Sleeper", "Non-AC"});
        seatsField = new JTextField();
        bookButton = new JButton("Book Bus");
        cancelButton = new JButton("Cancel Booking");

        formPanel.add(new JLabel("Pickup City:"));
        formPanel.add(pickupCombo);

        formPanel.add(new JLabel("Destination City:"));
        formPanel.add(destinationCombo);

        formPanel.add(new JLabel("Bus Type:"));
        formPanel.add(typeCombo);

        formPanel.add(new JLabel("Seats to Book:"));
        formPanel.add(seatsField);

        formPanel.add(bookButton);
        formPanel.add(cancelButton);

        add(formPanel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        pickupCombo.addActionListener(e -> {
            String selectedPickup = (String) pickupCombo.getSelectedItem();
            List<String> dests = service.getDestinationsFrom(selectedPickup);
            destinationCombo.removeAllItems();
            for (String dest : dests) {
                destinationCombo.addItem(dest);
            }
        });

        bookButton.addActionListener(e -> {
            String pickup = (String) pickupCombo.getSelectedItem();
            String dest = (String) destinationCombo.getSelectedItem();
            String type = (String) typeCombo.getSelectedItem();
            int seats;

            try {
                seats = Integer.parseInt(seatsField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid number of seats");
                return;
            }

            List<Bus> matching = service.findMatchingBuses(pickup, dest, type);
            if (matching.isEmpty()) {
                outputArea.setText("\u274C No matching buses.\n");
                return;
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < matching.size(); i++) {
                Bus b = matching.get(i);
                sb.append(String.format("%d. Bus ID: %s | Available Seats: %d | Fare: ₹%.2f\n",
                        i + 1, b.getBusId(), b.getAvailableSeats(), b.calculateFare(seats)));
            }

            Object[] busOptions = matching.stream().map(Bus::getBusId).toArray();
            int choice = JOptionPane.showOptionDialog(this, sb.toString(), "Select a Bus",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                    busOptions, null);

            if (choice >= 0 && choice < matching.size()) {
                Bus selected = matching.get(choice);
                List<Integer> avail = selected.getAvailableSeatNumbers();
                if (avail.size() < seats) {
                    outputArea.setText("\u274C Not enough seats.\nAvailable seats: " + avail + "\n");
                    return;
                }

                String seatStr = JOptionPane.showInputDialog(this, "Enter " + seats + " seat numbers (space-separated):");
                if (seatStr == null) return;
                String[] split = seatStr.trim().split("\\s+");
                if (split.length != seats) {
                    outputArea.setText("\u274C Number of seats mismatch.\n");
                    return;
                }

                try {
                    List<Integer> seatsToBook = new ArrayList<>();
                    for (String s : split) {
                        seatsToBook.add(Integer.parseInt(s));
                    }

                    if (!selected.bookSpecificSeats(seatsToBook)) {
                        outputArea.setText("\u274C Some seats already booked or invalid.\n");
                        return;
                    }

                    outputArea.setText("\u2705 Booking Confirmed!\n");
                    outputArea.append("Bus: " + selected.getBusId() + " | Route: " + selected.getRoute().getFrom() + " to " + selected.getRoute().getTo() + "\n");
                    outputArea.append("Seats: " + seatsToBook + " | Fare: ₹" + selected.calculateFare(seats) + "\n");

                } catch (Exception ex) {
                    outputArea.setText("\u274C Invalid input.\n");
                }
            }
        });

        cancelButton.addActionListener(e -> {
            String busId = JOptionPane.showInputDialog(this, "Enter Bus ID:");
            if (busId == null || busId.trim().isEmpty()) return;

            Bus bus = service.getBusById(busId.trim());
            if (bus == null) {
                outputArea.setText("\u274C Bus ID not found.\n");
                return;
            }

            String seatStr = JOptionPane.showInputDialog(this, "Enter seat numbers to cancel (space-separated):");
            if (seatStr == null || seatStr.trim().isEmpty()) return;

            String[] parts = seatStr.trim().split("\\s+");
            List<Integer> seatsToCancel = new ArrayList<>();
            try {
                for (String p : parts) {
                    seatsToCancel.add(Integer.parseInt(p));
                }

                if (bus.cancelSpecificSeats(seatsToCancel)) {
                    outputArea.setText("\u2705 Seats cancelled successfully.\n");
                    outputArea.append("Bus: " + bus.getBusId() + " | Route: " + bus.getRoute().getFrom() + " to " + bus.getRoute().getTo() + "\n");
                    outputArea.append("Cancelled Seats: " + seatsToCancel + "\n");
                } else {
                    outputArea.setText("\u274C Failed to cancel. Seats may not be booked.\n");
                }
            } catch (NumberFormatException ex1) {
                outputArea.setText("\u274C Invalid input. Please enter valid seat numbers.\n");
            }
        });

        if (pickupCombo.getItemCount() > 0) {
            pickupCombo.setSelectedIndex(0);
        }
    }
}