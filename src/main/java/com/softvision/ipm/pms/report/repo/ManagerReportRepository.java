package com.softvision.ipm.pms.report.repo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.softvision.ipm.pms.common.repo.AbstractRepository;
import com.softvision.ipm.pms.report.assembler.ReportAssembler;
import com.softvision.ipm.pms.report.model.PhasewiseEmployeeStatusCountDto;

@Repository
public class ManagerReportRepository extends AbstractRepository {

	public List<PhasewiseEmployeeStatusCountDto> getPhasewiseEmployeeStatusCounts(int requestedEmplyeeId) {
		List<PhasewiseEmployeeStatusCountDto> list = jdbcTemplate.query(
				ManagerReportSql.SELECT_PHASEWISE_EMPLOYEE_STATUS_COUNT,
			    new Object[] {requestedEmplyeeId},
			    new RowMapper<PhasewiseEmployeeStatusCountDto>() {
			        public PhasewiseEmployeeStatusCountDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			        	return ReportAssembler.getEmployeeStatusCount(rs);
			        }
			    });
		return list;
	}

}
