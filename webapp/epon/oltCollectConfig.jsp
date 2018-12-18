<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
 <Zeta:Loader>
	library ext
	library jquery
	library zeta
	module epon
</Zeta:Loader>
<head>
<script type="text/javascript">
var entityId = <s:property value='entityId'/>; 
function saveClick(){
	var mGlobal = $("#mGlobal").val();
	var hGlobal = $('#hGlobal').val();
	if(!checkInput(mGlobal) || mGlobal<1 || mGlobal>96){
		Zeta$("mGlobal").focus();
		return ;
	}
	if(!checkInput(hGlobal) || hGlobal<1 || hGlobal>30){
		Zeta$("hGlobal").focus();
		return ;
	}
	window.top.showOkCancelConfirmDlg(I18N.COMMON.tip, I18N.PERF.modifyConfirm, function (type) {
	       if(type=="ok"){
	    	   window.top.showWaitingDlg(I18N.COMMON.wait, I18N.PERF.modifying, 'ext-mb-waiting');
	    		Ext.Ajax.request({
	    			url : '/epon/perf/savePerfStatsGlobalSet.tv?r=' + Math.random(),
	    			success : function(text) {
	    				if (text.responseText != null && text.responseText != "success") {  
	    					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.port.modifyFailed , 'error');
	    				} else {
	    					 top.closeWaitingDlg();
    						 top.nm3kRightClickTips({
	    	    				title: I18N.COMMON.tip,
	    	    				html: I18N.PERF.port.modifySuccess
	    	    			 });
	    	                 //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.port.modifySuccess);
	    	                 cancelClick();
	    			}
	    			},
	    			failure : function() {
	    				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.PERF.port.modifyFailed);
	    			},
	    			params : {
	    				entityId: entityId,
	    				perfStats15MinMaxRecord: mGlobal,
	    				perfStats24HourMaxRecord: hGlobal
	    			}
	    		});
	           }
			})
}
function cancelClick() {
	window.parent.closeWindow('collectConfig');	
}
function checkInput(input){
	var reg1 = /^([0-9])+$/;
	if(input == "" ||input == null){
		return false;
	}else{
		if(reg1.exec(input)){
			return true;
		}else{
			return false;
		}
	}
}
function refreshClick(){
  	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.COMMON.fetching, 'ext-mb-waiting');
  	Ext.Ajax.request({
  		url : '/epon/perf/refreshPerfStatsGlobalSet.tv?r=' + Math.random(),
  		success : function(text) {
  			if (text.responseText != null && text.responseText != "success") {  
  				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAuth.tip.fetchFailed , 'error');
  			} else {
  				 top.closeWaitingDlg();
				 top.nm3kRightClickTips({
	   				title: I18N.COMMON.tip,
	   				html: I18N.COMMON.fetchOk
	   			 });
  	             //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.COMMON.fetchOk);
  	             window.location.reload();
  			} 
  		},
  		failure : function() {
  			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.onuAuth.tip.fetchFailed);
  		},
  		params : {
  			entityId: entityId
  		}
  	});
}
//设备操作权限------------------------------------------------
function authLoad(){
	var operationPower = <%=uc.hasPower("operationDevice")%>;
	$("#mGlobal").addClass("operationDeviceClass");
	$("#hGlobal").addClass("operationDeviceClass");
	//$("#fetch").addClass("operationDeviceClass");
	$("#okBt").addClass("operationDeviceClass");
	if(!operationPower){
		$(".operationDeviceClass").attr("disabled",true);		
	}
}
//-----------------------------------------------------------
</script>
</head>
<body class="openWinBody" onload="authLoad();">
	<div class="openWinHeader">
	    <div class="openWinTip"></div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT40">
	     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	         <tbody>
	             <tr>
	                 <td class="rightBlueTxt" width="260">
						@PERF.port.x15minRecord@:
	                 </td>
	                 <td>
						<input type="text" id="mGlobal" class="normalInput" value="<s:property value='perfStatsGlobalSet.perfStats15MinMaxRecord'/>"
						toolTip='@PERF.port.plsInput1to96@'
						size=20  maxlength=2  />
	                 </td>
	             </tr>
	             <tr class="darkZebraTr">
	                 <td class="rightBlueTxt">
						@PERF.port.x24hourRecord@:
	                 </td>
	                 <td>
						<input type="text" id="hGlobal" value="<s:property value='perfStatsGlobalSet.perfStats24HourMaxRecord'/>" size=20 maxlength=2 class="normalInput"
			        	toolTip='@PERF.port.plsInput1to30@'
			        	 />
	                 </td>
	             </tr>
	         </tbody>
	     </table>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT80 noWidthCenter">
		         <li><a id=fetch onclick="refreshClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
		         <li><a id="okBt" onclick="saveClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSave"></i>@BUTTON.apply@</span></a></li>
		         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@BUTTON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>