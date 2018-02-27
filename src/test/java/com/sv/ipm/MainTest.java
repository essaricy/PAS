package com.sv.ipm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.softvision.ipm.pms.common.util.ValidationUtil;
import com.softvision.ipm.pms.template.model.TemplateDetailDto;
import com.softvision.ipm.pms.template.model.TemplateDto;
import com.softvision.ipm.pms.template.model.TemplateHeaderDto;

public class MainTest {

	public static void main(String[] args) {
		/*GoalDto goalDto = new GoalDto();
		goalDto.setId(10);
		goalDto.setName("name");
		List<GoalParamDto> params = new ArrayList<>();
		GoalParamDto param = new GoalParamDto();
		param.setId(10);
		param.setName("name");
		param.setApplicable("Y");
		params.add(param);
		goalDto.setParams(params);
		ValidationUtil.validate(goalDto);*/

		TemplateDto templateDto = new TemplateDto();
		templateDto.setId(10);
		templateDto.setName("Test Template - " + System.currentTimeMillis());
		templateDto.setUpdatedAt(new Date());
		templateDto.setUpdatedBy("srikanth.kumar");
		List<TemplateHeaderDto> headers = new ArrayList<>();
		TemplateHeaderDto header = new TemplateHeaderDto();
		header.setGoalId(1);
		header.setGoalName("test");
		header.setWeightage(1000);
		List<TemplateDetailDto> details = new ArrayList<>();
		TemplateDetailDto detail = new TemplateDetailDto();
		detail.setParamId(1);
		detail.setParamName("paramName");
		detail.setApply("Y");
		details.add(detail);
		header.setDetails(details);
		headers.add(header);
		templateDto.setHeaders(headers);

		ValidationUtil.validate(templateDto);
	}

}
