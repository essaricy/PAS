package com.softvision.ipm.pms.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.softvision.ipm.pms.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment=WebEnvironment.DEFINED_PORT)
public class DataLoader {

	@Autowired private EmployeeDataManager employeeDataManager;

	@Autowired AppraisalCycleDataManager appraisalCycleDataManager;

	@Autowired TemplateDataManager templateDataManager;

	@Test
	public void cleanLoad() throws Exception {
		AbstractDataManager[] dataManagers = {
				//employeeDataManager, 
				//appraisalCycleDataManager,
				templateDataManager};
		for (AbstractDataManager abstractDataManager : dataManagers) {
			abstractDataManager.clearData();
			abstractDataManager.loadData();
		}
	}

}
