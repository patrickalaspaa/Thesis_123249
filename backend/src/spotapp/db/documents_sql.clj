(ns spotapp.db.documents-sql
  (:require [honeysql.core :as honey]
            [honeysql.helpers :as helper]))

(defn get-single-image [spot-id]
  (->
    (helper/select :Images.Name)
    (helper/from :Images)
    (helper/where [:= :Image.SpotId spot-id])
    (helper/limit 1)
    honey/format))

(defn get-all-spot-images [spot-id]
  (->
    (helper/select :Images.Id :Images.Name)
    (helper/from :Images)
    (helper/where [:= :Images.SpotId spot-id])
    honey/format))

(defn insert-image [id spot-id name person-id]
  (->
    (helper/insert-into :Images)
    (helper/columns :Id :Name :SpotId :Created :CreatedBy)
    (helper/values [[id name spot-id (honey/raw ["NOW()"]) person-id]])
    honey/format))
