(ns eth.js.test.eth.async
  (:require-macros
    [cljs.core.async.macros :refer [go go-loop]])
  (:require
    [cljs.core.async :as async]
    [shodan.console :as log :include-macros true]
    [eth.js.eth :as eth] 
    [eth.js.eth.util :as eth-util]
    [eth.js.test.eth.fixture :refer [multiply-7-source multiply-7-abi]]))
