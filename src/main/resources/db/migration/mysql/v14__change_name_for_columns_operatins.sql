ALTER TABLE `planned_operation` CHANGE `is_finished` `finished` BIT(1) NOT NULL;
ALTER TABLE `planned_operation` CHANGE `is_active` `active` BIT(1) NOT NULL;