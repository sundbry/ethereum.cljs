(ns eth.js.eth.util
  (:require
    [cljs.core.async :as async]
    [shodan.console :as log :include-macros true]
    [eth.js.eth :as eth]
    [eth.js.eth.async :as eth-async])
  (:require-macros
    [cljs.core.async.macros :refer [go go-loop]]
    [eth.js.eth.util]))

(def ^:private bignum-10e18 (js/BigNumber. "10e18"))

(defn ether
  "Convert Ether to BigNumber value in wei"
  [n]
  (let [n (if (instance? js/BigNumber n)
            n
            (js/BigNumber. n))]
    (.times n bignum-10e18)))

(defn currency-str 
  "Convert wei units to a human readable string with units."
  [wei]
  (str (.toExponential (js/parseInt wei)) " wei"))

(defn go-wait-mined 
  "Return a channel that will return the transaction when a contract tx is mined."
  [latest-blocks-chan from-address contract-code]
  (log/debug "waiting to mine contract from:" from-address)
  (go-loop
    []
    (let [block-hash (async/<! (:result latest-blocks-chan))
          [error block-info] (async/<! (eth-async/get-block block-hash true))]
      (let [mined-tx (.reduce (.-transactions block-info)
                              (fn [mined-tx block-tx]
                                (or mined-tx
                                    mined-tx
                                    (when (and (= (.-from block-tx)
                                                  from-address)
                                               (not= (.indexOf (.-input block-tx) contract-code)
                                                     -1))
                                      block-tx)))
                              nil)]
        (if (some? mined-tx)
          {:block block-info :tx mined-tx}
          (recur))))))

(defn build-solidity
  [contract-name src]
  (let [compiler-out (eth/solidity src)
        {:strs [code info] :as this-contract} (get compiler-out contract-name)
        contract-factory (eth/contract (get info "abiDefinition"))]
    (assoc this-contract
           "factory" contract-factory
           "name" contract-name)))

(defn go-mine
  [{:strs [code factory name] :as contract-info}
   from-address]
  (let [latest-blocks (eth/watch "latest")
        address (eth/send-transaction {:from from-address
                                       :code code})
        contract (.at factory address)
        tx {:from from-address}
        mined-chan (go-wait-mined latest-blocks from-address code)]
    (go
      (let [{:keys [block tx]} (async/<! mined-chan)]
        (log/debug "Mined contract!" name block tx)
        (eth/stop-watch latest-blocks)
        contract))))
