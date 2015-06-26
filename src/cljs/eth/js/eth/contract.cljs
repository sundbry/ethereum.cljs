(ns eth.js.eth.contract
  (:require-macros
    [cljs.core.async.macros :refer [go]]
    [eth.js.eth.contract])
  (:require
    [cljs.core.async :as async]
    [shodan.console :as log :include-macros true]
    [eth.js.eth :as eth]
    [eth.js.eth.util :as eth-util]))

(defn build-solidity
  [contract-name src]
  (let [compiler-out (eth/solidity src)
        {:strs [code info] :as this-contract} (get compiler-out contract-name)
        contract-factory (eth/contract (get info "abiDefinition"))]
    (assoc this-contract "factory" contract-factory)))

(defn go-mine
  [{:strs [code] :as contract-info}
   from-address]
  (let [latest-blocks (eth/watch "latest")
        address (eth/send-transaction {:from from-address
                                       :code code})
        contract (.at factory address)
        tx {:from from-address}
        mined-chan (eth-util/go-wait-mined latest-blocks from-address code)]
    (go
      (let [{:keys [block tx]} (async/<! mined-chan)]
        (log/debug "Mined contract!" contract-name block tx)
        (eth/stop-watch latest-blocks)
        contract))))
