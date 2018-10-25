package com.softvision.digital.pms.assess.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.Data;

@Data
@Entity(name="assess_detail")
public class AssessDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="assess_detail_id_seq",sequenceName="assess_detail_id_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="assess_detail_id_seq")
	private Long id;

	private long templateHeaderId;

	private double rating;

	private String comments;

	private double score;

}
