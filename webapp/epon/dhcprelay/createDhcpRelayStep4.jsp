<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML><HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css">
<fmt:setBundle basename="com.topvision.ems.epon.resources" var="epon"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<link rel="stylesheet" type="text/css" href="/css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/xtheme.css"/>  
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/mytheme.css"/>
<style type="text/css">
.postIcon {background-image: url(/images/system/post.gif) !important;}
#sequenceStep {
    padding-left : 380;position:absolute;top:290px;
}
</style>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript">
var _POLICY_ = {primary: 1, policy: 2, strict: 3};
var _DEFAULTOPTION60_ = ["docsis", "pktc", "stb"];
var _DEVICETYPE_ = ["","CM","HOST","MTA","STB"];
var option60Grid = null;
var option60Store = null;
var option60Data = {data: []};
var option60List = [];
var entityId = window.parent.entityId;
var deviceTypes = null;
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
		return false;
	}
	if(isExistInStore(val) || containsDefaultOption60(val)){
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
function checkOption(deviceTypes, options){
	for(var i = 0; i < deviceTypes.length; i++){
		var j = 0;
		for(; j < options.length; j++){
			if(deviceTypes[i] == options[j].deviceTypeStr){
				break;
			}			
		}
		if(j >= options.length){
            return false;
        }
	}
	return true;
}
function onSaveClick(){
	var option60s = [];
    option60Store.each(function(){ 
        option60s.push(this.data);
    });
    if(!checkOption(deviceTypes, option60s)){
    	return ;
    }
	var dataStr = "";
	dataStr += "dhcpRelayBundle.bundleInterface=" + window.parent.dhcpRelay.bundle.bundleInterface;
	dataStr += "&dhcpRelayBundle.topCcmtsDhcpBundlePolicy=" + window.parent.dhcpRelay.bundle.topCcmtsDhcpBundlePolicy;
	dataStr += "&dhcpRelayBundle.cableSourceVerify=" + window.parent.dhcpRelay.bundle.cableSourceVerify;
	var vlanMap;
	if(window.parent.dhcpRelay.bundle.vlanMapSwitch == 1){
		vlanMap = window.parent.dhcpRelay.bundle.vlanMapStr;
	}else{
		vlanMap="1-4094";
	}
	dataStr += "&dhcpRelayVlanMap.vlanMapStr=" + vlanMap;
	var giaddrList = window.parent.dhcpRelay.giAddrList;
	if(giaddrList && giaddrList.length>0){
		for(var i = 0; i < giaddrList.length; i++){
			dataStr +=String.format("&dhcpGiaddrConfigs[{0}].deviceTypeStr={1}", i,giaddrList[i].deviceTypeStr);
			dataStr +=String.format("&dhcpGiaddrConfigs[{0}].topCcmtsDhcpGiAddress={1}", i,giaddrList[i].topCcmtsDhcpGiAddress);
			dataStr +=String.format("&dhcpGiaddrConfigs[{0}].topCcmtsDhcpGiAddrMask={1}", i,giaddrList[i].topCcmtsDhcpGiAddrMask);
		}
	}
	var dhcpRelayServer = window.parent.dhcpRelay.serverList;
	if(dhcpRelayServer && dhcpRelayServer.length > 0){
		for(var i = 0; i < dhcpRelayServer.length; i++){
			dataStr +=String.format("&dhcpServerConfigs[{0}].deviceTypeStr={1}", i,dhcpRelayServer[i].deviceTypeStr);
			dataStr +=String.format("&dhcpServerConfigs[{0}].topCcmtsDhcpHelperIpAddr={1}", i,dhcpRelayServer[i].topCcmtsDhcpHelperIpAddr);
	    }
	}
	var option60s = [];
	option60Store.each(function(){ 
        option60s.push(this.data);
    });
	if(option60s && option60s.length > 0){
		for(var i = 0; i < option60s.length; i++){
			dataStr +=String.format("&dhcpOption60Configs[{0}].deviceTypeStr={1}", i,option60s[i].deviceTypeStr);
			dataStr +=String.format("&dhcpOption60Configs[{0}].topCcmtsDhcpOption60Str={1}", i,option60s[i].topCcmtsDhcpOption60Str);
		}
	}
    window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.DHCPRELAY.configuring, 'ext-mb-waiting');   
    $.ajax({
        url: '/epon/dhcprelay/modifyDhcpRelayBundleConfig.tv?entityId='+entityId + "&bundleInterfaceEnd=" + window.parent.dhcpRelay.bundle.bundleInterface,
        type: 'post',
        data: dataStr,
        dataType:'json',
        success: function(response) {
              if(response.message == "success"){   
            	  top.closeWaitingDlg();
              	  top.nm3kRightClickTips({
      				title: I18N.RECYLE.tip,
      				html: I18N.COMMON.addSuccess
      			  });
                  //window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.DHCPRELAY.modifySuccess);
                  setTimeout(function (){
                      window.top.closeWaitingDlg(I18N.RECYLE.tip);                      
                      window.parent.getFrame("entity-" + entityId).onRefresh();
                      cancelClick();
                  }, 500);                  
               }else{
                   window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.DHCPRELAY.modifyFail);
               }
          }, error: function(response) {
              window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.DHCPRELAY.modifyFail);
          }, cache: false
      });
}
function pageChangePrepare(){
    option60List = [];
    option60Store.each(function(){
        option60List.push(this.data);
    });
    window.parent.dhcpRelay.option60List = option60List;
}
function preClick(){
    pageChangePrepare();
    window.location.assign('/epon/dhcprelay/createDhcpRelayStep3.jsp');

}
function addOption60(){
    var deviceType = $("#deviceType").val();
    var option60Str = $("#option60").val();
    if(!option60InputValid(option60Str)){
        return;
    }
    var data = {
    		deviceTypeStr: deviceType,
            topCcmtsDhcpOption60Str: option60Str
        };
    var index = option60Store.getCount();
    var p = new option60Store.recordType(data); 
    option60Grid.stopEditing();
    option60Store.insert(index, p);
    option60Grid.startEditing(0, 0);

}
function buildDeviceTypeSelect(){
	var typeSelect = '<td width=60 align=center>' + I18N.DHCPRELAY.dhcpType + '</td>&nbsp;' +
	    '<td style="width: 141px;">' + 
	    '<select id="deviceType" style="width: 141px;">' +           
	    '<option value="CM">CM</option>' + 
	    '<option value="HOST">HOST</option>' + 
	    '<option value="MTA">MTA</option>' +
	    '<option value="STB">STB</option>';           
	var typeSelectEnd = '</select></td>' ;
	if(deviceTypes && deviceTypes.length > 0){
	for(var i = 0; i < deviceTypes.length; i++){
	    typeSelect += '<option value="' + deviceTypes[i] + '">'+ deviceTypes[i] +'</option>'
	}
	}
	typeSelect += typeSelectEnd;
	return typeSelect;
}
function buildOption60Input(){
    return '<td width=60 align=center>' + 'Option60' + '</td>&nbsp;' +
           '<td><input class="" type=text style="width: 141px" id="option60"></td>'
}
function removeRecord(id){
    var record = option60Store.getById(id);
    var option60Index = record.data.topCcmtsDhcpOption60Index;
    option60Store.remove(record);
}
function opeartionRender(value, cellmate, record){
    return String.format("<img src='/images/delete.gif' " + 
            "onclick='removeRecord(\"{0}\")'/>&nbsp;&nbsp;&nbsp;&nbsp;" , record.id);
}
function stateRender(value, cellmate, record){
	if(value && value!=""){
		return I18N.DHCPRELAY.original;
	}else{
		return I18N.DHCPRELAY.dhcpNewAdd;
	}
}
function createOption60Grid(){
    var w = document.body.clientWidth - 30;
    var h = document.body.clientHeight - 120;
    var columns = [
                  {header: I18N.DHCPRELAY.entitytype, width:w/4, align:"center", sortable:false, dataIndex:"deviceTypeStr"},
                  {header: "Option60", width:w/4, align:"center", sortable:false, dataIndex:"topCcmtsDhcpOption60Str"},
                  {header: I18N.DHCPRELAY.type, width:w/4-20, align:"center", sortable:false, dataIndex:"topCcmtsDhcpOption60Index", renderer: stateRender},
                  {header: I18N.DHCPRELAY.dhcpOperation, width:w/4-10, align:"center", sortable:false, dataIndex:"op",renderer: opeartionRender}
                  ];
    var cm = new Ext.grid.ColumnModel(
        columns
    );
    var toolbar = [
                   buildDeviceTypeSelect(),
                   buildOption60Input(),
                   {text: I18N.DHCPRELAY.add, iconCls: "bmenu_new", handler: addOption60}
                   ];
    option60Store = new Ext.data.JsonStore({
        root:'data',
        pruneModifiedRecords: false,
        proxy:new Ext.data.MemoryProxy(option60Data),
        fields:[
                'option60Id',
                'topCcmtsDhcpOption60Index', 
                'topCcmtsDhcpOption60DeviceType', 
                'topCcmtsDhcpOption60Str',
                'deviceTypeStr'
                ]
    });
    option60Grid  = new Ext.grid.EditorGridPanel({ 
        height: 300,
        trackMouseOver: false, 
        border: true, 
        store: option60Store,
        clicksToEdit: 1,
        tbar: toolbar,
        cm: cm,
        autofill:true,
        renderTo: 'option60-div',
        cls:'normalTable',
        viewConfig:{
        	forceFit:true
        }
    });
    option60Store.load();

}
Ext.onReady(function (){
    option60Data.data = window.parent.dhcpRelay.option60List;
    deviceTypes = window.parent.dhcpRelay.deviceTypes;
    createOption60Grid();
});


</script>
</HEAD>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	<p><b>Option60</b></p>
	    	<p><span id="newMsg"><fmt:message bundle="${epon}" key="DHCPRELAY.optionStepTip"/></span></p>
	    </div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	<div class="edgeTB10LR20">
		<div id="option60-div"></div>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
		         <li><a id="prevBt" onclick="preClick()" href="javascript:;" class="normalBtnBig"><span><fmt:message bundle="${epon}" key="DHCPRELAY.back"/></span></a></li>
		         <li><a id="saveClick" onclick="onSaveClick()" href="javascript:;" class="normalBtnBig"><span><fmt:message bundle="${epon}" key="DHCPRELAY.finish"/></span></a></li>
		         <li><a id="cancelBt" onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><fmt:message bundle="${epon}" key="DHCPRELAY.cancel"/></span></a></li>
		     </ol>
		</div>
	</div>
	
	
	
	
<div class=formtip id=tips style="display: none"></div>

</body>
</HTML>