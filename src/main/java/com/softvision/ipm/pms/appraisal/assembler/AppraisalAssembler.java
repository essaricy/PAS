package com.softvision.ipm.pms.appraisal.assembler;

import java.util.ArrayList;
import java.util.List;

import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.appraisal.model.AppraisalCycleDto;
import com.softvision.ipm.pms.appraisal.model.AppraisalPhaseDto;
import com.softvision.ipm.pms.constant.AppraisalCycleStatus;

public class AppraisalAssembler {

	public static List<AppraisalCycleDto> getCycles(List<AppraisalCycle> all) {
		List<AppraisalCycleDto> list = new ArrayList<>();
		for (AppraisalCycle object : all) {
			list.add(getCycle(object));
		}
		return list;
	}

	public static AppraisalCycleDto getCycle(AppraisalCycle object) {
		AppraisalCycleDto dto = null;
		if (object != null) {
			dto = new AppraisalCycleDto();
			dto.setId(object.getId());
			dto.setName(object.getName());
			dto.setStartDate(object.getStartDate());
			dto.setEndDate(object.getEndDate());
			dto.setCutoffDate(object.getCutoffDate());
			dto.setStatus(AppraisalCycleStatus.get(object.getStatus()));
			dto.setPhases(getPhasesList(object.getPhases()));
		}
		return dto;
	}

	public static List<AppraisalPhaseDto> getPhasesList(List<AppraisalPhase> all) {
		List<AppraisalPhaseDto> list = new ArrayList<>();
		for (AppraisalPhase object : all) {
			list.add(getPhase(object));
		}
		return list;
	}

	public static AppraisalPhaseDto getPhase(AppraisalPhase object) {
		AppraisalPhaseDto dto = null;
		if (object != null) {
			dto = new AppraisalPhaseDto();
			dto.setId(object.getId());
			dto.setName(object.getName());
			dto.setStartDate(object.getStartDate());
			dto.setEndDate(object.getEndDate());
		}
		return dto;
	}

	public static AppraisalCycle getCycle(AppraisalCycleDto dto) {
		AppraisalCycle object = null;
		if (dto != null) {
			object = new AppraisalCycle();
			object.setId(dto.getId());
			object.setName(dto.getName());
			object.setStartDate(dto.getStartDate());
			object.setEndDate(dto.getEndDate());
			object.setCutoffDate(dto.getCutoffDate());
			object.setStatus(dto.getStatus().toString());
			object.setPhases(getPhases(dto.getPhases()));
		}
		return object;
	}

	private static List<AppraisalPhase> getPhases(List<AppraisalPhaseDto> phases) {
		List<AppraisalPhase> all = new ArrayList<>();
		for (AppraisalPhaseDto dto : phases) {
			all.add(getPhase(dto));
		}
		return all;
	}

	private static AppraisalPhase getPhase(AppraisalPhaseDto dto) {
		AppraisalPhase object = null;
		if (dto != null) {
			object = new AppraisalPhase();
			object.setId(dto.getId());
			object.setName(dto.getName());
			object.setStartDate(dto.getStartDate());
			object.setEndDate(dto.getEndDate());
		}
		return object;
	}

}
