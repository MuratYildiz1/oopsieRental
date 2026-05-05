package oopsierental;

/**
 * @author MuratYildiz1
 * @author KeremHKardes
 */

public class SUV extends Vehicle {

    public SUV(String plate, String brand, double dailyRate, Branch branch) {
        super(plate, brand, dailyRate, branch);
    }

    // No additional surcharge - use base daily rate
    @Override
    public double calculateRent(int days) {
        return dailyRate * days;
    }
}