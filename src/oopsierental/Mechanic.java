package oopsierental;

public class Mechanic extends Employee {
    public Mechanic(String id, String name, String user, String pass, Branch branch) {
        super(id, name, user, pass, branch);
    }

    @Override
    public String getRolePermissions() {
        return "Technical Access: Manage Vehicle Maintenance and Damage Assessments";
    }

}
