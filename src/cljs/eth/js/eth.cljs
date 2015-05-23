(ns eth.js.eth
  (:require
    [cljs.core.async :as async]
    [shodan.console :as log :include-macros true]
    [eth.js.web3 :as web3 :refer [js-val]]))

(def rpc (.-eth web3/web3))

;; JS API
;; https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethcoinbase

(defn coinbase
  "Returns the coinbase address of the client."
  []
  (.-conbase rpc))

(defn mining?
  "Returns true iff actively mining"
  []
  (.-mining rpc))

(defn gas-price
  "Returns estimated gas price"
  []
  (.-gasPrice rpc))

(defn accounts
  "Returns the special key-pair list object corresponding to the address of each of the accounts owned by the client that this ÐApp has access to."
  []
  (js->clj (.-accounts rpc)))

(defn default-block
  "The default block number/age to use when querying state. When positive this is a block number, when 0 or negative it is a block age. -1 therefore means the most recently mined block, 0 means the block being currently mined (i.e. to include pending transactions). Defaults to -1."
  []
  (.-defaultBlock rpc))

(defn number
  "Returns the number of the most recent block."
  []
  (.-number rpc))

(defn get-balance
  "Returns the balance of the account of address given by the address _a"
  [account-id]
  (.getBalance rpc account-id))

(defn state-at 
  "Returns the value in storage at position given by the string _s of the account of address given by the address _a."
  [acct-addr store-addr]
  (.stateAt rpc acct-addr store-addr))

(defn storage-at
  "Dumps storage as json object."
  [acct-addr]
  (.storageAt rpc acct-addr))

(defn count-at
  "Returns the number of transactions send from the account of address given by _a."
  [acct-addr]
  (.countAt rpc acct-addr))

(defn code-at
  "Returns true if the account of address given by _a is a contract-account."
  [acct-addr]
  (.codeAt rpc acct-addr))

(defn contract?
  "Returns true iff the account at the address is executable."
  [acct-addr]
  (let [code (code-at acct-addr)]
    (some? code)))

(defn get-block
  "Returns the block with the given hash."
  ([block-id] (.getBlock rpc block-id))
  ([block-id tx-details?] (.getBlock rpc block-id tx-details?))
  ([block-id tx-details? cb] (.getBlock rpc block-id tx-details? cb)))

(defn transaction
  "Returns the transaction number _i from block with number _number."
  [blk-num tx-num]
  (.transaction rpc blk-num tx-num))

(defn txn
  [blk-num tx-num]
  (.transaction rpc blk-num tx-num))

(defn send-transaction
  ([params]
   (log/debug "Transaction params:" params)
   (.sendTransaction rpc (js-val params)))
  ([params callback]
   (log/debug "Transaction params:" params)
   (.sendTransaction rpc (js-val params) callback)))

(defn call 
  ([contract]
   (log/debug "doing call")
   (.call rpc contract))
  ([contract params]
   (log/debug "Call params:" (js-val params))
   (.call rpc contract (js-val params))))

(defn uncle
  "Returns the uncle number _i from block with number _number. "
  [blk-num tx-num]
  (.uncle rpc blk-num tx-num))

(defn logs
  "Returns list of log messages"
  [filter-params]
  (.logs rpc (js-val filter-params)))

(defn watch
  "Returns a dictionary of event channels."
  [filter-params]
  (let [filt (.filter rpc (js-val filter-params))
        error-ch (async/chan 1)
        result-ch (async/chan 1)]
    (.watch filt (fn [error block-info]
                   (when (some? error)
                     (async/put! error-ch error))
                   (when (some? block-info)
                     (async/put! result-ch block-info))))
    {:error error-ch
     :result result-ch
     :filter filt}))

(defn stop-watch
  "Stops watching a channel dictionary."
  [{:keys [error result filter]}]
  (.stopWatching filter)
  (async/close! result)
  (async/close! error)
  nil)

(defn contract 
  "Construct a contract interface from data"
  [abi]
  (let [abi-json (js-val abi)]
    (log/debug "Constructing contract with ABI:" (.stringify js/JSON abi-json))
    (.contract rpc abi-json)))

(defn compilers []
  "Returns list of available compilers"
  (seq (.getCompilers rpc)))

(defn solidity [source-code-str]
  (js->clj (.solidity (.-compile rpc) source-code-str)))

;; Custom functions

(defn currency-str [wei]
  (str (.toExponential (js/parseInt wei)) " wei"))
