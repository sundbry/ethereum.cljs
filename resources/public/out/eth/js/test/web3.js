// Compiled by ClojureScript 0.0-2740 {}
goog.provide('eth.js.test.web3');
goog.require('cljs.core');
goog.require('eth.js.web3');
goog.require('shodan.console');
eth.js.test.web3.test_sha3 = (function test_sha3(qassert){
var hash_str = eth.js.web3.sha3.call(null,eth.js.web3.from_ascii.call(null,"Hello!! McFly!~"));
console.debug("hash is: ",hash_str);

var G__5470 = qassert;
G__5470.ok(!((hash_str == null)));

G__5470.ok(cljs.core._EQ_.call(null,(66),hash_str.length));

return G__5470;
});
eth.js.test.web3.run_local_tests = (function run_local_tests(qunit){
var G__5472 = qunit;
G__5472.test("SHA3",eth.js.test.web3.test_sha3);

return G__5472;
});

//# sourceMappingURL=web3.js.map