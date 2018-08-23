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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((phase == null) ? 0 : phase.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeePhaseAssignmentDto other = (EmployeePhaseAssignmentDto) obj;
		if (phase == null) {
			if (other.phase != null)
				return false;
		} else if (!phase.equals(other.phase))
			return false;
		return true;
	}

}
