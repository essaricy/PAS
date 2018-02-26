package com.softvision.ipm.pms.template.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softvision.ipm.pms.template.entity.Template;
import com.softvision.ipm.pms.template.model.TemplateHeaderDto;

public interface TemplateDataRepository extends CrudRepository<Template, Long> {

	List<Template> findAll();

	Template findById(Long id);

	/*@Query(value = "select template_header.id as id, goal_ca.id as goalId, goal_ca.name as goalName, template_header.weightage as weightage "
			+ "from template_header "
			+ "inner join template on template.id=template_header.template_id "
			+ "inner join goal_ca on goal_ca.id=template_header.ca_id "
			+ "where template.id=?1", nativeQuery = true)*/
	/*List<TemplateHeaderDto> findByTemplateId(@Param("templateId") Long templateId);*/
	
}
