package bus.com.bus;

import javax.swing.SwingUtilities;

public class BusBookingSwingApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BookingUI().setVisible(true);
        });
    }
}
