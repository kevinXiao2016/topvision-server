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
	plugin LovCombo
    module cmc
</Zeta:Loader>
<style type="text/css">
#LB_MAC_RANGE {height: 100px;border: 1px solid #6593cf;background-color: #fff;overflow-y: scroll;}
</style>
<script type="text/javascript">
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var macDisplayFormat = '${macDisplayFormat}';
var DOC = document,
	WIN = window,
	WIN_WIDTH;
var cmcId = '${cmcId}',
	ranges = [],
	grpId = '${grpId}',
	pageAction = '${pageAction}',
	cmcLoadBalanceGroup = ${cmcLoadBalanceGroup};
//不能输入特殊字符。是否为空或是含有特殊字符
function validateName(str){
	var reg = /^[a-zA-Z0-9\u4e00-\u9fa5]+$/
	if(str.indexOf(" ")>-1 || !reg.test(str)){
		return true;
	}else{
		return false;
	}
}
function dealRanges(){
	var rangesStr = "";
	$.each(ranges,function(i,e){
		rangesStr = rangesStr+"@"+e;
	})
	rangesStr = rangesStr.substring(1);
	return rangesStr;
}
	
Ext.onReady(initialize);

function initialize(){
	if(pageAction == PageAction.MODIFY){
		//$("#createBT").text("@COMMON.modify@");
		//Zeta$('createBT').value("@COMMON.modify@");
		//R.createBT.setText( "@COMMON.modify@");
		$("#createBT").hide();
	}else{
		$("#upgradeBT").hide();
	}
	$("#createBT").attr("disabled",!operationDevicePower);
	var els = $("label[NESSARY=true]");
	$.each(els,function(idx,el){
		var $el = $(el);
		$el.html($el.html().concat("<font class=nessary>*</font>:"))
	});
	
	var upchannels = new Ext.ux.form.LovCombo({
		renderTo: "LB_Upchanel",
		id: "upchanelCombo",
        hideOnSelect : true,
        editable : false,
        store : new Ext.data.JsonStore({
        	url: "/cmc/loadbalance/getCcmtsUpchannelList.tv", autoLoad: true,
        	baseParams: {
        		cmcId : cmcId
        	},
        	listeners:{
            	load : function(){
            		var chs = [];
            		$.each(cmcLoadBalanceGroup.upchannels,function(i,c){
            			chs.push(c.docsLoadBalChannelIfIndex);
            		});
            		upchannels.setValue(chs.join(","));
            	}
            },
            fields: ["channelId", "channelIndex"]
        }),
        displayField : 'channelId',  
        valueField : 'channelIndex',
        triggerAction : 'all',  
        emptyText : "@loadbalance.selectUpChannel@",  
        mode : 'local',
        beforeBlur:function(){}
	});
	
	var downchannels = new Ext.ux.form.LovCombo({
		renderTo: "LB_Downchanel",
        hideOnSelect : true,
        editable : false,
        id: "downchanelCombo",
        store : new Ext.data.JsonStore({
        	url: "/cmc/loadbalance/getCcmtsDownchannelList.tv", autoLoad: true,
        	baseParams: {
        		cmcId : cmcId
        	},
        	listeners:{
            	load : function(){
            		var chs = [];
            		$.each(cmcLoadBalanceGroup.downchannels,function(i,c){
            			chs.push(c.docsLoadBalChannelIfIndex);
            		});
            		Ext.getCmp("downchanelCombo").setValue(chs.join(","));
            	}
            },
            fields: ["channelId", "channelIndex"]
        }),
        displayField : 'channelId',  
        valueField : 'channelIndex',
        triggerAction : 'all',  
        emptyText : "@loadbalance.selectDownChannel@",  
        mode : 'local',
        beforeBlur:function(){}
	});
	
	$.each(cmcLoadBalanceGroup.ranges,function(i,v){
		var array = v.topLoadBalRestrictCmMacRang.split(" ");
		pushRangeToWindow(Validator.convertMacToDisplayStyle(array[0], macDisplayFormat), Validator.convertMacToDisplayStyle(array[1], macDisplayFormat));
	});
}

/**
 * 添加MAC地址范围
 */
function addMacRange(){
	var st = $("#startMac").val();
	var em = $("#terminateMac").val();
	//对起始MAC地址进行验证
	if(!Validator.isMac(st)){
		return window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.startMacErrorTip@","tip",function(){
			$("#startMac").focus();
		});
	}else if(Validator.isSpecialMac(st)){
		return window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.noSpecialMacAddress@","tip",function(){
			$("#startMac").focus();
		});
	}
	
	//对终止MAC地址进行验证
	if(!Validator.isMac(em)){
		return window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.termMacErrorTip@","tip",function(){
			$("#terminateMac").focus();
		});
	}else if(Validator.isSpecialMac(em)){
		return window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.noSpecialMacAddress@","tip",function(){
			$("#terminateMac").focus();
		});
	}
	
	//格式统一为用户设定格式
	var formatStMac = Validator.convertMacToDisplayStyle(st, macDisplayFormat);
	var formatEtMac = Validator.convertMacToDisplayStyle(em, macDisplayFormat);
	
	//验证起止MAC地址与终止MAC地址的关系
	if(formatStMac>formatEtMac){
		return window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.startMacMustLessThenEnd@","tip",function(){
			$("#startMac").focus();
		});
	}
	//判断当前加入的MAC地址是否已经存在或冲突
	var isExist = false;
	$.each(ranges,function(i,e){
		var rT = e.split("#");
		if((formatStMac<=rT[0]&&formatEtMac>=rT[1])//加入的mac地址范围包含于 某个mac地址范围
				||(formatStMac>=rT[0]&&formatEtMac<=rT[1])//加入的mac地址范围包含某个mac地址
				||(formatStMac<=rT[0]&&formatEtMac>=rT[0])//加入的mac地址最小值包含于某个mac地址范围
				||(formatStMac<=rT[1]&&formatEtMac>=rT[1])//加入的mac地址最大值包含于某个mac地址范围
				){
			window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.includeRangeOverlap@","tip",function(){
				$("#startMac").focus();
			});
			isExist = true;
			return false;	
		}
	});
	if(!isExist){
		//通过则添加该MAC地址范围
		pushRangeToWindow(formatStMac,formatEtMac);	
	}
	
}

function pushRangeToWindow(mac1,mac2){
	var data = mac1+"--"+mac2;
	ranges.push(mac1+"#"+mac2);
	var seg = DOC.createElement("div");
	seg.className = "EXC-MAC-CLAZZ";
	seg.innerHTML = String.format("<span href='#'>{0}</span><img src='/images/close.gif' onclick='deleteRange(this);' />", data);
	seg.style.marginLeft = 30;
	Zeta$("LB_MAC_RANGE").appendChild(seg);
}

function deleteRange(el){
	ranges.remove($(el).prev().text().replace("--","#"));
	$(el).prev().remove();
	$(el).remove();
}

function modifyHandler(data){
	data.pageAction = PageAction.MODIFY;
	data.grpId = grpId;
	var res = getModifyChannelIndexs(data);
	data.addedGroupChannelsStr = res.added;
	data.deletedGroupChannelsStr = res.deleted;
	data.deletedGroupRangesStr = getDeletedMacRange(data);
	data.addedGroupRangesStr = dealRanges();
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saveCfg@", 'ext-mb-waiting');
	$.ajax({
		url : '/cmc/loadbalance/createOrModifyLoadbalance.tv',cache:false,
		data: data,
		dataTye:"json",
		success:function(res){
			var r = res.msg;
			if(r){
				window.parent.showMessageDlg("@COMMON.tip@", r);
			}else{
				//window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.mdfLBOk@");
				window.top.closeWaitingDlg();
				top.afterSaveOrDelete({
	   				title: '@COMMON.tip@',
	   				html: '<b class="orangeTxt">@loadbalance.mdfLBOk@</b>'
	   			});
			}
			cancelClick();
		},error:function(){
			window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.mdfLBEr@");
		}
	});
}

function getModifyChannelIndexs(data){
	var adds ="",
		deletes = "",
		ups = [],
		downs = [],
		channels = [],
		currentChanels = [];
	for(var i=0; i< cmcLoadBalanceGroup.channels.length; i++){
		var ch = cmcLoadBalanceGroup.channels[i];
		channels.push(ch.docsLoadBalChannelIfIndex);
	}
	
	if(data.groupUpchannel){
		ups = data.groupUpchannel.split(",");
		for(var i=0; i< ups.length; i++){
			var up = ups[i];
			if( !channels.contains(up) ){
				adds=adds+"@"+up;
			}
			currentChanels.push(up);
		}
	}
	if(data.groupDownchannel){
		downs = data.groupDownchannel.split(",");
		for(var i=0; i< downs.length; i++){
			var down = downs[i];
			if( !channels.contains(down)){
				adds=adds+"@"+down;
			}
			currentChanels.push(down);
		}
	}
	adds = adds.substring(1);
	$.each(channels,function(i,c){
		if( !currentChanels.contains(c)){ // 新增的信道里不含原有的信道，则表示这个信道被删除了
			deletes = deletes+"@"+c;
		}
	}); 
	deletes = deletes.substring(1);
	return {
		added : adds,
		deleted : deletes
	}
}

function getDeletedMacRange(data){
	var deletes = "",
		orginRanges = cmcLoadBalanceGroup.ranges; //topLoadBalRestrictCmMacRang;
		
		//如果找删除的地址段，则使用原有的与现有的进行比较。在原有中存在的，现有中不存在的，则视为要删除的
	$.each(orginRanges,function(i,v){
		var thisRange = v.topLoadBalRestrictCmMacRang;
		thisRange = thisRange.replace(" ","#");
		if( !ranges.contains(thisRange)){//如果当前的列表不包含原有列表的某值，则认为这个值被删除掉了
			deletes = deletes+"@"+v.topLoadBalRestrictCmIndex;
		}else{ //如果当前列表保护这个值，则认为这个值是重复的，需要从当前列表中删除掉
			ranges.remove(thisRange);
		}
	});
	deletes = deletes.substring(1);
	return deletes
}

function saveHandler(){
	var data = {};
	data.cmcId  = cmcId;
	data.groupName = $("#LB_Name").val();
	if(!data.groupName){
		return window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.groupNameNotNull@","tip",function(){
			$("#LB_Name").focus();
		});
	}
	if(validateName(data.groupName)){
		return window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.groupNameScope@","tip",function(){
			$("#LB_Name").focus();
		});
	}
	var guc =  Ext.getCmp("upchanelCombo").getValue();
	var gdc =  Ext.getCmp("downchanelCombo").getValue();
	if(!guc && !gdc){
		return window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.selectOneChannel@","tip",function(){
			Ext.getCmp("upchanelCombo").focus();
		});
	}
	if(guc){
		data.groupUpchannel = guc
	}
	if(gdc){
		data.groupDownchannel = gdc;
	}
	var str = $("#LB_MAC_RANGE").text();
	var arrs = str.split("--");
	
	if(arrs.length>33){
		return window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.groupRestrictCmSize@","tip");
	}
	/* if(!data.groupUpchannel){
		return window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.upchannelNotNull@","tip",function(){
			Ext.getCmp("upchanelCombo").focus();
		});
	} */
	/* if(!data.groupDownchannel){
		return window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.downchannelNotNull@","tip",function(){
			Ext.getCmp("downchanelCombo").focus();
		});
	} */
	if(pageAction == PageAction.MODIFY){
		data.pageAction = PageAction.MODIFY;
		return modifyHandler(data);
	}
	data.pageAction = PageAction.CREATE;
	data.macRangesStr = dealRanges();
	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saveCfg@", 'ext-mb-waiting');
	$.ajax({
		url : '/cmc/loadbalance/createOrModifyLoadbalance.tv',cache:false,
		data: data,
		success:function(res){
			var r = res.msg;
			if(r){
				window.parent.showMessageDlg("@COMMON.tip@", r);
			}else{
				//window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.addGroupOk@");
				window.top.closeWaitingDlg();
				top.afterSaveOrDelete({
   					title: '@COMMON.tip@',
   					html: '<b class="orangeTxt">@loadbalance.addGroupOk@</b>'
   				});
				cancelClick();
			}
			
		},error:function(){
			window.parent.showMessageDlg("@COMMON.tip@", "@loadbalance.addGroupEr@");
		}
	});
}

function cancelClick(){
	window.parent.closeWindow('addLoadBalance');
}
</script>
</head>
	<body class=openWinBody>
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@loadbalance.createLBTip@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>

		<div class="edge10">
			<table class="mCenter zebraTableRows">
				<tr>
					<td class="rightBlueTxt">@loadbalance.groupName@:</td>
					<td colspan="3"><input maxlength="32" id="LB_Name" class="w160 normalInput" tooltip="@loadbalance.maxlength32@,@loadbalance.groupNameScope@" value="${cmcLoadBalanceGroup.groupName}" /></td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">@CCMTS.upStreamChannel@:</td>
					<td colspan="3"><div id="LB_Upchanel"></div></td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@CCMTS.downStreamChannel@:</td>
					<td colspan="3"><div id="LB_Downchanel"></div></td>
				</tr>
				<tr class="darkZebraTr">
					<td class="rightBlueTxt">@loadbalance.startMac@:</td>
					<td><input id="startMac" class="w160 normalInput" /></td>
					<td class="rightBlueTxt">@loadbalance.terminateMac@:<input id="terminateMac" class="w160 normalInput"/></td>
					<td>
					<a  onclick="addMacRange()" href="javascript:;" class="normalBtn"><span><i class="miniIcoAdd"></i>@COMMON.add@</span></a>
					</td>
				</tr>
				<tr>
					<td colspan="4"><div id="LB_MAC_RANGE"></div></td>
				</tr>
			</table>
		</div>

		<%-- <Zeta:ButtonGroup>
			<Zeta:Button id="createBT" onClick="saveHandler()"
				icon="bmenu_new">@BUTTON.add@</Zeta:Button>
			<Zeta:Button id="modifyBT" onClick="saveHandler()"
				icon="bmenu_edit">@BUTTON.edit@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="bmenu_forbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup> --%>
		
		<div class="edgeTB10LR20 pT10">
	    <div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 p130 noWidthCenter">
		         <li><a  onclick="saveHandler()" id="createBT" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoAdd"></i>@COMMON.add@</span></a></li>
		         <li><a  onclick="saveHandler()" id="upgradeBT" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEdit"></i>@COMMON.modify@</span></a></li>
		         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>

	</body>
</Zeta:HTML>