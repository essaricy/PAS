package com.softvision.ipm.pms.assign.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softvision.ipm.pms.appraisal.constant.AppraisalCycleStatus;
import com.softvision.ipm.pms.appraisal.entity.AppraisalCycle;
import com.softvision.ipm.pms.appraisal.entity.AppraisalPhase;
import com.softvision.ipm.pms.appraisal.mapper.AppraisalMapper;
import com.softvision.ipm.pms.appraisal.repo.AppraisalCycleDataRepository;
import com.softvision.ipm.pms.appraisal.repo.AppraisalRepository;
import com.softvision.ipm.pms.assess.model.PhaseAssessmentDto;
import com.softvision.ipm.pms.assess.service.PhaseAssessmentService;
import com.softvision.ipm.pms.assign.constant.CycleAssignmentStatus;
import com.softvision.ipm.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.ipm.pms.assign.entity.CycleAssignment;
import com.softvision.ipm.pms.assign.entity.PhaseAssignment;
import com.softvision.ipm.pms.assign.model.EmployeeAssignmentDto;
import com.softvision.ipm.pms.assign.model.EmployeePhaseAssignmentDto;
import com.softvision.ipm.pms.assign.model.ManagerCycleAssignmentDto;
import com.softvision.ipm.pms.assign.model.PhaseAssignmentDto;
import com.softvision.ipm.pms.assign.repo.CycleAssignmentDataRepository;
import com.softvision.ipm.pms.assign.repo.ManagerAssignmentRepository;
import com.softvision.ipm.pms.assign.repo.PhaseAssignmentDataRepository;
import com.softvision.ipm.pms.assign.util.AssignmentUtil;
import com.softvision.ipm.pms.common.exception.ServiceException;
import com.softvision.ipm.pms.email.repo.EmailRepository;
import com.softvision.ipm.pms.interceptor.annotations.PreSecureAssignment;
import com.softvision.ipm.pms.role.service.RoleService;

@Service
public class ManagerAssignmentService {

    private static final String ASSIGN_TO_IS_NOT_A_MANAGER = "The employee that you are trying to assign to, is not a manager";

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagerAssignmentService.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private ManagerAssignmentRepository managerAssignmentRepository;

    @Autowired
    private AppraisalCycleDataRepository appraisalCycleDataRepository;

    @Autowired
    private PhaseAssignmentDataRepository phaseAssignmentDataRepository;

    @Autowired
    private CycleAssignmentDataRepository cycleAssignmentDataRepository;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired private AppraisalRepository appraisalRepository;

    @Autowired private AppraisalMapper appraisalMapper;

    @Autowired private PhaseAssessmentService phaseAssessmentService;

    @PreSecureAssignment(permitManager = true)
    public void assignToAnotherManager(long assignmentId, int fromEmployeeId, int toEmployeeId)
            throws ServiceException {
        LOGGER.info("assignToAnotherManager: START assignmentId={}, from={}, to={}", assignmentId, fromEmployeeId,
                toEmployeeId);
        if (fromEmployeeId == toEmployeeId) {
            throw new ServiceException("There is no change in the manager. Try assigning to a different manager.");
        }
        PhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(assignmentId);
        AssignmentUtil.validateStatus(employeePhaseAssignment.getStatus(), "change manager",
                PhaseAssignmentStatus.NOT_INITIATED, PhaseAssignmentStatus.SELF_APPRAISAL_PENDING);
        if (!roleService.isManager(toEmployeeId)) {
            LOGGER.error("assignToAnotherManager: assignmentId={}, from={}, to={}, ERROR=", assignmentId,
                    fromEmployeeId, toEmployeeId, ASSIGN_TO_IS_NOT_A_MANAGER);
            throw new ServiceException(ASSIGN_TO_IS_NOT_A_MANAGER);
        }
        employeePhaseAssignment.setAssignedBy(toEmployeeId);
        phaseAssignmentDataRepository.save(employeePhaseAssignment);

        // email trigger
        emailRepository.sendChangeManager(employeePhaseAssignment.getPhaseId(), employeePhaseAssignment.getEmployeeId(),
                fromEmployeeId, toEmployeeId);
        LOGGER.info("assignToAnotherManager: END assignmentId={}, from={}, to={}", assignmentId, fromEmployeeId, toEmployeeId);
    }

    public void sendRemiderToSubmit(long phaseAssignId, int loggedInEmployeeId) {
        LOGGER.info("sendRemiderToSubmit: START phaseAssignId={}, from={}", phaseAssignId, loggedInEmployeeId);
        PhaseAssignment employeePhaseAssignment = phaseAssignmentDataRepository.findById(phaseAssignId);
        // email trigger
        emailRepository.sendManagerToEmployeeReminder(employeePhaseAssignment.getPhaseId(),
                employeePhaseAssignment.getAssignedBy(), employeePhaseAssignment.getEmployeeId());
        LOGGER.info("sendRemiderToSubmit: END phaseAssignId={}, from={}", phaseAssignId, loggedInEmployeeId);
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
        AppraisalCycle cycle = appraisalRepository.getActiveCycle();
        if (cycle == null) {
            return null;
        }
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
        LOGGER.info("submitCycle: START cycleAssignId={}, from={}, to={}", cycleAssignId, fromEmployeeId, toEmployeeId);
        CycleAssignment employeeCycleAssignment = cycleAssignmentDataRepository.findById(cycleAssignId);
        if (employeeCycleAssignment == null) {
            throw new ServiceException("Assignment does not exist.");
        }
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
            LOGGER.error("assignToAnotherManager: cycleAssignId={}, from={}, to={}, ERROR=", cycleAssignId,
                    fromEmployeeId, toEmployeeId, ASSIGN_TO_IS_NOT_A_MANAGER);
            throw new ServiceException(ASSIGN_TO_IS_NOT_A_MANAGER);
        }
        employeeCycleAssignment.setSubmittedTo(toEmployeeId);
        employeeCycleAssignment.setStatus(CycleAssignmentStatus.CONCLUDED.getCode());
        cycleAssignmentDataRepository.save(employeeCycleAssignment);
        LOGGER.info("submitCycle: END cycleAssignId={}, from={}, to={}", cycleAssignId, fromEmployeeId, toEmployeeId);
    }

    public List<ManagerCycleAssignmentDto> getSubmittedCycles(int employeeId) {
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
                    .getAssignmentsBySubmittedToByCycle(employeeId, cycleId);
            cycleAssignment.setEmployeeAssignments(employeeAssignments);
            cycleAssignments.add(cycleAssignment);
        }
        return cycleAssignments;
    }

    public List<PhaseAssessmentDto> getAllPhaseAssessments(long assignId, int employeeId, int requestedEmployeeId)
            throws ServiceException {
        LOGGER.info("getAllPhaseAssessments: START assignId={}, employeeId={}, requestedEmployeeId={}", assignId, employeeId, requestedEmployeeId);
        CycleAssignment cycleAssignment = cycleAssignmentDataRepository.findById(assignId);
        if (cycleAssignment == null) {
            throw new ServiceException("Invalid assignment");
        }
        Integer submittedTo = cycleAssignment.getSubmittedTo();
        if (submittedTo == null || submittedTo != requestedEmployeeId) {
            throw new ServiceException("UNAUTHORIZED ACCESS ATTEMPTED");
        }

        List<PhaseAssessmentDto> phaseAssessments = new ArrayList<>();
        // Get all the phases
        List<EmployeePhaseAssignmentDto> phaseAssignmentsInCycle = managerAssignmentRepository
                .getPhaseAssignmentsByCycleByEmployeeId(cycleAssignment.getCycleId(), employeeId);
        if (phaseAssignmentsInCycle != null && !phaseAssignmentsInCycle.isEmpty()) {
            for (EmployeePhaseAssignmentDto phaseAssignment : phaseAssignmentsInCycle) {
                PhaseAssessmentDto phaseAssessment = phaseAssessmentService.getByAssignment(phaseAssignment.getAssignmentId(), employeeId);
                phaseAssessments.add(phaseAssessment);
            }
        }
        return phaseAssessments;
    }

}
