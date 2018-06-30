CREATE TABLE `HOME_BUDGET`.`investment` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) NOT NULL,
  `start_date` DATE NULL,
  `amount` DECIMAL(9,2) NULL,
  `percentage` DECIMAL(9,2) NULL,
  `end_date` DATE NULL,
  `length_days` INT NULL,
  `length` VARCHAR(45) NULL,
  `isActive` bit(1) NOT NULL,
  PRIMARY KEY (`id`));