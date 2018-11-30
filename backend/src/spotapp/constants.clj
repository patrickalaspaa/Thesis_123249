(ns spotapp.constants
  (:require
   [clojure.data.json :as json]))

; Messages
(def forbidden-response
  {:status 401
   :body (json/write-str {"access-denied" "Please login"})})

; File paths
(defn temp-path [is-production]
  (if is-production
    (str (System/getProperty "user.dir")  "/tmp/")
    "/home/spotapp/tmp/"))


(defn perm-path [is-production]
  (if is-production
    (str (System/getProperty "user.dir")  "/../../var/www/html/")
    "/var/www/html/"))


; Constant strings
(def photos "photos")
(def obstacles "obstacles")
(def spot "spot")
(def id "id")
(def rating "rating")
(def uupdate "update")
(def object-type "object-type")
(def invalid "invalid")
(def object "object")
(def exists "exists")
(def nname "name")
(def extension "extension")
(def spot-photo "spot-photo")
(def seaprator ".")
(def dash "-")
(def slash "/")
(def message "message")
(def authenticated "User has been authenticated")
(def created "created")
(def session "session")

; PATHs
(def root "/")
(def check-email "/check-email")
(def check-username "/check-username")
(def obstacles-path "/obstacles")
(def sspot "/spot")
(def person "/person")
(def route-param "/:id")
(def types "/types")
(def login "/login")
(def authentication "/authentication")
(def obstacle-ids "/:id/obsid")
(def spot-obstacles "/:id/obs")
(def spot-photos "/:id/photos")
(def spot-rating "/:id/rating")
