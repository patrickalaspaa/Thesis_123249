CREATE TABLE Images (
  Id int(11) NOT NULL DEFAULT 0,
  Name varchar(250) DEFAULT NULL,
  SpotId int(11) DEFAULT NULL,
  PRIMARY KEY (Id),
  KEY FK_Image_Spot (SpotId),
  CONSTRAINT FK_Image_Spot FOREIGN KEY (SpotId) REFERENCES Spot (Id)
)
