(defproject ethereum.cljs "0.4.0-SNAPSHOT"
  :plugins [[lein-cljsbuild "1.0.4"]]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2740"]
                 [shodan "0.4.1"]] ; logging
  :source-paths ["src/clj" "src/cljs"]
  :clean-targets ^{:protect false} [:target-path "resources/public/out" "resources/public/ethereum.cljs.js"]
  :cljsbuild
  {:builds
   {:dev
    {:source-paths ["src/cljs" "test"]
     :compiler
     {:output-dir "resources/public/out"
      :output-to "resources/public/ethereum.cljs.js"
      :asset-path "out"
      :optimizations :none ; breaks some functions in ethereum.js on :advanced 
      :source-map true
      :main "eth.js.test"
      ;:pretty-print true
      ;:source-map "build/cljs-web3-test.js.map"
      ;:preamble ["bignumber.js" "web3.js" "qunit-1.17.1.js"]
      :externs ["externs/bignumber.ext.js" "externs/ethereum.ext.js"]}}}}

  :profiles
  {:dev
   {:plugins []
    :dependencies [[ring "1.3.2"]
                   [compojure "1.3.1"]]
    :main http.server}})
