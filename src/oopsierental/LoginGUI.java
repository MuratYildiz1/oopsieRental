package oopsierental;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.formdev.flatlaf.FlatLightLaf;

public class LoginGUI extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    // Register fields
    private JTextField customerIdField;
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField registerEmailField;
    private JPasswordField setPasswordField;
    private JButton submitRegisterButton;
    private JButton backButton;

    private JPanel loginPanel;
    private JPanel registerPanel;

    public LoginGUI() {
        setLookAndFeel();
        setTitle("OOPSIE RENTAL - Login");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new CardLayout());

        createLoginPanel();
        createRegisterPanel();

        add(loginPanel, "Login");
        add(registerPanel, "Register");

        ((CardLayout) getContentPane().getLayout()).show(getContentPane(), "Login");
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(new JLabel("Email Address:"), gbc);

        emailField = new JTextField(20);
        gbc.gridy = 1;
        loginPanel.add(emailField, gbc);

        gbc.gridy = 2;
        loginPanel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(20);
        gbc.gridy = 3;
        loginPanel.add(passwordField, gbc);

        loginButton = new JButton("Log in");
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        loginPanel.add(loginButton, gbc);

        registerButton = new JButton("Register");
        gbc.gridx = 1;
        loginPanel.add(registerButton, gbc);

        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> showRegisterPanel());
    }

    private void createRegisterPanel() {
        registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        registerPanel.add(new JLabel("Customer ID (11 digits):"), gbc);

        customerIdField = new JTextField(20);
        gbc.gridy = 1;
        registerPanel.add(customerIdField, gbc);

        gbc.gridy = 2;
        registerPanel.add(new JLabel("Name:"), gbc);

        nameField = new JTextField(20);
        gbc.gridy = 3;
        registerPanel.add(nameField, gbc);

        gbc.gridy = 4;
        registerPanel.add(new JLabel("Surname:"), gbc);

        surnameField = new JTextField(20);
        gbc.gridy = 5;
        registerPanel.add(surnameField, gbc);

        gbc.gridy = 6;
        registerPanel.add(new JLabel("Email Address:"), gbc);

        registerEmailField = new JTextField(20);
        gbc.gridy = 7;
        registerPanel.add(registerEmailField, gbc);

        gbc.gridy = 8;
        registerPanel.add(new JLabel("Set Password:"), gbc);

        setPasswordField = new JPasswordField(20);
        gbc.gridy = 9;
        registerPanel.add(setPasswordField, gbc);

        submitRegisterButton = new JButton("Submit Register");
        gbc.gridy = 10;
        gbc.gridwidth = 1;
        registerPanel.add(submitRegisterButton, gbc);

        backButton = new JButton("Back to Login");
        gbc.gridx = 1;
        registerPanel.add(backButton, gbc);

        submitRegisterButton.addActionListener(e -> handleRegister());
        backButton.addActionListener(e -> showLoginPanel());
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Load customers and check login
        java.util.ArrayList<Customer> customers = FileManager.loadCustomers();
        boolean loginSuccess = false;
        for (Customer c : customers) {
            if (c.getEmail().equals(email) && c.getPassword().equals(password)) {
                loginSuccess = true;
                break;
            }
        }

        if (loginSuccess) {
            JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            // Open RentalGUI
            SwingUtilities.invokeLater(() -> {
                RentalGUI rentalGUI = new RentalGUI();
                rentalGUI.setVisible(true);
            });
            this.dispose(); // Close login window
        } else {
            JOptionPane.showMessageDialog(this, "Invalid email or password.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister() {
        String customerId = customerIdField.getText().trim();
        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String email = registerEmailField.getText().trim();
        String password = new String(setPasswordField.getPassword());

        if (customerId.isEmpty() || name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check Customer ID: exactly 11 digits
        if (!customerId.matches("\\d{11}")) {
            JOptionPane.showMessageDialog(this, "Customer ID must be exactly 11 digits.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check Email format
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (!email.matches(emailRegex)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if email already exists
        java.util.ArrayList<Customer> customers = FileManager.loadCustomers();
        for (Customer c : customers) {
            if (c.getEmail().equals(email)) {
                JOptionPane.showMessageDialog(this, "Email already registered.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Create and save customer
        Customer newCustomer = new Customer(customerId, name, surname, email, password);
        FileManager.saveCustomer(newCustomer);

        JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        showLoginPanel();
    }

    private void showRegisterPanel() {
        ((CardLayout) getContentPane().getLayout()).show(getContentPane(), "Register");
    }

    private void showLoginPanel() {
        ((CardLayout) getContentPane().getLayout()).show(getContentPane(), "Login");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginGUI gui = new LoginGUI();
            gui.setVisible(true);
        });
    }
}