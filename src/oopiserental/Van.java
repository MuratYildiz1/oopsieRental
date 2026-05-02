/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oopiserental;

/**
 *
 * @author murat
 */
public class Van extends Vehicle {

    // Constructor calling the superclass (Vehicle) constructor
    public Van(String plate, String brand, double dailyRate) {
        super(plate, brand, dailyRate);
    }

    // Method Overriding: adds 30% surcharge specifically for Van models
    @Override
    public double calculateRent(int days) {
        return dailyRate * days * 1.3;
    }
}
