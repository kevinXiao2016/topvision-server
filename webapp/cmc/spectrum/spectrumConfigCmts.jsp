<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<title></title>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library Jquery
    library Ext
    library Zeta
	module spectrum
	import cmc.spectrum.spectrumConfigCmts
</Zeta:Loader>
<style type="text/css">
body{
	min-height: 500px;
	min-width: 900px;
}
.td_1{
	width: 125px;
}
.td_2{
	width: 140px;
}
.td_3{
	width: 125px;
}
.td_4{
	width: 160px;
}
.td_5{
	width: 125px;
}
.td_6{
	width: 125px;
}
.td_7{
	width: 125px;
}
.switch{
	cursor: pointer;
}
</style>
<script type="text/javascript">
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var hisCollectCycle = ${hisCollectCycle}; // 历史采集周期
var hisCollectDuration = ${hisCollectDuration};  // 历史采集时长
var hisTimeInterval = ${hisTimeInterval}; // 历史采集步长
var timeInterval = ${timeInterval}; // 实时采集步长
var timeLimit = ${timeLimit}; // 实时观看时限
var pageSize = <%= uc.getPageSize() %>;
var cmcTypes = ${cmcTypes};//加载设备类型下拉框用
var grid = null;
var store = null;
var eponSupport = <%= uc.hasSupportModule("olt")%>;
</script>

</head>
<body class="whiteToBlack">
	<div class=formtip id=tips style="display: none"></div>
	
	<div class="edge10">
		<div id="putBtnGroup" class="pB10 floatLeft" style="width:100%;"></div>
		
		<!-- 历史频谱采集配置 -->
		<div class="jsTabPart  clearBoth">
			<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
				<thead>
					<tr>
						<th colspan="7" class="txtLeftTh">@spectrum.hisCollectConfig@</th>
					</tr>
				</thead>
			 	<tbody>
					<tr>
						<td class="rightBlueTxt td_1">@spectrum.collectPeriod@@COMMON.maohao@</td>
					 	<td class="td_2">
					 		<input id="hisCollectCycle" type="text" class="normalInput w100" tooltip='@spectrum.pleaseInput124@'/>
					 		@spectrum.hour@
					 	</td>
					 	<td class="rightBlueTxt td_3" width="140">@spectrum.collectTimeLength@@COMMON.maohao@</td>
					 	<td class="td_4">
					 		<input id="hisCollectDuration" type="text" class="normalInput w100" tooltip='@spectrum.pleaseInput160@'/>
					 		@spectrum.minute@
					 	</td>
					  	<td class="rightBlueTxt td_5" width="140">@spectrum.collectInterval@@COMMON.maohao@</td>
						<td class="td_6">
					 		<select id="hisTimeInterval" class="normalSel w100">
					 			<option value="1">1@spectrum.second@</option>
                       			<option value="5">5@spectrum.second@</option>
                       			<option value="10">10@spectrum.second@</option>
                       			<option value="60">1@spectrum.minute@</option>
                       			<option value="300">5@spectrum.minute@</option>
                       			<option value="900">15@spectrum.minute@</option>
                   		 	</select>
                   		</td>
					 	<td>
							<a onclick="saveHisCollectConfig()" href="javascript:;" class="normalBtn" style="margin-right:2px;">
			        			<span><i class="miniIcoData"></i>@COMMON.save@</span>
			        		</a>
			        		<a onclick="resetHisCollectConfig()" href="javascript:;" class="normalBtn">
			        			<span><i class="miniIcoBack"></i>@COMMON.reset@</span>
			        		</a>
					 	</td>	
					</tr>
				</tbody>
			</table>
			<div id='dataPolicyConfig' class="pB10"></div>
		</div>
		
		<!-- 实时频谱采集配置 -->
		<div class="jsTabPart clearBoth">
			<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
				<thead>
					<tr>
						<th colspan="7" class="txtLeftTh">@spectrum.realTimeCollectConfig@</th>
					</tr>
				</thead>
			 	<tbody>
					<tr>
						<td class="rightBlueTxt td_1">@spectrum.collectInterval@@COMMON.maohao@</td>
						<td class="td_2">
					 		<select id="timeInterval" class="normalSel w100">
					 		    <option value="300">300@spectrum.msec@</option>
                                <option value="600">600@spectrum.msec@</option>
					 			<option value="1000">1@spectrum.second@</option>
                       			<option value="2000">2@spectrum.second@</option>
                       			<option value="3000">3@spectrum.second@</option>
                       			<option value="5000">5@spectrum.second@</option>
                       			<option value="10000">10@spectrum.second@</option>
                       			<option value="60000">1@spectrum.minute@</option>
                       			<option value="300000">5@spectrum.minute@</option>
                       			<option value="900000">15@spectrum.minute@</option>
                   		 	</select>
                   		</td>
					 	<td class="rightBlueTxt td_3">@spectrum.viewTimeLimit@@COMMON.maohao@</td>
					 	<td class="td_4">
					 		<input id="timeLimit" type="text" class="normalInput w100" tooltip='@spectrum.pleaseInput1560@'/>
					 		@spectrum.minute@
					 	</td> 
					 	<td class="td_5"></td>
					 	<td class="td_6"></td>
						<td align="right">
							<a onclick="saveGlobalCollectConfig()" href="javascript:;" class="normalBtn" style="margin-right:2px;">
			        			<span><i class="miniIcoData"></i>@spectrum.save@</span>
			        		</a>
			        		<a onclick="resetGlobalCollectConfig()" href="javascript:;" class="normalBtn">
			        			<span><i class="miniIcoBack"></i>@COMMON.reset@</span>
			        		</a>
						</td>
				
					</tr>
				</tbody>
			</table>
			<div class="pB10"></div>
		</div>
		
		<!-- CC频谱采集配置 -->
		<div class="jsTabPart clearBoth">
			<table id="showLast" class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
				<thead>
					<tr>
						<th colspan="7" class="txtLeftTh">@spectrum.ccCollectConfig@</th>
					</tr>
				</thead>
				<tbody>	
					<tr>
						<td class="rightBlueTxt td_1">@spectrum.deviceType@@COMMON.maohao@</td>
						<td class="td_2"> 		
							<select class="normalSel w100" id="typeId" onchange="deviceTypeSelChanged()">
								<option value="-1">@COMMON.select@</option>
							</select>
	                   	</td>  
						<td class="rightBlueTxt td_3">@COMMON.alias@@COMMON.maohao@</td>
						<td class="td_4"><input id="cmtsName" class="normalInput w100" type="text"/></td> 
						<td class="rightBlueTxt td_5">@resources/COMMON.uplinkDevice@@COMMON.maohao@</td>
						<td class="td_6" colspan="2"><input id="cmtsIp" type="text" class="normalInput w100"/></td>
					</tr>
					<tr>	
						<td class="rightBlueTxt"><div  id='td_cmts_switch'>@spectrum.ccSwitch@@COMMON.maohao@</div></td>
						<td class="td_4">
							<div>
						 		<select id="cmtsCollectSwitch" class="normalSel w100">
	                       			<option value="-1">@COMMON.select@</option>
	                       			<option value="1">@COMMON.open@</option>
	                       			<option value="0">@COMMON.close@</option>
	                   		 	</select>
	                   		</div>
	                  	</td>
						<td class="rightBlueTxt"><div  id='td_his_switch'>@spectrum.hisSwitch@@COMMON.maohao@</div></td>
						<td class="td_6"> 
							<div>
						 		<select id="hisVideoSwitch" class="normalSel w100">
	                       			<option value="-1">@COMMON.select@</option>
	                       			<option value="1">@COMMON.open@</option>
	                       			<option value="0">@COMMON.close@</option>
	                   		 	</select>
	                   		</div>
	                  	</td>
	                  	<%if(uc.hasSupportModule("olt")){ %>
						<td class="rightBlueTxt" >
							<div id='td_olt_switch' style="display:none;">@spectrum.oltSwitch@@COMMON.maohao@</div>
						</td>
						<td class="td_2">
					 		<select id="oltCollectSwitch" class="normalSel w100" disabled="disabled"  style="display:none;">
	                   			<option value="-1">@COMMON.select@</option>
	                   			<option value="1">@COMMON.open@</option>
	                   			<option value="0">@COMMON.close@</option>
	               		 	</select>
	                  	</td>
	                  	<%}%>
	                  	<%if(!uc.hasSupportModule("olt")){ %>
	                  	<td class="" /> 
	                  	<%}%>
	                  	<td>
							<a id="queryButton"  onclick="queryCmtsSpectrumConfig()" href="javascript:;" class="normalBtn">
				         		<span><i class="miniIcoSearch"></i>@COMMON.query@</span>
				         	</a>
						</td>
					</tr>
				</tbody>
			</table>
			<div id="grid" class="pT10"></div>
		</div>
	</div>
	
	<script type="text/javascript" src="../js/jquery/Nm3kTabBtn.js"></script>
	<script type="text/javascript">
	$(function(){
		var tab1 = new Nm3kTabBtn({
		    renderTo:"putBtnGroup",
		    callBack:"msg",
		    tabs:["@spectrum.all@",
		          "@spectrum.hisCollectConfig@",
		          "@spectrum.realTimeCollectConfig@",
		          "@spectrum.ccCollectConfig@"]
		});
		tab1.init();
	});
	
	function msg(index){
	    var h = $(window).height();
	    var h2;
	    switch(index){
	    	case 0:
	    		$(".jsTabPart").css("display","block");
	    		var _height = $(window).height() - $('#grid').offset().top -20;
	    	    grid.setHeight(_height);
	    		break;
	    	default:
	    		$(".jsTabPart").css("display","none");
	    		$(".jsTabPart").eq(index-1).fadeIn();
	    		if(index===3){
		    		var _height = $(window).height() - $('#grid').offset().top -20;
		    		grid.setHeight(_height);
	    		}
	    		break;		
	    }	
	}
	</script>
</body>
</Zeta:HTML>