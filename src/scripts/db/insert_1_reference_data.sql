-- select * from goal_ca;
INSERT INTO goal_ca(id, name) VALUES (nextval('goal_ca_id_seq'), 'Client Orientation/Customer Focus');
INSERT INTO goal_ca(id, name) VALUES (nextval('goal_ca_id_seq'), 'Project Management');
INSERT INTO goal_ca(id, name) VALUES (nextval('goal_ca_id_seq'), 'Leadership');
INSERT INTO goal_ca(id, name) VALUES (nextval('goal_ca_id_seq'), 'Communication');
INSERT INTO goal_ca(id, name) VALUES (nextval('goal_ca_id_seq'), 'Business Domain');
INSERT INTO goal_ca(id, name) VALUES (nextval('goal_ca_id_seq'), 'Technical Solutions');
INSERT INTO goal_ca(id, name) VALUES (nextval('goal_ca_id_seq'), 'Process Awareness & Compliance');
INSERT INTO goal_ca(id, name) VALUES (nextval('goal_ca_id_seq'), 'Corporate Initiatives');


INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Client Orientation/Customer Focus'),
	'Establishes and maintains effective relationship with both internal and external customers and gains their trust');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Client Orientation/Customer Focus'),
	'Understands and  Proactively addresses customer needs in a timely manner by following the Client/Softvision Process');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Client Orientation/Customer Focus'),
	'Knowledge of Softvision and Customer processes (software development processes)');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Client Orientation/Customer Focus'),
	'Number of customer accolades');

INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Project Management'),
	'Ability to identify, plan, procure and utilize resources within the scope of the project');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Project Management'),
	'Ability to identify, mitigate and manage risks in the project. a)  Planning and Control b)  Resource Management');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Project Management'),
	'Knowledge of PM basics like Initiation, Planning, Monitoring, Negotiation skills, budgeting,Tracking and Closure');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Project Management'),
	'Concepts and application of PM disciplines like estimation, scope, time, cost and risk mgmt');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Project Management'),
	'Application of Softvision PM Process and Client PM Process');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Project Management'),
	'Process compliance in terms of non compliance');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Project Management'),
	'Consitency in communicatoion - gaps btw onshore communication etc - ');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Project Management'),
	'Frequency of commuinction within the team - internal and external');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Project Management'),
	'No of Delivery Escalations(anything greter than zero)');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Project Management'),
	'Ontime resolutions');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Project Management'),
	'Ontime zero defect deliveries');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Project Management'),
	'Initiating project activities as per QMS Process');

INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Leadership'),
	'People Management - timely closure of employee concerns');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Leadership'),
	'Mentoring, motivating and communicating with the team');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Leadership'),
	'Conducting Root cause analysis and implementing preventive & corrective actions on the conflict issues/defects/customer complaints etc at the Project Level');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Leadership'),
	'Accountability and Transperancy');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Leadership'),
	'Insightfull and Innovation');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Leadership'),
	'Team Acceptance and dynamics');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Leadership'),
	'Acting as point of contact for service issues that need to be or have been escalated (Project level)');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Leadership'),
	'Initiating Demos/ Walkthroughs / discussions with the client to ensure that the team is getting clarifications on issues in Customer supplied work products');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Leadership'),
	'Defining best practices in the critical evaluation and selection and / or development of the development methods, processes, best practices, tools, software components and hardware requirements of the applications and data.');

INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Communication'),
	'Good communication');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Communication'),
	'Knowledge of Spoken and Written English');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Communication'),
	'Listening Skills, clarity and comprehension');

INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Business Domain'),
	'Ability to understand and align with customer''s business / technology domain');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Business Domain'),
	'Knowledge of retail domain');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Business Domain'),
	'Process Abilities in handling 2 to 3 retail project');

INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Technical Solutions'),
	'Understanding the design & developing the code');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Technical Solutions'),
	'Creating, executing Unit Test cases and documenting results');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Technical Solutions'),
	'Fostering team building by sharing Technical knowledge, highlighting problems or critical situations to the Team');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Technical Solutions'),
	'Resolving production/post production problems coordinating with development/QA team');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Technical Solutions'),
	'Analysis of existing code/functionality and suggesting the improvement in performance');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Technical Solutions'),
	'Reviewing the work products on SDLC/STLC phases and document');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Technical Solutions'),
	'Ensuring effective resolution of complaints, problems tickets raised & taking responsibility for the decisions taken for the issues and escalation');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Technical Solutions'),
	'Ensuring effective resolution & taking responsibility on the improvement in backlog management');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Technical Solutions'),
	'Conducting Root cause analysis and implementing preventive & corrective actions on the conflict issues/defects/customer complaints etc at the Project Level');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Technical Solutions'),
	'Coordinating & Participating in Bug Validation/Defect review meetings');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Technical Solutions'),
	'Executing project activities');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Technical Solutions'),
	'Identifying, tracking and managing all risks and manage the mitigation plan at Project Level');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Technical Solutions'),
	'Ensuring quality and timely completion and delivery of the deliverables defined in SoW');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Technical Solutions'),
	'Providing direction on strategic technical issues at ORG level');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Technical Solutions'),
	'Executing the task to completion as per Work Breakdown Structure and schedules in line with the Project Plan');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Technical Solutions'),
	'Transforming, Implementing, tuning and maintaining the databases used by the application');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Technical Solutions'),
	'Specifying, placing and modifying data (Database) to support the development of applications solutions, and being responsible for the data integrity & security');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Technical Solutions'),
	'Analysis of new software product releases and the impact of these new releases onto application and come up with the plan to implement these releases');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Technical Solutions'),
	'Analysis of new Database product releases and the impact of these new releases onto application and come up with the plan to implement these releases');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Technical Solutions'),
	'Translating the client technical requirements for development into IT solutions that fit into a new technical architecture');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Technical Solutions'),
	'Establishing a regular and comprehensive schedule of project meetings, submission of reports, reviews and presentations as part of the project communication plan');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Technical Solutions'),
	'Supporting the (Test)Lead to establish a regular and comprehensive schedule of project meetings, submission of reports, reviews and presentations as part of the project communication plan');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Technical Solutions'),
	'Identifying & setting up of test environment');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Technical Solutions'),
	'Working with Test Lead/Development counterparts to help plan, build, operate and maintain the testing environment');

INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Process Awareness & Compliance'),
	'Following the defined Softvision/Client Process, Standards, Templates, Checklists, Guidelines for the SDLC/STLC phases in an engagement');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Process Awareness & Compliance'),
	'Adhering to the change/configuration management process');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Process Awareness & Compliance'),
	'Adherence to company policies');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Process Awareness & Compliance'),
	'Awareness to SHARP,ISMS/SOC processess');
INSERT INTO goal_cap(ID, CA_ID, NAME) VALUES (nextval('goal_cap_id_seq'),
	(select id from goal_ca where name='Process Awareness & Compliance'),
	'CMMi level 5 process ');