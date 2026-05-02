/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oopiserental;

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

    // Constructor calculates the final amount and marks the vehicle as rented
    public Reservation(String reservationId, Customer customer, Vehicle vehicle, int days) {
        this.reservationId = reservationId;
        this.customer = customer;
        this.vehicle = vehicle;
        this.days = days;
        this.totalPrice = calculateFinalAmount();

        vehicle.setRented(true);
    }

    // Business logic to apply customer's discount to the base rent
    private double calculateFinalAmount() {
        double baseAmount = vehicle.calculateRent(days);
        double discount = baseAmount * customer.getDiscountRate();
        return baseAmount - discount;
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
                + " | Vehicle: " + vehicle.getPlate() + " | Amount: " + totalPrice + " USD";
    }
}
