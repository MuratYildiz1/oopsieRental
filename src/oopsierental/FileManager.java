package oopsierental;

/**
 * @author MuratYildiz1
 * @author KeremHKardes
 */

import java.io.*;
import java.util.*;

public class FileManager {

    private static final String VEHICLE_FILE = "vehicles.txt";
    private static final String CUSTOMER_FILE = "customers.txt";
    private static final String RESERVATION_FILE = "reservations.txt";
    private static final String BRANCH_FILE = "branches.txt";
    private static final String EMPLOYEE_FILE = "employees.txt";
    private static final String MAINTENANCE_STATE_FILE = "maintenance_state.txt";

    public static void saveVehicles(ArrayList<Vehicle> vehicles) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(VEHICLE_FILE))) {
            for (Vehicle v : vehicles) {
                String type = v.getClass().getSimpleName();
                String branchId = v.getBranch() != null ? v.getBranch().getBranchId() : "UNKNOWN";

                writer.println(type + "," + v.getPlate() + "," + v.getBrand() + "," +
                        v.getDailyRate() + "," + branchId + "," + v.isRented() + "," +
                        v.getRentedDays() + "," + v.isUnderMaintenance());
            }
        } catch (IOException e) {
            System.err.println("File writing error: " + e.getMessage());
        }
    }

    public static void saveMaintenanceState(ArrayList<Vehicle> vehicles, Map<String, String> unavailableReasons,
            Map<String, String> assignedMechanics, Map<String, String> unavailableAddedBy,
            Map<String, String> lastRentedBy) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(MAINTENANCE_STATE_FILE))) {
            for (Vehicle v : vehicles) {
                if (v.isUnderMaintenance()) {
                    String reason = unavailableReasons.getOrDefault(v.getPlate(), "Maintenance");
                    String mechanic = assignedMechanics.getOrDefault(v.getPlate(), "Pending");
                    String admin = unavailableAddedBy.getOrDefault(v.getPlate(), "System");
                    String renter = lastRentedBy.getOrDefault(v.getPlate(), "-");
                    writer.println(v.getPlate() + "~" + reason + "~" + mechanic + "~" + admin + "~" + renter);
                }
            }
        } catch (IOException e) {
            System.err.println("Maintenance state saving error: " + e.getMessage());
        }
    }

    public static void loadMaintenanceState(ArrayList<Vehicle> vehicles, Map<String, String> unavailableReasons,
            Map<String, String> assignedMechanics, Map<String, String> unavailableAddedBy,
            Map<String, String> lastRentedBy) {
        try (BufferedReader br = new BufferedReader(new FileReader(MAINTENANCE_STATE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("~");
                if (parts.length < 4)
                    continue;

                String plate = parts[0];
                String reason = parts[1];
                String mechanic = parts[2];
                String admin = parts[3];
                String renter = parts.length >= 5 ? parts[4] : "-";

                Vehicle v = vehicles.stream().filter(veh -> veh.getPlate().equals(plate)).findFirst().orElse(null);
                if (v != null && v.isUnderMaintenance()) {
                    unavailableReasons.put(plate, reason);
                    assignedMechanics.put(plate, mechanic);
                    unavailableAddedBy.put(plate, admin);
                    if (!renter.equals("-")) {
                        lastRentedBy.put(plate, renter);
                    }
                }
            }
        } catch (Exception e) {
            // If file is missing or malformed, ignore and use defaults.
        }
    }

    public static void saveBranches(ArrayList<Branch> branches) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BRANCH_FILE))) {
            for (Branch b : branches) {
                writer.println(b.getBranchId() + "," + b.getBranchName() + "," + b.getCity());
            }
        } catch (IOException e) {
            System.err.println("Branch saving error: " + e.getMessage());
        }
    }

    public static ArrayList<Branch> loadBranches() {
        ArrayList<Branch> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(BRANCH_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                list.add(new Branch(data[0], data[1], data[2]));
            }
        } catch (Exception e) {
            System.out.println("No branches found.");
        }
        return list;
    }

    public static ArrayList<Vehicle> loadVehicles(ArrayList<Branch> branches) {
        ArrayList<Vehicle> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(VEHICLE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 5)
                    continue;

                String type = data[0];
                String plate = data[1];
                String brand = data[2];
                double dailyRate = Double.parseDouble(data[3]);
                String branchId = data[4];

                Branch b = Branch.findById(branches, branchId);
                if (b == null) {
                    b = branches.isEmpty() ? new Branch("BR00", "Unknown", "Unknown") : branches.get(0);
                }

                Vehicle v = null;
                switch (type) {
                    case "Economy":
                        v = new Economy(plate, brand, dailyRate, b);
                        break;
                    case "SUV":
                        v = new SUV(plate, brand, dailyRate, b);
                        break;
                    case "Luxury":
                        v = new Luxury(plate, brand, dailyRate, b);
                        break;
                    case "Van":
                        v = new Van(plate, brand, dailyRate, b);
                        break;
                }

                if (v != null) {
                    if (data.length >= 7) {
                        v.setRented(Boolean.parseBoolean(data[5]));
                        v.setRentedDays(Integer.parseInt(data[6]));
                    }
                    if (data.length >= 8) {
                        v.setUnderMaintenance(Boolean.parseBoolean(data[7]));
                    }
                    list.add(v);
                }
            }
        } catch (Exception e) {
            System.out.println("No registered vehicles yet, or the file could not be read.");
        }
        return list;
    }

    public static void saveEmployees(ArrayList<Employee> employees) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EMPLOYEE_FILE))) {
            for (Employee e : employees) {
                String type = e.getClass().getSimpleName();
                writer.println(type + "," + e.getEmployeeId() + "," + e.getFullName() + "," +
                        e.getUsername() + "," + e.getPassword() + "," + e.getBranch().getBranchId());
            }
        } catch (IOException e) {
            System.err.println("Employee saving error: " + e.getMessage());
        }
    }

    public static ArrayList<Employee> loadEmployees(ArrayList<Branch> branches) {
        ArrayList<Employee> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 6)
                    continue;

                String type = data[0];
                String id = data[1];
                String name = data[2];
                String user = data[3];
                String pass = data[4];
                String branchId = data[5];

                Branch b = Branch.findById(branches, branchId);
                if (b == null && !branches.isEmpty()) {
                    b = branches.get(0);
                }

                Employee e = null;
                switch (type) {
                    case "BranchManager":
                        e = new BranchManager(id, name, user, pass, b);
                        break;
                    case "RentalAgent":
                        e = new RentalAgent(id, name, user, pass, b);
                        break;
                    case "Mechanic":
                        e = new Mechanic(id, name, user, pass, b);
                        break;
                }
                if (e != null)
                    list.add(e);
            }
        } catch (Exception e) {
            System.out.println("No employees found. File will be created upon first registration.");
        }
        return list;
    }

    public static void saveCustomer(Customer c) {
        try (PrintWriter out = new PrintWriter(new FileWriter(CUSTOMER_FILE, true))) {
            out.println(c.getId() + "," + c.getName() + "," + c.getSurname() + "," + c.getEmail() + ","
                    + c.getPassword() + "," + c.getLoyaltyTier());
        } catch (IOException e) {
            System.err.println("File writing error: " + e.getMessage());
        }
    }

    public static ArrayList<Customer> loadCustomers() {
        ArrayList<Customer> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 5)
                    continue;

                Customer c = new Customer(data[0], data[1], data[2], data[3], data[4]);
                if (data.length >= 6) {
                    c.setLoyaltyTier(data[5]);
                }
                list.add(c);
            }
        } catch (Exception e) {
            System.out.println("No registered customers yet or file could not be read.");
        }
        return list;
    }

    // MODIFIED: Saves the reservation in a parsable CSV format instead of
    // toString()
    public static void saveReservation(Reservation res) {
        try (PrintWriter out = new PrintWriter(new FileWriter(RESERVATION_FILE, true))) {
            // Note: If 'getInsuranceDailyCost()' is underlined in red, change it to match
            // the getter in your Reservation.java
            out.println(res.getReservationId() + "," +
                    res.getCustomer().getId() + "," +
                    res.getVehicle().getPlate() + "," +
                    res.getDays() + "," +
                    res.getInsuranceType() + "," +
                    res.getInsuranceDailyCost() + "," +
                    res.getReturnLocation() + "," +
                    res.getEmployee());
        } catch (Exception e) {
            System.err.println("Reservation recording failed: " + e.getMessage());
        }
    }

    // NEW ADDITION: Safely loads active reservations on system startup
    public static ArrayList<Reservation> loadReservations(ArrayList<Customer> customers, ArrayList<Vehicle> vehicles) {
        ArrayList<Reservation> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RESERVATION_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // If it encounters the old format (toString), it skips safely
                if (data.length < 8)
                    continue;

                String resId = data[0];
                String customerId = data[1];
                String plate = data[2];
                int days = Integer.parseInt(data[3]);
                String insType = data[4];
                int insDailyCost = (int) Double.parseDouble(data[5]);
                String returnCity = data[6];
                String employee = data[7];

                Customer c = customers.stream().filter(cust -> cust.getId().equals(customerId)).findFirst()
                        .orElse(null);
                Vehicle v = vehicles.stream().filter(veh -> veh.getPlate().equals(plate)).findFirst().orElse(null);

                // Smart Logic: Only load the reservation if the vehicle's status is ACTUALLY
                // "rented"
                if (c != null && v != null && v.isRented()) {
                    Reservation res = new Reservation(resId, c, v, days, insType, insDailyCost, returnCity, employee,
                            true);

                    // Overwrite if multiple records exist for the same car (keep the latest)
                    list.removeIf(r -> r.getVehicle().getPlate().equals(v.getPlate()));
                    list.add(res);
                }
            }
        } catch (Exception e) {
            System.out.println("No active reservations found or format changed.");
        }
        return list;
    }

    public static void saveInvoice(Invoice invoice) {
        String fileName = "invoices.txt";
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName, true))) {
            out.println(invoice.getFormattedInvoice());
            out.println("=========================================\n");
        } catch (IOException e) {
            System.err.println("Invoice writing error: " + e.getMessage());
        }
    }
}