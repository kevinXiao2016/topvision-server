<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@ include file="/include/nocache.inc" %>
<%@ include file="/include/cssStyle.inc" %>
<fmt:setBundle basename="com.topvision.ems.cmc.resources" var="cmc" />
<link rel="stylesheet" type="text/css" href="/css/gui.css"/>  
<link rel="stylesheet" type="text/css" href="/css/ext-all.css"/>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/xtheme.css"/>
<link rel="stylesheet" type="text/css" href="/css/<%= cssStyleName %>/mytheme.css"/>
<script type="text/javascript" src="/js/ext/ext-base.js"></script>
<script type="text/javascript" src="/js/ext/ext-all.js"></script>
<script type="text/javascript" src="/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/js/zetaframework/zeta-core.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
Ext.BLANK_IMAGE_URL = '/images/s.gif';
var cmId = '<s:property value="cmId"/>';
var cpeClearSurport = <s:property value="cpeClearSurport"/>;
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var cpeGrid = null;
var cpeStore = null;
/*************
 * 页面初始化区**
 *************/
Ext.onReady(function(){
	initCpeGrid();
});
function clearCpe(cmId, mac){
	window.top.showOkCancelConfirmDlg(I18N.RECYLE.tip, I18N.cpe.confirmClear, function (type) {
	    if(type=="ok"){
	        window.top.showWaitingDlg(I18N.COMMON.waiting, I18N.text.configuring, 'ext-mb-waiting');
	        $.ajax({
	          url: '/cm/clearCpeInfo.tv?cmId='+cmId+"&cpeMac="+mac,
	          type: 'post',
	          success: function(response) {
	                if(response == "success"){                  
	                    window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.cpe.clearSuccess);
	                 }else{
	                     window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.cpe.clearFail);
	                 }
	                cpeStore.reload();
	            }, error: function(response) {
	                window.parent.showMessageDlg(I18N.RECYLE.tip, I18N.text.modifyFailureTip);
	            }, cache: false
	        });
        }
    });
}
function opeartionRender(value, cellmate, record){
    var statusMacAddress = record.data.topCmCpeMacAddressString;
    if(operationDevicePower && cpeClearSurport){
        return String.format("<img src='/images/delete.gif' title='" + I18N.cpe.clear + "'" + 
                "onclick='clearCpe(\"{0}\",\"{1}\")' style='cursor:pointer;'/>", 
                cmId, statusMacAddress);
    }else{
        return String.format("<img src='/images/deleteDisable.gif' title='" + I18N.cpe.clear + "' />" );
    }
}
/**
 * 初始化表格
 */
function initCpeGrid(){
	w = 560;
	h = 310;
	var columns = [
					{header:"IP",align : 'center',width:w/4,dataIndex:'topCmCpeIpAddress'},
					{header:"MAC",align : 'center',width:w/3,dataIndex:'topCmCpeMacAddressString'},
					{header:I18N.CMC.text.type,align : 'center',sortable:false,dataIndex:'topCmCpeTypeString'},
					{header:I18N.CM.operate,align : 'center',sortable:false,dataIndex: 'op',renderer: opeartionRender}
				  ];
	
	cpeStore = new Ext.data.JsonStore({
	    url: '/cmlist/loadCmCpeList.tv?cmId='+cmId,
	    root: 'data',
	    remoteSort: true, 
	    fields: [{name:'topCmCpeIpAddress'},{name:'topCmCpeMacAddressString'},{name:'topCmCpeTypeString'}, {name:'op'}]
	});
	
	var cm = new Ext.grid.ColumnModel(columns);
	
	cpeGrid = new Ext.grid.GridPanel({
		id:"gridcpeList",
		renderTo:"cpeList",
		cls: "normalTable",
		height: 260,
        cm:cm, 
        store:cpeStore,
        viewConfig:{
        	forceFit:true
        }
        //bbar: buildPagingToolBar()
	});
	cpeStore.load();
}

function cancelClick(){
	window.parent.closeWindow('cmCpeInfo');
}

</script>
</head>
<body class="openWinBody">
	<div id="cpeList" class="edge10"></div>
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 pT0 noWidthCenter">
	         <li><a onclick="cancelClick()" id=cancelBt href="javascript:;" class="normalBtnBig"><span><i class="miniIcoWrong"></i><fmt:message bundle="${cmc}" key="CMC.button.close" /></span></a></li>
	     </ol>
	</div>
		
	
</body>
</html>