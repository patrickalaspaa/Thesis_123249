DELIMITER $$
CREATE DEFINER=`patrick`@`%` PROCEDURE `sp_getUniqueIdentifier`(OUT param1 INT)
BEGIN
	SET SQL_SAFE_UPDATES = 0;
    
    UPDATE UniqueId
    SET Id = Id + 1;
    
    SELECT Id INTO param1 FROM UniqueId;
END$$
DELIMITER ;
