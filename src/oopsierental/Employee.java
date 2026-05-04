package oopsierental;

import java.io.Serializable;

public abstract class Employee implements Serializable {
    private String employeeId;
    private String fullName;
    private String username;
    private String password;

    // Composition: Each employee is strictly tied to a specific branch instance
    private Branch branch;

    public Employee(String employeeId, String fullName, String username, String password, Branch branch) {
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.branch = branch;
    }

    // Defines role-specific permissions to be overridden by subclasses (Mechanic,
    // Agent, Manager)
    public abstract String getRolePermissions();

    public String getFullName() {
        return fullName;
    }

    public String getName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Branch getBranch() {
        return branch;
    }

    public String getEmployeeId() {
        return employeeId;
    }
}