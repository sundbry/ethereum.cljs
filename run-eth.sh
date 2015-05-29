#!/bin/sh
geth --rpc --rpccorsdomain "*" --unlock primary --verbosity 3 --identity "ethereum.cljs" --mine --networkid "4353453510" --datadir "geth-data" 
