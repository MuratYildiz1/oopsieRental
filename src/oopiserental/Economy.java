/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oopiserental;

/**
 *
 * @author murat
 */
public class Economy extends Vehicle {

    public Economy(String plate, String brand, double dailyRate) {
        super(plate, brand, dailyRate); // Inheritance 
    }

    @Override
    public double calculateRent(int days) {
        // Economy araçlarda ek ücret yok, direkt günlük oran [cite: 65]
        return dailyRate * days;
    }
}
