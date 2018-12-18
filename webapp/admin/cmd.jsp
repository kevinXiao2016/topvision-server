<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>CMD</title>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
</head>
<body>
	<h2>CMD</h2>
	<div>
		<form action="/admin/cmd.tv" method="post">
			命令: <input id="command" type="text" size="80" class="input" name="m"
				title="DOS命令" value="" /> <input class="button" type="submit"
				value="执行" />
		</form>
		<div>
			<s:if test="m!=null">
				<s:property value='m' escape="false" />
			</s:if>
			<s:else>
				<ul>
					<li>sc start name:启动name服务</li>
					<li>sc stop name:停止name服务</li>
					<li>sc query name:查询name服务状态信息</li>
					<li>sc qc name:查询name服务配置信息</li>
					<li>服务名：Windows:TopvisionNM3000, Topvision Mysql；Linux:nm3000_server, nm3000_mysql</li>
				</ul>
			</s:else>
		</div>
	</div>
</body>
</html>
