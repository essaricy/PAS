package com.softvision.ipm.pms.template.repo;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.softvision.ipm.pms.common.repo.AbstractRepository;
import com.softvision.ipm.pms.template.entity.Template;
import com.softvision.ipm.pms.template.entity.TemplateDetail;
import com.softvision.ipm.pms.template.entity.TemplateHeader;

@Repository
public class TemplateRepository extends AbstractRepository {

	public void save(Template template) {
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
			int templateUpdate = jdbcTemplate.update("UPDATE template set name=?, updated_by=?, updated_at=? where id=?", template.getName(), template.getUpdatedBy(), template.getUpdatedAt(), templateId);
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
	}

}
