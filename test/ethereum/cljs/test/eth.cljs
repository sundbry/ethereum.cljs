(ns eth.js.test.eth
  (:require-macros
    [cljs.core.async.macros :refer [go go-loop]])
  (:require
    [cljs.core.async :as async]
    [shodan.console :as log :include-macros true]
    [shodan.inspection :refer [inspect]]
    [eth.js.eth :as eth]
    [eth.js.eth.util :as eth-util]
    [eth.js.test.eth.fixture :refer [test-account multiply-7-contract multiply-7-source multiply-7-abi]]))

(defn call-contract-fn [contract fn-caller]
  (log/debug "Calling contract at" (get contract "address"))
  (let [callee (eth/call contract {})
        result (fn-caller callee)]
    (log/debug "Call result:" result)
    result))

(defn test-solidity-compiler [qassert]
  (let [result (eth/solidity multiply-7-source)
        {:strs [code info]} (get result multiply-7-contract)]
    (log/debug "Multiply-7 compiled to bytecode")
    (inspect code)
    (doto qassert
      (.ok (not (nil? result)))
      (.ok (> (count code) 20)))))

(defn test-account-balance [qassert]
  (let [acct (test-account)
        balance (eth/get-balance acct)]
    (log/debug "Account balance is:" (eth-util/currency-str balance) (str "[" balance "]"))
    (doto qassert
      (.ok (.lte (eth-util/ether 1) balance)))))

(defn test-tx-create-contract [qassert]
  (let [compiler-out (eth/solidity multiply-7-source)
        {:strs [code info]} (get compiler-out multiply-7-contract)
        from-address (test-account)
        address (eth/send-transaction {:from from-address
                                       :code code})]
    (log/debug "Contract created at address:" address)
    (doto qassert
      (.ok (not (nil? address)))
      (.ok (= (count address) 42)))))

(defn test-contract-js-api [qassert]
  (let [compiler-out (eth/solidity multiply-7-source)
        {:strs [code info]} (get compiler-out multiply-7-contract)
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
  (let [compiler-out (eth/solidity multiply-7-source)
        {:strs [code info]} (get compiler-out multiply-7-contract)
        from-address (test-account)
        latest-blocks (eth/watch "latest")
        address (eth/send-transaction {:from from-address
                                       :code code})
        contract-factory (eth/contract multiply-7-abi)
        contract (.at contract-factory address)
        ;cb (fn [error result]
        ;     (log/debug "call result:" (str result) "error:" error))
        multiply (fn [x]
                   (let [res (-> contract
                                 .-multiply
                                 #_(.sendTransaction x tx cb)
                                 (.call x))]
                     (log/debug "result:" (str res))
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
  (doto qunit
    (.test "Solidity compiler" test-solidity-compiler)
    (.test "Account balance" test-account-balance)
    (.test "Create contract transaction" test-tx-create-contract)
    (.test "Instantiate contract" test-contract-js-api)
    (.test "Run contract" test-multiply-contract)))
