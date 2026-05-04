package oopsierental;

/**
 * @author MuratYildiz1
 * @author KeremHKardes
 */

public class RentalAgent extends Employee {

    public RentalAgent(String employeeId, String fullName, String username, String password, Branch branch) {
        super(employeeId, fullName, username, password, branch);
    }

    @Override
    public String getRolePermissions() {
        return "Limited Access: Create Reservations, Process Pick-ups/Returns";
    }

    // Processes vehicle return using boolean penalty flags instead of manual
    // calculations
    public Invoice processReturn(Reservation reservation, int drivenKm, boolean hasDamage, boolean hasWashingFee,
            boolean hasMissingObject) {
        Vehicle vehicle = reservation.getVehicle();
        vehicle.addMileage(drivenKm);

        if (hasDamage) {
            vehicle.setUnderMaintenance(true);
        } else {
            vehicle.setUnderMaintenance(false);
        }

        vehicle.setRented(false);
        vehicle.setRentedDays(0);

        String generatedInvoiceId = "INV-" + System.currentTimeMillis();
        Invoice finalInvoice = new Invoice(generatedInvoiceId, reservation, hasWashingFee, hasMissingObject);

        return finalInvoice;
    }
}