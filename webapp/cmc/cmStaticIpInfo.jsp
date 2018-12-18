<%@ page language="java" contentType="text/html;charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib prefix="s" uri="/struts-tags"%>
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
<script type="text/javascript" src="/js/zetaframework/IpTextField.js"></script>
<script type="text/javascript" src="/js/ext/ext-lang-<%= lang %>.js"></script>
<script type="text/javascript">
Ext.BLANK_IMAGE_URL = '/images/s.gif';
var cmId = <s:property value="cmId"/>;
var cpeGrid = null;

/*************
 * 页面初始化区**
 *************/
Ext.onReady(function(){
	initStaticIpGrid();
});

/**
 * 初始化表格
 */
function initStaticIpGrid(){
	w = 255;
	h = 310;
	var columns = [
					{header:"IP",align : 'center',width:230,dataIndex:'topCcmtsCmStaticIP'}
				  ];
	
	cpeStore = new Ext.data.JsonStore({
	    url: '/cm/loadCmStaticIpList.tv?cmId='+cmId,
	    root: 'data',
	    remoteSort: true, 
	    fields: [{name:'topCcmtsCmStaticIP'}]
	});
	
	var cm = new Ext.grid.ColumnModel(columns);
	
	cpeGrid = new Ext.grid.GridPanel({
		id:"staticIpGrid",
		renderTo:"staticIpList",
        height:260,
        cm:cm, 
        cls:'normalTable',
        store:cpeStore,
        viewConfig:{
        	forceFit:true
        }
        //bbar: buildPagingToolBar()
	});
	cpeStore.load();
}

function cancelClick(){
	window.parent.closeWindow('cmStaticIpInfo');
}
</script>
</head>
<body class="openWinBody">
	<div id="staticIpList" class="edge10"></div>
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 pT0 noWidthCenter">
	         <li><a onclick="cancelClick()" id=cancelBt href="javascript:;" class="normalBtnBig"><span><i class="miniIcoWrong"></i><fmt:message bundle="${cmc}" key="CMC.button.close" /></span></a></li>
	     </ol>
	</div>
	

</body>
</html>