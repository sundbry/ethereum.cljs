(ns eth.js.test
  (:require
    [shodan.console :as log :include-macros true]
    [eth.js.web3 :as web3]
    [eth.js.eth :as eth]
    [eth.js.test.eth :as test-eth]))

(defn- init-fixture []
  (web3/set-provider (web3/http-provider "http://localhost:8080")))


(defn- test-hello [qassert]
  (.ok qassert true "Passed!"))

(defn- test-connect [qassert]
  (.ok qassert (eth/listening?) "Listening for connections")
  (.ok qassert (> (eth/peer-count) 0) "More than 0 peers"))

(defn run-local-tests []
  (doto js/QUnit
    (.test "Meta test" test-hello)
    (.test "Connect test" test-connect)))

(defn ^:export run-tests []
  (log/info "Starting tests")
  (init-fixture)
  (run-local-tests)
  (test-eth/run-tests)
  (log/info "Tests complete"))

