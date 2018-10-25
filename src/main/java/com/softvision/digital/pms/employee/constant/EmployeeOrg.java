package com.softvision.digital.pms.employee.constant;

import java.util.Arrays;

public enum EmployeeOrg {

    BPO("BPO"),
    DIGITAL("DIGITAL"),
    IT("IT"),
    NONE("N/A"),
    PST("PST"),
    SHILOH("SHILOH");

    private String name;

    private EmployeeOrg(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public static EmployeeOrg get(String name) {
        if (name != null) {
            return Arrays.stream(values()).filter(e -> e.getName().equalsIgnoreCase(name.trim())).findFirst().orElse(null);
        }
        return null;
    }

}
