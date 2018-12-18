<%@ page language="java" contentType="text/html;charset=UTF-8"%>
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
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript" src="/js/tools/ipAddrCheck.js"></script>
<script type="text/javascript">
var _POLICY_ = {primary: 1, policy: 2, strict: 3};
var _SERVER_DEVICETYPE_ = ["","CM","HOST","MTA","STB","ALL"];
var serverGrid = null;
var serverStore = null;
var server = {data: []};
var deviceTypes = null;
var entityId = window.parent.entityId;
function initData(){
	server.data = window.parent.dhcpRelay.serverList;
    deviceTypes = window.parent.dhcpRelay.deviceTypes;
}
function initPage(){  
    if(window.parent.action == 2){
        $("#sequenceStep").css("padding-left", 300);
        $("#saveClick").css("display", "inline");
        $("#saveClick-label").css("display", "inline");
    }
}
function cancelClick() {
    window.top.closeWindow('createRelayConfig');
}
function pageChangePrepare(){
	var list = [];
	serverStore.each(function(){ 
		list.push(this.data);
    });
	server.data = list;
    window.parent.dhcpRelay.serverList = list;
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
    window.location.assign('/epon/dhcprelay/createDhcpRelayStep2.jsp');

}
function nextClick(){
	pageChangePrepare();
    window.location.assign('/epon/dhcprelay/createDhcpRelayStep4.jsp');

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
	pageChangePrepare();
    var option60s = window.parent.dhcpRelay.option60List;
    if(!checkOption(window.parent.dhcpRelay.deviceTypes, option60s)){
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
                  window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.DHCPRELAY.modifySuccess);
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
function onAddServer(){
    var serverIp = getIpValue("server");
    var deviceType = $("#serverType").val();
    if(!ipInputValid(serverIp)){
    	return;
    }
    var data = {
    		deviceTypeStr: deviceType,
    		topCcmtsDhcpHelperIpAddr: serverIp
        };
    var index = serverStore.getCount();
    var p = new serverStore.recordType(data); 
    serverStore.insert(index, p);

}

function buildServerInput(){
	return '<span id="span1"></span>';
   /*  return '<td width=140>' + 'IP' + '</td>' +
           '<td><span id="span1" style="background-color:#ffffff"></span></td>' */
}
function buildServerTypeSelect(){
    var typeSelect = '<td width=60 align=center>' + I18N.DHCPRELAY.dhcpType + '</td>&nbsp;' +
		    '<td style="width: 141px;">' + 
		    '<select id="serverType" style="width: 141px;">' + 		     
		    '<option value="CM">CM</option>' + 
		    '<option value="HOST">HOST</option>' + 
		    '<option value="MTA">MTA</option>' +
		    '<option value="STB">STB</option>'; 		    
	var typeSelectEnd = '<option value="ALL" selected>ALL</option></select></td>' ;
	if(deviceTypes && deviceTypes.length > 0){
		for(var i = 0; i < deviceTypes.length; i++){
			typeSelect += '<option value="' + deviceTypes[i] + '">'+ deviceTypes[i] +'</option>'
		}
	}
	typeSelect += typeSelectEnd;
	return typeSelect;
}
function removeRecord(id){
	var record = serverStore.getById(id);
	var serverIndex = record.data.helperId;
	serverStore.remove(record);
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
function initIpInput(){
    var server = new ipV4Input("server","span1");
    server.width(141);
}
function createServerGrid(){
    var w = document.body.clientWidth - 30;
    var h = document.body.clientHeight - 120;
    var columns = [
                  {header: I18N.DHCPRELAY.entitytype, width:w/4, align:"center", sortable:false, dataIndex:"deviceTypeStr"},
                  {header: "IP", width:w/4, align:"center", sortable:false, dataIndex:"topCcmtsDhcpHelperIpAddr"},
                  {header: I18N.DHCPRELAY.type, width:w/4-20, align:"center", sortable:false, dataIndex:"topCcmtsDhcpHelperIndex", renderer: stateRender},
                  {header: I18N.DHCPRELAY.dhcpOperation, width:w/4-10, align:"center", sortable:false, dataIndex:"op", renderer: opeartionRender}
                  ];
    var cm = new Ext.grid.ColumnModel(
        columns
    );
    var toolbar = [
                   buildServerTypeSelect(),
                   "IP",
                   buildServerInput(),                   
                   {text: I18N.DHCPRELAY.add, iconCls: "bmenu_new", handler: onAddServer}
                   ];
    serverStore = new Ext.data.JsonStore({
        root:'data',
        pruneModifiedRecords: false,
        proxy:new Ext.data.MemoryProxy(server),
        fields:[
                'helperId',
                'topCcmtsDhcpHelperIndex', 
                'topCcmtsDhcpHelperDeviceType', 
                'topCcmtsDhcpHelperIpAddr',
                'deviceTypeStr'
                ]
    });
    serverGrid  = new Ext.grid.EditorGridPanel({ 
        height: 300,
        trackMouseOver: false, 
        border: true, 
        store: serverStore,
        clicksToEdit: 1,
        tbar: toolbar,
        cm: cm,
        autofill:true,
        renderTo: 'dhcpServer-div',
        cls: 'normalTable',
        viewConfig:{
        	forceFit:true
        }
    });
    serverStore.load();
    initIpInput();
}
Ext.onReady(function (){
	initData();
	initPage();
	createServerGrid();
});


</script>
</HEAD>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	<p><b>DHCP Server</b></p>
	    	<p><span id="newMsg"></span><fmt:message bundle="${epon}" key="DHCPRELAY.dhcpServerConfig"/></p>
	    </div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	<div class="edgeTB10LR20">
		<div id="dhcpServer-div"></div>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT20 noWidthCenter">
		         <li><a id="prevBt" onclick="preClick()" href="javascript:;" class="normalBtnBig"><span><fmt:message bundle="${epon}" key="DHCPRELAY.back"/></span></a></li>
		         <li><a id="nextStep" onclick="nextClick()" href="javascript:;" class="normalBtnBig"><span><fmt:message bundle="${epon}" key="DHCPRELAY.nextStep"/></span></a></li>
		         <li><a id="saveClick" style="display: none;" onclick="onSaveClick()" href="javascript:;" class="normalBtnBig"><span><fmt:message bundle="${epon}" key="DHCPRELAY.finish"/></span></a></li>
		         <li><a id="cancelBt" onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><fmt:message bundle="${epon}" key="DHCPRELAY.cancel"/></span></a></li>
		     </ol>
		</div>
	</div>
	
	

<div class=formtip id=tips style="display: none"></div>


</body>
</HTML>