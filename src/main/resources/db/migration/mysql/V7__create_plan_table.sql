CREATE TABLE `planned_operation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` decimal(19,2) NOT NULL,
  `days` int(11) NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 NOT NULL,
  `due_date` date NOT NULL,
  `periodicity` varchar(255) CHARACTER SET utf8 NOT NULL,
  `planed_type` varchar(255) CHARACTER SET utf8 NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `money_holder_id` bigint(20) NOT NULL,
  `is_active` bit(1) NOT NULL,
  `is_finished` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5ivb4qj2hq4qjkxay7m8qqknr` (`user_id`)
)DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;