<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<Zeta:HTML>
<HEAD>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	LIBRARY jquery
	MODULE PLATFORM
</Zeta:Loader>
</HEAD>
<style>
th ,td{padding-left: 3px;padding-right: 3px;height: 23px;text-align: center;font-size: 15px;}
body ,html{ overflow: hidden;}
body{ padding: 15px;}
#buttonDiv{ position:absolute; margin-top:5px; align:right;}
.contentTable {border-color: #000;height:300px;width: 560px;}
</style>
<script type="text/javascript">
function cancelClick(){
	window.parent.closeWindow('restartDetail');
}
//打印预览
function onPrintviewClick() {
    showPrintWnd(pageView);
}
function showPrintWnd(obj, doc) {
    if(doc == null) {
        var wnd = window.open();
        doc = wnd.document;
    } else {
        doc.open();
    }
    doc.write('<html>');
    doc.write('<head>');
    doc.write('<title><s:property value="title"/></title>');
    doc.write('<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />');
    doc.write('<link rel="stylesheet" type="text/css" href="../css/report.css"/>');
    Zeta$NoHeaderAndFooter(doc);
    doc.write('</head>');
    doc.write('<body style="margin:50px;"><center>');
    doc.write(obj.innerHTML);
    doc.write('</center></body>');
    doc.write('</html>');
    doc.close();
}
//打印
function exportRestartDetail() {
	window.location.href="/performance/exportRestartDetail.tv?startTime=${startTime}&endTime=${endTime}&entityId=${entityId}&deviceIndex=${deviceIndex}";
}
</script>
<body class=openWinBody>
      <div id="toolbar" style="position: absolute; left: 0; top: 0;width:100%"></div>
      <div class="edge10">
			<table class="mCenter zebraTableRows" id="pageView" style="width:580px;height: 395px;overflow:scroll; ">
		<!-- report content start -->
		<tr>
			<td align=center style="font-size: 15pt;font-weight: 700;">设备 ${deviceDisplayName} 重启情况统计</td>
		</tr>
		<tr>
              <td style="padding-right: 15px; padding-bottom: 15px;align:right;text-align: center;" >
                                                统计区间：${startTime} 至 ${endTime}
              </td>
        </tr>
		<tr>
		    <td >
		      <div style="width: 585px;height: 330px;overflow: auto;" class="contentTable" >
		      <table style="border-collapse: collapse" border="1"
                        bordercolor="#000000">
		          <tr>
		              <th width="220"><b>重启时间</b></th>
		              <th width="280"><b>持续时长</b></th>
		          </tr>
		          <s:iterator value="restartRecords">
			          <tr>
			              <td><s:property value="deviceReStart"/></td>
			              <td><s:property value="runningTimeString"/></td>
			          </tr>
		          </s:iterator>
		      </table>
		      </div>
		    </td>
		</tr>
	</table>
	</div>
    <Zeta:ButtonGroup>
		<Zeta:Button onClick="exportRestartDetail()" icon="miniIcoSaveOK">@COMMON.exportExcel@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" >@COMMON.close@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>
