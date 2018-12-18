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
    css css.white.disabledStyle
    import performance/js/jquery-ui-1.10.3.custom.min
    import cmc/js/cmIndexPartition
    import js/customColumnModel
    import cm.util.cmUtil
    import cm.controller.cmList
    import js.zetaframework.component.NetworkNodeSelector static
    import js.json2
</Zeta:Loader>
<script src="../js/jquery/nm3kToolTip.js"></script>
<script src="../js/placeHolderHack.js"></script>
<script type="text/javascript" src="/js/ext/ux/RowExpander.js"></script>
<link rel="stylesheet" href="/js/ext/ux/RowExpander.css" />

<style type="text/css">
body,html{height:100%;overflow:hidden;}
#query-container{/* height: 155px; */overflow:hidden;}
#advance-toolbar-div{padding:10px 0 0 0px;}
#simple-toolbar-div{padding:20px 0 0 5px;}
#queryContent{width: 330px;padding-left: 5px;}
.queryTable a{color:#B3711A;}
#grid-div{position: relative;}
#cm-num-div{position: absolute;right: 0;top:0;z-index: 9;height: 16px;padding-top: 7px;padding-bottom: 5px;vertical-align: middle;}
#cm-num-div span{padding-right: 10px;vertical-align: middle;}
#cm-num-div label{vertical-align: middle;}
#cmListSidePart{ width:380px; padding:0px 10px; height:100%; overflow:auto; background: #F4F4F4; position:absolute; top:0; right:-400px; border-left:1px solid #9a9a9a; z-index:999; opacity:0.9; filter:alpha(opacity=90);}
.positionLi{ padding-top:3px; overflow:hidden;}
.cmListLeftFloatDl{ float:left;}
.cmListLeftFloatDl dt,.cmListLeftFloatDl dd{ float:left;}
.cmListLeftFloatDl dt{ width:60px; padding-top:6px;}
.cmListLeftFloatDl dd{ margin-right:3px; padding-top:8px;}
.cmListLeftFloatDl dd.eqName{padding-top:5px; width:245px; overflow:hidden; line-height:2em;}
.cmListBody{ padding:10px; border-left:1px solid #cacaca; border-right:1px solid #cacaca; background:#fff; position:relative;}
.bottomRight{ position:absolute; bottom:2px; right:5px;}
.cmListArr{float:left;}
.cmListArr li{ float:left;}
.cmListW155{ width:155px; overflow:hidden; padding-top:5px;}
.cmListW155 p{ padding:2px;}
.cmListW155 span{ padding:2px;}
.redBg{ background:#f00; color:#fff;}
.orangeBg{ background: #f90; color:#000;}
.sideTable td{ height: 20px;}
#cmListSideArrow{ width:19px; height:140px; overflow:hidden; position: absolute; right:0px; top:0px; z-index:1000; cursor:pointer;}
#arrow{ position:absolute; top:20px; left:8px; width:4px; height:8px; overflow: hidden; }
.yellow-div{height:16px;width:16px;background-color: #DCD345;}
.green-div{height:16px;width:16px;background-color: #ffffff;}
.red-div{height:16px;width:16px;background-color: #C07877;}
.normalTable .yellow-row{background-color:#DCD345 !important;}
.normalTable .red-row{background-color:#C07877 !important;}
.normalTable .green-row{background-color:#26B064 !important;}
.normalTable .white-row{background-color:#FFFFFF !important;}
.tip-dl{background: none repeat scroll 0 0 #F7F7F7;border: 1px solid #C2C2C2;color: #333333;padding: 2px 10px;position: absolute;z-index:2;}
.tip-dl dd {float: left;line-height: 1.5em;}

.thetips dd{ float:left;}
.thetips dl{border:1px solid #ccc; float:left; background:#E1E1E1; }
#color-tips {left:5px;top:67px;}
#operation-tips{left:311px;top:67px;}
#suc-num-dd{color: #26B064;}
#fail-num-dd{color: #C07877;}
#loading {
	padding: 5px 8px 5px 26px;
	border: 1px solid #069;
	background: #F1E087 url('../images/refreshing2.gif') no-repeat 2px center;
	position: absolute;
	z-index: 999;
	top: 0px;
	left: 0px;
	display: none;
	font-weight: bold;
}
#tip-div{ position:absolute; top:128px; right:18px;}
.mainChl{
	background-color: yellow;
}
.subDashedLine{ border-bottom:1px dashed #ccc; padding-bottom:8px; padding-top:8px;}
#advance-toolbar-div table{ line-height:1em;}
</style>

<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var cpeSwitch =  ${cpeSwitch};
var queryInitData = ${queryInitData};
var cmCollectMode = ${cmCollectMode};
var cmPingMode = ${cmPingMode};
var entityId = '${entityId}';
var language = '<%=uc.getUser().getLanguage()%>';
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower = <%=uc.hasPower("refreshDevice")%>;
var hasSupportEpon = <%= uc.hasSupportModule("olt") %>;
var entityTypes = ${entityTypes};
var isSimpleSearch = '${simpleModeFlag}' ? ${simpleModeFlag} : true;
var sysElecUnit = '@{unitConfigConstant.elecLevelUnit}@';
</script>
</head>
<body class="bodyGrayBg">
	<!-- 查询DIV -->
	<div id="query-container">
		<div id="simple-toolbar-div">
			<table class="queryTable">
				<tr>
					<td><textarea class="normalInput" id="queryContent" placeHolder="@ccm/CCMTS.CmList.fuzzyMatching@" maxlength="2000" style="width:600px;height: 100px;" ></textarea></td>
					<td><a id="simple-query" href="javascript:simpleQuery();" class="normalBtn" onclick=""><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a></td>
					<td><a href="#" id="advance-query-link" onclick="showAdvanceQuery()">@ccm/CCMTS.CmList.advancedSearch@</a></td>
				</tr>
			</table>
		</div>
		<div id="advance-toolbar-div" style="display: none;">
			<table class="queryTable">
				<tr>
					<td class="rightBlueTxt w100">@CCMTS.entityStyle@@COMMON.maohao@</td>
					<td>
						<select class="normalSel w150" id="select_deviceType" onchange="deviceTypeSelChanged()">
							<option value="0">@CMC.select.select@</option>
                            <% if(uc.hasSupportModule("olt")){%>
                            <option value="10000">OLT</option>
                            <% }%>
                            <% if(uc.hasSupportModule("cmts")){%>
							<option value="40000">CMTS</option>
                            <% }%> 
						</select>
					</td>
					<td width="70" class="rightBlueTxt" id="td_olt">OLT@COMMON.maohao@</td>
					<td width="90">
						<div id="oltContainer" class="w120"></div>
					</td>
					<td class="rightBlueTxt w70" id="td_pon">PON@COMMON.maohao@</td>
					<td>
						<select class="normalSel w120" id="select_pon" onchange="ponSelChanged()">
							<option value="0">@CMC.select.select@</option>
						</select>
					</td>
					<td class="rightBlueTxt " id="td_ccmts" width="80">CMTS@COMMON.maohao@</td>
					<td>
						<select class="normalSel w100" id="select_ccmts" onchange="ccmtsSelChanged()">
							<option value="0">@CMC.select.select@</option>
						</select>
					</td>
					<td class="rightBlueTxt" style="display: none;" id="td_cmts" width="80">CMTS@COMMON.maohao@</td>
					<td style="display: none;">
						<select class="normalSel" id="select_cmts" onchange="cmtsSelChanged()" style="width: 100px;">
							<option value="0">@CMC.select.select@</option>
						</select>
					</td>
					<td class="rightBlueTxt w80" id="td_upCnl">@CCMTS.upStream@@COMMON.maohao@</td>
					<td class="w70">
						<select class="normalSel w100" id="select_upCnl">
							<option value="0">@CMC.select.select@</option>
						</select>
					</td>
					<td class="rightBlueTxt w80" id="td_downCnl">@CCMTS.downStream@@COMMON.maohao@</td>
					<td>
						<select class="normalSel w90" id="select_downCnl">
							<option value="0">@CMC.select.select@</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@CCMTS.entityStatus@@COMMON.maohao@</td>
					<td>
						<select class="normalSel w150" id="select_status">
							<option value="0">@CMC.select.select@</option>
							<option value="1">offline</option>
							<option value="6">online</option>
							<option value="21">online(d)</option>
							<option value="26">p-online</option>
							<option value="30">p-online(d)</option>
							<option value="27">w-online</option>
							<option value="31">w-online(d)</option>
							<option value="10000">registering</option>
						</select>
					</td>
					<td class="rightBlueTxt" width="70">IP@COMMON.maohao@</td>
					<td><input id="cmIpAddress" class="normalInput w100"/></td>
					<td class="rightBlueTxt" width="70">MAC@COMMON.maohao@</td>
					<td><input id="cmMacAddress" class="normalInput w120"/></td>
					<td class="rightBlueTxt" width="70">@CM.userNo@@COMMON.maohao@</td>
					<td><input id="userId" class="normalInput w100" maxlength="25"/></td>
					<td class="rightBlueTxt" width="90">@CM.docsicVersion@@COMMON.maohao@</td>
					<td>
						<select class="normalSel w100" id="select_docsisMode">
							<option value="0">@CMC.select.select@</option>
							<option value="1">1.0</option>
							<option value="2">1.1</option>
							<option value="3">2.0</option>
							<option value="4">3.0</option>
						</select>
					</td>
					<td class="rightBlueTxt" width="70">@CM.ServiceType@@COMMON.maohao@</td>
					<td><input id="cmServiceType" class="normalInput" style="width:88px;" maxlength="128"/></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@CM.userName@@COMMON.maohao@</td>
					<td><input id="userName" class="normalInput w150" maxlength="25"/></td>
					<td class="rightBlueTxt">@CM.userAddr@@COMMON.maohao@</td>
					<td><input id="userAddr" class="normalInput w100" maxlength="50"/></td>
					<td class="rightBlueTxt">@CM.userPhone@@COMMON.maohao@</td>
					<td><input id="userPhoneNo" class="normalInput w120" maxlength="25"/></td>
					<td class="rightBlueTxt">@CM.packageType@@COMMON.maohao@</td>
					<td><input id="offerName" class="normalInput w100" maxlength="25"/></td>
					<td class="rightBlueTxt" width="70">@CM.configFile@@COMMON.maohao@</td>
					<td><input id="configFile" class="normalInput w100" maxlength="50"/></td>
					<td class="w80">
						
					</td>
					<td class="w80">
						
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@CM.partition@@COMMON.maohao@</td>
	    			<td colspan="9"><div id="partition"></div></td>
				</tr>
				<tr>
					<td></td>
					<td colspan="11">
						<ul class="leftFloatUl">
							<li>
								<a id="advance-query" href="javascript:queryCmList();" class="normalBtn"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a>
							</li>
							<li class="pT5">
								<a id="simple-query-link" href="javascript:showSimpleQuery();" class="mL5">@ccm/CCMTS.CmList.quickSearch@</a>	
							</li>
						</ul>
					</td>
				</tr>
			</table>
		</div>
	</div>
	
	<div id="tip-div" class="thetips">
		<dl id="color-tips" style="margin-right:5px; padding:3px;">
			<dd class="mR2 yellow-div"></dd>
			<dd class="mR10">@ccm/CCMTS.CmList.processing@</dd>
			<dd class="mR2 green-div"></dd>
			<dd class="mR10">@CMC.tip.successfully@ (<b id="suc-num-dd">0</b>)</dd>
			<dd class="mR2 red-div"></dd>
			<dd class="mR5">@CMC.label.failure@ (<b id="fail-num-dd">0</b>)</dd>
		</dl>
	</div>
	
	<div id="cmListSidePart">
		<ul class="rightFloatUl pT10" style="width:100%;">
			<li style="float:left;" class="positionLi"><b class="blueTxt" id="folderName-b"></b></li>
			<%-- <li><a href="javascript:;" class="normalBtn" onclick="refreshCmLocation()"><span><i class="miniIcoRefresh"></i>@route.button.refresh@</span></a></li> --%>
		</ul>
		<div class="cmListBroad olt-sidebar">
			<div class="cmListTitL">
				<div class="cmListTitR">
					<div class="cmListTitC">
						<dl class="cmListLeftFloatDl">
							<dt><b class="grayConnerL"><span class="grayConnerR">OLT</span></b></dt>
							<dd class="eqName" id="oltLocation-name"></dd>
							<dd id="oltLocation-state"></dd>
							<dd><div class="" id="oltLocation-maxAlarmLevel"></div></dd>
						</dl>
					</div>
				</div>
			</div>
			<div class="cmListBody">
				@oltPon.outflow@：<span id="oltLocation-ponOutSpeedForunit" class="perf"></span>
				<div class="bottomRight">
					<a href="javascript:loadCmListByOltJson();" class="normalBtn"><span>@ccm/CCMTS.CmList.checkCm@</span></a>
				</div>
			</div>
			<div class="cmListFootL">
				<div class="cmListFootR">
					<div class="cmListFootC"></div>
				</div>
			</div>
			<ol class="cmListArr">
				<li class="cmListW155">
					<p class="txtRight">@ccm/CCMTS.CmList.receive@<span id="oltCcmtsRela-ponInPowerForunit">--</span></p>
					<p class="txtRight">@ccm/CCMTS.CmList.attenuation@<span id="oltCcmtsRela-upAttenuation">--</span></p>
					<p class="txtRight">@ccm/CCMTS.CmList.send@<span id="oltCcmtsRela-onuPonOutPowerForunit">--</span></p>
				</li>
				<li class="cmListMiddleArr"></li>
				<li class="cmListW155">
					<p>@ccm/CCMTS.CmList.send@<span id="oltCcmtsRela-ponOutPowerForunit">--</span></p>
					<p>@ccm/CCMTS.CmList.attenuation@<span id="oltCcmtsRela-downAttenuation">--</span></p>
					<p>@ccm/CCMTS.CmList.receive@<span id="oltCcmtsRela-onuPonInPowerForunit">--</span></p>
				</li>
			</ol>
		</div>
		<div class="cmListBroad">
			<div class="cmListTitL">
				<div class="cmListTitR">
					<div class="cmListTitC">
						<dl class="cmListLeftFloatDl">
							<dt><b class="grayConnerL"><span class="grayConnerR" id="ccTitle">CMTS</span></b></dt>
							<dd class="eqName" id="ccmtsLocation-name"></dd>
							<dd id="ccmtsLocation-state"></dd>
							<dd><div class="" id="ccmtsLocation-maxAlarmLevel"></div></dd>
						</dl>
					</div>
				</div>
			</div>
			<div class="cmListBody">
				<table cellpadding="0" cellspacing="0" width="100%" border="0" rules="none" class="sideTable">
					<tr>
						<td width="50%">SNR@COMMON.maohao@<span id="ccmtsLocation-upChannelSnrForunit" class="perf"></span></td>
						<td>@CMCPE.inFlow@@COMMON.maohao@<span id="ccmtsLocation-upChannelInSpeedForunit" class="perf"></span></td>
					</tr>
					<tr>
						<td colspan="2">@ccm/CCMTS.CmList.cmNum@<span id="cmc-cm-num"></span></td>
					</tr>
				</table>
				<div class="bottomRight">
					<ul class="leftFloatUl">
						<li id="spectrum-li">
							<a href="javascript:;" class="normalBtn" onclick="showCcmtsSpectrum()"><span>@ccm/CCMTS.CmList.viewSpectrum@</span></a>
						</li>
						<li>
							<a href="javascript:loadCmListByCmcJson();" class="normalBtn"><span>@ccm/CCMTS.CmList.checkCm@</span></a>
						</li>
					</ul>
				</div>
			</div>
			<div class="cmListFootL">
				<div class="cmListFootR">
					<div class="cmListFootC"></div>
				</div>
			</div>
			<ol class="cmListArr">
				<li class="cmListW155">
					<p class="txtRight">@ccm/CCMTS.CmList.receive@<span id="ccmtsCmRela-ccmtsInPowerForunit">--</span></p>
					<p class="txtRight">@ccm/CCMTS.CmList.attenuation@<span id="ccmtsCmRela-upAttenuation">--</span></p>
					<p class="txtRight">@ccm/CCMTS.CmList.send@<span id="ccmtsCmRela-cmOutPowerForunit">--</span></p>
				</li>
				<li class="cmListMiddleArr"></li>
				<li class="cmListW155">
					<p>@ccm/CCMTS.CmList.send@<span id="ccmtsCmRela-ccmtsOutPowerForunit">--</span></p>
					<p>@ccm/CCMTS.CmList.attenuation@<span id="ccmtsCmRela-downAttenuation">--</span></p>
					<p>@ccm/CCMTS.CmList.receive@<span id="ccmtsCmRela-cmInPowerForunit">--</span></p>
				</li>
			</ol>
		</div>
		<div class="cmListBroad">
			<div class="cmListTitL">
				<div class="cmListTitR">
					<div class="cmListTitC">
						<dl class="cmListLeftFloatDl">
							<dt><b class="grayConnerL"><span class="grayConnerR">CM</span></b></dt>
							<dd class="eqName" id="cmName-dd"></dd>
							<dd id="cmLocation-state"></dd>
							<dd><div class="" id="cmLocation-maxAlarmLevel"></div></dd>
						</dl>
					</div>
				</div>
			</div>
			<div class="cmListBody">
				<table cellpadding="0" cellspacing="0" width="100%" border="0" rules="none" class="sideTable">
					<tr id="frequency_tr" style="display:none">
						<td width="50%">@CM.upFrequency@@COMMON.maohao@<span id="cmLocation-upChannelFrequencyForunit" class="perf"></span></td>
						<td>@CM.downFrequency@@COMMON.maohao@<span id="cmLocation-downChannelFrequencyForunit" class="perf"></span></td>
					</tr>
					<tr>
						<td width="50%">@CM.upSnr@@COMMON.maohao@<span id="cmLocation-upChannelSnrForunit" class="perf"></span></td>
						<td>@CM.downSnr@@COMMON.maohao@<span id="cmLocation-downChannelSnrForunit" class="perf"></span></td>
					</tr>
					<tr>
						<td width="50%">@CM.upSendPower@@COMMON.maohao@<span id="cmLocation-upChannelTxForunit" class="perf"></span></td>
						<td>@CM.downReceivePower@@COMMON.maohao@<span id="cmLocation-downChannelTxForunit" class="perf"></span></td>
					</tr>
					<tr>
						<td colspan="2">@CMCPE.CPENUM@@COMMON.maohao@<span id="cpe-num"></span></td>
					</tr>
				</table>
				<div class="bottomRight">
					<ul class="leftFloatUl">
						<li>
							<a href="javascript:;" id="refreshCmInfo" class="normalBtn" onclick="refreshCmInfo()"><span>@route.button.refresh@</span></a>
						</li>
						<li>
							<a href="javascript:;" id="restartCm" class="normalBtn" onclick="restarCm()"><span>@CCMTS.reset@</span></a>
						</li>
						<li>
							<a href="javascript:;" class="normalBtn" onclick="showCmDetail()"><span>@CM.cmMessage@</span></a>
						</li>
					</ul>
				</div>
			</div>
			<div class="cmListFootL">
				<div class="cmListFootR">
					<div class="cmListFootC"></div>
				</div>
			</div>
		</div>
	</div>
	<div class="cmListSideEnglish" id="cmListSideArrow">
		<div class="cmListSideArrLeft" id="arrow"></div>
	</div>
	
	<div id="loading">@ccm/CCMTS.CmList.refreshingSignal@</div>
	
	<div id="cm3Tip" style="position: absolute;"></div>
	
</body>
</Zeta:HTML>