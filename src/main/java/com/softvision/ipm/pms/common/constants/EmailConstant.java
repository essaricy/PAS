package com.softvision.ipm.pms.common.constants;
//
//public class EmailConstant {
//
//	public static final String KICK_OFF = "KICK_OFF";
//
//	public static final String EMPLOYEE_ENABLE = "EMPLOYEE_ENABLE";
//	
//	public static final String EMPLOYEE_SUBMITED = "EMPLOYEE_SUBMITED";
//	
//	public static final String MANAGER_REMAINDER = "MANAGER_REMAINDER";
//	
//	public static final String MANAGER_FROZEN = "MANAGER_FROZEN";
//	
//	public static final String MANAGER_REVIEW = "MANAGER_REVIEW";
//}

public enum EmailConstant {

KICK_OFF {
    public String toString() {
        return "KICK_OFF";
    }
},

EMPLOYEE_ENABLE {
    public String toString() {
        return "EMPLOYEE_ENABLE";
    }
},
EMPLOYEE_SUBMITED {
    public String toString() {
        return "EMPLOYEE_SUBMITED";
    }
},

MANAGER_REMAINDER {
    public String toString() {
        return "MANAGER_REMAINDER";
    }
},
MANAGER_FROZEN {
    public String toString() {
        return "MANAGER_FROZEN";
    }
},

MANAGER_REVIEW {
    public String toString() {
        return "MANAGER_REVIEW";
    }
}
}