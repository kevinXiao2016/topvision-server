<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
<style type="text/css">

</style>
 <Zeta:Loader>
	library ext
	library jquery
	library zeta
	module cmc
	css css/white/disabledStyle
</Zeta:Loader>
<head>
<script>
	var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
	var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
	var cmcType = '${cmcDocsis.cmcType}';
	var entityId = '${entityId}';
	var cmcId = '${cmcId}';
	var cmcMDFEnabled = '${cmcDocsis.ccmtsMdfEnabled}';
	var ccmtsMddInterval = '${cmcDocsis.ccmtsMddInterval}';
	
	//Get Config from Facility
	function refreshDocsisConfig(){
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
		$.ajax({
			url : '/cmc/refreshCmcDocsisFromFacility.tv?r=' + Math.random(),
			data : {
				entityId : entityId,
				cmcId : cmcId,
				cmcType : cmcType
			},
			dataType : 'json',
			success : function(json) {
				if (json.success) {
					window.top.closeWaitingDlg();
					top.afterSaveOrDelete({
		   				title: '@COMMON.tip@',
		   				html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
		   			});
					window.location.reload();
				} else {
					window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
				}
			},
			error : function() {
				window.top.showErrorDlg();
			}
		});
	}
	//close the dialog
	function cancelClick() {
		window.parent.closeWindow('cmcDocsisView');
	}

	Ext.onReady(function() {
		authLoad();
	});
	
	function authLoad(){
	    if(!refreshDevicePower){
	        $("#refreshButton").attr("disabled",true);
	    }
	}
</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader" style="height:90px;">
	    <div class="openWinTip">
	    	@cmc.mdfConfigMessage@
	    </div>
	    <div class="rightCirIco wheelCirIco" style="top:18px;"></div>
	</div>
	<div class="edge10">
		<form id="cmcDocsisForm" name="cmcDocsisForm">
		<input type="hidden" name="entityId" value="${entityId}" />
		<input type="hidden" name="cmcId" value="${cmcId}" />
		<input type="hidden" name="cmcType" value="${cmcDocsis.cmcType}" />
		<input type="hidden" name="ifIndex" value="${cmcDocsis.ifIndex}" />
			<div class="edge10">
			    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			        <tbody>
			            <tr>
			                <td class="rightBlueTxt" width="200">
			                    <label for="cmcMDDTime">@cmc.mddTimeInterval@:</label>
			                </td>
			                <td>
			                	@COMMON.couldntGetData@
			                </td>
			            </tr>
			            <tr class="darkZebraTr">
			                <td class="rightBlueTxt">
			                    @cmc.mdfEnabledConfig@:
			                </td>
			                <td>
			                    @COMMON.couldntGetData@
			                </td>
			            </tr>
			        </tbody>
			    </table>
			    <div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
				     <ol class="upChannelListOl pB0 pT60 noWidthCenter">
				         <li><a id="refreshButton"  onclick="refreshDocsisConfig();" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
				         <li><a id="closeButton" onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@BUTTON.cancel@</span></a></li>
				     </ol>
				</div>
			</div>
		</form>
	</div>
</body>
</Zeta:HTML>