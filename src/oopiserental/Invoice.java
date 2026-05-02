/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oopiserental;

import java.util.Date;

/**
 *
 * @author murat
 */
public class Invoice {

    private String invoiceId;
    private Reservation reservation; // Composition
    private Date generationDate;

    // Constructor to initialize invoice with ID and reservation data
    public Invoice(String invoiceId, Reservation reservation) {
        this.invoiceId = invoiceId;
        this.reservation = reservation;
        this.generationDate = new Date(); // Automatically sets the current date
    }

    // Formats the invoice details into a professional string for printing
    public String getFormattedInvoice() {
        StringBuilder sb = new StringBuilder();
        sb.append("---------- RENT-A-CAR INVOICE ----------\n");
        sb.append("Invoice No: ").append(invoiceId).append("\n");
        sb.append("Date: ").append(generationDate.toString()).append("\n");
        sb.append("-----------------------------------------\n");
        sb.append(reservation.toString()).append("\n"); // Calls reservation's summary
        sb.append("-----------------------------------------\n");
        sb.append("Have a safe trip!\n");
        return sb.toString();
    }

    public String getInvoiceId() {
        return invoiceId;
    }
}
