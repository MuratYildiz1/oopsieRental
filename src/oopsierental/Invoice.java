/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oopsierental;

import java.util.Date;

/**
 *
 * @author murat
 */
public class Invoice {

    private String invoiceId;
    private Reservation reservation;
    private Date generationDate;
    private int RentalDays;
    private double damageFee;
    private double totalAmount;
    private double discountAmount;

    // Constructor to initialize invoice with ID and reservation data
    public Invoice(String invoiceId, Reservation reservation, int RentalDays, double damageFee) {
        this.invoiceId = invoiceId;
        this.reservation = reservation;
        this.generationDate = new Date(); // Automatically sets the current date
        this.RentalDays = RentalDays;
        this.damageFee = damageFee;

        double baseRent = reservation.getVehicle().getDailyRate() * RentalDays;
        double insuranceTotal = reservation.getInsuranceDailyCost() * RentalDays;

        if (RentalDays == reservation.getDays()) {
            this.totalAmount = reservation.getTotalPrice() + damageFee;
            this.discountAmount = (baseRent + insuranceTotal) - reservation.getTotalPrice();
        } else {
            this.totalAmount = baseRent + insuranceTotal + damageFee;
            this.discountAmount = 0;
        }
    }

    // Formats the invoice details into a professional string for printing
    public String getFormattedInvoice() {
        double baseRent = reservation.getVehicle().getDailyRate() * RentalDays;
        double insuranceTotal = reservation.getInsuranceDailyCost() * RentalDays;

        StringBuilder sb = new StringBuilder();
        sb.append("---------- RENT-A-CAR INVOICE ----------\n");
        sb.append("Invoice No: ").append(invoiceId).append("\n");
        sb.append("Date: ").append(generationDate.toString()).append("\n");
        sb.append("-----------------------------------------\n");

        sb.append(reservation.toString()).append("\n");

        sb.append("-----------------------------------------\n");
        sb.append("Rental Days : ").append(RentalDays).append(" days\n");
        sb.append("Base Rent   : ").append(String.format("%.2f", baseRent)).append(" TL\n");
        if (insuranceTotal > 0) {
            sb.append("Insurance   : ").append(String.format("%.2f", insuranceTotal)).append(" TL\n");
        }
        if (discountAmount > 0) {
            sb.append("Discount    : -").append(String.format("%.2f", discountAmount)).append(" TL\n");
        }
        if (damageFee > 0) {
            sb.append("Damage Fee  : ").append(String.format("%.2f", damageFee)).append(" TL\n");
        }
        sb.append("-----------------------------------------\n");
        sb.append("TOTAL AMOUNT: ").append(String.format("%.2f", totalAmount)).append(" TL\n");

        return sb.toString();
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getDamageFee() {
        return damageFee;
    }
}
