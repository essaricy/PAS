package com.softvision.ipm.pms.template.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.common.repo.AbstractRepository;
import com.softvision.ipm.pms.template.entity.Template;
import com.softvision.ipm.pms.template.entity.TemplateDetail;
import com.softvision.ipm.pms.template.entity.TemplateHeader;

@Repository
public class TemplateRepository extends AbstractRepository {

	@Transactional
	public void save(Template template) throws ServiceException {
		Long templateId = template.getId();
		if (templateId == 0) {
			// Create
			templateId=getSequenceNextVal("template_id_seq");
			int result = jdbcTemplate.update("INSERT INTO template(id, name, updated_by, updated_at) VALUES(?,?, ?, ?)", templateId, template.getName(), template.getUpdatedBy(), template.getUpdatedAt());
			checkInsert(result);
		} else {
			// Update
			int result = jdbcTemplate.update("UPDATE template set name=?, updated_by=?, updated_at=? where id=?", template.getName(), template.getUpdatedBy(), template.getUpdatedAt(), templateId);
			checkInsert(result);
		}
		template.setId(templateId);

		List<TemplateHeader> templateHeaders = template.getTemplateHeaders();
		for (TemplateHeader header : templateHeaders) {
			Long headerId = header.getId();
			if (headerId == 0) {
				// Create
				headerId=getSequenceNextVal("template_header_id_seq");
				int result = jdbcTemplate.update("INSERT INTO template_header(id, template_id, goal_id, weightage) VALUES(?, ?, ?, ?)", headerId, templateId, header.getGoal().getId(), header.getWeightage());
				checkInsert(result);
			} else {
				// Update
				int result = jdbcTemplate.update("UPDATE template_header set weightage=? where id=?", header.getWeightage(), headerId);
				checkInsert(result);
			}
			header.setId(headerId);

			List<TemplateDetail> templateDetails = header.getTemplateDetails();
			for (TemplateDetail detail : templateDetails) {
				Long detailId = detail.getId();
				if (detailId == 0) {
					// Create
					detailId=getSequenceNextVal("template_detail_id_seq");
					int result = jdbcTemplate.update("INSERT INTO template_detail(id, header_id, param_id, apply) VALUES(?, ?, ?, ?)", detailId, headerId, detail.getGoalParam().getId(), detail.getApply());
					checkInsert(result);
				} else {
					// Update
					int result = jdbcTemplate.update("UPDATE template_detail set apply=? where id=?", detail.getApply(), detailId);
					checkInsert(result);
				}
				detail.setId(detailId);
			}
		}
	}

	private void checkInsert(int templateInsert) throws ServiceException {
		if (templateInsert != -1) {
			throw new ServiceException("Updating template has failed");
		}
	}

}
