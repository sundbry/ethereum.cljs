(ns eth.js.eth.async)

(defmacro call
  [contract-sym fn-sym txn-sym & args]
  (let [fn-sym (symbol (str ".-" (name (second fn-sym))))]
    fn-sym
    `(do
       (let [ch# (cljs.core.async/chan 1)
             cb# (fn [error# result#] (cljs.core.async/put! ch# (or error# result#)))]
         (.call (~fn-sym ~contract-sym) ~@args ~txn-sym cb#)
         ch#))))
