<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
	library zeta
	module optical
	import js.nm3k.Nm3kYesNoChange
</Zeta:Loader>
<script type="text/javascript">
	var entityId = ${ entityId };
	var	portIndex = ${ portIndex};
	var onuOpticalList = ${onuOpticalList};
	var blankText = "@optical.noSet@";
	$(function(){
		$(".jsPutNm3kDel").each(function(){
			var thisName = $(this).attr("name");
			var $this = $(this);
			var hasOne = false;
			$.each(onuOpticalList,function(i,o){
				if(o.topOnuOptAlarmIndex.toString() == thisName){
					var i = new Nm3kYesNoChange({
						renderTo:"alarmId"+thisName,
						id:"pa"+o.topOnuOptAlarmIndex,
						value:o.topOnuOptThresholdValue,
						yesTip:"@optical.active@",
						noTip:"@optical.del@",
						firstShow:"no",
						toolTip:"@optical.inputOpticTip@",
						width: 100,
						yesValue:blankText
					});
					i.init();
					hasOne = true;
				}
			});
			if(!hasOne){
				var i = new Nm3kYesNoChange({
					renderTo:"alarmId"+thisName,
					id:"pa"+thisName,
					value:"",
					yesTip:"@optical.active@",
					noTip:"@optical.del@",
					firstShow:"yes",
					toolTip:"@optical.inputOpticTip@",
					width: 100,
					yesValue:blankText
				});
				i.init();
			}
		})
		
	    /* $.each(onuOpticalList,function(i,o){
	        $(".clazz"+ o.topOnuOptAlarmIndex).val( o.topOnuOptThresholdValue );
	    }); */
	});
	function checkValue(value){
	    if(isNaN(value) || !value){
	        return false;
	    }
	    if(value < -65535 || value > 65535){
	      	return false;
	    }
		return /^[-]{0,1}[0-9]{1,}$/.test( value );
	}
	function saveHnd(){
	    var rxMaxValue = $("#pa"+16392).val();
	    var rxMinValue = $("#pa"+16391).val();
	    var txMaxValue = $("#pa"+16390).val();
	    var txMinValue = $("#pa"+16389).val();
	    var onuRxMaxValue = $("#pa"+12305).val();
	    var onuRxMinValue = $("#pa"+12306).val();
	    /* alert($("#pa"+16392).next().find(".yesIco").length)
	    	return; */
	    
	    if(rxMaxValue == blankText){
	    	rxMaxValue = "";
	    }else{
	    	if(!checkValue(rxMaxValue)){
		        return $("#pa"+16392).focus();
		    }
		}
	    if(rxMinValue == blankText){
	    	rxMinValue = "";
	    }else{
	    	if(!checkValue(rxMinValue)){
		        return $("#pa"+16391).focus();
		    }
		}
	    if(rxMaxValue != blankText && rxMinValue != blankText){
		    if(parseInt(rxMinValue) > parseInt(rxMaxValue)){
		        return $("#pa"+16392).focus();
		    }
	    }

	    if(txMaxValue == blankText){
	    	txMaxValue = "";
	    }else{
		    if(!checkValue(txMaxValue)){
		        return $("#pa"+16390).focus();
		    }
	    }

	    if(txMinValue == blankText){
	    	txMinValue = "";
	    }else{
		    if(!checkValue(txMinValue)){
		        return $("#pa"+16389).focus();
		    }
	    }
	    if(txMinValue != blankText && txMaxValue != blankText){
		    if(parseInt(txMinValue) > parseInt(txMaxValue)){
		        return $("#pa"+16390).focus();
		    }
	    }

	    if(onuRxMaxValue == blankText){
	    	onuRxMaxValue = "";
	    }else{
		    if(!checkValue(onuRxMaxValue)){
		        return $("#pa"+12305).focus();
		    }
	    }
	    if(onuRxMinValue == blankText){
	    	onuRxMinValue = "";
	    }else{
		    if(!checkValue(onuRxMinValue)){
		        return $("#pa"+12306).focus();
		    }
	    }
	    if(onuRxMinValue != blankText && onuRxMaxValue != blankText){
		    if(parseInt(onuRxMinValue) > parseInt(onuRxMaxValue)){
		        return $("#pa"+12305).focus();
		    }
	    }
	    window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
	    $.ajax({
	        url:'/epon/optical/saveOltOnuOptAlarm.tv',cache:false,
	        data : {
	            entityId : entityId,
	            portIndex : portIndex, // id or index?
	            rxMax : rxMaxValue,
	            rxMin : rxMinValue,
	            txMax : txMaxValue,
	            txMin : txMinValue,
	            onuRxMax : onuRxMaxValue,
	            onuRxMin : onuRxMinValue
	        },success : function() {
	            window.top.showMessageDlg("@COMMON.tip@", "@optical.setOnuOptSuc@");
	            cancelClick();
	        },error : function() {
	            window.top.showMessageDlg("@COMMON.tip@", "@optical.setOnuOptFail@");
	        }
	    });
	}
	function cancelClick(){
		window.top.closeWindow("oltOnuOpticalAlarm");
	}
	
	function refreshData(){
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/optical/refreshOnuOpticalAlarm.tv',
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
	function clearData(alarmIndex){
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
		$.ajax({
			url : '/epon/optical/deleteOnuOpticalAlarm.tv',
			cache:false,
			data : {
				entityId : entityId,
				portIndex : portIndex,
				alarmIndex : alarmIndex
			},
			success : function() {
				window.parent.showMessageDlg("@COMMON.tip@", "@optical.clearSuc@");
				window.location.reload();
			},
			error : function() {
				window.parent.showMessageDlg("@COMMON.tip@", "@optical.clearFail@");;
			}
		});
	}
</script>
</head>
<body class=openWinBody>
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@optical.onuOpticalSet@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT20">
		<table class="zebraTableRows" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td class="rightBlueTxt" width="230">@optical.rxOptLow@:</td>
				<td>
					<div id="alarmId16391" class="jsPutNm3kDel" name="16391"></div>
				</td>
			</tr>
			<tr>
				<td class="rightBlueTxt">@optical.rxOptUp@:</td>
				<td>
				<div id="alarmId16392" class="jsPutNm3kDel" name="16392"></div>
				</td>
			</tr>
			<tr>
				<td class="rightBlueTxt">@optical.txOptLow@:</td>
				<td>
				<div id="alarmId16389" class="jsPutNm3kDel" name="16389"></div>
				</td>
			</tr>
			<tr>
				<td class="rightBlueTxt">@optical.txOptUp@:</td>
				<td>
				<div id="alarmId16390" class="jsPutNm3kDel" name="16390"></div>
				</td>
			</tr>
			<tr>
				<td class="rightBlueTxt">@optical.ponRxOnuOptLow@:</td>
				<td>
				<div id="alarmId12306" class="jsPutNm3kDel" name="12306"></div>
				</td>
			</tr>
			<tr>
				<td class="rightBlueTxt">@optical.ponRxOnuOptUp@:</td>
				<td>
				<div id="alarmId12305" class="jsPutNm3kDel" name="12305"></div>
				</td>
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
