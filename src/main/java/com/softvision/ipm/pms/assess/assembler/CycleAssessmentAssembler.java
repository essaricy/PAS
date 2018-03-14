package com.softvision.ipm.pms.assess.assembler;

import java.util.ArrayList;
import java.util.List;

import com.softvision.ipm.pms.assess.entity.CycleAssessDetail;
import com.softvision.ipm.pms.assess.entity.CycleAssessHeader;
import com.softvision.ipm.pms.assess.model.CycleAssessDetailDto;
import com.softvision.ipm.pms.assess.model.CycleAssessHeaderDto;

public class CycleAssessmentAssembler {

	public static List<CycleAssessHeaderDto> getCycleAssessHeaderDtos(List<CycleAssessHeader> all) {
		List<CycleAssessHeaderDto> list = new ArrayList<>();
		if (all != null) {
			for (CycleAssessHeader object : all) {
				list.add(getCycleAssessHeaderDto(object));
			}
		}
		return list;
	}

	private static CycleAssessHeaderDto getCycleAssessHeaderDto(CycleAssessHeader object) {
		CycleAssessHeaderDto dto = null;
		if (object != null) {
			dto = new CycleAssessHeaderDto();
			dto.setAssessDate(object.getAssessDate());
			dto.setAssessedBy(object.getAssessedBy());
			dto.setAssignId(object.getAssignId());
			dto.setId(object.getId());
			dto.setCycleAssessDetails(getCycleAssessDetailDtos(object.getCycleAssessDetails()));
			dto.setStatus(object.getStatus());
		}
		return dto;
	}

	private static List<CycleAssessDetailDto> getCycleAssessDetailDtos(List<CycleAssessDetail> all) {
		List<CycleAssessDetailDto> list = new ArrayList<>();
		if (all != null) {
			for (CycleAssessDetail object : all) {
				list.add(getCycleAssessDetailDto(object));
			}
		}
		return list;
	}

	private static CycleAssessDetailDto getCycleAssessDetailDto(CycleAssessDetail object) {
		CycleAssessDetailDto dto = null;
		if (object != null) {
			dto = new CycleAssessDetailDto();
			dto.setComments(object.getComments());
			dto.setId(object.getId());
			dto.setRating(object.getRating());
			dto.setScore(object.getScore());
			dto.setTemplateHeaderId(object.getTemplateHeaderId());
		}
		return dto;
	}

	public static List<CycleAssessHeader> getCycleAssessHeaders(List<CycleAssessHeaderDto> list) {
		List<CycleAssessHeader> all = new ArrayList<>();
		if (list != null) {
			for (CycleAssessHeaderDto object : list) {
				all.add(getCycleAssessHeader(object));
			}
		}
		return all;
	}

	public static CycleAssessHeader getCycleAssessHeader(CycleAssessHeaderDto dto) {
		CycleAssessHeader object = null;
		if (dto != null) {
			object = new CycleAssessHeader();
			object.setAssessDate(dto.getAssessDate());
			object.setAssessedBy(dto.getAssessedBy());
			object.setAssignId(dto.getAssignId());
			object.setId(dto.getId());
			object.setCycleAssessDetails(getCycleAssessDetails(dto.getCycleAssessDetails()));
			object.setStatus(dto.getStatus());
		}
		return object;
	}

	private static List<CycleAssessDetail> getCycleAssessDetails(List<CycleAssessDetailDto> list) {
		List<CycleAssessDetail> all = new ArrayList<>();
		if (list != null) {
			for (CycleAssessDetailDto object : list) {
				all.add(getCycleAssessDetail(object));
			}
		}
		return all;
	}

	private static CycleAssessDetail getCycleAssessDetail(CycleAssessDetailDto dto) {
		CycleAssessDetail object = null;
		if (dto != null) {
			object = new CycleAssessDetail();
			object.setComments(dto.getComments());
			object.setId(dto.getId());
			object.setRating(dto.getRating());
			object.setScore(dto.getScore());
			object.setTemplateHeaderId(dto.getTemplateHeaderId());
		}
		return object;
	}

}
