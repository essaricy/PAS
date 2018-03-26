-- #######################################################
-- ROle Module
-- #######################################################
CREATE SEQUENCE emp_role_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 1000	CACHE 1;

-- #######################################################
-- appr Module
-- #######################################################
CREATE SEQUENCE appr_cycle_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 100000	CACHE 1;
CREATE SEQUENCE appr_phase_id_seq   INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 500000	CACHE 1;

-- #######################################################
-- Goals Module
-- #######################################################
CREATE SEQUENCE goal_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 1000	CACHE 1;
CREATE SEQUENCE goal_param_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 10000	CACHE 1;

-- #######################################################
-- Templates Module
-- #######################################################
CREATE SEQUENCE template_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 10000	CACHE 1;
CREATE SEQUENCE template_header_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 1000000	CACHE 1;
CREATE SEQUENCE template_detail_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 100000000	CACHE 1;

-- #######################################################
-- Assignment Module
-- #######################################################
CREATE SEQUENCE cycle_assign_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 100000000	CACHE 1;
CREATE SEQUENCE phase_assign_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 10000000000	CACHE 1;

-- #######################################################
-- Assessment Module
-- #######################################################
CREATE SEQUENCE assess_header_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 10000000000	CACHE 1;
CREATE SEQUENCE assess_detail_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 10000000000	CACHE 1;
