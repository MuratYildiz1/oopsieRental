package oopsierental;

/**
 * @author MuratYildiz1
 * @author KeremHKardes
 */

public class Customer {

    private String id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String loyaltyTier;
    private int loyaltyPoints;

    public Customer(String id, String name, String surname, String email, String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.loyaltyTier = "Bronze";
        this.loyaltyPoints = 0;
    }

    // Calculates discount rate dynamically based on customer's current loyalty tier
    public double getDiscountRate() {
        if (loyaltyTier.equalsIgnoreCase("Gold")) {
            return 0.20;
        } else if (loyaltyTier.equalsIgnoreCase("Silver")) {
            return 0.10;
        }
        return 0.0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getLoyaltyTier() {
        return loyaltyTier;
    }

    public void setLoyaltyTier(String loyaltyTier) {
        this.loyaltyTier = loyaltyTier;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
        updateLoyaltyTier();
    }

    public void addPoints(int points) {
        this.loyaltyPoints += points;
        updateLoyaltyTier();
    }

    public void updateLoyaltyTier() {
        if (loyaltyPoints >= 100) {
            this.loyaltyTier = "Gold";
        } else if (loyaltyPoints >= 20) {
            this.loyaltyTier = "Silver";
        } else {
            this.loyaltyTier = "Bronze";
        }
    }


    @Override
    public String toString() {
        return name + " " + surname + " (" + loyaltyTier + ")";
    }
}