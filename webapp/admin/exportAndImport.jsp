<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<link rel="stylesheet" href="/performance/css/smoothness/jquery-ui-1.10.3.custom.min.css" />
<script src="/performance/js/jquery-1.8.3.js"></script>
<Zeta:Loader>
    library ext
    library zeta
    module CMC
    css css/bootstrap
    import js/jquery/jquery.fakeUpload
    import js/jQueryFileUpload/ajaxfileupload
    import admin/exportAndImport
</Zeta:Loader>

<style type="text/css">
body{
	padding-top: 20px;
}
.normalInput, .normalInputDisabled{
	height: 24px;
}
</style>

<script type="text/javascript">
</script>
</head>
<body>
	<div class="container">
		<!-- 导出部分  -->
		<div class="panel panel-default">
			<div class="panel-heading">导出网管结构数据</div>
		  	<div class="panel-body">
		    	<p>导出网管结构数据将生成一个包含
		    		<mark>角色信息</mark>,
		    		<mark>角色的权限信息</mark>,
		    		<mark>用户基本信息</mark>,
		    		<mark>用户权限地域</mark>,
		    		<mark>用户个性化</mark>,
		    		<mark>用户角色关联</mark>,
		    		<mark>地域基本信息</mark>,
		    		<mark>设备信息</mark>
		    		的excel文件.
		    	</p>
		    	<button type="button" class="btn btn-default btn-sm" id="exportBtn">
		    		<span class="glyphicon glyphicon-export" aria-hidden="true"></span>
		    		导出EXCEL
		    	</button>
		  	</div>
		</div>
		
		<!-- 导入部分  -->
		<div class="panel panel-default">
			<div class="panel-heading">导入网管结构数据</div>
		  	<div class="panel-body">
		    	<div class="alert alert-danger" role="alert">请导入<mark>未改动结构</mark>的由<mark>导出网管结构数据功能</mark>生成的EXCEL文件！并确保数据库是初始化后未添加内容的状态！</div>
			  	<div style="margin-bottom:10px;">
				  	<span id="localFile" name="localFile"></span>
			  	</div>
			  	<button id="importBtn" type="button" class="btn btn-default btn-sm">
			  		<span class="glyphicon glyphicon-import" aria-hidden="true"></span>
			  		导入EXCEL
			  	</button>
			  	
			  	<div id="importDetail">
			  		<h4>导入进展</h4>
				  	<div class="well" id="importDetail-well">
				  	</div>
			  	</div>
		  	</div>
		</div>
	</div>
</body>
</Zeta:HTML>