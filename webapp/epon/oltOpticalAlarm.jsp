<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
	library ext
	library zeta
	library Ext
    module optical
</Zeta:Loader>
<script type="text/javascript">
	var entityId = ${ entityId };
	var	list = ${ oltSysOpticalList };
	function checkValue(value){
	    if(isNaN(value) || !value){
	        return false;
	    }
	    if(value <= 0 || value > 3600){
	       return false;
	    }
		return /^[-]{0,1}[0-9]{1,}$/.test( value / 5 );
	}

	function saveHnd(){
	    var ponRxSoapTimeValue = $("#ponRxSoapTime").val();
	    var ponRxSwitch = $("#ponRxSwitch").attr("checked");
	    var ponTxSoapTimeValue = $("#ponTxSoapTime").val();
	    var ponTxSwitch = $("#ponTxSwitch").attr("checked");
	    var onuUsSoapTimeValue = $("#onuUsSoapTime").val();
	    var onuUsSwitch = $("#onuUsSwitch").attr("checked");
	    var onuDsSoapTimeValue = $("#onuDsSoapTime").val();
	    var onuDsSwitch = $("#onuDsSwitch").attr("checked");
	    if(ponRxSwitch && !checkValue(ponRxSoapTimeValue)){
	        return $("#ponRxSoapTime").focus();
	    }
	    if(ponTxSwitch && !checkValue(ponTxSoapTimeValue)){
	        return $("#ponTxSoapTime").focus();
	    }
	    if(onuUsSwitch && !checkValue(onuUsSoapTimeValue)){
	        return $("#onuUsSoapTime").focus();
	    }
	    if(onuDsSwitch && !checkValue(onuDsSoapTimeValue)){
	        return $("#onuDsSoapTime").focus();
	    }
	    window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
	    $.ajax({
	        url:"/epon/optical/saveOltOptAlarm.tv",cache:false,
	        data:{
	            entityId : entityId,
	            portSfpRxRwr : ponRxSoapTimeValue,
	            portSfpTxRwr : ponTxSoapTimeValue,
	            onuUsTxRwr : onuUsSoapTimeValue,
	            onuDsRxRwr : onuDsSoapTimeValue
	        },success : function() {
	            window.top.showMessageDlg("@COMMON.tip@", "@optical.setOltOptSuc@");
	            cancelClick();
	        },error : function() {
	            window.top.showMessageDlg("@COMMON.tip@", "@optical.setOltOptFail@");
	        }
	    })
	    
	}
	function cancelClick(){
		window.top.closeWindow("alertOpticalHandler");
	}
	
	function refreshData(){
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/optical/refreshSysOpticalAlarm.tv',
			cache:false,
			data : {
				entityId : entityId
			},
			success : function() {
				window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchOk@");
				window.location.reload();
			},
			error : function() {
				window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");;
			}
		});
	}
	
    function checkboxHnd(){
	    var $elements = $(this).parent().parent().find(".soapClazz");
	    var $input = $elements.filter("input");
	    $input.filter("input").data('data',$input.val());
	    if( this.checked ){
	        this.title = "@COMMON.open@";
	        $elements.attr("disabled",false).attr("readonly",false).val($input.data('data') || 0);
	        $elements.eq(1).removeClass("normalInputDisabled").addClass("normalInput")
	    }else{
	        this.title = "@COMMON.close@";
			$elements.attr("disabled",true).attr("readonly",true).val(0);
			$elements.eq(1).removeClass("normalInput").addClass("normalInputDisabled")
	    }
	}
	
	$(function(){
		$(".switchCombo").click(checkboxHnd);
		$.each(list,function(i,o){
			$(".alarm"+o.topSysOptAlarmIndex).val(o.topSysOptAlarmSoapTime);
			$(".combo"+o.topSysOptAlarmIndex).attr("checked" , o.topSysOptAlarmSoapTime!=0);
			$(".combo"+o.topSysOptAlarmIndex).click();
			$(".combo"+o.topSysOptAlarmIndex).attr("checked" , o.topSysOptAlarmSoapTime!=0);
		});
	});
</script>
</head>
<body class=openWinBody>
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@optical.setOltOptTitle@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT20">
			<table class="zebraTableRows">
			<tr>
				<td class="rightBlueTxt">@optical.ponRxOptSwitch@:</td>
				<td><input type="checkbox"   class="switchCombo combo12305" id="ponRxSwitch"/>
				<td class="w20"></td>
				<td class="soapClazz rightBlueTxt">@optical.checkInterval@:</td>
				<td><input tooltip="@optical.oltOptTip@" class="alarm12305 soapClazz normalInputDisabled" style="width:80px;" id="ponRxSoapTime" /> @COMMON.S@</td>
			</tr>
			<tr>
				<td class="rightBlueTxt">@optical.ponTxOptSwitch@:</td>
				<td><input type="checkbox" class="switchCombo combo12307" id="ponTxSwitch" />
				<td class="w20"></td>
				<td class="soapClazz rightBlueTxt">@optical.checkInterval@:</td>
				<td><input tooltip="@optical.oltOptTip@"  class="alarm12307 soapClazz normalInputDisabled" style="width:80px;" id="ponTxSoapTime" /> @COMMON.S@</td>
			</tr>
			<tr>
				<td class="rightBlueTxt">@optical.onuTxOptSwitch@:</td>
				<td><input type="checkbox" class="switchCombo combo16390" id="onuUsSwitch" />
				<td class="w20"></td>
				<td class="soapClazz rightBlueTxt">@optical.checkInterval@:</td>
				<td><input tooltip="@optical.oltOptTip@" style="width:80px;" class="alarm16390 soapClazz normalInputDisabled" id="onuUsSoapTime" /> @COMMON.S@</td>
			</tr>
			<tr>
				<td class="rightBlueTxt">@optical.onuRxOptSwitch@:</td>
				<td><input type="checkbox" class="switchCombo combo16392" id="onuDsSwitch"/>
				<td class="w20"></td>
				<td class="soapClazz rightBlueTxt">@optical.checkInterval@:</td>
				<td><input tooltip="@optical.oltOptTip@" style="width:80px;" class="alarm16392 soapClazz normalInputDisabled" id="onuDsSoapTime" /> @COMMON.S@</td>
			</tr>
		</table>
	</div>
	<Zeta:ButtonGroup>
		<Zeta:Button onclick="refreshData();" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
		<Zeta:Button onClick="saveHnd()" icon="miniIcoSave">@COMMON.apply@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
<body>
</Zeta:HTML>
