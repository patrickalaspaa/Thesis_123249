CREATE TABLE Person (
  Id int(11) NOT NULL AUTO_INCREMENT,
  UserName varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  Hash varchar(10) CHARACTER SET utf8 DEFAULT NULL,
  Password varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  Email varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  Admin bit CHARACTER DEFAULT 0,
  PRIMARY KEY (Id),
  UNIQUE KEY U_Person_Email (Email),
  UNIQUE KEY U_Person_UserName (UserName)
) 
