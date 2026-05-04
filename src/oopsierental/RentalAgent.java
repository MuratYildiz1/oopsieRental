package oopsierental;

public class RentalAgent extends Employee {
    public RentalAgent(String id, String name, String user, String pass, Branch branch) {
        super(id, name, user, pass, branch);
    }

    @Override
    public String getRolePermissions() {
        return "Limited Access: Create Reservations, Process Pick-ups/Returns";
    }

    public Invoice processReturn(Reservation reservation, int drivenKm, boolean hasDamage, double damageFee) {
        
        Vehicle vehicle = reservation.getVehicle();

        vehicle.addMileage(drivenKm);

        if (hasDamage) {
            vehicle.setUnderMaintenance(true);
        } else {
            vehicle.setUnderMaintenance(false);
        }
        
        vehicle.setRented(false);
        int days = vehicle.getRentedDays(); 
        vehicle.setRentedDays(0); 

        String generatedInvoiceId = "INV-" + System.currentTimeMillis(); 
        
        Invoice finalInvoice = new Invoice(generatedInvoiceId, reservation, days, damageFee);
        
        return finalInvoice;
    }
}
