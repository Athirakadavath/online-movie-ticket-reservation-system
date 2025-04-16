import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TheatreSelection extends JFrame {
    private JComboBox<String> theatreComboBox;
    private JComboBox<String> showComboBox;
    private JButton nextButton;
    private JLabel theatreLabel;
    private JLabel showLabel;

    public TheatreSelection() {
        setTitle("Select Theatre and Show");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        theatreLabel = new JLabel("Select Theatre:");
        showLabel = new JLabel("Select Show:");

        theatreComboBox = new JComboBox<>();
        showComboBox = new JComboBox<>();
        nextButton = new JButton("Next");

        theatreLabel.setBounds(50, 20, 150, 30);
        theatreComboBox.setBounds(50, 50, 300, 30);
        showLabel.setBounds(50, 80, 150, 30);
        showComboBox.setBounds(50, 110, 300, 30);
        nextButton.setBounds(150, 150, 100, 30);

        add(theatreLabel);
        add(theatreComboBox);
        add(showLabel);
        add(showComboBox);
        add(nextButton);

        loadTheatres();

        theatreComboBox.addActionListener(e -> loadShowsForSelectedTheatre());

        nextButton.addActionListener(e -> {
            String selectedTheatre = theatreComboBox.getSelectedItem().toString();
            String selectedShow = showComboBox.getSelectedItem().toString();

            // Pass selected theatre and show details to TicketEntry
            TicketEntry ticketEntry = new TicketEntry(selectedTheatre, selectedShow);
            ticketEntry.setVisible(true);
            dispose(); // Close current window
        });
    }

    private void loadTheatres() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT NAME FROM THEATRES";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                theatreComboBox.addItem(resultSet.getString("NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadShowsForSelectedTheatre() {
        showComboBox.removeAllItems();
        String selectedTheatre = theatreComboBox.getSelectedItem().toString();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT MOVIE_TITLE, SHOWTIME FROM SHOWS WHERE THEATRE_ID = (SELECT THEATRE_ID FROM THEATRES WHERE NAME = ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, selectedTheatre);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String movieTitle = resultSet.getString("MOVIE_TITLE");
                String showTime = resultSet.getString("SHOWTIME");
                showComboBox.addItem(movieTitle + " - " + showTime);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TheatreSelection theatreSelection = new TheatreSelection();
            theatreSelection.setVisible(true);
        });
    }
}
