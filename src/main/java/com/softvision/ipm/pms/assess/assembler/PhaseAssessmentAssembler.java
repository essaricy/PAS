package com.softvision.ipm.pms.assess.assembler;

import java.util.ArrayList;
import java.util.List;

import com.softvision.ipm.pms.assess.entity.PhaseAssessDetail;
import com.softvision.ipm.pms.assess.entity.PhaseAssessHeader;
import com.softvision.ipm.pms.assess.model.PhaseAssessDetailDto;
import com.softvision.ipm.pms.assess.model.PhaseAssessHeaderDto;

public class PhaseAssessmentAssembler {

	public static List<PhaseAssessHeaderDto> getPhaseAssessHeaderDtos(List<PhaseAssessHeader> all) {
		List<PhaseAssessHeaderDto> list = new ArrayList<>();
		if (all != null) {
			for (PhaseAssessHeader object : all) {
				list.add(getPhaseAssessHeaderDto(object));
			}
		}
		return list;
	}

	private static PhaseAssessHeaderDto getPhaseAssessHeaderDto(PhaseAssessHeader object) {
		PhaseAssessHeaderDto dto = null;
		if (object != null) {
			dto = new PhaseAssessHeaderDto();
			dto.setAssessDate(object.getAssessDate());
			dto.setAssessedBy(object.getAssessedBy());
			dto.setAssignId(object.getAssignId());
			dto.setId(object.getId());
			dto.setPhaseAssessDetails(getPhaseAssessDetailDtos(object.getPhaseAssessDetails()));
			dto.setStatus(object.getStatus());
		}
		return dto;
	}

	private static List<PhaseAssessDetailDto> getPhaseAssessDetailDtos(List<PhaseAssessDetail> all) {
		List<PhaseAssessDetailDto> list = new ArrayList<>();
		if (all != null) {
			for (PhaseAssessDetail object : all) {
				list.add(getPhaseAssessDetailDto(object));
			}
		}
		return list;
	}

	private static PhaseAssessDetailDto getPhaseAssessDetailDto(PhaseAssessDetail object) {
		PhaseAssessDetailDto dto = null;
		if (object != null) {
			dto = new PhaseAssessDetailDto();
			dto.setComments(object.getComments());
			dto.setId(object.getId());
			dto.setRating(object.getRating());
			dto.setScore(object.getScore());
			dto.setTemplateHeaderId(object.getTemplateHeaderId());
		}
		return dto;
	}

	public static List<PhaseAssessHeader> getPhaseAssessHeaders(List<PhaseAssessHeaderDto> list) {
		List<PhaseAssessHeader> all = new ArrayList<>();
		if (list != null) {
			for (PhaseAssessHeaderDto object : list) {
				all.add(getPhaseAssessHeader(object));
			}
		}
		return all;
	}

	public static PhaseAssessHeader getPhaseAssessHeader(PhaseAssessHeaderDto dto) {
		PhaseAssessHeader object = null;
		if (dto != null) {
			object = new PhaseAssessHeader();
			object.setAssessDate(dto.getAssessDate());
			object.setAssessedBy(dto.getAssessedBy());
			object.setAssignId(dto.getAssignId());
			object.setId(dto.getId());
			object.setPhaseAssessDetails(getPhaseAssessDetails(dto.getPhaseAssessDetails()));
			object.setStatus(dto.getStatus());
		}
		return object;
	}

	private static List<PhaseAssessDetail> getPhaseAssessDetails(List<PhaseAssessDetailDto> list) {
		List<PhaseAssessDetail> all = new ArrayList<>();
		if (list != null) {
			for (PhaseAssessDetailDto object : list) {
				all.add(getPhaseAssessDetail(object));
			}
		}
		return all;
	}

	private static PhaseAssessDetail getPhaseAssessDetail(PhaseAssessDetailDto dto) {
		PhaseAssessDetail object = null;
		if (dto != null) {
			object = new PhaseAssessDetail();
			object.setComments(dto.getComments());
			object.setId(dto.getId());
			object.setRating(dto.getRating());
			object.setScore(dto.getScore());
			object.setTemplateHeaderId(dto.getTemplateHeaderId());
		}
		return object;
	}

}
