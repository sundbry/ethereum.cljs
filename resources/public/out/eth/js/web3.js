// Compiled by ClojureScript 0.0-2740 {}
goog.provide('eth.js.web3');
goog.require('cljs.core');
goog.require('shodan.console');
eth.js.web3.web3 = require("web3");
eth.js.web3.js_val = (function js_val(clj_or_js_dict){
if(cljs.core.map_QMARK_.call(null,clj_or_js_dict)){
return cljs.core.clj__GT_js.call(null,clj_or_js_dict);
} else {
if(cljs.core.vector_QMARK_.call(null,clj_or_js_dict)){
return cljs.core.clj__GT_js.call(null,clj_or_js_dict);
} else {
return clj_or_js_dict;

}
}
});
/**
* Returns the SHA3 of the given data.
*/
eth.js.web3.sha3 = (function sha3(hex_str){
return eth.js.web3.web3.sha3(hex_str);
});
/**
* Returns an ASCII string made from the data.
*/
eth.js.web3.to_ascii = (function to_ascii(hex_str){
return eth.js.web3.web3.toAscii(hex_str);
});
/**
* Returns data of the ASCII string _s, padded to _padding bytes
*/
eth.js.web3.from_ascii = (function() {
var from_ascii = null;
var from_ascii__1 = (function (input_str){
return eth.js.web3.web3.fromAscii(input_str);
});
var from_ascii__2 = (function (input_str,pad_length){
return eth.js.web3.web3.fromAscii(input_str,pad_length);
});
from_ascii = function(input_str,pad_length){
switch(arguments.length){
case 1:
return from_ascii__1.call(this,input_str);
case 2:
return from_ascii__2.call(this,input_str,pad_length);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
from_ascii.cljs$core$IFn$_invoke$arity$1 = from_ascii__1;
from_ascii.cljs$core$IFn$_invoke$arity$2 = from_ascii__2;
return from_ascii;
})()
;
/**
* Returns the decimal string representing the data _s (when interpreted as a big-endian integer).
*/
eth.js.web3.to_decimal = (function to_decimal(hex_str){
return eth.js.web3.web3.toDecimal(hex_str);
});
/**
* Returns the hex data string representing (in big-endian format) the decimal integer _s.
*/
eth.js.web3.from_decimal = (function from_decimal(big_int){
return eth.js.web3.web3.fromDecimal(big_int);
});
/**
* Should be called to reset state of web3. Resets everything except manager. Uninstalls all filters. Stops polling.
*/
eth.js.web3.reset = (function reset(){
return eth.js.web3.web3.reset();
});
eth.js.web3.set_provider = (function set_provider(provider){
console.debug("Setting web3 provider:",provider);

return eth.js.web3.web3.setProvider(provider);
});
eth.js.web3.http_provider = (function http_provider(uri){
var constructor = eth.js.web3.web3.providers.HttpSyncProvider;
return (new constructor(uri));
});
eth.js.web3.qt_provider = (function qt_provider(uri){
var constructor = eth.js.web3.web3.providers.QtSyncProvider;
return (new constructor(uri));
});

//# sourceMappingURL=web3.js.map