<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<Zeta:Loader>
    library Jquery
    library Ext
    library Zeta
    module  performance
    css  css.performanceMajorStyle
    import performance.js.performanceMajor
</Zeta:Loader>

<style type="text/css">
.winTip{
	padding-top: 20px;
	margin-left: 15px;	
}

.winIcon{
	position: absolute;
	right: 30px;
	top:15px;
}
.contentDiv{
	margin: 10px 20px;
	padding-top: 10px;
}
.contentTable {
    border-collapse: collapse;
    width: 100%;
}
.contentTable td {
    border-bottom: 1px solid #D6D6D6;
    height: 29px;
    padding: 4px 3px;
    vertical-align: middle;
}
.contentTable td input, .contentTable td span{
	vertical-align: middle;
}
.rightBlueTxt{
	text-align: right;
}
.numberInput{
	width:50px;
	height:18px; 
	padding-left: 4px; 
	padding-top:2px;
	border: 1px solid #7F9EB9; 
	background: #FFF;
}
.buttonDiv{
	text-align: right;
	margin-top: 10px;
}
</style>

<script type="text/javascript">
function cancel(){
	window.parent.closeWindow("applyToAllCmts");
}

function save(){
	var selectedPerfNames = new Array();
	var index = 0;
	$(".perfCheckbox").each(function(){
		var $checkbox = $(this);
		if($checkbox.attr("checked")){
			selectedPerfNames[index++] = $checkbox.attr("id");
		}
	});
	parent.frames["framecmtsPerfParamConfig"].setSelectedPerfNames(selectedPerfNames);
    parent.frames["framecmtsPerfParamConfig"].applyToAllCmts();
	cancel();
}

$(document).ready(function(){
	checkboxBindEventOfMainPage();
	$(".perfCheckbox").attr("checked", true);
	$(".allSelectCheck").attr("checked", true);
});
</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
		<div class="winTip">@Tip.applyToAllCmtsTitle@</div>
		<div class="winIcon"><img src="/images/icons/file_edit.png" width="32" height="32" border=0 /></div>
	</div>

	<form id="selectForm">
		<div class="edgeTB10LR20 pT30">
			<table class="contentTable" cellpadding="0" cellspacing="0" border="0">
				<tbody>
					<tr>
						<td class="rightBlueTxt" width="80">@Performance.sysUptime@:</td>
						<td><input id="sysUptime" name="sysUptime" type="checkbox" class="perfCheckbox"/></td>
						<td class="rightBlueTxt">@Performance.upLinkFlow@:</td>
						<td><input id="upLinkFlow" name="upLinkFlow" type="checkbox" class="perfCheckbox"/></td>
						<td class="rightBlueTxt" width="80">@Performance.channelSpeed@:</td>
						<td><input id="channelSpeed" name="channelSpeed" type="checkbox" class="perfCheckbox"/></td>
						<td colspan="2"></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@Performance.snr@:</td>
						<td><input id="snr" name="snr" type="checkbox" class="perfCheckbox"/></td>
						<td class="rightBlueTxt">@Performance.ber@:</td>
						<td><input id="ber" name="ber" type="checkbox" class="perfCheckbox"/></td>   
						<td colspan="4"></td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@Tip.allSelect@:</td>
						<td><input type="checkbox" id="allSelect" class="allSelectCheck"/></td>
						<td colspan="6"></td>
					</tr>
				</tbody>
			</table>
		</div>
	</form>
	
	<div class="noWidthCenterOuter clearBoth">
    	 <ol class="upChannelListOl pB0 noWidthCenter">
         	<li><a href="javascript:save();" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i>@Tip.save@</span></a></li>
         	<li><a href="javascript:cancel();" class="normalBtnBig"><span>@Tip.off@</span></a></li>
    	 </ol>
	</div>
</body>
</Zeta:HTML>