ALTER TABLE utilisateur DROP CONSTRAINT IF EXISTS utilisateur_role_check;

UPDATE utilisateur SET role = 'ROLE_EMPLOYE' WHERE role = 'EMPLOYE';
UPDATE utilisateur SET role = 'ROLE_MANAGER' WHERE role = 'MANAGER';
UPDATE utilisateur SET role = 'ROLE_ADMIN' WHERE role = 'ADMIN';

ALTER TABLE utilisateur
ADD CONSTRAINT utilisateur_role_check
CHECK (role IN ('ROLE_EMPLOYE', 'ROLE_MANAGER', 'ROLE_ADMIN'));