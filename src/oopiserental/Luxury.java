/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oopiserental;

/**
 *
 * @author murat
 */
public class Luxury extends Vehicle {

    // Constructor calling the superclass (Vehicle) constructor
    public Luxury(String plate, String brand, double dailyRate) {
        super(plate, brand, dailyRate);
    }

    // Method Overriding: adds 50% surcharge specifically for Luxury models
    @Override
    public double calculateRent(int days) {
        return dailyRate * days * 1.5;
    }
}
