package com.softvision.digital.pms.report.assembler;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.softvision.digital.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.digital.pms.report.model.PhasewiseEmployeeStatusCountDto;

public class ReportSqlAssembler {

	private ReportSqlAssembler() {}

	public static PhasewiseEmployeeStatusCountDto getEmployeeStatusCount(ResultSet rs) throws SQLException {
		PhasewiseEmployeeStatusCountDto countDto = new PhasewiseEmployeeStatusCountDto();
    	countDto.setPhaseAssignmentStatus(PhaseAssignmentStatus.get(rs.getInt("status")));
    	countDto.setCount(rs.getInt("count"));
        return countDto;
	}

}
