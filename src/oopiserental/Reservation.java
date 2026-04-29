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

    private String reservationId;
    private Customer customer; 
    private Vehicle vehicle;   
    private int days;
    private double totalPrice;

    public Reservation(String reservationId, Customer customer, Vehicle vehicle, int days) {
        this.reservationId = reservationId;
        this.customer = customer;
        this.vehicle = vehicle;
        this.days = days;
        this.totalPrice = calculateFinalAmount();
        
        vehicle.setRented(true);
    }

    private double calculateFinalAmount() {
        double baseAmount = vehicle.calculateRent(days);
        double discount = baseAmount * customer.getDiscountRate();
        return baseAmount - discount;
    }
    
    public void checkAvailability() throws RentalException {
    if (vehicle.isRented()) {
        throw new RentalException("Hata: Seçilen araç (" + vehicle.getPlate() + ") zaten kiralanmış!");
    }
}

    @Override
    public String toString() {
        return "ID: " + reservationId + " | Müşteri: " + customer.getName()
                + " | Araç: " + vehicle.getPlate() + " | Tutar: " + totalPrice + " TL";
    }
}
