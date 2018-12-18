<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<HTML><HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/gui.css">
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="/js/tools/ipAddrCheck.js"></script>
<link rel="stylesheet" type="text/css" href="/css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/xtheme.css"/>  
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/mytheme.css"/>
<style type="text/css">
.postIcon {background-image: url(/images/system/post.gif) !important;}

</style>
<script type="text/javascript" src="../js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="../js/jquery/jquery.js"></script>
<script type="text/javascript" src="../js/tools/ipText.js"></script>
<script type="text/javascript">
var _POLICY_ = {primary: 1, policy: 2, strict: 3};
var _SERVER_DEVICETYPE_ = ["","CM","HOST","MTA","STB","ALL"];
var serverNum = [0, 0, 0, 0, 0, 0];
var serverGrid = null;
var serverStore = null;
var server = {data: []};
var delServerList = [];
var addServerList = [];
var addServerTypeList = [];
var vlanList;
function initData(){
	if(window.parent.dhcpRelay.delServerList){
	    delServerList = window.parent.dhcpRelay.delServerList;  
	}
	server.data = window.parent.dhcpRelay.serverList;
	if(server.data && server.data.length > 0){
		for(var i = 0; i < server.data.length; i++){
	        var s = server.data[i];
	        serverNum[s.topCcmtsDhcpHelperDeviceType] ++;
	    }
	}    
	vlanList = window.parent.dhcpRelay.vlanList;
}
function cancelClick() {
    window.top.closeWindow('createRelayConfig');
}
function pageChangePrepare(){
	var serverList = new Array();
    serverStore.each(function(){
        serverList.push(this.data);
        if(!this.data.helperId){
            var helperIp = this.data.topCcmtsDhcpHelperIpAddr;
            var helperIpType = this.data.topCcmtsDhcpHelperDeviceType;
            addServerList.push(helperIp);
            addServerTypeList.push(helperIpType);
        }
    });
    window.parent.dhcpRelay.serverList = serverList;
    window.parent.dhcpRelay.addServerList = addServerList;
    window.parent.dhcpRelay.addServerTypeList = addServerTypeList;
    window.parent.dhcpRelay.delServerList = delServerList;
    
}
/************************************************************************************************************
 * 数据校验
 ************************************************************************************************************/
function ipInputValid(ip){
    if(!ipIsFilled("server")){
        return false;
    }
    var ipCheck = new IpAddrCheck(ip);    
    return ipCheck.isHostIp();
}
function preClick(){
	pageChangePrepare();
    window.location.assign('/cmc/createDhcpRelayStep5.jsp');

}
function nextClick(){
	pageChangePrepare();
    window.location.assign('/cmc/createDhcpRelayStep4.jsp');

}

function isExistServer(type, server){
	var exist = false;
	serverStore.each(function (){
		if(this.data.topCcmtsDhcpHelperDeviceType == type && 
				this.data.topCcmtsDhcpHelperIpAddr == server){
			exist = true;
		}
	});
	return exist;
}
function onAddServer(){
    var serverIp = getIpValue("server");
    var deviceType = $("#serverType").val();
    if(!ipInputValid(serverIp) || deviceType =="0" || isExistServer(deviceType, serverIp)){
    	return;
    }
    if(checkExistInDevice(serverIp, vlanList)){
        window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.CMC.tip.dhcpServerIsDeviceIp);
        return false;
    }
    var data = {
    		topCcmtsDhcpHelperDeviceType: deviceType,
    		topCcmtsDhcpHelperIpAddr: serverIp
        };
    var index = serverStore.getCount();
    var p = new serverStore.recordType(data); 
    serverStore.insert(index, p);
    serverNum[deviceType]++;
    if(serverNum[deviceType] >= 5){
    	initDeviceTypeSelect();
    }    
}
//检查是否是CCMTS上存在的IP地址, 是：true， 否：false
function checkExistInDevice(val,vlanList){
    for(v in vlanList){
        var ipTmp = vlanList[v].ipAddr;
        var maskTmp = vlanList[v].ipMask;
        if(ipTmp==undefined||ipTmp==""||ipTmp=="-"||maskTmp==undefined||maskTmp==""||maskTmp=="-"){
            continue;
        }
        if(val == ipTmp){
            return true;
        }
    }
    return false;
}
function buildServerInput(){
    return '<table><tbody><tr><td class="rightBlueTxt" width="20">' + 'IP' + '</td>' +
           '<td><span id="span1" style="background-color:#ffffff"></span></td></tr></tbody></table>'
}
function buildServerTypeSelect(){
    return '<table><tbody><tr><td class="rightBlueTxt" width="30">' + I18N.CMC.text.dhcpType + '</td>' +
           '<td style="width: 141px;">' + 
           '<select id="serverType" style="width: 141px;" class="normalSel">' +           
           '<option value="0" selected>'+ I18N.CMC.select.select+'</option>' + 
           '</select></td></tr></tbody></table>' 
}
function initDeviceTypeSelect(){
    var head = '<select id="serverType" class="normalSel" style="width: 141px;"><option value="0" selected>'+ 
        I18N.CMC.select.select+'</option>';
    var end = '</select>';    
    for(var i = 1; i < serverNum.length; i++){
        if(serverNum[i] < 5){
            head += '<option value="' + i + '">' + _SERVER_DEVICETYPE_[i] +'</option>';
        }
    }
    $("#serverType").replaceWith(head + end);
}
function removeRecord(id){
	var record = serverStore.getById(id);
	var serverIndex = record.data.helperId;
    if(serverIndex){
    	var helperId = serverIndex;
    	delServerList.push(helperId);
    }
	serverStore.remove(record);		
	if(serverNum[record.data.topCcmtsDhcpHelperDeviceType]-- >= 5){
		initDeviceTypeSelect();
	}
	
}
function opeartionRender(value, cellmate, record){
    return String.format("<img src='/images/delete.gif' " + 
            "onclick='removeRecord(\"{0}\")'/>&nbsp;&nbsp;&nbsp;&nbsp;" , record.id);
}
function stateRender(value, cellmate, record){
    if(value && value!=""){
        return I18N.CMC.label.original;
    }else{
        return I18N.CMC.text.dhcpNewAdd;
    }
}
function deviceTypeRender(value, cellmate, record){
    return _SERVER_DEVICETYPE_[value];
}
function initIpInput(){
    var server = new ipV4Input("server","span1");
    server.width(141);
}
function createServerGrid(){
    var w = document.body.clientWidth - 30;
    var h = document.body.clientHeight - 120;
    var columns = [
                  {header: I18N.CMC.label.entitytype, width:w/4, align:"center", sortable:false, dataIndex:"topCcmtsDhcpHelperDeviceType", renderer: deviceTypeRender},
                  {header: "IP", width:w/4, align:"center", sortable:false, dataIndex:"topCcmtsDhcpHelperIpAddr"},
                  {header: I18N.CMC.label.type, width:w/4-20, align:"center", sortable:false, dataIndex:"topCcmtsDhcpHelperIndex", renderer: stateRender},
                  {header: I18N.CMC.text.dhcpOperation, width:w/4-10, align:"center", sortable:false, dataIndex:"op", renderer: opeartionRender}
                  ];
    var cm = new Ext.grid.ColumnModel(
        columns
    );
    var toolbar = [
                   buildServerTypeSelect(),
                   buildServerInput(),                   
                   {text: I18N.CMC.text.dhcpAdd, iconCls: "bmenu_new", handler: onAddServer}
                   ];
    serverStore = new Ext.data.JsonStore({
        root:'data',
        pruneModifiedRecords: false,
        proxy:new Ext.data.MemoryProxy(server),
        fields:[
                'helperId',
                'topCcmtsDhcpHelperIndex', 
                'topCcmtsDhcpHelperDeviceType', 
                'topCcmtsDhcpHelperIpAddr'
                ]
    });
    serverGrid  = new Ext.grid.EditorGridPanel({ 
        height: 290,
        trackMouseOver: false, 
        border: true, 
        store: serverStore,
        clicksToEdit: 1,
        tbar: toolbar,
        cm: cm,
        autofill:true,
        bodyCssClass:'normalTable',
        viewConfig:{
        	forceFit:true
        },
        renderTo: 'dhcpServer-div'
    });
    serverStore.load();
    initIpInput();
    initDeviceTypeSelect();
}
Ext.onReady(function (){
	initData();
	createServerGrid();	
});


</script>
</HEAD>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	<p><b class="orangeTxt"><fmt:message bundle="${cmc}" key="CMC.label.dhcpRelayConfig"/></b></p>
	    	<p><span id="newMsg"></span><fmt:message bundle="${cmc}" key="CMC.text.dhcpServerConfig"/></p>
	    </div>
	    <div class="rightCirIco wheelCirIco"></div>
	</div>
	<div id="dhcpServer-div" class="edge10"></div>
	<div class="noWidthCenterOuter clearBoth" id=sequenceStep>
	     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
	         <li><a id=prevBt onclick="preClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrLeft"></i><fmt:message bundle="${cmc}" key="CMC.button.preStep"/></span></a></li>
	         <li><a id="nextStep"  onclick="nextClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrRight"></i><fmt:message bundle="${cmc}" key="CMC.button.nextStep"/></span></a></li>
	         <li><a id=cancelBt onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><fmt:message bundle="${cmc}" key="CMC.button.cancel"/></span></a></li>
	     </ol>
	</div>
	
</BODY>
</HTML>