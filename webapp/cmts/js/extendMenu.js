//需要加载entityType.js
var extendEntityId;
var extendCmcId;
var extendNote;
var extendType;
var loopbackStatus;
var loopbackStatusIcomcls;
function extendOper(items,entityId, cmcId, note,type) {
    extendCmcId = cmcId;
    extendNote = note;
    extendType = type;
    extendEntityId = entityId;
    items[items.length] = '-';
        items[items.length] = [
           { id:'ctmCmcMenu',
               text:'CMTS',
               enableScrolling: false,
               menu: [
                   {text: I18N.CMC.title.performanceMgt,id:"ccmts8800APerfManage",handler:ccmtsPerfManage},
               ]
           }
       ];
    return items;
}

function loadSniUplinkLoopbackStatus(v){
	$.ajax({
        url: '/cmc/sni/loadSniUplinkLoopbackStatus.tv',

function cmcDetailInfo(){
    viewCmcSnap(extendCmcId, extendNote, extendType);
}

function viewCmcSnap(cmcId,cmcNote,type){
	window.parent.addView('entity-' + cmcId, "CCMTS" + '[' + cmcNote + ']', 'entityTabIcon',
				'/cmc/showCmcPortal.tv?cmcId=' + extendCmcId);
}

function ccmtsPerfManage(){
	window.parent.addView('cmcPerfParamConfig', I18N.CMC.title.performanceCollectConfig,"bmenu_preference",'/cmc/perfTarget/showCmcPerfManage.tv?entityId=' + extendEntityId);
	/*window.top.createDialog('perfConfig', I18N.CMC.title.performanceCollectConfig, 800, 500,
		'/cmcperf/showCcmtsPerfConf.tv?cmcId=' + extendCmcId, null, true, true);*/
}

function refreshCC(){
	window.parent.showConfirmDlg(I18N.CMC.tip.tipMsg,I18N.CMC.tip.sureToRefreshCcmts,function(button,text){
		if(button == "yes"){
			window.parent.showWaitingDlg(I18N.CMC.tip.waiting, String.format(I18N.CMC.tip.refreshingCcmts),'waitingMsg','ext-mb-waiting');
			$.ajax({
    			url:'cmc/refreshCC.tv?cmcId='+ extendCmcId+'&cmcType='+extendType+'&entityId='+extendEntityId,
	  			type:'POST',
	  			dateType:'json',
	  			success:function(response){
        			if(response == "success"){
        				window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.refreshCcmtsSuccess);
					}else{
						window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.refreshCcmtsFail);
					}
					//TODO 页面该CC需同步刷新
        			doRefresh();
	  			},
	  			error:function(){
					 window.parent.showMessageDlg(I18N.CMC.tip.tipMsg, I18N.CMC.tip.refreshCcmtsFail);
	  			},
	  			cache:false
    		});
		}
	});

}

