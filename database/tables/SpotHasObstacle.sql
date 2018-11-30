CREATE TABLE SpotHasObstacle (
  SpotId int(11) NOT NULL DEFAULT 0,
  ObstacleId int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (SpotId,ObstacleId),
  CONSTRAINT FK_SpoHasObstacle_Obstacle FOREIGN KEY (ObstacleId) REFERENCES Obstacle (Id),
  CONSTRAINT FK_SpoHasObstacle_Spot FOREIGN KEY (SpotId) REFERENCES Spot (Id)
) 
