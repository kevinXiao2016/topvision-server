<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library jquery
</Zeta:Loader>
	</head>
	<script type="text/javascript">
		function debug() {
			$.ajax({
				url : '/admin/runScriptlet.tv',
				cache : false,
				dataType : 'text',
				data : {
					m : $("#rubyscript").val()
				},
				success : function(response) {
					$("#result").html(response);
				},
				error : function(e, status) {
					$("#result").html(e.message);
				}
			});
		};
	</script>
	<body>
		<center>
			<h2>Debug</h2>
		</center>
		<div>
			<h2>执行JRuby脚本</h2>
			<p>主要用于调试时点击页面来调用后台程序，点击Debug执行以下JRuby脚本，如果为空，则执行后台/com/topvision/platform/debug/debugService.rb脚本</p>
			<p>JRuby脚本:</p>
			<textarea id="rubyscript" type="text" class="input" name="rubyscript"
				title="JRuby脚本" value="" rows="20" cols="180"></textarea>
			<input class="button" type="submit" value="Run"
				onclick="debug();return null;" />
		</div>
		<div>
			执行返回结果：<br>
			<div id="result"></div>
		</div>
	</body>
</Zeta:HTML>