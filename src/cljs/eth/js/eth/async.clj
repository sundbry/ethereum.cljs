(ns eth.js.eth.async)

(defmacro call-async
  [contract-sym fn-sym txn-sym & args]
  (let [function-sym (symbol (str ".-" (name fn-symbol)))]
    `(do
       (let [ch# (cljs.core.async/chan 1)
             cb# (fn [error# result#] (cljs.core.async/put! ch# (or error# result#)))]
         (.call (~function-sym ~contract-sym) ~@args ~txn-sym cb#)
         invocation
         ch#))))
