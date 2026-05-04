/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oopsierental;

import java.io.*;
import java.util.*;

/**
 *
 * @author murat
 */
public class FileManager {

    // Constant file names for data storage
    private static final String VEHICLE_FILE = "vehicles.txt";
    private static final String CUSTOMER_FILE = "customers.txt";
    private static final String RESERVATION_FILE = "reservations.txt";
    private static final String BRANCH_FILE = "branches.txt";
    private static final String EMPLOYEE_FILE = "employees.txt";

    // Overwrites the vehicle file with the current list of vehicles
    public static void saveVehicles(ArrayList<Vehicle> vehicles) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(VEHICLE_FILE))) {
            for (Vehicle v : vehicles) {
                String type = v.getClass().getSimpleName();
                writer.println(type + "," + v.getPlate() + "," + v.getBrand() + "," +
                        v.dailyRate + "," + v.getBranch().getBranchId());
            }
        } catch (IOException e) {
            System.err.println("File writing error: " + e.getMessage());
        }
    }

    public static void saveBranches(ArrayList<Branch> branches) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BRANCH_FILE))) {
            for (Branch b : branches) {
                // Format: ID,Name,City
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
                list.add(new Branch(data[0], data[1], data[2], "Default Address"));
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
                String type = data[0];
                String plate = data[1];
                String brand = data[2];
                double dailyRate = Double.parseDouble(data[3]);
                String branchId = data[4];
                Branch b = Branch.findById(branches, branchId);
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
                    list.add(v);
                }
            }
        } catch (Exception e) {
            System.out.println("No registered vehicles yet, or the file could not be read.");
        }
        return list;
    }

    // --- EMPLOYEE OPERATIONS ---
    public static void saveEmployees(ArrayList<Employee> employees) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EMPLOYEE_FILE))) {
            for (Employee e : employees) {
                // Format: Type,ID,FullName,Username,Password,BranchID
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
                String type = data[0];
                String id = data[1];
                String name = data[2];
                String user = data[3];
                String pass = data[4];
                String branchId = data[5];

                Branch b = Branch.findById(branches, branchId);

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
            System.out.println("No employees found.");
        }
        return list;
    }

    // Appends a single customer record to the customer file[cite: 20]
    public static void saveCustomer(Customer c) {
        try (PrintWriter out = new PrintWriter(new FileWriter(CUSTOMER_FILE, true))) {
            out.println(c.getId() + "," + c.getName() + "," + c.getSurname() + "," + c.getLoyaltyTier());
        } catch (IOException e) {
            System.err.println("File writing error: " + e.getMessage());
        }
    }

    // Records a reservation summary in the reservation history file
    public static void saveReservation(Reservation res) {
        try (PrintWriter out = new PrintWriter(new FileWriter(RESERVATION_FILE, true))) {
            out.println(res.toString());
        } catch (IOException e) {
            System.err.println("Reservation recording failed: " + e.getMessage());
        }
    }

    // Reads the customer file and returns an ArrayList of Customer objects
    public static ArrayList<Customer> loadCustomers() {
        ArrayList<Customer> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Customer c = new Customer(data[0], data[1], data[2]);
                c.setLoyaltyTier(data[3]);
                list.add(c);
            }
        } catch (Exception e) {
            System.out.println("No registered customers yet or file could not be read.");
        }
        return list;
    }

    // Saves the full formatted invoice text to a file
    public static void saveInvoice(Invoice invoice) {
        String fileName = "invoices.txt";
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName, true))) {
            out.println(invoice.getFormattedInvoice());
            out.println("=========================================\n");
            System.out.println("Invoice saved successfully: " + fileName);
        } catch (IOException e) {
            System.err.println("Invoice writing error: " + e.getMessage());
        }
    }
}
