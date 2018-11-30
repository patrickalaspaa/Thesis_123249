(ns spotapp.utility.utility
  (:require [cheshire.core           :as cheshire]
            [spotapp.state       :refer [sessions]]))

(defn get-id-from-request [request]
  (get-in request [:route-params :id]))

(defn get-value-from-params [request key]
  (get-in request [:route-params key]))

(defn parse-request-body [request]
  (cheshire/parse-string
    (slurp
       (get request :body))))

(defn parse-int [s]
   (Integer. s))

(defn includes-val? [id item]
  (= (val item) id))

(defn return-json [status body header])

(defn parse-post-body [val])

(defn not-nil? [x]
  (not (nil? x)))

(defn not-contains? [coll x]
  (not (contains? coll x)))

(defn in?
  "true if coll contains elm"
  [coll elm]
  (some #(= elm %) coll))

(defn get-person-from-request [request]
  (def session (Integer. (get request :session)))
  (get-in @sessions [session :personid]))
