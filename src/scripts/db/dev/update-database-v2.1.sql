INSERT INTO role(id, role_name) VALUES (4, 'Support');

INSERT INTO employee_role(id, role_id, employee_id) VALUES (nextval('emp_role_id_seq'), 4, 1136);
INSERT INTO employee_role(id, role_id, employee_id) VALUES (nextval('emp_role_id_seq'), 4, 2388);