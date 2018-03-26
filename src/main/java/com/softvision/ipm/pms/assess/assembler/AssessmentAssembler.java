package com.softvision.ipm.pms.assess.assembler;

import java.util.ArrayList;
import java.util.List;

import com.softvision.ipm.pms.assess.entity.AssessDetail;
import com.softvision.ipm.pms.assess.entity.AssessHeader;
import com.softvision.ipm.pms.assess.model.AssessDetailDto;
import com.softvision.ipm.pms.assess.model.AssessHeaderDto;

public class AssessmentAssembler {

	public static List<AssessHeaderDto> getAssessHeaderDtos(List<AssessHeader> all) {
		List<AssessHeaderDto> list = new ArrayList<>();
		if (all != null) {
			for (AssessHeader object : all) {
				list.add(getAssessHeaderDto(object));
			}
		}
		return list;
	}

	private static AssessHeaderDto getAssessHeaderDto(AssessHeader object) {
		AssessHeaderDto dto = null;
		if (object != null) {
			dto = new AssessHeaderDto();
			dto.setAssessDate(object.getAssessDate());
			dto.setAssessedBy(object.getAssessedBy());
			dto.setAssignId(object.getAssignId());
			dto.setId(object.getId());
			dto.setAssessDetails(getAssessDetailDtos(object.getAssessDetails()));
			dto.setStatus(object.getStatus());
		}
		return dto;
	}

	private static List<AssessDetailDto> getAssessDetailDtos(List<AssessDetail> all) {
		List<AssessDetailDto> list = new ArrayList<>();
		if (all != null) {
			for (AssessDetail object : all) {
				list.add(getAssessDetailDto(object));
			}
		}
		return list;
	}

	private static AssessDetailDto getAssessDetailDto(AssessDetail object) {
		AssessDetailDto dto = null;
		if (object != null) {
			dto = new AssessDetailDto();
			dto.setComments(object.getComments());
			dto.setId(object.getId());
			dto.setRating(object.getRating());
			dto.setScore(object.getScore());
			dto.setTemplateHeaderId(object.getTemplateHeaderId());
		}
		return dto;
	}

	public static List<AssessHeader> getAssessHeaders(List<AssessHeaderDto> list) {
		List<AssessHeader> all = new ArrayList<>();
		if (list != null) {
			for (AssessHeaderDto object : list) {
				all.add(getAssessHeader(object));
			}
		}
		return all;
	}

	public static AssessHeader getAssessHeader(AssessHeaderDto dto) {
		AssessHeader object = null;
		if (dto != null) {
			object = new AssessHeader();
			object.setAssessDate(dto.getAssessDate());
			object.setAssessedBy(dto.getAssessedBy());
			object.setAssignId(dto.getAssignId());
			object.setId(dto.getId());
			object.setAssessDetails(getAssessDetails(dto.getAssessDetails()));
			object.setStatus(dto.getStatus());
		}
		return object;
	}

	private static List<AssessDetail> getAssessDetails(List<AssessDetailDto> list) {
		List<AssessDetail> all = new ArrayList<>();
		if (list != null) {
			for (AssessDetailDto object : list) {
				all.add(getAssessDetail(object));
			}
		}
		return all;
	}

	private static AssessDetail getAssessDetail(AssessDetailDto dto) {
		AssessDetail object = null;
		if (dto != null) {
			object = new AssessDetail();
			object.setComments(dto.getComments());
			object.setId(dto.getId());
			object.setRating(dto.getRating());
			object.setScore(dto.getScore());
			object.setTemplateHeaderId(dto.getTemplateHeaderId());
		}
		return object;
	}

}
