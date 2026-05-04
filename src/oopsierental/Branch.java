package oopsierental;

import java.io.Serializable;
import java.util.ArrayList;

public class Branch implements Serializable {
    private String branchId;
    private String branchName;
    private String city;

    public Branch(String branchId, String branchName, String city) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.city = city;
    }

    public static Branch findById(ArrayList<Branch> branches, String id) {
        for (Branch b : branches) {
            if (b.getBranchId().equals(id)) {
                return b;
            }
        }
        return null; // Bulunamazsa null döner
    }

    // Getters and Setters
    public String getBranchId() {
        return branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return branchName + " (" + city + ")";
    }

}
