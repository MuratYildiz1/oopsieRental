package oopsierental;

/**
 * @author MuratYildiz1
 * @author KeremHKardes
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import com.formdev.flatlaf.FlatDarkLaf;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RentalGUI extends JFrame {

    private String loggedInEmployeeName;

    private ArrayList<Customer> customers;
    private ArrayList<Vehicle> vehicles;
    private ArrayList<Branch> branches;
    private List<Vehicle> displayedVehicles;
    private List<Reservation> activeReservations;

    // GUI Caches
    private Map<String, String> unavailableReasons = new HashMap<>();
    private Map<String, String> assignedMechanics = new HashMap<>();
    private Map<String, String> lastRentedBy = new HashMap<>();
    private Map<String, String> reservedByEmployee = new HashMap<>();
    private Map<String, String> unavailableAddedBy = new HashMap<>();

    static class MaintenanceRecord {
        String plate, brand, reason, mechanic, notes, admin;

        public MaintenanceRecord(String plate, String brand, String reason, String mechanic, String notes,
                String admin) {
            this.plate = plate;
            this.brand = brand;
            this.reason = reason;
            this.mechanic = mechanic;
            this.notes = notes;
            this.admin = admin;
        }
    }

    private List<MaintenanceRecord> maintenanceHistory = new ArrayList<>();

    private JTabbedPane tabbedPane;
    private JTable carListTable, unavailableTable, rentedTable;
    private JComboBox<String> categoryFilterCombo, priceSortCombo, cityFilterCombo;
    private JLabel statusLabel;

    // Search fields
    private JTextField rentedSearchField;
    private JTextField returnSearchField;

    // Visible List Box for Return Panel
    private JList<String> returnReservationList;
    private DefaultListModel<String> returnListModel;

    private JLabel retCarLbl, retDaysLbl, retPickupLbl, retDropoffLbl, retInsuranceLbl, retDepositLbl;
    private JCheckBox washingCheck, missingObjectCheck;

    public RentalGUI(String employeeName) {
        this.loggedInEmployeeName = employeeName;
        setLookAndFeel();
        initData();
        loadMaintenanceHistory();
        initUI();
        statusLabel.setText("System Ready. Logged in as: " + loggedInEmployeeName);
    }

    public RentalGUI() {
        this("Admin User");
    }

    private void setLookAndFeel() {
        try {
            FlatDarkLaf.setup();
        } catch (Exception ignored) {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception ex) {
            }
        }
    }

    private void initData() {
        branches = FileManager.loadBranches();
        customers = FileManager.loadCustomers();
        vehicles = FileManager.loadVehicles(branches);
        displayedVehicles = new ArrayList<>(vehicles);

        // --- BUG FIX: Load active reservations and rebuild GUI Caches ---
        try {
            // Fetch saved reservations from the database/file
            activeReservations = FileManager.loadReservations(customers, vehicles);

            // If FileManager returns null for some reason, initialize an empty list
            if (activeReservations == null) {
                activeReservations = new ArrayList<>();
            }

            // Rebuild the memory hashmaps so the Rented and Unavailable tables
            // know exactly who rented which car after a system restart.
            for (Reservation res : activeReservations) {
                lastRentedBy.put(res.getVehicle().getPlate(),
                        res.getCustomer().getName() + " " + res.getCustomer().getSurname());
                reservedByEmployee.put(res.getReservationId(), res.getEmployee());

                // Ensure vehicle is marked as rented
                res.getVehicle().setRented(true);
                res.getVehicle().setRentedDays(res.getDays());
            }
        } catch (Exception e) {
            System.err.println("Notice: Could not load active reservations or file is empty. Starting fresh.");
            activeReservations = new ArrayList<>();
        }

        // Load stored unavailable maintenance state if available.
        FileManager.loadMaintenanceState(vehicles, unavailableReasons, assignedMechanics, unavailableAddedBy,
                lastRentedBy);
        // ----------------------------------------------------------------

        for (Vehicle v : vehicles) {
            if (v.isUnderMaintenance()) {
                unavailableReasons.putIfAbsent(v.getPlate(), "Maintenance");
                assignedMechanics.putIfAbsent(v.getPlate(), "Pending");
                unavailableAddedBy.putIfAbsent(v.getPlate(), "System");
            }
        }
    }

    private void loadMaintenanceHistory() {
        try (BufferedReader br = new BufferedReader(new FileReader("maintenance_history.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("~");
                if (parts.length == 6) {
                    maintenanceHistory
                            .add(new MaintenanceRecord(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]));
                }
            }
        } catch (Exception e) {
            // Safe to ignore if file does not exist yet.
        }
    }

    private void saveMaintenanceRecord(MaintenanceRecord record) {
        maintenanceHistory.add(record);
        try (PrintWriter out = new PrintWriter(new FileWriter("maintenance_history.txt", true))) {
            out.println(record.plate + "~" + record.brand + "~" + record.reason + "~" + record.mechanic + "~"
                    + record.notes + "~" + record.admin);
        } catch (Exception e) {
            System.err.println("Failed to save maintenance history.");
        }
    }

    private void initUI() {
        setTitle("OOPSIE RENTAL - Dashboard");
        setSize(1200, 900);
        setMinimumSize(new Dimension(1100, 800));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(15, 15, 5, 15));
        JLabel titleLabel = new JLabel("OOPSIE RENTAL - Management Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.add(titleLabel, BorderLayout.WEST);

        statusLabel = new JLabel("System Ready.");
        header.add(statusLabel, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        tabbedPane.addTab("Car List", createCarListPanel());
        tabbedPane.addTab("Unavailable Cars", createUnavailablePanel());
        tabbedPane.addTab("Rented", createRentedPanel());
        tabbedPane.addTab("Return", createReturnPanel());

        tabbedPane.addChangeListener(e -> refreshAllTables());
        add(tabbedPane, BorderLayout.CENTER);

        // Populate initial car list immediately after the UI is built.
        applyCarFilters();
    }

    private JPanel createCarListPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Sort & Filter"));

        categoryFilterCombo = new JComboBox<>(new String[] { "All Types", "Economy", "SUV", "Luxury", "Van" });
        priceSortCombo = new JComboBox<>(new String[] { "Default", "Price: High to Low", "Price: Low to High" });

        List<String> cities = branches.stream().map(Branch::getCity).distinct().collect(Collectors.toList());
        cities.add(0, "All Cities");
        cityFilterCombo = new JComboBox<>(cities.toArray(new String[0]));

        JButton applyBtn = new JButton("Apply Filters");
        applyBtn.addActionListener(e -> applyCarFilters());

        filterPanel.add(new JLabel("Type:"));
        filterPanel.add(categoryFilterCombo);
        filterPanel.add(new JLabel("Sort:"));
        filterPanel.add(priceSortCombo);
        filterPanel.add(new JLabel("City:"));
        filterPanel.add(cityFilterCombo);
        filterPanel.add(applyBtn);

        panel.add(filterPanel, BorderLayout.NORTH);

        carListTable = new JTable();
        carListTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        carListTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                    handleCarSelection();
            }
        });

        panel.add(new JScrollPane(carListTable), BorderLayout.CENTER);

        JButton rentBtn = new JButton("Rent Selected Car");
        rentBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        rentBtn.addActionListener(e -> handleCarSelection());
        panel.add(rentBtn, BorderLayout.SOUTH);

        return panel;
    }

    private void applyCarFilters() {
        String type = (String) categoryFilterCombo.getSelectedItem();
        String sort = (String) priceSortCombo.getSelectedItem();
        String city = (String) cityFilterCombo.getSelectedItem();

        displayedVehicles = vehicles.stream()
                .filter(v -> type.equals("All Types") || v.getClass().getSimpleName().equals(type))
                .filter(v -> city.equals("All Cities") || v.getBranch().getCity().equals(city))
                .collect(Collectors.toList());

        if (sort.equals("Price: High to Low")) {
            displayedVehicles.sort((v1, v2) -> Double.compare(v2.getDailyRate(), v1.getDailyRate()));
        } else if (sort.equals("Price: Low to High")) {
            displayedVehicles.sort(Comparator.comparingDouble(Vehicle::getDailyRate));
        }
        updateCarTable();
    }

    private void updateCarTable() {
        String[] cols = { "Plate", "Brand", "Type", "Current Location", "Daily Rate", "Status" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        for (Vehicle v : displayedVehicles) {
            String status = v.isRented() ? "Rented" : (v.isUnderMaintenance() ? "Unavailable" : "Available");
            model.addRow(new Object[] { v.getPlate(), v.getBrand(), v.getClass().getSimpleName(),
                    v.getBranch().getCity(), v.getDailyRate() + " TL", status });
        }
        carListTable.setModel(model);
    }

    private void handleCarSelection() {
        int row = carListTable.getSelectedRow();
        if (row < 0)
            return;

        Vehicle selectedVehicle = displayedVehicles.get(row);
        if (selectedVehicle.isRented() || selectedVehicle.isUnderMaintenance()) {
            JOptionPane.showMessageDialog(this, "This car is currently not available.", "Unavailable",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        openRentalDialog(selectedVehicle);
    }

    private void openRentalDialog(Vehicle vehicle) {
        JDialog dialog = new JDialog(this, "Rent Vehicle: " + vehicle.getBrand(), true);
        dialog.setSize(500, 550);
        dialog.setLayout(new GridBagLayout());
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = new JTextField(15);
        JTextField nameField = new JTextField(15);
        JTextField surnameField = new JTextField(15);
        JTextField daysField = new JTextField(5);

        JComboBox<String> insuranceCombo = new JComboBox<>(
                new String[] { "Basic - 300 TL/day", "Advanced - 700 TL/day", "Premium - 1200 TL/day" });

        // Sort cities alphabetically and add a default placeholder
        List<String> cities = branches.stream()
                .map(Branch::getCity)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        cities.add(0, "--- Select City ---");
        JComboBox<String> returnCityCombo = new JComboBox<>(cities.toArray(new String[0]));

        JLabel totalLabel = new JLabel("Total amount to be paid: 0 TL");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalLabel.setForeground(new Color(0, 102, 204));

        int y = 0;
        gbc.gridx = 0;
        gbc.gridy = y;
        dialog.add(new JLabel("Customer ID:"), gbc);
        gbc.gridx = 1;
        dialog.add(idField, gbc);
        gbc.gridx = 0;
        gbc.gridy = ++y;
        dialog.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        dialog.add(nameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = ++y;
        dialog.add(new JLabel("Surname:"), gbc);
        gbc.gridx = 1;
        dialog.add(surnameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = ++y;
        dialog.add(new JLabel("Insurance Type:"), gbc);
        gbc.gridx = 1;
        dialog.add(insuranceCombo, gbc);
        gbc.gridx = 0;
        gbc.gridy = ++y;
        dialog.add(new JLabel("Pick-up Location:"), gbc);
        gbc.gridx = 1;
        dialog.add(new JLabel(vehicle.getBranch().getCity()), gbc);
        gbc.gridx = 0;
        gbc.gridy = ++y;
        dialog.add(new JLabel("Return Location:"), gbc);
        gbc.gridx = 1;
        dialog.add(returnCityCombo, gbc);
        gbc.gridx = 0;
        gbc.gridy = ++y;
        dialog.add(new JLabel("Day(s) of Rent:"), gbc);
        gbc.gridx = 1;
        dialog.add(daysField, gbc);

        JButton calcBtn = new JButton("Calculate Receipt");
        gbc.gridx = 0;
        gbc.gridy = ++y;
        gbc.gridwidth = 2;
        dialog.add(calcBtn, gbc);
        gbc.gridy = ++y;
        dialog.add(totalLabel, gbc);

        JButton confirmBtn = new JButton("Confirm Rent");
        confirmBtn.setBackground(new Color(34, 139, 34));
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setEnabled(false);
        gbc.gridy = ++y;
        dialog.add(confirmBtn, gbc);

        calcBtn.addActionListener(e -> {
            try {
                if (returnCityCombo.getSelectedIndex() <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Please select a valid return location.", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int days = Integer.parseInt(daysField.getText());
                int insDailyCost = insuranceCombo.getSelectedIndex() == 0 ? 300
                        : (insuranceCombo.getSelectedIndex() == 1 ? 700 : 1200);

                // Get customer by ID to determine loyalty discount
                String customerId = idField.getText().trim();
                Customer cust = customers.stream()
                        .filter(c -> c.getId().equals(customerId))
                        .findFirst()
                        .orElse(null);

                double discountRate = (cust != null) ? cust.getDiscountRate() : 0.0;
                double baseRental = days * vehicle.getDailyRate();
                double insuranceCost = days * insDailyCost;
                double subtotal = baseRental + insuranceCost;
                double discountAmount = subtotal * discountRate;
                double discountedSubtotal = subtotal - discountAmount;
                double total = discountedSubtotal + 5000.0;

                String tierInfo = (cust != null)
                        ? " (" + cust.getLoyaltyTier() + " - " + String.format("%.0f%%", discountRate * 100)
                                + " discount)"
                        : " (Bronze - 0% discount)";
                totalLabel.setText("Total amount to be paid: " + String.format("%.2f", total) + " TL" + tierInfo);
                confirmBtn.setEnabled(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid number of days.");
            }
        });

        confirmBtn.addActionListener(e -> {
            try {
                if (returnCityCombo.getSelectedIndex() <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Please select a valid return location.", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String customerId = idField.getText().trim();
                int days = Integer.parseInt(daysField.getText());

                // Try to find existing customer by ID to get correct loyalty tier
                Customer cust = customers.stream()
                        .filter(c -> c.getId().equals(customerId))
                        .findFirst()
                        .orElse(null);

                // If customer not found, create new one
                if (cust == null) {
                    cust = new Customer(customerId, nameField.getText(), surnameField.getText(),
                            "user@mail.com", "123");
                    customers.add(cust);
                    FileManager.saveCustomer(cust);
                }

                String rawInsStr = (String) insuranceCombo.getSelectedItem();
                String insType = rawInsStr.split(" -")[0];

                int insDailyCost = insuranceCombo.getSelectedIndex() == 0 ? 300
                        : (insuranceCombo.getSelectedIndex() == 1 ? 700 : 1200);
                String returnCity = (String) returnCityCombo.getSelectedItem();

                Reservation res = new Reservation("RES-" + System.currentTimeMillis(), cust, vehicle, days, insType,
                        insDailyCost, returnCity, loggedInEmployeeName);

                activeReservations.add(res);
                lastRentedBy.put(vehicle.getPlate(), cust.getName() + " " + cust.getSurname());
                reservedByEmployee.put(res.getReservationId(), loggedInEmployeeName);

                // Set vehicle as rented
                vehicle.setRented(true);
                vehicle.setRentedDays(days);

                FileManager.saveReservation(res);
                FileManager.saveVehicles(vehicles);
                FileManager.saveCustomer(cust);

                dialog.dispose();
                refreshAllTables();
                JOptionPane.showMessageDialog(this,
                        "Payment received. Vehicle rented successfully!\n" +
                                "Customer: " + cust.getName() + " " + cust.getSurname() + "\n" +
                                "Loyalty Tier: " + cust.getLoyaltyTier() + " (" + cust.getLoyaltyPoints() + " points)");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error processing rental: " + ex.getMessage());
            }
        });

        dialog.setVisible(true);
    }

    private JPanel createUnavailablePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addCarBtn = new JButton("Add Car to Unavailable");
        JButton historyBtn = new JButton("View Maintenance History");

        addCarBtn.addActionListener(e -> openAddUnavailableCarDialog());
        historyBtn.addActionListener(e -> openHistoryDialog());

        topPanel.add(addCarBtn);
        topPanel.add(historyBtn);
        panel.add(topPanel, BorderLayout.NORTH);

        unavailableTable = new JTable();
        unavailableTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        unavailableTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)
                    resolveUnavailableCar();
            }
        });

        panel.add(new JScrollPane(unavailableTable), BorderLayout.CENTER);

        JLabel hint = new JLabel("Double-click a vehicle to resolve its unavailable status and add mechanic notes.");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        panel.add(hint, BorderLayout.SOUTH);
        return panel;
    }

    private void updateUnavailableTable() {
        String[] cols = { "Plate", "Brand", "Reason", "Renter", "Mechanic", "Added By" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        for (Vehicle v : vehicles) {
            if (v.isUnderMaintenance()) {
                String reason = unavailableReasons.getOrDefault(v.getPlate(), "Unknown");
                String mechanic = assignedMechanics.getOrDefault(v.getPlate(), "Pending");
                String admin = unavailableAddedBy.getOrDefault(v.getPlate(), "System");
                String renter = lastRentedBy.getOrDefault(v.getPlate(), "-");

                model.addRow(new Object[] { v.getPlate(), v.getBrand(), reason, renter, mechanic, admin });
            }
        }
        unavailableTable.setModel(model);
    }

    private void openAddUnavailableCarDialog() {
        JDialog dialog = new JDialog(this, "Add Vehicle to Unavailable", true);
        dialog.setSize(550, 500);
        dialog.setLayout(new GridBagLayout());
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        JTextField searchField = new JTextField(20);
        DefaultListModel<Vehicle> listModel = new DefaultListModel<>();
        JList<Vehicle> resultList = new JList<>(listModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane listScroll = new JScrollPane(resultList);
        listScroll.setPreferredSize(new Dimension(450, 150));
        gbc.weighty = 1.0;

        JComboBox<String> reasonCombo = new JComboBox<>(new String[] { "Crash", "Maintenance" });

        List<String> mechanics = FileManager.loadEmployees(branches).stream()
                .filter(emp -> emp instanceof Mechanic)
                .map(Employee::getName)
                .collect(Collectors.toList());

        if (mechanics.isEmpty())
            mechanics.add("No Mechanic Assigned");

        JComboBox<String> mechanicCombo = new JComboBox<>(mechanics.toArray(new String[0]));

        Runnable refreshList = () -> {
            String text = searchField.getText().toLowerCase();
            listModel.clear();
            for (Vehicle v : vehicles) {
                if (!v.isUnderMaintenance()) {
                    String renter = v.isRented() ? lastRentedBy.getOrDefault(v.getPlate(), "") : "";
                    if (text.isEmpty() || v.getPlate().toLowerCase().contains(text)
                            || v.getBrand().toLowerCase().contains(text)
                            || renter.toLowerCase().contains(text)) {
                        listModel.addElement(v);
                    }
                }
            }
        };

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                refreshList.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                refreshList.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                refreshList.run();
            }
        });

        // Populate list initially so empty search shows all vehicles
        refreshList.run();

        int y = 0;
        gbc.weighty = 0;
        gbc.gridx = 0;
        gbc.gridy = y;
        dialog.add(new JLabel("Search (Plate, Model, Renter):"), gbc);
        gbc.gridy = ++y;
        dialog.add(searchField, gbc);

        gbc.gridy = ++y;
        gbc.weighty = 1.0;
        dialog.add(listScroll, gbc);

        gbc.weighty = 0;
        gbc.gridy = ++y;
        dialog.add(new JLabel("Reason of Unavailability:"), gbc);
        gbc.gridy = ++y;
        dialog.add(reasonCombo, gbc);
        gbc.gridy = ++y;
        dialog.add(new JLabel("Responsible Mechanic:"), gbc);
        gbc.gridy = ++y;
        dialog.add(mechanicCombo, gbc);

        JButton addBtn = new JButton("Add to List");
        gbc.gridy = ++y;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(addBtn, gbc);

        addBtn.addActionListener(e -> {
            Vehicle selected = resultList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(dialog, "Please select a vehicle from the list.");
                return;
            }

            String selectedMechanic = (String) mechanicCombo.getSelectedItem();
            if (selectedMechanic == null || selectedMechanic.equals("No Mechanic Assigned")
                    || selectedMechanic.trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please select a valid mechanic.");
                return;
            }

            if (selected.isRented()) {
                Reservation resToRemove = null;
                for (Reservation r : activeReservations) {
                    if (r.getVehicle().getPlate().equals(selected.getPlate())) {
                        resToRemove = r;
                        break;
                    }
                }

                if (resToRemove != null) {
                    activeReservations.remove(resToRemove);
                    reservedByEmployee.remove(resToRemove.getReservationId());
                    JOptionPane.showMessageDialog(dialog,
                            "Warning: This vehicle was currently rented. Its active reservation has been terminated due to unavailability.",
                            "Reservation Terminated", JOptionPane.WARNING_MESSAGE);
                }
                selected.setRented(false);
                selected.setRentedDays(0);
            }

            selected.setUnderMaintenance(true);
            unavailableReasons.put(selected.getPlate(), (String) reasonCombo.getSelectedItem());
            assignedMechanics.put(selected.getPlate(), selectedMechanic);
            unavailableAddedBy.put(selected.getPlate(), loggedInEmployeeName);

            FileManager.saveVehicles(vehicles);
            FileManager.saveMaintenanceState(vehicles, unavailableReasons, assignedMechanics, unavailableAddedBy,
                    lastRentedBy);
            dialog.dispose();
            refreshAllTables();
        });

        searchField.setText("");
        dialog.setVisible(true);
    }

    private void resolveUnavailableCar() {
        int row = unavailableTable.getSelectedRow();
        if (row < 0)
            return;

        String plate = (String) unavailableTable.getValueAt(row, 0);
        String brand = (String) unavailableTable.getValueAt(row, 1);
        String reason = (String) unavailableTable.getValueAt(row, 2);
        String mechanic = (String) unavailableTable.getValueAt(row, 4);

        Vehicle vehicle = vehicles.stream().filter(v -> v.getPlate().equals(plate)).findFirst().orElse(null);
        if (vehicle == null)
            return;

        JTextArea notesArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(notesArea);

        int result = JOptionPane.showConfirmDialog(this, scrollPane, "Enter Mechanic Notes to Resolve:",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String notes = notesArea.getText().trim();
            if (notes.isEmpty())
                notes = "No notes provided.";

            MaintenanceRecord record = new MaintenanceRecord(plate, brand, reason, mechanic, notes,
                    loggedInEmployeeName);
            saveMaintenanceRecord(record);

            vehicle.setUnderMaintenance(false);
            unavailableReasons.remove(plate);
            assignedMechanics.remove(plate);
            unavailableAddedBy.remove(plate);

            lastRentedBy.remove(plate);

            FileManager.saveVehicles(vehicles);
            FileManager.saveMaintenanceState(vehicles, unavailableReasons, assignedMechanics, unavailableAddedBy,
                    lastRentedBy);
            refreshAllTables();
            JOptionPane.showMessageDialog(this, "Vehicle resolved and saved to Maintenance History.");
        }
    }

    private void openHistoryDialog() {
        JDialog dialog = new JDialog(this, "Maintenance History", true);
        dialog.setSize(800, 400);
        dialog.setLocationRelativeTo(this);

        String[] cols = { "Plate", "Brand", "Reason", "Mechanic", "Mechanic Notes", "Admin" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        for (MaintenanceRecord rec : maintenanceHistory) {
            model.addRow(new Object[] { rec.plate, rec.brand, rec.reason, rec.mechanic, rec.notes, rec.admin });
        }

        JTable historyTable = new JTable(model);

        // Double-click listener for the table to view full notes
        historyTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = historyTable.getSelectedRow();
                    if (row >= 0) {
                        String plate = (String) historyTable.getValueAt(row, 0);
                        String fullNotes = (String) historyTable.getValueAt(row, 4);

                        JTextArea textArea = new JTextArea(10, 40);
                        textArea.setText(fullNotes);
                        textArea.setWrapStyleWord(true);
                        textArea.setLineWrap(true);
                        textArea.setEditable(false);
                        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));

                        JScrollPane scrollPane = new JScrollPane(textArea);

                        JOptionPane.showMessageDialog(dialog, scrollPane, "Detailed Mechanic Notes - " + plate,
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        dialog.add(new JScrollPane(historyTable), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private JPanel createRentedPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Search (Customer, Car, Employee):"));
        rentedSearchField = new JTextField(20);
        topPanel.add(rentedSearchField);

        rentedSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateRentedTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateRentedTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateRentedTable();
            }
        });

        panel.add(topPanel, BorderLayout.NORTH);

        rentedTable = new JTable();
        panel.add(new JScrollPane(rentedTable), BorderLayout.CENTER);
        return panel;
    }

    private void updateRentedTable() {
        String[] cols = { "Car Info", "Customer", "Insurance Type", "Days Info", "Employee" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String searchText = rentedSearchField != null ? rentedSearchField.getText().toLowerCase() : "";

        for (Reservation res : activeReservations) {
            Vehicle v = res.getVehicle();
            if (v.isRented()) {
                String carInfo = v.getBrand() + " (" + v.getPlate() + ")";
                String cust = res.getCustomer().getName() + " " + res.getCustomer().getSurname();
                String insurance = res.getInsuranceType();
                String daysStr = res.getDays() + " Days Total";

                try {
                    String[] parts = res.getReservationId().split("-");
                    if (parts.length > 1) {
                        long timestamp = Long.parseLong(parts[1]);
                        java.time.Instant instant = java.time.Instant.ofEpochMilli(timestamp);
                        java.time.LocalDate startDate = instant.atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                        java.time.LocalDate returnDate = startDate.plusDays(res.getDays());

                        daysStr = res.getDays() + " (Return: " + returnDate.format(formatter) + ")";
                    }
                } catch (Exception e) {
                    System.out.println("Error occurred while processing reservation: " + e.getMessage());
                }

                String employeeName = reservedByEmployee.getOrDefault(res.getReservationId(), loggedInEmployeeName);

                // Dynamic Filtering Logic including insurance type
                if (searchText.isEmpty() ||
                        carInfo.toLowerCase().contains(searchText) ||
                        cust.toLowerCase().contains(searchText) ||
                        insurance.toLowerCase().contains(searchText) ||
                        employeeName.toLowerCase().contains(searchText)) {

                    model.addRow(new Object[] { carInfo, cust, insurance, daysStr, employeeName });
                }
            }
        }
        rentedTable.setModel(model);
    }

    private JPanel createReturnPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Process Vehicle Return"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Search field for filtering the list
        returnSearchField = new JTextField(20);
        returnSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                populateReturnList();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                populateReturnList();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                populateReturnList();
            }
        });

        // Visible List Box (Fits 5-6 items on screen)
        returnListModel = new DefaultListModel<>();
        returnReservationList = new JList<>(returnListModel);
        returnReservationList.setVisibleRowCount(6);
        returnReservationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScroll = new JScrollPane(returnReservationList);

        returnReservationList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateReturnDetails();
            }
        });

        retCarLbl = new JLabel("-");
        retDaysLbl = new JLabel("-");
        retPickupLbl = new JLabel("-");
        retDropoffLbl = new JLabel("-");
        retInsuranceLbl = new JLabel("-");
        retDepositLbl = new JLabel("-");

        washingCheck = new JCheckBox("Washing Fee (500 TL)");
        missingObjectCheck = new JCheckBox("Missing Object (2000 TL)");

        int y = 0;
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel("Search Name/Plate:"), gbc);
        gbc.gridx = 1;
        panel.add(returnSearchField, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++y;
        panel.add(new JLabel("Select Customer:"), gbc);
        gbc.gridx = 1;
        panel.add(listScroll, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++y;
        panel.add(new JLabel("Rented Vehicle:"), gbc);
        gbc.gridx = 1;
        panel.add(retCarLbl, gbc);
        gbc.gridx = 0;
        gbc.gridy = ++y;
        panel.add(new JLabel("Total Days:"), gbc);
        gbc.gridx = 1;
        panel.add(retDaysLbl, gbc);
        gbc.gridx = 0;
        gbc.gridy = ++y;
        panel.add(new JLabel("Pick-up City:"), gbc);
        gbc.gridx = 1;
        panel.add(retPickupLbl, gbc);
        gbc.gridx = 0;
        gbc.gridy = ++y;
        panel.add(new JLabel("Drop-off City:"), gbc);
        gbc.gridx = 1;
        panel.add(retDropoffLbl, gbc);
        gbc.gridx = 0;
        gbc.gridy = ++y;
        panel.add(new JLabel("Insurance Type:"), gbc);
        gbc.gridx = 1;
        panel.add(retInsuranceLbl, gbc);
        gbc.gridx = 0;
        gbc.gridy = ++y;
        panel.add(new JLabel("Deposit Held:"), gbc);
        gbc.gridx = 1;
        panel.add(retDepositLbl, gbc);

        gbc.gridx = 0;
        gbc.gridy = ++y;
        gbc.gridwidth = 2;
        panel.add(new JSeparator(), gbc);
        gbc.gridy = ++y;
        panel.add(new JLabel("Deductions:"), gbc);
        gbc.gridy = ++y;
        panel.add(washingCheck, gbc);
        gbc.gridy = ++y;
        panel.add(missingObjectCheck, gbc);

        JButton returnBtn = new JButton("Process Return & Generate Invoice");
        returnBtn.setBackground(new Color(200, 50, 50));
        returnBtn.setForeground(Color.WHITE);
        returnBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridy = ++y;
        panel.add(returnBtn, gbc);

        returnBtn.addActionListener(e -> processReturnAction());

        JPanel wrapper = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(1100, 720));
        wrapper.add(panel, BorderLayout.CENTER);
        return wrapper;
    }

    private void populateReturnList() {
        if (returnListModel == null)
            return;

        returnListModel.clear();
        String searchText = returnSearchField != null ? returnSearchField.getText().toLowerCase() : "";

        for (Reservation res : activeReservations) {
            String displayStr = res.getCustomer().getName() + " " + res.getCustomer().getSurname() + " - "
                    + res.getVehicle().getPlate();

            if (searchText.isEmpty() || displayStr.toLowerCase().contains(searchText)) {
                returnListModel.addElement(displayStr);
            }
        }

        clearReturnDetails();
    }

    private void populateReturnDetails() {
        String selectedStr = returnReservationList.getSelectedValue();

        if (selectedStr == null) {
            clearReturnDetails();
            return;
        }

        int lastDashIndex = selectedStr.lastIndexOf(" - ");
        if (lastDashIndex == -1)
            return;

        String plate = selectedStr.substring(lastDashIndex + 3).trim();

        Reservation res = activeReservations.stream()
                .filter(r -> r.getVehicle().getPlate().equals(plate))
                .findFirst()
                .orElse(null);

        if (res == null) {
            clearReturnDetails();
            return;
        }

        retCarLbl.setText(res.getVehicle().getBrand() + " (" + res.getVehicle().getPlate() + ")");

        String daysStr = String.valueOf(res.getDays());
        try {
            String[] parts = res.getReservationId().split("-");
            if (parts.length > 1) {
                long timestamp = Long.parseLong(parts[1]);
                java.time.Instant instant = java.time.Instant.ofEpochMilli(timestamp);
                java.time.LocalDate startDate = instant.atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                java.time.LocalDate returnDate = startDate.plusDays(res.getDays());

                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
                        .ofPattern("dd.MM.yyyy");
                daysStr = res.getDays() + " (" + returnDate.format(formatter) + ")";
            }
        } catch (Exception e) {
            // Fallback for legacy IDs
        }

        retDaysLbl.setText(daysStr);
        retPickupLbl.setText(res.getPickUpLocation());
        retDropoffLbl.setText(res.getReturnLocation());
        retInsuranceLbl.setText(res.getInsuranceType());
        retDepositLbl.setText(res.getDepositAmount() + " TL");

        washingCheck.setSelected(false);
        missingObjectCheck.setSelected(false);
    }

    private void clearReturnDetails() {
        retCarLbl.setText("-");
        retDaysLbl.setText("-");
        retPickupLbl.setText("-");
        retDropoffLbl.setText("-");
        retInsuranceLbl.setText("-");
        retDepositLbl.setText("-");
    }

    private void processReturnAction() {
        String selectedStr = returnReservationList.getSelectedValue();

        if (selectedStr == null) {
            JOptionPane.showMessageDialog(this, "Please select a valid customer from the list first.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int lastDashIndex = selectedStr.lastIndexOf(" - ");
        if (lastDashIndex == -1)
            return;

        String plate = selectedStr.substring(lastDashIndex + 3).trim();

        Reservation res = activeReservations.stream()
                .filter(r -> r.getVehicle().getPlate().equals(plate))
                .findFirst()
                .orElse(null);

        if (res == null)
            return;

        Vehicle vehicle = res.getVehicle();

        Invoice inv = new Invoice("INV-" + System.currentTimeMillis(), res, washingCheck.isSelected(),
                missingObjectCheck.isSelected());
        FileManager.saveInvoice(inv);

        JOptionPane.showMessageDialog(this, inv.getFormattedInvoice(), "Return & Invoice Summary",
                JOptionPane.INFORMATION_MESSAGE);

        String dropoffCity = res.getReturnLocation();
        for (Branch b : branches) {
            if (b.getCity().equalsIgnoreCase(dropoffCity)) {
                vehicle.setBranch(b);
                break;
            }
        }

        vehicle.setRented(false);
        vehicle.setRentedDays(0);
        activeReservations.remove(res);
        reservedByEmployee.remove(res.getReservationId());
        lastRentedBy.remove(vehicle.getPlate());

        FileManager.saveVehicles(vehicles);
        refreshAllTables();
    }

    private void refreshAllTables() {
        applyCarFilters();
        updateUnavailableTable();
        updateRentedTable();
        populateReturnList();
    }
}