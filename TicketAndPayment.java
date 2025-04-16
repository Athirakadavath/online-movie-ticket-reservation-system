import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TicketAndPayment extends JFrame {
    private JTextField txtUsername;
    private JButton btnConfirm;
    private JComboBox<String> paymentMethodComboBox;
    private JLabel lblTotalAmount;
    private String selectedTheatre;
    private String selectedShow;
    private int numberOfTickets;
    private double ticketPrice;

    public TicketAndPayment(String theatre, String show, int numberOfTickets, double ticketPrice) {
        this.selectedTheatre = theatre;
        this.selectedShow = show;
        this.numberOfTickets = numberOfTickets;
        this.ticketPrice = ticketPrice;
        initialize();
    }

    private void initialize() {
        setTitle("Payment Details");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        txtUsername = new JTextField(20);
        btnConfirm = new JButton("Confirm Booking");

        String[] paymentMethods = {"Credit Card", "Debit Card", "PayPal", "Net Banking"};
        paymentMethodComboBox = new JComboBox<>(paymentMethods);

        lblTotalAmount = new JLabel("Total Amount: " + calculateTotalAmount());

        btnConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmBooking();
            }
        });

        add(new JLabel("Username:"));
        add(txtUsername);
        add(new JLabel("Payment Method:"));
        add(paymentMethodComboBox);
        add(lblTotalAmount);
        add(btnConfirm);
    }

    private double calculateTotalAmount() {
        return numberOfTickets * ticketPrice;
    }

    private void confirmBooking() {
        String username = txtUsername.getText();
        String paymentMethod = (String) paymentMethodComboBox.getSelectedItem();
        double totalAmount = calculateTotalAmount();

        if (processPayment(username, paymentMethod, totalAmount)) {
            // Show ticket details if payment is successful
            TicketDetails ticketDetails = new TicketDetails( numberOfTickets, username);
            ticketDetails.setVisible(true);
            dispose(); // Close this frame
        }
    }

    private boolean processPayment(String username, String paymentMethod, double totalAmount) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO PAYMENT (TICKET_ID, PAYMENT_AMOUNT, PAYMENT_DATE, PAYMENT_STATUS) " +
                         "VALUES (?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);

            int ticketId = generateTicketId(); // Method to generate or retrieve the ticket ID

            statement.setInt(1, ticketId);
            statement.setDouble(2, totalAmount);
            statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now())); // Current timestamp
            statement.setString(4, "SUCCESS"); // Default payment status

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Payment processed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Failed to process payment. Please try again.", "Payment Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private int generateTicketId() {
        // This method should return a valid ticket ID based on your database design.
        // Replace this logic with the actual ticket ID retrieval or generation code.
        return 1; // Example ticket ID, replace with actual logic
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TicketAndPayment paymentFrame = new TicketAndPayment("Theatre 1", "Show A", 2, 150.0);
            paymentFrame.setVisible(true);
        });
    }
}
