(ns cljs-web3.test.eth
  ;(:require-macros [cemerick.cljs.test :refer [is deftest]])
  (:require 
    ;[cemerick.cljs.test :as t]
    [shodan.console :as log :include-macros true]
    [cljs-web3.eth :as eth]))

(def multiply-7-source "contract test {
                          function multiply(uint a) returns(uint d) {
                            return a * 7;
                          }
                        }")


(def multiply-7-abi [{"name" "multiply(uint256)"
                      "type" "function"
                      "inputs"
                      [{"name" "a"
                        "type" "uint256"}]
                      "outputs"
                      [{"name" "d"
                        "type" "uint256"}]}])

(defn make-solidity-contract [source abi]
  (let [code (eth/solidity source)
        address (eth/transact {:code code})
        _ (log/debug "New contract address:" address)
        contract (eth/contract address abi)]
    (log/debug "Contract constructed" contract)
    contract))

(defn call-contract-fn [contract fn-caller]
  (log/debug "Calling contract at" (get contract "address"))
  (let [callee (eth/call contract {})
        result (fn-caller callee)]
    (log/debug "Call result:" result)
    result))

#_(deftest test-multiply-contract 
  (let [contract (make-solidity-contract multiply-7-source multiply-7-abi)
        multiply (fn [x] (eth/return-value (call-contract-fn contract #(.multiply % x))))
        ;multiply (fn [x] (* x 7))
        ]
    (is (= 0 (multiply 0)))
    (is (= 21 (multiply 3)))
    (is (= 28 (multiply 4)))))

(defn test-multiply-contract [qassert]
  (let [contract (make-solidity-contract multiply-7-source multiply-7-abi)
        multiply (fn [x] (eth/return-value (call-contract-fn contract #(.multiply % x))))]
    (.ok qassert (= 0 (multiply 0)))
    (.ok qassert (= 21 (multiply 3)))
    (.ok qassert (= 28 (multiply 4)))))

(defn run-tests []
  (.test js/QUnit "Contract test" test-multiply-contract))

