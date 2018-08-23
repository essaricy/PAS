package com.softvision.digital.pms.assign.assembler;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.softvision.digital.pms.appraisal.model.AppraisalPhaseDto;
import com.softvision.digital.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.digital.pms.assign.model.EmployeePhaseAssignmentDto;
import com.softvision.digital.pms.employee.model.EmployeeDto;
import com.softvision.digital.pms.template.model.TemplateDto;

public class AssignmentSqlAssembler {

	private AssignmentSqlAssembler() {}

    public static EmployeeAssignmentDto getEmployeeCycleAssignment(ResultSet rs) throws SQLException {
        EmployeeAssignmentDto employeeAssignment = new EmployeeAssignmentDto();
        employeeAssignment.setAssignmentId(rs.getLong("id"));

        employeeAssignment.setStatus(rs.getInt("status"));
        employeeAssignment.setAssignedAt(rs.getDate("assigned_at"));

        EmployeeDto assignedBy = new EmployeeDto();
        assignedBy.setEmployeeId(rs.getInt("assigned_by_id"));
        assignedBy.setFirstName(rs.getString("assigned_by_first_name"));
        assignedBy.setLastName(rs.getString("assigned_by_last_name"));
        assignedBy.setFullName(assignedBy.getFirstName() + " " + assignedBy.getLastName());
        employeeAssignment.setAssignedBy(assignedBy);

        EmployeeDto assignedTo = new EmployeeDto();
        assignedTo.setEmployeeId(rs.getInt("assigned_to_id"));
        assignedTo.setFirstName(rs.getString("assigned_to_first_name"));
        assignedTo.setLastName(rs.getString("assigned_to_last_name"));
        assignedTo.setFullName(assignedTo.getFirstName() + " " + assignedTo.getLastName());
        employeeAssignment.setAssignedTo(assignedTo);

        employeeAssignment.setScore(rs.getDouble("score"));
        return employeeAssignment;
    }

    public static EmployeePhaseAssignmentDto getEmployeePhaseAssignment(ResultSet rs) throws SQLException {
        EmployeePhaseAssignmentDto employeePhaseAssignment = new EmployeePhaseAssignmentDto();
        employeePhaseAssignment.setAssignmentId(rs.getLong("id"));

        employeePhaseAssignment.setStatus(rs.getInt("status"));
        employeePhaseAssignment.setAssignedAt(rs.getDate("assigned_at"));

        EmployeeDto assignedBy = new EmployeeDto();
        assignedBy.setEmployeeId(rs.getInt("assigned_by_id"));
        assignedBy.setFirstName(rs.getString("assigned_by_first_name"));
        assignedBy.setLastName(rs.getString("assigned_by_last_name"));
        assignedBy.setFullName(assignedBy.getFirstName() + " " + assignedBy.getLastName());
        employeePhaseAssignment.setAssignedBy(assignedBy);

        EmployeeDto assignedTo = new EmployeeDto();
        assignedTo.setEmployeeId(rs.getInt("assigned_to_id"));
        assignedTo.setFirstName(rs.getString("assigned_to_first_name"));
        assignedTo.setLastName(rs.getString("assigned_to_last_name"));
        assignedTo.setFullName(assignedTo.getFirstName() + " " + assignedTo.getLastName());
        employeePhaseAssignment.setAssignedTo(assignedTo);
        employeePhaseAssignment.setScore(rs.getDouble("score"));

        TemplateDto template = new TemplateDto();
        template.setId(rs.getLong("template_id"));
        template.setName(rs.getString("template_name"));
        employeePhaseAssignment.setTemplate(template);

        AppraisalPhaseDto phaseDto = new AppraisalPhaseDto();
        phaseDto.setId(rs.getInt("appr_phase_id"));
        phaseDto.setName(rs.getString("appr_phase_name"));
        employeePhaseAssignment.setPhase(phaseDto);
        return employeePhaseAssignment;
    }

}
