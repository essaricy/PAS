package com.softvision.ipm.pms.template.assembler;

import java.util.ArrayList;
import java.util.List;

import com.softvision.ipm.pms.template.entity.Template;
import com.softvision.ipm.pms.template.entity.TemplateHeader;
import com.softvision.ipm.pms.template.model.TemplateDto;
import com.softvision.ipm.pms.template.model.TemplateHeaderDto;

public class TemplateAssembler {

	public static List<TemplateDto> getTemplates(List<Template> all) {
		List<TemplateDto> list = new ArrayList<>();
		for (Template object : all) {
			list.add(getTemplate(object));
		}
		return list;
	}

	public static TemplateDto getTemplate(Template object) {
		TemplateDto dto = null;
		if (object != null) {
			dto = new TemplateDto();
			dto.setId(object.getId());
			dto.setName(object.getName());
			dto.setUpdatedBy(object.getUpdatedBy());
			dto.setUpdatedAt(object.getUpdatedAt());
			dto.setHeaders(getHeadersList(object.getTemplateHeaders()));
		}
		return dto;
	}

	private static List<TemplateHeaderDto> getHeadersList(List<TemplateHeader> all) {
		List<TemplateHeaderDto> list = new ArrayList<>();
		for (TemplateHeader object : all) {
			list.add(getHeader(object));
		}
		return list;
	}

	private static TemplateHeaderDto getHeader(TemplateHeader object) {
		TemplateHeaderDto dto = null;
		if (object != null) {
			dto = new TemplateHeaderDto();
			dto.setId(object.getId());
			//dto.setGoalId(object.getName());
			//dto.setApplicable(object.getApply());
		}
		return dto;
	}

	public static Template getTemplate(TemplateDto dto) {
		Template object = null;
		if (dto != null) {
			object = new Template();
			object.setId(dto.getId());
			object.setName(dto.getName());
			//object.setHeaders(getHeaders(dto.getHeaders()));
		}
		return object;
	}

	private static List<TemplateHeader> getHeaders(List<TemplateHeaderDto> params) {
		List<TemplateHeader> all = new ArrayList<>();
		for (TemplateHeaderDto dto : params) {
			all.add(getHeader(dto));
		}
		return all;
	}

	private static TemplateHeader getHeader(TemplateHeaderDto dto) {
		TemplateHeader object = null;
		if (dto != null) {
			object = new TemplateHeader();
			object.setId(dto.getId());
			//object.setName(dto.getName());
			//object.setApply(dto.getApplicable());
		}
		return object;
	}

}
