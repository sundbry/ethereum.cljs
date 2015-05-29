(ns eth.js.test.shh
  (:require 
    [shodan.console :as log :include-macros true]
    [eth.js.web3 :as web3]
    [eth.js.shh :as shh]))

(defn get-idents []
  (let [idents [(shh/new-identity) (shh/new-identity)]]
    (log/info "Test idents created:" idents)))

(defn test-post [qassert]
  (let [data-str (web3/from-ascii "Hello world!")
        [to from] (get-idents)
        msg (shh/message {:payload data-str :to to :from from})
        post-result (shh/post msg)]
    (log/debug "post result is:" post-result)
    (doto qassert
      (.ok (not (nil? post-result))))))

(defn run-local-tests [qunit]
  #_(doto qunit 
    (.module (str (namespace ::x)))
    (.test "post()" test-post)))
