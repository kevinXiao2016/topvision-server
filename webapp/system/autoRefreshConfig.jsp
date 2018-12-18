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
	module platform
    import js.tools.ipText
</Zeta:Loader>
	<head>
<script>
//check Retries input
	function checkIntervalInput() {
		var reg = /^[0-9]\d*$/;
		var intervalValue = $.trim($("#autoRefreshInterval").val());
		
		if(intervalValue == null || intervalValue ==''){
			top.afterSaveOrDelete({
		      title: '@sys.tip@',
		      html: '<b class="orangeTxt">@sys.autoRefreshIntervalInput@</b>'
		    });                                 
			$("#autoRefreshInterval").focus();
			return false;
		}else if (reg.exec($("#autoRefreshInterval").val())
				&& parseInt($("#autoRefreshInterval").val()) <= 1440
				&& parseInt($("#autoRefreshInterval").val()) >= 15) {
			return true;
		} else {
			$("#autoRefreshInterval").focus();
			return false;
		}
    }

	//save config
	function okClick() {
		if (checkIntervalInput()) {
			var switchValue = $('#autoRefreshSwitch').val();
			var intervalValue = $('#autoRefreshInterval').val();
			window.top.showWaitingDlg("@sys.waiting@", "@sys.saveWaiting@");
			$.ajax({
				url : '/network/saveAutoRefreshConfig.tv',
				type : 'POST',
				data : {autoRefreshSwitch:switchValue, autoRefreshInterval:intervalValue * 60000},
				success : function(json) {
					window.top.closeWaitingDlg();
					top.afterSaveOrDelete({
						title : '@sys.tip@',
						html : '<b class="orangeTxt">@sys.saved@</b>'
					});
					//window.parent.showMessageDlg("@sys.tip@","@sys.saved@");
					cancelClick();
				},
				error : function(json) {
					window.top.showErrorDlg();
				},
				cache : false
			});
		}
	}
	//close the dialog
	function cancelClick() {
		window.top.closeWindow('modalDlg');
	}

	Ext.onReady(function() {
		$("#autoRefreshSwitch").val(String(${autoRefreshSwitch}));
		$("#autoRefreshInterval").val(${autoRefreshInterval} / 60000);
	});
	
	
	function changeSwitch(){
	    var st = $("#autoRefreshSwitch").val();
	    if (st == 'true'){
	        $("#autoRefreshInterval").attr("disabled",false);
	    } else {
	        $("#autoRefreshInterval").attr("disabled",true);
	    }
	}
</script>
	</head>
	<body class="openWinBody">
		<div class=formtip id=tips style="display: none"></div>
		<form id="autoRefreshConfigForm" name="autoRefreshConfigForm">
			<div class="openWinHeader">
				<div class="openWinTip">@sys.setAutoRefreshConfig@</div>
				<div class="rightCirIco wheelCirIco"></div>
			</div>
			<div class="edgeTB10LR20 pT40">
				<table class="zebraTableRows" cellpadding="0" cellspacing="0"
					border="0">
					<tbody>
						<tr>
							<td class="rightBlueTxt" width="200"><label for="autoRefreshSwitch">@sys.autoRefreshSwitch@:</label>
							</td>
							<td class="pR10"><select class="normalSel"
								id="autoRefreshSwitch">
									<option value="true">@sys.autoRefreshOpen@</option>
									<option value="false">@sys.autoRefreshClose@</option>
							</select></td>
						</tr>
						<tr>
							<td class="rightBlueTxt"><label for="autoRefreshInterval">@sys.autoRefreshInterval@:</label>
							</td>
							<td><input style="width: 150px;" id="autoRefreshInterval"
								class="normalInput" name="autoRefreshInterval" maxlength="6" toolTip="@sys.autoRefreshIntervalFocus@" />
								(@sys.Unit@:@sys.minutes@)</td>
						</tr>
					</tbody>
				</table>
				<div class="noWidthCenterOuter clearBoth" id="buttonPanel">
					<ol class="upChannelListOl pB0 pT40 noWidthCenter">
						<li><a href="javascript:;" class="normalBtnBig"
							onclick="okClick()" id="autoRefreshSubmit"> <span> <i
									class="miniIcoData"> </i> @sys.save@
							</span>
						</a></li>
						<li><a href="javascript:;" class="normalBtnBig"
							onclick="cancelClick()"> <span> <i
									class="miniIcoForbid"> </i> @sys.cancel@
							</span>
						</a></li>
					</ol>
				</div>
			</div>
		</form>
	</body>
</Zeta:HTML>