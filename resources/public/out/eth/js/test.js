// Compiled by ClojureScript 0.0-2740 {}
goog.provide('eth.js.test');
goog.require('cljs.core');
goog.require('eth.js.web3');
goog.require('eth.js.eth');
goog.require('eth.js.web3.net');
goog.require('shodan.console');
goog.require('eth.js.test.eth');
goog.require('eth.js.test.shh');
goog.require('eth.js.test.web3');
eth.js.test.init_fixture = (function init_fixture(){
return eth.js.web3.set_provider.call(null,eth.js.web3.http_provider.call(null,"http://localhost:30304"));
});
eth.js.test.test_lib = (function test_lib(qassert){
return qassert.ok(cljs.core.some_QMARK_.call(null,eth.js.web3.web3),"Passed!");
});
eth.js.test.test_connect = (function test_connect(qassert){
qassert.ok(eth.js.web3.net.listening_QMARK_.call(null),"Listening for connections");

return qassert.ok((eth.js.web3.net.peer_count.call(null) > (0)),"More than 0 peers");
});
eth.js.test.run_local_tests = (function run_local_tests(qunit){
var G__5476 = qunit;
G__5476.test("Test require",eth.js.test.test_lib);

G__5476.test("Connect test",eth.js.test.test_connect);

return G__5476;
});
eth.js.test.run_tests = (function run_tests(){
console.info("Starting tests");

eth.js.test.init_fixture.call(null);

var G__5478_5479 = QUnit;
eth.js.test.run_local_tests.call(null,G__5478_5479);

eth.js.test.web3.run_local_tests.call(null,G__5478_5479);

eth.js.test.eth.run_local_tests.call(null,G__5478_5479);

eth.js.test.shh.run_local_tests.call(null,G__5478_5479);


return console.info("Tests complete");
});
goog.exportSymbol('eth.js.test.run_tests', eth.js.test.run_tests);
eth.js.test.run_tests.call(null);

//# sourceMappingURL=test.js.map