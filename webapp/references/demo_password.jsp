<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
	library zeta
	module demo
</Zeta:Loader>
<style type="text/css">
.TD_SEPRETOR{width: 10px;}
button{display: inline;}
</style>
<script type="text/javascript">
	/***********************************************
						程序入口
	***********************************************/
	$( DOC ).ready( initialize );
	function initialize(){
		addEventListeners();
	}
	function addEventListeners(){
		$("#fetch").click(function(){
			var val = $("#password2").val();
			alert("@demo.passValue@  " + val);
		});
	}
</script>
</head>
<body class="openWinBody">
	<!-- 第一部分，说明文字加图标 -->
	<div class="openWinHeader">
		<div class="openWinTip">@demo.passInput@</div>
		<div class="rightCirIco linkCirIco"></div>
	</div>
	<!-- 第一部分，说明文字加图标 -->
	
	<!-- 第二部分 -->
	<div class="content edgeTB10LR20">
		<table class="mCenter tdEdged4">
			<tr>
				<th>@demo.default@180px:</th>
				<td class="TD_SEPRETOR"></td>
				<td><Zeta:Password id="password" /></td>
				<td class="TD_SEPRETOR"></td>
				<td></td>
			</tr>
			<tr height="5px;"></tr>
			<tr>
				<th>250px:</th>
				<td class="TD_SEPRETOR"></td>
				<td><Zeta:Password id="password1" width="250px" tooltip="@demo.plsIptPass@" /></td>
				<td class="TD_SEPRETOR"></td>
				<td></td>
			</tr>
			<tr height="5px;"></tr>
			<tr>
				<th>350px:</th>
				<td class="TD_SEPRETOR"></td>
				<td><Zeta:Password id="password2" width="350px" /></td>
				<td class="TD_SEPRETOR"></td>
				<td><button class="BUTTON75" id="fetch">@demo.fetch@</button></td>
			</tr>
		</table>
	</div>
	<!-- 第二部分 -->
</body>
</Zeta:HTML>