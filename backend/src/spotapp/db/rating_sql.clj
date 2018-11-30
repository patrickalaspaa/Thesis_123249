(ns spotapp.db.rating-sql
  (:require [honeysql.core :as honey]
            [honeysql.helpers :as helper]))

(defn get-personal-spot-rating [spot-id session]
  (->
    (helper/select [:Rating :rating] [1 :personal])
    (helper/from :Rating)
    (helper/join :Session [:= :Session.PersonId :Rating.PersonId])
    (helper/where [:= :SpotId spot-id] [:= :Session.Id session])
    honey/format))

(defn get-avg-spot-rating [spot-id]
  (->
    (helper/select [(honey/raw "AVG(Rating)") :rating] [0 :personal])
    (helper/from :Rating)
    (helper/where [:= :SpotId spot-id])
    honey/format))

(defn insert-rating [spot-id rating person-id]
  (->
    (helper/insert-into :Rating)
    (helper/columns :SpotId :Rating :PersonId :CreatedBy :Created)
    (helper/values [[spot-id rating person-id (honey/raw ["NOW()"]) person-id]])
    honey/format))

(defn update-rating [spot-id rating person-id]
  (->
   (helper/update :Rating)
   (helper/sset {:Rating rating :ModifiedBy person-id :Modified (honey/raw ["NOW()"])})
   (helper/where [:= :PersonId person-id] [:= :SpotId spot-id])
   honey/format))
