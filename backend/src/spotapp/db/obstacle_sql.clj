(ns spotapp.db.obstacle-sql
  (:require [honeysql.core :as honey]
            [honeysql.helpers :as helper]))

(def get-obstacles
  (->
    (helper/select :Id :Obstacle)
    (helper/from :Obstacle)
    honey/format))

(defn get-spot-obstacles [id]
  (->
    (helper/select :Obstacle)
    (helper/from :Obstacle)
    (helper/join :SpotHasObstacle [:= :SpotHasObstacle.ObstacleId :Obstacle.Id])
    (helper/where [:= :SpotHasObstacle.SpotId id])
    honey/format))

(defn get-spot-obstacle-ids [id]
  (->
    (helper/select :ObstacleId)
    (helper/from :SpotHasObstacle)
    (helper/where [:= :SpotHasObstacle.SpotId id])
    honey/format))

(defn insert-obstacles [id data person-id]
  (def values
    ;Create list for each row of data entered
    (map (fn [item] [id item (honey/raw ["NOW()"]) person-id]) data))
  (->
    (helper/insert-into :SpotHasObstacle)
    (helper/columns :SpotId :ObstacleId :Created :CreatedBy)
    (helper/values values)
    honey/format))

(defn delete-obstacles [id data]
  (->
    (helper/delete-from :SpotHasObstacle)
    (helper/where [:and
                   [:= :SpotId id]
                   [:in :ObstacleId data]])
    honey/format))
