(ns eth.js.test.web3
  (:require 
    [shodan.console :as log :include-macros true]
    [eth.js.web3 :as web3]))

(defn test-sha3 [qassert]
  (let [hash-str (-> "Hello!! McFly!~" web3/from-ascii web3/sha3)]
    (log/debug "hash is: " hash-str)
    (doto qassert
      (.ok (not (nil? hash-str)))
      (.ok (= 66 (.-length hash-str))))))

(defn run-local-tests [qunit]
  (doto qunit 
    (.module (str (namespace ::x)))
    (.test "SHA3" test-sha3)))
