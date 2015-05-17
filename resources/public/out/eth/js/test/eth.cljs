(ns eth.js.test.eth
  ;(:require-macros [cemerick.cljs.test :refer [is deftest]])
  (:require-macros
    [eth.js.macros :as macro])
  (:require 
    ;[cemerick.cljs.test :as t]
    [shodan.console :as log :include-macros true]
    [eth.js.eth :as eth]))

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
        contract (eth/contract address abi)]
    (log/debug "Contract constructed" contract)
    contract))

(defn call-contract-fn [contract fn-caller]
  (log/debug "Calling contract at" (get contract "address"))
  (let [callee (eth/call contract {})
        result (fn-caller callee)]
    (log/debug "Call result:" result)
    result))

(defn test-serpent-compiler [qassert]
  (let [code (eth/solidity multiply-7-source)]
    (log/debug "Multiply-7 compiled to bytecode:" code)
    (doto qassert
      (.ok (not (nil? code)))
      (.ok (> (count code) 20)))))

(defn test-tx-create-contract [qassert]
  (let [code (eth/solidity multiply-7-source)
        address (eth/transact {:code code})]
    (log/debug "Contract created at address: " address)
    (doto qassert
      (.ok (not (nil? address)))
      (.ok (= (count address) 42)))))

(defn test-contract-js-api [qassert]
  (let [code (eth/solidity multiply-7-source)
        address (eth/transact {:code code})
        contract (make-solidity-contract multiply-7-source multiply-7-abi)]
    (doto qassert
      (.ok (some? (.-address contract))))))

(defn test-multiply-contract [qassert]
  (let [contract (make-solidity-contract multiply-7-source multiply-7-abi)
        ;_ (log/debug "macroing invoke")
        ;invoke (fn [(macro/invoker 'multiply 
        ;multiply #(macro/invoke caller "multiply" %)
        multiply (fn [x] 
                   (-> contract
                       .call
                       (.multiply x)
                       (eth/return-value)))]
    ;(log/debug "Invoke result:" multiply)
    (doto qassert
      (.ok (= 0 (multiply 0)))
      (.ok (= 21 (multiply 3)))
      (.ok (= 28 (multiply 4))))))

(defn run-local-tests [qunit]
  (doto qunit
    (.test "Serpent compiler" test-serpent-compiler)
    (.test "Create contract transaction" test-tx-create-contract)
    (.test "Instantiate contract" test-contract-js-api)
    (.test "Run contract" test-multiply-contract)))

