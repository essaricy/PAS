package com.softvision.digital.pms.common.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public abstract class AbstractRepository {

	@Autowired protected JdbcTemplate jdbcTemplate;

	protected long getNextTemplateId() {
		return jdbcTemplate.queryForObject("select nextval('template_id_seq')", Long.class);
	}

	protected long getNextTemplateHeaderId() {
		return jdbcTemplate.queryForObject("select nextval('template_header_id_seq')", Long.class);
	}

	protected long getNextTemplateDetailId() {
		return jdbcTemplate.queryForObject("select nextval('template_detail_id_seq')", Long.class);
	}

}
