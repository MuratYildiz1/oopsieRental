/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oopiserental;

import java.util.ArrayList;

/**
 *
 * @author murat
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== OOPSIETRACK PRO: SYSTEM INTEGRATION TEST ===\n");

        try {
            // ---------------------------------------------------------
            // 1 & 2. INHERITANCE & ABSTRACTION
            // Creating specific vehicle types from the abstract Vehicle class.
            // ---------------------------------------------------------
            Vehicle suv = new SUV("35SUV01", "Jeep Renegade", 1200.0);
            Vehicle eco = new Economy("35ECO02", "Fiat Egea", 600.0);
            Vehicle lux = new Luxury("35LUX03", "Mercedes C200", 2500.0);
            Vehicle van = new Van("35VAN04", "Ford Transit", 1000.0);

            // ---------------------------------------------------------
            // 3 & 7. POLYMORPHISM & METHOD OVERRIDING
            // Each class calculates rent differently based on its own logic.
            // ---------------------------------------------------------
            System.out.println("Testing Polymorphic Rent Calculation (1 Day):");
            System.out.println("SUV (20% Surcharge): " + suv.calculateRent(1) + " USD");
            System.out.println("Economy (Base Price): " + eco.calculateRent(1) + " USD");
            System.out.println("Luxury (50% Surcharge): " + lux.calculateRent(1) + " USD");
            System.out.println("Van (30% Surcharge): " + van.calculateRent(1) + " USD\n");

            // ---------------------------------------------------------
            // 8. METHOD OVERLOADING
            // Using the same method name with different parameters (days vs days+discount).
            // ---------------------------------------------------------
            System.out.println("Testing Method Overloading (Direct 10% Discount):");
            double discountedPrice = eco.calculateRent(5, 10.0); 
            System.out.println("Economy 5-day with manual 10% discount: " + discountedPrice + " USD\n");

            // ---------------------------------------------------------
            // 1. ENCAPSULATION
            // Managing customer data via private fields and loyalty logic.
            // ---------------------------------------------------------
            Customer murat = new Customer("C-101", "Murat", "Yildiz");
            murat.setLoyaltyTier("Gold"); // Sets 20% discount rate internally
            System.out.println("Customer Profile Created: " + murat.toString() + "\n");

            // ---------------------------------------------------------
            // 5 & 6. COMPOSITION & INTERFACES
            // Reservation 'has-a' Vehicle and Customer. Vehicle implements 'Rentable'.
            // ---------------------------------------------------------
            System.out.println("--- Creating a Complex Reservation ---");
            Reservation res = new Reservation("RES-5001", murat, suv, 7);
            System.out.println(res.toString()); // Displays calculated total with Gold discount

            // ---------------------------------------------------------
            // 9. EXCEPTION HANDLING (Custom Exception)
            // Testing the availability check for a vehicle that is already rented.
            // ---------------------------------------------------------
            System.out.println("\n--- Testing Custom Exception Handling ---");
            try {
                System.out.println("Attempting to rent the same SUV again...");
                res.checkAvailability(); // This should trigger RentalException
            } catch (RentalException e) {
                System.err.println("ALERT: " + e.getMessage());
            }

            // ---------------------------------------------------------
            // DATA PERSISTENCE (.txt Files)
            // Saving all objects to their respective text files.
            // ---------------------------------------------------------
            System.out.println("\n--- Finalizing Data Persistence ---");
            ArrayList<Vehicle> inventory = new ArrayList<>();
            inventory.add(suv); inventory.add(eco); inventory.add(lux); inventory.add(van);
            
            FileManager.saveVehicles(inventory); // Saves types and rates
            FileManager.saveCustomer(murat);      // Appends to customers.txt
            FileManager.saveReservation(res);    // Logs the transaction
            
            Invoice invoice = new Invoice("INV-99", res);
            FileManager.saveInvoice(invoice);    // Generates formatted .txt invoice
            
            System.out.println("All data successfully synchronized to .txt files.");
            System.out.println("=== TEST COMPLETED SUCCESSFULLY ===");

        } catch (Exception e) {
            System.err.println("Critical System Failure: " + e.getMessage());
            e.printStackTrace();
        }
    }
}