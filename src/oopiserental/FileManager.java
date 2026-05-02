/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oopiserental;

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

    // Overwrites the vehicle file with the current list of vehicles
    public static void saveVehicles(ArrayList<Vehicle> vehicles) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(VEHICLE_FILE))) {
            for (Vehicle v : vehicles) {
                String type = v.getClass().getSimpleName();
                writer.println(type + "," + v.getPlate() + "," + v.getBrand() + "," + v.dailyRate);
            }
        } catch (IOException e) {
            System.err.println("File writing error: " + e.getMessage());
        }
    }

    public static ArrayList<Vehicle> loadVehicles() {
        ArrayList<Vehicle> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(VEHICLE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String type = data[0];
                String plate = data[1];
                String brand = data[2];
                double dailyRate = Double.parseDouble(data[3]);

                Vehicle v = null;
                switch (type) {
                    case "Economy":
                        v = new Economy(plate, brand, dailyRate);
                        break;
                    case "SUV":
                        v = new SUV(plate, brand, dailyRate);
                        break;
                    case "Luxury":
                        v = new Luxury(plate, brand, dailyRate);
                        break;
                    case "Van":
                        v = new Van(plate, brand, dailyRate);
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
