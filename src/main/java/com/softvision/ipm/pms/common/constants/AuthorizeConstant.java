package com.softvision.ipm.pms.common.constants;

public class AuthorizeConstant {

	public static final String IS_EMPLOYEE = "hasAuthority('EMPLOYEE')";

	public static final String IS_EMPLOYEE_OR_MANAGER = "hasAuthority('EMPLOYEE') or hasAuthority('MANAGER') ";

	public static final String IS_MANAGER = "hasAuthority('MANAGER')";

	public static final String IS_MANAGER_OR_ADMIN = "hasAuthority('MANAGER') or hasAuthority('ADMIN')";

	public static final String IS_ADMIN = "hasAuthority('ADMIN')";

	public static final String IS_SUPPORT = "hasAuthority('SUPPORT')";

}
