package oopsierental;

import java.io.*;
import java.util.*;

public class FileManager {

    private static final String VEHICLE_FILE = "vehicles.txt";
    private static final String CUSTOMER_FILE = "customers.txt";
    private static final String RESERVATION_FILE = "reservations.txt";
    private static final String BRANCH_FILE = "branches.txt";
    private static final String EMPLOYEE_FILE = "employees.txt";

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

    public static void saveReservation(Reservation res) {
        try (PrintWriter out = new PrintWriter(new FileWriter(RESERVATION_FILE, true))) {
            out.println(res.toString());
        } catch (IOException e) {
            System.err.println("Reservation recording failed: " + e.getMessage());
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