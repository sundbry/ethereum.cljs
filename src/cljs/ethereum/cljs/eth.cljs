(ns ethereum.cljs.eth
  (:require
    [shodan.console :as log :include-macros true]
    [ethereum.cljs.web3 :as web3]))

;; Web3 Javascript API

(defn accounts []
  (.-accounts web3/eth))

(defn balance-at [account-id]
  (.balanceAt web3/eth account-id))

(defn call [contract options]
  (let [options-json (clj->js options)]
    (.call contract options)))

(defn contract [addr abi]
  (let [abi-json (clj->js abi)]
    (log/debug "Contract ABI:" abi-json)
    (.contract web3/eth addr abi-json)))

#_(defn invoke [call-result fn-symbol params]
  ; TODO http://stackoverflow.com/questions/359788/how-to-execute-a-javascript-function-when-i-have-its-name-as-a-string
  )

(defn solidity [source-code-str]
  (.solidity web3/eth source-code-str))

(defn transact [params]
  (let [params-json (clj->js params)]
    (log/debug "Transaction params:" params-json)
    (.transact web3/eth params-json)))

;; Custom functions

(defn currency-str [wei]
  (str (.toExponential (js/parseInt wei)) " wei"))

(defn return-value [result]
  (-> result .-c (nth 0)))
