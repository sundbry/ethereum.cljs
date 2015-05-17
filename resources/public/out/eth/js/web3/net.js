// Compiled by ClojureScript 0.0-2740 {}
goog.provide('eth.js.web3.net');
goog.require('cljs.core');
goog.require('eth.js.web3');
eth.js.web3.net.rpc = eth.js.web3.web3.net;
/**
* Returns true iff the client is actively listening for network connections.
*/
eth.js.web3.net.listening_QMARK_ = (function listening_QMARK_(){
return eth.js.web3.net.rpc.listening;
});
/**
* Returns the number of peers currently connected to the client.
*/
eth.js.web3.net.peer_count = (function peer_count(){
return eth.js.web3.net.rpc.peerCount;
});

//# sourceMappingURL=net.js.map