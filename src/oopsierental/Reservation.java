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

        // Award loyalty points: 10 points per rental
        customer.addPoints(10);

        this.totalPrice = calculateFinalAmount();
        vehicle.setRented(true);
        vehicle.setRentedDays(days);
    }

    // Constructor for loading from file (skips availability check)
    public Reservation(String reservationId, Customer customer, Vehicle vehicle, int days,
            String insuranceType, int insuranceDailyCost, String returnLocation, String employee, boolean isLoading) {
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
        double insuranceCost = insuranceDailyCost * days;
        double subtotal = baseRent + insuranceCost;

        // Apply loyalty discount to rental + insurance (deposit excluded)
        double discountRate = customer.getDiscountRate();
        double discountAmount = subtotal * discountRate;
        double discountedSubtotal = subtotal - discountAmount;

        // Final amount: Discounted Rent + Insurance + Deposit
        return discountedSubtotal + DEPOSIT_AMOUNT;
    }

    public String getReservationId() {
        return reservationId;
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

    public double getDepositAmount() {
        return DEPOSIT_AMOUNT;
    }

    @Override
    public String toString() {
        return "ID: " + reservationId + " | Customer: " + customer.getName() + " " + customer.getSurname()
                + " | Vehicle: " + vehicle.getPlate() + " | Total: " + totalPrice + " TL";
    }
}