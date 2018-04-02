-- #######################################################
-- ROle Module
-- #######################################################
CREATE SEQUENCE emp_role_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 1000000	CACHE 1;

-- #######################################################
-- appr Module
-- #######################################################
CREATE SEQUENCE appr_cycle_id_seq		INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 100000		CACHE 1;
CREATE SEQUENCE appr_phase_id_seq   	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 500000		CACHE 1;

-- #######################################################
-- Goals Module
-- #######################################################
CREATE SEQUENCE goal_id_seq				INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 10000		CACHE 1;
CREATE SEQUENCE goal_param_id_seq		INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 100000		CACHE 1;

-- #######################################################
-- Templates Module
-- #######################################################
CREATE SEQUENCE template_id_seq			INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 1000000	CACHE 1;
CREATE SEQUENCE template_header_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 10000000	CACHE 1;
CREATE SEQUENCE template_detail_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 100000000	CACHE 1;

-- #######################################################
-- Assignment Module
-- #######################################################
CREATE SEQUENCE cycle_assign_id_seq		INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 100000000		CACHE 1;
CREATE SEQUENCE phase_assign_id_seq		INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 10000000000	CACHE 1;

-- #######################################################
-- Assessment Module
-- #######################################################
CREATE SEQUENCE assess_header_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 10000000000	CACHE 1;
CREATE SEQUENCE assess_detail_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 10000000000	CACHE 1;

-- #######################################################
-- Employee Module
-- #######################################################
CREATE TABLE employee (
	EMPLOYEE_ID		BIGINT NOT NULL,
	LOGIN_ID		varchar(25) NOT NULL,
	FIRST_NAME		varchar(60) NOT NULL,
	LAST_NAME		varchar(60) NOT NULL,
	EMPLOYMENT_TYPE	varchar(40) NOT NULL,
	BAND			varchar(15) NOT NULL,
	DESIGNATION		varchar(80) NULL,
	HIRED_ON		date NOT NULL,
	LOCATION		varchar(20),

	PRIMARY KEY (EMPLOYEE_ID),
	UNIQUE(LOGIN_ID),
	CHECK (EMPLOYEE_ID > 0),
	CHECK (FIRST_NAME <> ''),
	CHECK (LAST_NAME <> ''),
	CHECK (EMPLOYMENT_TYPE <> ''),
	CHECK (BAND <> '')
);
CREATE TABLE role (
	ID		BIGINT NOT NULL,
	ROLE_NAME		varchar(25) NOT NULL,

	PRIMARY KEY (ID),
	UNIQUE(ROLE_NAME),
	CHECK (ID > 0),
	CHECK (ROLE_NAME <> '')
);
CREATE TABLE employee_role (
	ID				BIGINT NOT NULL,
	ROLE_ID		BIGINT NOT NULL,
	EMPLOYEE_ID		BIGINT NOT NULL,

	PRIMARY KEY (ID),
	FOREIGN KEY (ROLE_ID) REFERENCES role (ID),
	FOREIGN KEY (EMPLOYEE_ID) REFERENCES employee (EMPLOYEE_ID),
	UNIQUE(ROLE_ID,EMPLOYEE_ID),
	CHECK (ID > 0)
);

-- #######################################################
-- Appraisal Module
-- #######################################################
CREATE TABLE appr_cycle (
	ID			SMALLINT NOT NULL,
	NAME		VARCHAR(40) NOT NULL,
	START_DATE	DATE NOT NULL,
	END_DATE	DATE NOT NULL,
	CUTOFF_DATE	DATE NOT NULL,
	STATUS		VARCHAR(10) NOT NULL DEFAULT 'DRAFT',
	PRIMARY KEY (ID),
	UNIQUE (NAME),
	UNIQUE (START_DATE),
	UNIQUE (END_DATE),
	CHECK (ID > 0),
	CHECK (NAME <> ''),
	CHECK (END_DATE > START_DATE),
	CHECK (STATUS IN ('DRAFT', 'READY', 'ACTIVE', 'COMPLETE') )
);

CREATE TABLE appr_phase (
	ID			SMALLINT NOT NULL,
	CYCLE_ID	SMALLINT NOT NULL,
	NAME		VARCHAR(40) NOT NULL,
	START_DATE	DATE NOT NULL,
	END_DATE	DATE NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (CYCLE_ID) REFERENCES appr_cycle (ID),
	UNIQUE (CYCLE_ID, NAME),
	UNIQUE (START_DATE),
	UNIQUE (END_DATE),
	CHECK (ID > 0),	
	CHECK (NAME <> ''),
	CHECK (END_DATE > START_DATE)
);

-- #######################################################
-- Goals Module
-- #######################################################
-- Goal
CREATE TABLE goal (
	ID			SMALLINT NOT NULL,
	NAME		VARCHAR(40) NOT NULL,
	PRIMARY KEY (ID),
	UNIQUE (NAME),
	CHECK (ID > 0),
	CHECK (NAME <> '')
);
-- Goal Parameters
CREATE TABLE goal_param (
	ID			SMALLINT NOT NULL,
	GOAL_ID		SMALLINT NOT NULL,
	APPLY		VARCHAR(1) NOT NULL DEFAULT 'Y',
	NAME		VARCHAR(300) NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (GOAL_ID) REFERENCES goal (ID),
	CHECK (ID > 0),
	CHECK (NAME <> ''),
	CHECK (APPLY IN ('Y', 'N'))
);

-- #######################################################
-- Template Module
-- #######################################################
-- Goal Template
CREATE TABLE template (
	ID			BIGINT NOT NULL,
	NAME		VARCHAR(100) NOT NULL,
	UPDATED_BY	SMALLINT NOT NULL,
	UPDATED_AT	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

	PRIMARY KEY (ID),
	FOREIGN KEY (UPDATED_BY) REFERENCES employee (EMPLOYEE_ID),
	UNIQUE (NAME),
	CHECK (ID > 0),
	CHECK (NAME <> ''),
	CHECK (UPDATED_BY > 0)
);
CREATE TABLE template_header (
	ID				BIGINT NOT NULL,
	TEMPLATE_ID		BIGINT NOT NULL,
	GOAL_ID			SMALLINT NOT NULL,
	WEIGHTAGE		INT NOT NULL,

	PRIMARY KEY (ID),
	FOREIGN KEY (TEMPLATE_ID) REFERENCES template (ID),
	FOREIGN KEY (GOAL_ID) REFERENCES goal (ID),
	UNIQUE (TEMPLATE_ID, GOAL_ID),
	CHECK (ID > 0),
	CHECK (WEIGHTAGE >= 0)
);

CREATE TABLE template_detail (
	ID				BIGINT NOT NULL,
	HEADER_ID		BIGINT NOT NULL,
	PARAM_ID		SMALLINT NOT NULL,
	APPLY			VARCHAR(1) NOT NULL DEFAULT 'Y',

	PRIMARY KEY (ID),
	FOREIGN KEY (HEADER_ID) REFERENCES template_header (ID),
	FOREIGN KEY (PARAM_ID) REFERENCES goal_param (ID),
	CHECK (ID > 0),
	CHECK (APPLY IN ('Y', 'N'))
);

-- #######################################################
-- Assignment Module
-- #######################################################
-- drop table cycle_assign;
CREATE TABLE cycle_assign (
	ID				BIGINT NOT NULL,
    CYCLE_ID		SMALLINT NOT NULL,
    TEMPLATE_ID		BIGINT NOT NULL,
    EMPLOYEE_ID		SMALLINT NOT NULL,
	ASSIGNED_BY		SMALLINT NOT NULL,
	ASSIGNED_AT	    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	STATUS			SMALLINT NOT NULL DEFAULT 0,

	SUBMITTED_TO		SMALLINT NULL,
	SCORE			DECIMAL(3,2) NOT NULL DEFAULT 0,
	
	PRIMARY KEY (ID),
	FOREIGN KEY (CYCLE_ID) REFERENCES appr_cycle (ID),
	FOREIGN KEY (TEMPLATE_ID) REFERENCES template (ID),
	FOREIGN KEY (EMPLOYEE_ID) REFERENCES employee (EMPLOYEE_ID),
	FOREIGN KEY (SUBMITTED_TO) REFERENCES employee (EMPLOYEE_ID),

	UNIQUE (CYCLE_ID,TEMPLATE_ID,EMPLOYEE_ID),

	CHECK (ID > 0),
	CHECK (CYCLE_ID > 0),
	CHECK (TEMPLATE_ID > 0),
	CHECK (EMPLOYEE_ID > 0),
	CHECK (EMPLOYEE_ID != ASSIGNED_BY),
	CHECK (EMPLOYEE_ID != SUBMITTED_TO),
	CHECK (SCORE >= 0)
);

-- drop table phase_assign;
CREATE TABLE phase_assign (
	ID						BIGINT NOT NULL,
	PHASE_ID				SMALLINT NOT NULL,
    TEMPLATE_ID		        BIGINT NOT NULL,
    EMPLOYEE_ID				SMALLINT NOT NULL,
	ASSIGNED_BY				SMALLINT NOT NULL,
	ASSIGNED_AT	            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	STATUS			SMALLINT NOT NULL DEFAULT 0,
	SCORE				DECIMAL(3,2) NOT NULL DEFAULT 0,

	PRIMARY KEY (ID),
	FOREIGN KEY (PHASE_ID) REFERENCES appr_phase (ID),
	FOREIGN KEY (TEMPLATE_ID) REFERENCES template (ID),
	FOREIGN KEY (EMPLOYEE_ID) REFERENCES employee (EMPLOYEE_ID),
	UNIQUE (PHASE_ID,TEMPLATE_ID,EMPLOYEE_ID),
	CHECK (ID > 0),
	CHECK (PHASE_ID > 0),
	CHECK (TEMPLATE_ID > 0),
	CHECK (EMPLOYEE_ID > 0),
	CHECK (EMPLOYEE_ID != ASSIGNED_BY),
	CHECK (SCORE >= 0)
);

-- #######################################################
-- Assessment Module
-- #######################################################
CREATE TABLE assess_header (
	ID				BIGINT NOT NULL,
	ASSIGN_ID		BIGINT NOT NULL,
	STATUS			SMALLINT NOT NULL DEFAULT 0,
	ASSESS_DATE		DATE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	ASSESSED_BY		SMALLINT NOT NULL,

	PRIMARY KEY (ID),
	FOREIGN KEY (ASSIGN_ID) REFERENCES phase_assign (ID),
	UNIQUE(ASSIGN_ID, STATUS),
	CHECK (ID > 0),
	CHECK (ASSESSED_BY >= 0)
);

CREATE TABLE assess_detail (
	ID					BIGINT NOT NULL,
	ASSESS_HEADER_ID	BIGINT NOT NULL,
	TEMPLATE_HEADER_ID	SMALLINT NOT NULL,
	RATING				DECIMAL(2,1) NOT NULL,
	COMMENTS			text NOT NULL,
	SCORE				DECIMAL(3,2) NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (ASSESS_HEADER_ID) REFERENCES assess_header (ID),
	FOREIGN KEY (TEMPLATE_HEADER_ID) REFERENCES template_header (ID),
	UNIQUE(ASSESS_HEADER_ID, TEMPLATE_HEADER_ID),
	CHECK (ID > 0),
	CHECK (RATING >= 0),
	CHECK (SCORE >= 0),
	CHECK (COMMENTS <> '')
);

CREATE TABLE email_template (
	ID			SMALLINT NOT NULL,
	NAME		VARCHAR(100) NOT NULL,
	FILE_NAME	VARCHAR(100) NOT NULL,
	SUBJECT 	VARCHAR(200) NOT NULL,
	BUTTON_URL	VARCHAR(200) NOT NULL,
	FROM_MAIL	VARCHAR(100) NULL,
	TO_MAIL	    VARCHAR(100) NULL,
	CC_MAIL	    VARCHAR(100) NULL,
	PRIMARY KEY (ID),
	UNIQUE (NAME),
	CHECK (ID > 0),
	CHECK (NAME <> '')
);

-- ###############################################################################################
-- ###############################################################################################
-- PERMISSIONS
-- ###############################################################################################
-- ###############################################################################################
GRANT CONNECT ON DATABASE SV_PMS TO sv_pms_app;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO sv_pms_app;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO sv_pms_app;


-- ###############################################################################################
-- ###############################################################################################
-- INSERT SCRIPTS
-- ###############################################################################################
-- ###############################################################################################
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

---- ######################### email_template for TEST
insert into email_template values(1,'KICK_OFF','AppraisalKickoff.ftl','Appraisal Kick off - {0} !!!','http://pms.softvision.com/','HR-App','srikanth.kumar@softvision.com',null);
insert into email_template values(2,'EMPLOYEE_ENABLE','EmploeeEnable.ftl','[{0},{1}]   [{2}]  Self Appraisal form available!','http://pms.softvision.com/',null,null,null);
insert into email_template values(3,'EMPLOYEE_SUBMITED','EmployeeSubmitted.ftl','[{0},{1}]   [{2}] Self Appraisal submitted!!','http://pms.softvision.com/',null,null,null);
insert into email_template values(4,'MGR_TO_EMP_REMAINDER','ManagerToEmploeeRemainder.ftl','[{0},{1}]  [{2}] Self Appraisal pending Remainder!!','http://pms.softvision.com/',null,null,null);
insert into email_template values(5,'MANAGER_REVIEW','ManagerReviewCompleted.ftl','[{0},{1}]   [{2}] Self Appraisal Reviewed!!','http://pms.softvision.com/',null,null,null);
insert into email_template values(6,'MANAGER_FROZEN','ManagerReviewFrozen.ftl','[{0},{1}]   [{2}] Self Appraisal Concluded!!','http://pms.softvision.com/',null,null,null);
insert into email_template values(7,'EMP_ACCEPTED','EmploeeAccepted.ftl','[{0},{1}]   [{2}] Self Appraisal Accepted!!','http://pms.softvision.com/',null,null,null);
insert into email_template values(8,'EMP_REJECTED','EmploeeRejected.ftl','[{0},{1}]   [{2}] Self Appraisal Rejected!!','http://pms.softvision.com/',null,null,null);
insert into email_template values(9,'UPDATE_REVIEW','UpdateReviewEmp.ftl','[{0},{1}]   [{2}] Self Appraisal Re visited!!','http://pms.softvision.com/',null,null,null);
insert into email_template values(10,'HR_TO_EMP_REM','HrToEmploeeRemainder.ftl','[{0},{1}]   [{2}] Self Appraisal pending!!','http://pms.softvision.com/',null,null,null);
insert into email_template values(11,'HR_TO_MGR_REM','HrToManagerRemainder.ftl','[{0},{1}]   [{2}] Self Appraisal Review pending!!','http://pms.softvision.com/',null,null,null);
insert into email_template values(12,'CYCLE_CONCLUDE','Appraisalconcluded.ftl','Appraisal {0} has been conclude !!!','http://pms.softvision.com/','HR-App',' srikanth.kumar@softvision.com',null);
insert into email_template values(13,'CHANGE_MGR','ChangeManager.ftl','[{0},{1}]   [{2}] Assign manager change!!','http://pms.softvision.com/','HR-App','srikanth.kumar@softvision.com',null);
