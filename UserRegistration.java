import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserRegistration extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JTextField contactField;
    private JButton registerButton;

    public UserRegistration() {
        setTitle("User Registration");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        emailField = new JTextField();
        contactField = new JTextField();
        registerButton = new JButton("Register");

        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Contact No:"));
        add(contactField);
        add(registerButton);

        registerButton.addActionListener(new RegisterUserAction());
    }

    private class RegisterUserAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText();
            String contact = contactField.getText();

            try (Connection connection = DatabaseConnection.getConnection()) {
                String sql = "INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, CONTACT_NO) VALUES (?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setString(3, email);
                statement.setString(4, contact);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Registration successful!");
                dispose(); // Close registration window
                new TheatreSelection().setVisible(true); // Open theatre selection window
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Registration failed. Try again.");
            }
        }
    }
}
