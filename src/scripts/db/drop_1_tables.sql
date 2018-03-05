-- #######################################################
-- Assessment Module
-- #######################################################
DROP TABLE IF EXISTS emp_phase_assess CASCADE;
DROP TABLE IF EXISTS emp_cycle_assess CASCADE;

-- #######################################################
-- Assignment Module
-- #######################################################
DROP TABLE IF EXISTS emp_phase_assign CASCADE;
DROP TABLE IF EXISTS emp_cycle_assign CASCADE;

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
