package oopsierental;

/**
 * @author MuratYildiz1
 * @author KeremHKardes
 */

import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.FlatDarkLaf;

public class LoginGUI extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    private JPanel loginPanel;
    private JPanel registerPanel;

    public LoginGUI() {
        setLookAndFeel();
        setTitle("OOPSIE RENTAL - Login");
        setSize(500, 450);
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
            FlatDarkLaf.setup();
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception ex) {
                // Fallback handled implicitly
            }
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
        loginPanel.add(new JLabel("Username / Email:"), gbc);

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

        registerButton = new JButton("Register Employee");
        gbc.gridx = 1;
        loginPanel.add(registerButton, gbc);

        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> showRegisterPanel());
    }

    private void createRegisterPanel() {
        registerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<String> typeCombo = new JComboBox<>(new String[] { "Rental Agent", "Mechanic" });
        JTextField idField = new JTextField(15);
        JTextField nameFieldReg = new JTextField(15);
        JTextField surnameFieldReg = new JTextField(15);
        JTextField emailFieldReg = new JTextField(15);
        JPasswordField passFieldReg = new JPasswordField(15);

        int y = 0;
        addLabelAndField(registerPanel, "Employee Type:", typeCombo, gbc, y++);
        addLabelAndField(registerPanel, "ID Number (11 Digits):", idField, gbc, y++);
        addLabelAndField(registerPanel, "Name:", nameFieldReg, gbc, y++);
        addLabelAndField(registerPanel, "Surname:", surnameFieldReg, gbc, y++);
        addLabelAndField(registerPanel, "Username / Email:", emailFieldReg, gbc, y++);
        addLabelAndField(registerPanel, "Password:", passFieldReg, gbc, y++);

        JButton submitBtn = new JButton("Submit Register");
        JButton backBtn = new JButton("Back to Login");

        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        registerPanel.add(submitBtn, gbc);
        gbc.gridx = 1;
        registerPanel.add(backBtn, gbc);

        backBtn.addActionListener(e -> showLoginPanel());

        submitBtn.addActionListener(e -> {
            String type = (String) typeCombo.getSelectedItem();
            String id = idField.getText().trim();
            String name = nameFieldReg.getText().trim();
            String surname = surnameFieldReg.getText().trim();
            String username = emailFieldReg.getText().trim();
            String pass = new String(passFieldReg.getPassword());

            // Validate empty fields
            if (id.isEmpty() || name.isEmpty() || surname.isEmpty() || username.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate 11-digit ID requirement
            if (!id.matches("\\d{11}")) {
                JOptionPane.showMessageDialog(this, "ID Number must be exactly 11 digits.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String fullName = name + " " + surname;

            // Load branches and employees to update the list
            java.util.ArrayList<Branch> branches = FileManager.loadBranches();
            java.util.ArrayList<Employee> employees = FileManager.loadEmployees(branches);

            // Assign default branch if none exists to prevent NullPointerException
            Branch branch = branches.isEmpty() ? new Branch("BR00", "Main Branch", "Izmir") : branches.get(0);

            // Instantiate appropriate Employee subclass using Polymorphism
            Employee newEmployee;
            if (type.equals("Mechanic")) {
                newEmployee = new Mechanic(id, fullName, username, pass, branch);
            } else {
                newEmployee = new RentalAgent(id, fullName, username, pass, branch);
            }

            // Append to list and save to file
            employees.add(newEmployee);
            FileManager.saveEmployees(employees);

            JOptionPane.showMessageDialog(this, "Employee (" + type + ") Registered Successfully!");

            // Clear fields after successful registration
            idField.setText("");
            nameFieldReg.setText("");
            surnameFieldReg.setText("");
            emailFieldReg.setText("");
            passFieldReg.setText("");

            showLoginPanel();
        });
    }

    private void addLabelAndField(JPanel p, String label, JComponent field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        p.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        p.add(field, gbc);
    }

    private void handleLogin() {
        String usernameInput = emailField.getText().trim();
        String passwordInput = new String(passwordField.getPassword());

        if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Dashboard login checks Employee credentials, not Customer credentials
        java.util.ArrayList<Branch> branches = FileManager.loadBranches();
        java.util.ArrayList<Employee> employees = FileManager.loadEmployees(branches);
        boolean loginSuccess = false;
        Employee loggedInUser = null;

        for (Employee e : employees) {
            if (e.getUsername().equals(usernameInput) && e.getPassword().equals(passwordInput)) {
                loginSuccess = true;
                loggedInUser = e;
                break;
            }
        }

        if (loginSuccess) {
            String fullName = loggedInUser.getFullName();
            JOptionPane.showMessageDialog(this, "Login successful!\nWelcome, " + fullName, "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            SwingUtilities.invokeLater(() -> {
                RentalGUI rentalGUI = new RentalGUI(fullName);
                rentalGUI.setVisible(true);
            });
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showRegisterPanel() {
        ((CardLayout) getContentPane().getLayout()).show(getContentPane(), "Register");
    }

    private void showLoginPanel() {
        ((CardLayout) getContentPane().getLayout()).show(getContentPane(), "Login");
    }
}