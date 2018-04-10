ALTER TABLE employee ADD COLUMN ACTIVE char(1) NOT NULL DEFAULT 'Y';
ALTER TABLE employee ADD COLUMN DEPARTMENT varchar(80) NOT NULL DEFAULT 'default';
ALTER TABLE employee ADD COLUMN DIVISION varchar(16) NOT NULL DEFAULT 'default';
ALTER TABLE employee ADD COLUMN ORG varchar(10) NOT NULL DEFAULT 'default';

ALTER TABLE employee ADD CONSTRAINT employee_active_check check IN ('Y', 'N');

-- Execute these statements after Sync Employees

-- ALTER TABLE employee ALTER COLUMN DEPARTMENT DROP DEFAULT;
-- ALTER TABLE employee ALTER COLUMN DIVISION DROP DEFAULT;
-- ALTER TABLE employee ALTER COLUMN ORG DROP DEFAULT;