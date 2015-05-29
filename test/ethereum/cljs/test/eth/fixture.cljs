(ns eth.js.test.eth.fixture
  (:require
    [cljs.core.async :as async]
    [shodan.console :as log :include-macros true]
    [shodan.inspection :refer [inspect]]
    [eth.js.eth :as eth]
    [eth.js.eth.async :as eth-async]
    [eth.js.eth.util :as eth-util])
  (:require-macros
    [cljs.core.async.macros :refer [go go-loop]]))

(def ^:dynamic *multiply-7-contract* nil)
(def ^:dynamic *counter-contract* nil)

;; Foo

(def foo-name "Foo")
(def foo-source
  "contract Foo {
     function foo() returns (uint32 bar) {
       return 2;
     }
   }")

;; Multiply7

(def multiply-7-name
  "MultiplySeven")

(def multiply-7-source
  "contract MultiplySeven {
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
               "type" "uint32"}]}]))

;; Counter

(def counter-name "Counter")
(def counter-source
  "contract Counter {
    uint count;

    function Counter() {
      count = 0;
    }

    function increment() returns (uint) {
      count = count + 1;
      return count;
    }

    function increment_n(uint n) returns (uint) {
      count = count + n;
      return count;
    }
  }")

(def test-account
  (memoize
    (fn []
      (let [acct (first (eth/accounts))]
        (log/info "Using test account:" acct) 
        acct))))

(defn go-mine-multiply-7
  []
  (let [compiler-out (eth/solidity multiply-7-source)
        {:strs [code info]} (get compiler-out multiply-7-name)
        from-address (test-account)
        latest-blocks (eth/watch "latest")
        address (eth/send-transaction {:from from-address
                                       :code code})
        contract-factory (eth/contract multiply-7-abi)
        contract (.at contract-factory address)
        tx {:from from-address}
        mined-chan (eth-util/go-wait-mined latest-blocks from-address code)]
    (go
      (let [{:keys [block tx]} (async/<! mined-chan)]
        (log/debug "Mined!" block tx)
        (eth/stop-watch latest-blocks)
        contract))))

(defn go-mine-contract
  [contract-name source]
  (let [compiler-out (eth/solidity source)
        {:strs [code info]} (get compiler-out contract-name)
        from-address (test-account)
        latest-blocks (eth/watch "latest")
        address (eth/send-transaction {:from from-address
                                       :code code})
        contract-factory (eth/contract (get info "abiDefinition"))
        contract (.at contract-factory address)
        tx {:from from-address}
        mined-chan (eth-util/go-wait-mined latest-blocks from-address code)]
    (go
      (let [{:keys [block tx]} (async/<! mined-chan)]
        (log/debug "Mined!" block tx)
        (eth/stop-watch latest-blocks)
        contract))))
