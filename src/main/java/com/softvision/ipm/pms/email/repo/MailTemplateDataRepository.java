package com.softvision.ipm.pms.email.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softvision.ipm.pms.email.entity.MailTemplate;

public interface MailTemplateDataRepository extends CrudRepository<MailTemplate, Integer>{

	MailTemplate findById(@Param("id") Integer id);
	
	MailTemplate findByName(@Param("name") String name);

}
