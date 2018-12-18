<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<script type="text/javascript">
var vlanConfigType = 'llid';
var heigh = 60;
var wid = 25;
</script>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module epon
    IMPORT epon.ponVlanCommon
</Zeta:Loader>
<style type="text/css">
#llidTree .x-tree-root-ct{overflow:auto; overflow:auto !important; height:392px;}
#llidTree .x-tree-root-ct .x-tree-selected span{ background:#C2C2C2;}
select[disabled]{ background:url("css/white/normalInputDisabled.png") repeat; height:22px; border:1px solid #B7B8CA;}
</style>
<link rel="stylesheet" type="text/css" href="../css/VerticalTabPanel.css"/>
<script type="text/javascript" src="/js/ext/ux/VerticalTabPanel.js"></script>
<script type="text/javascript" src="/epon/ponVlanTransMode.js"></script>
<script type="text/javascript" src="/epon/ponVlanAggrMode.js"></script>
<script type="text/javascript" src="/epon/ponVlanTrunkMode.js"></script>
<script type="text/javascript" src="/epon/ponVlanQinqMode.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript">
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var entityId = '${entity.entityId}';
var ponId = '${ponId}';
var tabNum = ${tabNum};
var tipNum = '${tipNum}' || 0;
var currentId = '${currentId}';
var rootNode;
var onuNode;
var ponPortName = "";
var macList = new Array();
var selectedMac;
var llidTree;
var lastSelectedOnu = "${treenodeid}";
var tpidDefault = '${vlanLlidTpidDefault}';
var tpidValue = '${vlanLlidTpidValue}';
if(tpidValue.split(",").length){
	tpidValue = tpidValue.replace("[", "").replace("]", "").split(",");
	for(var a=0,tmpL=tpidValue.length; a<tmpL; a++){
		tpidValue[a] = $.trim(tpidValue[a]);
	}
}

function LlidModeChange(){
	window.top.createDialog('ponVlanConfig', I18N.VLAN.cfgMgmtPon , 820, 550, '/epon/vlan/showPonVlanConfig.tv?tabNum=' +
			tabNum   +'&tipNum='+tipNum+'&entityId=' + entityId + '&ponId=' + ponId + '&currentId=' + currentId, null, true, true);
    cancelClick();
}
function cancelClick() {
    window.parent.closeWindow('ponVlanLlidConfig');
}
function onMacNodeClick(node){
	if(node.attributes.id != 'ponPort'){
		Ext.getCmp("llidTree").getNodeById(node.attributes.id).select();
		selectedMac = node.attributes.id;
		//reload选中Mac的所有grid数据
		// VLAN转换
		loadTransMode();
	    // VLAN聚合
	    loadAggrMode();
	    // VLAN Trunk
	    loadTrunkMode();
	    // QinQ
	    loadQinqMode();
	}
}
function checkMac(){
	var reg0 = /^([0-9a-f]{2})(([/\s-:][0-9a-f]{2}){0,5})+$/i;
	if($("#llidMac").val() == "" || $("#llidMac").val() == null || $("#llidMac").val() == undefined){
		return true;
	}
	if(reg0.exec($("#llidMac").val())){
		return true;
	}
	return false;
}
function macSearchKeyDown(){
	if(!window.event){
		return;
	}
	if(window.event.keyCode == 13){
		macSearch();
	}
}
function macSearch(){
	var w = DOC.body.clientWidth - 440;
	if( !$("#llidMac").val() ){
		reloadMacList(w,offsetHeight,macList);
	    selectFirstMac(macList);
	 	// VLAN转换
		loadTransMode();
	    // VLAN聚合
	    loadAggrMode();
	    // VLAN Trunk
	    loadTrunkMode();
	    // QinQ
	    loadQinqMode();
	}else{
		var macTmpList = new Array();
		var macText = eval("/"+$("#llidMac").val().replace(/([/\s-])/g,":")+"/ig");
		var j = 0;
		for(var i=0; i < macList.length; i++){
			if(macList[i].search(macText) != -1){
				macTmpList[j] = macList[i];
				j++;
			}
		}
		if (j > 0) {
			reloadMacList(w,offsetHeight,macTmpList);
		    selectFirstMac(macTmpList);
		 	// VLAN转换
			loadTransMode();
		    // VLAN聚合
		    loadAggrMode();
		    // VLAN Trunk
		    loadTrunkMode();
		    // QinQ
		    loadQinqMode();
		} else {
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.noMatchOnu)
		}
	}
}
function macInput(){
	if(checkMac){
		$("#llidMac").css("border","1px solid #8bb8f3");
	}else{
		$("#llidMac").css("border","1px solid #FF0000");
		$("#llidMac").focus();
	}
}
function reloadMacList(w,h,array){
   	$("#llidTreePanel").empty();
    rootNode = new Ext.tree.TreeNode({
    	text: ponPortName, id: "ponPort",icon:'/epon/image/pon.png',
    	listeners:{
    		expand :function(){
    			if(lastSelectedOnu){
    			    //llidTree.getNodeById( lastSelectedOnu ).select();
    			    var $node =llidTree.getNodeById( lastSelectedOnu );
    			    $node.getUI().fireEvent("click",$node);
    		    }
    		}
    	}
    });
    
    
    for(var p = 0; p < array.length; p++){
    	var $mac = array[p].split("[")[0];
		onuNode = new Ext.tree.TreeNode({text: array[p], id: $mac, icon:'/epon/image/port_48.gif'});
		rootNode.appendChild(onuNode);
	}//end for
	llidTree = new Ext.tree.TreePanel({
        id : 'llidTree',
        renderTo : 'llidTreePanel',
        border: true, 
        lines: true, 
        enableDD: false, 
        autoScroll: true,
        height:h,
        width:190,
        root : rootNode,
        listeners : {
        	click : onMacNodeClick
        },
        bbar: new Ext.Toolbar({
 	       scope : this,
 	       items :[{
					text : "Mac:",
					tooltip: "@VLAN.macAddrFilter@"
				},{
	 	            xtype:"textfield", 
	 	            id:"llidMac",
	 	            width:100,
	 	            enableKeyEvents:true,
	 	           	listeners : {
	 	           		keyup : macInput,
	 	           		keydown: macSearchKeyDown
 	           		}
 	            },'-',{
	 	 	        xtype:"button",
	 	 	        text : "@COMMON.query@", 
	 	 	        id : "macSearch",
	 	 	        width : 35,
	 	 	     	listeners : {
	 	 	     		click : macSearch
 	         		}
 	 	        }
 	       ]
 	    })
    });
    llidTree.getRootNode().expand();
}
function selectFirstMac(array){
	if(array.length != 0){
		var $thisId= array[0].split("[")[0];
		Ext.getCmp("llidTree").getNodeById($thisId).select();
		selectedMac = $thisId;
		$("#beforeTransId").val("");
		$("#afterTransId").val("");
		$("#aggregationSvlanId").val("");
		$("#aggregationCvlanId").val("");
		$("#trunkId").val("");
		$("#qinqOuterVlanId").val("");
		$("#qinqInnerVlanStartId").val("");
		$("#qinqInnerVlanEndId").val("");
	}
}
//COS
function transCosModeClick() {
	if ($("#transCosCheckbox1").attr("checked")) {
		$("#transCos").attr("disabled", false);
	} else if ($("#transCosCheckbox2").attr("checked")) {
		$("#transCos").val("0");
		$("#transCos").prop("disabled", true);
	}
}
function aggrCosModeClick() {
	if ($("#aggrCosCheckbox1").attr("checked")) {
		$("#aggrCos").attr("disabled", false);
	} else if ($("#aggrCosCheckbox2").attr("checked")) {
		$("#aggrCos").val("0");
		$("#aggrCos").attr("disabled", true);
	}
}
//TPID的验证、输入框样式
function tpidChanged(id){
	var tpid = $("#"+id).val().toLocaleLowerCase();
	var reg = /^([0-9a-f]{4})+$/i;
	if(tpid == "" || (reg.exec(tpid) && tpidValue.indexOf(tpid) > -1)){
		tpidCssChange(false,id);
		return true;
	}else{
		$("#"+id).focus();
		tpidCssChange(true,id);
		return false;
	}
}
function tpidCssChange(red,id){
	var pa1 = $("#"+id);
	var pa2 = $("#"+id+"0x");
	if(red){
		pa1.css("border","1px solid #FF0000");
		pa2.css("border","1px solid #FF0000");
	}else{
		pa1.css("border","1px solid #8bb8f3");
		pa2.css("border","1px solid #8bb8f3");
	}
	pa1.css("border-left","0px");
	pa2.css("border-right","0px");
	pa1.attr("title", String.format(I18N.VLAN.configVlanLimit, "0x" + tpidValue.join(",0x")));
}
Ext.onReady(function(){
	var w = document.body.clientWidth - 440;
	window.offsetHeight = document.body.clientHeight - 62;
	if(currentId == null || currentId == "" || currentId == undefined){
		ponPortName = I18N.VLAN.unknownOnu
	}else{
		ponPortName = "PON:" + currentId.split('_')[1] + "/" + currentId.split('_')[2];
	}
	// 加载Mac地址列表
	$.ajax({
       	url:"/epon/vlan/loadOnuMacAddress.tv?ponId="+ponId,
        method:"post",dataType:'json',
        success:function (json) {
            for(var i=0; i < json.length; i++){
            	macList[i] = json[i];
            }
            reloadMacList(w,offsetHeight,macList);
            selectFirstMac(macList);
         	// VLAN转换
        	loadTransMode();
            // VLAN聚合
            loadAggrMode();
            // VLAN Trunk
            loadTrunkMode();
            // QinQ
            loadQinqMode();
			if(macList.length == 0){
				$("#loadingMask").css("opacity",0.9);
		    	$(".loadingText").html("<p style='color:red'>" + I18N.VLAN.hasNoOnu+ "</p>")
		    }else{		    	
		    	$(".loadingText").html("<p style='color:green'>"+ I18N.COMMON.ok +"</p>")
				removeMask();
		    }
        },
        error:function () {
            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.onuLoadError)
            $(".loadingText").html("<p style='color:red'>"+I18N.VLAN.hasNoOnu+"</p>");
            $("#loadingMask").css("opacity",0.9);
    	}
    });
	tabs = new Ext.TabPanel({
        renderTo: 'tabs',
        width: w,
        height: offsetHeight,
        activeTab:tabNum,
        frame:true,
        defaults:{autoHeight: true},
        items:[
            {itemId:'x1',contentEl: 'ponVlanTransTab', title: I18N.VLAN.translate },
            {itemId:'x2',contentEl: 'ponVlanNto1Tab', title: I18N.VLAN.aggregate},
            {itemId:'x3',contentEl: 'ponVlanTrunkTab', title: 'VLAN Trunk'},
            {itemId:'x4',contentEl: 'ponVlanQinqTab', title: 'QinQ'}
        ],
        listeners:{
        	tabchange : function(){
					if(transGrid  && aggrSvlanGrid  && tabs && aggrCvlanGrid && qinqSvlanGrid && qinqCvlanGrid ){
						var a = tabs.getActiveTab().itemId;
						if(a == 'x1'){
							transStore.loadData(transData);
						}else if(a == 'x2'){
							aggrSvlanStore.loadData(aggrSvlanData);
							aggrSelectFirstRow();
							aggrCvlanReload();
						}else if(a == 'x3'){
							trunkStore.loadData(trunkData);
						}else if(a == 'x4'){
							qinqSvlanStore.loadData(qinqSvlanData);
							qinqSelectFirstRow();
							qinqCvlanReload();
						}
						tabNumChange(a);
					}
				}
   	   	    }
    });
    tips = new Ext.TabPanel({
    	renderTo: 'tip',
    	width:220,
    	height:offsetHeight,
    	activeTab: parseInt(tipNum),
    	frame:true,
    	autoScroll:true,
    	defaults:{autoHeight: false},
    	viewConfig:{forceFit: false},
    	deferredRender: true,
    	items:[
			{itemId:'t1',contentEl: 'tip1Tab', title: "@COMMON.note@"},
			{itemId:'t2',contentEl: 'tip2Tab', title: 'CVID'},
			{itemId:'t3',contentEl: 'tip3Tab', title: 'SVID'}
   	    ],
   	    listeners:{
   	    	tabchange: function(){
					if(cvidModeGrid!=null && cvidModeGrid!=undefined && svidModeGrid!=null && svidModeGrid!=undefined && tips){
						var a = tips.getActiveTab().itemId;
						if(a == 't2' && !cvidLoadFlag){
							var tmpF = true;
							setTimeout(function(){
								if(tmpF){
									$("#loadingDiv").show();
								}
							}, 500);
							loadCvidStore();
							tmpF = false;
							$("#loadingDiv").hide();
						}else if(a == 't3' && !svidLoadFlag){
							var tmpF = true;
							setTimeout(function(){
								if(tmpF){
									$("#loadingDiv").show();
								}
							}, 500);
							loadSvidStore();
							tmpF = false;
							$("#loadingDiv").hide();
						}
					}
				} ,
			scope:this
   	   	    }
   	    
    });

	// CVID和SVID列表
	loadCsvidMode();
	//tpid框样式初始化
	tpidCssChange(false,"transTpid");
	tpidCssChange(false,"aggrTpid");
	tpidCssChange(false,"qinqTpid");
	var $itemId = tabs.get( tabNum ).itemId;
	tabNumChange( $itemId );
});

function authLoad(){
	var ids = new Array();
	ids.add("transCosCheckbox2");
	ids.add("transCosCheckbox1");
	ids.add("transCos");
	ids.add("transTpid0x");
	ids.add("transTpid");
	ids.add("aggrCosCheckbox2");
	ids.add("aggrCosCheckbox1");
	ids.add("aggrCos");
	ids.add("aggrTpid0x");
	ids.add("aggrTpid");
	ids.add("aggregationSvlanId");
	ids.add("aggregationCvlanId");
	ids.add("trunkId");
	ids.add("beforeTransId");
	ids.add("afterTransId");
	ids.add("qinqCosCheckbox2");
	ids.add("qinqCosCheckbox1");
	ids.add("qinqCos");
	ids.add("qinqTpid0x");
	ids.add("qinqTpid");
	ids.add("qinqOuterVlanId");
	ids.add("qinqInnerVlanStartId");
	ids.add("qinqInnerVlanEndId");
	//ids.add("vidUsageBt");
	operationAuthInit(operationDevicePower,ids);
	
	if(!refreshDevicePower){
        R.vidUsageBt.setDisabled(true);
    }
}
</script>
</head>
<body class=openWinBody onload="authLoad()">
<div id="loadingMask">
	<img class="loadingImage" src='/images/gray_loading.gif' alt='@COMMON.fetching@'/>
	<div class="loadingText" >@COMMON.fetching@</div>
</div>
<div class="edge10">
<table cellspacing=0 cellpadding=0>
<tr height="430px">
<td><div id="llidTreePanel" style="margin-top: 5px;"></div></td>
<td width=0px></td><td>
<div id="tabs">
<table>
<tr>
<td>
<div id="ponVlanTransTab" class="x-hide-display">
    <div style="overflow: auto; padding-left: 10px; padding-top: 10px;">
        <table cellspacing=7>
            <tr>
                <td colspan="3">
                    <fieldset style='width: 507px; height: 238px;'>
                        <legend>@VLAN.translateList@</legend>
                        <div id="transIdList"></div>
                    </fieldset>
                </td>
            </tr>
            <tr>
			    <td>
			        <table>
			            <tr>
			                <td>
			                    <fieldset style='width: 507px; height: 45px;'>
			                        <legend>@VLAN.translatedVlanPri@</legend>
			                        <table cellpadding="0" cellspacing="0">
			                            <tr>
			                                <td>
			                                    <input id="transCosCheckbox2" type="radio" name="transCos" value="2"
			                                           onclick="transCosModeClick()" checked/>@VLAN.useOriginVlanPri@ 
			                                    <input id="transCosCheckbox1" type="radio" name="transCos" value="1"
			                                           onclick="transCosModeClick()"/> @VLAN.useNewVlanPri@
			                                    <select id="transCos" disabled class="normalSel w50">
			                                        <option value="0">0</option>
			                                        <option value="1">1</option>
			                                        <option value="2">2</option>
			                                        <option value="3">3</option>
			                                        <option value="4">4</option>
			                                        <option value="5">5</option>
			                                        <option value="6">6</option>
			                                        <option value="7">7</option>
			                                    </select>
			                                </td>
			                                <td>@VLAN.translatedVlan@TPID: 
												<input id="transTpid0x" type="text" disabled value="0x" class="normalInput" 
													style="width: 20px; text-align:right; border-right:0px" />
												<input id="transTpid"  class="normalInput"
													onchange=tpidChanged(this.id) maxlength="4" type="text" value="8100" 
													style="width: 40px; border-left:0px; border-right:0px;" />
											</td>
			                            </tr>
			                        </table>
			                    </fieldset>
			                </td>
			            </tr>
			        </table>
			    </td>
			</tr>
            <tr>
                <td>
                    <fieldset style='width: 507px;'>
                        <legend>@VLAN.ruleManu@</legend>
                        <table>
                            <tr>
                                <td class="rightBlueTxt w80">@VLAN.originVlan@ ID:</td>
                                <td><input id="beforeTransId" type="text" class="normalInput w100"
                                	tooltip='@COMMON.range4094@' maxlength="4" />
                                </td>
                                <td class="rightBlueTxt w110">@VLAN.translatedVlan@ ID:</td>
                                <td><input id="afterTransId" type="text" class="normalInput w100"
                                    tooltip='@COMMON.range4094@' maxlength="4" />
                                </td>
                                <td class="rightBlueTxt w80">
                                    <button id="transSubmit" class=BUTTON75 onclick="transOkClick()" disabled>@COMMON.confirm@</button>
                                </td>
                            </tr>
                        </table>
                    </fieldset>
                </td>
            </tr>
        </table>
    </div>
</div>
<div id="ponVlanNto1Tab" class="x-hide-display">
    <div
            style="overflow: auto; padding-left: 10px; padding-top: 10px;">
        <table cellspacing=5>
            <tr>
                <td>
                    <table>
                        <tr>
                            <td>
                                <fieldset style='width: 300px; height: 238px;'>
                                    <legend>@VLAN.aggregatedVlan@</legend>
                                    <div id='aggrSvlan_grid'></div>
                                </fieldset>
                            </td>
                            <td>
                                <fieldset style='width: 205px; height: 238px;'>
                                    <legend id='aggrlegend'>@VLAN.aggregatedVlanList@</legend>
                                    <div id='aggrCvlan_grid'></div>
                                </fieldset>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
			<tr>
			    <td>
			        <table>
			            <tr>
			                <td>
			                    <fieldset style='width: 507px; height: 45px;'>
			                        <legend>@VLAN.outerVlanPri@</legend>
			                        <table>
			                            <tr>
			                                <td>
			                                    <input id="aggrCosCheckbox2" type="radio" name="aggrCos" value="2"
			                                           onclick="aggrCosModeClick()" checked/>@VLAN.useBeforeAggPri@
			                                    <input id="aggrCosCheckbox1" type="radio" name="aggrCos" value="1"
			                                           onclick="aggrCosModeClick()"/>@VLAN.useNewVlanPri@
			                                    <select id="aggrCos" disabled class="normalSel w50">
			                                        <option value="0">0</option>
			                                        <option value="1">1</option>
			                                        <option value="2">2</option>
			                                        <option value="3">3</option>
			                                        <option value="4">4</option>
			                                        <option value="5">5</option>
			                                        <option value="6">6</option>
			                                        <option value="7">7</option>
			                                    </select>
			                                </td>
			                                <td>@VLAN.aggregatedTpid@: 
												<input id="aggrTpid0x" type="text" disabled value="0x" 
													style="width: 20px; text-align:right; border-right:0px" /><input id="aggrTpid" 
													onchange=tpidChanged(this.id) maxlength="4" type="text" value="8100" 
													style="width: 40px; border-left:0px" />
											</td>
			                            </tr>
			                        </table>
			                    </fieldset>
			                </td>
			            </tr>
			        </table>
			    </td>
			</tr> 
            <tr>
                <td>
                    <table>
                        <tr>
                            <td>
                                <fieldset style='width: 507px;'>
                                    <legend>@VLAN.ruleManu@</legend>
                                    <table>
                                        <tr>
                                            <td class="rightBlueTxt w110">@VLAN.aggVlan@ ID:</td>
                                            <td><input id="aggregationSvlanId" class="normalInput w60"
                                                       type="text" maxlength="4" tooltip='@COMMON.range4094@' 
                                                       onChange="aggregationSvlanIdChange()" />
                                            </td>
                                            <td class="rightBlueTxt w110">@VLAN.aggVlanBef@ ID:</td>
                                            <td><input id="aggregationCvlanId" type="text" class="normalInput w100"
                                                       tooltip='@VLAN.filterRule@' onfocus="aggregationCvlanIdOn()"/>
                                            </td>
                                            <td>
                                                <button id=aggrSubmit class=BUTTON75 disabled  onclick="aggrAdd()">@VLAN.add@</button>
                                            </td>
                                        </tr>
                                    </table>
                                </fieldset>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>
</div>
<div id="ponVlanTrunkTab" class="x-hide-display">
    <div
            style="overflow: auto; padding-left: 10px; padding-top: 10px;">
        <table cellspacing=10 style="margin-left: 50px">
            <tr>
                <td colspan="2">
                    <fieldset style='width: 400px; height: 285px;'>
                        <legend>@VLAN.trunkList@</legend>
                        <div id="trunkIdList"></div>
                    </fieldset>
                </td>
            </tr>
            <tr>
                <td>
                    <fieldset>
                        <legend>@VLAN.ruleManu@</legend>
                        <table>
                            <tr>
                                <td class="rightBlueTxt w110">Trunk VLAN ID:</td>
                                <td><input id="trunkId" type="text" class="normalInput"
	                                    onkeyup="this.value=this.value.replace(/[^\d\S,-]/g,'');"
	                                    tooltip='@VLAN.filterRule@'/>
                                </td>
                                <td class="rightBlueTxt w110">
                                    <button id="trunkSubmit" class=BUTTON75
                                            onclick="trunkOkClick()" disabled>@VLAN.add@
                                    </button>
                                </td>
                            </tr>
                        </table>
                    </fieldset>
                </td>
            </tr>
        </table>
    </div>
</div>
<div id="ponVlanQinqTab" class="x-hide-display">
<div style="overflow: auto; padding-left: 10px; padding-top: 10px;">
<table cellspacing=5>
<tr>
    <td>
        <table>
            <tr>
                <td>
                    <fieldset style='width: 150px; height: 238px;'>
                        <legend>@VLAN.outerVlanList@</legend>
						<div id='qinqSvlan_grid'></div>
                    </fieldset>
                </td>
                <td>
                    <fieldset style='width: 355px; height: 238px;'>
                        <legend id='qinqlegend'>@VLAN.add2OuterVlanList@</legend>
						<div id='qinqCvlan_grid'></div>
                    </fieldset>
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr>
    <td>
        <table>
            <tr>
                <td>
                    <fieldset style='width: 507px; height: 45px;'>
                        <legend>@VLAN.outerVlanPri@</legend>
                        <table>
                            <tr>
                                <td>
                                    <input id="qinqCosCheckbox2" type="radio" name="qinqCos" value="2"
                                           onclick="qinqCosModeClick()" checked/>@VLAN.useInnerVlanPri@
                                    <input id="qinqCosCheckbox1" type="radio" name="qinqCos" value="1"
                                           onclick="qinqCosModeClick()"/>@VLAN.useNewVlanPri@
                                    <select id="qinqCos" disabled style="width:40px;">
                                        <option value="0">0</option>
                                        <option value="1">1</option>
                                        <option value="2">2</option>
                                        <option value="3">3</option>
                                        <option value="4">4</option>
                                        <option value="5">5</option>
                                        <option value="6">6</option>
                                        <option value="7">7</option>
                                    </select>
                                </td>
                                <td class="rightBlueTxt">@VLAN.outerTpid@:
									<input id="qinqTpid0x" type="text" disabled value="0x" 
										style="width: 20px; text-align:right; border-right:0px" /><input 
										id="qinqTpid" onchange=tpidChanged(this.id) maxlength="4" type="text" value="8100" 
										style="width: 40px; border-left:0px" />
								</td>
                            </tr>
                        </table>
                    </fieldset>
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr>
    <td>
        <table>
            <tr>
                <td>
                    <fieldset style='width: 507px;'>
                        <legend>@VLAN.ruleManu@</legend>
                        <table>
                            <tr>
                                <td class="rightBlueTxt w100">@VLAN.outerVlan@ ID:</td>
                                <td>
                                     <input id="qinqOuterVlanId" class="normalInput w50"
                                            type="text" maxlength="4" tooltip='@COMMON.range4094@'
                                            onchange="qinqOuterIdChange()"/>
                                </td>
                                <td class="rightBlueTxt w100">Start VLAN ID:</td>
                                <td><input id="qinqInnerVlanStartId" class="normalInput w50"
                                           type="text" onfocus="qinqInnerIdOn()"
                                           tooltip='@COMMON.range4094@' maxlength='4' />
                                </td>
                                <td class="rightBlueTxt w100">End VLAN ID:</td>
                                <td><input id="qinqInnerVlanEndId" class="normalInput w50"
	                                      type="text" onfocus="qinqInnerIdOn()"
	                                      tooltip='@COMMON.range4094@' maxlength='4' disabled />
                                </td>
                                <td>
                                    <button id='qinqSubmit' class=BUTTON75 disabled onclick="qinqAdd()">@VLAN.add@</button>
                                </td>
                            </tr>
                        </table>
                    </fieldset>
                </td>
            </tr>
        </table>
    </td>
</tr>
</table>
</div>
</div>
</td>
</tr>
</table>
</div>
</Td>
<td>
<div>
<table>
<tr>
<td>
    <div id="tip">
        <table>
            <tr>
                <td>
                    <div id="tip1Tab" class="x-hide-display">
                        <div style="overflow: auto; padding-left: 10px; padding-top: 10px;">
                            <table cellspacing=0>
                                <tr>
                                    <td>
                                        <fieldset>
                                            <legend id="shuoming">@VLAN.trans@</legend>
                                        </fieldset>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                    <div id="tip2Tab" class="x-hide-display">
                        <div style="overflow: auto; padding-left: 10px; padding-top: 10px;">
                            <table cellspacing=0>
                                <tr>
                                    <td>
                                        <fieldset>
                                            <legend>@VLAN.cvidList@</legend>
                                            <div id="cvidGrid"></div>
                                        </fieldset>
                                    </td>
                                </tr>
                                <tr>
                                	<td>
                                		<fieldset>
                                			<legend>
                                				@VLAN.cvidQueryFilter@
                                			</legend>
                                            <table>
                                                <tr>
                                                    <td class="blueTxt w180">CVID:
					                                    <input id="CvidSearchId"  class="normalInput w100"
					                                       type="text" onkeyup="cvidKeyup()" 
					                                       title='@COMMON.range4094@' maxlength='4' />
                                                    </td>
                                                </tr>
                                            </table>
                                        </fieldset>
                                	</td>
                                </tr>
                            </table>
                        </div>
                    </div>
                    <div id="tip3Tab" class="x-hide-display">
                        <div style="overflow: auto; padding-left: 10px; padding-top: 10px;">
                            <table cellspacing=0>
                                <tr>
                                    <td>
                                        <fieldset>
                                            <legend>@VLAN.svidList@</legend>
                                            <div id="svidGrid"></div>
                                        </fieldset>
                                    </td>
                                </tr>
                                <tr>
                                	<td>
                                		<fieldset>
                                			<legend>@VLAN.svidQueryFilter@</legend>
                                            <table>
                                                <tr>
                                                    <td class="blueTxt w180">SVID:
					                                    <input id="SvidSearchId" class="normalInput w100"
					                                       type="text" onkeyup="svidKeyup()" 
					                                       title='@COMMON.range4094@' maxlength='4' />
                                                    </td>
                                                </tr>
                                            </table>
                                        </fieldset>
                                	</td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </td>
            </tr>
        </table>
</div>
</td>
</tr>
</table>
</div>
</td>
</tr>
</table>
<Zeta:ButtonGroup>
	<Zeta:Button id="saveBt" onClick="LlidModeChange()" icon="miniIcoSaveOK">@VLAN.ponBased@</Zeta:Button>
	<Zeta:Button id="vidUsageBt" onClick="getDataFormDevice()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
	<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
</Zeta:ButtonGroup>
  
<div id=loadingDiv style="position:absolute;background-color:#ffffbe;left:800;top:100;display:none;" >@COMMON.loading@</div>
</body>
<style>
body{ overflow:hidden; width:100%;}
#loadingMask {
	position: absolute;
	left: 0px;
	top: 0px;
	width:1000px;
	height: 448px;
	background-color: #ffffff;
	z-index: 100000;
	filter:alpha(opacity=20);
	opacity:0.2;
}
.loadingText {
	position: absolute;
	left: 350px;
	top: 129px;
	font-size: 10;
	color:'white';
}
.loadingImage {
	position: absolute;
	left: 310px;
	top: 120px;
	height: 32px;
	width: 32px;
}
</style>
</Zeta:HTML>