package com.softvision.digital.pms.assign.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softvision.digital.pms.assign.entity.CycleAssignment;
import com.softvision.digital.pms.assign.entity.PhaseAssignment;
import com.softvision.digital.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.digital.pms.assign.model.EmployeePhaseAssignmentDto;

@Component
public class AssignmentMapper {

    @Autowired ModelMapper mapper;

    public EmployeePhaseAssignmentDto get(PhaseAssignment phaseAssignment) {
        return mapper.map(phaseAssignment, EmployeePhaseAssignmentDto.class);
    }

    public EmployeeAssignmentDto get(CycleAssignment cycleAssignment) {
        return mapper.map(cycleAssignment, EmployeeAssignmentDto.class);
    }

}
