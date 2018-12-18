<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<Zeta:Loader>
	library jquery
	library zeta
	module demo
	import js.tools.ipText
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
		renderIpText();
		addEventListeners();
	}

	/***********************************************
				执行IP输入框渲染
	***********************************************/
	function renderIpText(){
		/** 初始化并且赋予默认值   **/
		//new ipV4Input("dem11o","demo").setValue("172.17.2.8");
		var demox =  new ipV4Input("ipDemo","demo");
		//demox.setValue("172.17.2.8");
		
		//设置IP输入框不能被更改
		var demox2 =  new ipV4Input("ipDemo2","demo2");
		demox2.setValue("172.17.2.7");
		demox2.setEnable(false);
		//聚焦
		var demox3 =  new ipV4Input("ipDemo3","demo3");
		demox3.focus();
		//设置宽度
		var demox4 =  new ipV4Input("ipDemo4","demo4");
		demox4.width(150);
		//获取值
		var demox5 =  new ipV4Input("ipDemo5","demo5");
		demox5.width(150);
	}
	
	/***********************************************
					添加全局事件监听
	***********************************************/
	function addEventListeners(){
		$("#btFocus").click(function(){
			ipFocus("ipDemo3",1);
		});
		$("#btWidth").click(function(){
			var wid = prompt("@demo.promtTip@",150);
			if(isNaN(wid)){
				return alert("@demo.mustInteger@")
			}
			setIpWidth("ipDemo4",wid);
		});
		$("#btFetch").click(function(){
			var val = getIpValue("ipDemo5");
			alert("@demo.value@：  "+val);
		})
	}
</script>
</head>
<body class="openWinBody">
	<!-- 第一部分，说明文字加图标 -->
	<div class="openWinHeader">
		<div class="openWinTip">@demo.ipInput@</div>
		<div class="rightCirIco linkCirIco"></div>
	</div>
	<!-- 第一部分，说明文字加图标 -->

	<!-- 第二部分 -->
	<div class="content edgeTB10LR20">
		<table class="mCenter tdEdged4">
			<tbody>
				<tr>
					<th>@demo.default@:</th>
					<td class="TD_SEPRETOR"></td>
					<td><div id="demo"></div></td>
					<td class="TD_SEPRETOR"></td>
					<td></td>
				</tr>
				<tr>
					<th>@demo.defaultAndDisable@:</th>
					<td class="TD_SEPRETOR"></td>
					<td><div id="demo2"></div></td>
					<td class="TD_SEPRETOR"></td>
					<td></td>
				</tr>
				<tr>
					<th>focus@demo.focus@:</th>
					<td class="TD_SEPRETOR"></td>
					<td><div id="demo3"></div></td>
					<td class="TD_SEPRETOR"></td>
					<td><button id="btFocus" class="BUTTON75">@demo.focus@</button></td>
				</tr>
				<tr>
					<th>@demo.height@:</th>
					<td class="TD_SEPRETOR"></td>
					<td><div id="demo4"></div></td>
					<td class="TD_SEPRETOR"></td>
					<td><button id="btWidth" class="BUTTON75">@demo.setHeight@</button></td>
				</tr>
				<tr>
					<th>@demo.canntSetHeight@</th>
					<td class="TD_SEPRETOR"></td>
					<td> - </td>
					<td class="TD_SEPRETOR"></td>
					<td></td>
				</tr>
				<tr>
					<th>@demo.fetchVal@</th>
					<td class="TD_SEPRETOR"></td>
					<td> <div id="demo5"></div> </td>
					<td class="TD_SEPRETOR"></td>
					<td><button id="btFetch" class="BUTTON75">@demo.fetch@</button></td>
				</tr>
			</tbody>
		</table>
	</div>
	<!-- 第二部分 -->
</body>
</Zeta:HTML>