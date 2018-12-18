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
		var status = '1';
		var imgBtn;
		var interfaceIdTip = '';
		
		$(function(){
			$("#interfaceType").val(interfaceType);
			imgBtn = new Nm3kSwitch('putImgBtn', status);
			imgBtn.init();
			
			if(interfaceType == 1){
				interfaceIdTip = '@interface.addInterfaceInput1@';
			}else if(interfaceType == 2){
				interfaceIdTip = '@interface.addInterfaceInput2@';
				$("#adminStatusTbody").css({display:'none'});
			}else if(interfaceType == 3){
				$("#interfaceIdTbody").css({display:'none'});
			}
			$("#interfaceId").attr({toolTip : interfaceIdTip});
		}); //end document.ready;

		function saveClick(){
			var interfaceId = $("#interfaceId").val();
			var interfaceDesc = $("#interfaceDesc").val()
			var interfaceAdminStatus = 1;
			if(interfaceType != 2){
				interfaceAdminStatus = imgBtn.getDisplayValue() == 'on'? 1:2;
			}
			if(!checkInterfaceId()){
				$("#interfaceId").focus();
				return;
			}
			if(!checkInterfaceDesc()){
				$("#interfaceDesc").focus();
				return;
			}
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
			$.ajax({
				url : '/epon/logicinterface/addLogicInterface.tv',
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
		       	      	html: '<b class="orangeTxt">@interface.addinterfaceSuc@</b>'
		       	    });
					top.getActiveFrame().reloadData();
					cancelClick();
				},
				error : function(json) {
					window.parent.showMessageDlg("@COMMON.tip@", "@interface.addinterfaceFail@");
				},
				cache : false
			});
		}
		function cancelClick(){
			top.closeWindow('addLogicInterface');
		}
	</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">@interface.addInterfaceTitle@</div>
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
	                   	 <input id="interfaceId" maxlength ='4' tooltip="" type="text" class="normalInput w200" />
	                </td>
	            </tr>
	        </tbody>
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt">
	                	@interface.desc@:<span class="redTxt">*</span>
	                </td>
	                <td>
	                   	 <input id="interfaceDesc" maxlength ='31' tooltip="@interface.descTip@" value='Top,interface' type="text" class="normalInput w200" />
	                </td>
	            </tr>
	        </tbody>
	        <tbody id='adminStatusTbody'>
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
	            <li><a href="javascript:;" class="normalBtnBig" onclick="saveClick()"><span><i class="miniIcoAdd"></i>@COMMON.add@</span></a></li>
	            <li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
	        </ol>
	    </div>
	</div>
</body>
</Zeta:HTML>