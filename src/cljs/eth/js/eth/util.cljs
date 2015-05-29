(ns eth.js.eth.util
  (:require
    [cljs.core.async :as async]
    [shodan.console :as log :include-macros true]
    [eth.js.eth.async :as eth-async])
  (:require-macros
    [cljs.core.async.macros :refer [go go-loop]]))

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
                                (log/debug "block-tx:" block-tx)
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

