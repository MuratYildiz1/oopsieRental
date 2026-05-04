/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package oopsierental;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kerem
 * @author murat
 */
public class RentalGUI extends JFrame {

    private ArrayList<Customer> customers;
    private ArrayList<Vehicle> vehicles;
    private List<Vehicle> displayedVehicles;

    // Swing Components
    private JComboBox<Customer> customerComboBox;
    private JComboBox<Vehicle> vehicleComboBox;
    private JComboBox<String> categoryFilterComboBox;
    private JComboBox<String> priceSortComboBox;
    private JTextField daysField;
    private JTable vehicleTable;
    private DefaultTableModel tableModel;
    private JButton cancelRentalButton;
    private JLabel statusLabel;

    public RentalGUI() {
        setLookAndFeel();
        initData();
        initUI();
    }

    private void setLookAndFeel() {
        try {
            Class<?> flatLafClass = Class.forName("com.formdev.flatlaf.FlatLightLaf");
            Method setup = flatLafClass.getMethod("setup");
            setup.invoke(null);
        } catch (Exception ignored) {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception ex) {
                System.err.println("Unable to set Nimbus LAF: " + ex.getMessage());
            }
        }
    }

    private void initUI() {
        setTitle("OOPSIE RENTAL - Vehicle Rental System");
        setSize(980, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));

        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(12, 12, 0, 12));
        JLabel titleLabel = new JLabel("OOPSIE RENTAL - Vehicle Rental System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.add(titleLabel, BorderLayout.WEST);

        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        header.add(statusLabel, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Vehicle Catalog", createVehicleListPanel());
        tabbedPane.addTab("Make Reservation", createReservationPanel());
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void initData() {
        ArrayList<Branch> branches = FileManager.loadBranches();
        if (branches.isEmpty()) {
            Branch defaultBranch = new Branch("BR01", "İzmir Center", "İzmir");
            branches.add(defaultBranch);
            FileManager.saveBranches(branches);
        }

        customers = FileManager.loadCustomers();
        if (customers.isEmpty()) {
            Customer c1 = new Customer("1", "Murat", "Yildiz", "murat@yildiz.com", "123");
            Customer c2 = new Customer("2", "Kerem", "Güler", "kerem@guler.com", "123");
            customers.add(c1);
            customers.add(c2);
            FileManager.saveCustomer(c1);
            FileManager.saveCustomer(c2);
        }

        vehicles = FileManager.loadVehicles(branches);
        if (vehicles.isEmpty()) {
            Branch defaultBranch = branches.get(0);
            vehicles.add(new SUV("35ABC123", "Dacia Duster", 1000.0, defaultBranch));
            vehicles.add(new Economy("35XYZ789", "Renault Clio", 500.0, defaultBranch));
            vehicles.add(new Luxury("34LXC001", "Mercedes C200", 2500.0, defaultBranch));
            vehicles.add(new Van("06VAN444", "Ford Transit", 1200.0, defaultBranch));
            FileManager.saveVehicles(vehicles);
        }

        displayedVehicles = new ArrayList<>(vehicles);
    }

    // --- 1: Vehicle List (Read Method) ---
    private JPanel createVehicleListPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter & Actions"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        filterPanel.add(new JLabel("Category:"), gbc);

        categoryFilterComboBox = new JComboBox<>(new String[] { "All Types", "Economy", "SUV", "Luxury", "Van" });
        gbc.gridx = 1;
        filterPanel.add(categoryFilterComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        filterPanel.add(new JLabel("Price Sort:"), gbc);

        priceSortComboBox = new JComboBox<>(new String[] { "Default", "High → Low", "Low → High" });
        gbc.gridx = 1;
        filterPanel.add(priceSortComboBox, gbc);

        JButton applyButton = new JButton("Apply");
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        filterPanel.add(applyButton, gbc);

        cancelRentalButton = new JButton("Cancel Selected Rental");
        cancelRentalButton.setEnabled(false);
        gbc.gridx = 3;
        gbc.gridy = 0;
        filterPanel.add(cancelRentalButton, gbc);

        panel.add(filterPanel, BorderLayout.NORTH);

        String[] columns = { "Plate", "Brand", "Category", "Branch", "Daily Rate", "Status" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        vehicleTable = new JTable(tableModel);
        vehicleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        refreshTable();

        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        applyButton.addActionListener(e -> applyFiltersAndSort());
        cancelRentalButton.addActionListener(e -> handleCancelRental());
        vehicleTable.getSelectionModel().addListSelectionListener(e -> updateCancelButtonState());

        return panel;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Vehicle v : displayedVehicles) {
            String status = v.isRented() ? "Rented (" + v.getRentedDays() + " days left)" : "Available";
            tableModel.addRow(new Object[] {
                    v.getPlate(),
                    v.getBrand(),
                    v.getClass().getSimpleName(),
                    v.getBranch().getBranchName(),
                    String.format("%.2f TL", v.getDailyRate()),
                    status
            });
        }
        updateCancelButtonState();
    }

    // --- 2: Reservation Screen (Create Method & Error Handling) ---
    private JPanel createReservationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Make New Reservation"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Select Customer:"), gbc);

        customerComboBox = new JComboBox<>(customers.toArray(new Customer[0]));
        gbc.gridx = 1;
        panel.add(customerComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Select Vehicle:"), gbc);

        vehicleComboBox = new JComboBox<>(getAvailableVehicles().toArray(new Vehicle[0]));
        gbc.gridx = 1;
        panel.add(vehicleComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Rental Duration (Days):"), gbc);

        daysField = new JTextField(10);
        gbc.gridx = 1;
        panel.add(daysField, gbc);

        JButton rentButton = new JButton("Rent Vehicle and Generate Invoice");
        rentButton.setBackground(new Color(45, 115, 255));
        rentButton.setForeground(Color.WHITE);
        rentButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(rentButton, gbc);

        rentButton.addActionListener(e -> handleReservation());

        return panel;
    }

    // ERROR HANDLING
    private void handleReservation() {
        try {
            Customer selectedCustomer = (Customer) customerComboBox.getSelectedItem();
            Vehicle selectedVehicle = (Vehicle) vehicleComboBox.getSelectedItem();

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
            if (selectedVehicle.isRented()) {
                JOptionPane.showMessageDialog(this, "Selected vehicle is already rented.", "Unavailable",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (daysField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the number of days!", "Missing Information",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int days = Integer.parseInt(daysField.getText().trim());
            if (days <= 0) {
                JOptionPane.showMessageDialog(this, "The number of days must be a positive integer!", "Invalid Value",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Reservation res = new Reservation("RES-" + System.currentTimeMillis(), selectedCustomer,
                    selectedVehicle, days);
            FileManager.saveReservation(res);

            Invoice inv = new Invoice("INV-" + System.currentTimeMillis(), res, days, 0);
            FileManager.saveInvoice(inv);

            selectedVehicle.setRented(true);
            selectedVehicle.setRentedDays(days);
            FileManager.saveVehicles(vehicles);
            updateVehicleCombo();
            applyFiltersAndSort();
            daysField.setText("");
            statusLabel.setText("Reservation completed: " + inv.getInvoiceId());

            JOptionPane.showMessageDialog(this,
                    "Reservation completed successfully!\nInvoice No: " + inv.getInvoiceId(),
                    "Operation Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter only numbers for the number of days!", "Format Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (RentalException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Availability Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "System error occurred: " + ex.getMessage(), "Critical Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyFiltersAndSort() {
        String selectedCategory = (String) categoryFilterComboBox.getSelectedItem();
        String selectedSort = (String) priceSortComboBox.getSelectedItem();

        displayedVehicles = vehicles.stream()
                .filter(v -> selectedCategory == null || selectedCategory.equals("All Types")
                        || v.getClass().getSimpleName().equals(selectedCategory))
                .collect(Collectors.toList());

        if ("High → Low".equals(selectedSort)) {
            displayedVehicles.sort(Comparator.comparingDouble(Vehicle::getDailyRate).reversed());
        } else if ("Low → High".equals(selectedSort)) {
            displayedVehicles.sort(Comparator.comparingDouble(Vehicle::getDailyRate));
        }

        refreshTable();
        statusLabel.setText("Filter: " + selectedCategory + " | Sort: " + selectedSort);
    }

    private void handleCancelRental() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a rented vehicle from the list first.",
                    "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Vehicle selectedVehicle = displayedVehicles.get(selectedRow);
        if (!selectedVehicle.isRented()) {
            JOptionPane.showMessageDialog(this, "The selected vehicle is not currently rented.", "Invalid Action",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String input = JOptionPane.showInputDialog(this,
                "Kaç gün kullanıldı?\n(5 dk sonra iptal için 0 giriniz, 3.5 gün sonra iptal için 3 giriniz.)",
                "Kiralama İptal Ücreti Hesaplama", JOptionPane.QUESTION_MESSAGE);
        if (input == null) {
            return;
        }
        try {
            int usedDays = Integer.parseInt(input.trim());
            if (usedDays < 0) {
                throw new NumberFormatException();
            }

            int reservedDays = selectedVehicle.getRentedDays();
            int chargedDays = Math.min(reservedDays, Math.max(1, usedDays + 1));
            double fee = chargedDays * selectedVehicle.getDailyRate();

            selectedVehicle.setRented(false);
            selectedVehicle.setRentedDays(0);
            FileManager.saveVehicles(vehicles);
            updateVehicleCombo();
            applyFiltersAndSort();
            statusLabel.setText("Rental cancelled: " + chargedDays + " day(s) charged");

            JOptionPane.showMessageDialog(this,
                    "Rental cancelled.\nMinimum charge: " + chargedDays + " day(s)\nToplam: " +
                            String.format("%.2f TL", fee),
                    "Cancellation Completed", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lütfen geçerli bir tam sayı girin.", "Format Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateVehicleCombo() {
        vehicleComboBox.setModel(new DefaultComboBoxModel<>(getAvailableVehicles().toArray(new Vehicle[0])));
    }

    private List<Vehicle> getAvailableVehicles() {
        return vehicles.stream().filter(v -> !v.isRented()).collect(Collectors.toList());
    }

    private void updateCancelButtonState() {
        int selectedRow = vehicleTable.getSelectedRow();
        boolean enabled = selectedRow >= 0 && displayedVehicles.get(selectedRow).isRented();
        cancelRentalButton.setEnabled(enabled);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            RentalGUI gui = new RentalGUI();
            gui.setVisible(true);
        });
    }
}