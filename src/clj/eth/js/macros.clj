(ns eth.js.macros)

#_(defn call-contract-fn [contract fn-caller]
     (log/debug "Calling contract at" (get contract "address"))
     (let [callee (eth/call contract {})
                   result (fn-caller callee)]
           (log/debug "Call result:" result)
           result))

(defn make-caller [fn-sym arg-symbols]
  (let [memfn-sym (symbol (str "." fn-sym))]
    (list 'fn ['staged-call] 
          ; '(.foo staged-call a b c)
           (cons memfn-sym 
                 (cons 'staged-call arg-symbols)))))

(defmacro invoker [fn-sym arg-symbols]
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

(macroexpand `(invoke {:contract :stub} ~'foo 1))
