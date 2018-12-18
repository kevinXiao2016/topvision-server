<%@page import="net.sf.json.JSONArray"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@page import="com.topvision.ems.epon.onu.domain.UniPort"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
	<%@include file="/include/ZetaUtil.inc"%>
	<Zeta:Loader>
	    library ext
	    library jquery
	    library zeta
	    module onu
	    CSS css/white/disabledStyle
	    IMPORT js.zetaframework.component.NetworkNodeSelector static
	    IMPORT js/nm3k/Nm3kTools 
	    IMPORT epon/onu/applyToOtherOnu
	</Zeta:Loader>
	<style type="text/css">
	.leftFloatUl input{ position:relative; top:3px;}
	</style>
	<script type="text/javascript">
	    var pageSize = <%= uc.getPageSize() %>;
	    var partitionData = {
	    		receiveMin:  '',
	    		receiveMax:  '',
	    	    transmitMin: '',
	    	    transmitMax: '',
	    	    ponRecvMin:  '',
	    	    ponRecvMax:  ''
	    };
		var grid,
		    cm,
		    sm,
		    store;
		
		
	</script>
	</head>
    <body class="whiteToBlack">
    	<div id="putNorth">
			<table class="queryTable">
				<tr>
					<td class="rightBlueTxt">@COMMON.alias@:</td>
					<td class="pR10"><input type="text" class="normalInput"
						style="width: 150px" id="nameInput" maxlength="63" /></td>
					<td class="rightBlueTxt w50">MAC:</td>
					<td class="pR10"><input type="text" class="normalInput w150"
						id="macInput" /></td>
					<td class="rightBlueTxt w50">@ONU.type@:</td>
					<td class="pR10"><input type="text" style="width: 150px"
						id="typeSelect" /></td>
					<td colspan="1"></td>
					<td valign="middle" rowspan="3" >
						<ul class="leftFloatUl">
							<li>
								<a href="javascript:;" class="normalBtn"style="margin-right: 5px;" onclick="onSeachClick()"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a>
							</li>
						</ul>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">OLT:</td>
					<td class="pR10">
						<div style="width: 152px" id="oltContainer"></div>
					</td>
					<td class="rightBlueTxt">@COMMON.slot@:</td>
					<td class="pR10"><input type="text" class="" style="width: 150px" id="slotSelect" /></td>
					<td class="rightBlueTxt">@COMMON.pon@:</td>
					<td class="pR10"><input type="text" class="" style="width: 164px" id="ponSelect" /></td>
					<td class="rightBlueTxt pR10">@COMMON.status@:<select type="text" class="normalSel"  id="statusSelect">
						<option value="-1">@COMMON.all@</option>
						<option value="1">@COMMON.online@</option>
						<option value="2">@COMMON.offline@</option>
					</select></td>
				</tr>
			</table>
		</div>
			
		<div id="putSouth" class="edge10 pT0">
			<div id="putConfig" class="pB10"></div>
			<ul class="leftFloatUl">
				<li>
					<label class="pR20 blueTxt"><input type="radio" name="applyRadio" checked="checked" /><span class="pL2">@ONU.applyToNoUni@</span></label>
				</li>
				<li>
					<label class="blueTxt"><input type="radio" name="applyRadio" /><span class="pL2">@ONU.applyToAllUni@</span></label>
				</li>
			</ul>
			<div class="noWidthCenterOuter clearBoth">
			     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
			         <li><a href="javascript:;" class="normalBtnBig"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
			     </ol>
			</div>
		</div>
	</body>
</Zeta:HTML> 


