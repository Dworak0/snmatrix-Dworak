package com.example.crudapp.model;

public class User {
    private Long id;
    private String email;
    private String lastName;
    private String firstName;
    private String phone;
    private String employeeType;
    private String companyName;
    private boolean active;

    public User() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmployeeType() { return employeeType; }
    public void setEmployeeType(String employeeType) { this.employeeType = employeeType; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
