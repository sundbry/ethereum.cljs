(ns eth.js.eth.async
  (:require
    [cljs.core.async :as async]
    [eth.js.eth :as eth]))

(defn get-block
  [block-id tx-details?]
  (let [out (async/chan 1)]
    (eth/get-block block-id tx-details? 
                   (fn [error result]
                     (async/put! out [error result])))
    out))

(defn get-transaction
  [tx-id]
  (let [out (async/chan 1)]
    (eth/get-transaction tx-id
                         (fn [error result]
                           (async/put! out [error result])))
    out))

(defn send-transaction
  [params]
  (let [out (async/chan 1)]
    (eth/send-transaction params
                          (fn [error result]
                            (async/put! out [error result])))
    out))
