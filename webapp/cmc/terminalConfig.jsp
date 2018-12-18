<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    library FileUpload
    module cmc
    IMPORT js/jquery/nm3kPassword
    plugin DropdownTree
    plugin PasswordField
    CSS css/white/disabledStyle
    import cmc/terminalConfig
    IMPORT js/components/segmentButton/SegmentButton
    IMPORT js/jQueryFileUpload/ajaxfileupload
</Zeta:Loader>
<style type="text/css">
	.x-btn button{color:#676767;}
	.noWidthCenterOuter{ width:100%;}
	#putSegmentButton{ height:29px; display:inline-block; position:relative; left:50%;}
	#chooseFileFileUpload{ top:0 !important; left:0 !important;}
</style>
<script type="text/javascript">
var cmPollManager =  <%= uc.hasPower("cmPollConfig") %> || false;
var pass1;
var pass2;
function isDigit(s) 
{ 
var patrn=/^[0-9]{1,20}$/; 
if (!patrn.exec(s)) return false 
return true 
}
var dataPolicyForm = null;
var terminalForm = null;
var cmCollectConfig = ${cmCollectConfigJson};
var cpeCollectConfig = ${cpeCollectConfigJson};
var cmCpeCollectDataPolicy = ${cmCpeCollectDataPolicyJson};
var cmPollInterval = '${cmPollInterval}';
var cmPollStatus = '${cmPollStatus}';
var readCommunity = '${readCommunity}';
var writeCommunity = '${writeCommunity}';
function initData(){
	if(cmCollectConfig.cmCollectStatus == 0){
		$("#cmCollectStatus").attr("checked",true);
	}else{
		$("#cmTypeStatisticStatus").attr("disabled",true);
		$("#cmActionStatisticStatus").attr("disabled",true);
		$("#cpeCollectStatus").attr("disabled",true);
		//$("#statisticSource").attr("disabled",true);
	}
	if(cmCollectConfig.cmTypeStatisticStatus == 0){
		$("#cmTypeStatisticStatus").attr("checked",true);
	}
	if(cmCollectConfig.cmActionStatisticStatus == 0){
		$("#cmActionStatisticStatus").attr("checked",true);
	}
	$("#cmCollectPeriod").val(cmCollectConfig.cmCollectPeriod/(60*1000));
	
	if(cpeCollectConfig.cpeCollectStatus == 0){
		$("#cpeCollectStatus").attr("checked",true);
	}else{
		$("#cpeNumStatisticStatus").attr("disabled",true);
		$("#cpeActionStatisticStatus").attr("disabled",true);
	}
	if(cpeCollectConfig.cpeNumStatisticStatus == 0){
		$("#cpeNumStatisticStatus").attr("checked",true);
	}
	if(cpeCollectConfig.cpeActionStatisticStatus == 0){
		$("#cpeActionStatisticStatus").attr("checked",true);
	}
	
	<%if(uc.hasPower("cmPollConfig")){ %>
	if(cmPollStatus == 1){
		//$("#cmPoll").attr("checked",true);
	}else{
		//$("#cmPollInterval").attr("disabled",true);
	}
	$("#cmPollInterval").val(cmPollInterval / 60);
	<%}%>
	<%if(uc.hasSupportModule("onu")){ %>
		if('${onuCpeStatus}' == "1"){
			$("#onuCpeStatus").attr("checked",true);
		}else{
			$("#onuCpeStatus").attr("checked",false);
		}
		$("#onuCpeInterval").val(${onuCpeInterval} / 60000);
	<%} %>
	
	$("#cmPollInterval").val(cmPollInterval / 60);
	$("#initData").val(cmCpeCollectDataPolicy.initDataSavePolicy/(24*60*60*1000));
	$("#statisticData").val(cmCpeCollectDataPolicy.statisticDataSavePolicy/(24*60*60*1000));
	$("#actionData").val(cmCpeCollectDataPolicy.actionDataSavePolicy/(24*60*60*1000));
	$("#cmHistoryData").val(cmCpeCollectDataPolicy.cmHistorySavePolicy/(24*60*60*1000));
	
	//$("#readCommunity").val(readCommunity);
	//$("#writeCommunity").val(writeCommunity);
	pass1.setValue(readCommunity);
	pass2.setValue(writeCommunity);
	//Ext.getCmp('statisticSource').setValue(cmCollectConfig.cmStatisticSource)
}
Ext.onReady(function(){
	
	pass1 = new nm3kPassword({
	    id : "pass1",
	    renderTo : "readCommunity",
	    toolTip : "@onfocus.pleaseInputCommunity@",
	    width : 120,
	    value : "",
	    firstShowPassword : true,
	    disabled : false,
	    maxlength : 32
	})
	pass1.init();
	
	pass2 = new nm3kPassword({
	    id : "pass2",
	    renderTo : "writeCommunity",
	    toolTip : "@onfocus.pleaseInputWriteCommunity@",
	    width : 120,
	    value : "",
	    firstShowPassword : true,
	    disabled : false,
	    maxlength : 32
	})
	pass2.init();
	
	var cmCheck = Ext.get('cmCollectStatus'); 
	cmCheck.on('click',function() {
		if(!$("#cmCollectStatus").attr("checked")){
			//关闭采集
			$("#cmTypeStatisticStatus").attr("checked",false);
			$("#cmActionStatisticStatus").attr("checked",false);
			$("#cmTypeStatisticStatus").attr("disabled",true);
			$("#cmActionStatisticStatus").attr("disabled",true);
			//$("#statisticSource").attr("disabled",true);
			$("#cpeNumStatisticStatus").attr("checked",false);
			$("#cpeActionStatisticStatus").attr("checked",false);
			$("#cpeCollectStatus").attr("checked",false);
			$("#cpeNumStatisticStatus").attr("disabled",true);
			$("#cpeActionStatisticStatus").attr("disabled",true);
			$("#cpeCollectStatus").attr("disabled",true);
			$("#cmCollectPeriod").attr("disabled",true);
		}else{
			//开启采集
            $("#cmTypeStatisticStatus").attr("checked",true);
            $("#cmActionStatisticStatus").attr("checked",true);
			$("#cmTypeStatisticStatus").attr("disabled",false);
			$("#cmActionStatisticStatus").attr("disabled",false);
			//$("#statisticSource").attr("disabled",false);
            $("#cpeNumStatisticStatus").attr("checked",true);
            $("#cpeActionStatisticStatus").attr("checked",true);
            $("#cpeCollectStatus").attr("checked",true);
			$("#cpeNumStatisticStatus").attr("disabled",false);
			$("#cpeActionStatisticStatus").attr("disabled",false);
			$("#cpeCollectStatus").attr("disabled",false);
			$("#cmCollectPeriod").attr("disabled",false);
		}
	}); 
	
	var cpeCheck = Ext.get('cpeCollectStatus'); 
	cpeCheck.on('click',function() { 
		if(!$("#cpeCollectStatus").attr("checked")){
            //关闭采集
            $("#cpeNumStatisticStatus").attr("checked",false);
            $("#cpeActionStatisticStatus").attr("checked",false);
            $("#cpeNumStatisticStatus").attr("disabled",true);
            $("#cpeActionStatisticStatus").attr("disabled",true);
		}else{
			//开启采集
            $("#cpeNumStatisticStatus").attr("checked",true);
            $("#cpeActionStatisticStatus").attr("checked",true);
			$("#cpeNumStatisticStatus").attr("disabled",false);
			$("#cpeActionStatisticStatus").attr("disabled",false);
		}
	}); 
	
	
	initData();
})

function saveDataPolicy(){  
   	var initData = $("#initData").val() * (24*60*60*1000);
   	var statisticData = $("#statisticData").val() * (24*60*60*1000);
   	var actionData = $("#actionData").val() * (24*60*60*1000);
   	var cmHistoryData = $("#cmHistoryData").val() * (24*60*60*1000);
   	if(!isDigit($("#initData").val()) || parseInt($("#initData").val(), 10) < 1 || parseInt($("#initData").val(), 10) > 36500){
   		$("input#initData").focus();
   		return;
   	}
   	if(!isDigit($("#statisticData").val()) || parseInt($("#statisticData").val(), 10) < 1 || parseInt($("#statisticData").val(), 10) > 36500){
   		$("input#statisticData").focus();
   		return;
   	}
   	if(!isDigit($("#actionData").val()) || parseInt($("#actionData").val(), 10) < 1 || parseInt($("#actionData").val(), 10) > 36500){
   		$("input#actionData").focus();
   		return;
   	}
   	if(!isDigit($("#cmHistoryData").val()) || parseInt($("#cmHistoryData").val(), 10) < 1 || parseInt($("#cmHistoryData").val(), 10) > 36500){
   		$("input#cmHistoryData").focus();
   		return;
   	}
	window.top.showWaitingDlg("@COMMON.waiting@", "@text.modifyConfig@", 'ext-mb-waiting',null,false);
    	$.ajax({
  		  url: '/cmCpe/modifyCmCpeCollectDataPolicy.tv?initData=' + initData + '&statisticData=' + statisticData 
  				  + '&actionData=' + actionData + '&cmHistoryData=' + cmHistoryData,
    	      type: 'post',
    	      success: function(response) {
    	    	  if(response == "success"){
   					window.top.closeWaitingDlg("@COMMON.waiting@");
   	  	    		window.location.reload();
   	  	    	}else{
   					window.top.closeWaitingDlg("@COMMON.waiting@");
   	  	    		window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.updatefailure@");
   	  	    	}
  			}, error: function(response) {
			window.top.closeWaitingDlg("@COMMON.waiting@");
  				window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.updatefailure@");
  			}, cache: false
  		});
}

function saveOnuCpeConfig(){
	var onuCpeStatus = $("#onuCpeStatus").attr("checked");
	if(onuCpeStatus){
		onuCpeStatus = 1;
	}else{
		onuCpeStatus = 0;
	}
	var onuCpeInterval =  $("#onuCpeInterval").val();
	if(!V.isInRange(onuCpeInterval,[15,240])){
		return $("#onuCpeInterval").focus();
	}
	onuCpeInterval = onuCpeInterval*60;
	window.top.showWaitingDlg("@COMMON.waiting@", "@ONUCPE/ONUCPE.modifyConfig@", 'ext-mb-waiting',null,false);
	$.ajax({
	  url: '/epon/onucpe/modifyOnuCpeConfig.tv',
	  data:{onuCpeStatus:onuCpeStatus,onuCpeInterval:onuCpeInterval},
      success: function() {
    	  window.top.closeWaitingDlg("@COMMON.waiting@");
    	  window.location.reload();
      },error:function(){
    	  window.top.closeWaitingDlg("@COMMON.waiting@");
    	  window.parent.showMessageDlg("@COMMON.tip@", "@ONUCPE/ONUCPE.modifyCpeCollectFail@");
      }
	});
}

function saveCmSnmpParamConfig(){
	readCommunity = $("#pass1").val()
	writeCommunity = $("#pass2").val()
	if(readCommunity == ''){
		$("input#readCommunity").focus();
		return;
	}
	if(writeCommunity == ''){
		$("input#writeCommunity").focus();
		return;
	}
	window.top.showWaitingDlg("@COMMON.waiting@", "@text.modifyConfig@", 'ext-mb-waiting',null,false);
	$.ajax({
		  url: '/cmCpe/modifyCmSnmpParamConfig.tv?writeCommunity=' + writeCommunity + '&readCommunity=' + readCommunity,
	      type: 'post',
	      success: function(response) {
	  	    	if(response == "success"){
					window.top.closeWaitingDlg("@COMMON.waiting@");
	  	    		window.location.reload();
	  	    	}else{
					window.top.closeWaitingDlg("@COMMON.waiting@");
	  	    		window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.updatefailure@");
	  	    	}
			}, error: function(response) {
				window.top.closeWaitingDlg("@COMMON.waiting@");
				window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.updatefailure@");
			}, cache: false
		});
}
function saveCmCpeConfig(){  
	        	var cmCollectStatus = $("#cmCollectStatus").attr("checked");
	        	if(cmCollectStatus){
	        		cmCollectStatus = 0;
	        	}else{
	        		cmCollectStatus = 1;
	        	}
	        	//var statisticSource = $("#statisticSource").val();
	        	var cmTypeStatisticStatus = $("#cmTypeStatisticStatus").attr("checked");
	        	if(cmTypeStatisticStatus){
	        		cmTypeStatisticStatus = 0;
	        	}else{
	        		cmTypeStatisticStatus = 1;
	        	}
	        	var cmActionStatisticStatus = $("#cmActionStatisticStatus").attr("checked");
	        	if(cmActionStatisticStatus){
	        		cmActionStatisticStatus = 0;
	        	}else{
	        		cmActionStatisticStatus = 1;
	        	}
	        	var cpeCollectStatus = $("#cpeCollectStatus").attr("checked");
	        	if(cpeCollectStatus){
	        		cpeCollectStatus = 0;
	        	}else{
	        		cpeCollectStatus = 1;
	        	}
	        	var cpeActionStatisticStatus = $("#cpeActionStatisticStatus").attr("checked");
	        	if(cpeActionStatisticStatus){
	        		cpeActionStatisticStatus = 0;
	        	}else{
	        		cpeActionStatisticStatus = 1;
	        	}
	        	var cpeNumStatisticStatus = $("#cpeNumStatisticStatus").attr("checked");
	        	if(cpeNumStatisticStatus){
	        		cpeNumStatisticStatus = 0;
	        	}else{
	        		cpeNumStatisticStatus = 1;
	        	}
                if(!isDigit($("#cmCollectPeriod").val()) || parseInt($("#cmCollectPeriod").val(), 10) < 15 || parseInt($("#cmCollectPeriod").val(), 10) > 240){
                    $("input#cmCollectPeriod").focus();
                    //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.CMCPE.collectPeriodTip);
                    return;
                }
	        	var cmCollectPeriod = $("#cmCollectPeriod").val() * (60*1000);
				window.top.showWaitingDlg("@COMMON.waiting@", "@text.modifyConfig@", 'ext-mb-waiting',null,false);
	        	$.ajax({
	      		  url: '/cmCpe/modifyCmCpeCollectConfig.tv?cmCollectStatus=' + cmCollectStatus + '&cmTypeStatisticStatus=' 
	      				  + cmTypeStatisticStatus + '&cmActionStatisticStatus=' + cmActionStatisticStatus + '&cpeCollectStatus=' + cpeCollectStatus + '&cpeActionStatisticStatus=' 
	      				  + cpeActionStatisticStatus + '&cpeNumStatisticStatus=' + cpeNumStatisticStatus + '&cmCollectPeriod=' + cmCollectPeriod,
	        	      type: 'post',
	        	      success: function(response) {
	      	  	    	if(response == "success"){
	      					window.top.closeWaitingDlg("@COMMON.waiting@");
	      	  	    		window.location.reload();
	      	  	    	}else{
	      					window.top.closeWaitingDlg("@COMMON.waiting@");
	      	  	    		window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.updatefailure@");
	      	  	    	}
	      			}, error: function(response) {
      					window.top.closeWaitingDlg("@COMMON.waiting@");
	      				window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.updatefailure@");
	      			}, cache: false
	      		});
	        }  
</script>

</head>
<body class="whiteToBlack">
<div class=formtip id=tips style="display: none"></div>
	<div class="edge10">
		<div id="putBtnGroup" class="pB10 floatLeft" style="width:100%;"></div>
		<div class="jsTabPart">
			<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			 <thead>
				 <tr>
					 <th colspan="6" class="txtLeftTh">@CMCPE.terminalCollectConfig@</th>
				 </tr>
			 </thead>
			 <tbody>
			 	<tr>
			 		<td class="rightBlueTxt" style="text-align: right">@CMCPE.startCmCollect@</td>
					<td colspan="5" style="text-align:left;"><input id="cmCollectStatus" type="checkbox" class="vAlignMidden" /></td>
			 	</tr>
			 </tbody>
			 <tbody>
				 <tr>
					 <td class="rightBlueTxt" width="140"> @CMCPE.startCmTypeStatistics@</td>
					 <td><div style="width:200px;"><input id="cmTypeStatisticStatus" type="checkbox" class="vAlignMidden" /></div></td>
					 <td class="rightBlueTxt" width="140">@CMCPE.startCmActionStatistics@</td>
					 <td><div style="width:200px;"><input id="cmActionStatisticStatus" type="checkbox" class="vAlignMidden" /></div></td>
					 <td class="rightBlueTxt" width="140">@CMCPE.collectDataSources@</td>
					 <td>
					 	<div style="width:200px;">
					 		<input id="statisticSource" disabled="disabled" type="text" class="normalInput" value="@CMCPE.cmDataSources@"/>
					 	</div>
					 </td>
				</tr>
				 <tr>
					 <td class="rightBlueTxt">@CMCPE.startCpeCollect@
					 </td>
					 <td><input id="cpeCollectStatus" type="checkbox" class="vAlignMidden" /></td> 
					 <td class="rightBlueTxt">@CMCPE.startCpeCountStatistics@
					 </td>
					 <td><input id="cpeNumStatisticStatus" type="checkbox" class="vAlignMidden" /></td> 
					 <td class="rightBlueTxt">@CMCPE.startCpeActionStatistics@
					 </td>
					 <td><input id="cpeActionStatisticStatus" type="checkbox" class="vAlignMidden" /></td> 
				</tr>
				<tr>
					<td class="rightBlueTxt">@CMCPE.collectPeriod@</td>
					<td colspan="5"><input id="cmCollectPeriod" type="text" class="normalInput w120" tooltip='@CMCPE.collectPeriodTip@'/> @ENTITYSNAP.deviceUpTime.minute@</td> 
				</tr>
				<tr>
					<td colspan="6">
						<div class="noWidthCenterOuter clearBoth">
						     <ol class="upChannelListOl pB5 pT5 noWidthCenter">
						         <li><a  onclick="saveCmCpeConfig()" href="javascript:;" class="normalBtn"><span><i class="miniIcoData"></i>@CMC.button.save@</span></a></li>
						     </ol>
						</div>
					</td>
				</tr>
			</tbody>
			</table>
			<div id='cmSnmpParamConfig' class="pB10"></div>
		</div>
		
		<div class="jsTabPart">
			<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			 <thead>
				 <tr>
					 <th colspan="6" class="txtLeftTh">@cmPoll.snmpConfig@</th>
				 </tr>
			 </thead>
			 <tbody>
				 <tr>
				 	 <td class="rightBlueTxt" width="140">@CMC.text.rocommunity@：
					 </td>
					 <td>
					 	<div style="width:200px;" id="readCommunity"></div>
					 </td>
					 <td class="rightBlueTxt" width="140">@CMC.text.rwcommunity@：
					 </td>
					 <td>
					 	<div style="width:200px;" id="writeCommunity"></div>
					 </td>
					 <td class="rightBlueTxt" width="140">
					 </td>
					 <td>
					 	<div style="width:200px;"></div>
                    </td> 
				</tr>
				<tr>
					<td colspan="6">
						<div class="noWidthCenterOuter clearBoth">
						     <ol class="upChannelListOl pB5 pT5 noWidthCenter">
						         <li><a  onclick="saveCmSnmpParamConfig()" href="javascript:;" class="normalBtn"><span><i class="miniIcoData"></i>@CMC.button.save@</span></a></li>
						     </ol>
						</div>
					</td>
				</tr>
			</tbody>
			</table>
		<%if(uc.hasPower("cmPollConfig")){ %>
		<div id='cmPollConfig' class="pB10"></div>
		</div>
		<div class="jsTabPart">
			<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
				 <thead>
					 <tr>
						 <th colspan="6" class="txtLeftTh">@cmPoll.config@</th>
					 </tr>
				 </thead>
				 <tbody>
				 	<tr>
						<td class="rightBlueTxt" width="140">@cmPoll.config@@COMMON.maohao@</td>
						 <td>
						 	<div style="width:200px">
						 		<select id="cmPollConfigSel" class="w160 normalSel" onchange=changeCmPollSel()>
						 			<option value="GlobalCmList">@cmPoll.useCmPoll@</option>
						 			<option value="SpecifiedCmList">@cmPoll.useManageList@</option>
						 			<option value="CmPollClosed">@cmPoll.closeCmPoll@</option>
						 		</select>
						 	</div>
						 </td>
						 <td width="140"></td>
						 <td>
						 	<div style="width:200px">
						 	</div>
						 </td>
						 <td class="rightBlueTxt" width="140"></td>
						 <td>
						 	<div style="width:200px"></div>
						 </td>
					</tr>
				 </tbody>
				 <tbody id="cmPollConfigTbody">
					 <tr>
						 <td class="rightBlueTxt" width="140">@cmPoll.pollInterval@@COMMON.maohao@</td>
						 <td>
						 	<div style="width:200px;">
							 	<ul class="leftFloatUl">
							 		<li>
							 			<input id="cmPollInterval" type="text" class="normalInput w120" tooltip='@cmPoll.pollIntervalTip@' maxlength="5" />
							 		</li>
							 		<li class="pT2 pR10">@ENTITYSNAP.deviceUpTime.minute@</li>
							 	</ul>
						 	</div>
						 </td>
						 <td class="rightBlueTxt" width="140"><label class="jsManageList">@cmPoll.importManageList@@COMMON.maohao@</label></td>
						 <td colspan="3">
						 	<div class="jsManageList">
						 		<ul class="leftFloatUl">
						 			<li>
						 				<a href="javascript:;" class="normalBtn" onclick="openImportPage()"><span><i class="miniIcoInport"></i>@cmPoll.import@</span></a>
						 			</li>
						 			<li>
						 				<div id="putExportBtn" class="clearDataTable"></div>
						 			</li>
						 			<li class="pT2">
						 				<label class="jsManageList pL10" id="cmRollCount">@cmPoll.maxImport@</label>
						 			</li>
						 		</ul>
						 	</div>
						 </td>
					</tr>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="6">
							<div class="noWidthCenterOuter clearBoth">
        						<ol class="upChannelListOl pB0 pT0 noWidthCenter">
        							<li>
										<a onclick="saveCmPollConfigClick()" href="javascript:;" class="normalBtn"><span><i class="miniIcoData"></i>@CMC.button.save@</span></a>
									</li>
								</ol>
							</div>
						</td>
					</tr>
				</tfoot>
			</table>
			<%} %>
		<div id='dataPolicyConfig' class="pB10"></div>
		</div>
			
		<div class="jsTabPart clearBoth">
			<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			 <thead>
				 <tr>
					 <th colspan="6" class="txtLeftTh">@CMCPE.dataPolicyConfig@</th>
				 </tr>
			 </thead>
			 <tbody>
				 <tr>
					 <td class="rightBlueTxt" width="140">@CMCPE.initialdata@
					 </td>
					 <td>
					 	<div style="width:200px">
					 		<input id="initData" type="text" class="normalInput w120" tooltip='@CMCPE.dataSavePolicyPeriod@'/> @ENTITYSNAP.deviceUpTime.day@
					 	</div>
					 </td>
					 <td class="rightBlueTxt" width="140">@CMCPE.statisticsdata@
					 </td>
					 <td>
					 	<div style="width:200px">
					 		<input id="statisticData" type="text" class="normalInput w120" tooltip='@CMCPE.dataSavePolicyPeriod@'/> @ENTITYSNAP.deviceUpTime.day@
					 	</div>
					 </td>
					 <td class="rightBlueTxt" width="140">@CMCPE.actiondata@</td>
					 <td>
					 	<div style="width:200px">
					 		<input id="actionData" type="text" class="normalInput w120" tooltip='@CMCPE.dataSavePolicyPeriod@'/> @ENTITYSNAP.deviceUpTime.day@
					 	</div>	
					 </td> 
				</tr>
				<tr>
					 <td class="rightBlueTxt" width="140">@cm.cmHistoryData@</td>
					 <td colspan="5"><input id="cmHistoryData" type="text" class="normalInput w120" tooltip='@CMCPE.dataSavePolicyPeriod@'/> @ENTITYSNAP.deviceUpTime.day@</td>
				</tr>
				<tr>
					<td colspan="6">
						<div class="noWidthCenterOuter clearBoth">
						     <ol class="upChannelListOl pB5 pT5 noWidthCenter">
						         <li><a  onclick="saveDataPolicy()" href="javascript:;" class="normalBtn"><span><i class="miniIcoData"></i>@CMC.button.save@</span></a></li>
						     </ol>
						</div>
					</td>
				</tr>
			</tbody>
			</table>
		</div>
			
			<%if(uc.hasSupportModule("onu")){ %>
			<div id='onuCpeConfig' class="pB10"></div>
		<div class="jsTabPart">
			<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			 <thead>
				 <tr>
					 <th colspan="6" class="txtLeftTh">@ONUCPE/ONUCPE.onuCpeCollect@</th>
				 </tr>
			 </thead>
			 <tbody>
				 <tr>
					 <td class="rightBlueTxt" width="140">@ONUCPE/ONUCPE.startOnuCpeCollect@@COMMON.maohao@</td>
					 <td>
					 	<div style="width:200px;"><input id="onuCpeStatus" type="checkbox" class="vAlignMidden" /></div>
					 </td>
					 <td class="rightBlueTxt" width="140">@ONUCPE/ONUCPE.collectInterval@@COMMON.maohao@</td>
					 <td>
					 	<div style="width:200px;">
					 		<input id="onuCpeInterval" type="text" class="normalInput w120" tooltip='@CMCPE.collectPeriodTip@'/> @ENTITYSNAP.deviceUpTime.minute@
					 	</div>
					 </td>
					 <td class="rightBlueTxt" width="140">
					 </td>
					 <td>
					 	<div style="width:200px;"></div>
                     </td> 
				</tr>
				<tr>
					<td colspan="6">
						<div class="noWidthCenterOuter clearBoth">
						     <ol class="upChannelListOl pB5 pT5 noWidthCenter">
						         <li><a  onclick="saveOnuCpeConfig()" href="javascript:;" class="normalBtn"><span><i class="miniIcoData"></i>@COMMON.save@</span></a></li>
						     </ol>
						</div>
					</td>
				</tr>
			</tbody>
			</table>
		</div>
		</div>
			
		
		<%} %>
	</div>
	<iframe width="1" height="1" name="download_iframe" id="download_iframe" frameborder="0"></iframe>
	<script type="text/javascript" src="../js/jquery/Nm3kTabBtn.js"></script>
	<script type="text/javascript">
		$(function(){
		var tabs = ["@CMC.GP.viewAll@","@CMCPE.terminalCollectConfig@","@cmPoll.snmpConfig@","@CMCPE.dataPolicyConfig@","@ONUCPE/ONUCPE.onuCpeCollect@"];
		<%if(uc.hasPower("cmPollConfig")){ %>
		tabs = ["@CMC.GP.viewAll@","@CMCPE.terminalCollectConfig@","@cmPoll.snmpConfig@","@cmPoll.config@", "@CMCPE.dataPolicyConfig@","@ONUCPE/ONUCPE.onuCpeCollect@"]
		<%}%>
		var tab1 = new Nm3kTabBtn({
		    renderTo:"putBtnGroup",
		    callBack:"msg",
		    tabs:tabs
		});
		tab1.init();
	});
	
	function msg(index){
	    switch(index){
	    	case 0:
	    		$(".jsTabPart").css("display","block");
	    		break;
	    	default:
	    		$(".jsTabPart").css("display","none");
	    		$(".jsTabPart").eq(index-1).fadeIn();
	    		break;		
	    	
	    }	
	}
	</script>
</body>
</Zeta:HTML>