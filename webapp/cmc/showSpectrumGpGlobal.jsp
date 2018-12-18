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
	module cmc
</Zeta:Loader>
<script type="text/javascript">
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var entityId = '<s:property value="entityId"/>';
var ccmtsSpectrumGpGlobal = ${ccmtsSpectrumGpGlobalJson};
function doOnload(){
	if(ccmtsSpectrumGpGlobal!=null){
		$("input[name='globalAdminStatus'][value="+ccmtsSpectrumGpGlobal.globalAdminStatus+"]").attr("checked",true);
		$("#snrQueryPeriod").val(ccmtsSpectrumGpGlobal.snrQueryPeriod);
		$("#hopHisMaxCount").val(ccmtsSpectrumGpGlobal.hopHisMaxCount);
	}
}

function saveClick() {
	var globalAdminStatus = $("input[name='globalAdminStatus']:checked").val();
	var snrQueryPeriod = $("#snrQueryPeriod").val();
	if(!checkSnrQueryPeriodValue(snrQueryPeriod)){
		$("#snrQueryPeriod").focus();
		return;
	}
	var hopHisMaxCount = $("#hopHisMaxCount").val();
	if(!checkHopHisMaxCountValue(hopHisMaxCount)){
		$("#hopHisMaxCount").focus();
		return;
	}
	window.top.showWaitingDlg("@COMMON.wait@", "@CMC.GP.onUpdatingGlobal@");
    $.ajax({
        url:'/ccmtsspectrumgp/modifyDeviceGpGlobal.tv?entityId=' + entityId + '&globalAdminStatus=' + globalAdminStatus + "&snrQueryPeriod=" + snrQueryPeriod+ "&hopHisMaxCount=" + hopHisMaxCount,
        type:'POST',
        dateType:'text',
        success:function(response) {
            if (response == "success") {
                window.parent.showMessageDlg("@COMMON.tip@", "@CMC.GP.updateGlobalSuccess@");
            } else {
                window.parent.showMessageDlg("@COMMON.tip@", "@CMC.GP.updateGlobalFaild@");
            }
            cancelClick();
        },
        error:function() {
            window.parent.showMessageDlg("@COMMON.tip@", "@CMC.GP.updateGlobalFaild@");
        },
        cache:false
    });
}

function checkHopHisMaxCountValue(v) {
	var reg1 = /^([0-9])+$/;
	if (!reg1.exec(v)|| (v > 32) || v < 1) {
		return false;
	}else{
		return true;
	}
}

function checkSnrQueryPeriodValue(v) {
	var reg1 = /^([0-9])+$/;
	if (!reg1.exec(v)|| (v > 360) || v < 5) {
		return false;
	}else{
		return true;
	}
}

function cancelClick() {
	window.parent.closeWindow('ccmtsSpectrumGroupGlobal');	
}

function authLoad(){
	if(!operationDevicePower){
		$(":input").attr("disabled",true);
		$("#refreshBt").attr("disabled",false);
		$("#cancelBt").attr("disabled",false);
	}
}

function refreshData() {
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@");
	$.ajax({
		url : '/ccmtsspectrumgp/refreshGpGlobalFromDevice.tv',
		type : 'POST',
		data : "entityId=" + entityId + "&num="	+ Math.random(),
		dataType : "text",
		success : function(text) {
			if (text == 'success') {
				window.parent.showMessageDlg("@COMMON.tip@","@COMMON.fetchOk@");
				window.location.reload();
			} else {
				window.parent.showMessageDlg("@COMMON.tip@","@COMMON.fetchBad@");
			}
		},
		error : function(text) {
			window.parent.showMessageDlg("@COMMON.tip@","@COMMON.fetchEr@");
		},
		cache : false
	});
}
</script>
</head>
<body class="openWinBody" onload="doOnload();authLoad();">
	<div class="openWinHeader">
	    <div class="openWinTip">@network/MENU.globalTitle@</div>
	    <div class="rightCirIco wheelCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT40">
			 <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td class="rightBlueTxt" width="200">@CMC.GP.globalAdminStatus@@COMMON.maohao@</td>
					<td>
					<input type="radio" name="globalAdminStatus" value="1"/>@CMC.GP.yes@ &nbsp;&nbsp;&nbsp;&nbsp; 
					<input type="radio" name="globalAdminStatus" value="2"/>@CMC.GP.no@
					</td>
				</tr>
				 <tr class="darkZebraTr">
					<td class="rightBlueTxt" width="200">@CMC.GP.snrQueryPeriod@@COMMON.maohao@</td>
					<td><input type="text" id="snrQueryPeriod" class="normalInput w160" 
						maxlength="3" toolTip="@CMC.GP.queryPeriodTip@"/>S</td>
				</tr>
				<tr>
					<td class="rightBlueTxt" width="200">@CMC.GP.hopHisMaxCount@@COMMON.maohao@</td>
					<td><input type=text id="hopHisMaxCount" class="normalInput w160"
						maxlength="2" toolTip="@CMC.GP.maxCountTip@"/></td>
				</tr>
			</table>
	</div>
	<div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
	    <ol class="upChannelListOl pB0 pT40 noWidthCenter">
	        <li>
	            <a href="javascript:;" class="normalBtnBig" onclick="refreshData()"  id="refreshBt">
	                <span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span>
	            </a>
	        </li>
	        <li>
	            <a href="javascript:;" class="normalBtnBig" onclick="saveClick()"  id="saveBt">
	                <span><i class="miniIcoSaveOK"></i>@COMMON.ok@</span>
	            </a>
	        </li>
	        <li>
	            <a href="javascript:;" class="normalBtnBig" onclick="cancelClick()" id="cancelBt">
	                <span>@COMMON.cancel@</span>
	            </a>
	        </li>
	    </ol>
	</div>
</body>
</Zeta:HTML>