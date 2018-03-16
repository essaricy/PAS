package com.sv.ipm.pms;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.softvision.ipm.pms.Application;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.util.ValidationUtil;
import com.softvision.ipm.pms.template.model.TemplateDetailDto;
import com.softvision.ipm.pms.template.model.TemplateDto;
import com.softvision.ipm.pms.template.model.TemplateHeaderDto;
import com.softvision.ipm.pms.template.service.TemplateService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment=WebEnvironment.DEFINED_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TemplateTest {

	@Autowired TemplateService templateService;

	@Test
	@Ignore
	public void test1_getAll() {
		System.out.println("test1_getAll");
		List<TemplateDto> templates = templateService.getTemplates();
		System.out.println(templates);
		assertNotNull(templates);
		assertTrue(!templates.isEmpty());
		TemplateDto template = templates.get(0);
		assertNotNull(template);
		assertTrue(template.getId() != 0);
		assertTrue(!StringUtils.isBlank(template.getName()));
		List<TemplateHeaderDto> headers = template.getHeaders();
		assertNotNull(headers);
		assertTrue(!headers.isEmpty());
		TemplateHeaderDto header = headers.get(0);
		assertNotNull(header);
		assertTrue(header.getId() != 0);
		assertTrue(header.getWeightage() != 0);
		assertTrue(header.getGoalId() != 0);
		assertNotNull(header.getGoalName());
		List<TemplateDetailDto> details = header.getDetails();
		assertNotNull(details);
		assertTrue(!details.isEmpty());
		TemplateDetailDto detail = details.get(0);
		assertNotNull(detail);
		assertTrue(detail.getId() != 0);
		assertTrue(detail.getParamId() != 0);
		assertNotNull(detail.getParamName());
		assertNotNull(detail.getApply());
	}

	@Test
	@Ignore
	public void test2_getById() {
		System.out.println("test2_getById");
		TemplateDto template = templateService.getTemplate(1);
		System.out.println("Template=" + template);
		assertNotNull(template);
		assertTrue(template.getId() != 0);
		assertTrue(!StringUtils.isBlank(template.getName()));
		List<TemplateHeaderDto> headers = template.getHeaders();
		assertNotNull(headers);
		assertTrue(!headers.isEmpty());
		TemplateHeaderDto header = headers.get(0);
		assertNotNull(header);
		assertTrue(header.getId() != 0);
		assertTrue(header.getWeightage() != 0);
		assertTrue(header.getGoalId() != 0);
		assertNotNull(header.getGoalName());
		List<TemplateDetailDto> details = header.getDetails();
		assertNotNull(details);
		assertTrue(!details.isEmpty());
		TemplateDetailDto detail = details.get(0);
		assertNotNull(detail);
		assertTrue(detail.getId() != 0);
		assertTrue(detail.getParamId() != 0);
		assertNotNull(detail.getParamName());
		assertNotNull(detail.getApply());
	}

	@Test
	@Ignore
	public void test3_update() throws ServiceException {
		System.out.println("test3_update");

		TemplateDto templateDto = new TemplateDto();
		templateDto.setId(0);
		templateDto.setName("Test Template - " + System.currentTimeMillis());
		templateDto.setUpdatedAt(new Date());
		templateDto.setUpdatedBy("srikanth.kumar");

		ValidationUtil.validate(templateDto);
		//templateService.update(templateDto);
	}

	@Test
    @Ignore
	public void test4_isTemplateInUse() throws ServiceException {
		boolean inUse = templateService.isInUse(6);
		System.out.println("inUse = " + inUse);
	}

}
