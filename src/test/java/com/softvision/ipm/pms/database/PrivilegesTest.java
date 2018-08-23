package com.softvision.ipm.pms.database;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.test.context.junit4.SpringRunner;

import com.softvision.ipm.pms.Application;
import com.softvision.ipm.pms.appraisal.model.AppraisalCycleDto;
import com.softvision.ipm.pms.appraisal.service.AppraisalService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment=WebEnvironment.DEFINED_PORT)
public class PrivilegesTest {

	@Autowired private JdbcTemplate jdbcTemplate;

	@Autowired private AppraisalService appraisalService;

	@Test
	@Ignore
	public void shouldConnectToDatabase() throws Exception {
		log.info("shouldConnectToDatabase()");
		DataSource dataSource = jdbcTemplate.getDataSource();
		Connection connection = dataSource.getConnection();
		assertNotNull(connection);
	}

	@Test
	@Ignore
	public void should_SELECT_onTables() throws Exception {
		log.info("should_SELECT_onTables()");
		List<AppraisalCycleDto> cycles = appraisalService.getCycles();
		assertNotNull(cycles);
	}

	@Test
	@Ignore
	public void should_USAGE_onSequences() throws Exception {
		log.info("should_USAGE_onSequences()");
		jdbcTemplate.query("select nextval('goal_id_seq') as goal_id", new ResultSetExtractor<Integer>() {
			@Override
			public Integer extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				return resultSet.getInt("goal_id");
			}
		});
		List<AppraisalCycleDto> cycles = appraisalService.getCycles();
		assertNotNull(cycles);
	}

}
