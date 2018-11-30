(ns spotapp.db.connection
  (:require [clojure.java.jdbc :as query]
            [spotapp.state     :refer [config]]))

(defn con []
  (get @config :db))

(defn launch-query [sql]
  (query/query (con) sql))

(defn launch-insert-or-update [sql]
  (query/execute! (con) sql))

(defn get-id []
  (def query "CALL sp_newId;")
  (get (first (launch-query query)) :id))
