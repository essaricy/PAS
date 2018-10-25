package com.softvision.digital.pms.assign.model;

import com.softvision.digital.pms.appraisal.model.AppraisalPhaseDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
public class EmployeePhaseAssignmentDto extends EmployeeAssignmentDto {

    private static final long serialVersionUID = 1L;

    private AppraisalPhaseDto phase;

}
