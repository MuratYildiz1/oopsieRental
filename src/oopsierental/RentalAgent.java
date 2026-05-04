package oopsierental;

public class RentalAgent extends Employee {

    public RentalAgent(String id, String name, String user, String pass, Branch branch) {
        super(id, name, user, pass, branch);
    }

    @Override
    public String getRolePermissions() {
        return "Limited Access: Create Reservations, Process Pick-ups/Returns";
    }

    // YENI MANTIK: Artık gün sayısı veya double hasar yerine, boolean kesintileri
    // alıyor.
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

        // PARAMETRELER YENI INVOICE SINIFINA UYGUN HALE GETIRILDI
        Invoice finalInvoice = new Invoice(generatedInvoiceId, reservation, hasWashingFee, hasMissingObject);

        return finalInvoice;
    }
}