package oopsierental;

/**
 * @author MuratYildiz1
 * @author KeremHKardes
 */

public class Economy extends Vehicle {

    public Economy(String plate, String brand, double dailyRate, Branch branch) {
        super(plate, brand, dailyRate, branch);
    }

    // Economy class does not apply additional surcharge to the base rate
    @Override
    public double calculateRent(int days) {
        return dailyRate * days;
    }
}