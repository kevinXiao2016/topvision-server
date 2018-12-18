<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/cssStyle.inc"%>
<head>
<Zeta:Loader>
    library Jquery
    library Ext
    library Zeta
	module  performance
</Zeta:Loader>
<style type="text/css">
.dataTable td label{ padding-left:40px;}
.dataTable td label input{ margin-right:10px; position:relative; top:2px;}
.outer{ width:100%; height:300px; overflow:auto;}
</style>
<script type="text/javascript">
	$(function(){ 
		$("#fiveC").attr("checked",${nbiMultiPeriod.five_enable});
		$("#tenC").attr("checked",${nbiMultiPeriod.ten_enable});
		$("#fifteenC").attr("checked",${nbiMultiPeriod.fifteen_enable});
		$("#thirtyC").attr("checked",${nbiMultiPeriod.thirty_enable});
		$("#sixtyC").attr("checked",${nbiMultiPeriod.sixty_enable});
	});//end document.ready;
	
	function saveClick() {
		var five_enable = $("#fiveC").attr("checked");
		var ten_enable = $("#tenC").attr("checked");
		var fifteen_enable = $("#fifteenC").attr("checked");
		var thirty_enable = $("#thirtyC").attr("checked");
		var sixty_enable = $("#sixtyC").attr("checked");
		$.ajax({
			url : '/nbi/saveNbiExportConfig.tv',
			type : 'POST',
			data : {
				five_enable:five_enable,
				ten_enable:ten_enable,
				fifteen_enable:fifteen_enable,
				thirty_enable:thirty_enable,
				sixty_enable:sixty_enable
			},
			success : function(json) {
				window.top.closeWaitingDlg();
				top.afterSaveOrDelete({
					title : '@COMMON.tip@',
					html : '<b class="orangeTxt">@resources/COMMON.saveSuccess@</b>'
				});
				cancelClick();
			},
			error : function(json) {
				window.top.showErrorDlg();
			},
			cache : false
		});
	}
	//点击取消按钮;
	function cancelClick() {
	    window.parent.closeWindow("nbiExportConfig");
	}
</script>
</head>
<body class="openWinBody">
	<div class="edge10">	
		<div class="outer">
			<table class="dataTable zebra noWrap" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">
				<thead>
		        	<tr>
		        		<th width="200">@Performance.fileExport@</th>
		        		<th>@Performance.exportPeroid@</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>
							<label><input type="checkbox" id="fiveC"/></label>
						</td>
						<td align="center">5</td>
					</tr>
					<tr>
						<td>
							<label><input type="checkbox" id="tenC" /></label>
						</td>
						<td align="center">10</td>
					</tr>
					<tr>
						<td>
							<label><input type="checkbox" id="fifteenC" /></label>
						</td>
						<td align="center">15</td>
					</tr>
					<tr>
						<td>
							<label><input type="checkbox" id="thirtyC" /></label>
						</td>
						<td align="center">30</td>
					</tr>
					<tr>
						<td>
							<label><input type="checkbox" id="sixtyC"/></label>
						</td>
						<td align="center">60</td>
					</tr>
				</tbody>
			</table>	
		</div>
	</div>
	<div class="noWidthCenterOuter clearBoth">
           <ol class="upChannelListOl pB0 pT60 noWidthCenter">
              <li><a href="javascript:;" class="normalBtnBig" onclick="saveClick()"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
	          <li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
	    </ol>
	</div>
</body>
</Zeta:HTML>