(ns spotapp.endpoint.authentication
  (:require
    [clojure.data.json        :as json]
    [spotapp.db.connection    :as con]
    [spotapp.utility.utility  :as util]
    [spotapp.constants        :as const]
    [spotapp.db.auth-sql      :as sql]
    [spotapp.session-manager  :as sm]
    [spotapp.state        :refer [sessions]]))

(def login-response-map
  {:status 200
   :body ""})

(defn confirm [data]
  {:status 200
   :body (json/write-str {const/message const/authenticated})})

(defn return-session [session-id]
  (def session-map (assoc login-response-map :body (json/write-str {"session" session-id})))
  session-map)

(defn successful-login [data person-id]
  (def session-id (hash (new java.util.Date)))
  (def session-object
    (sm/return-session-object
     {:id       session-id
      :personid person-id}))
  (sm/add-session-object session-object)
  (con/launch-insert-or-update (sql/add-session person-id session-id))
  (return-session session-id))

(defn get-session-id [id]
  (def filtered-sessions (filter (partial util/includes-val? id) @sessions))
  (def session (first (first filtered-sessions)))
  session)

(defn current-session [id]
  (if (not-empty @sessions)
    (get-session-id id)
    nil))

(defn login [data]
  (def result-set (con/launch-query (sql/check-credentials data)))
  (def id  (get (first result-set) :id))
  (if (not (nil? id))
    (do
      (def valid-session (current-session id))
      (if (nil? valid-session)
        (successful-login data id)
        (return-session valid-session)))
    const/forbidden-response))

(defn validate-session [data function]
  (def session-id (get data :session))
  (if (and (not (nil? session-id)) (contains? @sessions (Integer. session-id)))
    (function data)
    const/forbidden-response))
