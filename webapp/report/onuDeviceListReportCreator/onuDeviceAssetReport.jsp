<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module epon
	css css.report
	import report.javascript.CommonMethod
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
        queryForm.action = '/epon/report/showOnuDeviceAsset.tv';
        queryForm.target = "_self";
        queryForm.submit();
    }
    //打印预览
    function onPrintviewClick() {
        showPrintWnd(Zeta$('pageview'));
    }
    //打印
    function onPrintClick() {
        var wnd = window.open();
        showPrintWnd(Zeta$('pageview'), wnd.document);
        var print = function() {
            wnd.print();
            wnd.close();
        };
        if (isFirefox) {
            wnd.setTimeout(print, 500)
        } else {
            print();
        }
        //window.print();
    }
    //点击选项
    function optionClick() {
        $('#queryDiv').toggle();
        $("html,body").animate({scrollTop:"0px"},200);
    }

    function exportExcelClick() {
        var onuSortName = '<s:property value="onuSortName"/>';
        var operationStatusDisplayable = '<s:property value="operationStatusDisplayable"/>';
        var adminStatusDisplayable = '<s:property value="adminStatusDisplayable"/>';
        window.location.href = "/epon/report/exportOnuAssetToExcel.tv?onuSortName=" + onuSortName
                + "&operationStatusDisplayable=" + operationStatusDisplayable + "&adminStatusDisplayable="
                + adminStatusDisplayable;
    }
    $(function(){
    	new Ext.SplitButton({
    		renderTo : "topPutExportBtn",
    		text : "@BUTTON.export@",
    		handler : function(){this.showMenu()},
    		iconCls : "bmenu_exportWithSub",
    		menu : new Ext.menu.Menu({
    			items : [{text : "EXCEL",   handler : exportExcelClick}]
    		})
    	});
    	new Ext.SplitButton({
    		renderTo : "bottomPutExportBtn",
    		text : "@BUTTON.export@",
    		handler : function(){this.showMenu()},
    		iconCls : "bmenu_exportWithSub",
    		menu : new Ext.menu.Menu({
    			items : [{text : "EXCEL",   handler : exportExcelClick}]
    		})
    	});
    });//end document.ready;
</script>
</head>
<body class=whiteToBlack onload="doOnload()">
	<!-- 按钮区域 -->
	<div class="buttonDiv noWidthCenterOuter clearBoth">
		<Zeta:ButtonGroup>
			<%-- <Zeta:Button onClick="exportExcelClick()" icon="miniIcoExport">@COMMON.exportExcel@</Zeta:Button> --%>
			<li id="topPutExportBtn" class="splitBtn"></li>
			<Zeta:Button onClick="onPrintviewClick()" icon="miniIcoSearch">@COMMON.preview@</Zeta:Button>
			<Zeta:Button onClick="onPrintClick()" icon="miniIcoPrint">@COMMON.print@</Zeta:Button>
			<Zeta:Button onClick="optionClick()" icon="miniIcoManager">@COMMON.option@</Zeta:Button>
		</Zeta:ButtonGroup>
	</div>
	
	<!-- 查询区域 -->
	<div id="queryDiv" class="queryDiv zebraTableCaption" style="display: none;">
		<div class="zebraTableCaptionTitle"><span>@report/tip.queryCondition@</span></div>
		<form id="queryForm" name="queryForm" action="" method="post">
			<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				<tbody>
					<tr>
						<td width="200" class="rightBlueTxt">@report.sortMode@:</td>
						<td>
							<div class="floatLeftDiv">
								<input type="radio" id="onuNameSort" name="onuSortName" value="onuName" />
								<label for="onuNameSort">@report.onuName@</label>
							</div>
							<div class="floatLeftDiv">
								<input type="radio" id="onuMacAddressSort" name="onuSortName" value="onuMacAddress" />
								<label for="onuMacAddressSort">@report.mac@</label>
							</div>
							<div class="floatLeftDiv">
								<input type="radio" id="onuTypeSort" name="onuSortName" value="onuType" />
								<label for="onuTypeSort">@report.type@</label>
							</div>
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@report.propertyMode@:</td>
						<td>
							<div class="floatLeftDiv">
								<input <s:if test="nameDisplayable">checked</s:if> disabled="disabled" id="nameDisplayable" 
									name="nameDisplayable" type=checkbox value="true" />
								<label for="nameDisplayable">@report.onuName@</label>
							</div>
							<div class="floatLeftDiv">
								<input <s:if test="positionDisplayable">checked</s:if>  disabled="disabled"
									id="positionDisplayable" name="positionDisplayable" type=checkbox value="true" />
								<label for="positionDisplayable">@report.onuAddr@</label>
							</div>
							<div class="floatLeftDiv">
								<input <s:if test="macDisplayable">checked</s:if> disabled="disabled" id="macColumn" 
									name="macDisplayable" type="checkbox" value="true" />
								<label for="macColumn">@report.mac@</label>
							</div>
							<div class="floatLeftDiv">
								<input <s:if test="typeDisplayable">checked</s:if> disabled="disabled" id="typeColumn" 
									name="typeDisplayable" type=checkbox value="true" />
								<label for="typeColumn">@report.type@</label>
							</div>
							<div class="floatLeftDiv">
								<input <s:if test="operationStatusDisplayable">checked</s:if> id="operationStatusColumn" 
									name="operationStatusDisplayable" type="checkbox" value="true" />
								<label for="operationStatusColumn">@report.operStatus@</label>
							</div>
							<div class="floatLeftDiv">
								<input <s:if test="adminStatusDisplayable">checked</s:if> id="adminStatusColumn" 
									name="adminStatusDisplayable" type=checkbox value="true" />
								<label for="adminStatusColumn">@report.adminStatus@</label>
							</div>
						</td>
					</tr>
					<tr>
						<td></td>
						<td>
							<a style="float: left;" href="javascript:;" class="normalBtn" onclick="queryClick()"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
	
	<!-- 报表内容 -->
	<div class="report-content-div" id="pageview">
		<h1>@report.onuAsset@</h1>
		<h3 id="queryTime"><s:date name="statDate" format="yyyy-MM-dd HH:mm:ss" /></h3>
		<h4>@report.deviceList@(@report.onuCountNum@: <s:property value="onuTotalNum" />)</h4>
		<!-- 报表内容表格 -->
		<table class="reportTable" id="reportTable">
			<thead>
				<tr>
					<th align="center"><strong>#</strong></th>
					<s:if test="nameDisplayable">
						<th align="center"><strong>@report.onuName@</strong></th>
					</s:if>
					<s:if test="positionDisplayable">
						<th align="center"><strong>@report.onuAddr@</strong></th>
					</s:if>
					<s:if test="macDisplayable">
						<th align="center" class="nowrap"><strong>@report.mac@</strong></th>
					</s:if>
					<s:if test="typeDisplayable">
						<th align="center" class=nowrap"><strong>@report.type@</strong></th>
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
			</thead>
			<tbody>
				<s:iterator value="deviceListItemArray" status="statu">
					<tr>
						<td><s:property value="#statu.index+1" /></td>
						<s:if test="nameDisplayable">
							<td><s:property value="onuName" /></td>
						</s:if>
						<s:if test="positionDisplayable">
							<td><s:property value="position" /></td>
						</s:if>
						<s:if test="macDisplayable">
							<td class="nowrap"><s:property value="onuMac" /></td>
						</s:if>
						<s:if test="typeDisplayable">
							<td class="nowrap"><s:property value="onuTypeString" /></td>
						</s:if>
						<s:if test="operationStatusDisplayable">
							<td><s:property value="onuOperationStatusString" /></td>
						</s:if>
						<s:if test="adminStatusDisplayable">
							<td><s:property value="onuAdminStatusString" /></td>
						</s:if>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</div>
	
	<!-- 按钮区域 -->
	<div class="buttonDiv noWidthCenterOuter clearBoth">
		<Zeta:ButtonGroup>
			<%-- <Zeta:Button onClick="exportExcelClick()" icon="miniIcoExport">@COMMON.exportExcel@</Zeta:Button> --%>
			<li id="bottomPutExportBtn" class="splitBtn"></li>
			<Zeta:Button onClick="onPrintviewClick()" icon="miniIcoSearch">@COMMON.preview@</Zeta:Button>
			<Zeta:Button onClick="onPrintClick()" icon="miniIcoPrint">@COMMON.print@</Zeta:Button>
			<Zeta:Button onClick="optionClick()" icon="miniIcoManager">@COMMON.option@</Zeta:Button>
		</Zeta:ButtonGroup>
	</div>
</body>
</Zeta:HTML>