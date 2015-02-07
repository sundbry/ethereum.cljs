(ns cljs-web3.core
  (:require
    [shodan.console :as log :include-macros true]))

(def web3 (js/require "web3"))
(def eth (.-eth web3))

(defn http-provider [uri]
  (let [constructor (-> web3 .-providers .-HttpSyncProvider)]
    (constructor.  uri)))

(defn set-provider [provider]
  (log/debug "Setting web3 provider:" provider)
  (.setProvider web3 provider))

(defn to-decimal [hex-str]
  (.toDecimal web3 hex-str))
