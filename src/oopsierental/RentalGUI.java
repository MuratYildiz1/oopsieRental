/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package oopsierental;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author kerem
 * @author murat
 */
public class RentalGUI extends JFrame {

    private ArrayList<Customer> customers;
    private ArrayList<Vehicle> vehicles;

    // Swing Components
    private JComboBox<Customer> customerComboBox;
    private JComboBox<Vehicle> vehicleComboBox;
    private JTextField daysField;
    private JTable vehicleTable;
    private DefaultTableModel tableModel;

    public RentalGUI() {
        // 1. Startup: Load data from files or initialize with sample data
        initData();

        // 2. Main Window Setup
        setTitle("OOPSIE RENTAL - Vehicle Rental System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen
        setLayout(new BorderLayout());

        // 3. Segmented Interface
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Vehicle List", createVehicleListPanel());
        tabbedPane.addTab("Make Reservation", createReservationPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void initData() {
        ArrayList<Branch> branches = FileManager.loadBranches();
        // Fetch customers from file, if empty create sample customers
        customers = FileManager.loadCustomers();

        if (branches.isEmpty()) {
            branches.add(new Branch("BR01", "İzmir Center", "İzmir", "Bornova"));
            FileManager.saveBranches(branches);
        }
        if (customers.isEmpty()) {
            customers.add(new Customer("1", "Murat", "Yildiz"));
            customers.add(new Customer("2", "Kerem", "Güler"));
        }
        this.vehicles = FileManager.loadVehicles(branches);
        ArrayList<Employee> employees = FileManager.loadEmployees(branches);

        // Fetch vehicles from file, if empty create sample vehicles
        vehicles = FileManager.loadVehicles();
        if (vehicles.isEmpty()) {
            vehicles.add(new SUV("35ABC123", "Dacia Duster", 1000.0));
            vehicles.add(new Economy("35XYZ789", "Renault Clio", 500.0));
            vehicles.add(new Luxury("34LXC001", "Mercedes C200", 2500.0));
            vehicles.add(new Van("06VAN444", "Ford Transit", 1200.0));
            // Yeni araçları dosyaya kaydet
            FileManager.saveVehicles(vehicles);
        }
    }

    // --- 1: Vehicle List (Read Method) ---
    private JPanel createVehicleListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columns = { "Plate", "Brand", "Type", "Daily Rate", "Status" };
        tableModel = new DefaultTableModel(columns, 0);
        vehicleTable = new JTable(tableModel);

        refreshTable(); // Fill the table with current vehicle data

        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        panel.add(new JLabel("All Vehicles:"), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void refreshTable() {
        tableModel.setRowCount(0); // Clear the rows
        for (Vehicle v : vehicles) {
            String status = v.isRented() ? "Rented (" + v.getRentedDays() + " days left)" : "Available";
            Object[] row = {
                    v.getPlate(),
                    v.getBrand(),
                    v.getClass().getSimpleName(),
                    v.calculateRent(1) + " TL",
                    status
            };
            tableModel.addRow(row);
        }
    }

    // --- 2: Reservation Screen (Create Method & Error Handling) ---
    private JPanel createReservationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Make New Reservation"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Customer Selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Select Customer:"), gbc);
        customerComboBox = new JComboBox<>(customers.toArray(new Customer[0]));
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(customerComboBox, gbc);

        // Vehicle Selection
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Select Vehicle:"), gbc);
        vehicleComboBox = new JComboBox<>(vehicles.toArray(new Vehicle[0]));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(vehicleComboBox, gbc);

        // Days Input
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Rental Duration (Days):"), gbc);
        daysField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(daysField, gbc);

        // Rent Button
        JButton rentButton = new JButton("Rent Vehicle and Generate Invoice");
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(rentButton, gbc);

        // EVENT HANDLING - Button Action
        rentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleReservation();
            }
        });

        return panel;
    }

    // ERROR HANDLING
    private void handleReservation() {
        try {
            Customer selectedCustomer = (Customer) customerComboBox.getSelectedItem();
            Vehicle selectedVehicle = (Vehicle) vehicleComboBox.getSelectedItem();

            // Null Check
            if (selectedCustomer == null) {
                JOptionPane.showMessageDialog(this, "Please select a customer!", "Missing Selection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (selectedVehicle == null) {
                JOptionPane.showMessageDialog(this, "Please select a vehicle!", "Missing Selection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 1. Input Validation: Empty field check
            if (daysField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the number of days!", "Missing Information",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Input Validation: Number format check and positive value check
            int days = Integer.parseInt(daysField.getText().trim());
            if (days <= 0) {
                JOptionPane.showMessageDialog(this, "The number of days must be a positive integer!", "Invalid Value",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 3. Job Logic and Exception Handling: Create reservation and handle potential
            // errors
            // Reservation constructor will check availability and calculate total price; it
            // may throw RentalException if the vehicle is already rented
            Reservation res = new Reservation("RES-" + System.currentTimeMillis(), selectedCustomer, selectedVehicle,
                    days);

            // If no exception was thrown, the reservation was successful. Save the
            // reservation and invoice.
            FileManager.saveReservation(res);

            Invoice inv = new Invoice("INV-" + System.currentTimeMillis(), res);
            FileManager.saveInvoice(inv);

            // Update the vehicle list to reflect the new rental status
            selectedVehicle.setRented(true);
            selectedVehicle.setRentedDays(days);
            refreshTable();

            // Success Message with Invoice Number
            JOptionPane.showMessageDialog(this,
                    "Reservation completed successfully!\nInvoice No: " + inv.getInvoiceId(),
                    "Operation Successful",
                    JOptionPane.INFORMATION_MESSAGE);

            // Clear the form
            daysField.setText("");

        } catch (NumberFormatException ex) {
            // Shouldn't crash the program if user enters non-numeric value for days; show
            // error message instead
            JOptionPane.showMessageDialog(this, "Please enter only numbers for the number of days!", "Format Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (RentalException ex) {
            // If the vehicle is already rented, show the specific error message from the
            // exception
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Availability Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            // Unexpected exceptions should also be caught to prevent crashes; show a
            // generic error message with details
            JOptionPane.showMessageDialog(this, "System error occurred: " + ex.getMessage(), "Critical Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            RentalGUI gui = new RentalGUI();
            gui.setVisible(true);
        });
    }
}