(ns spotapp.endpoint.person
  (:require
    [clojure.data.json        :as json]
    [spotapp.utility.utility  :as util]
    [spotapp.db.person-sql    :as sql]
    [spotapp.constants        :as const]
    [spotapp.db.connection    :refer [launch-query launch-insert-or-update]]))

(defn get-single-person [data]
  {:status 200
    :body (json/write-str
            (launch-query
              (sql/get-person
                (util/get-id-from-request data))))
    :headers {}})

(defn get-person-list [request]
  {:status 200
    :body (json/write-str
            (launch-query sql/get-person-list))
    :headers {}})


(defn post-person [data]
  (launch-insert-or-update (sql/create-new-person data))
  {:status 201
    :body (json/write-str {:user const/created})
    :headers {}})

(defn check-username [data]
  (def result-set (launch-query (sql/check-username data)))
  {:status 200
    :body (json/write-str {:username (if (empty? result-set) true false)})
    :headers {}})

(defn check-email [data]
  (def result-set (launch-query (sql/check-email data)))
  {:status 200
    :body (json/write-str (if (empty? result-set) {:email true} {:email false}))
    :headers {}})
