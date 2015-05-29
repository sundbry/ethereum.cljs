(ns eth.js.test
  (:require
    [shodan.console :as log :include-macros true]
    [eth.js.web3 :as web3]
    [eth.js.web3.net :as net]
    [eth.js.eth :as eth]
    [eth.js.test.web3 :as test-web3]
    [eth.js.test.eth :as test-eth]
    [eth.js.test.shh :as test-shh]))

(defn- init-fixture
  []
  (web3/set-provider (web3/http-provider "http://localhost:8545")))

(defn- test-lib [qassert]
  (.ok qassert (some? web3/web3) "Passed!"))

(defn- test-connect [qassert]
  (.ok qassert (net/listening?) "Listening for connections")
  (.ok qassert (integer? (net/peer-count)) "More than 0 peers"))

(defn run-local-tests [qunit]
  (doto qunit
    (.module (str (namespace ::x)))
    (.test "Test require" test-lib)
    (.test "Connect test" test-connect)))

(defn ^:export run-tests []
  (log/info "Starting tests")
  (init-fixture)
  (doto js/QUnit
    (run-local-tests)
    (test-web3/run-local-tests)
    (test-eth/run-local-tests)
    (test-shh/run-local-tests))
  (log/info "Tests complete"))

(run-tests)
