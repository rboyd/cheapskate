(ns cheapskate.core
  (:require [clj-http.client :as client]
            [carica.core :refer [config]]
            [net.cgrand.enlive-html :as html]
            [net.cgrand.xml :as xml]))

(defn xml-from-api [cmd & params]
  (let [query-params (merge {:Command cmd} (config :auth) (apply merge params))]
    (:body (client/get (config :service-url) {:query-params query-params}))))

(defn parse-response [xml]
 (xml/parse (java.io.ByteArrayInputStream. (.getBytes xml))))

(defn unbox-domains [edn]
  (map :attrs (html/select edn [:Domain])))

(defn paging-results [edn]
  (let [paging-elements [:TotalItems :CurrentPage :PageSize]
        paging-values (map #(-> (html/select edn [:Paging % html/content]) first Integer/parseInt) paging-elements)]
    (zipmap paging-elements paging-values)))

(defn get-domains []
  (-> (xml-from-api "namecheap.domains.getList" {:PageSize 100}) parse-response unbox-domains))
