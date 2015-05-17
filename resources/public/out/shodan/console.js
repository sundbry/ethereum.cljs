// Compiled by ClojureScript 0.0-2740 {}
goog.provide('shodan.console');
goog.require('cljs.core');
var methods_11607 = new cljs.core.PersistentVector(null, 22, 5, cljs.core.PersistentVector.EMPTY_NODE, ["assert","clear","count","debug","dir","dirxml","error","exception","group","groupCollapsed","groupEnd","info","log","markTimeline","profile","profileEnd","table","time","timeEnd","timeStamp","trace","warn"], null);
var console_11608 = ((typeof console !== 'undefined')?console:(function (){var polyfill = {"memory": null};
(window["console"] = polyfill);

return polyfill;
})());
var noop_11609 = cljs.core.constantly.call(null,null);
var seq__11603_11610 = cljs.core.seq.call(null,methods_11607);
var chunk__11604_11611 = null;
var count__11605_11612 = (0);
var i__11606_11613 = (0);
while(true){
if((i__11606_11613 < count__11605_11612)){
var m_11614 = cljs.core._nth.call(null,chunk__11604_11611,i__11606_11613);
var or__3759__auto___11615 = (console_11608[m_11614]);
if(cljs.core.truth_(or__3759__auto___11615)){
} else {
(console_11608[m_11614] = noop_11609);
}

var G__11616 = seq__11603_11610;
var G__11617 = chunk__11604_11611;
var G__11618 = count__11605_11612;
var G__11619 = (i__11606_11613 + (1));
seq__11603_11610 = G__11616;
chunk__11604_11611 = G__11617;
count__11605_11612 = G__11618;
i__11606_11613 = G__11619;
continue;
} else {
var temp__4126__auto___11620 = cljs.core.seq.call(null,seq__11603_11610);
if(temp__4126__auto___11620){
var seq__11603_11621__$1 = temp__4126__auto___11620;
if(cljs.core.chunked_seq_QMARK_.call(null,seq__11603_11621__$1)){
var c__4546__auto___11622 = cljs.core.chunk_first.call(null,seq__11603_11621__$1);
var G__11623 = cljs.core.chunk_rest.call(null,seq__11603_11621__$1);
var G__11624 = c__4546__auto___11622;
var G__11625 = cljs.core.count.call(null,c__4546__auto___11622);
var G__11626 = (0);
seq__11603_11610 = G__11623;
chunk__11604_11611 = G__11624;
count__11605_11612 = G__11625;
i__11606_11613 = G__11626;
continue;
} else {
var m_11627 = cljs.core.first.call(null,seq__11603_11621__$1);
var or__3759__auto___11628 = (console_11608[m_11627]);
if(cljs.core.truth_(or__3759__auto___11628)){
} else {
(console_11608[m_11627] = noop_11609);
}

var G__11629 = cljs.core.next.call(null,seq__11603_11621__$1);
var G__11630 = null;
var G__11631 = (0);
var G__11632 = (0);
seq__11603_11610 = G__11629;
chunk__11604_11611 = G__11630;
count__11605_11612 = G__11631;
i__11606_11613 = G__11632;
continue;
}
} else {
}
}
break;
}

//# sourceMappingURL=console.js.map