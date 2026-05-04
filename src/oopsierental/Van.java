package oopsierental;

public class Van extends Vehicle {

    public Van(String plate, String brand, double dailyRate, Branch branch) {
        super(plate, brand, dailyRate, branch);
    }

    // Adds a 30% surcharge for Van models
    @Override
    public double calculateRent(int days) {
        return dailyRate * days * 1.3;
    }
}