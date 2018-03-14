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
	CHECK (STATUS IN ('DRAFT', 'ACTIVE', 'COMPLETE') )
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
-- Competency Assessment
CREATE TABLE goal (
	ID			SMALLINT NOT NULL,
	NAME		VARCHAR(40) NOT NULL,
	PRIMARY KEY (ID),
	UNIQUE (NAME),
	CHECK (ID > 0),
	CHECK (NAME <> '')
);
-- Competency Assessment Parameters
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
-- Competency Assessment Template
CREATE TABLE template (
	ID			BIGINT NOT NULL,
	NAME		VARCHAR(100) NOT NULL,
	UPDATED_BY	VARCHAR(40) NOT NULL,
	UPDATED_AT	TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

	PRIMARY KEY (ID),
	UNIQUE (NAME),
	CHECK (ID > 0),
	CHECK (NAME <> ''),
	CHECK (UPDATED_BY <> '')
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
	CHECK (BAND <> ''),
	CHECK (DESIGNATION <> '')
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
-- Assignment Module
-- #######################################################
-- drop table emp_cycle_assign;
CREATE TABLE emp_cycle_assign (
	ID				BIGINT NOT NULL,
    CYCLE_ID		SMALLINT NOT NULL,
    TEMPLATE_ID		BIGINT NOT NULL,
    EMPLOYEE_ID		SMALLINT NOT NULL,
	ASSIGNED_BY		SMALLINT NOT NULL,
	ASSIGNED_AT	    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	STATUS			SMALLINT NOT NULL DEFAULT 0,
	PRIMARY KEY (ID),
	FOREIGN KEY (CYCLE_ID) REFERENCES appr_cycle (ID),
	FOREIGN KEY (TEMPLATE_ID) REFERENCES template (ID),
	FOREIGN KEY (EMPLOYEE_ID) REFERENCES employee (EMPLOYEE_ID),
	UNIQUE (CYCLE_ID,TEMPLATE_ID,EMPLOYEE_ID),

	CHECK (ID > 0),
	CHECK (CYCLE_ID > 0),
	CHECK (TEMPLATE_ID > 0),
	CHECK (EMPLOYEE_ID > 0),
	CHECK (EMPLOYEE_ID != ASSIGNED_BY)
);

-- drop table emp_phase_assign;
CREATE TABLE emp_phase_assign (
	ID						BIGINT NOT NULL,
	PHASE_ID				SMALLINT NOT NULL,
    TEMPLATE_ID		        BIGINT NOT NULL,
    EMPLOYEE_ID				SMALLINT NOT NULL,
	ASSIGNED_BY				SMALLINT NOT NULL,
	ASSIGNED_AT	            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	STATUS			SMALLINT NOT NULL DEFAULT 0,

	PRIMARY KEY (ID),
	FOREIGN KEY (PHASE_ID) REFERENCES appr_phase (ID),
	FOREIGN KEY (TEMPLATE_ID) REFERENCES template (ID),
	FOREIGN KEY (EMPLOYEE_ID) REFERENCES employee (EMPLOYEE_ID),
	UNIQUE (PHASE_ID,TEMPLATE_ID,EMPLOYEE_ID),
	CHECK (ID > 0),
	CHECK (PHASE_ID > 0),
	CHECK (TEMPLATE_ID > 0),
	CHECK (EMPLOYEE_ID > 0),
	CHECK (EMPLOYEE_ID != ASSIGNED_BY)
	--CHECK (STATUS IN (0, 10, 20, 30, 40))
);

-- #######################################################
-- Assessment Module
-- #######################################################
CREATE TABLE cycle_assess_header (
	ID				BIGINT NOT NULL,
	ASSIGN_ID		BIGINT NOT NULL,
	STATUS			SMALLINT NOT NULL DEFAULT 0,
	ASSESS_DATE		DATE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	ASSESSED_BY		SMALLINT NOT NULL,

	PRIMARY KEY (ID),
	FOREIGN KEY (ASSIGN_ID) REFERENCES emp_cycle_assign (ID),
	UNIQUE(ASSIGN_ID, STATUS),
	CHECK (ID > 0),
	CHECK (ASSESSED_BY >= 0)
);

CREATE TABLE cycle_assess_detail (
	ID					BIGINT NOT NULL,
	ASSESS_HEADER_ID	BIGINT NOT NULL,
	TEMPLATE_HEADER_ID	SMALLINT NOT NULL,
	RATING				DECIMAL(2,1) NOT NULL,
	COMMENTS			VARCHAR(600) NOT NULL,
	SCORE				DECIMAL(3,2) NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (ASSESS_HEADER_ID) REFERENCES cycle_assess_header (ID),
	FOREIGN KEY (TEMPLATE_HEADER_ID) REFERENCES template_header (ID),
	UNIQUE(ASSESS_HEADER_ID, TEMPLATE_HEADER_ID),
	CHECK (ID > 0),
	CHECK (RATING >= 0),
	CHECK (SCORE >= 0),
	CHECK (COMMENTS <> '')
);

CREATE TABLE phase_assess_header (
	ID				BIGINT NOT NULL,
	ASSIGN_ID		BIGINT NOT NULL,
	STATUS			SMALLINT NOT NULL DEFAULT 0,
	ASSESS_DATE		DATE NOT NULL DEFAULT CURRENT_TIMESTAMP,
	ASSESSED_BY		SMALLINT NOT NULL,

	PRIMARY KEY (ID),
	FOREIGN KEY (ASSIGN_ID) REFERENCES emp_phase_assign (ID),
	UNIQUE(ASSIGN_ID, STATUS),
	CHECK (ID > 0),
	CHECK (ASSESSED_BY >= 0)
);

CREATE TABLE phase_assess_detail (
	ID					BIGINT NOT NULL,
	ASSESS_HEADER_ID	BIGINT NOT NULL,
	TEMPLATE_HEADER_ID	SMALLINT NOT NULL,
	RATING				DECIMAL(2,1) NOT NULL,
	COMMENTS			VARCHAR(600) NOT NULL,
	SCORE				DECIMAL(3,2) NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (ASSESS_HEADER_ID) REFERENCES phase_assess_header (ID),
	FOREIGN KEY (TEMPLATE_HEADER_ID) REFERENCES template_header (ID),
	UNIQUE(ASSESS_HEADER_ID, TEMPLATE_HEADER_ID),
	CHECK (ID > 0),
	CHECK (RATING >= 0),
	CHECK (SCORE >= 0),
	CHECK (COMMENTS <> '')
);
