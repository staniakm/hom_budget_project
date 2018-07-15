CREATE TABLE `currency` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `link` VARCHAR(100) NOT NULL ,
  `currency` VARCHAR(45) NOT NULL,
  `code` VARCHAR(5) NOT NULL,
  `date` DATE NOT NULL,
  `rate` DECIMAL(9,4) NOT NULL,
  `last_update` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`));


CREATE TABLE `user_currency` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) NOT NULL,
  `currency_list` varchar(1000) null,
  PRIMARY KEY (`id`));