(ns eth.js.shh
  (:require
    [shodan.console :as log :include-macros true]
    [eth.js.web3 :as web3]))

(def rpc (.-shh web3/web3))

;;; Whsiper

(def message-defaults
  {:ttl 300})

(defn message [msg-opts]
  "Construct a message with default options"
  (merge message-defaults msg-opts))

(defn post
  "Post a whsiper message.
     from: identity of sender
     to: identity of receiver
     payload: message payload
     ttl: time to live
     workToProve: TODO
     topic: string or array of strings, with message topics"
  [params-dict]
  (let [js-params (clj->js params-dict)]
    (.post rpc js-params)))

(defn new-identity
  "Creates a new identity address."
  []
  (.newIdentity rpc))

;; Return true iff an entity belongs to us.
;; web3.shh.haveIdentity = function(ident) {};
;; Returns a watcher for whisper messages matching params.
;; topic: string or array
;; to: identity of receiver
;; web3.ssh.watch = function(params) {};
