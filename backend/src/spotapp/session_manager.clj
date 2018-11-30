(ns spotapp.session-manager
  (:require
    [spotapp.db.auth-sql    :as sql]
    [spotapp.db.connection  :as con :refer [launch-query launch-insert-or-update]]
    [spotapp.state      :refer [sessions]]
    [clojure.core.async     :refer [thread]]))

(defn return-session-object [item]
  {(Integer. (get item :id)) {:personid (get item :personid) :admin (get item :admin)}})

(defn add-session-object [item]
  (swap! sessions merge item))

(defn load-sessions []
  (def session-objects (map return-session-object (launch-query sql/load-sessions)))
  (doseq [x session-objects]
    (add-session-object x)))
