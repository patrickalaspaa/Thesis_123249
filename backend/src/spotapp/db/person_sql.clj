(ns spotapp.db.person-sql
  (:require [honeysql.core :as honey]
            [honeysql.helpers :as helper]))

(def get-person-list
  (honey/format
   {:select [:*]
    :from [:Person]}))

(defn get-person [id]
  (honey/format
    {:select [:*]
     :from [:Person]
     :where [:= :Id id]}))

(defn check-username [data]
  (let [{:strs [username]} data]
    (honey/format
      {:select [:id]
       :from [:Person]
       :where [:= :Username username]})))

(defn check-email [data]
  (let [{:strs [email]} data]
    (honey/format
      {:select [:id]
       :from [:Person]
       :where [:= :Email email]})))

(defn create-new-person [data]
  (let [{:strs [username, email, password]}
        data]
    (->
      (helper/insert-into :Person)
      (helper/columns :Username :Email :Password :Admin :Created)
      (helper/values [[username, email, password, 0, (honey/raw ["NOW()"])]])
      honey/format)))
