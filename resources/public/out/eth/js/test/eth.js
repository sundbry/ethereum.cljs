// Compiled by ClojureScript 0.0-2740 {}
goog.provide('eth.js.test.eth');
goog.require('cljs.core');
goog.require('eth.js.eth');
goog.require('shodan.console');
eth.js.test.eth.multiply_7_source = "contract test {\n                          function multiply(uint a) returns(uint d) {\n                            return a * 7;\n                          }\n                        }";
eth.js.test.eth.multiply_7_abi = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 4, ["name","multiply(uint256)","type","function","inputs",new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 2, ["name","a","type","uint256"], null)], null),"outputs",new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentArrayMap(null, 2, ["name","d","type","uint256"], null)], null)], null)], null);
eth.js.test.eth.make_solidity_contract = (function make_solidity_contract(source,abi){
var code = eth.js.eth.solidity.call(null,source);
var address = eth.js.eth.transact.call(null,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"code","code",1586293142),code], null));
var contract = eth.js.eth.contract.call(null,address,abi);
console.debug("Contract constructed",contract);

return contract;
});
eth.js.test.eth.call_contract_fn = (function call_contract_fn(contract,fn_caller){
console.debug("Calling contract at",cljs.core.get.call(null,contract,"address"));

var callee = eth.js.eth.call.call(null,contract,cljs.core.PersistentArrayMap.EMPTY);
var result = fn_caller.call(null,callee);
console.debug("Call result:",result);

return result;
});
eth.js.test.eth.test_serpent_compiler = (function test_serpent_compiler(qassert){
var code = eth.js.eth.solidity.call(null,eth.js.test.eth.multiply_7_source);
console.debug("Multiply-7 compiled to bytecode:",code);

var G__5450 = qassert;
G__5450.ok(!((code == null)));

G__5450.ok((cljs.core.count.call(null,code) > (20)));

return G__5450;
});
eth.js.test.eth.test_tx_create_contract = (function test_tx_create_contract(qassert){
var code = eth.js.eth.solidity.call(null,eth.js.test.eth.multiply_7_source);
var address = eth.js.eth.transact.call(null,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"code","code",1586293142),code], null));
console.debug("Contract created at address: ",address);

var G__5452 = qassert;
G__5452.ok(!((address == null)));

G__5452.ok(cljs.core._EQ_.call(null,cljs.core.count.call(null,address),(42)));

return G__5452;
});
eth.js.test.eth.test_contract_js_api = (function test_contract_js_api(qassert){
var code = eth.js.eth.solidity.call(null,eth.js.test.eth.multiply_7_source);
var address = eth.js.eth.transact.call(null,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"code","code",1586293142),code], null));
var contract = eth.js.test.eth.make_solidity_contract.call(null,eth.js.test.eth.multiply_7_source,eth.js.test.eth.multiply_7_abi);
var G__5454 = qassert;
G__5454.ok(cljs.core.some_QMARK_.call(null,contract.address));

return G__5454;
});
eth.js.test.eth.test_multiply_contract = (function test_multiply_contract(qassert){
var contract = eth.js.test.eth.make_solidity_contract.call(null,eth.js.test.eth.multiply_7_source,eth.js.test.eth.multiply_7_abi);
var multiply = ((function (contract){
return (function (x){
return eth.js.eth.return_value.call(null,contract.call().multiply(x));
});})(contract))
;
var G__5456 = qassert;
G__5456.ok(cljs.core._EQ_.call(null,(0),multiply.call(null,(0))));

G__5456.ok(cljs.core._EQ_.call(null,(21),multiply.call(null,(3))));

G__5456.ok(cljs.core._EQ_.call(null,(28),multiply.call(null,(4))));

return G__5456;
});
eth.js.test.eth.run_local_tests = (function run_local_tests(qunit){
var G__5458 = qunit;
G__5458.test("Serpent compiler",eth.js.test.eth.test_serpent_compiler);

G__5458.test("Create contract transaction",eth.js.test.eth.test_tx_create_contract);

G__5458.test("Instantiate contract",eth.js.test.eth.test_contract_js_api);

G__5458.test("Run contract",eth.js.test.eth.test_multiply_contract);

return G__5458;
});

//# sourceMappingURL=eth.js.map