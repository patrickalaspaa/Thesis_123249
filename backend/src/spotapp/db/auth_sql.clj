(ns spotapp.db.auth-sql
  (:require [honeysql.core :as honey]
            [honeysql.helpers :as helper]))

(defn check-credentials [data]
  (let [{:strs [username password]}  data]
    (->
      (helper/select :id)
      (helper/from :Person)
      (helper/where [:= :username username]
             [:= :password password])
      honey/format)))

(defn add-session [id session-id]
  (->
    (helper/insert-into :Session)
    (helper/columns :Id :PersonId :SessionDate)
    (helper/values [[session-id id (honey/raw ["NOW()"])]])
    honey/format))

(def load-sessions
  (->
    (helper/select :Session.id :Session.PersonId :Person.Admin)
    (helper/from :Session)
    (helper/join :Person [:= :Person.Id :Session.PersonId])
    honey/format))
