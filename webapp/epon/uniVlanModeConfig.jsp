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
    module epon
    IMPORT /epon/uniVlanTrunk
    IMPORT /epon/uniVlanAggr
    IMPORT /epon/uniVlanTrans
    IMPORT /epon/uniVlanCommon
</Zeta:Loader>
<script type="text/javascript">
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var entityId = '${entityId}';
var uniId = '${uniId}';
var vlanMode = '${portVlanAttribute.vlanMode}';
Ext.onReady(function(){
	//加载转换数据
	loadTransData();
	//加载聚合数据
    loadAggrData();
  	//加载trunk数据
  	loadTrunkData();
  	show_configDiv(0);
});
function cancelClick() {
	window.top.closeWindow("vlanModeConfig");
}
function loadMode(){
	$("#vlanMode").val(vlanMode);
}
function show_configDiv(flag){
	//判断进入的VLAN模式
	var selectId = $("#vlanMode").val();
	if(flag == 0 || selectId == vlanMode){//判断选择的模式是否与实际模式相等
		if(vlanMode == 1){//1标识为标记模式
			if(operationDevicePower){
				$("#tagSubmit").attr("disabled", false);	
			}
		}
		$("#refreshSubmit" + selectId).attr("disabled", false);	
		shinning('showMessage');
		$("#discription").show();
		$("#modeChange").hide();
	}else{//非标记模式
		shinning('configMessage');
		$("#discription").hide();
		$("#modeChange").show();
	}
	if(selectId == 0){// 0标识tag模式
		$("#tag").hide();
		$("#hybrid").show();
		$("#translationList").hide();
		$("#aggregation").hide();
		$("#trunk").hide();
	}else if(selectId == 1){//1 标识标记模式
		$("#tag").show();
		$("#hybrid").hide();
		$("#translationList").hide();
		$("#aggregation").hide();
		$("#trunk").hide();
	}else if(selectId == 2){//2 标识转换模式
		$("#tag").hide();
		$("#hybrid").hide();
		$("#translationList").show();
		$("#aggregation").hide();
		$("#trunk").hide();
	}else if(selectId == 3){//3 标识聚合模式
		$("#tag").hide();
		$("#hybrid").hide();
		$("#translationList").hide();
		$("#aggregation").show();
		$("#trunk").hide();
	}else if(selectId == 4){//4 标识Trunk模式
		$("#tag").hide();
		$("#hybrid").hide();
		$("#translationList").hide();
		$("#aggregation").hide();
		$("#trunk").show();
	}
}
function shinning(shinId,sec,speed,count){
	speed = (!speed)? 500 : speed == 'fast'?100:speed =='slow'?1000:isNaN(speed)?500:speed;	
	count = (count == 'undefined' ||count == null ||count == 0)  ? 0:count;
	sec = (sec == 'undefined' ||sec == null ||sec == 0) ? 2 : sec;
	if(count < sec){			
		$("#"+shinId).animate({
			opacity : 0
		},{
			duration : speed,
			complete : function(){
					$("#"+shinId).animate({
						opacity : 1
					},{
						duration : speed,  
						complete : function(){
							shinning(shinId,sec,speed,++count);
						}					
					});	
			}			
		});		
	}	
}
function authLoad(){
	var ids = new Array();
	ids.add("pvid");
	ids.add("tagSubmit");
	ids.add("beforeTransId");
	ids.add("afterTransId");
	ids.add("aggregationSvlanId");
	ids.add("aggregationCvlanId");
	ids.add("trunkId");
	ids.add("vlanMode");
	operationAuthInit(operationDevicePower,ids);
}
</script>
</head>
<body class="openWinBody" onload="loadMode();authLoad()">
<div class=formtip id=tips style="display: none"></div>
<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@ONU.uniVlanMode@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	
<div class="edge10 pB0 clearBoth tabBody">
		<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			<thead>
				<tr>
					<th colspan="6" class="txtLeftTh">@VLAN.uniVlanCfg@</th>
				</tr>
			</thead>
			<tbody>
		<tr>
			<td>@VLAN.vlanMode@:</td>
			<td align=left width=70%><select id="vlanMode" style="width:127px;" onchange="show_configDiv(1);" >
    		<option value="0">@VLAN.transa@</option>
    		<option value="1">@VLAN.mark@</option>
    		<option value="2">@VLAN.trans@</option>
   			<option value="3">@VLAN.agg@</option>
   			<option value="4">Trunk</option>
    		</select>				
			</td>
		</tr>
		<tr>
			<td valign=middle width=100% colspan='2'>
				<div id="modeChange" style= "display:none;padding-left:10px;valign:middle;">
				<table cellspacing=0 cellpadding=0>
					<tr>
						<td valign="middle">
							<img src="../../images/system/monitor32.gif" align="top" /> 
						</td>
						<td valign="middle">
							<label id="configMessage">@VLAN.modeHasNoRule@</label>
							<button id="okBt" class=BUTTON75 onclick="saveModeClick()">
								@COMMON.apply@
							</button>
							<button id="cancelBt" class=BUTTON75 onclick="cancelModeClick()">@VLAN.ret@ </button>
						</td>
					</tr>
				</table>
				</div>
				<div id="discription" style= "display:none;padding-left:10px;">
				<table cellspacing=0 cellpadding=0>
					<tr>
						<td valign="middle"><img src="/images/icon-info.gif" align="middle"/>
						</td>
						<td valign="middle"><div id="showMessage">@VLAN.modeSwitchTip@</div></td>
					</tr>
				</table>
				</div>
			</td>
		</tr>
		</tbody>
	</table>
</div>
<div class="edge10 pB0 clearBoth tabBody" id='tag' style= "display:none;">
		<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			<thead>
				<tr>
					<th colspan="6" class="txtLeftTh">@VLAN.markModeNote@</th>
				</tr>
			</thead>
			<tbody>
		<tr>
			<td>
				<div align="left" style="padding:10px;">
					@VLAN.handleManner@
				</div>
          </td>
		</tr>
		<tr>
                <td>
                 <fieldset style="background-color:#ffffff;padding-top:5px;padding-bottom:5px;">
                     <legend>@VLAN.ruleManu@</legend>
                        <table>
                            <tr>
                                <td align=center width="15%">PVID@COMMON.maohao@</td>
                                <td width="40%"><input id="pvid" type="text"  size=30 value="${portVlanAttribute.vlanPVid }" class="normalInput"
                                    maxlength="4" tooltpi="@COMMON.range4094@" />
                                </td>
                                <td width="30%" align="left">
                                    <button id="tagSubmit" class=BUTTON75 onclick="okClick('tag');" disabled>@COMMON.modify@</button>
                                </td>
                            </tr>
                        </table>
                    </fieldset>
             </td>
           </tr>
           </tbody>
       </table>
	</div>
	<div class="edge10 pB0 clearBoth tabBody" id='hybrid' style= "display:none;">
		<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			<thead>
				<tr>
					<th colspan="6" class="txtLeftTh">@VLAN.transModeNote@</th>
				</tr>
			</thead>
			<tbody>
		<tr>
			<td>
				<div align="left" style="padding:10px;">
					@VLAN.transModeManner@
				</div>
          </td>
		</tr>
		<tr>
			<td style="padding-left:448px;padding-top:8px;">
				<button class=BUTTON75 
                     onclick="cancelClick();">@COMMON.close@</button>
          	</td>
		</tr>
		</tbody>
       </table>
	</div>
	
		<div class="edge10 pB0 clearBoth tabBody" id='translationList' style= "display:none;">
		<table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			<tbody>
			<tr>
				<td colspan="5"><div id="translation"></div></td>
			</tr>
	            <tr>
                  <td>@VLAN.originVlan@ ID:</td>
                  <td width="10%"><input id="beforeTransId" class="normalInput" type="text" maxlength="4" /></td>
                  <td>@VLAN.translatedVlan@ ID:</td>
                  <td width="10%"><input id="afterTransId" type="text" maxlength="4"  class="normalInput" tooltip="@COMMON.range4094@"/></td>
                  <td><button id="transSubmit" class=BUTTON75 onclick="okClick('trans');" disabled>@VLAN.add@</button></td>
              </tr>
           </tbody>
       </table>
	</div>
	
	<div class="edge10 pB0 clearBoth tabBody" id='aggregation' style= "display:none;">
		<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			<tbody>
			<tr>
					<td colspan="2">
						<div id='aggrSvlan_grid'></div>
					</td>
					<td colspan="3">
						<div id='aggrCvlan_grid'></div>
					</td>
				</tr>
                <tr>
                    <td>@VLAN.aggVlan@ ID@COMMON.maohao@</td>
                    <td><input id="aggregationSvlanId" type="text" size=10  class="normalInput"
                        maxlength="4" title='@VLAN.filterRuleUni@'/>
                    </td>
                    <td>@VLAN.aggVlanBef@ ID@COMMON.maohao@</td>
                    <td><input id="aggregationCvlanId" type="text" size=10  class="normalInput"
                        maxlength="4" title='@VLAN.filterRuleUni@'/>  
                    </td>
                    <td>
                        <button id="aggrSubmit" class=BUTTON75   onclick="aggrAdd()" disabled>@COMMON.confirm@</button>
                    </td>
                </tr>
           </tbody>
			</table>
	</div>
	<div class="edge10 pB0 clearBoth tabBody" id='trunk' style= "display:none;">
		<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			<tbody>
			<tr>
				<td colspan="3"><div id="trunkIdList"></div></td>
			</tr>
			<tr>
               <td>Trunk VLAN ID@COMMON.maohao@</td>
               <td><input id="trunkId" type="text" tooltip="@COMMON.range4094@" class="normalInput w200" maxlength="4"/></td>
               <td width="auto"><button id="trunkSubmit" class=BUTTON75  onclick="okClick('trunk');" disabled>@VLAN.add@</button></td>
           </tr>
           </tbody>
		</table>
	</div>
	
	<Zeta:ButtonGroup>
		<Zeta:Button onClick="refreshClick('trunk');" icon="miniIcoSaveOK">@COMMON.fetch@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()" >@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>