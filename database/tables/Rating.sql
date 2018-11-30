CREATE TABLE Rating (
  SpotId int(11) NOT NULL DEFAULT 0,
  Rating int(11) DEFAULT NULL,
  PersonId int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (SpotId,PersonId),
  CONSTRAINT FK_Rating_PersonId FOREIGN KEY (PersonId) REFERENCES Person (Id),
  CONSTRAINT FK_Rating_SpotId FOREIGN KEY (SpotId) REFERENCES Spot (Id)
)
