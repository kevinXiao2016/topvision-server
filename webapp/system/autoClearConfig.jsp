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
	function okClick() {
		
		var reg = /^[0-9]{1,3}$/,
		    autoClearCmtsPeriod = $("#autoClearCmtsPeriod").val(),
		    autoClearCmciPeriod = $("#autoClearCmciPeriod").val(),
		    autoClearOnuPeriod = $("#autoClearOnuPeriod").val();
		
		if(!reg.test(autoClearOnuPeriod) || autoClearOnuPeriod < 0 || autoClearOnuPeriod > 365){
			$("#autoClearOnuPeriod").focus();
			return;
		}
		if(!reg.test(autoClearCmciPeriod) || autoClearCmciPeriod < 0 || autoClearCmciPeriod > 365){
			$("#autoClearCmciPeriod").focus();
			return;
		}
		if(!reg.test(autoClearCmtsPeriod) || autoClearCmtsPeriod < 0 || autoClearCmtsPeriod > 365){
			$("#autoClearCmtsPeriod").focus();
			return;
		}
		window.top.showWaitingDlg("@sys.waiting@", "@sys.saveWaiting@");
		$.ajax({
			url : '/autoclear/saveAutoClearConfig.tv?autoClearCmtsPeriod=' + autoClearCmtsPeriod + "&autoClearOnuPeriod=" + autoClearOnuPeriod + "&autoClearCmciPeriod=" + autoClearCmciPeriod,
			type : 'POST',
			success : function(json) {
				window.top.closeWaitingDlg();
				top.afterSaveOrDelete({
			      title: '@sys.tip@',
			      html: '<b class="orangeTxt">@sys.saved@</b>'
			    });
				cancelClick();
			},
			error : function(json) {
				window.top.closeWaitingDlg();
				window.top.showErrorDlg();
			},
			cache : false
		});
	}
	function cancelClick() {
		window.top.closeWindow('modalDlg');
	}
	Ext.onReady(function() {
		$("#autoClearCmtsPeriod").val(${autoClearCmtsPeriod});
		$("#autoClearCmciPeriod").val(${autoClearCmciPeriod});
		$("#autoClearOnuPeriod").val(${autoClearOnuPeriod});
		if(!<%= uc.hasSupportModule("cmc")%>){
			$("#cmci").hide();
			$("#cmcii").hide();
		}
	});
</script>
	</head>
	<body class="openWinBody">
		<div class=formtip id=tips style="display: none"></div>
		<form id="autoClearConfigForm" name="autoClearConfigForm">
			<div class="openWinHeader">
				<div class="openWinTip">@sys.autoClearConfig@</div>
				<div class="rightCirIco wheelCirIco"></div>
			</div>
			<div class="edgeTB10LR20 pT40">
				<table class="zebraTableRows" cellpadding="0" cellspacing="0"
					border="0">
					<tbody>
						<tr>
							<td class="rightBlueTxt" width="220"><label
								for="autoClearOnuPeriod">@sys.autoClearOnuConfig@:</label></td>
							<td>
								<ul class="leftFloatUl">
									<li>
										<input id="autoClearOnuPeriod" type="text" class="normalInput w140" maxlength="3" toolTip="@sys.clearTimeTip@" value="0" />
									</li>
									<li class="pT4">
										@ENTITYSNAP.deviceUpTime.day@
									</li>
								</ul>
								
							</td>
						</tr>
						<tr class="darkZebraTr" id="cmci">
							<td class="rightBlueTxt"><label for="autoClearCmciPeriod">@sys.autoClearCmciConfig@:</label>
							</td>
							<td>
								<ul class="leftFloatUl">
									<li>
										<input id="autoClearCmciPeriod" type="text" class="normalInput w140" maxlength="3" toolTip="@sys.clearTimeTip@" value="0" />
									</li>
									<li class="pT4">
										@ENTITYSNAP.deviceUpTime.day@
									</li>
								</ul>
							</td>
						</tr>
						<tr class="darkZebraTr" id="cmcii">
							<td class="rightBlueTxt"><label for="autoClearCmtsPeriod">@sys.autoClearCmciiConfig@:</label>
							</td>
							<td>
								<ul class="leftFloatUl">
									<li>
										<input id="autoClearCmtsPeriod" type="text" class="normalInput w140" maxlength="3" toolTip="@sys.clearTimeTip@" value="0" />
									</li>
									<li class="pT4">
										@ENTITYSNAP.deviceUpTime.day@
									</li>
								</ul>
							</td>
						</tr>
					</tbody>
				</table>
				<div class="noWidthCenterOuter clearBoth" id="buttonPanel">
					<ol class="upChannelListOl pB0 pT50 noWidthCenter">
						<li><a href="javascript:;" class="normalBtnBig"
							onclick="okClick()"> <span> <i class="miniIcoData">
								</i> @sys.save@
							</span>
						</a></li>
						<li><a href="javascript:;" class="normalBtnBig"
							onclick="cancelClick()"> <span> <i
									class="miniIcoForbid"></i> @sys.cancel@
							</span>
						</a></li>
					</ol>
				</div>
			</div>
		</form>
	</body>
</Zeta:HTML>