(ns spotapp.db.spot-sql
  (:require [honeysql.core :as honey]
            [honeysql.helpers :as helper]
            [spotapp.utility.utility :as util :refer [not-nil?]]))

(defn get-spot-list [limit offset]
  (def sql-map
    {:select [:Spot.Id :Spot.name :Spot.Address :SpotType.Type
              :Spot.lat :Spot.lng
              [{:select [:name]
                :from [:Images]
                :where [:= :Images.spotId :Spot.Id]
                :limit 1} :cover]
              [{:select [(honey/raw "AVG(rating)")]
                :from [:Rating]
                :where [:= :Rating.SpotId :Spot.Id]} :rating]]
     :from [:Spot]
     :join [:SpotType [:= :Spot.Type :SpotType.Id]]
     :order-by [:Spot.Name]})

  (if (and (not-nil? limit) (> limit 0))
    (honey/format
      (honey/build sql-map :offset (Integer. offset) :limit (Integer. limit)))
    (honey/format sql-map)))

(defn get-spot [id]
  (->
    (helper/select :Spot.Id :Spot.name :Spot.Address :SpotType.Type :Spot.lat :Spot.lng)
    (helper/from :Spot)
    (helper/join :SpotType [:= :Spot.Type :SpotType.Id])
    (helper/where [:= :Spot.Id id])
    honey/format))


(defn insert-spot [id data person-id]
  (let [{:strs [name type address lat lng]} data]
    (->
      (helper/insert-into :Spot)
      (helper/columns :Id :Name :Address :Type :lat :lng :Created :CreatedBy)
      (helper/values [[id name address type lat lng (honey/raw ["NOW()"]) person-id]])
      honey/format)))

(defn update-spot [id data person-id]
  (let [{:strs [name type address lat lng]} data]
    (->
     (helper/update :Spot)
     (helper/sset {:Name name
                   :Type type
                   :Address address
                   :lat lat
                   :lng lng
                   :Modified (honey/raw ["NOW()"])
                   :ModifiedBy person-id})
     (helper/where [:= :Id id])
     honey/format)))

(defn get-spot-types []
  (->
    (helper/select :Id :Type)
    (helper/from :SpotType)
    (helper/order-by :Type)
    honey/format))
