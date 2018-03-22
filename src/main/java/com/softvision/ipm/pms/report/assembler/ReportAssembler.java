package com.softvision.ipm.pms.report.assembler;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.softvision.ipm.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.ipm.pms.report.model.PhasewiseEmployeeStatusCountDto;

public class ReportAssembler {

	public static PhasewiseEmployeeStatusCountDto getEmployeeStatusCount(ResultSet rs) throws SQLException {
		PhasewiseEmployeeStatusCountDto countDto = new PhasewiseEmployeeStatusCountDto();
    	countDto.setPhaseAssignmentStatus(PhaseAssignmentStatus.get(rs.getInt("status")));
    	countDto.setCount(rs.getInt("count"));
        return countDto;
	}

}
