CREATE TABLE `planned_budget` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `planned` decimal(19,2) DEFAULT NULL,
  `spend` decimal(19,2) DEFAULT NULL,
  `category` varchar(255) COLLATE utf8_polish_ci DEFAULT NULL,
  `date` date DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpmvcvfk6cc56rf44ks11s2jl7` (`user_id`)
)DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;