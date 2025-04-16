import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TicketDetails extends JFrame {

    public TicketDetails(int numberOfTickets, String username) {
        initialize(numberOfTickets, username);
    }

    private void initialize(int numberOfTickets, String username) {
        setTitle("Ticket Details");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1));  // Change the layout to have 4 rows

        // Adding only the relevant details to the JFrame
        add(new JLabel("Username: " + username));
        add(new JLabel("Number of Tickets: " + numberOfTickets));
        add(new JLabel("Thank you for your booking!"));

        setVisible(true);
    }

    // Removed the getTheatreAndShowNames method, as it's no longer needed.

    public static void main(String[] args) {
        // Example usage with just the number of tickets and username
        int numberOfTickets = 3;
        String username = "username123";

        // Directly passing number of tickets and username to the constructor
        TicketDetails ticketDetails = new TicketDetails(numberOfTickets, username);
        ticketDetails.setVisible(true);
    }
}
