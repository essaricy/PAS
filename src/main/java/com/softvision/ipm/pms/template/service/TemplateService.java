package com.softvision.ipm.pms.template.service;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.util.ExceptionUtil;
import com.softvision.ipm.pms.common.util.ValidationUtil;
import com.softvision.ipm.pms.goal.model.GoalDto;
import com.softvision.ipm.pms.goal.service.GoalService;
import com.softvision.ipm.pms.template.assembler.TemplateAssembler;
import com.softvision.ipm.pms.template.entity.Template;
import com.softvision.ipm.pms.template.entity.TemplateDetail;
import com.softvision.ipm.pms.template.entity.TemplateHeader;
import com.softvision.ipm.pms.template.model.TemplateDto;
import com.softvision.ipm.pms.template.model.TemplateHeaderDto;
import com.softvision.ipm.pms.template.repo.TemplateDataRepository;
import com.softvision.ipm.pms.template.repo.TemplateHeaderDataRepository;

@Service
public class TemplateService {

	@Autowired private TemplateDataRepository templateDataRepository;

	@Autowired private GoalService goalService;

	@Autowired private JdbcTemplate jdbcTemplate;

	public List<TemplateDto> getTemplates() {
		return TemplateAssembler.getTemplateDtoList(templateDataRepository.findAll());
	}

	public TemplateDto getTemplate(long id) {
		return TemplateAssembler.getTemplateDto(templateDataRepository.findById(id));
	}

	public TemplateDto getNewTemplate() {
		List<GoalDto> activeGoals = goalService.getActiveGoals();
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public TemplateDto update(TemplateDto templateDto) throws ServiceException {
		try {
			if (templateDto == null) {
				throw new ServiceException("Competency Assessment information is not provided.");
			}
			System.out.println("##### templateDto=" + templateDto);
			ValidationUtil.validate(templateDto);
			List<TemplateHeaderDto> headers = templateDto.getHeaders();
			int totalWeightage=0;
			for (TemplateHeaderDto header : headers) {
				totalWeightage += header.getWeightage();
			}
			if (totalWeightage != 100) {
				throw new ValidationException("Total Weightage should accumulate to 100%. Its now " + totalWeightage + "%");
			}
			Template template = TemplateAssembler.getTemplate(templateDto);
			System.out.println("##### template=" + template);
			//Template savedTemplate = templateDataRepository.save(template);
			Long templateId = template.getId();
			if (templateId == 0) {
				// Create
				templateId=getSequenceNextVal("template_id_seq");
				int templateInsert = jdbcTemplate.update("INSERT INTO template(id, name, updated_by, updated_at) VALUES(?,?, ?, ?)", templateId, template.getName(), template.getUpdatedBy(), template.getUpdatedAt());
				System.out.println("templateInsert=" + templateInsert);
			} else {
				// Update
				int templateUpdate = jdbcTemplate.update("UPDATE template set name=?, updated_by=?, updated_at=? where id=?)", template.getName(), template.getUpdatedBy(), template.getUpdatedAt(), templateId);
				System.out.println("templateUpdate=" + templateUpdate);
			}
			template.setId(templateId);

			List<TemplateHeader> templateHeaders = template.getTemplateHeaders();
			for (TemplateHeader header : templateHeaders) {
				Long headerId = header.getId();
				if (headerId == 0) {
					// Create
					headerId=getSequenceNextVal("template_header_id_seq");
					int headerInsert = jdbcTemplate.update("INSERT INTO template_header(id, template_id, ca_id, weightage) VALUES(?, ?, ?, ?)", headerId, templateId, header.getGoalCa().getId(), header.getWeightage());
					System.out.println("headerInsert=" + headerInsert);
				} else {
					// Update
					int headerUpdate = jdbcTemplate.update("UPDATE template_header set weightage=? where id=?", header.getWeightage(), headerId);
					System.out.println("headerUpdate=" + headerUpdate);
				}
				header.setId(headerId);

				List<TemplateDetail> templateDetails = header.getTemplateDetails();
				for (TemplateDetail detail : templateDetails) {
					Long detailId = detail.getId();
					if (detailId == 0) {
						// Create
						detailId=getSequenceNextVal("template_detail_id_seq");
						int detailInsert = jdbcTemplate.update("INSERT INTO template_detail(id, header_id, cap_id, apply) VALUES(?, ?, ?, ?)", detailId, headerId, detail.getGoalParam().getId(), detail.getApply());
						System.out.println("detailInsert=" + detailInsert);
					} else {
						// Update
						int detailUpdate = jdbcTemplate.update("UPDATE template_detail set apply=? where id=?", detail.getApply(), detailId);
						System.out.println("detailUpdate=" + detailUpdate);
					}
					detail.setId(detailId);
				}
			}
			templateDto=TemplateAssembler.getTemplateDto(template);
		} catch (Exception exception) {
			exception.printStackTrace();
			String message = ExceptionUtil.getExceptionMessage(exception);
			throw new ServiceException(message, exception);
		}
		return templateDto;
	}

	private long getSequenceNextVal(String sequence) {
		return jdbcTemplate.queryForObject("select nextval('" + sequence + "')", Long.class);
	}

	public void delete(Long id) throws ServiceException {
		try {
			templateDataRepository.delete(id);
		} catch (Exception exception) {
			String message = ExceptionUtil.getExceptionMessage(exception);
			throw new ServiceException(message, exception);
		}
	}

}
