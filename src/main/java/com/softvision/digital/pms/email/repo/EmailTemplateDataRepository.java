package com.softvision.digital.pms.email.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softvision.digital.pms.email.entity.EMailTemplate;

public interface EmailTemplateDataRepository extends CrudRepository<EMailTemplate, Integer> {

	EMailTemplate findByName(@Param("name") String name);

}
