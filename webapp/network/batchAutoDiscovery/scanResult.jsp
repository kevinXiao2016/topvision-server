<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<script src="/performance/js/jquery-1.8.3.js"></script>
<Zeta:Loader>
    library ext
    library zeta
    module network
    import js/jquery/nm3kToolTip
</Zeta:Loader>
<script type="text/javascript"
	src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources,com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<style type="text/css">
#rear {position: absolute;top: 382px;left: 75px;font-size: 25;}
#scaning {position: absolute;top: 382px;left: 15px;font-size: 25;}
.backRed {color: red;}
.backGreen {color: green;}
</style>
<script type="text/javascript">
var count = 0;
var isStop = false;
var scanIds = null;

function getQueryStringRegExp(name){
    var reg = new RegExp("(^|\\?|&)"+ name +"=([^&]*)(\\s|&|$)", "i"); 
    if (reg.test(location.href)){
    	return unescape(RegExp.$2.replace(/\+/g, " ")); 
    }
    return "";
};

function randomInteger(min, max) {
    var r = Math.random() * (max - min);
    var re = Math.round(r + min);
    re = Math.max(Math.min(re, max), min)
    return re;
}

var operationId = 'scanNetSegment_' + randomInteger(0, 100);

$(function(){
	
	R.finishBT.hide();
	loadTheGrid();
	walk();
	scanIds = getQueryStringRegExp('scanIds');
	//填写dwr回调
	window.top.addCallback("scanOption",function(entity){
		 var ipAddress = entity.ip;
		 var typeName = entity.typeName;
		 var sysName = entity.sysName;
		 var name = entity.name;
		 var topoInfo = entity.topoInfo;
	     var tmp = {ipAddress:ipAddress,typeName:typeName,sysName:sysName,name:name,topoInfo:topoInfo};
	     var re = new Ext.data.Record();
	     re.data = tmp;
	     store.add(re);
	}, operationId);
	window.top.addCallback("scanFinish",function(){
		isStop = true;
		$("#rear").hide();
		$("#scaning").hide();
		R.stopBT.hide();
		// R.hideBT.hide();
		R.finishBT.show();
		//window.top.removeCallback("scanOption", 999);
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.batchTopo.topoFinish);
		try{
			var win = window.parent.getFrame("batchTopo")
			if(win != null){
				win.batchTopo.reloadStore();
			}
			win = window.parent.getFrame("oltList")
			if(win != null){
				win.store.reload();
			}
		}catch(e){}
	}, operationId);
	//发起批量请求
	var batchScanPromise = $.post('/batchautodiscovery/scanNetSegment.tv', {
		ids: scanIds,
		jconnectID : WIN.top.GLOBAL_SOCKET_CONNECT_ID,
		operationId: operationId
	});
	batchScanPromise.done(function(){
		
	})
})

function walk(){
	setTimeout(function(){
		if(isStop)return;
		count++;
		if(count % 4 == 0) 
			$("#rear").text("");
		else if(count % 4 == 1) 
			$("#rear").text(".");
		else if(count % 4 == 2) 
			$("#rear").text("..");
		else if(count % 4 == 3) 
			$("#rear").text("...");
		walk();
	},300);
}

function removeMask(){
	$("#loadingMask").remove();
}
var proData = new Array();
var proGrid;
var store;
function stateRenderer(v,m,r){
	if(v == "EntityExists"){
		return "<span style='color:#f00;' title='@batchTopo.entityExists@'>"+I18N.batchTopo.entityExists +"</span>";
	} else if(v == "ReplaceEntity"){
		return "<span style='color:green;'>" + I18N.batchTopo.entityReplace + "</span>";
	} else if(v == "LicenseLimit"){
		return "<span style='color:#f00;' title='@batchTopo.licenseLimit@'>" + I18N.batchTopo.licenseLimit + "</span>";
	} else if(v == "blackEntity"){
		return "<span style='color:#f00;' title='@batchTopo.entityForbidDiscovery@'>" +  I18N.batchTopo.entityForbidDiscovery + "</span>";
	} else{
		return "<span style='color:green;' title='@batchTopo.entityDiscoverySuccess@'>" +  I18N.batchTopo.entityDiscoverySuccess + "</span>";
	}
}

function loadTheGrid(){
	var cm = [ {header: I18N.batchTopo.ipAddress, dataIndex: 'ipAddress', sortable: true}
		 , {header: I18N.batchTopo.entityType, dataIndex: 'typeName', sortable: true}
		 , {header: I18N.batchTopo.entityName, dataIndex: 'sysName', sortable: true}
		 , {header: I18N.batchTopo.entityDisplayName, dataIndex: 'name', sortable: true}
		 , {header: I18N.batchTopo.entityTopoResult, dataIndex: 'topoInfo', sortable: true,renderer: stateRenderer}];
	store = new Ext.data.SimpleStore({
       root: 'data',
       fields: ['ipAddress', 'typeName', 'snmpParamIndex','sysName', 'name', 'topoInfo']
	});      
	proGrid = new Ext.grid.GridPanel({
	    stripeRows:true,
   		region: "center",
   		bodyCssClass: 'normalTable',
		viewConfig:{forceFit: true},
		id : 'grid',
		renderTo : 'gridDiv',
		border : true,
		frame : false,
		autoScroll : true,
		width : 620,
		height : 350,
		store : store,
		loadMask:{msg : I18N.batchTopo.entityIsScaning},
		columns : cm
	});
	//store.load();
}


function cancelClick() {
	//window.win.destroy() 
	//取消dwr推送接收
    window.top.removeCallback("scanOption", operationId);
    window.top.removeCallback("scanFinish", operationId);
	window.top.closeWindow("scanResult");
}
function finishTopo(){
	 R.stopBT.setText("@batchTopo.stopping@").setDisabled(true);
	 $.ajax({
        url: '/entity/stopTopo.tv',
        type: 'GET',cache:false
     });
}

function hideWin(){
	window.parent.getWindow("scanResult").hide();
}
</script>
</head>
<body class=openWinBody>
	<div id=gridDiv style='padding-left: 15px; padding-top: 15px;'></div>
	<span id="scaning">@batchTopo.scanning@</span>
	<span id="rear"></span>
	<Zeta:ButtonGroup>
		<Zeta:Button id="stopBT" onClick="finishTopo()" icon="miniIcoStop">@batchTopo.stopTopo@</Zeta:Button>
		<Zeta:Button id="finishBT" onClick="cancelClick()" icon="miniIcoSaveOK">@batchTopo.topoComplete@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>
