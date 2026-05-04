package oopsierental;

import java.util.Date;

public class Invoice {

    private String invoiceId;
    private Reservation reservation;
    private Date generationDate;
    private double totalDeductions;
    private double finalRefund;
    private String deductionDetails;

    public Invoice(String invoiceId, Reservation reservation, boolean hasWashingFee, boolean hasMissingObject) {
        this.invoiceId = invoiceId;
        this.reservation = reservation;
        this.generationDate = new Date();

        this.totalDeductions = 0;
        StringBuilder details = new StringBuilder();

        if (hasWashingFee) {
            this.totalDeductions += 500;
            details.append("- Washing Fee: 500 TL\n");
        }
        if (hasMissingObject) {
            this.totalDeductions += 2000;
            details.append("- Missing Object: 2000 TL\n");
        }

        if (details.length() == 0) {
            details.append("- No deductions.\n");
        }
        this.deductionDetails = details.toString();

        // Calculate how much of the 5000 TL deposit goes back to the customer
        this.finalRefund = reservation.getDepositAmount() - this.totalDeductions;
    }

    public String getFormattedInvoice() {
        StringBuilder sb = new StringBuilder();
        sb.append("---------- RETURN & REFUND INVOICE ----------\n");
        sb.append("Invoice No: ").append(invoiceId).append("\n");
        sb.append("Date: ").append(generationDate.toString()).append("\n");
        sb.append("Customer: ").append(reservation.getCustomer().getName()).append(" ")
                .append(reservation.getCustomer().getSurname()).append("\n");
        sb.append("Vehicle: ").append(reservation.getVehicle().getBrand()).append(" (")
                .append(reservation.getVehicle().getPlate()).append(")\n");
        sb.append("Pick-up City: ").append(reservation.getPickUpLocation()).append("\n");
        sb.append("Drop-off City: ").append(reservation.getReturnLocation()).append("\n");
        sb.append("-----------------------------------------\n");
        sb.append("Initial Deposit: ").append(reservation.getDepositAmount()).append(" TL\n");
        sb.append("Deductions:\n").append(deductionDetails);
        sb.append("-----------------------------------------\n");
        sb.append("FINAL REFUND TO CUSTOMER: ").append(finalRefund).append(" TL\n");
        sb.append("=========================================\n");

        return sb.toString();
    }

    public String getInvoiceId() {
        return invoiceId;
    }
}