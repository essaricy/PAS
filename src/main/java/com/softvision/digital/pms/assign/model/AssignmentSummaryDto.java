package com.softvision.digital.pms.assign.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
public class AssignmentSummaryDto extends EmployeeAssignmentDto {

	private static final long serialVersionUID = 1L;

	private double managerScore;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(managerScore);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		AssignmentSummaryDto other = (AssignmentSummaryDto) obj;
		if (Double.doubleToLongBits(managerScore) != Double.doubleToLongBits(other.managerScore))
			return false;
		return true;
	}

}
