(ns eth.js.eth
  (:require
    [shodan.console :as log :include-macros true]
    [eth.js.web3 :as web3]))

(def rpc (.-eth web3/web3))

;; JS API
;; https://github.com/ethereum/wiki/wiki/JavaScript-API#web3ethcoinbase

(defn coinbase
  "Returns the coinbase address of the client."
  []
  (.-conbase rpc))

(defn listening?
  "Returns true iff the client is actively listening for network connections."
  []
  (.-listening rpc))

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
  (.-accounts rpc))

(defn peer-count
  "Returns the number of peers currently connected to the client."
  []
  (.-peerCount rpc))

(defn default-block
  "The default block number/age to use when querying state. When positive this is a block number, when 0 or negative it is a block age. -1 therefore means the most recently mined block, 0 means the block being currently mined (i.e. to include pending transactions). Defaults to -1."
  []
  (.-defaultBlock rpc))

(defn number
  "Returns the number of the most recent block."
  []
  (.-number rpc))

(defn balance-at 
  "Returns the balance of the account of address given by the address _a"
  [account-id]
  (.balanceAt rpc account-id))

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

(defn block
  "Returns the block with number _number."
  [blk-num]
  (.block rpc blk-num))

(defn transaction
  "Returns the transaction number _i from block with number _number."
  [blk-num tx-num]
  (.transaction rpc blk-num tx-num))

(defn txn
  [blk-num tx-num]
  (.transaction rpc blk-num tx-num))

(defn transact
  ([params]
   (log/debug "Transaction params:" params)
   (.transact rpc (clj->js params)))
  ([params callback]
   (log/debug "Transaction params:" params)
   (.transact rpc (clj->js params) callback)))

(defn call 
  [contract params]
  (log/debug "Call params:" params)
  (.call rpc contract (clj->js params)))

(defn uncle
  "Returns the uncle number _i from block with number _number. "
  [blk-num tx-num]
  (.uncle rpc blk-num tx-num))

(defn logs
  "Returns list of log messages"
  [filter-params]
  (.logs rpc (clj->js filter-params)))

(defn watch
  "Creates a watch object to notify when the state changes in a particular way, given by _filter. Filter may be a log filter object, as defined above. It may also be either 'chain' or 'pending' to watch for changes in the chain or pending transactions respectively.
    changed(_f): Installs a handler, _f, which is called when the state changes due to logs that fit _filter. These (new) log entries are passed as a parameter in a format equivalent to the return value of web3.eth.logs.
    logs(): Returns all of the log entries that fit _filter.
    uninstall(): Uninstalls the watch. Should always be called once it is done with."
  [filter-params]
  (.watch rpc (clj->js filter-params)))

(defn contract 
  "Construct a contract interface from data"
  [addr abi]
  (let [abi-json (clj->js abi)]
    (log/debug "Constructing contract with ABI:" abi-json)
    (.contract rpc addr abi-json)))

(defn compilers []
  "Returns list of available compilers"
  (seq (.compilers rpc)))

(defn solidity [source-code-str]
  (.solidity rpc source-code-str))

;; Custom functions

(defn currency-str [wei]
  (str (.toExponential (js/parseInt wei)) " wei"))

(defn return-value [result]
  (-> result .-c (nth 0)))
