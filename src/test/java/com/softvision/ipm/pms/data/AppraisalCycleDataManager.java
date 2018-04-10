package com.softvision.ipm.pms.data;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softvision.ipm.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.ipm.pms.appraisal.model.AppraisalCycleDto;
import com.softvision.ipm.pms.appraisal.model.AppraisalPhaseDto;
import com.softvision.ipm.pms.appraisal.service.AppraisalService;
import com.softvision.ipm.pms.common.exception.ServiceException;

@Component
public class AppraisalCycleDataManager implements AbstractDataManager {

	@Autowired AppraisalService appraisalService;

	@Override
	public void clearData() throws Exception {
		List<AppraisalCycleDto> cycles = appraisalService.getCycles();
		if (cycles != null && !cycles.isEmpty()) {
			for (AppraisalCycleDto appraisalCycleDto : cycles) {
				appraisalService.delete(appraisalCycleDto.getId());
			}
		}
	}

	@Override
	public void loadData() throws Exception {
		createAppraisalCycles(2010, 10);
		//activateCycle();
	}

	private void activateCycle() throws ServiceException {
		List<AppraisalCycleDto> cycles = appraisalService.getCycles();
		System.out.println("##################################### "+ cycles.size());
		for (AppraisalCycleDto cycle : cycles) {
			System.out.println("################## "+cycle);
			int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(cycle.getStartDate()));
			System.out.println(year);
			if (year < 2017) {
				appraisalService.changeStatus(cycle.getId(), AppraisalCycleStatus.READY);
				appraisalService.changeStatus(cycle.getId(), AppraisalCycleStatus.ACTIVE);
				appraisalService.changeStatus(cycle.getId(), AppraisalCycleStatus.COMPLETE);
			} else if (year == 2017) {
				appraisalService.changeStatus(cycle.getId(), AppraisalCycleStatus.READY);
				appraisalService.changeStatus(cycle.getId(), AppraisalCycleStatus.ACTIVE);
			} else if (year == 2018) {
				appraisalService.changeStatus(cycle.getId(), AppraisalCycleStatus.READY);
			}
		}
		//AppraisalCycleDto activeCycle = cycles.get(RANDOM.nextInt(cycles.size()));
		//appraisalService.changeStatus(activeCycle.getId(), AppraisalCycleStatus.ACTIVE);
	}

	private void createAppraisalCycles(int startYear, int numberOfYears) throws ServiceException {
		int[] numberOfPhaseChoice = {1, 2, 3, 4, 6, 12};

		LocalDate localDate = LocalDate.of(startYear, 1, 1);
		ZoneId zoneId = ZoneId.systemDefault();

		for (int year = startYear; year < startYear+numberOfYears; year++) {
			AppraisalCycleDto appraisalCycleDto = new AppraisalCycleDto();
			appraisalCycleDto.setId(0);
			appraisalCycleDto.setName(String.valueOf(year));

			localDate=localDate.withYear(year).with(TemporalAdjusters.firstDayOfYear());
			appraisalCycleDto.setStartDate(Date.from(localDate.atStartOfDay(zoneId).toInstant()));

			localDate=localDate.with(TemporalAdjusters.lastDayOfYear());
			appraisalCycleDto.setEndDate(Date.from(localDate.atStartOfDay(zoneId).toInstant()));

			localDate=localDate.minusMonths(3);
			appraisalCycleDto.setCutoffDate(Date.from(localDate.atStartOfDay(zoneId).toInstant()));

			List<AppraisalPhaseDto> phases = new ArrayList<>();
			int numberOfPhases = numberOfPhaseChoice[RANDOM.nextInt(numberOfPhaseChoice.length)];
			if (year == 2017) {
			    numberOfPhases=2;
			} else if (year == 2018) {
                numberOfPhases=4;
            }
			int monthsPerPhases=12/numberOfPhases;

			localDate=localDate.withYear(year).with(TemporalAdjusters.firstDayOfYear());
			for (int index = 0; index < numberOfPhases; index++) {
				AppraisalPhaseDto phase = new AppraisalPhaseDto();
				phase.setName("Phase " + (index+1));

				localDate=localDate.with(TemporalAdjusters.firstDayOfMonth());
				phase.setStartDate(Date.from(localDate.atStartOfDay(zoneId).toInstant()));
				localDate=localDate.plusMonths(monthsPerPhases).minusDays(1);
				phase.setEndDate(Date.from(localDate.atStartOfDay(zoneId).toInstant()));
				phases.add(phase);
				localDate=localDate.plusDays(1);
			}
			appraisalCycleDto.setPhases(phases);
			//System.out.println(appraisalCycleDto);
			appraisalService.update(appraisalCycleDto);
		}
	}

}
