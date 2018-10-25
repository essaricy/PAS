package com.softvision.digital.pms.web.config;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import com.softvision.digital.pms.assess.entity.AssessDetail;
import com.softvision.digital.pms.assess.entity.AssessHeader;
import com.softvision.digital.pms.assess.model.AssessDetailDto;
import com.softvision.digital.pms.assess.model.AssessHeaderDto;
import com.softvision.digital.pms.assign.entity.CycleAssignment;
import com.softvision.digital.pms.assign.entity.PhaseAssignment;
import com.softvision.digital.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.digital.pms.assign.model.EmployeePhaseAssignmentDto;
import com.softvision.digital.pms.auth.model.User;
import com.softvision.digital.pms.employee.entity.Employee;
import com.softvision.digital.pms.employee.model.EmployeeDto;
import com.softvision.digital.pms.goal.entity.Goal;
import com.softvision.digital.pms.goal.entity.GoalParam;
import com.softvision.digital.pms.goal.model.GoalDto;
import com.softvision.digital.pms.goal.model.GoalParamDto;
import com.softvision.digital.pms.template.entity.Template;
import com.softvision.digital.pms.template.entity.TemplateDetail;
import com.softvision.digital.pms.template.entity.TemplateHeader;
import com.softvision.digital.pms.template.model.TemplateDetailDto;
import com.softvision.digital.pms.template.model.TemplateDto;
import com.softvision.digital.pms.template.model.TemplateHeaderDto;

@Component
public class MyModelMapper extends ModelMapper {

	@PostConstruct
	public void loadMappings() {
		getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(AccessLevel.PRIVATE).setMatchingStrategy(MatchingStrategies.STRICT);

		// Goal Mappings
		typeMap(Goal.class, GoalDto.class)
		.addMapping(Goal::getId, GoalDto::setId)
		.addMapping(Goal::getName, GoalDto::setName)
		.addMapping(Goal::getParams, GoalDto::setParams);

		typeMap(GoalParam.class, GoalParamDto.class)
        .addMapping(GoalParam::getId, GoalParamDto::setId)
        .addMapping(GoalParam::getName, GoalParamDto::setName)
        .addMapping(GoalParam::getApply, GoalParamDto::setApplicable);

		typeMap(GoalDto.class, Goal.class)
        .addMapping(GoalDto::getId, Goal::setId)
        .addMapping(GoalDto::getName, Goal::setName)
        .addMapping(GoalDto::getParams, Goal::setParams);

        typeMap(GoalParamDto.class, GoalParam.class)
        .addMapping(GoalParamDto::getId, GoalParam::setId)
        .addMapping(GoalParamDto::getName, GoalParam::setName)
        .addMapping(GoalParamDto::getApplicable, GoalParam::setApply);

        // Template Mappings
		typeMap(Template.class, TemplateDto.class)
		.addMapping(Template::getTemplateHeaders, TemplateDto::setHeaders)
		.addMapping(Template::getUpdatedBy, (dest, value) -> dest.getUpdatedBy().setEmployeeId(1));

		typeMap(TemplateHeader.class, TemplateHeaderDto.class)
		.addMapping(src -> src.getGoal().getId(), TemplateHeaderDto::setGoalId)
		.addMapping(src -> src.getGoal().getName(), TemplateHeaderDto::setGoalName)
		.addMapping(TemplateHeader::getTemplateDetails, TemplateHeaderDto::setDetails);

		typeMap(TemplateDetail.class, TemplateDetailDto.class)
		.addMapping(src -> src.getGoalParam().getId(), TemplateDetailDto::setParamId)
		.addMapping(src -> src.getGoalParam().getName(), TemplateDetailDto::setParamName);

		typeMap(TemplateDto.class, Template.class)
		.addMapping(TemplateDto::getHeaders, Template::setTemplateHeaders)
		.addMapping(src -> src.getUpdatedBy().getEmployeeId(), Template::setUpdatedBy)
		;

		typeMap(TemplateHeaderDto.class, TemplateHeader.class)
		.addMapping(TemplateHeaderDto::getGoalId, (dest, value) -> dest.getGoal().setId((Long) value))
		.addMapping(TemplateHeaderDto::getGoalName, (dest, value) -> dest.getGoal().setName((String) value))
		.addMapping(TemplateHeaderDto::getDetails, TemplateHeader::setTemplateDetails);

		typeMap(TemplateDetailDto.class, TemplateDetail.class)
		.addMapping(TemplateDetailDto::getParamId, (dest, value) -> dest.getGoalParam().setId((Long) value))
		.addMapping(TemplateDetailDto::getParamName, (dest, value) -> dest.getGoalParam().setName((String) value));

		typeMap(GoalDto.class, TemplateHeaderDto.class)
		.addMappings(m -> m.skip(TemplateHeaderDto::setId))
		.addMapping(GoalDto::getId, TemplateHeaderDto::setGoalId)
		.addMapping(GoalDto::getName, TemplateHeaderDto::setGoalName)
		.addMapping(GoalDto::getParams, TemplateHeaderDto::setDetails);

		typeMap(GoalParamDto.class, TemplateDetailDto.class)
		.addMappings(m -> m.skip(TemplateDetailDto::setId))
		.addMapping(GoalParamDto::getId, TemplateDetailDto::setParamId)
		.addMapping(GoalParamDto::getName, TemplateDetailDto::setParamName)
		.addMapping(GoalParamDto::getApplicable, TemplateDetailDto::setApply);

		// Assignment Mappings
		typeMap(PhaseAssignment.class, EmployeePhaseAssignmentDto.class)
        .addMapping(PhaseAssignment::getId, EmployeePhaseAssignmentDto::setAssignmentId)
        .addMapping(PhaseAssignment::getEmployeeId, (dest, value) -> dest.getAssignedTo().setEmployeeId(1))
        .addMapping(PhaseAssignment::getAssignedBy, (dest, value) -> dest.getAssignedBy().setEmployeeId(1))
        .addMapping(PhaseAssignment::getAssignedAt, EmployeePhaseAssignmentDto::setAssignedAt)
        .addMapping(PhaseAssignment::getStatus, EmployeePhaseAssignmentDto::setStatus);

		typeMap(PhaseAssignment.class, EmployeeAssignmentDto.class)
        .addMapping(PhaseAssignment::getId, EmployeeAssignmentDto::setAssignmentId)
        .addMapping(PhaseAssignment::getEmployeeId, (dest, value) -> dest.getAssignedTo().setEmployeeId(1))
        .addMapping(PhaseAssignment::getAssignedBy, (dest, value) -> dest.getAssignedBy().setEmployeeId(1))
        .addMapping(PhaseAssignment::getAssignedAt, EmployeeAssignmentDto::setAssignedAt)
        .addMapping(PhaseAssignment::getStatus, EmployeeAssignmentDto::setStatus);

		typeMap(CycleAssignment.class, EmployeeAssignmentDto.class)
        .addMapping(CycleAssignment::getId, EmployeeAssignmentDto::setAssignmentId)
        .addMapping(CycleAssignment::getEmployeeId, (dest, value) -> dest.getAssignedTo().setEmployeeId(1))
        .addMapping(CycleAssignment::getAssignedBy, (dest, value) -> dest.getAssignedBy().setEmployeeId(1))
        .addMapping(CycleAssignment::getAssignedAt, EmployeeAssignmentDto::setAssignedAt)
        .addMapping(CycleAssignment::getStatus, EmployeeAssignmentDto::setStatus);

		// Assessment Mappings
		typeMap(AssessHeader.class, AssessHeaderDto.class)
        .addMapping(AssessHeader::getId, AssessHeaderDto::setId)
        .addMapping(AssessHeader::getAssignId, AssessHeaderDto::setAssignId)
        .addMapping(AssessHeader::getAssessDate, AssessHeaderDto::setAssessDate)
        .addMapping(AssessHeader::getAssessedBy, AssessHeaderDto::setAssessedBy)
        .addMapping(AssessHeader::getStatus, AssessHeaderDto::setStatus)
        .addMapping(AssessHeader::getAssessDetails, AssessHeaderDto::setAssessDetails);

		typeMap(AssessHeaderDto.class, AssessHeader.class)
        .addMapping(AssessHeaderDto::getId, AssessHeader::setId)
        .addMapping(AssessHeaderDto::getAssignId, AssessHeader::setAssignId)
        .addMapping(AssessHeaderDto::getAssessDate, AssessHeader::setAssessDate)
        .addMapping(AssessHeaderDto::getAssessedBy, AssessHeader::setAssessedBy)
        .addMapping(AssessHeaderDto::getStatus, AssessHeader::setStatus)
        .addMapping(AssessHeaderDto::getAssessDetails, AssessHeader::setAssessDetails);

		typeMap(AssessDetail.class, AssessDetailDto.class)
        .addMapping(AssessDetail::getId, AssessDetailDto::setId)
        .addMapping(AssessDetail::getTemplateHeaderId, AssessDetailDto::setTemplateHeaderId)
        .addMapping(AssessDetail::getRating, AssessDetailDto::setRating)
        .addMapping(AssessDetail::getScore, AssessDetailDto::setScore)
        .addMapping(AssessDetail::getComments, AssessDetailDto::setComments);

		typeMap(AssessDetailDto.class, AssessDetail.class)
        .addMapping(AssessDetailDto::getId, AssessDetail::setId)
        .addMapping(AssessDetailDto::getTemplateHeaderId, AssessDetail::setTemplateHeaderId)
        .addMapping(AssessDetailDto::getRating, AssessDetail::setRating)
        .addMapping(AssessDetailDto::getScore, AssessDetail::setScore)
        .addMapping(AssessDetailDto::getComments, AssessDetail::setComments);

		typeMap(Employee.class, EmployeeDto.class);

		typeMap(Employee.class, User.class)
        .addMapping(Employee::getBand, User::setBand)
        .addMapping(Employee::getDesignation, User::setDesignation)
        .addMapping(Employee::getEmployeeId, User::setEmployeeId)
        .addMapping(Employee::getFirstName, User::setFirstName)
        .addMapping(Employee::getHiredOn, User::setJoinedDate)
        .addMapping(Employee::getLastName, User::setLastName)
        .addMapping(Employee::getLocation, User::setLocation)
        .addMapping(Employee::getLoginId, User::setUsername)
        ;	
	}

	@Override
    public <D> D map(Object source, Class<D> destinationType) {
        if (source == null) {
            return null;
        }
        return super.map(source, destinationType);
    }

}
