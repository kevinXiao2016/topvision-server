<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
		<%@ include file="/include/cssStyle.inc"%>
		<%@include file="../include/cssStyle.inc"%>
		<fmt:setBundle basename="com.topvision.ems.workbench.resources" var="workbench"/>
		<link rel="stylesheet" type="text/css" href="/css/gui.css" />
		<link rel="stylesheet" type="text/css" href="/css/ext-all.css" />
		<link rel="stylesheet" type="text/css" href="/css/ext-plugin.css" />
		<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/xtheme.css" />
		<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/mytheme.css" />
		<script type="text/javascript" src="/visifire/Visifire.js"></script>
		<script type="text/javascript" src="/js/jquery/jquery.js"></script>
        <script type="text/javascript">
			var target= '<%=request.getParameter("target")%>';
			var entityId= '<%=request.getParameter("entityId")%>';
        	function cancelClick() {
				window.top.closeWindow('realtimeDetail');
			}
            function showChart(chartname, chartdiv) {
                jQuery.ajax({
                    type : "POST",
                    url : '/wlan/loadApHistoryPerformanceChart.tv?target=' + target + '&entityId=' + entityId + '&c=' + Math.random(),
                    dataType : "text",
                    success : function(data) {
                        var vChart = new Visifire('SL.Visifire.Charts.xap', chartname, 724, 324);
                        vChart.setDataXml(data);
                        vChart.render(chartdiv);
                    }
                });
            }
            jQuery(showChart("ChartName", "ChartDiv"));
    	</script>
	</head>
	<body>
        <div id="ChartDiv" style="padding-left:21px; padding-top:20px; width:740px; height:320px;"></div>
        &nbsp;
        <table width=100% height=30px cellspacing=0 cellpadding=0>
      		<tr>
				<td align=right><button class=BUTTON75 onMouseOver="this.className='BUTTON_OVER75'"
					onMouseOut="this.className='BUTTON75'" onclick="cancelClick()"><fmt:message key="button.cancel" bundle="${workbench}" /></button>
				</td>
				<td>&nbsp;</td>
			</tr>
		</table>
	</body>
</html>