package com.softvision.digital.pms.template.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softvision.digital.pms.goal.model.GoalDto;
import com.softvision.digital.pms.template.entity.Template;
import com.softvision.digital.pms.template.entity.TemplateDetail;
import com.softvision.digital.pms.template.entity.TemplateHeader;
import com.softvision.digital.pms.template.model.TemplateDetailDto;
import com.softvision.digital.pms.template.model.TemplateDto;
import com.softvision.digital.pms.template.model.TemplateHeaderDto;

@Component
public class TemplateMapper {

	@Autowired private ModelMapper mapper;

	public List<TemplateDto> getTemplateDtoList(List<Template> all) {
		return mapper.map(all, new TypeToken<List<TemplateDto>>() {}.getType());
	}

	public TemplateDto getTemplateDto(Template object) {
		return mapper.map(object, TemplateDto.class);
	}

	public List<TemplateHeaderDto> getTemplateHeaderDtoList(List<TemplateHeader> all) {
		return mapper.map(all, new TypeToken<List<TemplateHeaderDto>>() {}.getType());
	}

	public TemplateHeaderDto getTemplateHeaderDto(TemplateHeader object) {
		return mapper.map(object, TemplateHeaderDto.class);
	}

	public List<TemplateDetailDto> getTemplateDetailDtoList(List<TemplateDetail> all) {
		return mapper.map(all, new TypeToken<List<TemplateDetailDto>>() {}.getType());
	}

	public TemplateDetailDto getTemplateDetailDto(TemplateDetail object) {
		return mapper.map(object, TemplateDetailDto.class);
	}

	public List<Template> getTemplates(List<TemplateDto> all) {
		return mapper.map(all, new TypeToken<List<Template>>() {}.getType());
	}

	public Template getTemplate(TemplateDto dto) {
		return mapper.map(dto, Template.class);
	}

	public List<TemplateHeader> getTemplateHeaders(List<TemplateHeaderDto> all) {
		return mapper.map(all, new TypeToken<List<TemplateHeader>>() {}.getType());
	}

	public TemplateHeader getTemplateHeader(TemplateHeaderDto dto) {
		return mapper.map(dto, TemplateHeader.class);
	}

	public List<TemplateDetail> getTemplateDetails(List<TemplateDetailDto> all) {
		return mapper.map(all, new TypeToken<List<TemplateDetail>>() {}.getType());
	}

	public TemplateDetail getTemplateDetail(TemplateDetailDto dto) {
		return mapper.map(dto, TemplateDetail.class);
	}

	public TemplateDto getTemplateDto(List<GoalDto> goals) {
		if (goals != null) {
			TemplateDto templateDto = new TemplateDto();
			templateDto.setHeaders(mapper.map(goals, new TypeToken<List<TemplateHeaderDto>>() {}.getType()));
			return templateDto;
		}
		return null;
	}

}
