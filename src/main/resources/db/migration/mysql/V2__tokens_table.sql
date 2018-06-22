CREATE TABLE `verification_tokens` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `token` VARCHAR(45) NULL,
  `user_id` INT NULL,
  `expiry_date` DATETIME NULL,
  PRIMARY KEY (`id`));