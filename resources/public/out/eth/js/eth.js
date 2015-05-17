// Compiled by ClojureScript 0.0-2740 {}
goog.provide('eth.js.eth');
goog.require('cljs.core');
goog.require('eth.js.web3');
goog.require('shodan.console');
eth.js.eth.rpc = eth.js.web3.web3.eth;
/**
* Returns the coinbase address of the client.
*/
eth.js.eth.coinbase = (function coinbase(){
return eth.js.eth.rpc.conbase;
});
/**
* Returns true iff actively mining
*/
eth.js.eth.mining_QMARK_ = (function mining_QMARK_(){
return eth.js.eth.rpc.mining;
});
/**
* Returns estimated gas price
*/
eth.js.eth.gas_price = (function gas_price(){
return eth.js.eth.rpc.gasPrice;
});
/**
* Returns the special key-pair list object corresponding to the address of each of the accounts owned by the client that this ÐApp has access to.
*/
eth.js.eth.accounts = (function accounts(){
return cljs.core.js__GT_clj.call(null,eth.js.eth.rpc.accounts);
});
/**
* The default block number/age to use when querying state. When positive this is a block number, when 0 or negative it is a block age. -1 therefore means the most recently mined block, 0 means the block being currently mined (i.e. to include pending transactions). Defaults to -1.
*/
eth.js.eth.default_block = (function default_block(){
return eth.js.eth.rpc.defaultBlock;
});
/**
* Returns the number of the most recent block.
*/
eth.js.eth.number = (function number(){
return eth.js.eth.rpc.number;
});
/**
* Returns the balance of the account of address given by the address _a
*/
eth.js.eth.balance_at = (function balance_at(account_id){
return eth.js.eth.rpc.balanceAt(account_id);
});
/**
* Returns the value in storage at position given by the string _s of the account of address given by the address _a.
*/
eth.js.eth.state_at = (function state_at(acct_addr,store_addr){
return eth.js.eth.rpc.stateAt(acct_addr,store_addr);
});
/**
* Dumps storage as json object.
*/
eth.js.eth.storage_at = (function storage_at(acct_addr){
return eth.js.eth.rpc.storageAt(acct_addr);
});
/**
* Returns the number of transactions send from the account of address given by _a.
*/
eth.js.eth.count_at = (function count_at(acct_addr){
return eth.js.eth.rpc.countAt(acct_addr);
});
/**
* Returns true if the account of address given by _a is a contract-account.
*/
eth.js.eth.code_at = (function code_at(acct_addr){
return eth.js.eth.rpc.codeAt(acct_addr);
});
/**
* Returns true iff the account at the address is executable.
*/
eth.js.eth.contract_QMARK_ = (function contract_QMARK_(acct_addr){
var code = eth.js.eth.code_at.call(null,acct_addr);
return cljs.core.some_QMARK_.call(null,code);
});
/**
* Returns the block with number _number.
*/
eth.js.eth.block = (function block(blk_num){
return eth.js.eth.rpc.block(blk_num);
});
/**
* Returns the transaction number _i from block with number _number.
*/
eth.js.eth.transaction = (function transaction(blk_num,tx_num){
return eth.js.eth.rpc.transaction(blk_num,tx_num);
});
eth.js.eth.txn = (function txn(blk_num,tx_num){
return eth.js.eth.rpc.transaction(blk_num,tx_num);
});
eth.js.eth.transact = (function() {
var transact = null;
var transact__1 = (function (params){
console.debug("Transaction params:",params);

return eth.js.eth.rpc.transact(eth.js.web3.js_val.call(null,params));
});
var transact__2 = (function (params,callback){
console.debug("Transaction params:",params);

return eth.js.eth.rpc.transact(eth.js.web3.js_val.call(null,params),callback);
});
transact = function(params,callback){
switch(arguments.length){
case 1:
return transact__1.call(this,params);
case 2:
return transact__2.call(this,params,callback);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
transact.cljs$core$IFn$_invoke$arity$1 = transact__1;
transact.cljs$core$IFn$_invoke$arity$2 = transact__2;
return transact;
})()
;
eth.js.eth.call = (function() {
var call = null;
var call__1 = (function (contract){
console.debug("doing call");

return eth.js.eth.rpc.call(contract);
});
var call__2 = (function (contract,params){
console.debug("Call params:",eth.js.web3.js_val.call(null,params));

return eth.js.eth.rpc.call(contract,eth.js.web3.js_val.call(null,params));
});
call = function(contract,params){
switch(arguments.length){
case 1:
return call__1.call(this,contract);
case 2:
return call__2.call(this,contract,params);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
call.cljs$core$IFn$_invoke$arity$1 = call__1;
call.cljs$core$IFn$_invoke$arity$2 = call__2;
return call;
})()
;
/**
* Returns the uncle number _i from block with number _number.
*/
eth.js.eth.uncle = (function uncle(blk_num,tx_num){
return eth.js.eth.rpc.uncle(blk_num,tx_num);
});
/**
* Returns list of log messages
*/
eth.js.eth.logs = (function logs(filter_params){
return eth.js.eth.rpc.logs(eth.js.web3.js_val.call(null,filter_params));
});
/**
* Creates a watch object to notify when the state changes in a particular way, given by _filter. Filter may be a log filter object, as defined above. It may also be either 'chain' or 'pending' to watch for changes in the chain or pending transactions respectively.
* changed(_f): Installs a handler, _f, which is called when the state changes due to logs that fit _filter. These (new) log entries are passed as a parameter in a format equivalent to the return value of web3.eth.logs.
* logs(): Returns all of the log entries that fit _filter.
* uninstall(): Uninstalls the watch. Should always be called once it is done with.
*/
eth.js.eth.watch = (function watch(filter_params){
return eth.js.eth.rpc.watch(eth.js.web3.js_val.call(null,filter_params));
});
/**
* Construct a contract interface from data
*/
eth.js.eth.contract = (function contract(addr,abi){
var abi_json = eth.js.web3.js_val.call(null,abi);
console.debug("Constructing contract with ABI:",abi_json);

return eth.js.eth.rpc.contract(addr,abi_json);
});
eth.js.eth.compilers = (function compilers(){

return cljs.core.seq.call(null,eth.js.eth.rpc.compilers());
});
eth.js.eth.solidity = (function solidity(source_code_str){
return eth.js.eth.rpc.solidity(source_code_str);
});
eth.js.eth.currency_str = (function currency_str(wei){
return [cljs.core.str(parseInt(wei).toExponential()),cljs.core.str(" wei")].join('');
});
eth.js.eth.return_value = (function return_value(result){
console.debug("Transaction result:",result);

return cljs.core.nth.call(null,result.c,(0));
});

//# sourceMappingURL=eth.js.map