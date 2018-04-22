package com.softvision.ipm.pms.mappertest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.modelmapper.ModelMapper;

import com.softvision.ipm.pms.goal.entity.Goal;
import com.softvision.ipm.pms.goal.entity.GoalParam;
import com.softvision.ipm.pms.template.entity.Template;
import com.softvision.ipm.pms.template.entity.TemplateDetail;
import com.softvision.ipm.pms.template.entity.TemplateHeader;
import com.softvision.ipm.pms.template.mapper.TemplateMapper;
import com.softvision.ipm.pms.template.model.TemplateDetailDto;
import com.softvision.ipm.pms.template.model.TemplateDto;
import com.softvision.ipm.pms.template.model.TemplateHeaderDto;
import com.softvision.ipm.pms.web.config.MyModelMapper;

public class Template_ModelToDto_MapperTest {

	@Spy ModelMapper mapper = new MyModelMapper();

	@InjectMocks private TemplateMapper templateMapper = new TemplateMapper();

	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Before
	public void prepare() {
		((MyModelMapper)mapper).loadMappings();
	}

    @Test
    public void test_templateNull() {
        assertNull(templateMapper.getTemplateDto((Template) null));
    }

    @Test
    public void test_headerNull() {
        assertNull(templateMapper.getTemplateHeaderDto((TemplateHeader) null));
    }

    @Test
    public void test_detailNull() {
        assertNull(templateMapper.getTemplateDetailDto((TemplateDetail) null));
    }

    @Test
    public void test_template() {
    	Template template = new Template();
    	template.setId(11L);
    	template.setName("Template Name");
    	template.setUpdatedAt(new Date());
    	template.setUpdatedBy(1136);
    	
    	int numberOfHeaders = 10;
    	int numberOfDetails = 20;
    	List<TemplateHeader> templateHeaders = new ArrayList<>();
    	for (int index = 0; index < numberOfHeaders; index++) {
    		TemplateHeader templateHeader = new TemplateHeader();
    		Goal goal = new Goal();
    		goal.setId((long)index);
    		goal.setName("Goal Name " + index);
    		List<GoalParam> params = new ArrayList<>();
    		GoalParam param = new GoalParam();
    		param.setId((long)index);
    		param.setApply("Y");
    		param.setName("Goal Param " + index);
			params.add(param);
			goal.setParams(params);


			List<TemplateDetail> templateDetails = new ArrayList<>();
			for (int jindex = 0; jindex < numberOfDetails; jindex++) {
				TemplateDetail templateDetail = new TemplateDetail();

				GoalParam param2 = new GoalParam();
				param2.setId((long)jindex);
				param2.setApply("Y");
				param2.setName("Goal Param " + jindex);

				templateDetail.setId((long)jindex);
				templateDetail.setApply("Y");
	    		templateDetail.setGoalParam(param2);
				templateDetails.add(templateDetail);
			}

			templateHeader.setId((long)index);
			templateHeader.setWeightage(index * 2);
			templateHeader.setGoal(goal);
			templateHeader.setTemplateDetails(templateDetails);
    		templateHeaders.add(templateHeader);
		}
		template.setTemplateHeaders(templateHeaders);


    	TemplateDto templateDto = templateMapper.getTemplateDto(template);
        assertNotNull(templateDto);
        assertEquals((Long)template.getId(), (Long)templateDto.getId());
        assertEquals(template.getName(), templateDto.getName());
        assertEquals(template.getUpdatedBy(), templateDto.getUpdatedBy().getEmployeeId());
        assertEquals(template.getUpdatedAt(), templateDto.getUpdatedAt());
        
        List<TemplateHeader> headers = template.getTemplateHeaders();
        List<TemplateHeaderDto> headerDtoList = templateDto.getHeaders();
        assertEquals(headers.size(), headerDtoList.size());
        for (int index = 0; index < headerDtoList.size(); index++) {
        	TemplateHeaderDto templateHeaderDto = headerDtoList.get(index);
        	TemplateHeader templateHeader = headers.get(index);
        	Goal goal = templateHeader.getGoal();
        	
        	assertEquals((long)templateHeader.getId(), (long)templateHeaderDto.getId());
        	assertEquals(templateHeader.getWeightage(), templateHeaderDto.getWeightage());
        	assertEquals((long)goal.getId(), (long)templateHeaderDto.getGoalId());
        	assertEquals(goal.getName(), templateHeaderDto.getGoalName());
        	
        	List<TemplateDetailDto> detailDtoList = templateHeaderDto.getDetails();
        	List<TemplateDetail> templateDetails = templateHeader.getTemplateDetails();
        	assertEquals(templateDetails.size(), detailDtoList.size());

        	for (int jindex = 0; jindex < detailDtoList.size(); jindex++) {
        		TemplateDetailDto templateDetailDto = detailDtoList.get(jindex);
        		TemplateDetail templateDetail = templateDetails.get(jindex);
        		
        		assertEquals((long)templateDetail.getId(), (long)templateDetailDto.getId());
        		assertEquals(templateDetail.getApply(), templateDetailDto.getApply());
        		assertEquals((long)templateDetail.getGoalParam().getId(), (long)templateDetailDto.getParamId());
        		assertEquals(templateDetail.getGoalParam().getName(), templateDetailDto.getParamName());
			}
		}
    }

    @Test
    public void test_templateList() {
    	int numberOfTemplates=10;
    	int numberOfHeaders = 20;
    	int numberOfDetails = 30;

    	List<Template> templates = new ArrayList<>();
    	for (int templateIndex = 0; templateIndex < numberOfTemplates; templateIndex++) {
			Template template = new Template();
			template.setId(11L);
			template.setName("Template Name");
			template.setUpdatedAt(new Date());
			template.setUpdatedBy(1136);
			List<TemplateHeader> templateHeaders = new ArrayList<>();
			for (int index = 0; index < numberOfHeaders; index++) {
				TemplateHeader templateHeader = new TemplateHeader();
				Goal goal = new Goal();
				goal.setId((long) index);
				goal.setName("Goal Name " + index);
				List<GoalParam> params = new ArrayList<>();
				GoalParam param = new GoalParam();
				param.setId((long) index);
				param.setApply("Y");
				param.setName("Goal Param " + index);
				params.add(param);
				goal.setParams(params);

				List<TemplateDetail> templateDetails = new ArrayList<>();
				for (int jindex = 0; jindex < numberOfDetails; jindex++) {
					TemplateDetail templateDetail = new TemplateDetail();

					GoalParam param2 = new GoalParam();
					param2.setId((long) jindex);
					param2.setApply("Y");
					param2.setName("Goal Param " + jindex);

					templateDetail.setId((long) jindex);
					templateDetail.setApply("Y");
					templateDetail.setGoalParam(param2);
					templateDetails.add(templateDetail);
				}

				templateHeader.setId((long) index);
				templateHeader.setWeightage(index * 2);
				templateHeader.setGoal(goal);
				templateHeader.setTemplateDetails(templateDetails);
				templateHeaders.add(templateHeader);
			}
			template.setTemplateHeaders(templateHeaders);
			templates.add(template);
		}

    	List<TemplateDto> templateDtoList = templateMapper.getTemplateDtoList(templates);
    	assertEquals(templates.size(), templateDtoList.size());

    	for (int templateIndex = 0; templateIndex < templateDtoList.size(); templateIndex++) {
    		TemplateDto templateDto = templateDtoList.get(templateIndex);
    		Template template = templates.get(templateIndex);
    		
    		assertNotNull(templateDto);
    		assertEquals((Long) template.getId(), (Long) templateDto.getId());
    		assertEquals(template.getName(), templateDto.getName());
    		System.out.println("Using = " + templateDto.getUpdatedBy());
    		assertEquals(template.getUpdatedBy(), templateDto.getUpdatedBy().getEmployeeId());
    		assertEquals(template.getUpdatedAt(), templateDto.getUpdatedAt());
    		List<TemplateHeader> headers = template.getTemplateHeaders();
    		List<TemplateHeaderDto> headerDtoList = templateDto.getHeaders();
    		assertEquals(headers.size(), headerDtoList.size());
    		for (int index = 0; index < headerDtoList.size(); index++) {
    			TemplateHeaderDto templateHeaderDto = headerDtoList.get(index);
    			TemplateHeader templateHeader = headers.get(index);
    			Goal goal = templateHeader.getGoal();
    			
    			assertEquals((long) templateHeader.getId(), (long) templateHeaderDto.getId());
    			assertEquals(templateHeader.getWeightage(), templateHeaderDto.getWeightage());
    			assertEquals((long) goal.getId(), (long) templateHeaderDto.getGoalId());
    			assertEquals(goal.getName(), templateHeaderDto.getGoalName());
    			
    			List<TemplateDetailDto> detailDtoList = templateHeaderDto.getDetails();
    			List<TemplateDetail> templateDetails = templateHeader.getTemplateDetails();
    			assertEquals(templateDetails.size(), detailDtoList.size());
    			
    			for (int jindex = 0; jindex < detailDtoList.size(); jindex++) {
    				TemplateDetailDto templateDetailDto = detailDtoList.get(jindex);
    				TemplateDetail templateDetail = templateDetails.get(jindex);
    				
    				assertEquals((long) templateDetail.getId(), (long) templateDetailDto.getId());
    				assertEquals(templateDetail.getApply(), templateDetailDto.getApply());
    				assertEquals((long) templateDetail.getGoalParam().getId(), (long) templateDetailDto.getParamId());
    				assertEquals(templateDetail.getGoalParam().getName(), templateDetailDto.getParamName());
    			}
    		} 
		}
    }

    @Test
    public void test_header() {
    	int numberOfDetails = 20;

    	TemplateHeader templateHeader = new TemplateHeader();
		Goal goal = new Goal();
		goal.setId(11L);
		goal.setName("Goal Name ");
		List<GoalParam> params = new ArrayList<>();
		GoalParam param = new GoalParam();
		param.setId((long)100);
		param.setApply("Y");
		param.setName("Goal Param ");
		params.add(param);
		goal.setParams(params);

		List<TemplateDetail> templateDetails = new ArrayList<>();
		for (int jindex = 0; jindex < numberOfDetails; jindex++) {
			TemplateDetail templateDetail = new TemplateDetail();
			GoalParam param2 = new GoalParam();
			param2.setId((long)jindex);
			param2.setApply("Y");
			param2.setName("Goal Param " + jindex);

			templateDetail.setId((long)jindex);
			templateDetail.setApply("Y");
    		templateDetail.setGoalParam(param2);
			templateDetails.add(templateDetail);
		}

		templateHeader.setId((long)21);
		templateHeader.setWeightage(21 * 2);
		templateHeader.setGoal(goal);
		templateHeader.setTemplateDetails(templateDetails);

    	TemplateHeaderDto templateHeaderDto = templateMapper.getTemplateHeaderDto(templateHeader);
        
    	assertEquals((long)templateHeader.getId(), (long)templateHeaderDto.getId());
    	assertEquals(templateHeader.getWeightage(), templateHeaderDto.getWeightage());
    	assertEquals((long)goal.getId(), (long)templateHeaderDto.getGoalId());
    	assertEquals(goal.getName(), templateHeaderDto.getGoalName());

    	List<TemplateDetailDto> detailDtoList = templateHeaderDto.getDetails();
    	assertEquals(templateDetails.size(), detailDtoList.size());

    	for (int jindex = 0; jindex < detailDtoList.size(); jindex++) {
    		TemplateDetailDto templateDetailDto = detailDtoList.get(jindex);
    		TemplateDetail templateDetail = templateDetails.get(jindex);
    		
    		assertEquals((long)templateDetail.getId(), (long)templateDetailDto.getId());
    		assertEquals(templateDetail.getApply(), templateDetailDto.getApply());
    		assertEquals((long)templateDetail.getGoalParam().getId(), (long)templateDetailDto.getParamId());
    		assertEquals(templateDetail.getGoalParam().getName(), templateDetailDto.getParamName());
    	}
    }

    @Test
    public void test_headerList() {
    	int numberOfHeaders = 20;
    	int numberOfDetails = 30;

    	List<TemplateHeader> templateHeaders = new ArrayList<>();
    	for (int index = 0; index < numberOfHeaders; index++) {
    		TemplateHeader templateHeader = new TemplateHeader();
    		Goal goal = new Goal();
    		goal.setId((long) index);
    		goal.setName("Goal Name " + index);
    		List<GoalParam> params = new ArrayList<>();
    		GoalParam param = new GoalParam();
    		param.setId((long) index);
    		param.setApply("Y");
    		param.setName("Goal Param " + index);
    		params.add(param);
    		goal.setParams(params);

    		List<TemplateDetail> templateDetails = new ArrayList<>();
    		for (int jindex = 0; jindex < numberOfDetails; jindex++) {
    			TemplateDetail templateDetail = new TemplateDetail();
    			
    			GoalParam param2 = new GoalParam();
    			param2.setId((long) jindex);
    			param2.setApply("Y");
    			param2.setName("Goal Param " + jindex);
    			
    			templateDetail.setId((long) jindex);
    			templateDetail.setApply("Y");
    			templateDetail.setGoalParam(param2);
    			templateDetails.add(templateDetail);
    		}

    		templateHeader.setId((long) index);
    		templateHeader.setWeightage(index * 2);
    		templateHeader.setGoal(goal);
    		templateHeader.setTemplateDetails(templateDetails);
    		templateHeaders.add(templateHeader);
    	}

    	List<TemplateHeaderDto> headerDtoList = templateMapper.getTemplateHeaderDtoList(templateHeaders);
    	assertEquals(templateHeaders.size(), headerDtoList.size());
    	for (int index = 0; index < headerDtoList.size(); index++) {
    		TemplateHeaderDto templateHeaderDto = headerDtoList.get(index);
    		TemplateHeader templateHeader = templateHeaders.get(index);
    		Goal goal = templateHeader.getGoal();
    			
    		assertEquals((long) templateHeader.getId(), (long) templateHeaderDto.getId());
    		assertEquals(templateHeader.getWeightage(), templateHeaderDto.getWeightage());
    		assertEquals((long) goal.getId(), (long) templateHeaderDto.getGoalId());
    		assertEquals(goal.getName(), templateHeaderDto.getGoalName());

    		List<TemplateDetailDto> detailDtoList = templateHeaderDto.getDetails();
    		List<TemplateDetail> templateDetails = templateHeader.getTemplateDetails();
    		assertEquals(templateDetails.size(), detailDtoList.size());

    		for (int jindex = 0; jindex < detailDtoList.size(); jindex++) {
    			TemplateDetailDto templateDetailDto = detailDtoList.get(jindex);
    			TemplateDetail templateDetail = templateDetails.get(jindex);

    			assertEquals((long) templateDetail.getId(), (long) templateDetailDto.getId());
    			assertEquals(templateDetail.getApply(), templateDetailDto.getApply());
    			assertEquals((long) templateDetail.getGoalParam().getId(), (long) templateDetailDto.getParamId());
    			assertEquals(templateDetail.getGoalParam().getName(), templateDetailDto.getParamName());
    		}
		}
    }

    @Test
    public void test_detail() {
    	TemplateDetail templateDetail = new TemplateDetail();
    	GoalParam param2 = new GoalParam();
    	param2.setId((long)33);
    	param2.setApply("Y");
    	param2.setName("Goal Param ");
    	
    	templateDetail.setId((long)29);
    	templateDetail.setApply("Y");
    	templateDetail.setGoalParam(param2);

    	TemplateDetailDto templateDetailDto = templateMapper.getTemplateDetailDto(templateDetail);
    	assertEquals((long)templateDetail.getId(), (long)templateDetailDto.getId());
    	assertEquals(templateDetail.getApply(), templateDetailDto.getApply());
    	assertEquals((long)templateDetail.getGoalParam().getId(), (long)templateDetailDto.getParamId());
    	assertEquals(templateDetail.getGoalParam().getName(), templateDetailDto.getParamName());
    }

    @Test
    public void test_detailList() {
    	int numberOfDetails = 30;

    	List<TemplateDetail> templateDetails = new ArrayList<>();
    	for (int jindex = 0; jindex < numberOfDetails; jindex++) {
    		TemplateDetail templateDetail = new TemplateDetail();
    		
    		GoalParam param2 = new GoalParam();
    		param2.setId((long) jindex);
    		param2.setApply("Y");
    		param2.setName("Goal Param " + jindex);
    		
    		templateDetail.setId((long) jindex);
    		templateDetail.setApply("Y");
    		templateDetail.setGoalParam(param2);
    		templateDetails.add(templateDetail);
    	}

    	List<TemplateDetailDto> detailDtoList = templateMapper.getTemplateDetailDtoList(templateDetails);
    	assertEquals(templateDetails.size(), detailDtoList.size());
    	for (int jindex = 0; jindex < detailDtoList.size(); jindex++) {
    		TemplateDetailDto templateDetailDto = detailDtoList.get(jindex);
    		TemplateDetail templateDetail = templateDetails.get(jindex);
    		
    		assertEquals((long) templateDetail.getId(), (long) templateDetailDto.getId());
    		assertEquals(templateDetail.getApply(), templateDetailDto.getApply());
    		assertEquals((long) templateDetail.getGoalParam().getId(), (long) templateDetailDto.getParamId());
    		assertEquals(templateDetail.getGoalParam().getName(), templateDetailDto.getParamName());
    	}
    }

}
