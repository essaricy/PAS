package com.softvision.ipm.pms.assess.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.Data;

@Data
@Entity(name="cycle_assess_detail")
public class CycleAssessDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="cycle_assess_detail_id_seq",sequenceName="cycle_assess_detail_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="cycle_assess_detail_id_seq")
	private Long id;

	private long templateHeaderId;

	private double rating;

	private String comments;

	private double score;

	@Override
	public String toString() {
		return "CycleAssessDetail [id=" + id + ", templateHeaderId=" + templateHeaderId + ", rating=" + rating
				+ ", comments=" + comments + ", score=" + score + "]";
	}

}
