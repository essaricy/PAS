INSERT INTO role(id, role_name) 	VALUES (1, 'Admin');
INSERT INTO role(id, role_name) 	VALUES (2, 'Manager');
INSERT INTO role(id, role_name) 	VALUES (3, 'Employee');

-- select * from goal;
INSERT INTO goal(id, name) VALUES (nextval('goal_id_seq'), 'Client Orientation/Customer Focus');
INSERT INTO goal(id, name) VALUES (nextval('goal_id_seq'), 'Project Management');
INSERT INTO goal(id, name) VALUES (nextval('goal_id_seq'), 'Leadership');
INSERT INTO goal(id, name) VALUES (nextval('goal_id_seq'), 'Communication');
INSERT INTO goal(id, name) VALUES (nextval('goal_id_seq'), 'Business Domain');
INSERT INTO goal(id, name) VALUES (nextval('goal_id_seq'), 'Technical Solutions');
INSERT INTO goal(id, name) VALUES (nextval('goal_id_seq'), 'Process Awareness & Compliance');
INSERT INTO goal(id, name) VALUES (nextval('goal_id_seq'), 'Corporate Initiatives');


INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Client Orientation/Customer Focus'),
	'Establishes and maintains effective relationship with both internal and external customers and gains their trust');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Client Orientation/Customer Focus'),
	'Understands and  Proactively addresses customer needs in a timely manner by following the Client/Softvision Process');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Client Orientation/Customer Focus'),
	'Knowledge of Softvision and Customer processes (software development processes)');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Client Orientation/Customer Focus'),
	'Number of customer accolades');

INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Project Management'),
	'Ability to identify, plan, procure and utilize resources within the scope of the project');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Project Management'),
	'Ability to identify, mitigate and manage risks in the project. a)  Planning and Control b)  Resource Management');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Project Management'),
	'Knowledge of PM basics like Initiation, Planning, Monitoring, Negotiation skills, budgeting,Tracking and Closure');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Project Management'),
	'Concepts and application of PM disciplines like estimation, scope, time, cost and risk mgmt');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Project Management'),
	'Application of Softvision PM Process and Client PM Process');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Project Management'),
	'Process compliance in terms of non compliance');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Project Management'),
	'Consitency in communicatoion - gaps btw onshore communication etc - ');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Project Management'),
	'Frequency of commuinction within the team - internal and external');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Project Management'),
	'No of Delivery Escalations(anything greter than zero)');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Project Management'),
	'Ontime resolutions');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Project Management'),
	'Ontime zero defect deliveries');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Project Management'),
	'Initiating project activities as per QMS Process');

INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Leadership'),
	'People Management - timely closure of employee concerns');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Leadership'),
	'Mentoring, motivating and communicating with the team');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Leadership'),
	'Conducting Root cause analysis and implementing preventive & corrective actions on the conflict issues/defects/customer complaints etc at the Project Level');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Leadership'),
	'Accountability and Transperancy');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Leadership'),
	'Insightfull and Innovation');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Leadership'),
	'Team Acceptance and dynamics');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Leadership'),
	'Acting as point of contact for service issues that need to be or have been escalated (Project level)');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Leadership'),
	'Initiating Demos/ Walkthroughs / discussions with the client to ensure that the team is getting clarifications on issues in Customer supplied work products');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Leadership'),
	'Defining best practices in the critical evaluation and selection and / or development of the development methods, processes, best practices, tools, software components and hardware requirements of the applications and data.');

INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Communication'),
	'Good communication');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Communication'),
	'Knowledge of Spoken and Written English');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Communication'),
	'Listening Skills, clarity and comprehension');

INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Business Domain'),
	'Ability to understand and align with customer''s business / technology domain');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Business Domain'),
	'Knowledge of retail domain');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Business Domain'),
	'Process Abilities in handling 2 to 3 retail project');

INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Technical Solutions'),
	'Understanding the design & developing the code');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Technical Solutions'),
	'Creating, executing Unit Test cases and documenting results');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Technical Solutions'),
	'Fostering team building by sharing Technical knowledge, highlighting problems or critical situations to the Team');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Technical Solutions'),
	'Resolving production/post production problems coordinating with development/QA team');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Technical Solutions'),
	'Analysis of existing code/functionality and suggesting the improvement in performance');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Technical Solutions'),
	'Reviewing the work products on SDLC/STLC phases and document');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Technical Solutions'),
	'Ensuring effective resolution of complaints, problems tickets raised & taking responsibility for the decisions taken for the issues and escalation');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Technical Solutions'),
	'Ensuring effective resolution & taking responsibility on the improvement in backlog management');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Technical Solutions'),
	'Conducting Root cause analysis and implementing preventive & corrective actions on the conflict issues/defects/customer complaints etc at the Project Level');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Technical Solutions'),
	'Coordinating & Participating in Bug Validation/Defect review meetings');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Technical Solutions'),
	'Executing project activities');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Technical Solutions'),
	'Identifying, tracking and managing all risks and manage the mitigation plan at Project Level');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Technical Solutions'),
	'Ensuring quality and timely completion and delivery of the deliverables defined in SoW');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Technical Solutions'),
	'Providing direction on strategic technical issues at ORG level');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Technical Solutions'),
	'Executing the task to completion as per Work Breakdown Structure and schedules in line with the Project Plan');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Technical Solutions'),
	'Transforming, Implementing, tuning and maintaining the databases used by the application');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Technical Solutions'),
	'Specifying, placing and modifying data (Database) to support the development of applications solutions, and being responsible for the data integrity & security');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Technical Solutions'),
	'Analysis of new software product releases and the impact of these new releases onto application and come up with the plan to implement these releases');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Technical Solutions'),
	'Analysis of new Database product releases and the impact of these new releases onto application and come up with the plan to implement these releases');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Technical Solutions'),
	'Translating the client technical requirements for development into IT solutions that fit into a new technical architecture');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Technical Solutions'),
	'Establishing a regular and comprehensive schedule of project meetings, submission of reports, reviews and presentations as part of the project communication plan');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Technical Solutions'),
	'Supporting the (Test)Lead to establish a regular and comprehensive schedule of project meetings, submission of reports, reviews and presentations as part of the project communication plan');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Technical Solutions'),
	'Identifying & setting up of test environment');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Technical Solutions'),
	'Working with Test Lead/Development counterparts to help plan, build, operate and maintain the testing environment');

INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Process Awareness & Compliance'),
	'Following the defined Softvision/Client Process, Standards, Templates, Checklists, Guidelines for the SDLC/STLC phases in an engagement');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Process Awareness & Compliance'),
	'Adhering to the change/configuration management process');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Process Awareness & Compliance'),
	'Adherence to company policies');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Process Awareness & Compliance'),
	'Awareness to SHARP,ISMS/SOC processess');
INSERT INTO goal_param(ID, GOAL_ID, NAME) VALUES (nextval('goal_param_id_seq'),
	(select id from goal where name='Process Awareness & Compliance'),
	'CMMi level 5 process ');

---- ######################### email_template for PRD
insert into email_template
values(1,'KICK_OFF','AppraisalKickoff.ftl','Appraisal Kick off - {0} !!!','http://localhost:9000/','hrconnect@softvision.com','offshoresoftvision@softvision.com',null);

insert into email_template 
values(2,'EMPLOYEE_ENABLE','EmploeeEnable.ftl','[{0},{1}]   [{2}] Self 
Appraisal Form Available!!','http://localhost:9000/',null,null,null);

insert into email_template
values(3,'EMPLOYEE_SUBMITED','EmployeeSubmitted.ftl','[{0},{1}] [{2}] Self Appraisal Submitted!!','http://localhost:9000/',null,null,null);

insert into email_template
values(4,'MGR_TO_EMP_REMAINDER','ManagerToEmploeeRemainder.ftl','[{0},{1}]
[{2}] Self Appraisal Pending
Remainder!!','http://localhost:9000/',null,null,null);

insert into email_template
values(5,'MANAGER_REVIEW','ManagerReviewCompleted.ftl','[{0},{1}] [{2}] Self Appraisal Reviewed!!','http://localhost:9000/',null,null,null);

--cc requried
insert into email_template
values(6,'MANAGER_FROZEN','ManagerReviewFrozen.ftl','[{0},{1}] [{2}] Self Appraisal Concluded!!','http://localhost:9000/',null,null,null);

-- HR for group?
insert into email_template 
values(7,'EMP_ACCEPTED','EmploeeAccepted.ftl','[{0},{1}]   [{2}] Self 
Appraisal
Accepted!!','http://localhost:9000/',null,null,'hrconnect@softvision.com');

-- HR for group?
insert into email_template 
values(8,'EMP_REJECTED','EmploeeRejected.ftl','[{0},{1}]   [{2}] Self 
Appraisal
Rejected!!','http://localhost:9000/',null,null,'hrconnect@softvision.com');

-- HR for group?
insert into email_template 
values(9,'UPDATE_REVIEW','UpdateReviewEmp.ftl','[{0},{1}]   [{2}] Self 
Appraisal
Re-visited!!','http://localhost:9000/',null,null,'hrconnect@softvision.com');

insert into email_template
values(10,'HR_TO_EMP_REM','HrToEmploeeRemainder.ftl','[{0},{1}] [{2}] Self Appraisal Pending Remainder!!','http://localhost:9000/',null,null,null);

insert into email_template
values(11,'HR_TO_MGR_REM','HrToManagerRemainder.ftl','[{0},{1}] [{2}] Self Appraisal Review Pending!!','http://localhost:9000/',null,null,null);

-- no button
insert into email_template
values(12,'CYCLE_CONCLUDE','Appraisalconcluded.ftl','Appraisal {0} Has Been Conclude!!!','','hrconnect@softvision.com','offshoresoftvision@softvision.com',null);

insert into email_template 
values(13,'CHANGE_MGR','ChangeManager.ftl','[{0},{1}]   [{2}] Assign 
Manager Changed!!','http://localhost:9000/',null,null,null);

---- ######################### email_template for TEST
insert into email_template values(1,'KICK_OFF','AppraisalKickoff.ftl','Appraisal Kick off - {0} !!!','http://localhost:9000/','HR-App','rohith.ramesh@softvision.com','');
insert into email_template values(2,'EMPLOYEE_ENABLE','EmploeeEnable.ftl','[{0},{1}]   [{2}]  Self Appraisal form available!','http://localhost:9000/','','','');
insert into email_template values(3,'EMPLOYEE_SUBMITED','EmployeeSubmitted.ftl','[{0},{1}]   [{2}] Self Appraisal submitted!!','http://localhost:9000/','','','');
insert into email_template values(4,'MGR_TO_EMP_REMAINDER','ManagerToEmploeeRemainder.ftl','[{0},{1}]  [{2}] Self Appraisal pending Remainder!!','http://localhost:9000/','','','');
insert into email_template values(5,'MANAGER_REVIEW','ManagerReviewCompleted.ftl','[{0},{1}]   [{2}] Self Appraisal Reviewed!!','http://localhost:9000/','','','');
insert into email_template values(6,'MANAGER_FROZEN','ManagerReviewFrozen.ftl','[{0},{1}]   [{2}] Self Appraisal Concluded!!','http://localhost:9000/','','','');
insert into email_template values(7,'EMP_ACCEPTED','EmploeeAccepted.ftl','[{0},{1}]   [{2}] Self Appraisal Accepted!!','http://localhost:9000/','','','rohith.ramesh@softvision.com');
insert into email_template values(8,'EMP_REJECTED','EmploeeRejected.ftl','[{0},{1}]   [{2}] Self Appraisal Rejected!!','http://localhost:9000/','','','rohith.ramesh@softvision.com');
insert into email_template values(9,'UPDATE_REVIEW','UpdateReviewEmp.ftl','[{0},{1}]   [{2}] Self Appraisal Re visited!!','http://localhost:9000/','','','rohith.ramesh@softvision.com');
insert into email_template values(10,'HR_TO_EMP_REM','HrToEmploeeRemainder.ftl','[{0},{1}]   [{2}] Self Appraisal pending!!','http://localhost:9000/','','','rohith.ramesh@softvision.com');
insert into email_template values(11,'HR_TO_MGR_REM','HrToManagerRemainder.ftl','[{0},{1}]   [{2}] Self Appraisal Review pending!!','http://localhost:9000/','','','');
insert into email_template values(12,'CYCLE_CONCLUDE','Appraisalconcluded.ftl','Appraisal {0} has been conclude !!!','','HR-App',' rohith.ramesh@softvision.com','');
insert into email_template values(13,'CHANGE_MGR','ChangeManager.ftl','[{0},{1}]   [{2}] Assign manager change!!','http://localhost:9000/','HR-App','rohith.ramesh@softvision.com','');
