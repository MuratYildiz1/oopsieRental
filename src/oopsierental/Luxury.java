package oopsierental;

/**
 * @author MuratYildiz1
 * @author KeremHKardes
 */

public class Luxury extends Vehicle {

    public Luxury(String plate, String brand, double dailyRate, Branch branch) {
        super(plate, brand, dailyRate, branch);
    }

    // Adds a 50% surcharge specifically for Luxury models
    @Override
    public double calculateRent(int days) {
        return dailyRate * days * 1.5;
    }
}