package com.softvision.digital.pms.template.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softvision.digital.pms.template.entity.TemplateHeader;

public interface TemplateHeaderDataRepository extends CrudRepository<TemplateHeader, Long> {

	@Query(value="delete from template_header where template_id=:templateId", nativeQuery=true)
	void deleteByTemplateId(@Param("templateId") Long templateId);

}
