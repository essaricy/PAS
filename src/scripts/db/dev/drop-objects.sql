-- #######################################################
-- ROle Module
-- #######################################################
DROP SEQUENCE IF EXISTS emp_role_id_seq CASCADE;

-- #######################################################
-- appr Module
-- #######################################################
DROP SEQUENCE IF EXISTS appr_cycle_id_seq CASCADE;
DROP SEQUENCE IF EXISTS appr_phase_id_seq CASCADE;

-- #######################################################
-- Goals Module
-- #######################################################
DROP SEQUENCE IF EXISTS goal_id_seq CASCADE;
DROP SEQUENCE IF EXISTS goal_param_id_seq CASCADE;

-- #######################################################
-- Templates Module
-- #######################################################
DROP SEQUENCE IF EXISTS template_id_seq CASCADE;
DROP SEQUENCE IF EXISTS template_header_id_seq CASCADE;
DROP SEQUENCE IF EXISTS template_detail_id_seq CASCADE;

-- #######################################################
-- Assignment Module
-- #######################################################
DROP SEQUENCE IF EXISTS cycle_assign_id_seq CASCADE;
DROP SEQUENCE IF EXISTS phase_assign_id_seq CASCADE;

-- #######################################################
-- Assessment Module
-- #######################################################
DROP SEQUENCE IF EXISTS assess_header_id_seq CASCADE;
DROP SEQUENCE IF EXISTS assess_detail_id_seq CASCADE;




-- #######################################################
-- Assessment Module
-- #######################################################
DROP TABLE IF EXISTS assess_detail CASCADE;
DROP TABLE IF EXISTS assess_header CASCADE;

-- #######################################################
-- Assignment Module
-- #######################################################
DROP TABLE IF EXISTS phase_assign CASCADE;
DROP TABLE IF EXISTS cycle_assign CASCADE;

-- #######################################################
-- Employee Module
-- #######################################################
DROP TABLE IF EXISTS employee_role CASCADE;
DROP TABLE IF EXISTS role CASCADE;
DROP TABLE IF EXISTS employee CASCADE;

-- #######################################################
-- Template Module
-- #######################################################
DROP TABLE IF EXISTS template CASCADE;
DROP TABLE IF EXISTS template_detail CASCADE;
DROP TABLE IF EXISTS template_header CASCADE;

-- #######################################################
-- Goal Module
-- #######################################################
DROP TABLE IF EXISTS goal_param CASCADE;
DROP TABLE IF EXISTS goal CASCADE;

-- #######################################################
-- Appraisal Module
-- #######################################################
DROP TABLE IF EXISTS appr_cycle CASCADE;
DROP TABLE IF EXISTS appr_phase CASCADE;

--
DROP TABLE IF EXISTS email_template CASCADE;