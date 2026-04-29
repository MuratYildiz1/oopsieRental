/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oopiserental;

/**
 *
 * @author murat
 */
public abstract class Vehicle {

    private String plate;
    private String brand;
    protected double dailyRate;

    public abstract double calculateRent(int days);

    public Vehicle(String plate, String brand, double dailyRate) {
        this.plate = plate;
        this.brand = brand;
        this.dailyRate = dailyRate;
    }

    public double calculateRent(int days, double discountPercent) {
        double original = calculateRent(days);
        return original - (original * discountPercent / 100);
    }

    public String getPlate() {
        return plate;
    }

    public String getBrand() {
        return brand;
    }
}
