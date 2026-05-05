package oopsierental;

/**
 * @author MuratYildiz1
 * @author KeremHKardes
 */

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

        // Calculate final refund from the initial deposit
        this.finalRefund = reservation.getDepositAmount() - this.totalDeductions;
    }

    public String getFormattedInvoice() {
        StringBuilder sb = new StringBuilder();
        sb.append("---------- RETURN & REFUND INVOICE ----------\n");
        sb.append("Invoice No: ").append(invoiceId).append("\n");
        sb.append("Date: ").append(generationDate.toString()).append("\n");

        // Customer info with loyalty status
        sb.append("Customer: ").append(reservation.getCustomer().getName()).append(" ")
                .append(reservation.getCustomer().getSurname()).append("\n");
        sb.append("Loyalty Tier: ").append(reservation.getCustomer().getLoyaltyTier())
                .append(" (").append(reservation.getCustomer().getLoyaltyPoints()).append(" points)\n");

        sb.append("Vehicle: ").append(reservation.getVehicle().getBrand()).append(" (")
                .append(reservation.getVehicle().getPlate()).append(")\n");
        sb.append("Pick-up City: ").append(reservation.getPickUpLocation()).append("\n");
        sb.append("Drop-off City: ").append(reservation.getReturnLocation()).append("\n");

        // Rental cost calculation with loyalty discount
        double discountRate = reservation.getCustomer().getDiscountRate();
        double baseRental = reservation.getVehicle().calculateRent(reservation.getDays());
        double insuranceCost = reservation.getInsuranceDailyCost() * reservation.getDays();
        double subtotal = baseRental + insuranceCost;
        double discountAmount = subtotal * discountRate;
        double discountedSubtotal = subtotal - discountAmount;

        sb.append("-----------------------------------------\n");
        sb.append("Rental Cost: ").append(String.format("%.2f", baseRental)).append(" TL\n");
        sb.append("Insurance: ").append(String.format("%.2f", insuranceCost)).append(" TL\n");
        sb.append("Subtotal: ").append(String.format("%.2f", subtotal)).append(" TL\n");

        if (discountRate > 0) {
            sb.append("Loyalty Discount (")
                    .append(String.format("%.0f%%", discountRate * 100))
                    .append("): -").append(String.format("%.2f", discountAmount)).append(" TL\n");
            sb.append("After Discount: ").append(String.format("%.2f", discountedSubtotal)).append(" TL\n");
        }

        sb.append("Initial Deposit: ").append(reservation.getDepositAmount()).append(" TL\n");
        sb.append("Deductions:\n").append(deductionDetails);
        sb.append("-----------------------------------------\n");
        sb.append("FINAL REFUND TO CUSTOMER: ").append(String.format("%.2f", finalRefund)).append(" TL\n");
        sb.append("=========================================\n");

        return sb.toString();
    }

    public String getInvoiceId() {
        return invoiceId;
    }
}