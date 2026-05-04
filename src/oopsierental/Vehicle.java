/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oopsierental;

/**
 *
 * @author murat
 */
public abstract class Vehicle implements Rentable {

    // Encapsulation: private fields for data security
    private String plate;
    private String brand;
    protected double dailyRate;
    private boolean isRented;
    private int rentedDays;

    private Branch branch;
    private boolean isUnderMaintenance;
    private int mileage;

    // Abstract method to be implemented by specific vehicle types
    public abstract double calculateRent(int days);

    // Constructor to set basic vehicle info
    public Vehicle(String plate, String brand, double dailyRate, Branch branch) {
        this.plate = plate;
        this.brand = brand;
        this.dailyRate = dailyRate;
        this.branch = branch;
        this.isRented = false;
        this.isUnderMaintenance = false;
        this.mileage = 0;
    }

    // Overloading: calculates rent with a percentage discount
    public double calculateRent(int days, double discountPercent) {
        double original = calculateRent(days);
        return original - (original * discountPercent / 100);
    }

    // Getters and Setters for private fields
    public String getPlate() {
        return plate;
    }

    public String getBrand() {
        return brand;
    }

    public boolean isRented() {
        return isRented;
    }

    public void setRented(boolean rented) {
        isRented = rented;
    }

    public int getRentedDays() {
        return rentedDays;
    }

    public void setRentedDays(int rentedDays) {
        this.rentedDays = rentedDays;
    }

    public Branch getBranch() {
        return branch;
    }
    public double getDailyRate() {
        return this.dailyRate;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public boolean isUnderMaintenance() {
        return isUnderMaintenance;
    }

    public void setUnderMaintenance(boolean underMaintenance) {
        isUnderMaintenance = underMaintenance;
    }

    public int getMileage() {
        return mileage;
    }

    public void addMileage(int km) {
        this.mileage += km;
    }

    // Implements rent method from Rentable interface
    @Override
    public void rent() {
        this.setRented(true);
        System.out.println("Vehicle with plate " + plate + " has been rented.");
    }

    // Implements returnVehicle method from Rentable interface
    @Override
    public void returnVehicle() {
        this.setRented(false);
        System.out.println("Vehicle with plate " + plate + " has been returned.");
    }

    @Override
    public String toString() {
        return brand + " (" + plate + ")";
    }
}
