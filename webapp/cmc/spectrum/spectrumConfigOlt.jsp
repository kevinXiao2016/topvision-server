<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
	plugin DateTimeField
	module spectrum
	import cmc.spectrum.spectrumConfigOlt
</Zeta:Loader>
<style type="text/css">
.dataTableRows td{
	word-wrap: break-word;
	word-break:break-all;
	white-space: nowrap;
}
.switch{
	cursor: pointer;
}
</style>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var store = null;
var grid = null;
var entityIp = '${entityIp}';
</script>
</head>
<body>
<div id="queryDiv">
<table class="topSearchTable" cellspacing="0" cellpadding="0" rules="none">
     <tbody>
         <tr>
             <td class="rightBlueTxt w70"> 
             	@COMMON.alias@@COMMON.maohao@
             </td>
             <td class="w100"><input class="normalInput mR10" type="text" style="width: 100px" id="oltName"></input></td>
             
             <td class="rightBlueTxt w70">
             	@spectrum.manageIp@@COMMON.maohao@
             </td>
             <td class="w100"><input class="normalInput mR10" type="text" style="width: 100px" id="entityIp"></input></td>
             
             <td class="rightBlueTxt w70">
             	@spectrum.switchStatus@@COMMON.maohao@
             </td>
			 <td><select id="switch" class="w100 normalSel">
			 	<option value="-1">@COMMON.select@</option>
			 	<option value="0">@COMMON.close@</option>
				<option value="1">@COMMON.open@</option>
			 </select></td>
             
	         <td style="padding:10px;width:250px;">
				<ol class="upChannelListOl pB0">
					<li>
						<a onclick="query();" id="btn1" href="javascript:;" class="normalBtn"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a>
					</li>
				</ol>
			  </td>
         </tr>
     </tbody>
</table>
</div>
</body>
</Zeta:HTML>