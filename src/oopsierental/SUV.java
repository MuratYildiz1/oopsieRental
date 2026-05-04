package oopsierental;

public class SUV extends Vehicle {

    public SUV(String plate, String brand, double dailyRate, Branch branch) {
        super(plate, brand, dailyRate, branch);
    }

    // Adds a 20% surcharge for SUV models
    @Override
    public double calculateRent(int days) {
        return dailyRate * days * 1.2;
    }
}