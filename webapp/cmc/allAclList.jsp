<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML vmlSupport="true">
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
    library jquery
    library zeta
    module cmc
    CSS css/white/disabledStyle
    import js.tools.ipText
    import cmc.allAclList
</Zeta:Loader>
<style type="text/css">
.actClazz {
	background-image: url(../images/icons/fileList.png) !important;
}

.lableClazz {
	text-align: right;
	padding-left: 0px;
	padding-right: 5px;
}

.inputClazz {
	text-align: left;
	padding-left: 10px;
	padding-right: 5px;
}

.blueTxt div{
    padding:2px 0px 2px 0px;
}

.seclectedPosition {
	background-image: url(../images/monitor/monitorBack.png);
}
</style>
</head>
<script type="text/javascript">
var isNewAclActionMask = '${isNewAclActionMask}';
var cmcId = ${cmcId};
vcEntityKey = 'cmcId';
//定义一个全局变量，保存所有动作的check id和input id
var checkIDArray = [
	{checkID:'aclVlanActionReplaceVlanID',
    inputIDs:['topActionNewVlanId']
    },
    {checkID:'aclVlanActionReplaceVlanCos',
    inputIDs:['topActionNewVlanCos']
    },
    {checkID:'aclVlanActionReplaceVlanTpid',
    inputIDs:['topActionNewVlanTpid']
    },
    {checkID:'aclVlanActionReplaceVlanCfi',
    inputIDs:['topActionNewVlanCfi']
    },
    {checkID:'aclVlanActionNewVlan',
    inputIDs:['topActionAddVlanId','topActionAddVlanCos','topActionAddVlanTpid','topActionAddVlanCfi']
    },
    {checkID:'aclActionReplaceIpTos',
    inputIDs:['topActionNewIpTos']
    },
    {checkID:'aclActionReplaceIpDscp',
    inputIDs:['topActionNewIpDscp']
    },
    {checkID:'aclVlanActionRemoveVlan',
        inputIDs:[]
    }];

var aclInfoPanel = new Ext.Panel({
    id:'aclInfoPanel',
    aclID:null,
    autoScroll:'auto',
    contentEl:'singleAclInfoDiv',
    region: 'center',
    reload:function(aclID){
        this.aclID = aclID;
        $('#normalAclInfoDiv').attr('style','display:block');
        $('#defActInfoDiv').attr('style','display:none');
        //$('#topCcmtsAclListIndex').attr("readOnly",true);
        
        var urlStr = "/cmcacl/loadSingleAcl.tv";
        Ext.Ajax.request({
            url : urlStr,
            success : function(response) {
                //alert(response.responseText);
                var aclObj = Ext.util.JSON.decode(response.responseText);
                //从后台查询一个acl的信息，在页面显示
                
                if(aclObj.topCcmtsAclListIndex=="0"){
                    //不能自动生成aclID,不能再增加ACL
                    $('#saveBt').attr('disabled',true);
                    window.parent.showMessageDlg('@COMMON.tip@', '@CMC.label.aclOverMaxCountCreatError@');
                }else{
                    $('#topCcmtsAclListIndex').val(aclObj.topCcmtsAclListIndex);
                    if(operationDevicePower){
	                	$('#saveBt').attr('disabled',false);
                    }
                    
	                $('#topCcmtsAclPrio').val(aclObj.topCcmtsAclPrio);
	                $('#topCcmtsAclDesc').val(aclObj.topCcmtsAclDesc);
	
	                //放置点
	                $("#installPosionUpOut").attr("checked",(aclObj.installPosionUpOut==1));
	                $("#installPosionUpIn").attr("checked",(aclObj.installPosionUpIn==1));
	                $("#installPosionCaOut").attr("checked",(aclObj.installPosionCaOut==1));
	                $("#installPosionCaIn").attr("checked",(aclObj.installPosionCaIn==1));
	
	                $("input[type='hidden'][name='aclInfo.installPosionUpOut']").val((aclObj.installPosionUpOut==1)?"1":"0");
	                $("input[type='hidden'][name='aclInfo.installPosionUpIn']").val((aclObj.installPosionUpIn==1)?"1":"0");
	                $("input[type='hidden'][name='aclInfo.installPosionCaOut']").val((aclObj.installPosionCaOut==1)?"1":"0");
	                $("input[type='hidden'][name='aclInfo.installPosionCaIn']").val((aclObj.installPosionCaIn==1)?"1":"0");
	
	                //匹配规则
	              	/*  if(aclObj.topMatchSrcIp!="0.0.0.0"){
	                    setIpValue("topMatchSrcIp", aclObj.topMatchSrcIp);
	                } */
	                if(aclObj.topMatchSrcIpMask!="0.0.0.0"){
	                	setIpValue("topMatchSrcIp", aclObj.topMatchSrcIp);
	                 	setIpValue("topMatchSrcIpMask", aclObj.topMatchSrcIpMask);
	                }
	                /* if(aclObj.topMatchDstIp!="0.0.0.0"){
	                 setIpValue("topMatchDstIp", aclObj.topMatchDstIp);
	                } */
	                if(aclObj.topMatchDstIpMask!="0.0.0.0"){
	                	setIpValue("topMatchDstIp", aclObj.topMatchDstIp);
	                 	setIpValue("topMatchDstIpMask", aclObj.topMatchDstIpMask);
	                }
	
	                /* if(aclObj.topMatchlSrcMac!="00:00:00:00:00:00"){
	                 $('#topMatchlSrcMac').val(aclObj.topMatchlSrcMac);
	                } */
	                if(Validator.formatMac(aclObj.topMatchSrcMacMask)!="00:00:00:00:00:00"){
	                	$('#topMatchlSrcMac').val(aclObj.topMatchlSrcMac);
	                 	$('#topMatchSrcMacMask').val(aclObj.topMatchSrcMacMask);
	                }
	                /* if(aclObj.topMatchDstMac!="00:00:00:00:00:00"){
	                 $('#topMatchDstMac').val(aclObj.topMatchDstMac);
	                } */
	                if(Validator.formatMac(aclObj.topMatchDstMacMask)!="00:00:00:00:00:00"){
	                	$('#topMatchDstMac').val(aclObj.topMatchDstMac);
	                 	$('#topMatchDstMacMask').val(aclObj.topMatchDstMacMask);
	                }
	
	                if(aclObj.topMatchSrcPort!="65536"){
	                	$('#topMatchSrcPort').val(aclObj.topMatchSrcPort);
	                }
	                if(aclObj.topMatchDstPort!="65536"){
	                	$('#topMatchDstPort').val(aclObj.topMatchDstPort);
	                }
	                if(aclObj.topMatchVlanId!="4095"){
	                	$('#topMatchVlanId').val(aclObj.topMatchVlanId);
	                }
	                if(aclObj.topMatchVlanCos!="8"){
	                	$('#topMatchVlanCos').val(aclObj.topMatchVlanCos);
	                }
	                if(aclObj.topMatchEtherType!="0"){
	                	$('#topMatchEtherType').val(aclObj.topMatchEtherType);
	                }
	                if(aclObj.topMatchIpProtocol!="256"){
	                	$('#topMatchIpProtocol').val(aclObj.topMatchIpProtocol);
	                }
	                if(aclObj.topMatchDscp!="64"){
	               		$('#topMatchDscp').val(aclObj.topMatchDscp);
	                }
	                //动作
	                if(aclObj.actDenyOrPermit==2){
	                    $("input[type=radio][name=aclInfo.actDenyOrPermit][value=2]").attr("checked","checked");
	                    matchActionChanged(2);
	                }else if(aclObj.actDenyOrPermit==1){
	                    $("input[type=radio][name=aclInfo.actDenyOrPermit][value=1]").attr("checked","checked");
	                    matchActionChanged(1);
	                    if(aclObj.actRepVlanId==1){
	                        $('#aclVlanActionReplaceVlanID').attr("checked",true);
	                        $('#topActionNewVlanId').val(aclObj.topActionNewVlanId);
	                        $('#topActionNewVlanId').attr("disabled",false);
	                    }else if(aclObj.actReplaceCos==1){
	                        $('#aclVlanActionReplaceVlanCos').attr("checked",true);
	                        $('#topActionNewVlanCos').val(aclObj.topActionNewVlanCos);
	                        $('#topActionNewVlanCos').attr("disabled",false);
	                    }else if(aclObj.actRepVlanTpid==1){
	                        $('#aclVlanActionReplaceVlanTpid').attr("checked",true);
	                        $('#topActionNewVlanTpid').val(aclObj.hexTopActionNewVlanTpid);
	                        $('#topActionNewVlanTpid').attr("disabled",false);
	                    }else if(aclObj.actRepVlanCfi==1){
	                        $('#aclVlanActionReplaceVlanCfi').attr("checked",true);
	                        $('#topActionNewVlanCfi').val(aclObj.topActionNewVlanCfi);
	                        $('#topActionNewVlanCfi').attr("disabled",false);
	                    }else if(aclObj.actAddVlan==1){
                            $('#aclVlanActionNewVlan').attr("checked",true);
                            $('#topActionAddVlanCos').val(aclObj.topActionAddVlanCos);
                            $('#topActionAddVlanId').val(aclObj.topActionAddVlanId);
                            $('#topActionAddVlanTpid').val(aclObj.hexTopActionAddVlanTpid);
                            $('#topActionAddVlanCfi').val(aclObj.topActionAddVlanCfi);

                            $('#topActionAddVlanCos').attr("disabled",false);
                            $('#topActionAddVlanId').attr("disabled",false);
                            $('#topActionAddVlanTpid').attr("disabled",false);
                            $('#topActionAddVlanCfi').attr("disabled",false);
	                    }else if(aclObj.actRemoveVlan==1){
                            $('#aclVlanActionRemoveVlan').attr("checked",true);
	                    }else if(aclObj.actRepIpTos==1){
	                        $('#aclActionReplaceIpTos').attr("checked",true);
	                        $('#topActionNewIpTos').val(aclObj.topActionNewIpTos);
	                        $('#topActionNewIpTos').attr("disabled",false);
	                    }else if(aclObj.actRepIpDscp==1){
	                        $('#aclActionReplaceIpDscp').attr("checked",true);
	                        $('#topActionNewIpDscp').val(aclObj.topActionNewIpDscp);
	                        $('#topActionNewIpDscp').attr("disabled",false);
	                    }
	
	                    if(aclObj.actCopyToCpu==1){
	                        $('#aclCPUActionMask').attr("checked",true);
	                    }   
	                }else{
                        $("input[type=radio][name=aclInfo.actDenyOrPermit][value=0]").attr("checked","checked");
                        matchActionChanged(0);
	                }
                
	                var isInstall = false;
	                if(aclObj.installPosionUpOut==1||aclObj.installPosionUpIn==1||aclObj.installPosionCaOut==1||aclObj.installPosionCaIn==1){
	                    isInstall = true;
	                    for(var i=0;i<checkIDArray.length;i++){
	                        var checkObj = checkIDArray[i];
	                        var inputArray = checkObj.inputIDs;
	                               $('#'+checkObj.checkID).attr("disabled",isInstall);
	                               for(var m=0;m<inputArray.length;m++){
	                                   $('#'+inputArray[m]).attr("disabled",isInstall);
	                               }
	                        }
	                    $('#aclCPUActionMask').attr("disabled",isInstall);
	                }
	               
	                $('#topCcmtsAclListIndex').attr("disabled",isInstall);
                	//已经安装到至少一个放置点,则除了放置点,其他任何数据都禁止修改.
                	$('input[type=radio][name=aclInfo.actDenyOrPermit]').attr("disabled",isInstall);
                
                    $('#topCcmtsAclPrio').attr("disabled",isInstall);
                    $('#topCcmtsAclDesc').attr("disabled",isInstall);
                    
                    setIpEnable("topMatchSrcIp", !isInstall);
                    setIpEnable("topMatchSrcIpMask", !isInstall);
                    setIpEnable("topMatchDstIp", !isInstall);
                    setIpEnable("topMatchDstIpMask", !isInstall);

                    $('#topMatchlSrcMac').attr("disabled",isInstall);
                    $('#topMatchSrcMacMask').attr("disabled",isInstall);
                    $('#topMatchDstMac').attr("disabled",isInstall);
                    $('#topMatchDstMacMask').attr("disabled",isInstall);

                    $('#topMatchSrcPort').attr("disabled",isInstall);
                    $('#topMatchDstPort').attr("disabled",isInstall);

                    $('#topMatchVlanId').attr("disabled",isInstall);
                    $('#topMatchVlanCos').attr("disabled",isInstall);
                    $('#topMatchEtherType').attr("disabled",isInstall);
                    $('#topMatchIpProtocol').attr("disabled",isInstall);
                    $('#topMatchDscp').attr("disabled",isInstall);
                    
                    $('#clearBt').attr("disabled",isInstall);
                }
            },
            failure : function() {
                window.parent.showMessageDlg('@COMMON.tip@', '@CMC.text.datageterror@');
            },
            params : {
                cmcId: cmcId,
                aclID:this.aclID
            }
        });
	}
});

var allAclStore = new Ext.data.JsonStore({
    url: '/cmcacl/loadAllAclList.tv?cmcId='+cmcId,
    root: 'data',
    totalProperty: 'rowCount',
    fields: ['defActPosion','defAct','topCcmtsAclListIndex', 'topCcmtsAclDesc','topCcmtsAclPrio',  'actDenyOrPermit', 'actReplaceCos', 'actAddVlan', 'actRemoveVlan','actCopyToCpu']
});
allAclStore.setDefaultSort('topCcmtsAclListIndex', 'ASC');

var aclGrid = new Ext.grid.GridPanel({
    store: allAclStore,
    bodyCssClass: "normalTable",
    columns: [
        {id:'aclIDCol',header: 'ACL ID', width: 60, sortable: true, dataIndex: 'topCcmtsAclListIndex',renderer:renderAclID},
        {id:'descriptionCol',header: '@CMC.label.aclDesc@', width: 150,align:'center',  dataIndex: 'topCcmtsAclDesc',renderer:renderDesc},
        {id:'actCol',header: '@CMC.label.aclAction@',align:'center', renderer: renderActCol},
        {header: '@CMC.label.aclPriority@', width: 60, sortable: true,dataIndex: 'topCcmtsAclPrio',renderer:renderPrority}
    ],
    columnLines:true,
    tbar:[{
         xtype: 'buttongroup',
         title: '@CMC.label.aclPositionUp@',
         columns: 2,
         defaults: {
             scale: 'small'
         },
         items: [{
             text: '@CMC.label.aclPositionOut@',
             id:'position2',
             iconCls: 'add16',
             handler:function(){
                 reloadOnePositionAclList(2);
             }
         },{

             text: '@CMC.label.aclPositionIn@',
             id:'position1',
             iconCls: 'add16',
             handler:function(){
                 reloadOnePositionAclList(1);
                
             }   
         }]
        },{ 
        	xtype: 'buttongroup',
            title: '@CMC.label.aclPositionCa@',
            columns: 2,
            defaults: {
                scale: 'small'
            },
            items: [{
                text: '@CMC.label.aclPositionOut@',
                id:'position3',
                iconCls: 'add16',
                handler:function(){
                    reloadOnePositionAclList(3);
                }   
            },{
                text: '@CMC.label.aclPositionIn@',
                id:'position4',
                iconCls: 'add16',
                handler:function(){
                    reloadOnePositionAclList(4);
                }
            }] 
        },{ 
        	xtype: 'buttongroup',
            title: '@CMC.button.aclOther@',
            columns: 1,
            defaults: {
            	scale: 'small'
            },
            items: [{
            	text: '@CMC.button.aclNotInstall@',
            	id:'position0',
            	iconCls: 'add16',
            	handler:function(){
           			reloadOnePositionAclList(0);
                }
            }] 
       }
        /* ,{ 
        	xtype: 'buttongroup',
         title: '@CMC.label.operation@',
         columns: 1,
         defaults: {
         	scale: 'small'
         },
         items: [{
         	text: '@CMC.label.refreshPage@',
         	id:'position5',
         	iconCls: 'bmenu_refresh',
         	handler:function(){
        			window.location.reload();
             }
         }]
    } */
    ],
    region: 'west',
    width:410,
    stripeRows: true,
    autoExpandColumn:'actCol',  
    autoExpandMin:70,
    listeners:{
        rowclick:function(grid,rowIndex,e){
            var record = allAclStore.getAt(rowIndex);
            var aclID = record.get("topCcmtsAclListIndex");
            if(aclID==0){
             var positionNum = record.get("defActPosion");
             var defAct = record.get("defAct");
             viewPositionDefAct(positionNum,defAct);
         }else{
             $("#aclInfoForm")[0].reset();
             aclInfoPanel.reload(aclID);
         }
        }
    }
});

Ext.onReady(function () {
    new Ext.Viewport({
        layout: "border",
        items: [{
            region: 'north',
            height:40,
            contentEl:'header'
            },aclGrid,aclInfoPanel]
     });
     reloadOnePositionAclList();
     var srcIp = new ipV4Input("topMatchSrcIp","srcIPSpan");
     srcIp.width(130);
     
     var srcIpMask = new ipV4Input("topMatchSrcIpMask","srcIPMaskSpan");
     srcIpMask.width(130);
     
     var dstIp = new ipV4Input("topMatchDstIp","dstIPSpan");
     dstIp.width(130);
     
     var dstIpMask = new ipV4Input("topMatchDstIpMask","dstIPMaskSpan");
     dstIpMask.width(130);

     matchActionChanged(0);
     //$('#topCcmtsAclListIndex').attr("readOnly",true);
     
     //leexiang备注，由于是ext布局，通过F12发现，滚动条来自$("#aclInfoPanel .x-panel-body");
     $("#aclInfoPanel .x-panel-body").scroll(function(){//滚动条滚动;
    	 if($("#nm3kToolTip:visible").length > 0){//如果toolTip标签是可见的,说明input是被focus的;
    	 	var inputHeight = $("#nm3kToolTip").outerHeight();
    		var inputTop = $(this).find("input:focus").offset().top - inputHeight;//获取现在被focus的input的top距离;
    	 	$("#nm3kToolTip").css("top",inputTop);
    	 }
     })
});
</script>
<body class="whiteToBlack" onload="authLoad()">
	<div id=header><%@ include file="entity.inc"%></div>
	<div class=formtip id=tips style="display: none"></div>
	<div id="singleAclInfoDiv">
		<div id="normalAclInfoDiv">			    
		    <div class="openWinHeader" style="height:80px;">
		        <div class="openWinTip">
		            <div><font style="font-weight: bold">@CMC.label.aclConfigInfo@</font></div>
		            <div class="clearBoth" style="padding-top: 15px; padding-botom: 10px;">
                            <ol class="upChannelListOl">
                            <li><a id="newBt" onclick="newAcl()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoAdd"></i>@CMC.button.create@</span></a></li>
                                <li><a id="saveBt" onclick="saveAcl()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoData"></i>@CMC.button.save@</span></a></li>
                                <li><a id="finishBt" onclick="deleteAcl()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoClose"></i>@CMC.button.delete@</span></a></li>
                                <li><a id="refreshBt" onclick="refreshAllAcl()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
                            </ol>
                       </div>
		        </div>
		        <div  class="rightCirIco wheelCirIco" style="top:18px;"></div>
		    </div>
			<form id="aclInfoForm">
				<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
				   <tbody>
					<tr>
						<td class="rightBlueTxt" width=160>ACL ID:</td>
						<td class="inputClazz blueTxt" width=150><input type="text"
								maxlength='3' id="topCcmtsAclListIndex" class="normalInput"
								tooltip="@CMC.tip.aclIDInput@" name="aclInfo.topCcmtsAclListIndex" />
						</td>
						<td class="rightBlueTxt" width=100>@CMC.label.aclPriority@:</td>
						<td class="inputClazz blueTxt"><select id="topCcmtsAclPrio"
							name="aclInfo.topCcmtsAclPrio" style="width: 130px;">
								<option value=0>0</option>
								<option value=1>1</option>
								<option value=2>2</option>
								<option value=3>3</option>
								<option value=4>4</option>
								<option value=5 selected>5</option>
								<option value=6>6</option>
								<option value=7>7</option>
								<option value=8>8</option>
								<option value=9>9</option>
								<option value=10>10</option>
								<option value=11>11</option>
								<option value=12>12</option>
								<option value=13>13</option>
								<option value=14>14</option>
								<option value=15>15</option>
						</select>
						</td>
					</tr>
					<tr class="darkZebraTr">
						<td class="rightBlueTxt">@CMC.label.aclDesc@:</td>
						<td class="inputClazz blueTxt"><textArea id="topCcmtsAclDesc"
								cols=22 rows=2 name="aclInfo.topCcmtsAclDesc"
								onfocus="inputFocused('topCcmtsAclDesc', '@CMC.tip.aclDesc@', 'topCcmtsAclDesc_focused')"
								onblur="inputBlured(this, 'topCcmtsAclDesc');"
								onclick="clearOrSetTips(this);" /></textarea>
						</td>
						<td colspan="2">
						</td>
					</tr>
                   </tbody>
                   </table>
                   <table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
                   <thead>
                       <tr>
                           <th colspan="4" class="txtLeftTh">@CMC.label.aclMatchRule@
                           </th>
                       </tr>
                   </thead>
                   <tbody>
					<tr>
						<td class="rightBlueTxt"  width=160>@CMC.label.aclMatchSrcIp@:</td>
						<td class="inputClazz blueTxt" width=150><span id="srcIPSpan"></span> <input
							type='hidden' name="aclInfo.topMatchSrcIp"/>
						</td>
						<td class="rightBlueTxt"  width=100>@CMC.label.aclMatchSrcIpMask@:</td>
						<td class="inputClazz blueTxt"><span id="srcIPMaskSpan"></span> <input
							type='hidden' name="aclInfo.topMatchSrcIpMask"/>
						</td>
					</tr>
					<tr class="darkZebraTr">
						<td class="rightBlueTxt">@CMC.label.aclMatchDstIp@:</td>
						<td class="inputClazz blueTxt"><span id="dstIPSpan"></span> <input
							type='hidden' name="aclInfo.topMatchDstIp"/>
						</td>
						<td class="rightBlueTxt">@CMC.label.aclMatchDstIpMask@:</td>
						<td class="inputClazz blueTxt"><span id="dstIPMaskSpan"></span> <input
							type='hidden' name="aclInfo.topMatchDstIpMask"/>
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@CMC.label.aclMatchSrcMac@:</td>
						<td class="inputClazz blueTxt"><input maxlength='17' class="normalInput" type="text"
								name="aclInfo.topMatchlSrcMac" id="topMatchlSrcMac"
								tooltip="@CMC.tip.aclMatchSrcMac@"
								value="${aclInfo.topMatchlSrcMac}" />
						</td>
						<td class="rightBlueTxt">@CMC.label.aclMatchSrcMacMask@:</td>
						<td class="inputClazz blueTxt"><input maxlength='17' class="normalInput" type="text"
								name="aclInfo.topMatchSrcMacMask" id="topMatchSrcMacMask"
								tooltip="@CMC.tip.aclMatchSrcMacMask@"
								value="${aclInfo.topMatchSrcMacMask}" />
						</td>
					</tr>
					<tr class="darkZebraTr">

						<td class="rightBlueTxt">@CMC.label.aclMatchDstMac@:</td>
						<td class="inputClazz blueTxt"><input maxlength='17' class="normalInput" type="text"
								name="aclInfo.topMatchDstMac" id="topMatchDstMac"
								tooltip="@CMC.tip.aclMatchDstMac@"
								value="${aclInfo.topMatchDstMac}" />
						</td>
						<td class="rightBlueTxt">@CMC.label.aclMatchDstMacMask@:</td>
						<td class="inputClazz blueTxt"><input maxlength='17' class="normalInput" type="text"
								name="aclInfo.topMatchDstMacMask" id="topMatchDstMacMask"
								tooltip="@CMC.tip.aclMatchDstMacMask@"
								value="${aclInfo.topMatchDstMacMask}" />
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">@CMC.label.aclMatchSrcPort@:</td>
						<td class="inputClazz blueTxt"><input maxlength='5' class="normalInput" type="text"
								name="aclInfo.topMatchSrcPort" id="topMatchSrcPort"
								tooltip="@CMC.tip.aclMatchSrcPort@"
								value="${aclInfo.topMatchSrcPort}" />
						</td>
						<td class="rightBlueTxt">@CMC.label.aclMatchDstPort@:</td>
						<td class="inputClazz blueTxt"><input maxlength='5' class="normalInput" type="text"
								name="aclInfo.topMatchDstPort" id="topMatchDstPort"
								tooltip="@CMC.tip.aclMatchDstPort@"
								value="${aclInfo.topMatchDstPort}" />
						</td>
					</tr>
					<tr class="darkZebraTr">
						<td class="rightBlueTxt">VLAN ID:</td>
						<td class="inputClazz blueTxt"><input maxlength='4' class="normalInput" type="text"
								name="aclInfo.topMatchVlanId" id="topMatchVlanId"
								tooltip="@CMC.tip.aclMatchVlanID@"
								value="${aclInfo.topMatchVlanId}" />
						</td>
						<td class="rightBlueTxt">VLAN COS:</td>
						<td class="inputClazz blueTxt"><input maxlength='1' class="normalInput" type="text"
								name="aclInfo.topMatchVlanCos" id="topMatchVlanCos"
								tooltip="@CMC.tip.aclMatchVlanCos@"
								value="${aclInfo.topMatchVlanCos}" />
						</td>
					</tr>
					<tr>
						<td class="rightBlueTxt">Ethernet type:</td>
						<td class="inputClazz blueTxt"><input maxlength='5' class="normalInput" type="text"
								name="aclInfo.topMatchEtherType" id="topMatchEtherType"
								tooltip="@CMC.tip.aclMatchEthernetType@"
								value="${aclInfo.topMatchEtherType}" />
						</td>
						<td class="rightBlueTxt">@CMC.label.aclMatchIpProtocol@:</td>
						<td class="inputClazz blueTxt"><input maxlength='3' class="normalInput" type="text"
								name="aclInfo.topMatchIpProtocol" id="topMatchIpProtocol"
								tooltip="@CMC.tip.aclMatchIpProtocol@"
								value="${aclInfo.topMatchIpProtocol}" />
						</td>
					</tr>
					<tr class="darkZebraTr">
						<td class="rightBlueTxt">DSCP:</td>
						<td class="inputClazz blueTxt"><input maxlength='2' class="normalInput" type="text"
								name="aclInfo.topMatchDscp" id="topMatchDscp"
								tooltip="@CMC.tip.aclMatchDscp@"
								value="${aclInfo.topMatchDscp}" />
						</td>
						<td></td>
						<td>
							<a class="normalBtn" href="javascript:;" onclick="clearMatchRules()" id="clearBt"><span><i class="miniIcoClose"></i>@CMC.acl.clearMatchRule@</span></a>
						</td>
					</tr>
				</tbody>
				</table>
				<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="row">
				<thead>
					<tr>
						<th colspan="5" class="txtLeftTh">@CMC.label.aclAction@
						</th>
					</tr>
				</thead>
				<tbody>
					<tr class="zebraTableRows">
                           <td class="rightBlueTxt" width=160></td>
                           <td class="inputClazz blueTxt" colspan=3>
                               <span id="aclActionPermit_container">
                                <input type=radio name="aclInfo.actDenyOrPermit" value="1" onclick="matchActionChanged(1);"/>
                                @CMC.label.aclActionPermit@&nbsp;&nbsp;&nbsp;&nbsp;
                               </span>
                               <span id="aclActionDeny_container">
                                <input type=radio name="aclInfo.actDenyOrPermit" value="2" onclick="matchActionChanged(2);"/>
                                @CMC.label.aclActionDeny@&nbsp;&nbsp;&nbsp;&nbsp;
                               </span>
                               <span id="noAction_container">
                                <input type=radio id="noAction" name="aclInfo.actDenyOrPermit" value="0" onclick="matchActionChanged(0);" checked/>
                                @CMC.label.aclActionNo@
                               </span>
                           </td>
                       </tr>
                       <tr class="zebraTableRows darkZebraTr">
                           <td class="rightBlueTxt"></td>
                           <td class="inputClazz blueTxt" width=10><input type=checkbox
                               name="aclInfo.actRepVlanId" id="aclVlanActionReplaceVlanID"
                               onclick="vlanActionCheckChanged(this);" value="1"/></td>
                           <td class="blueTxt" width=140>@CMC.label.aclActionReplaceVlanID@</td>
                           <td class="inputClazz blueTxt"><input type="text" class="normalInput"
                                   maxlength='4' id="topActionNewVlanId"
                                   name="aclInfo.topActionNewVlanId"
                                   tooltip="@CMC.tip.aclMatchVlanID@"
                                   value="${aclInfo.topActionNewVlanId}" />
                           </td>
                       </tr>
                       <tr class="zebraTableRows">
                           <td class="rightBlueTxt"></td>
                           <td class="inputClazz blueTxt"><input type=checkbox
                               name="aclInfo.actReplaceCos" id="aclVlanActionReplaceVlanCos"
                               onclick="vlanActionCheckChanged(this);" value="1"/></td>
                           <td class="blueTxt"> @CMC.label.aclActionReplaceVlanCos@</td>
                           <td class="inputClazz blueTxt"><input type="text" class="normalInput"
                                   maxlength='1' id="topActionNewVlanCos"
                                   name="aclInfo.topActionNewVlanCos"
                                   tooltip="@CMC.tip.aclActionReplaceVlanCos@"
                                   value="${aclInfo.topActionNewVlanCos}" />
                           </td>
                       </tr>
                       <tr class="zebraTableRows darkZebraTr">
                           <td class="rightBlueTxt"></td>
                           <td class="inputClazz blueTxt"><input type=checkbox
                               name="aclInfo.actRepVlanTpid" id="aclVlanActionReplaceVlanTpid"
                               onclick="vlanActionCheckChanged(this);" value="1"/>
                           </td>
                           <td class="blueTxt"> @CMC.label.aclActionReplaceVlanTpid@</td>
                           <td class="inputClazz blueTxt"><input type="text" class="normalInput"
                                   maxlength='4' id="topActionNewVlanTpid"
                                   name="aclInfo.hexTopActionNewVlanTpid"
                                   tooltip="@CMC.tip.topActionAddVlanTpid@"
                                   value="${aclInfo.hexTopActionNewVlanTpid}" />
                           </td>
                       </tr>
                       <tr class="zebraTableRows">
                           <td class="rightBlueTxt"></td>
                           <td class="inputClazz blueTxt"><input type=checkbox
                               name="aclInfo.actRepVlanCfi" id="aclVlanActionReplaceVlanCfi"
                               onclick="vlanActionCheckChanged(this);" value="1"/>
                           </td>
                           <td class="blueTxt">@CMC.label.aclActionReplaceVlanCfi@</td>
                           <td class="inputClazz blueTxt"><input type="text" class="normalInput"
                                   maxlength='1' id="topActionNewVlanCfi"
                                   name="aclInfo.topActionNewVlanCfi"
                                   tooltip="@CMC.tip.topActionAddVlanCfi@"
                                   value="${aclInfo.topActionNewVlanCfi}" />
                           </td>
                       </tr>
                       <tr class="zebraTableRows darkZebraTr">
                           <td class="rightBlueTxt"  rowspan="4" style="padding-top:1px; padding-bottom:1px;"></td>
                           <td class="inputClazz blueTxt" rowspan="4" style="padding-top:1px; padding-bottom:1px;"><input type=checkbox
                               name="aclInfo.actAddVlan" id="aclVlanActionNewVlan"
                               onclick="vlanActionCheckChanged(this);" value="1"/>
                           </td>
                           <td class="blueTxt" style="border:none; padding-top:1px; padding-bottom:1px;">  
                                <div>@CMC.label.aclActionAddVlanID@</div>
                           </td>
                           <td class="inputClazz blueTxt" style="border:none; padding-top:1px; padding-bottom:1px;">
                               <div>
                               	<input type="text" class="normalInput"
                                   	maxlength='4' id="topActionAddVlanId"
                                   	tooltip="@CMC.tip.aclMatchVlanID@"
                                   	name="aclInfo.topActionAddVlanId"
                                   	value="${aclInfo.topActionAddVlanId}" /> 
                               </div>
                           </td>
                       </tr>
                       <tr class="zebraTableRows darkZebraTr" style="background:#F4F4F4;">
                       	<td class="blueTxt" style="border:none; padding-top:1px; padding-bottom:1px;"> 
                       		<div>@CMC.label.aclActionAddVlanCos@</div>
                       	</td>
                       	<td class="inputClazz blueTxt" style="border:none; padding-top:1px; padding-bottom:1px;">
                       		<div>
                       			<input type="text" class="normalInput"
                                   maxlength='1' id="topActionAddVlanCos"
                                   tooltip="@CMC.tip.aclActionAddVlanCos@"
                                   name="aclInfo.topActionAddVlanCos"
                                   value="${aclInfo.topActionAddVlanCos}" />
                               </div>
                       	</td>
                       </tr>
                       <tr class="zebraTableRows darkZebraTr" style="background:#F4F4F4;">
                       	<td class="blueTxt" style="border:none; padding-top:1px; padding-bottom:1px;"> 
                       		 <div>@CMC.label.aclActionAddVlanTpid@</div>
                       	</td>
                       	<td class="inputClazz blueTxt" style="border:none; padding-top:1px; padding-bottom:1px;">
                       		<div>
                       			<input type="text" class="normalInput"
                                   maxlength='4' id="topActionAddVlanTpid"
                                   tooltip="@CMC.tip.topActionAddVlanTpid@"
                                   name="aclInfo.hexTopActionAddVlanTpid"
                                   value="${aclInfo.hexTopActionAddVlanTpid}" />
                               </div>
                       	</td>
                       </tr>
                       <tr class="zebraTableRows darkZebraTr" style="background:#F4F4F4;">
                       	<td class="blueTxt" style="border:none; padding-top:1px; padding-bottom:1px;"> 
                       		<div>@CMC.label.aclActionAddVlanCfi@</div>
                       	</td>
                       	<td class="inputClazz blueTxt" style="border:none; padding-top:1px; padding-bottom:1px;">
                       		<div>
                       			<input type="text" class="normalInput"
                                   maxlength='1' id="topActionAddVlanCfi"
                                   tooltip="@CMC.tip.topActionAddVlanCfi@"
                                   name="aclInfo.topActionAddVlanCfi"
                                   value="${aclInfo.topActionAddVlanCfi}" />
                               </div>
                       	</td>
                       </tr>
                       <tr class="zebraTableRows">
                           <td class="rightBlueTxt"></td>
                           <td class="inputClazz blueTxt"><input type=checkbox
                               name="aclInfo.actRemoveVlan" id="aclVlanActionRemoveVlan"
                               onclick="vlanActionCheckChanged(this);" value="1"/></td>
                           <td class="blueTxt"> @CMC.label.aclActionRemoveVlanTag@</td>
                           <td colspan=2></td>
                       </tr>
                       <tr class="zebraTableRows darkZebraTr">
                           <td class="rightBlueTxt"></td>
                           <td class="inputClazz blueTxt"><input type=checkbox
                               name="aclInfo.actRepIpTos" id="aclActionReplaceIpTos"
                               onclick="vlanActionCheckChanged(this);" value="1"/></td>
                           <td class="blueTxt"> @CMC.label.aclActionReplaceIpTos@</td>
                           <td class="inputClazz blueTxt"><input type="text" class="normalInput"
                                   maxlength='3' id="topActionNewIpTos"
                                   name="aclInfo.topActionNewIpTos"
                                   tooltip="@CMC.tip.topActionNewIpTos@"
                                   value="${aclInfo.topActionNewIpTos}" />
                           </td>
                       </tr>
                       <tr class="zebraTableRows">
                           <td class="rightBlueTxt"></td>
                           <td class="inputClazz blueTxt"><input type=checkbox
                               name="aclInfo.actRepIpDscp" id="aclActionReplaceIpDscp"
                               onclick="vlanActionCheckChanged(this);" value="1"/></td>
                           <td class="blueTxt"> @CMC.label.aclActionReplaceIpDscp@</td>
                           <td class="inputClazz blueTxt"><input type="text" class="normalInput"
                                   maxlength='2' id="topActionNewIpDscp"
                                   name="aclInfo.topActionNewIpDscp"
                                   tooltip="@CMC.tip.topActionNewIpDscp@"
                                   value="${aclInfo.topActionNewIpDscp}" />
                           </td>
                       </tr>
                       <tr class="zebraTableRows darkZebraTr">
                           <td class="rightBlueTxt"></td>
                           <td class="inputClazz blueTxt"><input id="aclCPUActionMask"
                               name="aclInfo.actCopyToCpu" type=checkbox
                               name="aclInfo.aclCPUActionMask" value="1"/></td>
                           <td class="blueTxt"> @CMC.label.aclActionCopyToCPU@</td>
                           <td></td>
                       </tr>
				</tbody>
                   </table>
                   
                   <table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
                   <thead>
                       <tr>
                           <th colspan="5" class="txtLeftTh">@CMC.label.aclPosition@
                           </th>
                       </tr>
                   </thead>
                   <tbody>
					<tr class="zebraTableRows">
						<td class="rightBlueTxt" width=160px></td>
						<td width="10">
							<input type=checkbox
							id="installPosionUpOut" value="1" disabled />
						</td>							
						<td class="inputClazz blueTxt" width="150px">@CMC.label.aclPositionUpOut@</td>
						<td width="10">
							<input type=checkbox
							id="installPosionUpIn" value="1" disabled />
						</td>
						<td class="inputClazz blueTxt">@CMC.label.aclPositionUpIn@</td>
					</tr>
					<tr class="blueTxtTr darkZebraTr">
						<td class="rightBlueTxt"></td>
						<td width="10">
							<input type=checkbox
							id="installPosionCaOut" value="1" disabled />
						</td>
						<td class="inputClazz blueTxt">@CMC.label.aclPositionCaOut@</td>
						<td width="10">
							<input type=checkbox
							id="installPosionCaIn" value="1" disabled />
						</td>
						<td class="inputClazz blueTxt">@CMC.label.aclPositionCaIn@</td>

					</tr>
					<tr class="zebraTableRows">
						<td class="inputClazz blueTxt">
							<input type='hidden' name="aclInfo.installPosionUpOut" />
							<input type='hidden' name="aclInfo.installPosionUpIn" /> 
							<input type='hidden' name="aclInfo.installPosionCaOut" /> 
							<input type='hidden' name="aclInfo.installPosionCaIn" />
						</td>
						<td colspan="4"></td>
					</tr>
					</tbody>
				</table>
			</form>
		</div>
		<div id="defActInfoDiv" style="display: none">
			<table width=100% height=60 cellspacing=0 cellpadding=0>
				<tr>
					<td class=WIZARD-HEADER style="padding-right: 20px;">
						<table width=100%>
							<tr>
								<td><font style="font-weight: bold"
									id="positionNameLabel">@CMC.label.aclPositionDefAct@<br>
									<br>
									<button id="savedefBt" class="BUTTON75"
										onclick="modifyPositionDefAct()">@CMC.button.save@</button>&nbsp;
								</td>
								<td align=right><img src="../images/icons/fileList.png"
									border=0 width=50 height=50></td>
							</tr>
						</table></td>
				</tr>
			</table>
			<form id="defaultActForm">
				<table width="100%">
					<tr>
						<td class="rightBlueTxt">@CMC.label.aclAction@:</td>
						<input type='hidden' name="defAct.topAclPositionIndex">
						<td class="inputClazz blueTxt"><input type=radio
							name="defAct.topPositionDefAction" value="1">@CMC.label.aclActionPermit@&nbsp;&nbsp;&nbsp;&nbsp;
							<input type=radio name="defAct.topPositionDefAction" value="0">@CMC.label.aclActionDeny@
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</Zeta:HTML>