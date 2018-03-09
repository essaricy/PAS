package com.softvision.ipm.pms.assess.assembler;

import java.util.ArrayList;
import java.util.List;

import com.softvision.ipm.pms.assess.entity.PhaseAssessDetail;
import com.softvision.ipm.pms.assess.entity.PhaseAssessHeader;
import com.softvision.ipm.pms.assess.model.PhaseAssessDetailDto;
import com.softvision.ipm.pms.assess.model.PhaseAssessHeaderDto;

public class AssessmentAssembler {

	public static List<PhaseAssessHeaderDto> getHeaderDtoList(List<PhaseAssessHeader> all) {
		List<PhaseAssessHeaderDto> list = new ArrayList<>();
		if (all != null) {
			for (PhaseAssessHeader object : all) {
				list.add(getHeaderDto(object));
			}
		}
		return list;
	}

	private static PhaseAssessHeaderDto getHeaderDto(PhaseAssessHeader object) {
		PhaseAssessHeaderDto dto = null;
		if (object != null) {
			dto = new PhaseAssessHeaderDto();
			dto.setAssessDate(object.getAssessDate());
			dto.setAssessedBy(object.getAssessedBy());
			dto.setAssignId(object.getAssignId());
			dto.setId(object.getId());
			dto.setPhaseAssessDetails(getDetailDtoList(object.getPhaseAssessDetails()));
			dto.setStatus(object.getStatus());
		}
		return dto;
	}

	private static List<PhaseAssessDetailDto> getDetailDtoList(List<PhaseAssessDetail> all) {
		List<PhaseAssessDetailDto> list = new ArrayList<>();
		if (all != null) {
			for (PhaseAssessDetail object : all) {
				list.add(getDetailDto(object));
			}
		}
		return list;
	}

	private static PhaseAssessDetailDto getDetailDto(PhaseAssessDetail object) {
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

	public static List<PhaseAssessHeader> getHeaderList(List<PhaseAssessHeaderDto> list) {
		List<PhaseAssessHeader> all = new ArrayList<>();
		if (list != null) {
			for (PhaseAssessHeaderDto object : list) {
				all.add(getHeader(object));
			}
		}
		return all;
	}

	public static PhaseAssessHeader getHeader(PhaseAssessHeaderDto dto) {
		PhaseAssessHeader object = null;
		if (dto != null) {
			object = new PhaseAssessHeader();
			object.setAssessDate(dto.getAssessDate());
			object.setAssessedBy(dto.getAssessedBy());
			object.setAssignId(dto.getAssignId());
			object.setId(dto.getId());
			object.setPhaseAssessDetails(getDetailList(dto.getPhaseAssessDetails()));
			object.setStatus(dto.getStatus());
		}
		return object;
	}

	private static List<PhaseAssessDetail> getDetailList(List<PhaseAssessDetailDto> list) {
		List<PhaseAssessDetail> all = new ArrayList<>();
		if (list != null) {
			for (PhaseAssessDetailDto object : list) {
				all.add(getDetail(object));
			}
		}
		return all;
	}

	private static PhaseAssessDetail getDetail(PhaseAssessDetailDto dto) {
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
