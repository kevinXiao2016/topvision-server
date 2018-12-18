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
	css css/white/disabledStyle
	module spectrum
	import cmc.spectrum.spectrumVideoMgmt
    import js/nm3k/nm3kPickDate
</Zeta:Loader>
<script type="text/javascript">
var startTime = null;
var endTime = null; 
var pageSize = <%= uc.getPageSize() %>;
var store = null;
var grid = null;
</script>
</head>
<body class="whiteToBlack">
<div id="queryDiv">
<table class="topSearchTable" width="100%" border="0" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="none">
     <tbody>
         <tr>
             <td class="rightBlueTxt" width="50"> 
             	@COMMON.alias@@COMMON.maohao@
             </td>
             <td width="110"><input class="normalInput mR10" type="text" style="width: 100px" id="cmtsAlias"></input></td>
             
             <td class="rightBlueTxt" width="70">
             	@spectrum.videoName@@COMMON.maohao@
             </td>
             <td width="110"><input class="normalInput mR10" type="text" style="width: 100px" id="videoName"></input></td>
             
             <td class="rightBlueTxt" width="70">
             	@COMMON.startTime@@COMMON.maohao@
             </td>
             <td width="160"><div id="startTime" class="w150"></div></td>
             
             <td class="rightBlueTxt" width="70">
             	@COMMON.endTime@@COMMON.maohao@
             </td>
             <td width="160"><div id="endTime" class="w150"></div></td> 
             
	         <td rowspan="2">
				<ol class="upChannelListOl pB0">
					<li style="margin-bottom:4px;">
						<a id="btn1" href="javascript:;" class="normalBtn normalBtnWithDateTime" onclick="query()"><span><i class="miniIcoSearch"></i><b>@COMMON.query@</b></span></a>
					</li>
					<li style="margin-bottom:4px;">
						<a onclick="clearVideo();" id="btn3" href="javascript:;" class="normalBtn"><span><i class="miniIcoClose"></i>@spectrum.clearRecord@</span></a>
					</li>
					<li style="margin-bottom:4px;">
						<a onclick="deleteVideo();" id="btn2" disabled="disabled" href="javascript:;" class="normalBtn"><span><i class="miniIcoClose"></i>@spectrum.batchDelete@</span></a>
					</li>
				</ol>
			  </td>
         </tr>
         
         <tr>
             <td class="rightBlueTxt"> 
             	@spectrum.userName@@COMMON.maohao@
             </td>
             <td><input class="normalInput mR10" type="text" style="width: 100px" id="userName"></input></td>
             
             <td class="rightBlueTxt">
             	@spectrum.terminalIp@@COMMON.maohao@
             </td>
             <td><input class="normalInput mR10" type="text" style="width: 100px" id="terminalIp"></input></td>
             
             <td class="rightBlueTxt">
             	@spectrum.realTimeOrHis@@COMMON.maohao@
             </td>
			 <td><select id="videoType" class="normalSel w150">
			 	<option value="0">@spectrum.allVideo@</option>
				<option value="1">@spectrum.realTimeVideo@</option>
				<option value="2">@spectrum.historyVideo@</option>
			 </select></td>
			 <td class="rightBlueTxt"></td>
             <td></td> 
         </tr>
     </tbody>
</table>
</div>
</body>
</Zeta:HTML>