package com.softvision.ipm.pms.template.assembler;

import java.util.ArrayList;
import java.util.List;

import com.softvision.ipm.pms.goal.entity.Goal;
import com.softvision.ipm.pms.goal.entity.GoalParam;
import com.softvision.ipm.pms.template.entity.Template;
import com.softvision.ipm.pms.template.entity.TemplateDetail;
import com.softvision.ipm.pms.template.entity.TemplateHeader;
import com.softvision.ipm.pms.template.model.TemplateDetailDto;
import com.softvision.ipm.pms.template.model.TemplateDto;
import com.softvision.ipm.pms.template.model.TemplateHeaderDto;

public class TemplateAssembler {

	public static List<TemplateDto> getTemplateDtoList(List<Template> all) {
		List<TemplateDto> list = new ArrayList<>();
		for (Template object : all) {
			list.add(getTemplateDto(object));
		}
		return list;
	}

	public static TemplateDto getTemplateDto(Template object) {
		TemplateDto dto = null;
		if (object != null) {
			dto = new TemplateDto();
			dto.setId(object.getId());
			dto.setName(object.getName());
			dto.setUpdatedBy(object.getUpdatedBy());
			dto.setUpdatedAt(object.getUpdatedAt());
			dto.setHeaders(getTemplateHeaderDtoList(object.getTemplateHeaders()));
		}
		return dto;
	}

	private static List<TemplateHeaderDto> getTemplateHeaderDtoList(List<TemplateHeader> all) {
		List<TemplateHeaderDto> list = new ArrayList<>();
		for (TemplateHeader object : all) {
			list.add(getTemplateHeaderDto(object));
		}
		return list;
	}

	private static TemplateHeaderDto getTemplateHeaderDto(TemplateHeader object) {
		TemplateHeaderDto dto = null;
		if (object != null) {
			dto = new TemplateHeaderDto();
			dto.setId(object.getId());
			dto.setWeightage(object.getWeightage());

			Goal goalCa = object.getGoalCa();
			dto.setGoalId(goalCa.getId());
			dto.setGoalName(goalCa.getName());
			dto.setDetails(getTemplateDetailDtoList(object.getTemplateDetails()));
		}
		return dto;
	}

	private static List<TemplateDetailDto> getTemplateDetailDtoList(List<TemplateDetail> all) {
		List<TemplateDetailDto> list = new ArrayList<>();
		for (TemplateDetail object : all) {
			list.add(getTemplateDetailDto(object));
		}
		return list;
	}

	private static TemplateDetailDto getTemplateDetailDto(TemplateDetail object) {
		TemplateDetailDto dto = null;
		if (object != null) {
			dto = new TemplateDetailDto();
			dto.setId(object.getId());

			GoalParam goalParam = object.getGoalParam();
			dto.setParamId(goalParam.getId());
			dto.setParamName(goalParam.getName());
			dto.setApply(goalParam.getApply());
		}
		return dto;
	}

	public static Template getTemplate(TemplateDto dto) {
		Template object = null;
		if (dto != null) {
			object = new Template();
			object.setId(dto.getId());
			object.setName(dto.getName());
			object.setUpdatedBy(dto.getUpdatedBy());
			object.setUpdatedAt(dto.getUpdatedAt());
			object.setTemplateHeaders(getTemplateHeaders(dto.getHeaders()));
		}
		return object;
	}

	private static List<TemplateHeader> getTemplateHeaders(List<TemplateHeaderDto> headers) {
		List<TemplateHeader> all = new ArrayList<>();
		for (TemplateHeaderDto dto : headers) {
			all.add(getHeader(dto));
		}
		return all;
	}

	private static TemplateHeader getHeader(TemplateHeaderDto dto) {
		TemplateHeader object = null;
		if (dto != null) {
			object = new TemplateHeader();
			object.setId(dto.getId());
			object.setWeightage(dto.getWeightage());

			Goal goalCa = new Goal();
			goalCa.setId(dto.getId());
			goalCa.setName(dto.getGoalName());
			object.setGoalCa(goalCa);

			object.setTemplateDetails(getTemplateDetails(dto.getDetails()));
		}
		return object;
	}

	private static List<TemplateDetail> getTemplateDetails(List<TemplateDetailDto> details) {
		List<TemplateDetail> all = new ArrayList<>();
		for (TemplateDetailDto dto : details) {
			all.add(getDetail(dto));
		}
		return all;
	}

	private static TemplateDetail getDetail(TemplateDetailDto dto) {
		TemplateDetail object = null;
		if (dto != null) {
			object = new TemplateDetail();
			object.setId(dto.getId());

			GoalParam goalParam = new GoalParam();
			goalParam.setId(dto.getId());
			//goalParam.setName(name);
			//goalCa.setName(dto.getGoalName());
			//object.setGoalCa(goalCa);
			//object.setTemplateDetails(getTemplateDetails(dto.getDetails()));
		}
		return object;
	}

}
