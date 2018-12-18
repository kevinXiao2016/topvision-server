<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library Ext
	library jquery
	library zeta
	module Optical
	import js.nm3k.Nm3kYesNoChange
</Zeta:Loader>
<script type="text/javascript">
	var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
	var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
	var entityId = ${ entityId },
		portIndex = ${ portIndex },
		ponOpticalList = ${ ponOpticalList };
	var blankText = "@optical.noSet@";
	Ext.onReady(function(){
		$(".jsPutNm3kDel").each(function(){
			var thisName = $(this).attr("name");
			var $this = $(this);
			var hasOne = false;
			$.each(ponOpticalList,function(i,o){
				if(o.topPonOptAlarmIndex.toString() == thisName){
					var i = new Nm3kYesNoChange({
						renderTo:"alarmId"+thisName,
						id:"pa"+o.topPonOptAlarmIndex,
						value:o.topPonOptThresholdValue,
						yesTip:"@optical.active@",
						noTip:"@optical.del@",
						firstShow:"no",
						toolTip:"@optical.inputTip@",
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
					toolTip:"@optical.inputTip@",
					width: 100,
					yesValue:blankText
				});
				i.init();
			}
		})
	});	
		
	function checkValue(value){
	    if(isNaN(value) || !value){
	        return false;
	    }
	    if(value < -40 || value > 8){
	       return false;
	    }
		return /^[-]{0,1}[0-9]{1,}$/.test( value );
	}
	function saveHnd(){
	    var txMaxValue = $("#pa"+12307).val();
	    var txMinValue = $("#pa"+12308).val();
	    if(txMaxValue == blankText){
	    	txMaxValue = "";
		}else{
			if(!checkValue(txMaxValue)){
		        return $("#pa"+12307).focus();
		    }
		}
	    if(txMinValue == blankText){
	    	txMinValue = "";
		}else{
			if(!checkValue(txMinValue)){
			    return $("#pa"+12308).focus();
			}
		}
	   if(txMinValue != blankText && txMaxValue != blankText){
		   if(parseInt(txMinValue) > parseInt(txMaxValue)){
		        return $("#pa"+12308).focus();
		    }
		}
	    window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
	    $.ajax({
	        url:'/epon/optical/saveOltPonOptAlarm.tv',cache:false,
	        data : {
	            entityId : entityId,
	            portIndex : portIndex,
	            txMax : txMaxValue,
	            txMin : txMinValue
	        },success : function() {
	            window.top.showMessageDlg("@COMMON.tip@", "@optical.setPonOptSuc@");
	        	cancelClick();
	        },error : function() {
	            window.top.showMessageDlg("@COMMON.tip@", "@optical.setPonOptFail@");
	        }
	    });
	}
	function cancelClick(){
		window.top.closeWindow("oltPonOpticalAlarm");
	}
	
	function refreshData(){
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/optical/refreshPonOpticalAlarm.tv',
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
	function authLoad(){
	    if(!operationDevicePower){
	        R.saveBtn.setDisabled(true);
	    }
	    if(!refreshDevicePower){
	        R.fetchData.setDisabled(true);
	    }
	}
</script>
</head>
<body class=openWinBody onload="authLoad()">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@optical.ponOptTitle@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT60">
		<table class="zebraTableRows" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td class="rightBlueTxt" width="230">@optical.txOptLow@:</td>
				<td width="170">
				<div id="alarmId12308" class="jsPutNm3kDel" name="12308"></div>
				</td>
				<td>dBm</td>
			</tr>
			<tr class="">
				<td class="rightBlueTxt">@optical.txOptUp@:</td>
				<td>
				<div id="alarmId12307" class="jsPutNm3kDel" name="12307"></div>
				</td>
				<td>dBm</td>
			</tr>
		</table>
	</div>
	<Zeta:ButtonGroup>
		<Zeta:Button id="fetchData" onclick="refreshData();" icon="miniIcoEquipment">@BUTTON.fetch@</Zeta:Button>
		<Zeta:Button id="saveBtn" onClick="saveHnd()" icon="miniIcoSave">@BUTTON.apply@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@BUTTON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
<body>
</Zeta:HTML>
