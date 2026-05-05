package oopsierental;

/**
 * @author MuratYildiz1
 * @author KeremHKardes
 */

public class Reservation {

    private String reservationId;
    private Customer customer;
    private Vehicle vehicle;
    private int days;
    private double totalPrice;

    private String insuranceType;
    private double insuranceDailyCost;
    private String pickUpLocation;
    private String returnLocation;
    private String employee;
    private final double DEPOSIT_AMOUNT = 5000.0;

    public Reservation(String reservationId, Customer customer, Vehicle vehicle, int days,
            String insuranceType, double insuranceDailyCost, String returnLocation, String employee)
            throws RentalException {
        this.reservationId = reservationId;
        this.customer = customer;
        this.vehicle = vehicle;
        this.days = days;
        this.insuranceType = insuranceType;
        this.insuranceDailyCost = insuranceDailyCost;
        this.pickUpLocation = vehicle.getBranch().getCity();
        this.returnLocation = returnLocation;
        this.employee = employee;

        checkAvailability();

        this.totalPrice = calculateFinalAmount();
        vehicle.setRented(true);
        vehicle.setRentedDays(days);
    }

    // Constructor for loading from file (skips availability check)
    public Reservation(String reservationId, Customer customer, Vehicle vehicle, int days,
            String insuranceType, double insuranceDailyCost, String returnLocation, String employee, boolean isLoading) {
        this.reservationId = reservationId;
        this.customer = customer;
        this.vehicle = vehicle;
        this.days = days;
        this.insuranceType = insuranceType;
        this.insuranceDailyCost = insuranceDailyCost;
        this.pickUpLocation = vehicle.getBranch().getCity();
        this.returnLocation = returnLocation;
        this.employee = employee;

        this.totalPrice = calculateFinalAmount();
        // Do not set vehicle rented here, as it's already set in loadVehicles
    }

    public void checkAvailability() throws RentalException {
        if (vehicle.isRented()) {
            throw new RentalException("Error: Selected vehicle (" + vehicle.getPlate() + ") is already rented!");
        }
    }

    private double calculateFinalAmount() {
        double baseRent = vehicle.calculateRent(days);
        double insurance = insuranceDailyCost * days;
        double subtotal = baseRent + insurance;
        double discount = subtotal * customer.getDiscountRate();
        // Final amount: Subtotal after discount + Deposit
        return (subtotal - discount) + DEPOSIT_AMOUNT;
    }

    public String getReservationId() {
        return reservationId;
    }

    public double getDepositAmount() {
        return DEPOSIT_AMOUNT;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public int getDays() {
        return days;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public double getInsuranceDailyCost() {
        return insuranceDailyCost;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public String getReturnLocation() {
        return returnLocation;
    }

    public String getEmployee() {
        return employee;
    }

    public double getSubtotalBeforeDiscount() {
        double baseRent = vehicle.calculateRent(days);
        double insurance = insuranceDailyCost * days;
        return baseRent + insurance;
    }

    public double getDiscountAmount() {
        return getSubtotalBeforeDiscount() * customer.getDiscountRate();
    }

    public double getSubtotalAfterDiscount() {
        return getSubtotalBeforeDiscount() - getDiscountAmount();
    }

    @Override
    public String toString() {
        return "ID: " + reservationId + " | Customer: " + customer.getName() + " " + customer.getSurname()
                + " | Vehicle: " + vehicle.getPlate() + " | Total: " + totalPrice + " TL";
    }
}