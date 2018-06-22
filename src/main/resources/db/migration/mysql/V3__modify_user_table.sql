ALTER TABLE `application_users`
ADD COLUMN `enabled` VARCHAR(45) NOT NULL DEFAULT 0 AFTER `password`;
