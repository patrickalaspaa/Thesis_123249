(defproject spotapp "0.1.0-SNAPSHOT"

  :min-lein-version "2.0.0"

  :description "App to store BMX and skate spots around town"

  :url ""

  :license {
            :name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [
                  [org.clojure/clojure "1.8.0"]
                  [ring "1.7.0-RC1"]
                  [compojure "1.6.1"]
                  [org.clojure/java.jdbc "0.4.2"]
                  [mysql/mysql-connector-java "5.1.38"]
                  [org.clojure/data.json "0.2.6"]
                  [ring-cors "0.1.12"]
                  [honeysql "0.9.3"]
                  [cheshire "5.8.0"]
                  [org.clojure/core.async "0.4.474"]]

  :main spotapp.core

  :profiles {
              :uberjar {
                        :source-paths ["src/spotapp"]
                        :main spotapp.core
                        :ommit-source true
                        :aot :all}
              :dev {
                      :main spotapp.core/-dev-main}
              :prod {
                     :main spotapp.core}}


  :uberjar-name "hfl.jar")
