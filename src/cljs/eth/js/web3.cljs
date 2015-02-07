(ns eth.js.web3
  (:require
    [shodan.console :as log :include-macros true]))

(def web3 (js/require "web3"))

;; Web3 Javascript API
;; from https://github.com/ethereum/wiki/wiki/JavaScript-API

(defn sha3
  "Returns the SHA3 of the given data."
  [hex-str]
  (.sha3 web3 hex-str))

(defn to-ascii
  "Returns an ASCII string made from the data."
  [hex-str]
  (.toAscii web3 hex-str))

(defn from-ascii
  "Returns data of the ASCII string _s, padded to _padding bytes"
  ([input-str]
   (.fromAscii web3 input-str))
  ([input-str pad-length]
   (.fromAscii web3 input-str pad-length)))

(defn to-decimal
  "Returns the decimal string representing the data _s (when interpreted as a big-endian integer)."
  [hex-str]
  (.toDecimal web3 hex-str))

(defn from-decimal
  "Returns the hex data string representing (in big-endian format) the decimal integer _s."
  [big-int]
  (.fromDecimal web3 big-int))

(defn reset
  "Should be called to reset state of web3. Resets everything except manager. Uninstalls all filters. Stops polling."
  []
  (.reset web3))

(defn set-provider [provider]
  (log/debug "Setting web3 provider:" provider)
  (.setProvider web3 provider))


;; Providers

(defn http-provider [uri]
  (let [constructor (-> web3 .-providers .-HttpSyncProvider)]
    (constructor.  uri)))

(defn qt-provider [uri]
  (let [constructor (-> web3 .-providers .-QtSyncProvider)]
    (constructor.  uri)))
