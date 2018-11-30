CREATE TABLE Spot (
  Id int(11) NOT NULL,
  Name varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  Address varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  lat float DEFAULT NULL,
  lng float DEFAULT NULL,
  type int(11) NOT NULL,
  PRIMARY KEY (Id),
  CONSTRAINT FK_Type_Spot FOREIGN KEY (type) REFERENCES SpotType (Id)
) 
