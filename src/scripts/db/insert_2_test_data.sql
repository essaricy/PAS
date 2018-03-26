-- delete from appr_cycle;
insert into appr_cycle (ID, NAME, START_DATE, END_DATE, CUTOFF_DATE) values (nextval('appr_cycle_ID_SEQ'), '2016', '2016-01-01', '2016-12-31', '2015-10-30');
insert into appr_cycle (ID, NAME, START_DATE, END_DATE, CUTOFF_DATE) values (nextval('appr_cycle_ID_SEQ'), '2017', '2017-01-01', '2017-12-31', '2016-10-30');

-- delete from appr_phase;
insert into appr_phase (ID, CYCLE_ID, NAME, START_DATE, END_DATE) values (nextval('appr_phase_ID_SEQ'), (select ID from appr_cycle where name='2017'), 'Q1', '2017-01-01', '2017-06-30');
insert into appr_phase (ID, CYCLE_ID, NAME, START_DATE, END_DATE) values (nextval('appr_phase_ID_SEQ'), (select ID from appr_cycle where name='2017'), 'Q2', '2017-07-01', '2017-12-31');

-- delete from template
INSERT INTO template(id, name, updated_by) VALUES (nextval('template_id_seq'), 'Template for testing', 'srikanth.kumar');
-- delete from template_header
INSERT INTO template_header(id, TEMPLATE_ID, goal_id, WEIGHTAGE)
	VALUES (nextval('template_header_id_seq'), (select ID from template where name='Template for testing'), (select id from goal where name='Client Orientation/Customer Focus'), 50);
INSERT INTO template_header(id, TEMPLATE_ID, goal_id, WEIGHTAGE)
	VALUES (nextval('template_header_id_seq'), (select ID from template where name='Template for testing'), (select id from goal where name='Project Management'), 50);

-- delete from template_detail
INSERT INTO template_detail(id, header_ID, param_id) VALUES (nextval('template_detail_id_seq'), 12, (select id from goal_param where name='Knowledge of Softvision and Customer processes (software development processes)'));
INSERT INTO template_detail(id, header_ID, param_id) VALUES (nextval('template_detail_id_seq'), 12, (select id from goal_param where name='Number of customer accolades'));

INSERT INTO template_detail(id, header_ID, param_id) VALUES (nextval('template_detail_id_seq'), 13, (select id from goal_param where name='Ontime resolutions'));
INSERT INTO template_detail(id, header_ID, param_id) VALUES (nextval('template_detail_id_seq'), 13, (select id from goal_param where name='Ontime zero defect deliveries'));

insert into employee values(2006,'rohith.ramesh','Rohith','Ramesh','Regular Employee','3Y','senior software engineer','2009-06-14T00:00:00','India');
insert into employee values(1136,'srikanth.kumar','srikanth','kumar','Regular Employee','5Y','Associate Technical Manager','2009-06-14T00:00:00','India');

-- Insert Employees
INSERT INTO employee_role(id, role_id, employee_id) VALUES (nextval('emp_role_id_seq'), 1, 1136);
INSERT INTO employee_role(id, role_id, employee_id) VALUES (nextval('emp_role_id_seq'), 2, 1136);
INSERT INTO employee_role(id, role_id, employee_id) VALUES (nextval('emp_role_id_seq'), 2, 2388);
INSERT INTO employee_role(id, role_id, employee_id) VALUES (nextval('emp_role_id_seq'), 2, 2006);

