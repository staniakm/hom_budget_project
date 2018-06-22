ALTER TABLE `HOME_BUDGET`.`expense`
  ADD COLUMN `money_holder_id` BIGINT(20) NOT NULL AFTER `amount`;

ALTER TABLE `HOME_BUDGET`.`income`
  ADD COLUMN `money_holder_id` BIGINT(20) NOT NULL AFTER `amount`;
