(ns eth.js.test.eth.contracts.multiply-async
  (:require-macros
    [cljs.core.async.macros :refer [go]])
  (:require
    [cljs.core.async :as async]
    [eth.js.test.eth.fixture :refer [test-account]]
    [eth.js.eth.util :as eth-util :include-macros true]))

(def ^:dynamic *contract* nil)

(defn build
  []
  (->> "contract/multiply-async.sol"
      eth-util/source
      (eth-util/build-solidity "MultiplyAsync")))

(defn test-call [qassert])

#_(defn test-call [qassert]
  (let [from-address (test-account)
        tx {:from from-address}
        contract fixture/*multiply-7-contract*
        async-done (.async qassert)]
    (go
      (doto qassert
        (.ok (= "0" (log/spy (str (async/<! (eth-async/call contract 'multiply tx 0))))))
        (.ok (= "7" (log/spy (str (async/<! (eth-async/call contract 'multiply tx 1))))))
        (.ok (= "14" (log/spy (str (async/<! (eth-async/call contract 'increment_n tx 2)))))))
      (async-done))))

(defn run-local-tests [qunit]
  (go
    (binding [*contract* 
              (async/<! (eth-util/go-mine (build) (test-account)))]
      (doto qunit
        (.module (str (namespace ::x)))
        (.test "Async call on contract" test-call)))))

