(ns cheapskate.core
  (:require [clj-http.client :as client]
            [carica.core :refer [config]]
            [net.cgrand.enlive-html :as html]
            [net.cgrand.xml :as xml]))

(defn xml-from-api [cmd]
  (:body (client/get (config :service-url) {:query-params (merge (config :auth) {:Command cmd}) })))

(defn parse-response [xml]
 (xml/parse (java.io.ByteArrayInputStream. (.getBytes xml))))

(defn unbox-domains [edn]
 (map :attrs (html/select edn [:Domain])))

(defn get-domains []
 (-> (xml-from-api "namecheap.domains.getList") parse-response unbox-domains))
