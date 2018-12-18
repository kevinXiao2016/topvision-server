<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
	module resources
</Zeta:Loader>
<script type="text/javascript" src="/js/jquery/jquery.fakeUpload.js"></script>
<script type="text/javascript">
function initClick(){
	window.top.showOkCancelConfirmDlg(I18N.COMMON.tip, I18N.SYSTEM.note2, function (type) {
		if (type == "cancel") {
			return;
		}
		window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.SYSTEM.note3);
		Ext.Ajax.request({url: "reInitDatabase.tv", success:function () {
			restart();
		}, failure:function () {
			window.parent.showErrorDlg(I18N.COMMON.tip, I18N.SYSTEM.initDataBaseFail);
		}});
	});
}
function restart() {
	if (<%=SystemConstants.getInstance().getBooleanParam(
						"jconsole.standalone.mode", false)%>) {
		$.ajax({url: '../system/restartSystem.tv', type: 'GET',
		success: function() {
			startTimer();
		},
		error: function() {
			window.top.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.rebootSystemFail);
		},
		dataType: 'plain', cache: false});		
	} else {
		//成功右下角提示
		top.afterSaveOrDelete({
	      title: '@COMMON.tip@',
	      html: '<b class="orangeTxt">'+I18N.SYSTEM.note4+'</b>'
	    });
		//window.top.showMessageDlg(I18N.COMMON.tip, I18N.SYSTEM.note4);
	}
}

var timer = null;
function connectTest() {
	$.ajax({url: '../network/ping.tv', type: 'GET',
	success: function() {
		stopTimer();
		window.top.closeWaitingDlg();
	   	window.top.location.href = 'showLogon.tv';
	},
	error: function() {},
	dataType: 'plain', cache: false});	
	
}
function startTimer() {
	if (timer == null) {
		timer = setInterval("connectTest()", 5000);
	}
	window.top.stopEventDispatcher();
}
function stopTimer() {
	if (timer != null) {
		clearInterval();
		timer = null;
	}
}

function checkKeepInput() {
	var reg = /^[0-9]\d*$/;
	var intervalValue = $.trim($("#historyKeepInterval").val());
	
	if(intervalValue == null || intervalValue ==''){
		top.afterSaveOrDelete({
	      title: '@COMMON.tip@',
	      html: '<b class="orangeTxt">@SYSTEM.HistoryData.HistoryDataKeepDurationInput@</b>'
	    });                                 
		$("#historyKeepInterval").focus();
		return false;
	}else if (reg.exec($("#historyKeepInterval").val())
			&& parseInt($("#historyKeepInterval").val()) <= 12
			&& parseInt($("#historyKeepInterval").val()) >= 1) {
		return true;
	} else {
		$("#historyKeepInterval").focus();
		return false;
	}
}


function okClick() {
	if (checkKeepInput()) {
		$.ajax({
      	   url: '/system/cycleHistoryDataClean.tv',
           data: {historyKeepInterval : $('#historyKeepInterval').val()},
           type: 'post',
           cache : false,
           success: function(json) {
        	   top.afterSaveOrDelete({
					title : '@COMMON.tip@',
					html : '<b class="orangeTxt">@COMMON.saveSuccess@</b>'
				});
        	   window.top.closeWindow('modalDlg');
           },
           error: function(json) {
           }
        });
    } 
}


function cancelClick() {
	window.parent.closeWindow('modalDlg');
}

Ext.BLANK_IMAGE_URL = '../images/s.gif';
Ext.onReady(function(){
	Zeta$('buttonPanel').style.display = '';
	
	$("#fake_upload").fakeUpload('init',{
		"width":160,
		"btntext":"@COMMON.browse@..."
	});
    $("#historyKeepInterval").val(${historyKeepInterval});
    
});
</script>
	</head>
	<body class="openWinBody">
		<div class="edge10">
			<div class="zebraTableCaption pT20">
				<div class="zebraTableCaptionTitle">
					<span>@SYSTEM.dataBaseInfo@</span>
				</div>
				<table class="zebraTableRows" cellpadding="0" cellspacing="0"
					border="0">
					<tbody>
						<tr>
							<td class="rightBlueTxt" width="200">@SYSTEM.dataBaseType@</td>
							<td><s:property value="databaseInfo.databaseProductName" /></td>
						</tr>
						<tr class="darkZebraTr">
							<td class="rightBlueTxt">@SYSTEM.version@</td>
							<td><s:property value="databaseInfo.databaseProductVersion" />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="zebraTableCaption pT20 mT10">
				<div class="zebraTableCaptionTitle">
					<span>@SYSTEM.HistoryData.HistoryDataStorageStrategy@</span>
				</div>
				<table class="zebraTableRows" cellpadding="0" cellspacing="0"
					border="0">
					<tbody>
						<tr>
							<td class="rightBlueTxt" width="200">@SYSTEM.HistoryData.HistoryDataKeepDuration@</td>
							<td><input style="width: 50px;" id="historyKeepInterval"
								class="normalInput" name="historyKeepInterval" maxlength="2"
								toolTip="@SYSTEM.HistoryData.HistoryDataKeepDurationFocus@" />
								@SYSTEM.HistoryData.UnitDesc@</td>
						</tr>
					</tbody>
				</table>
				<div class="noWidthCenterOuter clearBoth" id="buttonPanel">
					<ol class="upChannelListOl pB0 pT20 noWidthCenter">
						<li><a href="javascript:;" class="normalBtnBig"
							onclick="okClick()" id="historyKeepSubmit"> <span> <i
									class="miniIcoData"> </i> @SYSTEM.HistoryData.Save@
							</span>
						</a></li>
					</ol>
				</div>
			</div>

			<%-- <div class="zebraTableCaption pT20 mT10">
				<div class="zebraTableCaptionTitle">
					<span>@SYSTEM.initDataBase@</span>
				</div>
				<table class="zebraTableRows" cellpadding="0" cellspacing="0"
					border="0">
					<tbody>
						<tr>
							<td><span class="orangeTxt">@SYSTEM.note5@</span></td>
						</tr>
						<tr class="darkZebraTr">
							<td><Zeta:ButtonGroup>
									<Zeta:Button onClick="initClick()" icon="miniIcoBack">@SYSTEM.initDataBase@</Zeta:Button>
								</Zeta:ButtonGroup></td>
						</tr>
					</tbody>
				</table>
			</div> --%>
			<div class="noWidthCenterOuter clearBoth" id="buttonPanel"
				style="display: none">
				<ol class="upChannelListOl pB0 pT20 noWidthCenter">
					<li><a href="javascript:;" class="normalBtnBig"
						onclick="cancelClick()"><span><i class="miniIcoWrong"></i>@COMMON.close@</span></a></li>
				</ol>
			</div>
		</div>
	</body>
</Zeta:HTML>