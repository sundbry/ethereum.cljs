(ns eth.js.web3.net
  (:require [eth.js.web3 :as web3]))

(def rpc (.-net web3/web3))

(defn listening?
  "Returns true iff the client is actively listening for network connections."
  []
  (.-listening rpc))

(defn peer-count
  "Returns the number of peers currently connected to the client."
  []
  (.-peerCount rpc))
