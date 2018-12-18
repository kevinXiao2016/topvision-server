<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    LIBRARY ext
    LIBRARY jquery
    LIBRARY zeta
    MODULE CongfigBackup
    CSS config/css/configBackupView
    IMPORT js.textDiff.diff_match_patch
    IMPORT config.ConfigBackupService
</Zeta:Loader>
</head>
<body class="bodyWH100percent bodyGrayBg">
	<div id="toolbar" style="height: 35px;width: 100%;"></div>
	<div class="wrapWH100percent" id="subDiv">
		<div class="left-LR" id="sidePart">
			<p class="pannelTit" id="sidePartTit">@Config.eponDeviceList@</p>
			<div id="deviceGridDiv" class="leftMain-LR clear-x-panel-body" style="position:relative;"></div>
			<a href="javascript:;" class="questionTipLink"></a>
		</div>
		<div class="line-LR" id="line"></div>
		<div class="right-LR" id="mainPart">
			<p id="filePathDiv" class="pannelTit">@Config.explanation@@COMMON.maohao@</p>
			<div id="fileShowDiv" class="rightMain-LR">
				@Config.oltConfigFileMgmt.br1@<br />
				@Config.oltConfigFileMgmt.br2@<br />
				<br />
				<br />
				<b class="f14">@Config.oltConfigFileMgmt.br3@</b>
				<br />
				<br />
				@Config.oltConfigFileMgmt.br4@
				<br />
				<br />
				@Config.oltConfigFileMgmt.br5@
				<br />
				<br />
				@Config.oltConfigFileMgmt.br6@
				<br />
				<br />
				@Config.oltConfigFileMgmt.br7@
			</div>
		</div>
	</div>
<div id="compareText" style="visibility:hidden"></div>
<div id="compareTip" title="@Config.clickHide@" class="displayNone"
style="padding:5px;position:absolute;z-index:1000000;top:55px;left:277px;
	border:1px solid black;background-color:#ffffbe;text-align:left;" >@Config.oltConfigFileMgmt.compareTip@</div>

	<script type="text/javascript" src="../js/jquery/dragMiddle.js"></script>
	<script type="text/javascript">
		var supportCmc = false;
		<% if(uc.hasSupportModule("cmc")){%>
			supportCmc = true;
		<% } %>
		$(function(){
			//现实提示框;
			showSideTip();
			//设置定时器，10秒后关闭提示框;
			tip.interval = window.setInterval(function(){
				tip.changeSecond();
			}, 1000);
			$(".questionTipLink").hover(function(){
				showSideTip();
			},function(){
				closeSideTip();
			});
			
			function autoHeight(){
				var subH = $(window).height() - $("#toolbar").outerHeight();
				$("#subDiv").height(subH);
				var h = $(".wrapWH100percent").outerHeight();
				var leftTitH  = $("#sidePartTit").outerHeight();
				var leftMainH = h - leftTitH - 10;//因为padding:5px;
				if(leftMainH < 0){ leftMainH = 200;}
				$("#deviceGridDiv").height(leftMainH);
				
				var rightTitH = $("#filePathDiv").outerHeight();
				var rightBottomH = $(".upChannelListOl").outerHeight();
				var rightMainH = h - rightTitH - rightBottomH - 32; //因为padding:16px;
				if(rightMainH < 0){ rightMainH = 200;}
				$("#fileShowDiv").height(rightMainH);
			}
			autoHeight();
			$(window).resize(function(){
				autoHeight();
			});//end resize;
			
		});//end document.ready;
		
		$(window).load(function(){
	 		//左侧可以拖拽宽度;
	 		var o1 = new DragMiddle({ id: "line", leftId: "sidePart", rightId: "mainPart", minWidth: 200, maxWidth:400,leftBar:true });
	 		o1.init();
		});//end window.onload;
	</script>

</body>
</Zeta:HTML>