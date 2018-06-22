CREATE TABLE `money_container` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_polish_ci DEFAULT NULL,
  `amount` decimal(9,2) DEFAULT '0.00',
  `type` varchar(45) COLLATE utf8_polish_ci DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;
