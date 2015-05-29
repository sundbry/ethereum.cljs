(ns eth.js.test.eth.fixture
  (:require
    [shodan.console :as log :include-macros true]
    [eth.js.eth :as eth]))

(def multiply-7-contract
  "Test")

(def multiply-7-source
  "contract Test {
     function multiply(uint256 a) returns (uint256 d) {
       return a * 7;
     }
     function one() returns (uint32 r) {
       return 1;
     }
   }")

(def multiply-7-abi 
  (clj->js [{"name" "multiply"
             "type" "function"
             "inputs"
             [{"name" "a"
               "type" "uint256"}]
             "outputs"
             [{"name" "d"
               "type" "uint256"}]}
            {"name" "one"
             "type" "function"
             "inputs"
             []
             "outputs"
             [{"name" "r"
               "type" "uint32"}]}]))

(def test-account
  (memoize
    (fn []
      (let [acct (first (eth/accounts))]
        (log/info "Using test account:" acct) 
        acct))))
