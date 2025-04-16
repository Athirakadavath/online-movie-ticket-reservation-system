import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TicketEntry extends JFrame {
    private JLabel theatreLabel;
    private JLabel showLabel;
    private JTextField ticketCountField;
    private JButton confirmButton;

    private String selectedTheatre;
    private String selectedShow;

    public TicketEntry(String theatre, String show) {
        selectedTheatre = theatre;
        selectedShow = show;

        setTitle("Ticket Entry");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        theatreLabel = new JLabel("Theatre: " + selectedTheatre);
        showLabel = new JLabel("Show: " + selectedShow);
        JLabel ticketCountLabel = new JLabel("Number of Tickets:");
        ticketCountField = new JTextField();
        confirmButton = new JButton("Confirm");

        theatreLabel.setBounds(50, 20, 300, 30);
        showLabel.setBounds(50, 50, 300, 30);
        ticketCountLabel.setBounds(50, 80, 150, 30);
        ticketCountField.setBounds(200, 80, 100, 30);
        confirmButton.setBounds(150, 130, 100, 30);

        add(theatreLabel);
        add(showLabel);
        add(ticketCountLabel);
        add(ticketCountField);
        add(confirmButton);

        confirmButton.addActionListener(e -> insertTicketDetails());
    }

    private void insertTicketDetails() {
        int ticketCount = Integer.parseInt(ticketCountField.getText());

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO TICKETS (SHOW_ID, USER_ID, TICKET_COUNT, BOOKING_DATE, TICKET_PRICE) VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            int showId = getShowId(selectedShow, selectedTheatre, connection); // Helper function to get SHOW_ID
            statement.setInt(1, showId);
            statement.setInt(2, 1); // Example USER_ID
            statement.setInt(3, ticketCount);
            statement.setDouble(4, 100.00); // Example ticket price

            statement.executeUpdate();

            // Open the TicketAndPayment window instead of showing booking success
            TicketAndPayment ticketAndPayment = new TicketAndPayment(sql, sql, ticketCount, 100.00); // Pass ticketCount and price to the next screen
            ticketAndPayment.setVisible(true);
            dispose(); // Close the current window

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getShowId(String show, String theatre, Connection connection) {
        int showId = -1;
        try {
            String sql = "SELECT SHOW_ID FROM SHOWS WHERE MOVIE_TITLE = ? AND THEATRE_ID = (SELECT THEATRE_ID FROM THEATRES WHERE NAME = ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            String movieTitle = show.split(" - ")[0];
            statement.setString(1, movieTitle);
            statement.setString(2, theatre);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                showId = resultSet.getInt("SHOW_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showId;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TicketEntry ticketEntry = new TicketEntry("Sample Theatre", "Sample Show");
            ticketEntry.setVisible(true);
        });
    }
}
