<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
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
    css cmc/cmcRealtimeInfo
    css epon/css/oltRealtimeInfo
    import js/jquery/jquery.wresize
    import epon.javascript.SlotConstant
    import epon.vlan.oltVlanUtil
    import epon.oltrealtime.oltRealtimeInfo
    import js/nm3k/rangeColor
    css css/white/disabledStyle
</Zeta:Loader>

<script type="text/javascript">
	var entityId = ${entityId};
	var entityName = '${entityName}';
	var entityIp = '${entityIp}';
	var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
	var slotTypeMapping = ${slotTypeMapping};
	
	 //加载分区间信息
	 function queryRange(){
		 $.ajax({
			 url:"/epon/oltRealtime/loadOpticalStatstics.tv",data:{entityId: entityId},
			 dataType:'json',cache:false,
			 success:function(json){
				 var $rx = json.rx;
				 var $tx = json.tx;
				 $rx.renderTo = "putRangeColor";
				 $tx.renderTo =  "putRangeColor2";
				 addRangeColor($rx)
				 addRangeColor($tx)
			 },error:function(){
				 
			 }
		 });
	 }
	
	$(function(){
		queryRange();
	})
	
</script>
</head>
	<body class="whiteToBlack">
		<div id="topToolBar"></div>
		<div class="edge10">
			<p class="flagP">
				<span class="flagInfo">
					<label class="flagOpen" id="tb1_tit">@REALTIME.summaryInfo@</label>
				</span>
			</p>
			<table class="dataTableAll" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows" id="tb1" >
				<thead>
					<tr>
						<th colspan="4">@REALTIME.baseinfo@</th>
						<th>@REALTIME.opticalStas@</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td width="16.6%" class="rightBlueTxt">@EPON.entityDeviceName@:</td>
						<td width="16.6%"><span id="deviceName">@entitySnapPage.loading@</span></td>
						<td width="16.6%" class="rightBlueTxt">@REALTIME.upTime@:</td>
						<td width="16.6%"><span id="upTime">@entitySnapPage.loading@</span></td>
						<td rowspan="5">
							<div id="putRangeColor"><p class="txtCenter">@entitySnapPage.loading@</p></div>
							<div id="putRangeColor2"><p class="txtCenter">@entitySnapPage.loading@</p></div>
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@EPON.softwareVersion@:</td>
						<td><span id="softVersion">@entitySnapPage.loading@</span></td>
						<td></td>
						<td></td>
					</tr>
					<tr>	
						<td class="rightBlueTxt">@REALTIME.sniTotal@:</td>
						<td><span id="sniTotal">@entitySnapPage.loading@</span></td>
						<td class="rightBlueTxt">@REALTIME.ponTotal@:</td>
						<td id="ponTd">@entitySnapPage.loading@</td>
					</tr>
					<tr>	
						<td class="rightBlueTxt">@REALTIME.subTotal@:</td>
						<td id="subTd">@entitySnapPage.loading@</td>
						<% if(uc.hasSupportModule("cmc")){%>
							<td class="rightBlueTxt"><span vcid="ccmts">@REALTIME.cmTotal@:</span></td>
							<td><span id="cmTotal" vcid="ccmts">@entitySnapPage.loading@</span></td>
						<% }else{ %>
							<td></td>
							<td></td>
						<% } %>
					</tr>
					<tr>
						<td colspan="4"></td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<div id="putBoardGrid"></div>
		<div id="putPortGrid"></div>
	</body>
</Zeta:HTML>
