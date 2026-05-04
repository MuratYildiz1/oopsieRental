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

    // Constructor to initialize invoice with ID and reservation data
    public Invoice(String invoiceId, Reservation reservation, int RentalDays, double damageFee) {
        this.invoiceId = invoiceId;
        this.reservation = reservation;
        this.generationDate = new Date(); // Automatically sets the current date
        this.RentalDays = RentalDays;
        this.damageFee = damageFee;

        double dailyVehicleRate = reservation.getVehicle().getDailyRate();
        double dailyInsuranceRate = reservation.getInsuranceDailyCost();

        double baseRent = reservation.getVehicle().getDailyRate() * RentalDays;
        this.totalAmount = baseRent + damageFee;
    }

    // Formats the invoice details into a professional string for printing
    public String getFormattedInvoice() {
        StringBuilder sb = new StringBuilder();
        sb.append("---------- RENT-A-CAR INVOICE ----------\n");
        sb.append("Invoice No: ").append(invoiceId).append("\n");
        sb.append("Date: ").append(generationDate.toString()).append("\n");
        sb.append("-----------------------------------------\n");

        sb.append(reservation.toString()).append("\n");

        sb.append("-----------------------------------------\n");
        sb.append("Rental Days : ").append(RentalDays).append(" days\n");
        sb.append("Base Rent   : ").append(totalAmount - damageFee).append(" TL\n");
        
        sb.append(" (Includes ").append(reservation.getInsuranceType()).append(" Insurance)\n");

        if (damageFee > 0) {
            sb.append("DAMAGE FEE  : ").append(damageFee).append(" TL (Damage Assessment Applied)\n");
        }
        sb.append("-----------------------------------------\n");
        sb.append("TOTAL AMOUNT: ").append(totalAmount).append(" TL\n");
        sb.append("-----------------------------------------\n");
        sb.append("Have a safe trip!\n");

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
