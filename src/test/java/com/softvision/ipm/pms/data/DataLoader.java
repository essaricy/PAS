package com.softvision.ipm.pms.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.softvision.ipm.pms.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment=WebEnvironment.DEFINED_PORT)
public class DataLoader {

	@Autowired JdbcTemplate jdbcTemplate;

	@Autowired EmployeeDataManager employeeDataManager;

	@Autowired AppraisalCycleDataManager appraisalCycleDataManager;

	@Autowired TemplateDataManager templateDataManager;

	@Autowired AssignmentDataManager assignmentDataManager;

	@Test
	public void cleanLoad() throws Exception {
		clean();

		appraisalCycleDataManager.loadData();
		employeeDataManager.loadData();
		templateDataManager.loadData();
		assignmentDataManager.loadData();
	}

	private void clean() {
		clean("assess_detail");
		clean("assess_header");
		clean("phase_assign");
		clean("cycle_assign");
		clean("template_detail");
		clean("template_header");
		clean("template");
		clean("employee_role");
		clean("employee");
		clean("appr_phase");
		clean("appr_cycle");
	}

	private void clean(String table) {
		jdbcTemplate.update("delete from " + table);
	}

}
