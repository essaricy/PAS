-- #######################################################
-- ROle Module
-- #######################################################
CREATE SEQUENCE emp_role_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 9223372036854775807	CACHE 1;

-- #######################################################
-- appr Module
-- #######################################################
CREATE SEQUENCE appr_cycle_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 9223372036854775807	CACHE 1;
CREATE SEQUENCE appr_phase_id_seq   INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 9223372036854775807	CACHE 1;

-- #######################################################
-- Goals Module
-- #######################################################
CREATE SEQUENCE goal_ca_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 9223372036854775807	CACHE 1;
CREATE SEQUENCE goal_cap_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 9223372036854775807	CACHE 1;

-- #######################################################
-- Templates Module
-- #######################################################
CREATE SEQUENCE template_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 9223372036854775807	CACHE 1;
CREATE SEQUENCE template_header_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 9223372036854775807	CACHE 1;
CREATE SEQUENCE template_detail_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 9223372036854775807	CACHE 1;

-- #######################################################
-- Assignment Module
-- #######################################################
CREATE SEQUENCE emp_cycle_assign_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 9223372036854775807	CACHE 1;
CREATE SEQUENCE emp_phase_assign_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 9223372036854775807	CACHE 1;

-- #######################################################
-- Assessment Module
-- #######################################################
CREATE SEQUENCE emp_cycle_assess_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 9223372036854775807	CACHE 1;
CREATE SEQUENCE emp_phase_assess_id_seq	INCREMENT 1	START 1	MINVALUE 1	MAXVALUE 9223372036854775807	CACHE 1;

