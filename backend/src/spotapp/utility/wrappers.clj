(ns spotapp.utility.wrappers
  (:require
   [spotapp.utility.utility :as util :refer [not-nil?]]
   [clojure.string          :as string]
   [spotapp.constants       :as const]
   [clojure.walk            :refer [keywordize-keys]]))

(defn wrap-parse-body [handler]
  (defn- get-single-item [map-item]
    {(key map-item) (val map-item)})

  (defn split-params [item]
    (string/split item #"="))

  (defn handle-query-string [query-string]
    (if (not-nil? query-string)
      (do
        (def params (string/split query-string #"&"))
        (doall (keywordize-keys (into {} (map split-params params)))))))

  (defn- get-session-item [data]
    {:session (get-in data [:headers const/session])})

  (defn- create-data-map [request]
    (def base-data
      {:request-method  (request :request-method)
       :uri             (request :uri)
       :query-string    (handle-query-string (request :query-string))})

    (def session
      (get-session-item request))

    (def body-map
      (into {}
            (map get-single-item
                   (util/parse-request-body request))))

    (conj
      (conj base-data body-map)
      session))

  (fn [request]
    (if (= (get request :request-method) :put)
      (do
        ;Add session to data map
        (handler (conj request (get-session-item request))))
      (do
        (def data
          (create-data-map request))
        (handler data)))))
