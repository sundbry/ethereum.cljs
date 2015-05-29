(ns eth.js.test.eth.async
  (:require-macros
    [cljs.core.async.macros :refer [go go-loop]])
  (:require
    [cljs.core.async :as async]
    [shodan.console :as log :include-macros true]
    [eth.js.eth :as eth] 
    [eth.js.eth.async :as eth-async :include-macros true]
    [eth.js.eth.util :as eth-util]
    [eth.js.test.eth.fixture :refer [multiply-7-source multiply-7-abi]]))

#_(defn test-multiply-contract [qassert]
  (let [{:strs [code info]} (eth/solidity multiply-7-source)
        from-address (test-account)
        latest-blocks (eth/watch "latest")
        address (eth/send-transaction {:from from-address
                                       :code code})
        contract-factory (eth/contract multiply-7-abi)
        contract (.at contract-factory address)
        ;cb (fn [error result]
        ;     (log/debug "call result:" (str result) "error:" error))
        multiply-async (eth-util)
        (fn [x]
          (let [ch (async/chan 1)
                cb ( async/put! ch)
                res (-> contract
                        .-multiply
                        (.call x cb))]
            res))
        one (fn []
              (let [res (-> contract .-one (.call))]
                (log/debug "one result:" (str res))
                res))
        async-done (.async qassert)
        mined-chan (eth-util/go-wait-mined latest-blocks from-address code)]
    (go
      (let [{:keys [block tx]} (async/<! mined-chan)]
        (log/debug "Mined!" block tx)
        (eth/stop-watch latest-blocks)
        (doto qassert
          (.ok (= "1" (str (one))))
          (.ok (= "14" (str (multiply 2))))
          (.ok (= "21" (str (multiply 3))))
          (.ok (= "28" (str (multiply 4)))))
        (async-done)))))

(defn run-local-tests [qunit]
  #_(doto qunit
    (.test "Run contract" test-multiply-contract)))
