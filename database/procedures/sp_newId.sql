DELIMITER $$
CREATE DEFINER=`patrick`@`%` PROCEDURE `sp_newId`()
BEGIN
	CALL sp_getUniqueIdentifier(@id);
	SELECT @Id AS id;
END$$
DELIMITER ;
