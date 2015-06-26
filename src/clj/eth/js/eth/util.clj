(ns eth.js.eth.util
  (:require [clojure.java.io :as io]))

(defmacro source
  "Opens a reader on file and returns it's contents."
  [file]
  (slurp (io/resource file)))
