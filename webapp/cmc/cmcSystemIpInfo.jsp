<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../include/cssStyle.inc"%>
<script type="text/javascript" src="/js/tools/ipAddrCheck.js"></script>
<Zeta:Loader>
    library ext
    library jquery
    module cmc
    import js.tools.ipText
</Zeta:Loader>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/disabledStyle.css" />
<script type="text/javascript">
var entityId = '${entityId}';
var vlanList = ${vlanList};
var grid = null;
var store = null;
var gatewayIpAddr;
function cancelClick() {
	window.top.closeWindow('systemIpInfo');
}

function modifyBtClick(ip, gate, mask){
	var win = window.parent.createDialog("modifyCmcSystemIpInfo", "@CMC.tip.modifySystemIp@", 600, 370, 
			'/cmc/config/showModifyCmcSystemIpInfo.tv?entityId='+ entityId +'&topCcmtsEthIpAddr='+ip+'&topCcmtsEthIpMask='+mask +'&topCcmtsEthVlanGateway='+gate, null, true, true);
			win.on('close', function(){
			store.reload();
		});
}

function addSubIp(){
	var win = window.parent.createDialog("addCmcSubIp", "@CMC.tip.addSubIp@", 600, 370, 
			'/cmc/config/showAddCmcSubIpInfo.tv?entityId='+ entityId, null, true, true);
			win.on('close', function(){
			store.reload();
		});
}


function deleteBtClick(ip, seqIndex){
	window.top.showOkCancelConfirmDlg("@COMMON.tip@", "@CMC.tip.confirmCancelSubIp@" + ip + ' ?', function(type) {
	if (type == 'ok') {
	    //window.top.showWaitingDlg("@CMC.tip.waiting@", "@CMC.tip.deletingSubIp@", 'ext-mb-waiting');
	    $.ajax({
    		url:'/cmc/config/deleteSubIpVlan0.tv?entityId='+ entityId + "&subIpAddress=" + ip
    		+ "&subSeqIndex=" + seqIndex,
	  		type:'POST',
	  		dateType:'text',
	  		success:function(response){
	  			if(response == "success"){
	  				//window.parent.showMessageDlg("@CMC.tip.tipMsg@", "@CMC.tip.deleteSubIpSuccess@");
	  				//window.top.closeWaitingDlg();
	  				top.afterSaveOrDelete({
	  	   				title: '@CMC.tip.tipMsg@',
	  	   				html: '<b class="orangeTxt">@CMC.tip.deleteSubIpSuccess@</b>'
	  	   			});
	  			}else{
	  				window.parent.showMessageDlg("@CMC.tip.tipMsg@", "@CMC.tip.deleteSubIpFail@");
	  			}
				//cancelClick();
	  			store.reload();
	  		},
	  		error:function(){
	  			window.parent.showMessageDlg("@CMC.tip.tipMsg@", "@CMC.tip.deleteSubIpFail@");
	  		},
	  		cache:false
      	 	});
		}
	});
}

function manuRender(v, c, record){
	if(record.data.ipType == "Primary IP"){
		var imgStr1 = String.format("onclick='modifyBtClick(\"{0}\",\"{1}\",\"{2}\")'",record.data.ipAddress,
				record.data.ipGateAddress, record.data.ipMaskAddress);
		if(operationDevicePower){
			return String.format("<a  href='javascript:;' {0}>@CMC.label.edit@</a>" , imgStr1);
		}else{
		    return "@CMC.label.edit@";
		}
	}else if(record.data.ipType == "Second IP"){
		var imgStr2 = String.format("onclick='deleteBtClick(\"{0}\",\"{1}\",\"{2}\")'" ,record.data.ipAddress,
				record.data.seqIndex);
		if(operationDevicePower){
			return String.format("<a href='javascript:;' {0}>@CMC.button.delete@</a>" , imgStr2);
		}else{
		    return "@CMC.button.delete@";
		}
	}
}
function isReachable(gateway){
    var check;
    for(var v in vlanList){
        var ipTmp = vlanList[v].ipAddr;
        var maskTmp = vlanList[v].ipMask;
        if(ipTmp==undefined||ipTmp==""||ipTmp=="-"||maskTmp==undefined||maskTmp==""||maskTmp=="-"){
            continue;
        }
        check = new IpAddrCheck(ipTmp, maskTmp);
        if(check.isGateway(gateway)){
            return true;
        }
    }
}

function modifyGatewayIp(){
    var gateway = $("#gatewayIp").val().trim();
    if(!checkedIpValue(gateway)){
        window.parent.showMessageDlg("@CMC.tip.tipMsg@", "@route.ipFormatError@");
        return;
    }
    if(!checkIsNomalIp(gateway)){
        window.parent.showMessageDlg("@CMC.tip.tipMsg@", "@route.ipAddrTypeTip@");
        return;
    }
    if(!isReachable(gateway)){
        window.parent.showMessageDlg("@CMC.tip.tipMsg@", "@CMC.tip.gatewayUnreachable@");
        return;
    }
    //window.top.showWaitingDlg("@CMC.tip.waiting@", "@text.configuring@", 'ext-mb-waiting');
    $.ajax({
        url:'/cmc/config/modifyCmcGatewayInfo.tv?entityId='+ entityId + "&topCcmtsEthVlanGateway=" + gateway,
        type:'POST',
        dateType:'text',
        success:function(response){
            if(response == "success"){
                gatewayIpAddr = gateway;
                onInputChange();
                //window.parent.showMessageDlg("@CMC.tip.tipMsg@", "@CMC.tip.modifySuccess@");
                //window.top.closeWaitingDlg();
                top.afterSaveOrDelete({
       				title: '@CMC.tip.tipMsg@',
       				html: '<b class="orangeTxt">@CMC.tip.modifySuccess@</b>'
       			});
            }else{
                window.parent.showMessageDlg("@CMC.tip.tipMsg@", "@CMC.tip.modifyFail@");
            }
        },
        error:function(){
            window.parent.showMessageDlg("@CMC.tip.tipMsg@", "@CMC.tip.modifyFail@");
        },
        cache:false
    });
}
function onInputChange(){
    var tempGateway = $("#gatewayIp").val().trim();
    if(gatewayIpAddr != tempGateway){
        $("#modifyButton").attr("disabled", false); 
    }else{
        $("#modifyButton").attr("disabled", true);
    }
}

Ext.onReady(function () {    	
	Ext.QuickTips.init();
	var w = Ext.getBody().getWidth();
	var h = Ext.getBody().getHeight();
	var cm = new Ext.grid.ColumnModel([
	     {header: "@CMC.label.IPType@", dataIndex:'ipType',width:100,align:"center"},
	     {header: "@CMC.label.IP@", dataIndex:'ipAddress',width:240,align:"center"},
	     //{header: "@CMC.label.gatewayIp@", dataIndex:'ipGateAddress',width:120,align:"center"},
	     {header: "@CMC.label.iPMask@", dataIndex:'ipMaskAddress',width:190,align:"center"},
	     {header: "@CMC.text.operation@", width:60, sortable:false, groupable: false, menuDisabled:true,dataIndex:'ipAddress',renderer:manuRender}
	    ]);
    store = new Ext.data.GroupingStore
          ({
          proxy : new Ext.data.HttpProxy({
          url: '/cmc/config/loadSystemIpInfo.tv?entityId='+entityId,
          method: 'POST'
              }),
          reader: new Ext.data.JsonReader
          ({  
            root: 'data',
            totalProperty: 'totalProperty',   
            fields:['ipType', 'ipAddress', 'ipGateAddress','ipMaskAddress', 'seqIndex']
          }),
          remoteSort: true, //是否从后台排序
          groupField: 'ipType',
          sortInfo:{field:'ipAddress',direction:'asc'}   
          });
     store.on("load",function (store, records, options){
    	 for(var i = 0; i < records.length; i++){
    		 var record = records[i];
    		 var ipType = record.data.ipType.toUpperCase();
    		 if(ipType.indexOf("PRIMARY") != -1){
    			 if(!record.data.ipAddress || record.data.ipAddress.trim() =="" || 
    					 record.data.ipAddress.trim() == "0.0.0.0"){
    				 $("#addSubIp").attr("disabled", true);    				 
    			 }else{
    			     if(operationDevicePower){
	    				 $("#addSubIp").attr("disabled", false); 
    			     }else{
    			         $("#addSubIp").attr("disabled", true); 
    			     }
    			 }
    			 gatewayIpAddr = record.data.ipGateAddress;
    			 $("#gatewayIp").val(gatewayIpAddr);
    			 break;
    		 }
    	 }
     });
 
     grid = new Ext.grid.GridPanel({
                height:350,               
                renderTo: 'grid',
                view: new Ext.grid.GroupingView({
                    forceFit: true, hideGroupedColumn: true,enableNoGroups: false
                }),
                store: store,    
                cm: cm
            });
     store.load();
     
     //设备操作权限
 	if(!operationDevicePower){
	    $("#gatewayIp").attr("disabled",true);
	    $("#modifyButton").attr("disabled",true);
	}
});
</script>
</head>
<body class="openWinBody">
	<div class="edge10">
		<div id="grid" class="normalTable"></div>
		<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
             <tbody>
                 <tr>
                     <td class="rightBlueTxt" width="140"> 
                        @CMC.label.gatewayIp@
                     </td>
                     <td>
                        <input id="gatewayIp" type="text" onkeyup="onInputChange()" class="normalInput w400 floatLeft" /> 
                        <a onclick="modifyGatewayIp()" id="modifyButton" disabled="true" href="javascript:;" class="nearInputBtn"><span><i class="miniIcoEdit"></i>@CMC.text.modify@</span></a>
                     </td>                 
                 </tr>
             </tbody>
        </table>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
		         <li><a id="addSubIp" onclick="addSubIp()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoAdd"></i>@CMC.button.newSubIp@</span></a></li>
		         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>