(ns spotapp.core
  (:gen-class)
  (:require
    [ring.adapter.jetty               :as jetty]
    [spotapp.views.index              :as index]
    [spotapp.endpoint.person          :as person]
    [spotapp.endpoint.spot            :as spot]
    [spotapp.endpoint.authentication  :as auth]
    [spotapp.endpoint.obstacle        :as obs]
    [spotapp.endpoint.documents       :as docs]
    [spotapp.endpoint.rating          :as rating]
    [spotapp.constants                :as const]
    [spotapp.utility.wrappers         :as wrappers]
    [spotapp.session-manager          :as sm]
    [spotapp.startup                  :as startup]
    [spotapp.state                    :refer [config]]
    [ring.middleware.reload           :refer [wrap-reload]]
    [ring.middleware.cors             :refer [wrap-cors]]
    [ring.middleware.multipart-params :refer [wrap-multipart-params]]
    [compojure.core                   :refer [defroutes context GET POST PUT]]
    [compojure.route                  :refer [not-found]]))


(def person-context
  (context const/person []
    ;(GET "/" request (auth/validate-session request person/get-person-list))
    ;(GET  const/root            request person/get-person-list)
    (POST const/root            request person/post-person)
    (GET  const/route-param     request (auth/validate-session request person/get-single-person))
    (POST const/check-username  request person/check-username)
    (POST const/check-email     request person/check-email)))

(def spot-context
  (context const/sspot []
    (GET  const/root            request (auth/validate-session request spot/get-spot-list))
    (POST const/root            request (auth/validate-session request spot/post-spot))
    (PUT  const/root            request (auth/validate-session request docs/add-picture))
    (GET  const/types           request spot/getTypes)
    (GET  const/route-param     request (auth/validate-session request spot/get-single-spot))
    (GET  const/spot-obstacles  request (auth/validate-session request obs/get-spot-obstacles))
    (GET  const/obstacle-ids    request (auth/validate-session request obs/get-spot-obstacle-ids))
    (GET  const/spot-photos     request (auth/validate-session request docs/get-spot-photos))
    (GET  const/spot-rating     request (auth/validate-session request rating/get-spot-rating))
    (POST const/spot-rating     request (auth/validate-session request rating/post-spot-rating))))

(def obstacle-context
  (context const/obstacles-path []
    (GET const/root request (auth/validate-session request obs/get-obstacles))))


(defroutes app
  (GET const/root [] index/indexpage)
  ;Person routes
  person-context
  ;Spot routes
  spot-context
  ;Obstacle context
  obstacle-context
  (POST const/login           request auth/login)
  (GET  const/authentication  request (auth/validate-session request auth/confirm))
  (not-found (str "<h1>404</h1>")))

(defn -main []
  (startup/load-config true)
  (sm/load-sessions)
  (jetty/run-jetty
    (wrap-cors
     (wrap-multipart-params
       (wrappers/wrap-parse-body app))
     :access-control-allow-methods (get-in @config [:app :cors-methods])
     :access-control-allow-origin  (get-in @config [:app :cors-urls]))
    {:port (get-in @config [:app :port-number])}))

(defn -dev-main []
  (startup/load-config false)
  (sm/load-sessions)
  (jetty/run-jetty
   (wrap-reload
     (wrap-cors
      (wrap-multipart-params
        (wrappers/wrap-parse-body app))
      :access-control-allow-methods (get-in @config [:app :cors-methods])
      :access-control-allow-origin  (get-in @config [:app :cors-urls])))
   {:port (get-in @config [:app :port-number])}))
