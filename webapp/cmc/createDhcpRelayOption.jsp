<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<script type="text/javascript" src="/js/tools/ipAddrCheck.js"></script>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module cmc
    import js.tools.ipText
</Zeta:Loader>
<script type="text/javascript">
var _POLICY_ = {primary: 1, policy: 2, strict: 3};
var _DEFAULTOPTION60_ = ["docsis", "pktc", "stb"];
var _DEVICETYPE_ = ["","CM","HOST","MTA","STB"];
var entityId = ${entityId};
var cmcId = ${cmcId};
var action = ${action};
var bundleInterface = "${bundleInterface}";
var dhcpOptionArray = ${dhcpOptionArray};
var option60Grid = null;
var option60Store = null;
var option60Data = {data: []};
var delOption60List = [];
var option60List = [];
var addOption60List = [];
var addOption60TypeList = [];
var option60Num = [0,0,0,0,0];

function initData(){
	if(window.parent.dhcpRelay.delOption60){
	    delOption60List = window.parent.dhcpRelay.delOption60;
	}
	if(window.parent.dhcpRelay.option60List){
	    option60Data.data = window.parent.dhcpRelay.option60List;
	}else{
	    option60Data.data = dhcpOptionArray;
	}
	if(option60Data.data && option60Data.data.length>0){
		for(var i = 0; i < option60Data.data.length; i++){
	        var option = option60Data.data[i];
	        option60Num[option.topCcmtsDhcpOption60DeviceType] ++;
	    }
	}
	
}
/************************************************************************************************************
 * 数据校验
 ************************************************************************************************************/
function isCharacter(val){
    var reg = /^[A-Za-z0-9_]{1,16}$/;
    return reg.test(val);
}
function containsDefaultOption60(val){
    if(val.indexOf(_DEFAULTOPTION60_[0])!=-1 || val.indexOf(_DEFAULTOPTION60_[1])!=-1 || 
            val.indexOf(_DEFAULTOPTION60_[2])!=-1){
        return true;
    }else{
        return false;
    }
}
function isExistInStore(val){
	var option60s = option60Store.data.items;
	if(option60s && option60s.length>0){
        for(var i = 0; i < option60s.length; i++){
            if(val == option60s[i].data.topCcmtsDhcpOption60Str){
                return true;
            }
        }
    }
	return false;
}
function option60InputValid(val){
	if(!isCharacter(val)){
	    window.parent.showMessageDlg("@RESOURCES/RECYLE.tip@", "@CMC.text.option60Format@");
		$('#option60').focus();
		return false;
	}
	if(isExistInStore(val)){
	    window.parent.showMessageDlg("@RESOURCES/RECYLE.tip@", "@CMC.text.option60Conflict@");
	    $('#option60').focus();
		return false
	}
	if(containsDefaultOption60(val)){
	    window.parent.showMessageDlg("@RESOURCES/RECYLE.tip@", "@CMC.text.option60ContainDef@");
	    $('#option60').focus();
        return false
	}
	return true;
}
/*********************************************************************************************************
 * 按钮动作
 *********************************************************************************************************/
function cancelClick() {
    window.top.closeWindow('createRelayConfig');
}
function onSaveClick(){
    var addOption60List = [];
    var addOption60TypeList = [];
    option60Store.each(function(){
        if(!this.data.topCcmtsDhcpOption60Index){
            var data = {};
            topCcmtsDhcpOption60DeviceType = this.data.topCcmtsDhcpOption60DeviceType;
            topCcmtsDhcpOption60Str = this.data.topCcmtsDhcpOption60Str;
            addOption60List.push(topCcmtsDhcpOption60Str);
            addOption60TypeList.push(topCcmtsDhcpOption60DeviceType);
        }
    });
    window.parent.dhcpRelay.addOption60 = addOption60List;
    window.parent.dhcpRelay.delOption60 = delOption60List;
    var giAddrList = window.parent.dhcpRelay.giAddrIpArray;
    var giAddrMaskList = window.parent.dhcpRelay.giAddrMaskArray;
    var giAddrTypeList = window.parent.dhcpRelay.giAddrTypeArray;
    var addVirtualIp = window.parent.dhcpRelay.addVirtualIp;
    var addVirtualIpMask = window.parent.dhcpRelay.addVirtualIpMask;
    var delVirtualIp = window.parent.dhcpRelay.delVirtualIp;
    var addServerList = window.parent.dhcpRelay.addServerList;
    var addServerTypeList = window.parent.dhcpRelay.addServerTypeList;
    var delServerList = window.parent.dhcpRelay.delServerList;
    var policy = window.parent.dhcpRelay.bundle.topCcmtsDhcpBundlePolicy;
    var cableSourceVerify = window.parent.dhcpRelay.bundle.cableSourceVerify;
    var primaryIp = "0.0.0.0";
    var primaryIpMask = "0.0.0.0";
    if(window.parent.dhcpRelay.primaryIp){
    	primaryIp = window.parent.dhcpRelay.primaryIp.virtualPrimaryIpAddr;
    	primaryIpMask = window.parent.dhcpRelay.primaryIp.virtualPrimaryIpMask;
    }    
    var vlan = window.parent.dhcpRelay.vlan;
    var priotity = window.parent.dhcpRelay.priotity;
    var param = {};
    if(giAddrList){
    	param.giAddrList = giAddrList;
    }
    if(giAddrMaskList && giAddrMaskList.length >0 ){
    	param.giAddrMaskList = giAddrMaskList;
    }
    if(giAddrTypeList && giAddrTypeList.length > 0){
        param.giAddrTypeList = giAddrTypeList;
    }
    if(addVirtualIp){
        param.addVirtualIp = addVirtualIp;
    }
    if(addVirtualIpMask){
        param.addVirtualIpMask = addVirtualIpMask;
    }
    if(delVirtualIp){
        param.delVirtualIp = delVirtualIp;
    }
    if(addServerList){
        param.addServerList = addServerList;
    }
    if(addServerTypeList){
        param.addServerTypeList = addServerTypeList;
    }
    if(delServerList){
        param.delServerList = delServerList;
    }
    if(addOption60List){
        param.addOption60 = addOption60List;
    }
    if(addOption60TypeList){
        param.addOption60Type = addOption60TypeList;
    }
    if(delOption60List){
        param.delOption60 = delOption60List;
    }
    if(vlan){
        param.vlan = vlan;
    }
    if(priotity){
        param.priotity = priotity;
    }
    window.top.showWaitingDlg("@RESOURCES/COMMON.waiting@", "@text.configuring@", 'ext-mb-waiting');
    $.ajax({
      url: '/cmc/dhcprelay/modifyDhcpRelayConfig.tv?entityId='+entityId+"&bundleInterface=" +bundleInterface
              +"&policy=" + policy +"&cableSourceVerify=" + cableSourceVerify + "&primaryIp=" + primaryIp + 
              '&primaryIpMask=' + primaryIpMask,
      type: 'post',
      data: param,
      dataType:'json',
      success: function(response) {
            if(response.message == "success"){     
            	window.top.closeWaitingDlg("@RESOURCES/RECYLE.tip@");   
                //window.parent.showMessageDlg("@RESOURCES/RECYLE.tip@", "@CMC.tip.modifySuccess@");
                top.afterSaveOrDelete({
   					title: '@COMMON.tip@',
   					html: '<b class="orangeTxt">@CMC.tip.operateSuccess@</b>'
   				});
                window.parent.getFrame("entity-" + entityId).onRefresh();
                cancelClick();
             }else{
            	 window.top.closeWaitingDlg("@RESOURCES/RECYLE.tip@");
            	 window.parent.dhcpRelay.result = response.result;
            	 window.location.assign('/cmc/createDhcpRelayResult.jsp');
             }
        }, error: function(response) {
            window.parent.showMessageDlg("@RESOURCES/RECYLE.tip@", "@CMC.tip.modifyFail@");
        }, cache: false
    });
}
function pageChangePrepare(){
    option60List = [];
    addOption60List = [];
    option60Store.each(function(){
        option60List.push(this.data);
        if(!this.data.topCcmtsDhcpOption60Index){
            addOption60List.push(this.data);
        }
    });
    window.parent.dhcpRelay.option60List = option60List;
    window.parent.dhcpRelay.addOption60 = addOption60List;
    window.parent.dhcpRelay.delOption60 = delOption60List;
}
function preClick(){
    pageChangePrepare();
    window.location.assign('cmc/dhcprelay/showDhcpRelayConfigServerStep.tv?entityId='+entityId + 
            '&cmcId=' + cmcId + '&action=' + action + "&bundleInterface=" + bundleInterface );

}
function addOption60(){
    var deviceType = $("#deviceType").val();
    var option60Str = $("#option60").val();
    if(deviceType=="0"){
        window.parent.showMessageDlg("@RESOURCES/RECYLE.tip@", "@CMC.text.deviceTypeTip@");
        return ;
    }
    if(!option60InputValid(option60Str)){
        return;
    }
    var data = {
            topCcmtsDhcpOption60DeviceType: deviceType,
            topCcmtsDhcpOption60Str: option60Str
        };
    var index = option60Store.getCount();
    var p = new option60Store.recordType(data); 
    option60Grid.stopEditing();
    option60Store.insert(index, p);
    option60Grid.startEditing(0, 0);
    option60Num[deviceType] ++;
    if(option60Num[deviceType] >= 4){
    	initDeviceTypeSelect();
    }    

}
function buildDeviceTypeSelect(){
    return '<table><tbody><tr><td class="rightBlueTxt" width="30">' + "@CMC.text.dhcpType@" + '</td>' +
            '<td style="width: 141px;">' + 
            '<select id="deviceType" style="width: 141px;">' +           
            '<option value="0" selected>'+ "@CMC.select.select@"+'</option>' + 
            '</select></td></tr></tbody></table>' 
}
function initDeviceTypeSelect(){
	var head = '<select id="deviceType" style="width: 141px;"><option value="0" selected>'+ 
        "@CMC.select.select@"+'</option>';
    var end = '</select>';    
    for(var i = 1; i < option60Num.length; i++){
    	if(option60Num[i] < 4){
    		head += '<option value="' + i + '">' + _DEVICETYPE_[i] +'</option>';
    	}
    }
    $("#deviceType").replaceWith(head + end);
}
function buildOption60Input(){
    return '<table><tbody><tr><td class="rightBlueTxt" width="30">' + 'Option60' + '</td>' +
           '<td><input class="" type=text style="width: 141px" id="option60" maxlength="16" ' +
           "tooltip=\"" + "@CMC.text.option60Tip@" + "\"></td></tr></tbody></table>" ;
           
}
function removeRecord(id){
    var record = option60Store.getById(id);
    var option60Index = record.data.topCcmtsDhcpOption60Index;
    if(option60Index){
        var option60Id = record.data.option60Id;
        delOption60List.push(option60Id);
    }
    option60Store.remove(record);
    if(option60Num[record.data.topCcmtsDhcpOption60DeviceType]-- >=4){
    	initDeviceTypeSelect();
    }    
}
function opeartionRender(value, cellmate, record){
    return String.format("<a href='javascript:;' " + 
            "onclick='removeRecord(\"{0}\")'>@COMMON.del@</a>" , record.id);
}
function deviceTypeRender(value, cellmate, record){
	return _DEVICETYPE_[value];
}
function stateRender(value, cellmate, record){
	if(value && value!=""){
		return "@CMC.label.original@";
	}else{
		return "@CMC.text.dhcpNewAdd@";
	}
}
function createOption60Grid(){
    var w = document.body.clientWidth - 30;
    var h = document.body.clientHeight - 120;
    var columns = [
                  {header: "@CMC.label.entitytype@", width:w/4, align:"center", sortable:false, dataIndex:"topCcmtsDhcpOption60DeviceType", renderer:deviceTypeRender},
                  {header: "Option60", width:w/4, align:"center", sortable:false, dataIndex:"topCcmtsDhcpOption60Str"},
                  {header: "@CMC.label.type@", width:w/4-20, align:"center", sortable:false, dataIndex:"topCcmtsDhcpOption60Index", renderer: stateRender},
                  {header: "@CMC.text.dhcpOperation@", width:w/4-10, align:"center", sortable:false, dataIndex:"op",renderer: opeartionRender}
                  ];
    var cm = new Ext.grid.ColumnModel(
        columns
    );
    var toolbar = [
                   buildDeviceTypeSelect(),
                   buildOption60Input(),
                   {text: "@CMC.text.dhcpAdd@", iconCls: "bmenu_new", handler: addOption60}
                   ];
    option60Store = new Ext.data.JsonStore({
        root:'data',
        pruneModifiedRecords: false,
        proxy:new Ext.data.MemoryProxy(option60Data),
        fields:[
                'option60Id',
                'topCcmtsDhcpOption60Index', 
                'topCcmtsDhcpOption60DeviceType', 
                'topCcmtsDhcpOption60Str'
                ]
    });
    option60Grid  = new Ext.grid.EditorGridPanel({ 
        viewConfig:{
        	forceFit:true
        },
        bodyCssClass:"normalTable",
        height: 290,
        trackMouseOver: false, 
        border: true, 
        store: option60Store,
        clicksToEdit: 1,
        tbar: toolbar,
        cm: cm,
        autofill:true,
        renderTo: 'option60-div'
    });
    option60Store.load();
    initDeviceTypeSelect();
}
Ext.onReady(function (){
    initData();
    createOption60Grid();    
});


</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	<p><b class="orangeTxt">@CMC.label.dhcpRelayConfig@</b></p>
	    	<p><span id="newMsg">@CMC.text.dhcpOption60Set@</span></p>
	    </div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	<div id="option60-div" class="edge10"></div>
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
	         <li><a id=prevBt  onclick="preClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrLeft"></i> @CMC.button.preStep@</span></a></li>
	         <li><a id="nextStep" onclick="onSaveClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSaveOK"></i>@CMC.button.finish@</span></a></li>
	         <li><a id=cancelBt onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@CMC.button.cancel@</span></a></li>
	     </ol>
	</div>
</body>
</Zeta:HTML>