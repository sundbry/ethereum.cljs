(ns eth.js.test.eth.async
  (:require-macros
    [cljs.core.async.macros :refer [go go-loop]])
  (:require
    [cljs.core.async :as async]
    [shodan.console :as log :include-macros true]
    [eth.js.eth :as eth] 
    [eth.js.eth.async :as eth-async :include-macros true]
    [eth.js.eth.util :as eth-util]
    [eth.js.test.eth.fixture :as fixture :refer [test-account]])) 

(defn test-call [qassert]
  (let [from-address (test-account)
        tx {:from from-address}
        contract fixture/*counter-contract*
        async-done (.async qassert)]
    (log/debug "calling counter")
    (go
      (doto qassert
        (.ok (< 0 (log/spy (str (async/<! (eth-async/call contract 'increment tx))))))
        (.ok (< 0 (log/spy (str (async/<! (eth-async/call contract 'increment tx))))))
        (.ok (< 0 (log/spy (str (async/<! (eth-async/call contract 'increment_n tx 2)))))))
      (async-done))))

#_(defn test-send-transaction-async [qassert]
  (let [from-address (test-account)
        tx {:from from-address}
        contract fixture/*counter-contract*
        async-done (.async qassert)]
    (log/debug "calling counter")
    (go
      (doto qassert
        (.ok (< 0 (log/spy (str (async/<! (call-async contract 'increment tx))))))
        (.ok (< 0 (log/spy (str (async/<! (call-async contract 'increment tx))))))
        (.ok (< 0 (log/spy (str (async/<! (call-async contract 'increment_n tx 2)))))))
      (async-done))))

(defn run-local-tests [qunit]
  (go
    (binding [fixture/*counter-contract*
              (async/<! (fixture/go-mine-contract
                          fixture/counter-name
                          fixture/counter-source))]
      (doto qunit
        (.module (str (namespace ::x)))
        (.test "Async call on contract" test-call)))))
