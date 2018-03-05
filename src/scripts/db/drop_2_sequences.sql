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
DROP SEQUENCE IF EXISTS emp_cycle_assign_id_seq CASCADE;
DROP SEQUENCE IF EXISTS emp_phase_assign_id_seq CASCADE;

-- #######################################################
-- Assessment Module
-- #######################################################
DROP SEQUENCE IF EXISTS emp_cycle_assess_id_seq CASCADE;
DROP SEQUENCE IF EXISTS emp_phase_assess_id_seq CASCADE;
