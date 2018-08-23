package com.softvision.ipm.pms.email.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softvision.ipm.pms.email.entity.EMailTemplate;

public interface EmailTemplateDataRepository extends CrudRepository<EMailTemplate, Integer> {

	//EMailTemplate findById(@Param("id") Integer id);
	
	EMailTemplate findByName(@Param("name") String name);

}
