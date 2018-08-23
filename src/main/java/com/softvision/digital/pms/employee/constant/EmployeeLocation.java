package com.softvision.digital.pms.employee.constant;

import java.util.Arrays;

public enum EmployeeLocation {

    AUSTRALIA("Australia"),
    BRAZIL("Brazil"),
    CANADA("Canada"),
    INDIA("India"),
    NEPAL("Nepal"),
    ROMANIA("Romania"),
    SINGAPORE("Singapore"),
    USA("USA");

    private String name;

    private EmployeeLocation(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public static EmployeeLocation get(String name) {
        if (name != null) {
            return Arrays.stream(values()).filter(e -> e.getName().equalsIgnoreCase(name.trim())).findFirst().orElse(null);
        }
        return null;
    }

}
