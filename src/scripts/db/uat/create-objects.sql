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
	NAME		VARCHAR(50) NOT NULL,
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

---- ######################### email_template for UAT
insert into email_template values(1,'KICK_OFF','AppraisalKickoff.ftl','Appraisal Kick off - {0} !!!','http://10.100.1.219/','pms@softvision.com','pms@softvision.com',null);
insert into email_template values(2,'EMPLOYEE_ENABLE','EmployeeEnable.ftl','[{0},{1}]   [{2}] Self Appraisal Form Available!!','http://10.100.1.219/',null,null,null);
insert into email_template values(3,'EMPLOYEE_SUBMITED','EmployeeSubmitted.ftl','[{0},{1}] [{2}] Self Appraisal Submitted!!','http://10.100.1.219/',null,null,null);
insert into email_template values(4,'MGR_TO_EMP_REMINDER','ManagerToEmployeeReminder.ftl','[{0},{1}] [{2}] Self Appraisal Pending Reminder!!','http://10.100.1.219/',null,null,null);
insert into email_template values(5,'MANAGER_REVIEW','ManagerReviewCompleted.ftl','[{0},{1}] [{2}] Self Appraisal Reviewed!!','http://10.100.1.219/',null,null,null);
insert into email_template values(6,'MANAGER_FROZEN','ManagerReviewFrozen.ftl','[{0},{1}] [{2}] Self Appraisal Concluded!!','http://10.100.1.219/',null,null,null);
insert into email_template values(7,'EMP_ACCEPTED','EmployeeAccepted.ftl','[{0},{1}]   [{2}] Self Appraisal Accepted!!','http://10.100.1.219/',null,null,'pms@softvision.com');
insert into email_template values(8,'EMP_REJECTED','EmployeeRejected.ftl','[{0},{1}]   [{2}] Self Appraisal Rejected!!','http://10.100.1.219/',null,null,'pms@softvision.com');
insert into email_template values(9,'UPDATE_REVIEW','UpdateReviewEmp.ftl','[{0},{1}]   [{2}] Self Appraisal Re-visited!!','http://10.100.1.219/',null,null,'pms@softvision.com');
insert into email_template values(10,'HR_TO_EMP_REM','HrToEmployeeReminder.ftl','[{0},{1}] [{2}] Self Appraisal Pending Reminder!!','http://10.100.1.219/',null,null,null);
insert into email_template values(11,'HR_TO_MGR_REM','HrToManagerReminder.ftl','[{0},{1}] [{2}] Self Appraisal Review Pending!!','http://10.100.1.219/',null,null,null);
insert into email_template values(12,'CYCLE_CONCLUDE','AppraisalConcluded.ftl','Appraisal {0} Has Been Conclude!!!','','pms@softvision.com','pms@softvision.com',null);
insert into email_template values(13,'CHANGE_MGR','ChangeManager.ftl','[{0},{1}]   [{2}] Assign Manager Changed!!','http://10.100.1.219/',null,null,null);
