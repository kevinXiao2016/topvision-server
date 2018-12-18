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
<head>
<script>
	//save config
	function okClick() {
		var cmtsFlapClearPeriod = $('#cmtsFlapClearPeriod').val();
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
		$.ajax({
			url : '/cmc/saveCmtsFlapClearConfig.tv',
			type : 'POST',
			data : {cmtsFlapClearPeriod:cmtsFlapClearPeriod},
			success : function() {
				window.top.closeWaitingDlg();
				top.afterSaveOrDelete({
					title : '@COMMON.tip@',
					html : '<b class="orangeTxt">@platform/sys.saved@</b>'
				});
				cancelClick();
			},
			error : function() {
				window.top.showErrorDlg();
			},
			cache : false
		});
	}
	//close the dialog
	function cancelClick() {
		window.top.closeWindow('cmcFlapClear');
	}

	Ext.onReady(function() {
		$("#cmtsFlapClearPeriod").val(String(${cmtsFlapClearPeriod}));
	});
	
</script>
	</head>
	<body class="openWinBody">
			<div class="openWinHeader">
				<div class="openWinTip">@cmc.cmcFlapClearInterval@</div>
				<div class="rightCirIco wheelCirIco"></div>
			</div>
			<div class="edgeTB10LR20 pT40">
				<table class="zebraTableRows" cellpadding="0" cellspacing="0"
					border="0">
					<tbody>
						<tr>
							<td class="rightBlueTxt" width="260"><label for="autoRefreshSwitch">@cmc.cmcFlapClearInterval@:</label>
							</td>
							<td class="pR10"><select class="normalSel"
								id="cmtsFlapClearPeriod">
									<option value="1">1@COMMON.day@</option>
									<option value="3">3@COMMON.day@</option>
									<option value="7">7@COMMON.day@</option>
									<option value="15">15@COMMON.day@</option>
									<option value="0">@COMMON.never@</option>
							</select></td>
						</tr>
					</tbody>
				</table>
				<div class="noWidthCenterOuter clearBoth" id="buttonPanel">
					<ol class="upChannelListOl pB0 pT80 noWidthCenter">
						<li><a href="javascript:;" class="normalBtnBig"
							onclick="okClick()" id="autoClearSubmit"> 
							<span> 
								<i class="miniIcoData"> </i> @COMMON.save@
							</span>
						</a></li>
						<li><a href="javascript:;" class="normalBtnBig"
							onclick="cancelClick()"> 
							<span> 
								<i class="miniIcoForbid"> </i>@COMMON.cancel@
							</span>
						</a></li>
					</ol>
				</div>
			</div>
	</body>
</Zeta:HTML>