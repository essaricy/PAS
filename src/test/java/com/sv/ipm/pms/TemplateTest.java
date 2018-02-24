package com.sv.ipm.pms;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.softvision.ipm.pms.Application;
import com.softvision.ipm.pms.goal.entity.GoalCa;
import com.softvision.ipm.pms.template.entity.Template;
import com.softvision.ipm.pms.template.entity.TemplateHeader;
import com.softvision.ipm.pms.template.service.TemplateService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment=WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TemplateTest {

	private static final String GOAL_NAME = "goalCa-Test-" + System.currentTimeMillis();
	private static Long id;

	@Autowired TemplateService templateService;

	@Test()
	public void test1_getAll() {
		System.out.println("test1_getAll");
		List<Template> templates = templateService.getTemplates();
		System.out.println(templates);
		assertNotNull(templates);
		assertTrue(!templates.isEmpty());
		Template template = templates.get(0);
		assertNotNull(template);
		assertTrue(template.getId() != 0);
		assertTrue(!StringUtils.isBlank(template.getName()));
		List<TemplateHeader> headers = template.getTemplateHeaders();
		assertNotNull(headers);
		assertTrue(!headers.isEmpty());
		TemplateHeader header = headers.get(0);
		assertNotNull(header);
		assertTrue(header.getId() != 0);
		assertTrue(header.getWeightage() != 0);
		GoalCa goalCa = header.getGoalCa();
		assertNotNull(goalCa);
	}

}
