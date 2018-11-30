(ns spotapp.endpoint.rating
    (:require
     [clojure.data.json       :as json]
     [spotapp.db.rating-sql   :as sql]
     [spotapp.constants       :as const]
     [spotapp.utility.utility :as util :refer [not-nil?]]
     [spotapp.db.connection          :refer [launch-query launch-insert-or-update]]
     [spotapp.state       :refer [sessions]]))


(defn get-spot-rating [data]
  (def session (get data :session))
  (def spot-id (get-in data [:route-params :id]))
  (def user-rating
    (if (not-nil? session)
      (launch-query (sql/get-personal-spot-rating spot-id session))
      nil))

  (def avg-rating
    (if (empty? user-rating)
      (launch-query (sql/get-avg-spot-rating spot-id))))

  {:status 200
   :body (json/write-str (if (not-empty user-rating) user-rating avg-rating))
   :header {}})

(defn add-rating [session data]
  (def spot-id (get-in data [:route-params :id]))
  (def person-id (get-in @sessions [session :personid]))
  (def rating (get data const/rating))
  (def updateRating (get data const/uupdate))
  (def sql
    (if updateRating
      (sql/update-rating spot-id rating person-id)
      (sql/insert-rating spot-id rating person-id)))
  (launch-insert-or-update sql))

(defn post-spot-rating [data]
  (def session (get data :session))
  (if (not-nil? session)
    (add-rating (Integer. session) data))
  {:status 200})
