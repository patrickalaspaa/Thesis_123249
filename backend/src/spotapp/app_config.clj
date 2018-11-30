(ns spotapp.app-config)

(defn settings []
  {
    :db {
         :subprotocol "mysql"
         :subname ""
         :user ""
         :password ""}

    :app {
          :cors-urls [#"" #""]
          :cors-methods [:get :put :post :delete]
          :port-number 8000}

    :nginx {
            :url ""}})
