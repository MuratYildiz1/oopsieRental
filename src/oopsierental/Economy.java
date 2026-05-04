/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oopsierental;

/**
 *
 * @author murat
 */
public class Economy extends Vehicle {

    // Constructor calling the superclass (Vehicle) constructor
    public Economy(String plate, String brand, double dailyRate, Branch branch) {
        super(plate, brand, dailyRate, branch);
    }

    // Method Overriding: economy does not have a different rate
    @Override
    public double calculateRent(int days) {
        return dailyRate * days;
    }
}
