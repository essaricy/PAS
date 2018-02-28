package com.softvision.ipm.pms.assessment.assembler;

import java.util.ArrayList;
import java.util.List;

import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.assessment.entity.EmployeeAssign;
import com.softvision.ipm.pms.assessment.model.EmployeeAssignDto;
import com.softvision.ipm.pms.employee.entity.Employee;
import com.softvision.ipm.pms.template.entity.Template;

public class EmployeeAssignAssembler {

	public static List<EmployeeAssignDto> getAllEmployeeAssigns(List<EmployeeAssign> findAll) {
		List<EmployeeAssignDto> list = new ArrayList<>();
		for (EmployeeAssign object : findAll) {
			list.add(getEmployeeAssignDto(object));
		}
		return list;
	}
	
	public static EmployeeAssignDto getEmployeeAssignDto(EmployeeAssign object) {
		EmployeeAssignDto dto = null;
		if (object != null) {
			dto = new EmployeeAssignDto();
			dto.setId(object.getId());
			Employee emp=object.getEmployee();
			dto.setEmployee_number(emp.getEmployeeId());
			dto.setEmployee_name(emp.getFirstName());
			
			AppraisalCycle appraisalCycle=object.getCycle();
			dto.setAppraisal_cycle_id(appraisalCycle.getId());
			dto.setAppraisalCycleName(appraisalCycle.getName());
			
			AppraisalPhase appraisalPhase=object.getPhases();
			dto.setAppraisal_phase_id(appraisalPhase.getId());
			dto.setAppraisalPhaseName(appraisalPhase.getName());
			
			Template template =object.getTemplate();
			dto.setTemplate_id(template.getId());
			dto.setAppraisalTemplateName(template.getName());
			dto.setAssignedBy(object.getAssignedBy());
			dto.setAssignedAt(object.getAssignedAt());
		}
		return dto;
	}
	
	public static EmployeeAssign getEmployeeAssign(EmployeeAssignDto dto) {
		EmployeeAssign object = null;
		if (dto != null) {
			object = new EmployeeAssign();
			object.setId(dto.getId());
			object.setAssignedAt(dto.getAssignedAt());
			object.setAssignedBy(dto.getAssignedBy());
			System.out.println("---> "+dto.getEmployee_number());
			Employee employee=new Employee();
			employee.setEmployeeId(dto.getEmployee_number());
			object.setEmployee(employee);
			
			AppraisalCycle appraisalCycle=new AppraisalCycle();
			appraisalCycle.setId( dto.getAppraisal_cycle_id());
			object.setCycle(appraisalCycle);
			
			AppraisalPhase appraisalPhase=new AppraisalPhase();
			appraisalPhase.setId(dto.getAppraisal_phase_id());
			object.setPhases(appraisalPhase);
			
			Template template =new Template();
			template.setId(dto.getTemplate_id());
			object.setTemplate(template);
			
		}
		return object;
	}

}
