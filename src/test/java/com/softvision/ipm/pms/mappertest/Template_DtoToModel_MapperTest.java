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

import com.softvision.ipm.pms.employee.model.EmployeeDto;
import com.softvision.ipm.pms.template.entity.Template;
import com.softvision.ipm.pms.template.entity.TemplateDetail;
import com.softvision.ipm.pms.template.entity.TemplateHeader;
import com.softvision.ipm.pms.template.mapper.TemplateMapper;
import com.softvision.ipm.pms.template.model.TemplateDetailDto;
import com.softvision.ipm.pms.template.model.TemplateDto;
import com.softvision.ipm.pms.template.model.TemplateHeaderDto;
import com.softvision.ipm.pms.web.config.MyModelMapper;

public class Template_DtoToModel_MapperTest {

	@Spy ModelMapper mapper = new MyModelMapper();

	@InjectMocks private TemplateMapper templateMapper;

	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Before
	public void prepare() {
		((MyModelMapper)mapper).loadMappings();
	}

    @Test
    public void test_templateNull() {
        assertNull(templateMapper.getTemplate((TemplateDto) null));
    }

    @Test
    public void test_headerNull() {
        assertNull(templateMapper.getTemplateHeader((TemplateHeaderDto) null));
    }

    @Test
    public void test_detailNull() {
        assertNull(templateMapper.getTemplateDetail((TemplateDetailDto) null));
    }

    @Test
    public void test_template() {
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
    public void test_templateList() {
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
    public void test_header() {
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
    public void test_headerList() {
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
    public void test_detail() {
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
    public void test_detailList() {
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

}
