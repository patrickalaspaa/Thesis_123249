(ns spotapp.startup
  (:require [spotapp.state      :refer [config environment]]
            [spotapp.app-config :refer [settings]]
            [spotapp.constants  :as const]))

(defn load-config [is-production]
  (println "Retrieving configurations")
  (def context (settings))
  (reset! config context)
  (reset! environment {:is-production is-production}))
