package com.softvision.digital.pms.appraisal.repo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.softvision.digital.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.digital.pms.appraisal.entity.AppraisalCycle;

public class AppraisalSpecs {

	private AppraisalSpecs() {}

	public static Specification<AppraisalCycle> searchActive() {
		return ((Root<AppraisalCycle> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> 
			builder.equal(builder.upper(root.get("status")), AppraisalCycleStatus.ACTIVE.toString())
		);
	}

}
