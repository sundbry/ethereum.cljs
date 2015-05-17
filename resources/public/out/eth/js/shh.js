// Compiled by ClojureScript 0.0-2740 {}
goog.provide('eth.js.shh');
goog.require('cljs.core');
goog.require('eth.js.web3');
goog.require('shodan.console');
eth.js.shh.rpc = eth.js.web3.web3.shh;
eth.js.shh.message_defaults = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ttl","ttl",-1115275118),(300)], null);
eth.js.shh.message = (function message(msg_opts){

return cljs.core.merge.call(null,eth.js.shh.message_defaults,msg_opts);
});
/**
* Post a whsiper message.
* from: identity of sender
* to: identity of receiver
* payload: message payload
* ttl: time to live
* workToProve: TODO
* topic: string or array of strings, with message topics
*/
eth.js.shh.post = (function post(params_dict){
var js_params = cljs.core.clj__GT_js.call(null,params_dict);
return eth.js.shh.rpc.post(js_params);
});
/**
* Creates a new identity address.
*/
eth.js.shh.new_identity = (function new_identity(){
return eth.js.shh.rpc.newIdentity();
});

//# sourceMappingURL=shh.js.map