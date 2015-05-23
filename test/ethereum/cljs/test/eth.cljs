(ns eth.js.test.eth
  (:require-macros
    [cljs.core.async.macros :refer [go go-loop]])
  (:require
    [cljs.core.async :as async]
    [shodan.console :as log :include-macros true]
    [eth.js.eth :as eth]
    [eth.js.eth.util :as eth-util]
    [eth.js.test.eth.fixture :refer [test-account multiply-7-source multiply-7-abi]]))

(defn call-contract-fn [contract fn-caller]
  (log/debug "Calling contract at" (get contract "address"))
  (let [callee (eth/call contract {})
        result (fn-caller callee)]
    (log/debug "Call result:" result)
    result))

(defn test-solidity-compiler [qassert]
  (let [{:strs [code info] :as result} (eth/solidity multiply-7-source)]
    (log/debug "Multiply-7 compiled to bytecode:" result)
    (doto qassert
      (.ok (not (nil? result)))
      (.ok (> (count code) 20)))))

(defn test-tx-create-contract [qassert]
  (let [{:strs [code info]} (eth/solidity multiply-7-source)
        from-address (test-account)
        address (eth/send-transaction {:from from-address
                                       :code code})]
    (log/debug "Contract created at address:" address)
    (doto qassert
      (.ok (not (nil? address)))
      (.ok (= (count address) 42)))))

(defn test-contract-js-api [qassert]
  (let [{:strs [code info]} (eth/solidity multiply-7-source)
        from-address (test-account)
        address (eth/send-transaction {:from from-address
                                       :code code})
        contract-factory (eth/contract multiply-7-abi)
        contract (.at contract-factory address)]
    (log/debug "contract:" contract)
    (doto qassert
      (.ok (some? (.-address contract)))
      (.ok (some? (.-multiply contract))))))

(defn test-multiply-contract [qassert]
  (let [{:strs [code info]} (eth/solidity multiply-7-source)
        from-address (test-account)
        latest-blocks (eth/watch "latest")
        address (eth/send-transaction {:from from-address
                                       :code code})
        contract-factory (eth/contract multiply-7-abi)
        contract (.at contract-factory address)
        ;cb (fn [error result]
        ;     (log/debug "call result:" (str result) "error:" error))
        tx (clj->js {:from from-address})
        tx2 (clj->js {:from from-address})
        multiply (fn [x]
                   (let [res (-> contract
                                 .-multiply
                                 #_(.sendTransaction x tx cb)
                                 (.call x))]
                     (log/debug "result:" (str res))
                     res))

        one (fn []
              (let [res (-> contract .-one #_(.sendTransaction tx2 cb) (.call))]
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
  (doto qunit
    (.test "Solidity compiler" test-solidity-compiler)
    (.test "Create contract transaction" test-tx-create-contract)
    (.test "Instantiate contract" test-contract-js-api)
    (.test "Run contract" test-multiply-contract)))
