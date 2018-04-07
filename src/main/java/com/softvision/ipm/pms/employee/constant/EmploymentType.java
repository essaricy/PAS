package com.softvision.ipm.pms.employee.constant;

import java.util.Arrays;

public enum EmploymentType {

    PROBATIONARY("Probationary"),
    CONTRACTOR("Contractor"),
    CONFIRMED("Confirmed"),
    PROJECT_EMPLOYEE("Project Employee"),
    TEMPORARY("Temporary"),
    CONSULTANT("Consultant"),
    CONTRACT("Contract"),
    REGULAR_EMPLOYEE("Regular Employee");

    private String name;

    private EmploymentType(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public static EmploymentType get(String name) {
        if (name != null) {
            return Arrays.stream(values()).filter(e -> e.getName().equalsIgnoreCase(name.trim())).findFirst().orElse(null);
        }
        return null;
    }

}
