package oopsierental;

/**
 * @author MuratYildiz1
 * @author KeremHKardes
 */

public class BranchManager extends Employee {
    public BranchManager(String employeeId, String fullName, String username, String password, Branch branch) {
        super(employeeId, fullName, username, password, branch);
    }

    @Override
    public String getRolePermissions() {
        return "Full Access: View Reports, Manage Branch Inventory";
    }
}