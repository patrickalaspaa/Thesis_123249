CREATE TABLE Session (
  id int(11) NOT NULL DEFAULT '0',
  personId int(11) NOT NULL,
  sessionDate datetime DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_session_person FOREIGN KEY (personId) REFERENCES Person (Id)
) 
