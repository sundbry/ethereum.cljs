(defproject ethereum.cljs "0.1.0-SNAPSHOT"
  :plugins [[lein-cljsbuild "1.0.4"]]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2740"]
                 [shodan "0.4.1"]] ; logging
  :source-paths ["test-server-src"]
  :cljsbuild
  {:builds
   {#_:dev 
    #_{:source-paths ["src"]
     :compiler
     {:output-to "build/cljs-web3.js"
      :optimizations :simple
      :pretty-print true
      :preamble ["bignumber.js" "ethereum.js"]
      :externs ["externs/bignumber.ext.js" "externs/ethereum.ext.js"]
      }}

    :test
    {:source-paths ["src" "test"]
     :compiler
     {:output-to "build/cljs-web3-test.js"
      :optimizations :simple
      :pretty-print true
      ;:source-map "build/cljs-web3-test.js.map"
      :preamble ["bignumber.js" "ethereum.min.js" "qunit-1.17.1.js"]
      :externs ["externs/bignumber.ext.js" "externs/ethereum.ext.js"]}}}

   ;:test-commands {"unit-tests"
   ;                [;"phantomjs" :runner
   ;                 ;"node" :node-runner
   ;                 "slimerjs" :runner
   ;                 "build/cljs-web3.js"]}
   }
  :profiles
  {:dev
   {:plugins []
    :dependencies [[ring "1.3.2"]
                   [compojure "1.3.1"]]
    :main http.server}})
