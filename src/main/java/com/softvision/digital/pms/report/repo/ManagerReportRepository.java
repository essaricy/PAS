package com.softvision.digital.pms.report.repo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.softvision.digital.pms.common.repo.AbstractRepository;
import com.softvision.digital.pms.report.assembler.ReportSqlAssembler;
import com.softvision.digital.pms.report.model.PhasewiseEmployeeStatusCountDto;

@Repository
public class ManagerReportRepository extends AbstractRepository {

	public List<PhasewiseEmployeeStatusCountDto> getPhasewiseEmployeeStatusCounts(int requestedEmplyeeId) {
		return jdbcTemplate.query(
				ManagerReportSql.SELECT_PHASEWISE_EMPLOYEE_STATUS_COUNT,
			    new Object[] {requestedEmplyeeId},
			    new RowMapper<PhasewiseEmployeeStatusCountDto>() {
			        public PhasewiseEmployeeStatusCountDto mapRow(ResultSet rs, int rowNum) throws SQLException {
			        	return ReportSqlAssembler.getEmployeeStatusCount(rs);
			        }
			    });
	}

}
