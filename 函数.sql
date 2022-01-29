/* currval函数 */
DROP FUNCTION IF EXISTS `currval` ;
DELIMITER $$
CREATE DEFINER=`root`@`%` FUNCTION `currval`(seq_name VARCHAR(50)) RETURNS int(11)
BEGIN
 DECLARE VALUE INTEGER;
 SET VALUE=0;
 SELECT current_value INTO VALUE FROM sys_sequence WHERE NAME=seq_name;
 RETURN VALUE;
END;
$$
DELIMITER ;


/*`nextval`;函数  */ 


DROP FUNCTION IF EXISTS `nextval`;

DELIMITER $$
CREATE DEFINER=`root`@`%` FUNCTION `nextval`(seq_name varchar(50)) RETURNS int(11)
BEGIN
UPDATE sys_sequence
SET CURRENT_VALUE = CURRENT_VALUE + INCREMENT
where name=seq_name;
return currval(seq_name);
END;
