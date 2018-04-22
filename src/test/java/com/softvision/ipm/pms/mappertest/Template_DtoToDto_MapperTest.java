package com.softvision.ipm.pms.mappertest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
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

import com.softvision.ipm.pms.goal.model.GoalDto;
import com.softvision.ipm.pms.goal.model.GoalParamDto;
import com.softvision.ipm.pms.template.mapper.TemplateMapper;
import com.softvision.ipm.pms.template.model.TemplateDetailDto;
import com.softvision.ipm.pms.template.model.TemplateDto;
import com.softvision.ipm.pms.template.model.TemplateHeaderDto;
import com.softvision.ipm.pms.web.config.MyModelMapper;

public class Template_DtoToDto_MapperTest {

	@Spy ModelMapper mapper = new MyModelMapper();

	@InjectMocks private TemplateMapper templateMapper = new TemplateMapper();

	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Before
	public void prepare() {
		((MyModelMapper)mapper).loadMappings();
	}

    @Test
    public void test_goalsNull() {
        assertNull(templateMapper.getTemplateDto((List<GoalDto>) null));
    }

    @Test
    public void test_goalList() {
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

}
