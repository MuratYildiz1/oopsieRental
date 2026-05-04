package oopsierental;

public class BranchManager extends Employee {
    public BranchManager(String id, String name, String user, String pass, Branch branch) {
        super(id, name, user, pass, branch);
    }

    @Override
    public String getRolePermissions() {
        return "Full Access: View Reports, Manage Branch Inventory";
    }
}
