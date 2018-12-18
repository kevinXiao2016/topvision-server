<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module epon
    CSS css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
.w165{width:165px}
</style>
<script type="text/javascript">
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var entityId = '<s:property value="entityId"/>';
var sniId = '<s:property value="portId"/>';
var oltStpEnable = '<s:property value="oltStpGlobalConfig.enable"/>';
var sniIndex = '<s:property value="sniIndex"/>';
var sniAutoNegotiationMode = '<s:property value="sniAutoNegotiationMode"/>';
var portStatus = ['unknow','disabled','blocking','listening','learning','forwarding','broken'];
var oltStpPortConfig = ${oltStpPortConfigObject};
function doOnload(){
	//if(oltStpPortConfig[0]!=null){
		if(oltStpEnable == 1){
			if(oltStpPortConfig[0]!=null){
				$("#stpPortEnabled").val(oltStpPortConfig[0].stpPortEnabled);
				$("#stpPortPriority").val(oltStpPortConfig[0].stpPortPriority);
				$("#stpPortStatus").val(portStatus[oltStpPortConfig[0].stpPortStatus]);
				$("#stpPortPathCost").val(oltStpPortConfig[0].stpPortPathCost);
				$("#stpPortDesignatedRoot").val(oltStpPortConfig[0].stpPortDesignatedRootString);
				$("#stpPortDesignatedCost").val(oltStpPortConfig[0].stpPortDesignatedCost);
				$("#stpPortDesignatedBridge").val(oltStpPortConfig[0].stpPortDesignatedBridgeString);
				$("#stpPortDesignatedPort").val(oltStpPortConfig[0].stpPortDesignatedPort);
				$("#stpPortForwardTransitions").val(oltStpPortConfig[0].stpPortForwardTransitions);
				$("#stpPortRstpAdminEdgePort").val(oltStpPortConfig[0].stpPortRstpAdminEdgePort);
				$("#stpPortRstpOperEdgePort").val(oltStpPortConfig[0].stpPortRstpOperEdgePort);
				$("#stpPortPointToPointAdminStatus").val(oltStpPortConfig[0].stpPortPointToPointAdminStatus);
				$("#stpPortPointToPointOperStatus").val(oltStpPortConfig[0].stpPortPointToPointOperStatus);
				if(oltStpPortConfig[0].stpPortEnabled == 1){
					Zeta$('stpPortEnabled').checked = true;
			   		Zeta$('stpPortPriority').disabled = false;
			   		Zeta$('stpPortPathCost').disabled = false;
			   		Zeta$('stpPortRstpAdminEdgePort').disabled = false;
			   		Zeta$('stpPortPointToPointAdminStatus').disabled = false;
				}else if(oltStpPortConfig[0].stpPortEnabled == 2 || oltStpPortConfig[0].stpPortEnabled == 0){
					Zeta$('stpPortEnabled').checked = false;
			   		Zeta$('stpPortPriority').disabled = true;
			   		Zeta$('stpPortPathCost').disabled = true;
			   		Zeta$('stpPortRstpAdminEdgePort').disabled = true;
			   		Zeta$('stpPortPointToPointAdminStatus').disabled = true;
				}
			}
		}else{
			R.refreshBt.setDisabled(true);
			R.saveBt.setDisabled(true);
			/* $("#refreshBt").attr("disabled", true);
			$("#saveBt").attr("disabled", true); */
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.RSTP.rstpUStart )
		}
		if(oltStpPortConfig[0]!=null){
			if(oltStpPortConfig[0].stpPortRstpOperEdgePort == 1){
				Zeta$('stpOperEdgePort').checked = true;
			}
			if(oltStpPortConfig[0].stpPortPointToPointOperStatus == 1){
				Zeta$('stpPointToPoint').checked = true;
			}
		}
    //}
}
function saveClick(){
	 //var stpPortEnabled = $("#stpPortEnabled").val();
	 var stpPortEnabled = Zeta$('stpPortEnabled').checked;
	 if(stpPortEnabled == true){
		 stpPortEnabled = 1;
	 } else {
		 stpPortEnabled = 2;
	 }
	 var stpPortPriority = $("#stpPortPriority").val();
	 var stpPortPathCost = $("#stpPortPathCost").val();
	 var stpPortRstpAdminEdgePort = $("#stpPortRstpAdminEdgePort").val();
	 var stpPortPointToPointAdminStatus = $("#stpPortPointToPointAdminStatus").val();
	 if(stpPortEnabled == 1 && !checkStpPortPathCost()){
	    Zeta$("stpPortPathCost").focus();
	    return;
	 }
	 if(stpPortEnabled == 1){
		 if(stpPortPointToPointAdminStatus == 2){
			 if(sniAutoNegotiationMode == 3 || sniAutoNegotiationMode == 1 || sniAutoNegotiationMode == 5 || sniAutoNegotiationMode == 6 || sniAutoNegotiationMode == 7 ){
				 window.top.showWaitingDlg(I18N.COMMON.wait, I18N.RSTP.setting )
				 $.ajax({ 
				        url: '/epon/rstp/updateStpPortConfig.tv',
				        type: 'POST',
				        data: "entityId=" + entityId+
				              "&sniId=" + sniId+
				              "&sniIndex=" + sniIndex + 
				              "&stpPortEnabled=" + stpPortEnabled + 
				              "&stpPortPriority=" + stpPortPriority + 
				              "&stpPortPointToPointAdminStatus=" + stpPortPointToPointAdminStatus +
				              "&stpPortPathCost=" + stpPortPathCost + 
				              "&stpPortRstpAdminEdgePort=" + stpPortRstpAdminEdgePort +  
				              "&num=" + Math.random(),
				        dataType:"text",
				        success: function(text) {
					        if(text == 'success'){
					        	top.afterSaveOrDelete({
					                title: '@COMMON.tip@',
					                html: I18N.RSTP.mdfSniInfoOk
					            });
				            	// 配置成功后，修改面板图中RSTP使能状态
				  	            window.parent.getFrame("entity-" + entityId).updateStpPortConifg(sniIndex,stpPortEnabled);
				            	cancelClick();
					        }else{
					        	window.parent.showMessageDlg(I18N.COMMON.tip,  I18N.RSTP.mdfSniInfoEr ,'error');
						    }
				        }, error: function(text) {
				        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.RSTP.mdfSniInfoEr ,'error');
				    }, cache: false
				    });
				 }else{
					 window.parent.showMessageDlg(I18N.COMMON.tip, I18N.RSTP.ethernetTip );
				 }
		 }else{
			 window.top.showWaitingDlg(I18N.COMMON.wait, I18N.RSTP.setting )
			 $.ajax({ 
			        url: '/epon/rstp/updateStpPortConfig.tv',
			        data: "entityId=" + entityId + 
			              "&sniId=" + sniId +
			              "&sniIndex=" + sniIndex+ 
			              "&stpPortEnabled=" + stpPortEnabled + 
			              "&stpPortPriority=" + stpPortPriority + 
			              "&stpPortPointToPointAdminStatus=" + stpPortPointToPointAdminStatus +
			              "&stpPortPathCost=" + stpPortPathCost +  
			              "&stpPortRstpAdminEdgePort=" + stpPortRstpAdminEdgePort +  
			              "&num=" + Math.random(),
			        dataType:"text",
			        success: function(text) {
				        if(text == 'success'){
				        	top.afterSaveOrDelete({
				                title: '@COMMON.tip@',
				                html: I18N.RSTP.mdfSniInfoOk
				            });
			  	            // 配置成功后，修改面板图中RSTP使能状态
			  	            window.parent.getFrame("entity-" + entityId).updateStpPortConifg(sniIndex,stpPortEnabled);
			            	cancelClick();
				        }else{
				        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.RSTP.mdfSniInfoEr ,'error');
					    }
			        }, error: function(text) {
			        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.RSTP.mdfSniInfoEr ,'error');
			    }, cache: false
			    });
			 }
     }else{
		 window.top.showWaitingDlg(I18N.COMMON.wait, I18N.RSTP.setting )
		 $.ajax({ 
		        url: '/epon/rstp/setStpPortEnable.tv',
		        type: 'POST',
		        data: "entityId=" + entityId + "&sniId=" + sniId + "&stpPortEnabled=" + stpPortEnabled + "&r=" + Math.random(),
		        dataType:"text",
		        success: function(json) {
			        if(json.message){
			        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.RSTP.mdfSniInfoEr ,'error');
			        }else{
			        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.RSTP.mdfSniInfoOk );
		  	             // 配置成功后，修改面板图中RSTP使能状态
		  	            window.parent.getFrame("entity-" + entityId).updateStpPortConifg(sniIndex,stpPortEnabled);
		  	          	cancelClick();
				    }
		        }, error: function(json) {
		        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.RSTP.mdfSniInfoEr ,'error');
		    }, cache: false
		    });
     }

}
function checkStpPortPathCost(){
	var reg0 = /^([0-9])+$/;
	var stpPortPathCost = $("#stpPortPathCost").val();
	if(stpPortPathCost == "" || stpPortPathCost == null||stpPortPathCost>200000000||stpPortPathCost==0){
		return false;
	}else{
		if(reg0.exec(stpPortPathCost)){
			return true;
		}else{
			return false;
		}
	}
}
function cancelClick(){
	window.top.closeWindow("sniRstpConfig");
}
function refreshClick(){
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.fetching )
	 $.ajax({
	        url: 'epon/rstp/refreshStpPortConfig.tv',
	        type: 'POST',
	        data: "sniId=" + sniId + "&entityId=" + entityId + "&num=" + Math.random(),
	        dataType:"text",
	        success: function(text) {
		        if(text == 'success'){
	            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.RSTP.fetchRstpOk );
	            	window.location.reload();
		        }else{
		        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.RSTP.fetchRstpEr );
			    }
	        }, error: function(text) {
	        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.RSTP.fetchRstpEr );
	    }, cache: false
	    });
}
function stpPortEnabledChanged(){
	if(!Zeta$('stpPortEnabled').checked){
   		Zeta$('stpPortPriority').disabled = true;
   		Zeta$('stpPortPathCost').disabled = true;
   		Zeta$('stpPortRstpAdminEdgePort').disabled = true;
   		Zeta$('stpPortPointToPointAdminStatus').disabled = true;
    }else{
   		Zeta$('stpPortPriority').disabled = false;
   		Zeta$('stpPortPathCost').disabled = false;
   		Zeta$('stpPortRstpAdminEdgePort').disabled = false;
   		Zeta$('stpPortPointToPointAdminStatus').disabled = false;
    }
}
function authLoad(){
	if(!operationDevicePower){
		$(":input").attr("disabled",true);
		R.saveBt.setDisabled(true);
	}
	if(!refreshDevicePower){
        R.refreshBt.setDisabled(true);
    }
}
</script>
</head>
	<body class=openWinBody onload="doOnload();authLoad();">
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@EPON.sniRstpSet@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>

		<div class="edge10">
			<form onsubmit="return false;">
				<table class="mCenter zebraTableRows">
					<tr>
						<td class="rightBlueTxt w130"><label for="community">@RSTP.portStpStatus@:</label></td>
						<td align=left colspan=3><input type="checkbox" class="normalInput" id="stpPortEnabled" onClick="stpPortEnabledChanged()" />@RSTP.on@
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt"><label for="ip">@RSTP.portPriority@:</label></td>
						<td><select id="stpPortPriority" class="w100 normalSel">
								<option value="0">0</option>
								<option value="16">16</option>
								<option value="32">32</option>
								<option value="48">48</option>
								<option value="64">64</option>
								<option value="80">80</option>
								<option value="96">96</option>
								<option value="112">112</option>
								<option value="128" selected>128</option>
								<option value="144">144</option>
								<option value="160">160</option>
								<option value="176">176</option>
								<option value="192">192</option>
								<option value="208">208</option>
								<option value="224">224</option>
								<option value="240">240</option>
						</select></td>
						<td class="rightBlueTxt"><label for="ip">@RSTP.portStatus@:</label></td>
						<td><input type="text" id="stpPortStatus" class="normalInput w165" disabled /></td>
					</tr>
					<tr>
						<td class="rightBlueTxt"><label for="community">@RSTP.portRootSpend@:</label></td>
						<td><input type="text" id="stpPortPathCost" class="normalInput w165" tooltip="@RSTP.range200000000@" /></td>
						<td class="rightBlueTxt"><label for="community">@RSTP.rootBdgId@:</label></td>
						<td><input type="text" id="stpPortDesignatedRoot" class="normalInput w165" disabled /></td>
					</tr>
					<tr>
						<td class="rightBlueTxt"><label for="community">@RSTP.assignPathSpend@:</label></td>
						<td><input type="text" id="stpPortDesignatedCost" class="normalInput w165" disabled /></td>
						<td class="rightBlueTxt"><label for="community">@RSTP.assignBridgeId@:</label></td>
						<td><input type="text" id="stpPortDesignatedBridge" class="normalInput w165" disabled /></td>
					</tr>
					<tr>
						<td class="rightBlueTxt"><label for="community">@RSTP.assignPort@:</label></td>
						<td><input type="text" id="stpPortDesignatedPort" class="normalInput w165" disabled /></td>
						<td class="rightBlueTxt"><label for="community">@RSTP.statusChangeTime@:</label></td>
						<td><input type="text" id="stpPortForwardTransitions" class="normalInput w165" disabled />@COMMON.S@</td>
					</tr>
					<tr>
						<td class="rightBlueTxt"><label for="community">@RSTP.edgePortMgmtStatus@:</label></td>
						<td><select id="stpPortRstpAdminEdgePort"
							class="normalSel w100">
								<option value="1">@RSTP.edgePort@</option>
								<option value="2">@RSTP.nedgePort@</option>
						</select></td>
						<td class="rightBlueTxt"><label for="community">@RSTP.edgePortStatus@:</label></td>
						<td><input type="checkbox" id="stpOperEdgePort" class="normalInput" disabled /><label
							disabled>@RSTP.edgePort@</label></td>
					</tr>
					<tr>
						<td class="rightBlueTxt"><label for="community">@RSTP.p2pMgmtStatus@:</label></td>
						<td><select id="stpPortPointToPointAdminStatus" class="normalSel w100">
								<option value="0">@RSTP.np2pLink@</option>
								<option value="1">@RSTP.p2pLink@</option>
								<option value="2">@RSTP.autoChk@</option>
						</select></td>
						<td class="rightBlueTxt"><label for="community"> @RSTP.p2pStatus@:</label></td>
						<td><input type="checkbox" id="stpPointToPoint" class="normalInput" disabled />
							<label disabled>@RSTP.p2pLink@</label></td>
					</tr>
				</table>
			</form>
		</div>

		<Zeta:ButtonGroup>
			<Zeta:Button onClick="refreshClick()" icon="miniIcoEquipment" id="refreshBt">@COMMON.fetch@</Zeta:Button>
			<Zeta:Button id="saveBt" onClick="saveClick()" id="saveBt" icon="miniIcoSave">@COMMON.apply@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" id="closeButton" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>