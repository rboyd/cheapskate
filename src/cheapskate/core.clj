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

(defn set-contacts [domain params]
  (xml-from-api "namecheap.domains.setContacts" (merge {:DomainName domain}
                                                       (config :contacts)
                                                       params)))

(defn dns-setdefault [domain]
  (let [[sld tld] (clojure.string/split domain #"\.")]
    (xml-from-api "namecheap.domains.dns.setDefault" (merge {:SLD sld :TLD tld}))))

(defn dns-sethosts [domain address]
  (let [[sld tld] (clojure.string/split domain #"\.")]
    (xml-from-api "namecheap.domains.dns.setHosts" (merge {:SLD sld :TLD tld
                                                           :HostName1 "@" :RecordType1 "A" :Address1 address
                                                           :HostName2 "www" :RecordType2 "A" :Address2 address}))))
