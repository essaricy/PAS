package com.softvision.ipm.pms.assign.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softvision.ipm.pms.assign.entity.CycleAssignment;
import com.softvision.ipm.pms.assign.entity.PhaseAssignment;
import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;

@Component
public class AssignmentMapper {

    @Autowired ModelMapper mapper;

    public EmployeeAssignmentDto get(PhaseAssignment employeePhaseAssignment) {
        return mapper.map(employeePhaseAssignment, EmployeeAssignmentDto.class);
    }

    public EmployeeAssignmentDto get(CycleAssignment employeeCycleAssignment) {
        return mapper.map(employeeCycleAssignment, EmployeeAssignmentDto.class);
    }

}
