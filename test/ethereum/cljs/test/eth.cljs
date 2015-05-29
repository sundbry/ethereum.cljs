(ns eth.js.test.eth
  (:require-macros
    [cljs.core.async.macros :refer [go go-loop]])
  (:require
    [cljs.core.async :as async]
    [shodan.console :as log :include-macros true]
    [shodan.inspection :refer [inspect]]
    [eth.js.eth :as eth]
    [eth.js.eth.util :as eth-util]
    [eth.js.test.eth.fixture :as fixture :refer [test-account]]
    [eth.js.test.eth.async :as test-eth-async]))

(defn call-contract-fn [contract fn-caller]
  (log/debug "Calling contract at" (get contract "address"))
  (let [callee (eth/call contract {})
        result (fn-caller callee)]
    (log/debug "Call result:" result)
    result))

(defn test-solidity-compiler [qassert]
  (doseq [[contract-name contract-source]
          [[fixture/multiply-7-name fixture/multiply-7-source]
           [fixture/foo-name fixture/foo-source]
           [fixture/counter-name fixture/counter-source]]]
    (log/debug "Compiling" contract-name)
    (let [result (eth/solidity contract-source)
          {:strs [code info]} (get result contract-name)]
      (doto qassert
        (.ok (some? result))
        (.ok (> (count code) 0))
        (.ok (some? (get info "abiDefinition")))))))

(defn test-account-balance [qassert]
  (let [acct (test-account)
        balance (eth/get-balance acct)]
    (log/debug "Account balance is:" (eth-util/currency-str balance) (str "[" balance "]"))
    (doto qassert
      (.ok (.lte (eth-util/ether 1) balance)))))

(defn test-tx-create-contract [qassert]
  (let [compiler-out (eth/solidity fixture/foo-source)
        {:strs [code info]} (get compiler-out fixture/foo-name)
        from-address (test-account)
        latest-blocks (eth/watch "latest")
        address (eth/send-transaction {:from from-address
                                       :code code})
        mined-chan (eth-util/go-wait-mined latest-blocks from-address code)
        async-done (.async qassert)]
    (log/debug "Contract created at address:" address)
    (doto qassert
      (.ok (not (nil? address)))
      (.ok (= (count address) 42)))
    (log/debug "Mining contract")
    (go
      (let [{:keys [block tx]} (async/<! mined-chan)]
        (log/debug "Mined!" block tx)
        (eth/stop-watch latest-blocks)
        (doto qassert
          (.ok (some? block))
          (.ok (some? tx)))
        (async-done)))))

(defn test-multiply-contract [qassert]
  (let [from-address (test-account)
        multiply (fn [x]
                   (let [res (-> fixture/*multiply-7-contract*
                                 .-multiply
                                 (.call x))]
                     (log/debug "result:" (str res))
                     res))
        one (fn []
              (let [res (-> fixture/*multiply-7-contract* .-one (.call))]
                (log/debug "one result:" (str res))
                res))]
    (doto qassert
      (.ok (= "1" (str (one))))
      (.ok (= "14" (str (multiply 2))))
      (.ok (= "21" (str (multiply 3))))
      (.ok (= "28" (str (multiply 4)))))))

(defn run-local-tests [qunit]
  (go 
    (binding [fixture/*multiply-7-contract* 
              (async/<! (fixture/go-mine-contract 
                          fixture/multiply-7-name
                          fixture/multiply-7-source))]
      (doto qunit
        (.module (str (namespace ::x)))
        (.test "Solidity compiler" test-solidity-compiler)
        (.test "Account balance" test-account-balance)
        (.test "Create contract transaction" test-tx-create-contract)
        (.test "Call contract" test-multiply-contract))))
  (doto qunit
    (test-eth-async/run-local-tests)))
