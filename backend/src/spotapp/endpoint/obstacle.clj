(ns spotapp.endpoint.obstacle
  (:require
   [spotapp.db.obstacle-sql :as sql]
   [clojure.data.json       :as json]
   [spotapp.db.connection   :as con :refer [launch-query launch-insert-or-update]]
   [spotapp.utility.utility :as util :refer [not-contains? in?]]))

(defn get-obstacles [data]
  { :status 200
    :body (json/write-str
            (launch-query
              sql/get-obstacles))
    :headers {}})

(defn get-spot-obstacles [data]
  { :status 200
    :body (json/write-str
            (launch-query
              (sql/get-spot-obstacles
                (util/get-id-from-request data))))
    :headers {}})

(defn get-spot-obstacle-ids [data]
  { :status 200
    :body (json/write-str
            (launch-query
              (sql/get-spot-obstacle-ids
                (util/get-id-from-request data))))
    :headers {}})

(defn handle-obstacle-changes [id obstacle-list person-id]
  (defn- get-obstacel-id [item]
    (get item :obstacleid))

  (def db-spots
    (into []
          (map get-obstacel-id
               (doall
                 (launch-query (sql/get-spot-obstacle-ids id))))))

  (defn get-value-not-in-coll [coll item]
    (if
      (not (in? coll item))
      item))

  (def add-obstacles
    (filter (partial get-value-not-in-coll db-spots) obstacle-list))

  (def delete-obstacles
    (filter (partial get-value-not-in-coll obstacle-list) db-spots))

  (if (> (count add-obstacles) 0)
    (launch-insert-or-update (sql/insert-obstacles id add-obstacles person-id)))

  (if (> (count delete-obstacles) 0)
    (launch-insert-or-update (sql/delete-obstacles id delete-obstacles))))

(defn add-obstacles-to-spot [new-spot id obstacle-list person-id]
  (if new-spot
    (launch-insert-or-update (sql/insert-obstacles id obstacle-list person-id))
    (handle-obstacle-changes id obstacle-list person-id)))
