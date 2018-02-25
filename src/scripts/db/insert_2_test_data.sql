insert into appr_cycle (ID, NAME, START_DATE, END_DATE, CUTOFF_DATE) values (nextval('appr_cycle_ID_SEQ'), '2017-18', '2017-01-01', '2018-04-30', '2016-10-30');
insert into appr_cycle (ID, NAME, START_DATE, END_DATE, CUTOFF_DATE) values (nextval('appr_cycle_ID_SEQ'), '2018', '2018-05-01', '2018-12-31', '2017-10-30');

insert into appr_phase (ID, CYCLE_ID, NAME, START_DATE, END_DATE) values (nextval('appr_phase_ID_SEQ'), (select ID from appr_cycle where name='2017-18'), 'FY', '2017-01-01', '2018-04-30');
insert into appr_phase (ID, CYCLE_ID, NAME, START_DATE, END_DATE) values (nextval('appr_phase_ID_SEQ'), (select ID from appr_cycle where name='2018'), 'Q1', '2018-05-01', '2018-06-30');
insert into appr_phase (ID, CYCLE_ID, NAME, START_DATE, END_DATE) values (nextval('appr_phase_ID_SEQ'), (select ID from appr_cycle where name='2018'), 'Q2', '2018-07-01', '2018-09-30');
insert into appr_phase (ID, CYCLE_ID, NAME, START_DATE, END_DATE) values (nextval('appr_phase_ID_SEQ'), (select ID from appr_cycle where name='2018'), 'Q3', '2018-10-01', '2018-12-31');


INSERT INTO template(id, name, updated_by) VALUES (nextval('template_id_seq'), 'Template 1', 'basavaraju.n');
INSERT INTO template_header(id, TEMPLATE_ID, ca_id, WEIGHTAGE)
	VALUES (nextval('template_header_id_seq'), (select ID from template where name='Template 1'), (select id from goal_ca where name='Client Orientation/Customer Focus'), 50);
INSERT INTO template_header(id, TEMPLATE_ID, ca_id, WEIGHTAGE)
	VALUES (nextval('template_header_id_seq'), (select ID from template where name='Template 1'), (select id from goal_ca where name='Project Management'), 50);

INSERT INTO template_detail(id, header_ID, cap_id) VALUES (nextval('template_detail_id_seq'), 1, (select id from goal_cap where name='Knowledge of Softvision and Customer processes (software development processes)'));
INSERT INTO template_detail(id, header_ID, cap_id) VALUES (nextval('template_detail_id_seq'), 1, (select id from goal_cap where name='Number of customer accolades'));

INSERT INTO template_detail(id, header_ID, cap_id) VALUES (nextval('template_detail_id_seq'), 2, (select id from goal_cap where name='Ontime resolutions'));
INSERT INTO template_detail(id, header_ID, cap_id) VALUES (nextval('template_detail_id_seq'), 2, (select id from goal_cap where name='Ontime zero defect deliveries'));