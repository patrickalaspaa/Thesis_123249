(ns spotapp.endpoint.spot
  (:require
    [clojure.data.json          :as json]
    [spotapp.utility.utility    :as util]
    [spotapp.db.spot-sql        :as sql]
    [spotapp.endpoint.obstacle  :as obs]
    [spotapp.endpoint.documents :as docs]
    [spotapp.constants          :as const]
    [spotapp.db.connection      :refer [launch-query launch-insert-or-update get-id]]))

(defn get-single-spot [data]
  {:status    200
    :body     (json/write-str
                (launch-query
                  (sql/get-spot
                    (util/get-id-from-request data))))
    :headers  {}})

(defn get-spot-list [data]
  (def limit (get-in data [:query-string :limit]))
  (def offset (get-in data [:query-string :offset]))
  {:status    200
    :body     (json/write-str
                (launch-query (sql/get-spot-list limit offset)))
    :headers  {}})

(defn getTypes [data]
  {:status    200
    :body     (json/write-str
                (launch-query (sql/get-spot-types)))
    :headers  {}})

(defn post-spot [data]
  (let [{:strs [spot]} data]
    (def new-spot
      (< (util/parse-int (get spot const/id)) 1))

    (def person-id (util/get-person-from-request data))

    (def id
      (if new-spot
        (get-id)
        (util/parse-int (get spot const/id))))

    ;check values before insert
    (def query
      (if new-spot
        (sql/insert-spot id spot person-id)
        (sql/update-spot id spot person-id)))

    (launch-insert-or-update query)

    (if (not-empty (get data const/obstacles))
      (obs/add-obstacles-to-spot new-spot id (get data const/obstacles) person-id))
    ;Handle images
    (if (not-empty (get data const/photos))
      (docs/handle-tmp-images id (get data const/photos) person-id)))
  {
   :status  201
   :body    (json/write-str {const/spot id})
   :headers {}})
