(ns eth.js.macros)

#_(defn call-contract-fn [contract fn-caller]
     (log/debug "Calling contract at" (get contract "address"))
     (let [callee (eth/call contract {})
                   result (fn-caller callee)]
           (log/debug "Call result:" result)
           result))

#_(defn make-caller [fn-sym arg-symbols]
  (let [memfn-sym (symbol (str "." fn-sym))]
    (list 'fn ['staged-call] 
          ; '(.foo staged-call a b c)
           (cons memfn-sym 
                 (cons 'staged-call arg-symbols)))))

#_(defmacro invoker [fn-sym arg-symbols]
  (let [memfn-sym (symbol (str "." fn-sym))]
    (list 'fn ['staged-call] 
          ; '(.foo staged-call a b c)
           (cons memfn-sym 
                 (cons 'staged-call arg-symbols)))))
#_(defmacro invoke
  [stage-call fn-symbol & args]
  (let [fn-symbol (str fn-symbol)]
    `(let [staged-call# ~stage-call
           lambda-fn# ~(make-caller fn-symbol args)
           result# (lambda-fn# staged-call#)]
       result#)))

#_(macroexpand `(invoke {:contract :stub} ~'foo 1))


(defmacro call-async
  [contract-sym fn-sym txn-sym & args]
  (let [fn-sym (symbol (str ".-" (name (second fn-sym))))]
    fn-sym
    `(do
       (let [ch# (cljs.core.async/chan 1)
             cb# (fn [error# result#] (cljs.core.async/put! ch# (or error# result#)))]
         (.call (~fn-sym ~contract-sym) ~@args ~txn-sym cb#)
         ch#))))
