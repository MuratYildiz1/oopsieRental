/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oopsierental;

/**
 *
 * @author murat
 */
public class Customer {
    // Encapsulation: private fields to protect data from direct access
    private String id;
    private String name;
    private String surname;
    private String loyaltyTier;
    private int loyaltyPoints;

    // Constructor to initialize a new customer with default Bronze tier
    public Customer(String id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.loyaltyTier = "Bronze";
        this.loyaltyPoints = 0;
    }

    // Logic to return discount percentage based on loyalty tier
    public double getDiscountRate() {
        if (loyaltyTier.equalsIgnoreCase("Gold")) {
            return 0.20;
        } else if (loyaltyTier.equalsIgnoreCase("Silver")) {
            return 0.10;
        }
        return 0.0;
    }

    // Getters and Setters (Encapsulation)
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
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

    public void addPoints(int points) {
        this.loyaltyPoints += points;
    }

    @Override
    public String toString() {
        return name + " " + surname + " (" + loyaltyTier + ")";
    }
}
