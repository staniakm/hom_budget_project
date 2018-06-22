ALTER TABLE `expense`
  ADD COLUMN `money_holder_id` BIGINT(20) NOT NULL AFTER `amount`;

ALTER TABLE `income`
  ADD COLUMN `money_holder_id` BIGINT(20) NOT NULL AFTER `amount`;
