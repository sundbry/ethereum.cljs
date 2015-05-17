// Compiled by ClojureScript 0.0-2740 {}
goog.provide('eth.js.test.shh');
goog.require('cljs.core');
goog.require('eth.js.shh');
goog.require('eth.js.web3');
goog.require('shodan.console');
eth.js.test.shh.get_idents = (function get_idents(){
var idents = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [eth.js.shh.new_identity.call(null),eth.js.shh.new_identity.call(null)], null);
return console.info("Test idents created:",idents);
});
eth.js.test.shh.test_post = (function test_post(qassert){
var data_str = eth.js.web3.from_ascii.call(null,"Hello world!");
var vec__5463 = eth.js.test.shh.get_idents.call(null);
var to = cljs.core.nth.call(null,vec__5463,(0),null);
var from = cljs.core.nth.call(null,vec__5463,(1),null);
var msg = eth.js.shh.message.call(null,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"payload","payload",-383036092),data_str,new cljs.core.Keyword(null,"to","to",192099007),to,new cljs.core.Keyword(null,"from","from",1815293044),from], null));
var post_result = eth.js.shh.post.call(null,msg);
console.debug("post result is:",post_result);

var G__5464 = qassert;
G__5464.ok(!((post_result == null)));

return G__5464;
});
eth.js.test.shh.run_local_tests = (function run_local_tests(qunit){
var G__5466 = qunit;
G__5466.test("post()",eth.js.test.shh.test_post);

return G__5466;
});

//# sourceMappingURL=shh.js.map