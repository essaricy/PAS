package com.softvision.ipm.pms.common.constants;

public class AuthorizeConstant {

	public static final String IS_MANAGER = "hasAuthority('MANAGER')";

	public static final String IS_MANAGER_OR_ADMIN = "hasAuthority('MANAGER') or hasAuthority('ADMIN')";

	public static final String IS_ADMIN = "hasAuthority('ADMIN')";

}
