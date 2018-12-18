<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
	library zeta
    module epon
	css css.report
</Zeta:Loader>
<script>
var queryVisible = false;
//数据初始化
function doOnload() {
	//初始化，设备的排序方式
	Zeta$('<s:property value="onuSortName"/>Sort').checked = true;
}
//查询按钮
function queryClick() {
	queryForm.action = 'showOnuDeviceAsset.tv';
	queryForm.target = "_self";
	queryForm.submit();
}
//生成PDF
function createPdfClick() {
	queryForm.action = '/report/createDeviceListPdf.tv';
	queryForm.target = "_blank";
	queryForm.submit();
}
//打印预览
function onPrintviewClick() {
	showPrintWnd(Zeta$('pageview'));
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
function onPrintClick() {
	var wnd = window.open();
	showPrintWnd(Zeta$('pageview'), wnd.document);
	var print = function() {
		wnd.print();
		wnd.close();
	};
	if(isFirefox) {
		wnd.setTimeout(print, 500)
	} else {
		print();
	}
	//window.print();
}
//点击选项
function optionClick() {
	var el = Zeta$('querybar');
	if(el.style.display == 'none') {
		el.style.display = '';
		Zeta$('queryVisible').value = '1';
	} else {
		el.style.display = 'none';
		Zeta$('queryVisible').value = '0';
	}	
	document.body.scrollTop = 0;
}
//无用方法
function doOnAfterPrint() {
	Zeta$('header').style.display = 'block';
	Zeta$('footer').style.display = 'block';
	if(queryVisible) {
		Zeta$('querybar').style.display= 'block'
	}
	Zeta$('pageview').style.border = "solid 1px black";
}
function doOnBeforePrint() {
	Zeta$('header').style.display = 'none';
	Zeta$('footer').style.display = 'none';
	Zeta$('pageview').style.border = 0;
	var el = Zeta$('querybar');
	queryVisible = (el.style.display != 'none');
	el.style.display= 'none'
}
//生成EXCEL，目前没有用到
function createExcelClick() {
	queryForm.action = 'createDeviceListExcel.tv';
	queryForm.target = "_blank";
	queryForm.submit();
}
function exportExcelClick() {
	var onuSortName = '<s:property value="onuSortName"/>';
	var operationStatusDisplayable = '<s:property value="operationStatusDisplayable"/>';
	var adminStatusDisplayable = '<s:property value="adminStatusDisplayable"/>';
	window.location.href="/epon/report/exportOnuAssetToExcel.tv?onuSortName=" + onuSortName+ "&operationStatusDisplayable=" + operationStatusDisplayable+ "&adminStatusDisplayable=" + adminStatusDisplayable; 
}
</script>
</head><body class=whiteToBlack onload="doOnload()">
<table style="margin: 0 auto;">

			<tr id=header>
				<td align=center height=60px>
					<!-- 第三部分，按钮组合 --> <Zeta:ButtonGroup>
						<Zeta:Button onClick="exportExcelClick()" icon="miniIcoInfo">@COMMON.exportExcel@</Zeta:Button>
						<Zeta:Button onClick="onPrintviewClick()" icon="miniIcoSearch">@COMMON.preview@</Zeta:Button>
						<Zeta:Button onClick="onPrintClick()" icon="miniIcoPrint">@COMMON.print@</Zeta:Button>
						<Zeta:Button onClick="optionClick()" icon="miniIcoInfo">@COMMON.option@</Zeta:Button>
					</Zeta:ButtonGroup> <!-- 第三部分，按钮组合 -->
				</td>
			</tr>

<tr id=querybar style="display: none">
	<td class=CONDITIONVIEW align=center>
		<form id="queryForm" name="queryForm" action="" method="post">
		    <input type=hidden id="queryVisible" name="queryVisible" value="<s:property value="queryVisible"/>" /> 
			<table cellspacing=8 class="txtLeftTable" style="text-align: left;">
				<tr>
					<td colspan=4 align="left">@report.sortMode@:</td>
				</tr>
				<tr>
					<td colspan=4><table width=240 cellspacing=0 cellpadding=0>
							<tr>
								<td><input type=radio id="onuNameSort" name="onuSortName"
									value="onuName" /><label for="onuNameSort">@report.onuName@</label>
								</td>
								<td><input type=radio id="onuMacAddressSort" name="onuSortName"
									value="onuMacAddress" /><label for="onuMacAddressSort">@report.mac@</label>
								</td>
								<td><input type=radio id="onuTypeSort" name="onuSortName"
									value="onuType" /><label for="onuTypeSort">@report.type@</label>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan=4 align="left">@report.propertyMode@:</td>
				</tr>
				<tr>
					<td colspan=4>
						<table width=400 cellspacing=0 cellpadding=0>
							<tr>
								<td><input <s:if test="nameDisplayable">checked</s:if>
									disabled id="nameDisplayable" name="nameDisplayable"
									type=checkbox value="true" /><label for="nameDisplayable">@report.onuName@</label>
								</td>
								<td><input <s:if test="positionDisplayable">checked</s:if>
									disabled id="positionDisplayable" name="positionDisplayable"
									type=checkbox value="true" /><label for="positionDisplayable">@report.onuAddr@</label>
								</td>
								<td><input <s:if test="macDisplayable">checked</s:if>
									disabled id="macColumn" name="macDisplayable" type=checkbox
									value="true" /><label for="macColumn">@report.mac@</label>
								</td>
 								<td><input <s:if test="typeDisplayable">checked</s:if>
									disabled id="typeColumn" name="typeDisplayable" type=checkbox
									value="true" /><label for="typeColumn">@report.type@</label>
								</td>
							</tr>
							<tr>
								<td><input <s:if test="operationStatusDisplayable">checked</s:if>
									id="operationStatusColumn" name="operationStatusDisplayable" type=checkbox
									value="true" /><label for="operationStatusColumn">@report.operStatus@</label>
								</td>
								<td><input <s:if test="adminStatusDisplayable">checked</s:if>
									id="adminStatusColumn" name="adminStatusDisplayable" type=checkbox
									value="true" /><label for="adminStatusColumn">@report.adminStatus@</label>
								</td>
							</tr>
						</table></td>
				</tr>
				<tr>
					<td colspan=4 align=right><input type="button" class=BUTTON75 onclick="queryClick()" value="@COMMON.query@" /></td>
				</tr>
			</table>
		</form>
	</td>
</tr>
<tr>
	<td id="pageview" align=center width=800 class=REPORTVIEW>
		<table class=REPORTCONTENT>
			<!-- report content start -->
			<tr>
				<td align=center class="REPORT-TITLE">@report.onuAsset@</td>
			</tr>
			<tr>
				<td style="padding-right: 15px; padding-bottom: 15px;"
					align=right><s:date name="statDate"
						format="yyyy-MM-dd HH:mm:ss" />
				</td>
			</tr>
			<tr>
				<td class="REPORT-SUBTITLE4">@report.deviceList@(@report.onuCountNum@: <s:property value="onuTotalNum"/>)</td>
			</tr>
			<tr>
				<td align=center style="padding-top: 10px; padding-bottom: 20px;">
					<table class=myTable width=600px border="1"
						bordercolor="#000000" style="border-collapse: collapse"
						cellpadding="0" cellspacing="0">
						<tr>
							<th align="center"><strong>#</strong></th>
							<s:if test="nameDisplayable">
								<th align="center"><strong>@report.onuName@</strong>
								</th>
							</s:if>
							<s:if test="positionDisplayable">
								<th align="center"><strong>@report.onuAddr@</strong>
								</th>
							</s:if>
							<s:if test="macDisplayable">
								<th align="center"><strong>@report.mac@</strong>
								</th>
							</s:if>
 							<s:if test="typeDisplayable">
								<th align="center"><strong>@report.type@</strong>
								</th>
							</s:if>
							<s:if test="operationStatusDisplayable">
								<th align="center"><strong>@report.operStatus@</strong>
								</th>
							</s:if>
							<s:if test="adminStatusDisplayable">
								<th align="center"><strong>@report.adminStatus@</strong>
								</th>
							</s:if>
						</tr>
						<s:iterator value="deviceListItems" status="statu">
							<tr>
								<td><s:property value="#statu.index+1" /></td>
								<s:if test="nameDisplayable">
									<td><s:property value="onuName" />
									</td>
								</s:if>
								<s:if test="positionDisplayable">
									<td><s:property value="position" />
									</td>
								</s:if>
								<s:if test="macDisplayable">
									<td><s:property value="onuMac" />
									</td>
								</s:if>
								<s:if test="typeDisplayable">
									<td><s:property value="onuTypeString" />
									</td>
								</s:if>
								<s:if test="operationStatusDisplayable">
									<td><s:property value="onuOperationStatusString" />
									</td>
								</s:if>
								<s:if test="adminStatusDisplayable">
									<td><s:property value="onuAdminStatusString" />
									</td>
								</s:if>
							</tr>
						</s:iterator>
					</table></td>
			</tr>
			<!-- report content end -->
		</table></td>
</tr>

<tr id=footer>
	<td align=center height=40px>
		<!-- 第三部分，按钮组合 --> <Zeta:ButtonGroup>
		<Zeta:Button onClick="exportExcelClick()" icon="miniIcoInfo">@COMMON.exportExcel@</Zeta:Button>
		<Zeta:Button onClick="onPrintviewClick()" icon="miniIcoSearch">@COMMON.preview@</Zeta:Button>
		<Zeta:Button onClick="onPrintClick()" icon="miniIcoPrint">@COMMON.print@</Zeta:Button>
		<Zeta:Button onClick="optionClick()" icon="miniIcoInfo">@COMMON.option@</Zeta:Button>
	</Zeta:ButtonGroup> <!-- 第三部分，按钮组合 -->
	</td>
</tr>
</table>
</body></Zeta:HTML>