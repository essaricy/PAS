package com.softvision.ipm.pms.assign.model;

import com.softvision.ipm.pms.appraisal.model.AppraisalPhaseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeePhaseAssignmentDto extends EmployeeAssignmentDto {

    private static final long serialVersionUID = 1L;

    private AppraisalPhaseDto phase;

    @Override
    public String toString() {
        return "EmployeePhaseAssignmentDto [phase=" + phase + "]";
    }


}
