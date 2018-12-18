<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<title></title>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library Jquery
    library Ext
    library Zeta
</Zeta:Loader>
<style type="text/css">
.td_1{
	width: 80px;
}
.td_2{
	width: 100px;
}
.td_3{
	width: 140px;
}
.tr{
	width: 500px;
}
</style>
<script type="text/javascript">
function insert1Data(){
	window.top.showWaitingDlg('@COMMON.waiting@', '正在插入数据', 'ext-mb-waiting');
	$.ajax({
		url:'/cmHistory/insert1Data.tv',
		success:function(){
			window.top.closeWaitingDlg();
			top.afterSaveOrDelete({
					title: '@COMMON.tip@',
					html: '<b class="orangeTxt">数据插入成功</b>'
		   	});
		},error:function(){
		    top.afterSaveOrDelete({
				title: '@COMMON.tip@',
				html: '<b class="orangeTxt">数据插入失败</b>'
	   		});
		}
	});
}

function insert3600WData(){
	window.top.showWaitingDlg('@COMMON.waiting@', '正在插入数据', 'ext-mb-waiting');
	$.ajax({
		url:'/cmHistory/insert3600WData.tv',
		success:function(){
			window.top.closeWaitingDlg();
			top.afterSaveOrDelete({
					title: '@COMMON.tip@',
					html: '<b class="orangeTxt">数据插入成功</b>'
		   	});
		},error:function(){
		    top.afterSaveOrDelete({
				title: '@COMMON.tip@',
				html: '<b class="orangeTxt">数据插入失败</b>'
	   		});
		}
	});
}
</script>
</head>
<body class="whiteToBlack">
	<div class="jsTabPart  clearBoth">
		<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			<thead>
					<tr>
						<th colspan="7" class="txtLeftTh">CM历史数据测试</th>
					</tr>
			</thead>
			<tbody>
					 <tr>
						<td class="rightBlueTxt td_1"></td>
						<td class="td_1">
					 		<a onclick="insert1Data()" href="javascript:;" class="normalBtn" style="margin-right:2px;">
			        			<span><i class="miniIcoData"></i>插入1条数据</span>
			        		</a>
					 	</td>
					 	<td class="td_1">
					 		<a onclick="insert3600WData()" href="javascript:;" class="normalBtn" style="margin-right:2px;">
			        			<span><i class="miniIcoData"></i>插入3600万条数据</span>
			        		</a>
					 	</td>
					 	<td class="td_1"></td>
					 </tr>
			</tbody>
		</table>
	</div>
	<div id="grid"></div>
</body>
</Zeta:HTML>