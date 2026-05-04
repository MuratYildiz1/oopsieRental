/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oopsierental;

/**
 *
 * @author murat
 */
public class Reservation {

    // Composition: Reservation "has-a" Customer and a Vehicle
    private String reservationId;
    private Customer customer;
    private Vehicle vehicle;
    private int days;
    private double totalPrice;
    private String insuranceType;
    private double insuranceDailyCost;

    // Constructor calculates the final amount and marks the vehicle as rented
    public Reservation(String reservationId, Customer customer, Vehicle vehicle, int days, String insuranceType,
            double insuranceDailyCost) throws RentalException {
        this.reservationId = reservationId;
        this.customer = customer;
        this.vehicle = vehicle;
        this.days = days;
        this.insuranceType = insuranceType;
        this.insuranceDailyCost = insuranceDailyCost;

        // First check availability
        checkAvailability();

        // Then calculate and mark as rented
        this.totalPrice = calculateFinalAmount();
        vehicle.setRented(true);
        vehicle.setRentedDays(days);
    }

    public Reservation(String reservationId, Customer customer, Vehicle vehicle, int days) throws RentalException {
        this(reservationId, customer, vehicle, days, "Standard", 0.0);
    }

    public int getDays() {
        return days;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    // Business logic to apply customer's discount to the base rent
    private double calculateFinalAmount() {
        double vehicleRent = vehicle.calculateRent(days);
        double insuranceTotal = insuranceDailyCost * days;

        double baseAmount = vehicleRent + insuranceTotal;
        double discount = baseAmount * customer.getDiscountRate();

        return baseAmount - discount;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public double getInsuranceDailyCost() {
        return insuranceDailyCost;
    }

    // Checks if the vehicle is available; throws custom exception if not[cite: 25]
    public void checkAvailability() throws RentalException {
        if (vehicle.isRented()) {
            throw new RentalException("Error: Selected vehicle (" + vehicle.getPlate() + ") is already rented!");
        }
    }

    // Overriding toString to display reservation summary[cite: 25]
    @Override
    public String toString() {
        return "ID: " + reservationId + " | Customer: " + customer.getName()
                + " | Vehicle: " + vehicle.getPlate() + " | Amount: " + totalPrice + " TL";
    }
}
