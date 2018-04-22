package com.softvision.ipm.pms.common.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public abstract class AbstractRepository {

	@Autowired protected JdbcTemplate jdbcTemplate;

	protected long getSequenceNextVal(String sequence) {
		return jdbcTemplate.queryForObject("select nextval('" + sequence + "')", Long.class);
	}

}
