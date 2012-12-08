(ns cheapskate.core
  (:require [clj-http.client :as client]
            [carica.core :refer [config]]))


(defn get-domains []
  (client/get (config :service-url) {:query-params (merge (config :auth) {:Command "namecheap.domains.getList"}) }))
