package com.softvision.digital.pms.assign.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.digital.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.digital.pms.appraisal.entity.AppraisalCycle;
import com.softvision.digital.pms.appraisal.entity.AppraisalPhase;
import com.softvision.digital.pms.appraisal.mapper.AppraisalMapper;
import com.softvision.digital.pms.appraisal.repo.AppraisalCycleDataRepository;
import com.softvision.digital.pms.appraisal.repo.AppraisalRepository;
import com.softvision.digital.pms.assign.constant.CycleAssignmentStatus;
import com.softvision.digital.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.digital.pms.assign.entity.CycleAssignment;
import com.softvision.digital.pms.assign.entity.PhaseAssignment;
import com.softvision.digital.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.digital.pms.assign.model.EmployeePhaseAssignmentDto;
import com.softvision.digital.pms.assign.model.ManagerCycleAssignmentDto;
import com.softvision.digital.pms.assign.model.PhaseAssignmentDto;
import com.softvision.digital.pms.assign.repo.CycleAssignmentDataRepository;
import com.softvision.digital.pms.assign.repo.ManagerAssignmentRepository;
import com.softvision.digital.pms.assign.repo.PhaseAssignmentDataRepository;
import com.softvision.digital.pms.assign.util.AssignmentUtil;
import com.softvision.digital.pms.common.exception.ServiceException;
import com.softvision.digital.pms.email.repo.EmailRepository;
import com.softvision.digital.pms.interceptor.annotations.PreSecureAssignment;
import com.softvision.digital.pms.role.service.RoleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ManagerAssignmentService {

    private static final String ASSIGN_TO_IS_NOT_A_MANAGER = "The employee that you are trying to assign to, is not a manager";

    @Autowired private RoleService roleService;

    @Autowired private ManagerAssignmentRepository managerAssignmentRepository;

    @Autowired private AppraisalCycleDataRepository appraisalCycleDataRepository;

    @Autowired private PhaseAssignmentDataRepository phaseAssignmentDataRepository;

    @Autowired private CycleAssignmentDataRepository cycleAssignmentDataRepository;

    @Autowired private EmailRepository emailRepository;

    @Autowired private AppraisalRepository appraisalRepository;

    @Autowired private AppraisalMapper appraisalMapper;

    @PreSecureAssignment(permitManager = true)
    public void changeManager(long assignmentId, int fromEmployeeId, int toEmployeeId)
            throws ServiceException {
        log.info("assignToAnotherManager: START assignmentId={}, from={}, to={}", assignmentId, fromEmployeeId,
                toEmployeeId);
        if (fromEmployeeId == toEmployeeId) {
            throw new ServiceException("There is no change in the manager. Try assigning to a different manager.");
        }
        Optional<PhaseAssignment> optional = phaseAssignmentDataRepository.findById(assignmentId);
        if (!optional.isPresent()) {
        	throw new ServiceException("Invalid assignment");
        }
        PhaseAssignment employeePhaseAssignment = optional.get();
        AssignmentUtil.validateStatus(employeePhaseAssignment.getStatus(), "change manager",
                PhaseAssignmentStatus.NOT_INITIATED, PhaseAssignmentStatus.SELF_APPRAISAL_PENDING,
                PhaseAssignmentStatus.SELF_APPRAISAL_SAVED, PhaseAssignmentStatus.MANAGER_REVIEW_PENDING);

        if (!roleService.isManager(toEmployeeId)) {
            log.error("assignToAnotherManager: assignmentId={}, from={}, to={}, ERROR=", assignmentId,
                    fromEmployeeId, toEmployeeId, ASSIGN_TO_IS_NOT_A_MANAGER);
            throw new ServiceException(ASSIGN_TO_IS_NOT_A_MANAGER);
        }
        employeePhaseAssignment.setAssignedBy(toEmployeeId);
        phaseAssignmentDataRepository.save(employeePhaseAssignment);

        // email trigger
        emailRepository.sendChangeManagerEmail(employeePhaseAssignment.getPhaseId(), employeePhaseAssignment.getEmployeeId(),
                fromEmployeeId, toEmployeeId);
        log.info("assignToAnotherManager: END assignmentId={}, from={}, to={}", assignmentId, fromEmployeeId, toEmployeeId);
    }

    public void sendRemiderToSubmit(long phaseAssignId, int loggedInEmployeeId) throws ServiceException {
        log.info("sendRemiderToSubmit: START phaseAssignId={}, from={}", phaseAssignId, loggedInEmployeeId);
        Optional<PhaseAssignment> optional = phaseAssignmentDataRepository.findById(phaseAssignId);
        if (!optional.isPresent()) {
        	throw new ServiceException("Invalid phase assignment");
        }
        PhaseAssignment employeePhaseAssignment = optional.get();
        // email trigger
        emailRepository.sendManagerToEmployeeReminder(employeePhaseAssignment.getPhaseId(),
                employeePhaseAssignment.getAssignedBy(), employeePhaseAssignment.getEmployeeId());
        log.info("sendRemiderToSubmit: END phaseAssignId={}, from={}", phaseAssignId, loggedInEmployeeId);
    }

    public List<ManagerCycleAssignmentDto> getAllCycles(int employeeId) {
        List<ManagerCycleAssignmentDto> cycleAssignments = new ArrayList<>();
        List<AppraisalCycle> allCycles = appraisalCycleDataRepository.findAllByOrderByStartDateDesc();
        for (AppraisalCycle cycle : allCycles) {
            int cycleId = cycle.getId();
            AppraisalCycleStatus appraisalCycleStatus = AppraisalCycleStatus.get(cycle.getStatus());
            if (appraisalCycleStatus == AppraisalCycleStatus.DRAFT) {
                continue;
            }
            ManagerCycleAssignmentDto cycleAssignment = new ManagerCycleAssignmentDto();
            cycleAssignment.setCycle(appraisalMapper.getCycle(cycle));
            cycleAssignment.setEmployeeAssignments(
                    managerAssignmentRepository.getAssignmentsByAssignedByByCycle(employeeId, cycleId));
            List<PhaseAssignmentDto> phaseAssignments = new ArrayList<>();
            cycleAssignment.setPhaseAssignments(phaseAssignments);
            List<AppraisalPhase> phases = cycle.getPhases();
            for (AppraisalPhase phase : phases) {
                PhaseAssignmentDto phaseAssignment = new PhaseAssignmentDto();
                phaseAssignment.setPhase(appraisalMapper.getPhase(phase));
                phaseAssignment.setEmployeeAssignments(managerAssignmentRepository
                        .getAssignmentsByAssignedByByPhase(employeeId, cycleId, phase.getId()));
                phaseAssignments.add(phaseAssignment);
            }
            cycleAssignments.add(cycleAssignment);
        }
        return cycleAssignments;
    }

    public ManagerCycleAssignmentDto getActiveCycle(int employeeId) {
        Optional<AppraisalCycle> optional = appraisalRepository.getActiveCycle();
        if (!optional.isPresent()) {
            return null;
        }
        AppraisalCycle cycle = optional.get();
        int cycleId = cycle.getId();
        ManagerCycleAssignmentDto cycleAssignment = new ManagerCycleAssignmentDto();
        cycleAssignment.setCycle(appraisalMapper.getCycle(cycle));
        cycleAssignment.setEmployeeAssignments(
                managerAssignmentRepository.getAssignmentsByAssignedByByCycle(employeeId, cycleId));
        List<PhaseAssignmentDto> phaseAssignments = new ArrayList<>();
        cycleAssignment.setPhaseAssignments(phaseAssignments);
        List<AppraisalPhase> phases = cycle.getPhases();
        for (AppraisalPhase phase : phases) {
            PhaseAssignmentDto phaseAssignment = new PhaseAssignmentDto();
            phaseAssignment.setPhase(appraisalMapper.getPhase(phase));
            phaseAssignment.setEmployeeAssignments(managerAssignmentRepository
                    .getAssignmentsByAssignedByByPhase(employeeId, cycleId, phase.getId()));
            phaseAssignments.add(phaseAssignment);
        }
        return cycleAssignment;
    }

    @Transactional
    public void submitCycle(long cycleAssignId, int fromEmployeeId, int toEmployeeId) throws ServiceException {
        log.info("submitCycle: START cycleAssignId={}, from={}, to={}", cycleAssignId, fromEmployeeId, toEmployeeId);
        Optional<CycleAssignment> optional = cycleAssignmentDataRepository.findById(cycleAssignId);
        CycleAssignment employeeCycleAssignment = optional.orElseThrow(() -> new ServiceException("Assignment does not exist."));

        // he must be the same employee who has been assigned
        int assignedBy = employeeCycleAssignment.getAssignedBy();
        if (assignedBy != fromEmployeeId) {
            throw new ServiceException(
                    "The manager who assessed the last phase can only assign it to the next level manager");
        }
        int status = employeeCycleAssignment.getStatus();
        CycleAssignmentStatus cycleAssignmentStatus = CycleAssignmentStatus.get(status);
        if (status != CycleAssignmentStatus.ABRIDGED.getCode()) {
            throw new ServiceException(
                    "Assignment is in '" + cycleAssignmentStatus.getName() + "' status. Cannot change the manager now");
        }
        if (!roleService.isManager(toEmployeeId)) {
            log.error("assignToAnotherManager: cycleAssignId={}, from={}, to={}, ERROR=", cycleAssignId,
                    fromEmployeeId, toEmployeeId, ASSIGN_TO_IS_NOT_A_MANAGER);
            throw new ServiceException(ASSIGN_TO_IS_NOT_A_MANAGER);
        }
        employeeCycleAssignment.setSubmittedTo(toEmployeeId);
        employeeCycleAssignment.setStatus(CycleAssignmentStatus.CONCLUDED.getCode());
        cycleAssignmentDataRepository.save(employeeCycleAssignment);
        log.info("submitCycle: END cycleAssignId={}, from={}, to={}", cycleAssignId, fromEmployeeId, toEmployeeId);
    }

    public List<ManagerCycleAssignmentDto> getSubmittedCycles(int requestedEmployeeId) {
        List<ManagerCycleAssignmentDto> cycleAssignments = new ArrayList<>();
        List<AppraisalCycle> allCycles = appraisalCycleDataRepository.findAllByOrderByStartDateDesc();
        // Get all the cycles, ignore DRAFT cycles
        for (AppraisalCycle cycle : allCycles) {
            int cycleId = cycle.getId();
            AppraisalCycleStatus appraisalCycleStatus = AppraisalCycleStatus.get(cycle.getStatus());
            if (appraisalCycleStatus == AppraisalCycleStatus.DRAFT) {
                continue;
            }
            // For each cycle, get cycle assignment for this employee
            ManagerCycleAssignmentDto cycleAssignment = new ManagerCycleAssignmentDto();
            cycleAssignment.setCycle(appraisalMapper.getCycle(cycle));
            List<EmployeeAssignmentDto> employeeAssignments = managerAssignmentRepository
                    .getAssignmentsBySubmittedToByCycle(cycleId, requestedEmployeeId);
            cycleAssignment.setEmployeeAssignments(employeeAssignments);
            cycleAssignments.add(cycleAssignment);
        }
        return cycleAssignments;
    }

	public ManagerCycleAssignmentDto getSubmittedCycleByEmployeeId(int cycleId, int employeeId) throws ServiceException {
    	ManagerCycleAssignmentDto cycleAssignment = new ManagerCycleAssignmentDto();

    	Optional<AppraisalCycle> optional = appraisalCycleDataRepository.findById(cycleId);
    	if (!optional.isPresent()) {
    		throw new ServiceException("Appraisal cycle does not exist");
    	}
    	AppraisalCycle cycle = optional.get();
    	AppraisalCycleStatus appraisalCycleStatus = AppraisalCycleStatus.get(cycle.getStatus());
    	if (appraisalCycleStatus != AppraisalCycleStatus.DRAFT) {
    		List<EmployeePhaseAssignmentDto> employeePhaseAssignments = managerAssignmentRepository.getPhaseAssignmentsByCycleByEmployeeId(cycleId, employeeId);
    		cycleAssignment.setCycle(appraisalMapper.getCycle(cycle));
    		cycleAssignment.setEmployeeAssignments(employeePhaseAssignments);
        }
    	return cycleAssignment;
    }

    @PreSecureAssignment(permitManager = true)
	public void deleteAssignment(long assignmentId, int fromEmployeeId) throws ServiceException {
    	log.info("deleteAssignment: START assignmentId={}, from={}", assignmentId, fromEmployeeId);
    	Optional<PhaseAssignment> optional = phaseAssignmentDataRepository.findById(assignmentId);
    	if(!optional.isPresent()) {
    		throw new ServiceException("Assignment does not exist");
    	}
        PhaseAssignment phaseAssignment = optional.get();
        PhaseAssignmentStatus phaseAssignmentStatus = PhaseAssignmentStatus.get(phaseAssignment.getStatus());
        if (phaseAssignmentStatus != PhaseAssignmentStatus.NOT_INITIATED) {
        	throw new ServiceException("This assignment is in the status '" + phaseAssignmentStatus.getName() + "'. This cannot be deleted now");
        }
        phaseAssignmentDataRepository.deleteById(assignmentId);
        log.info("deleteAssignment: END assignmentId={}, from={}, to={}", assignmentId, fromEmployeeId);
	}

}
