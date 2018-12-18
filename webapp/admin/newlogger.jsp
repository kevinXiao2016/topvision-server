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
    LIBRARY Socket
</Zeta:Loader>
<style type="text/css">
body,html{ width:100%; height:100%; overflow:hidden;}
.topInput{ width:375px; overflow:hidden; height:480px;}
.content{ clear:both;}
.toCenter{ border:1px solid #ccc; background:#fff; width:1042px; padding:10px; margin:0px auto 0px; height:400px; clear:both;}
.topPart{ width:1062px; overflow:hidden; height:100px; margin:0 auto; padding-top:10px;}
.side{ width:400px; height:100%; overflow:auto; position:absolute; top:0; left:0;}
.middleLine{width:8px; overflow: hidden; height: 100%;  position:absolute; top:0; left:200px;}
.mainBody{ margin:0px 0px 0px 408px; height:100%; overflow:auto;}
#content{ overflow:auto;}
</style>
<script type="text/javascript">
var GLOBAL_SOCKET;
var ws;
$(DOC).ready(function(){
	var $content = $("#content");
	
	ws = new TopvisionWebSocket('loggerConnector', {
		onopen: function() {
	    	$content.append("<p class='greenTxt'>SOCKET已连接上...</p>");
			R.bt_exec.setDisabled(false);
	    },
	    onmessage: function (message) {
	    	$content.append("<pre>"+message+"</pre>");
	    },
	    onclose: function() {
	    	$content.append("<p class='noWrap'>SOCKET连接已中断!</p>");
			R.bt_exec.setDisabled(true);
	    },
	    onerror: function(err) {
	    	$content.append("<pre>"+err+"</pre>");
	    }
	});
});

function connect(){
	R.bt_exec.setDisabled(true);
	ws.reconnect();
}

function exec(){
	R.bt_stop.setDisabled(false);
	ws.send({
		"SOCKET.QUERY" : $("#inputArea").val()
	});
	/* GLOBAL_SOCKET.sendRequest("loggerConnector",{
		"SOCKET.QUERY" : $("#inputArea").val()
	},true); */
}

function clearContent(){
	$("#content").empty();
}

function stop(){
	R.bt_stop.setDisabled(true);
	ws.close();
	/* GLOBAL_SOCKET.sendRequest("loggerConnector",{
		"SOCKET.QUERY" : "STOP"
	},true); */
}
</script>
</head>
<body class="whiteToBlack">
	<div class="leftMain-LR side" style="padding:0px;">
		<p class="pannelTit">输入命令区域<a href="help/logger.help.doc" class="lightGrayTxt">		下载说明文档</a></p>
		<div id="putInput" class="edge10">
			<textarea id="inputArea" class="normalInput topInput"></textarea>
			<div class="noWidthCenterOuter clearBoth">
				    <Zeta:ButtonGroup>
						<Zeta:Button id="bt_connect" onClick="connect()" icon="miniIcoAdd">重连</Zeta:Button>
						<Zeta:Button id="bt_exec" disabled="true" onClick="exec()" icon="miniIcoForbid">运行</Zeta:Button>
						<Zeta:Button onClick="clearContent()" icon="miniIcoAdd">清屏</Zeta:Button>
						<Zeta:Button id="bt_stop" onClick="stop()" disabled="true" icon="miniIcoAdd">停止</Zeta:Button>
					</Zeta:ButtonGroup>
			</div>
		</div>
	</div>
	<div class="middleLine line-LR" style="left:400px; cursor:default"></div>
	<div class="mainBody">
		<div id="content" class="edge10">
			<pre class="lightGrayTxt">
目前支持的命令如下: UPDATE,ACCEPT,RESET
未支持的命令：ADD APPENDER等
分别举例如下：
1.  UPDATE com.topvision.ems.epon.* AND com.topvision.ems.cmc.* SET	LEVEL = INFO
	表示对于对于epon模块和cmc模块下的所有日志,级别调整为INFO，这些模块里面的所有DEBUG级别的日志将不再打印
2.  RESET
	恢复出厂设置
3.  ENGINE #id RESET
	把id对应的engine恢复出厂设置
4.  ACCEPT com.topvision.ems.epon.* AND com.topvision.ems.cmc.* FILTER  LEVEL >=  INFO
	只将epon模块和cmc模块下的所有日志的级别大于等于INFO级别的日志输出到当前WEB控制台中

注意事项：
1.  严格区分大小写，换行时注意空格问题
2.	执行UPDATE时,WEB控制台将不会有输出,如果要输出请执行ACCEPT...操作
3.	* 是至少匹配一个字符,而不是至少0个字符,操作没有达到预期时可以往此考虑
4.	条件/配置值是不带引号的
常用技巧:
1.	只查看某一个类或者某一个类的方法的日志输出到WEB控制台，便于定位
2.	日志文件过大时,可以修改某个类的日志级别，比如设置级别为TRACE，这样日志文件中则没有这个类或者方法的日志输出了
			</pre>
		</div>
	</div>	
		<script type="text/javascript">
			function autoHeight(){
				var h = $(window).height(),
				    leftH = h - 120,
				    rightH = h - 80;
				if(leftH < 20){ leftH = 50;}
				if(rightH < 20){ rightH = 50;}
				$("#content").height(rightH)
				$("#inputArea").height(leftH);
			};//end autoHeight;
			
			autoHeight();
			$(window).resize(function(){
				autoHeight();
			});//end resize;
			
			</script>
</body>
</Zeta:HTML>
