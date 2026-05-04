package oopsierental;

/**
 * @author MuratYildiz1
 * @author KeremHKardes
 */

public abstract class Vehicle implements Rentable {

    private String plate;
    private String brand;
    protected double dailyRate;
    private boolean isRented;
    private int rentedDays;

    private Branch branch;
    private boolean isUnderMaintenance;
    private int mileage;

    public abstract double calculateRent(int days);

    public Vehicle(String plate, String brand, double dailyRate, Branch branch) {
        this.plate = plate;
        this.brand = brand;
        this.dailyRate = dailyRate;
        this.branch = branch;
        this.isRented = false;
        this.isUnderMaintenance = false;
        this.mileage = 0;
    }

    public double calculateRent(int days, double discountPercent) {
        double original = calculateRent(days);
        return original - (original * discountPercent / 100);
    }

    public String getPlate() {
        return plate;
    }

    public String getBrand() {
        return brand;
    }

    public boolean isRented() {
        return isRented;
    }

    public void setRented(boolean rented) {
        this.isRented = rented;
    }

    public int getRentedDays() {
        return rentedDays;
    }

    public void setRentedDays(int rentedDays) {
        this.rentedDays = rentedDays;
    }

    public Branch getBranch() {
        return branch;
    }

    public double getDailyRate() {
        return this.dailyRate;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public boolean isUnderMaintenance() {
        return isUnderMaintenance;
    }

    public void setUnderMaintenance(boolean underMaintenance) {
        this.isUnderMaintenance = underMaintenance;
    }

    public int getMileage() {
        return mileage;
    }

    public void addMileage(int km) {
        this.mileage += km;
    }

    @Override
    public void rent() {
        this.setRented(true);
        System.out.println("Vehicle with plate " + plate + " has been rented.");
    }

    @Override
    public void returnVehicle() {
        this.setRented(false);
        System.out.println("Vehicle with plate " + plate + " has been returned.");
    }

    @Override
    public String toString() {
        return brand + " (" + plate + ")";
    }
}