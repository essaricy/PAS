package com.softvision.digital.pms.template.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.modelmapper.ModelMapper;

import com.softvision.digital.pms.employee.model.EmployeeDto;
import com.softvision.digital.pms.goal.entity.Goal;
import com.softvision.digital.pms.goal.entity.GoalParam;
import com.softvision.digital.pms.goal.model.GoalDto;
import com.softvision.digital.pms.goal.model.GoalParamDto;
import com.softvision.digital.pms.template.entity.Template;
import com.softvision.digital.pms.template.entity.TemplateDetail;
import com.softvision.digital.pms.template.entity.TemplateHeader;
import com.softvision.digital.pms.template.mapper.TemplateMapper;
import com.softvision.digital.pms.template.model.TemplateDetailDto;
import com.softvision.digital.pms.template.model.TemplateDto;
import com.softvision.digital.pms.template.model.TemplateHeaderDto;
import com.softvision.digital.pms.web.config.MyModelMapper;

public class TemplateMapperTest {

	@Spy ModelMapper mapper = new MyModelMapper();

	@InjectMocks private TemplateMapper templateMapper;

	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Before
	public void prepare() {
		((MyModelMapper)mapper).loadMappings();
	}

    @Test
    public void shouldReturn_Null_When_GoalDtoList_isNull() {
        assertNull(templateMapper.getTemplateDto((List<GoalDto>) null));
    }

    @Test
    public void shouldReturn_TemplateDto_When_GoalDtoList_isValid() {
    	int numberOfGoals = 12;
    	int numberOfGoalParams = 120;

    	List<GoalDto> goals = new ArrayList<>();

    	String[] applicable = {"Y", "N"};
    	Random random = new Random();

    	for (int index = 0; index < numberOfGoals; index++) {
    		GoalDto goal = new GoalDto();
    		goal.setId(index);
    		goal.setName("Goal Name " + index);

    		List<GoalParamDto> params = new ArrayList<>();
    		for (int jindex = 0; jindex < numberOfGoalParams; jindex++) {
    			GoalParamDto param = new GoalParamDto();
    			param.setId(jindex);
    			param.setName("Goal Param Name " + jindex);
    			param.setApplicable(applicable[random.nextInt(applicable.length)]);
				params.add(param);
    		}
			goal.setParams(params);
			goals.add(goal);
		}

    	TemplateDto templateDto = templateMapper.getTemplateDto(goals);
    	assertNotNull(templateDto);
    	assertEquals(0, templateDto.getId());
    	assertNull(templateDto.getName());
    	assertNull(templateDto.getUpdatedAt());
    	assertNull(templateDto.getUpdatedBy());
    	List<TemplateHeaderDto> headers = templateDto.getHeaders();
    	assertNotNull(headers);
    	assertEquals(goals.size(), headers.size());

    	for (int index = 0; index < headers.size(); index++) {
    		TemplateHeaderDto headerDto = headers.get(index);
    		GoalDto goalDto = goals.get(index);

    		assertEquals(0, headerDto.getId());
    		assertEquals(0, headerDto.getWeightage());
    		assertEquals(goalDto.getId(), headerDto.getGoalId());
    		assertEquals(goalDto.getName(), headerDto.getGoalName());
    		
    		List<TemplateDetailDto> details = headerDto.getDetails();
    		List<GoalParamDto> params = goalDto.getParams();
    		assertEquals(params.size(), details.size());
    		
    		for (int jindex = 0; jindex < details.size(); jindex++) {
    			TemplateDetailDto templateDetailDto = details.get(jindex);
    			GoalParamDto goalParamDto = params.get(jindex);

    			assertEquals(0, templateDetailDto.getId());
    			assertEquals(goalParamDto.getId(), templateDetailDto.getParamId());
    			assertEquals(goalParamDto.getName(), templateDetailDto.getParamName());
        		assertEquals(goalParamDto.getApplicable(), templateDetailDto.getApply());
			}
		}
    }

    @Test
    public void shouldReturn_Null_When_TemplateDto_isNull() {
        assertNull(templateMapper.getTemplate((TemplateDto) null));
    }

    @Test
    public void shouldReturn_Null_When_TemplateHeaderDto_isNull() {
        assertNull(templateMapper.getTemplateHeader((TemplateHeaderDto) null));
    }

    @Test
    public void shouldReturn_Null_When_TemplateDetailDto_isNull() {
        assertNull(templateMapper.getTemplateDetail((TemplateDetailDto) null));
    }

    @Test
    public void shouldReturn_Template_When_TemplateDto_isValid() {
    	TemplateDto templateDto = new TemplateDto();
    	templateDto.setId(11L);
    	templateDto.setName("Template Name");
    	templateDto.setUpdatedAt(new Date());
    	EmployeeDto updatedBy = new EmployeeDto();
    	updatedBy.setEmployeeId(1136);
		templateDto.setUpdatedBy(updatedBy);
    	
    	int numberOfHeaders = 10;
    	int numberOfDetails = 20;
    	List<TemplateHeaderDto> templateHeaderDtoList = new ArrayList<>();
    	for (int index = 0; index < numberOfHeaders; index++) {
    		TemplateHeaderDto templateHeaderDto = new TemplateHeaderDto();

			List<TemplateDetailDto> templateDetailDtoList = new ArrayList<>();
			for (int jindex = 0; jindex < numberOfDetails; jindex++) {
				TemplateDetailDto templateDetailDto = new TemplateDetailDto();

				templateDetailDto.setId((long)jindex);
				templateDetailDto.setApply("Y");
	    		templateDetailDto.setParamId(jindex+1);
	    		templateDetailDto.setParamName("Param Name " + jindex * 2);
				templateDetailDtoList.add(templateDetailDto);
			}
			templateHeaderDto.setId((long)index);
			templateHeaderDto.setWeightage(index * 2);
			templateHeaderDto.setGoalId(index*2);
			templateHeaderDto.setGoalName("Goal Name " + index+1);
			templateHeaderDto.setDetails(templateDetailDtoList);
    		templateHeaderDtoList.add(templateHeaderDto);
		}
		templateDto.setHeaders(templateHeaderDtoList);

    	Template template = templateMapper.getTemplate(templateDto);
        assertNotNull(template);
        assertEquals((Long)templateDto.getId(), (Long)template.getId());
        assertEquals(templateDto.getName(), template.getName());
        assertEquals(templateDto.getUpdatedBy().getEmployeeId(), template.getUpdatedBy());
        assertEquals(templateDto.getUpdatedAt(), template.getUpdatedAt());

        List<TemplateHeaderDto> templateHeaderDtoList1 = templateDto.getHeaders();
        List<TemplateHeader> headers = template.getTemplateHeaders();
        assertEquals(templateHeaderDtoList1.size(), headers.size());
        for (int index = 0; index < headers.size(); index++) {
        	TemplateHeader templateHeader = headers.get(index);
        	TemplateHeaderDto templateHeaderDto = templateHeaderDtoList1.get(index);
        	
        	assertEquals((long)templateHeaderDto.getId(), (long)templateHeader.getId());
        	assertEquals(templateHeaderDto.getWeightage(), templateHeader.getWeightage());
        	assertEquals((long)templateHeaderDto.getGoalId(), (long)templateHeader.getGoal().getId());
        	assertEquals(templateHeaderDto.getGoalName(), templateHeader.getGoal().getName());

        	List<TemplateDetail> details = templateHeader.getTemplateDetails();
        	List<TemplateDetailDto> templateDetailDtoList = templateHeaderDto.getDetails();
        	assertEquals(templateDetailDtoList.size(), details.size());

        	for (int jindex = 0; jindex < details.size(); jindex++) {
        		TemplateDetail templateDetail = details.get(jindex);
        		TemplateDetailDto templateDetailDto = templateDetailDtoList.get(jindex);
        		
        		assertEquals((long)templateDetailDto.getId(), (long)templateDetail.getId());
        		assertEquals(templateDetailDto.getApply(), templateDetail.getApply());
        		assertEquals((long)templateDetailDto.getParamId(), (long)templateDetail.getGoalParam().getId());
        		assertEquals(templateDetailDto.getParamName(), templateDetail.getGoalParam().getName());
			}
		}
    }

    @Test
    public void shouldReturn_TemplateList_When_TemplateDtoList_isValid() {
    	int numberOfTemplates=10;
    	int numberOfHeaders = 20;
    	int numberOfDetails = 30;

    	List<TemplateDto> templateDtoList = new ArrayList<>();
    	for (int templateIndex = 0; templateIndex < numberOfTemplates; templateIndex++) {
        	TemplateDto templateDto = new TemplateDto();
        	templateDto.setId(11L);
        	templateDto.setName("Template Name " + templateIndex);
        	templateDto.setUpdatedAt(new Date());
        	EmployeeDto updatedBy = new EmployeeDto();
        	updatedBy.setEmployeeId(1136);
    		templateDto.setUpdatedBy(updatedBy);
        	
        	List<TemplateHeaderDto> templateHeaderDtoList = new ArrayList<>();
        	for (int index = 0; index < numberOfHeaders; index++) {
        		TemplateHeaderDto templateHeaderDto = new TemplateHeaderDto();

    			List<TemplateDetailDto> templateDetailDtoList = new ArrayList<>();
    			for (int jindex = 0; jindex < numberOfDetails; jindex++) {
    				TemplateDetailDto templateDetailDto = new TemplateDetailDto();

    				templateDetailDto.setId((long)jindex);
    				templateDetailDto.setApply("Y");
    	    		templateDetailDto.setParamId(jindex+1);
    	    		templateDetailDto.setParamName("Param Name " + jindex * 2);
    				templateDetailDtoList.add(templateDetailDto);
    			}
    			templateHeaderDto.setId((long)index);
    			templateHeaderDto.setWeightage(index * 2);
    			templateHeaderDto.setGoalId(index*2);
    			templateHeaderDto.setGoalName("Goal Name " + index+1);
    			templateHeaderDto.setDetails(templateDetailDtoList);
        		templateHeaderDtoList.add(templateHeaderDto);
    		}
    		templateDto.setHeaders(templateHeaderDtoList);
			templateDtoList.add(templateDto);
		}

    	List<Template> templates = templateMapper.getTemplates(templateDtoList);
    	assertEquals(templateDtoList.size(), templates.size());

    	for (int templateIndex = 0; templateIndex < templates.size(); templateIndex++) {
    		Template template = templates.get(templateIndex);
    		TemplateDto templateDto = templateDtoList.get(templateIndex);

            assertNotNull(template);
            assertEquals((Long)template.getId(), (Long)templateDto.getId());
            assertEquals(template.getName(), templateDto.getName());
            assertEquals(template.getUpdatedBy(), templateDto.getUpdatedBy().getEmployeeId());
            assertEquals(template.getUpdatedAt(), templateDto.getUpdatedAt());

            List<TemplateHeader> headers = template.getTemplateHeaders();
            List<TemplateHeaderDto> templateHeaderDtoList1 = templateDto.getHeaders();

            assertEquals(templateHeaderDtoList1.size(), headers.size());
            for (int index = 0; index < headers.size(); index++) {
            	TemplateHeader templateHeader = headers.get(index);
            	TemplateHeaderDto templateHeaderDto = templateHeaderDtoList1.get(index);
            	
            	assertEquals((long)templateHeaderDto.getId(), (long)templateHeader.getId());
            	assertEquals(templateHeaderDto.getWeightage(), templateHeader.getWeightage());
            	assertEquals((long)templateHeaderDto.getGoalId(), (long)templateHeader.getGoal().getId());
            	assertEquals(templateHeaderDto.getGoalName(), templateHeader.getGoal().getName());

            	List<TemplateDetail> details = templateHeader.getTemplateDetails();
            	List<TemplateDetailDto> templateDetailDtoList = templateHeaderDto.getDetails();
            	assertEquals(templateDetailDtoList.size(), details.size());

            	for (int jindex = 0; jindex < details.size(); jindex++) {
            		TemplateDetail templateDetail = details.get(jindex);
            		TemplateDetailDto templateDetailDto = templateDetailDtoList.get(jindex);
            		
            		assertEquals((long)templateDetailDto.getId(), (long)templateDetail.getId());
            		assertEquals(templateDetailDto.getApply(), templateDetail.getApply());
            		assertEquals((long)templateDetailDto.getParamId(), (long)templateDetail.getGoalParam().getId());
            		assertEquals(templateDetailDto.getParamName(), templateDetail.getGoalParam().getName());
    			}
    		}
		}
    }

    @Test
    public void shouldReturn_TemplateHeader_When_TemplateHeaderDto_isValid() {
    	int numberOfDetails = 20;

    	TemplateHeaderDto templateHeaderDto = new TemplateHeaderDto();
		List<TemplateDetailDto> templateDetailDtoList = new ArrayList<>();
		for (int jindex = 0; jindex < numberOfDetails; jindex++) {
			TemplateDetailDto templateDetailDto = new TemplateDetailDto();
			templateDetailDto.setId((long)jindex);
			templateDetailDto.setApply("Y");
    		templateDetailDto.setParamId(jindex+1);
    		templateDetailDto.setParamName("Param Name " + jindex * 2);
			templateDetailDtoList.add(templateDetailDto);
		}
		templateHeaderDto.setId((long)49);
		templateHeaderDto.setWeightage(42 * 2);
		templateHeaderDto.setGoalId(21*3);
		templateHeaderDto.setGoalName("Goal Name " + (11*2));
		templateHeaderDto.setDetails(templateDetailDtoList);

    	TemplateHeader templateHeader = templateMapper.getTemplateHeader(templateHeaderDto);
        
    	assertEquals((long)templateHeaderDto.getId(), (long)templateHeader.getId());
    	assertEquals(templateHeaderDto.getWeightage(), templateHeader.getWeightage());
    	assertEquals((long)templateHeaderDto.getGoalId(), (long)templateHeader.getGoal().getId());
    	assertEquals(templateHeaderDto.getGoalName(), templateHeader.getGoal().getName());

    	List<TemplateDetail> templateDetails = templateHeader.getTemplateDetails();
    	assertEquals(templateDetailDtoList.size(), templateDetails.size());

    	for (int jindex = 0; jindex < templateDetails.size(); jindex++) {
    		TemplateDetailDto templateDetailDto = templateDetailDtoList.get(jindex);
    		TemplateDetail templateDetail = templateDetails.get(jindex);
    		
    		assertEquals((long)templateDetail.getId(), (long)templateDetailDto.getId());
    		assertEquals(templateDetail.getApply(), templateDetailDto.getApply());
    		assertEquals((long)templateDetail.getGoalParam().getId(), (long)templateDetailDto.getParamId());
    		assertEquals(templateDetail.getGoalParam().getName(), templateDetailDto.getParamName());
    	}
    }

    @Test
    public void shouldReturn_TemplateHeaderList_When_TemplateHeaderDtoList_isValid() {
    	int numberOfHeaders = 20;
    	int numberOfDetails = 30;

    	List<TemplateHeaderDto> templateHeaderDtoList = new ArrayList<>();
    	for (int index = 0; index < numberOfHeaders; index++) {
    		TemplateHeaderDto templateHeaderDto = new TemplateHeaderDto();
    		List<TemplateDetailDto> templateDetailDtoList = new ArrayList<>();
    		for (int jindex = 0; jindex < numberOfDetails; jindex++) {
    			TemplateDetailDto templateDetailDto = new TemplateDetailDto();
    			templateDetailDto.setId((long)jindex);
    			templateDetailDto.setApply("Y");
        		templateDetailDto.setParamId(jindex+1);
        		templateDetailDto.setParamName("Param Name " + jindex * 2);
    			templateDetailDtoList.add(templateDetailDto);
    		}
    		templateHeaderDto.setId((long)index);
    		templateHeaderDto.setWeightage(index * 2);
    		templateHeaderDto.setGoalId(21*3);
    		templateHeaderDto.setGoalName("Goal Name " + (index*2));
    		templateHeaderDto.setDetails(templateDetailDtoList);
    		templateHeaderDtoList.add(templateHeaderDto);
    	}

    	List<TemplateHeader> headers = templateMapper.getTemplateHeaders(templateHeaderDtoList);
    	assertEquals(templateHeaderDtoList.size(), headers.size());
    	for (int index = 0; index < headers.size(); index++) {
    		TemplateHeader templateHeader = headers.get(index);
    		TemplateHeaderDto templateHeaderDto = templateHeaderDtoList.get(index);
    			
    		assertEquals((long) templateHeaderDto.getId(), (long) templateHeader.getId());
    		assertEquals(templateHeaderDto.getWeightage(), templateHeader.getWeightage());
    		assertEquals((long) templateHeaderDto.getGoalId(), (long) templateHeader.getGoal().getId());
    		assertEquals(templateHeaderDto.getGoalName(), templateHeader.getGoal().getName());

    		List<TemplateDetail> details = templateHeader.getTemplateDetails();
    		List<TemplateDetailDto> templateDetailDtoList = templateHeaderDto.getDetails();
    		assertEquals(templateDetailDtoList.size(), details.size());

    		for (int jindex = 0; jindex < details.size(); jindex++) {
    			TemplateDetail templateDetail = details.get(jindex);
    			TemplateDetailDto templateDetailDto = templateDetailDtoList.get(jindex);

    			assertEquals((long) templateDetailDto.getId(), (long) templateDetail.getId());
    			assertEquals(templateDetailDto.getApply(), templateDetail.getApply());
    			assertEquals((long) templateDetailDto.getParamId(), (long) templateDetail.getGoalParam().getId());
    			assertEquals(templateDetailDto.getParamName(), templateDetail.getGoalParam().getName());
    		}
		}
    }

    @Test
    public void shouldReturn_TemplateDetail_When_TemplateDetailDto_isValid() {
    	TemplateDetailDto templateDetailDto = new TemplateDetailDto();
		templateDetailDto.setId((long)12);
		templateDetailDto.setApply("Y");
		templateDetailDto.setParamId(121);
		templateDetailDto.setParamName("Param Name ");

    	TemplateDetail templateDetail = templateMapper.getTemplateDetail(templateDetailDto);
    	assertEquals((long) templateDetailDto.getId(), (long) templateDetail.getId());
		assertEquals(templateDetailDto.getApply(), templateDetail.getApply());
		assertEquals((long) templateDetailDto.getParamId(), (long) templateDetail.getGoalParam().getId());
		assertEquals(templateDetailDto.getParamName(), templateDetail.getGoalParam().getName());
    }

    @Test
    public void shouldReturn_TemplateDetailList_When_TemplateDetailDtoList_isValid() {
    	int numberOfDetails = 30;

    	List<TemplateDetailDto> templateDetailDtoList = new ArrayList<>();
    	for (int jindex = 0; jindex < numberOfDetails; jindex++) {
    		TemplateDetailDto templateDetailDto = new TemplateDetailDto();
			templateDetailDto.setId((long)jindex);
			templateDetailDto.setApply("Y");
    		templateDetailDto.setParamId(jindex+1);
    		templateDetailDto.setParamName("Param Name " + jindex * 2);
			templateDetailDtoList.add(templateDetailDto);
    	}

    	List<TemplateDetail> details = templateMapper.getTemplateDetails(templateDetailDtoList);
    	assertEquals(templateDetailDtoList.size(), details.size());
    	for (int jindex = 0; jindex < details.size(); jindex++) {
    		TemplateDetailDto templateDetailDto = templateDetailDtoList.get(jindex);
    		TemplateDetail templateDetail = details.get(jindex);
    		
    		assertEquals((long) templateDetail.getId(), (long) templateDetailDto.getId());
    		assertEquals(templateDetail.getApply(), templateDetailDto.getApply());
    		assertEquals((long) templateDetail.getGoalParam().getId(), (long) templateDetailDto.getParamId());
    		assertEquals(templateDetail.getGoalParam().getName(), templateDetailDto.getParamName());
    	}
    }

    @Test
    public void shouldReturn_Null_When_Template_isNull() {
        assertNull(templateMapper.getTemplateDto((Template) null));
    }

    @Test
    public void shouldReturn_Null_When_TemplateHeader_isNull() {
        assertNull(templateMapper.getTemplateHeaderDto((TemplateHeader) null));
    }

    @Test
    public void shouldReturn_Null_When_TemplateDetail_isNull() {
        assertNull(templateMapper.getTemplateDetailDto((TemplateDetail) null));
    }

    @Test
    public void shouldReturn_TemplateDto_When_Template_isValid() {
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
    public void shouldReturn_TemplateDtoList_When_TemplateList_isValid() {
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
    public void shouldReturn_TemplateHeaderDto_When_TemplateHeader_isValid() {
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
    public void shouldReturn_TemplateHeaderDtoList_When_TemplateHeaderList_isValid() {
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
    public void shouldReturn_TemplateDetailDto_When_TemplateDetail_isValid() {
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
    public void shouldReturn_TemplateDetailDtoList_When_TemplateDetailList_isValid() {
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
