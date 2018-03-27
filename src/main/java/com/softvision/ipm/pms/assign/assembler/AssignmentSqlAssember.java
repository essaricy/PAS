package com.softvision.ipm.pms.assign.assembler;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.employee.entity.Employee;

public class AssignmentSqlAssember {

	public static EmployeeAssignmentDto getEmployeeAssignment(ResultSet rs) throws SQLException {
		EmployeeAssignmentDto employeeAssignment = new EmployeeAssignmentDto();
		employeeAssignment.setAssignmentId(rs.getLong("id"));

		employeeAssignment.setStatus(rs.getInt("status"));
		employeeAssignment.setAssignedAt(rs.getDate("assigned_at"));

		Employee assignedBy = new Employee();
		assignedBy.setEmployeeId(rs.getInt("assigned_by_id"));
		assignedBy.setFirstName(rs.getString("assigned_by_first_name"));
		assignedBy.setLastName(rs.getString("assigned_by_last_name"));
		employeeAssignment.setAssignedBy(assignedBy);

		Employee assignedTo = new Employee();
		assignedTo.setEmployeeId(rs.getInt("assigned_to_id"));
		assignedTo.setFirstName(rs.getString("assigned_to_first_name"));
		assignedTo.setLastName(rs.getString("assigned_to_last_name"));
		employeeAssignment.setAssignedTo(assignedTo);
		employeeAssignment.setScore(rs.getDouble("score"));
		return employeeAssignment;
	}

}
