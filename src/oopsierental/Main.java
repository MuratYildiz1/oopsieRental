/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oopsierental;

import java.util.ArrayList;

/**
 *
 * @author murat
 */
public class Main {

    public static void main(String[] args) throws Exception{
        
        System.out.println("OOPSIE RENTAL - BACKEND INTEGRATION TEST INITIATED\n");

        // =====================================================================
        // PHASE 1: DATA CREATION (Mock Data in Memory)
        // =====================================================================
        System.out.println("--- PHASE 1: SYSTEM SETUP ---");
        
        ArrayList<Branch> branches = new ArrayList<>();
        ArrayList<Employee> employees = new ArrayList<>();
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        ArrayList<Customer> customers = new ArrayList<>();

        // 1. Create Branch
        Branch mainBranch = new Branch("BR-35", "Izmir Center", "Izmir", "Bornova");
        branches.add(mainBranch);
        System.out.println("Status: Branch created -> " + mainBranch.getBranchName());

        // 2. Create Employees and Link to Branch
        RentalAgent agent = new RentalAgent("EMP-01", "John Doe", "jdoe", "1234", mainBranch);
        Mechanic mechanic = new Mechanic("EMP-02", "Mike Smith", "msmith", "1234", mainBranch);
        employees.add(agent);
        employees.add(mechanic);
        System.out.println("Status: Employees created and linked to branch.");

        // 3. Create Vehicles and Link to Branch
        Vehicle car1 = new Economy("35-ABC-123", "Renault Clio", 500.0, mainBranch);
        Vehicle car2 = new SUV("34-XYZ-987", "Nissan Qashqai", 1200.0, mainBranch);
        vehicles.add(car1);
        vehicles.add(car2);
        System.out.println("Status: Vehicles created and linked to branch.");

        // 4. Create Customer
        Customer customer1 = new Customer("CST-01", "Murat", "Yildiz");
        customers.add(customer1);
        System.out.println("Status: Customer created.");


        // =====================================================================
        // PHASE 2: BUSINESS LOGIC (Rental, Return, Damage, and Invoice)
        // =====================================================================
        System.out.println("\n--- PHASE 2: RENTAL & RETURN SIMULATION ---");
        
        // Customer rents "car1" for 5 days with Full Coverage insurance
        int rentalDays = 5;
        double insuranceCost = 150.0;
        Reservation res1 = new Reservation("RES-001", customer1, car1, rentalDays, "Full Coverage", insuranceCost);
        car1.setRented(true);
        car1.setRentedDays(rentalDays);
        
        System.out.println("Action: Vehicle Rented -> " + car1.getBrand() + " for " + rentalDays + " days with Full Coverage.");

        // Simulation: Customer returns the vehicle after 5 days...
        // Agent processes the return. (200 km driven, HAS DAMAGE, 3000.0 TL Damage Fee)
        System.out.println("Warning: Customer returns the vehicle with a cracked bumper!");
        
        Invoice finalInvoice = agent.processReturn(res1, 200, true, 3000.0);
        
        // Print Invoice to Console
        System.out.println("\n" + finalInvoice.getFormattedInvoice());
        
        // Check vehicle maintenance status after damage assessment
        System.out.println("Status: Is vehicle under maintenance? " + car1.isUnderMaintenance()); 


        // =====================================================================
        // PHASE 3: DATA PERSISTENCE (File Saving Test)
        // =====================================================================
        System.out.println("\n--- PHASE 3: FILE SAVING TEST ---");
        
        FileManager.saveBranches(branches);
        FileManager.saveEmployees(employees);
        FileManager.saveVehicles(vehicles);
        FileManager.saveCustomer(customer1);
        FileManager.saveInvoice(finalInvoice);
        
        System.out.println("Status: All data successfully serialized to .txt files.");

        // Clear memory to ensure we are actually reading from the files below
        branches.clear(); 
        employees.clear(); 
        vehicles.clear();
        
        System.out.println("\n--- PHASE 4: FILE LOADING TEST ---");
        
        // Loading order is critical: Branch must be loaded first
        ArrayList<Branch> loadedBranches = FileManager.loadBranches();
        ArrayList<Employee> loadedEmployees = FileManager.loadEmployees(loadedBranches);
        ArrayList<Vehicle> loadedVehicles = FileManager.loadVehicles(loadedBranches);
        
        System.out.println("Status: Data loaded from files.");
        System.out.println("Loaded Branches : " + loadedBranches.size());
        System.out.println("Loaded Employees: " + loadedEmployees.size());
        System.out.println("Loaded Vehicles : " + loadedVehicles.size());

        if(!loadedVehicles.isEmpty()) {
            System.out.println("Verification: Loaded Vehicle Plate = " + loadedVehicles.get(0).getPlate());
            System.out.println("Verification: Linked Branch = " + loadedVehicles.get(0).getBranch().getBranchName());
        }

        System.out.println("\nBACKEND INTEGRATION TEST COMPLETED SUCCESSFULLY.");
    }
}