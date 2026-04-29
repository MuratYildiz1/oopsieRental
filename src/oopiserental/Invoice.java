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

    public Invoice(String invoiceId, Reservation reservation) {
        this.invoiceId = invoiceId;
        this.reservation = reservation;
        this.generationDate = new Date(); // Fatura oluşturulma tarihi
    }

    // Faturayı profesyonel bir formatta metne dönüştürür
    public String getFormattedInvoice() {
        StringBuilder sb = new StringBuilder();
        sb.append("---------- RENT-A-CAR FATURASI ----------\n");
        sb.append("Fatura No: ").append(invoiceId).append("\n");
        sb.append("Tarih: ").append(generationDate.toString()).append("\n");
        sb.append("-----------------------------------------\n");
        sb.append(reservation.toString()).append("\n");
        sb.append("-----------------------------------------\n");
        sb.append("İyi yolculuklar dileriz!\n");
        return sb.toString();
    }

    public String getInvoiceId() { return invoiceId; }
}
