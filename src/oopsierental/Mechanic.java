package oopsierental;

/**
 * @author MuratYildiz1
 * @author KeremHKardes
 */

public class Mechanic extends Employee {

    public Mechanic(String employeeId, String fullName, String username, String password, Branch branch) {
        super(employeeId, fullName, username, password, branch);
    }

    @Override
    public String getRolePermissions() {
        return "Technical Access: Manage Vehicle Maintenance and Damage Assessments";
    }
}