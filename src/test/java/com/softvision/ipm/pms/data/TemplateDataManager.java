package com.softvision.ipm.pms.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.employee.model.EmployeeDto;
import com.softvision.ipm.pms.goal.model.GoalDto;
import com.softvision.ipm.pms.goal.model.GoalParamDto;
import com.softvision.ipm.pms.goal.service.GoalService;
import com.softvision.ipm.pms.template.model.TemplateDetailDto;
import com.softvision.ipm.pms.template.model.TemplateDto;
import com.softvision.ipm.pms.template.model.TemplateHeaderDto;
import com.softvision.ipm.pms.template.service.TemplateService;

@Component
public class TemplateDataManager implements AbstractDataManager {

	@Autowired TemplateService templateService;

	@Autowired GoalService goalService;

	@Override
	public void clearData() throws Exception {
		List<TemplateDto> templates = templateService.getTemplates();
		if (templates != null && !templates.isEmpty()) {
			for (TemplateDto template : templates) {
				templateService.delete(template.getId());
			}
		}
	}

	@Override
	public void loadData() throws Exception {
		createTemplates(25);
	}

	private void createTemplates(int numberOfTemplates) throws ServiceException {
		List<GoalDto> goals = goalService.getGoals();
		for (int templateIndex = 0; templateIndex < numberOfTemplates; templateIndex++) {
			TemplateDto templateDto = new TemplateDto();
			templateDto.setId(0);
			templateDto.setName("Template " + (templateIndex+1));
			EmployeeDto updatedBy = new EmployeeDto();
			updatedBy.setEmployeeId(1136); // TODO Randomize
			templateDto.setUpdatedBy(updatedBy);
			templateDto.setUpdatedAt(new Date());

			List<TemplateHeaderDto> headers = new ArrayList<>();

			int numberOfGoals=RANDOM.nextInt(goals.size());
			if (numberOfGoals == 0) {
				templateIndex--;
				continue;
			}
			List<Long> usedGoals = new ArrayList<>();
			System.out.println("numberOfGoals=" + numberOfGoals);
			int totalWeightage=0;
			for (int index = 0; index < numberOfGoals; index++) {

				GoalDto randomGoal = goals.get(RANDOM.nextInt(goals.size()));
				long goalId = randomGoal.getId();
				if (usedGoals.contains(goalId)) {
					index--;
					continue;
				}

				TemplateHeaderDto header = new TemplateHeaderDto();
				System.out.println("goalId to use: " + goalId);
				System.out.println("randomGoal.getName(): " + randomGoal.getName());
				usedGoals.add(goalId);

				header.setGoalId(goalId);
				header.setGoalName(randomGoal.getName());

				totalWeightage+=(100/numberOfGoals);
				if ((index+1) == numberOfGoals) {
					header.setWeightage((100/numberOfGoals) + (100-totalWeightage));
				} else {
					header.setWeightage(100/numberOfGoals);
				}

				List<TemplateDetailDto> details = new ArrayList<>();
				List<GoalParamDto> params = randomGoal.getParams();
				for (GoalParamDto param : params) {
					TemplateDetailDto detail = new TemplateDetailDto();
					detail.setApply("Y");
					detail.setParamId(param.getId());
					detail.setParamName(param.getName());
					details.add(detail);
				}
				header.setDetails(details);
				headers.add(header);
			}
			templateDto.setHeaders(headers);
			System.out.println(templateDto);
			templateService.update(templateDto);
		}
	}

}
