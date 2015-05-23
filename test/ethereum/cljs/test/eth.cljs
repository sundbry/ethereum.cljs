(ns eth.js.test.eth
  ;(:require-macros [cemerick.cljs.test :refer [is deftest]])
  (:require-macros
    [cljs.core.async.macros :refer [go go-loop]]
    [eth.js.macros :as macro])
  (:require
    [cljs.core.async :as async]
    ;[cemerick.cljs.test :as t]
    [shodan.console :as log :include-macros true]
    [eth.js.eth :as eth]))

(def multiply-7-source
  "contract Test {
     function multiply(uint256 a) returns (uint256 d) {
       return a * 7;
     }
     function one() returns (uint32 r) {
       return 1;
     }
   }")


(def multiply-7-abi 
  (clj->js [{"name" "multiply"
             "type" "function"
             "inputs"
             [{"name" "a"
               "type" "uint256"}]
             "outputs"
             [{"name" "d"
               "type" "uint256"}]}
            {"name" "one"
             "type" "function"
             "inputs"
             []
             "outputs"
             [{"name" "r"
               "type" "uint32"}]}
            ]))

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
        from-address (first (eth/accounts))
        address (eth/send-transaction {:from from-address
                                       :code code})]
    (log/debug "Contract created at address:" address)
    (doto qassert
      (.ok (not (nil? address)))
      (.ok (= (count address) 42)))))

(defn test-contract-js-api [qassert]
  (let [{:strs [code info]} (eth/solidity multiply-7-source)
        from-address (first (eth/accounts))
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
        from-address (first (eth/accounts))
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
        async-done (.async qassert)]
    (go-loop []
        (let [block-hash (async/<! (:result latest-blocks))
              block-info (eth/get-block block-hash true)
              mined? (.reduce (.-transactions block-info)
                              (fn [mined block-tx]
                                (log/debug "block-tx:" block-tx)
                                (or mined (and (= (.-from block-tx)
                                                  from-address)
                                               (not= (.indexOf (.-input block-tx) code)
                                                     -1))))
                              false)]
          (if mined?
            (do
              (log/debug "Mined!")
              (eth/stop-watch latest-blocks)
              (doto qassert
                (.ok (= "1" (str (one))))
                (.ok (= "14" (str (multiply 2))))
                #_(.ok (= "21" (str (multiply 3))))
                #_(.ok (= "28" (str (multiply 4)))))
              (async-done))
            (recur))))))

(defn run-local-tests [qunit]
  (doto qunit
    (.test "Solidity compiler" test-solidity-compiler)
    (.test "Create contract transaction" test-tx-create-contract)
    (.test "Instantiate contract" test-contract-js-api)
    (.test "Run contract" test-multiply-contract)))
