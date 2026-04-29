/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oopiserental;

/**
 *
 * @author murat
 */
public class SUV extends Vehicle{
    public SUV(String plate, String brand, double dailyRate) {
        super(plate, brand, dailyRate);
    }

    @Override
    public double calculateRent(int days) {
        return dailyRate * days * 1.2;
    }
}
