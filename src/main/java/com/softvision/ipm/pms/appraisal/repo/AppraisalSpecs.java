package com.softvision.ipm.pms.appraisal.repo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.softvision.ipm.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;

public class AppraisalSpecs {

	public static Specification<AppraisalCycle> searchActive() {
		return new Specification<AppraisalCycle>() {
			public Predicate toPredicate(Root<AppraisalCycle> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.equal(builder.upper(root.get("status")), AppraisalCycleStatus.ACTIVE.toString());
			}
		};
	}

}
