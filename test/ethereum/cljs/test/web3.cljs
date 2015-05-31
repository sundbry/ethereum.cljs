(ns eth.js.test.web3
  (:require 
    [shodan.console :as log :include-macros true]
    [eth.js.web3 :as web3]))

(defn test-sha3 [qassert]
  (let [hash-str (-> "Hello!! McFly!~" web3/sha3)]
    (doto qassert
      (.ok (not (nil? hash-str)))
      (.ok (= "2c935fa4b133094d021cfbb3220dc422fdf20dc3f9cc8d35d35b8907fb4da269" hash-str)))))

(defn run-local-tests [qunit]
  (doto qunit 
    (.module (str (namespace ::x)))
    (.test "SHA3" test-sha3)))
