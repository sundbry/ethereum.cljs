(ns cljs-web3.test
  (:require
    [shodan.console :as log :include-macros true]
    [cljs-web3.core :as web3]
    [cljs-web3.test.eth :as test-eth]))

(defn- init-fixture []
  (web3/set-provider (web3/http-provider "http://localhost:8080")))


(defn- test-hello [qassert]
  (.ok qassert true "Passed!"))

(defn ^:export run-tests []
  (log/info "Starting tests")
  (init-fixture)
  (.test js/QUnit "Hello test" test-hello)
  (test-eth/run-tests)
  (log/info "Tests complete"))
