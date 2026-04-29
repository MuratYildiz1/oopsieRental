/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oopiserental;

/**
 *
 * @author murat
 */
public class Luxury extends Vehicle{
    public Luxury(String plate, String brand, double dailyRate) {
        super(plate, brand, dailyRate);
    }

    @Override
    public double calculateRent(int days) {
        // Luxury araçlar için %50 konfor vergisi [cite: 65]
        return dailyRate * days * 1.5;
    }
}
