<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
	library ext
	library zeta
    module epon
    css css/white/disabledStyle
</Zeta:Loader>
<script type="text/javascript">
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var entityId = <s:property value="entityId"/>;
var uniId = '<s:property value = "uniId"/>';
var topUniAttrMacAddrLearnMaxNum = '<s:property value = "topUniAttrMacAddrLearnMaxNum"/>';
function loadMax(){
	if(topUniAttrMacAddrLearnMaxNum == -1){
		$("#topUniAttrMacAddrLearnMaxNum").val();
	}else{
		$("#topUniAttrMacAddrLearnMaxNum").val(topUniAttrMacAddrLearnMaxNum);
	}
}
function saveClick() {
	macAddrLearnMaxNum = $("#topUniAttrMacAddrLearnMaxNum").val();
	//ONU别名修改提交验证
	if(uniMacAddrLearnMaxNumCheck()){		
		showWaitingDlg("@COMMON.wait@", "@SERVICE.settingUniMacMaxLearn@" , 'ext-mb-waiting');	
		$.ajax({
			url: '/onu/modifyUniMacAddrLearnMaxNum.tv', 
			type: 'POST',
			data: "entityId="+entityId+"&uniId="+uniId+"&topUniAttrMacAddrLearnMaxNum="+macAddrLearnMaxNum,
	        dataType:"text",
	        success: function(text) {
		        if(text == 'success'){
		        	//sinceOnuName = onuName;
					window.parent.showMessageDlg("@COMMON.wait@", "@SERVICE.setUniMacMaxLearnOk@" );
					//updateOnuJson();
					cancelClick();
		        }else{
		        	window.parent.showMessageDlg("@COMMON.wait@", text, 'error');
			    }
	        }, error: function(text) {
	        	window.parent.showMessageDlg("@COMMON.wait@", "@SERVICE.setUniMacMaxLearnEr@" ,'error');
	    }, cache: false
	    });
	}
}

function refreshData(){
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
	$.ajax({
		url: '/onu/refreshUniUSUtgPri.tv', 
		type: 'POST',
		data: {
			entityId: entityId,
			uniId: uniId
		},
        dataType:"text",
        success: function(text) {
	        if(text == 'success'){
	        	window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchOk@");
				window.location.reload();
	        }else{
	        	window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");;
		    }
        }, 
        error: function() {
        	window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");;
    	}, 
    	cache: false
    });
}

//UNI Mac地址最大学习数校验
function uniMacAddrLearnMaxNumCheck(){
	//$("#onuName").css("border","1px solid #8bb8f3");
	var reg = /^([0-9])+$/;
	var MaxNum = $("#topUniAttrMacAddrLearnMaxNum").val();
	if(!reg.exec(MaxNum) || parseInt(MaxNum) < 0 || parseInt(MaxNum) > 64){
		//$("#onuName").css("border","1px solid #FF0000");
		$("#topUniAttrMacAddrLearnMaxNum").focus();
		return false;
	}
	/* if (MaxNum == topUniAttrMacAddrLearnMaxNum){
		return false;
	} */
	return true;
}

function cancelClick() {
	window.parent.closeWindow('uniMacAddrConfig');
}
function updateOnuJson() {
	window.parent.getFrame("entity-" + entityId).updateOnuJson();
}
function showWaitingDlg(title, icon, text, duration) {
	window.top.showWaitingDlg(title, icon, text, duration);
}
function authLoad(){
	if(!operationDevicePower){
	    $("#topUniAttrMacAddrLearnMaxNum").attr("disabled",true);
	    R.saveBt.setDisabled(true);
	}
	if(!refreshDevicePower){
		R.refreshData.setDisabled(true);
	}
}
</script>
</head>
	<body class="openWinBody" onload="loadMax();authLoad();">
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@SERVICE.uniMacMaxLearn@</div>
			<div class="rightCirIco pageCirIco"></div>
		</div>

		<div class="edgeTB10LR20 pT20">
			<table class="zebraTableRows">
				<tr >
					<td class="rightBlueTxt w200 withoutBorderBottom">@SERVICE.macMaxLearn@:</td>
					<td class="withoutBorderBottom"><input id="topUniAttrMacAddrLearnMaxNum" maxlength="2"
						type=text class="normalInput w200"
						tooltip="@SERVICE.uniMacAddrLearnMaxNumTip@" /></td>
				</tr>
			</table>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button id="refreshData" onclick="refreshData();" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
			<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoSave">@COMMON.apply@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>