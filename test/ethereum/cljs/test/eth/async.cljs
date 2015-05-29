(ns eth.js.test.eth.async
  (:require-macros
    [cljs.core.async.macros :refer [go go-loop]]
    [eth.js.macros :refer [call-async]])
  (:require
    [cljs.core.async :as async]
    [shodan.console :as log :include-macros true]
    [eth.js.eth :as eth] 
    [eth.js.eth.async :as eth-async]
    [eth.js.eth.util :as eth-util]
    [eth.js.test.eth.fixture :as fixture :refer [test-account]])) 

(defn test-multiply-contract [qassert]
  (let [from-address (test-account)
        tx {:from from-address}
        contract fixture/*multiply-7-contract*
        async-done (.async qassert)]
    (go
      (doto qassert
        (.ok (= "1" (str (async/<! (call-async contract 'one tx)))))
        (.ok (= "14" (str (async/<! (call-async contract 'multiply tx 2)))))
        (.ok (= "21" (str (async/<! (call-async contract 'multiply tx 3)))))
        (.ok (= "28" (str (async/<! (call-async contract 'multiply tx 4))))))
      (async-done))))

(defn run-local-tests [qunit]
  (doto qunit
    (.module (str (namespace ::x)))
    (.test "Run contract" test-multiply-contract)))
