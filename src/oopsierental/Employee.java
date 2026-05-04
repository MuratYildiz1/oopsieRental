package oopsierental;

import java.io.Serializable;


public abstract class Employee implements Serializable {
    private String employeeId;
    private String fullName;
    private String username;
    private String password;
    private Branch branch; // Composition: Each employee belongs to a branch

    public Employee(String employeeId, String fullName, String username, String password, Branch branch) {
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.branch = branch;
    }

    // Abstract method: Each role will have its own primary responsibility
    public abstract String getRolePermissions();

    // Getters
    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Branch getBranch() { return branch; }
    public String getEmployeeId() { return employeeId; }
}