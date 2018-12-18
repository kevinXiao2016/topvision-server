<%@ page language="java" contentType="text/html;charset=utf-8"%>
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
	    module logicinterface
	    CSS css/white/disabledStyle
	    IMPORT js/nm3k/Nm3kSwitch
	    IMPORT /epon/logicinterface/logicInterface
	</Zeta:Loader>
	<style type="text/css">
	
	</style>
	<script type="text/javascript">
		var entityId = '${entityId}';
		var interfaceType = '${interfaceType}';
		var interfaceId = '${interfaceId}';
		var adminStatus = '${logicInterface.interfaceAdminStatus}';
		var interfaceDesc = '${logicInterface.interfaceDesc}';
		var imgBtn;
		
		$(function(){
			imgBtn = new Nm3kSwitch('putImgBtn', adminStatus);
			imgBtn.init();
			$("#interfaceType").val(interfaceType);
			$("#interfaceId").val(interfaceId);
			$("#interfaceDesc").val(interfaceDesc);
			if(interfaceType == 2){
				$("#interfaceIdAdminTbody").css({display:'none'});
			}
		}); //end document.ready;

		function saveClick(){
			var interfaceDesc = $("#interfaceDesc").val()
			var interfaceAdminStatus = imgBtn.getDisplayValue() == 'on'?1:2;
			if(!checkInterfaceDesc()){
				$("#interfaceDesc").focus();
				return;
			}
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
			$.ajax({
				url : '/epon/logicinterface/modifyLogicInterface.tv',
				type : 'POST',
				data : {
					entityId : entityId,
					interfaceType : interfaceType,
					interfaceId : interfaceId,
					interfaceDesc : interfaceDesc,
					interfaceAdminStatus : interfaceAdminStatus
				},
				success : function() {
					top.afterSaveOrDelete({
		       	      	title: "@COMMON.tip@",
		       	      	html: '<b class="orangeTxt">@interface.modifyInterfaceSuc@</b>'
		       	    });
					top.getActiveFrame().reloadData();
					cancelClick();
				},
				error : function(json) {
					window.parent.showMessageDlg("@COMMON.tip@", "@interface.modifyInterfaceFail@");
				},
				cache : false
			});
		}
		function cancelClick(){
			top.closeWindow('editLogicInterface');
		}
		
	</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip"></div>
	    <div class="rightCirIco equipmentCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT20">
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width="180">
	                   	 @interface.logicType@:
	                </td>
	                <td>
	                   	<select id='interfaceType' class="w200 normalSel" disabled="disabled">
	                   		<option value='1'>@interface.vlanIf@</option>
	                   		<option value='2'>@interface.loopBack@</option>
	                   		<option value='3'>@interface.outBandType@</option>
	                   	</select>
	                </td>
	            </tr>
	        </tbody>
	        <tbody id="interfaceIdTbody">
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                    @interface.interfaceId@:<span class="redTxt">*</span>
	                </td>
	                <td>
	                   	 <input id="interfaceId"  type="text"  disabled="disabled" class="normalInput w200" />
	                </td>
	            </tr>
	        </tbody>
	        <tbody id="interfaceIdDescTbody">
	            <tr>
	                <td class="rightBlueTxt">
	                	@interface.desc@<span class="redTxt">*</span>:
	                </td>
	                <td>
	                   	 <input id="interfaceDesc" maxlength = '31' tooltip="@interface.descTip@" type="text" class="normalInput w200" />
	                </td>
	            </tr>
	        </tbody>
	        <tbody id="interfaceIdAdminTbody">
	            <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                	@interface.adminStatus@:
	                </td>
	                <td id="putImgBtn">
	                </td>
	            </tr>
	        </tbody>
	    </table>
	    <div class="noWidthCenterOuter clearBoth">
	        <ol class="upChannelListOl pB0 pT20 noWidthCenter">
	            <li><a href="javascript:;" class="normalBtnBig" onclick="saveClick()"><span><i class="miniIcoAdd"></i>@COMMON.save@</span></a></li>
	            <li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
	        </ol>
	    </div>
	</div>
</body>
</Zeta:HTML>