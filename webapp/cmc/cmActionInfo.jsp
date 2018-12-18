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
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
Ext.BLANK_IMAGE_URL = '/images/s.gif';
var cmId = '<s:property value="cmId"/>';
var cmActionGrid = null;
/*************
 * 页面初始化区**
 *************/
Ext.onReady(function(){
	initCmActionGrid();
});
function actionRender(value, cellmate, record){
		var status = record.data.action;
		if( status == 1){
			return String.format(I18N.linkDown);
		}else{
			return String.format(I18N.linkUp);
		}
}
/**
 * 初始化表格
 */
function initCmActionGrid(){
	w = 560;
	h = 310;
	var columns = [
					{header:"IP",align : 'center',width:w/4,dataIndex:'cmipString'},
					{header:"MAC",align : 'center',width:w/4,dataIndex:'cmmacString'},
					{header:I18N.cmList.action,align : 'center',width:w/5,dataIndex:'action',renderer: actionRender},
					{header:I18N.PERF.mo.time,align : 'center',width:w/4,dataIndex:'realtimeString'}
				  ];
	
	store = new Ext.data.JsonStore({
	    url: '/cm/loadCmActionInfo.tv?cmId='+cmId,
	    root: 'data',
	    remoteSort: true, 
	    fields: [{name:'cmip'},{name:'cmmac'},{name:'action'},{name:'realtime'},{name:'cmipString'},{name:'realtimeString'},{name:'cmmacString'}]
	});
	
	var cm = new Ext.grid.ColumnModel(columns);
	
	cmActionGrid = new Ext.grid.GridPanel({
		id:"gridCmInfoList",
		renderTo:"cmInfoList",
		height: 250,
		cls: 'normalTable',
        cm:cm, 
        store:store,
        viewConfig:{
        	forceFit:true        	
        }
	});
	store.load();
}

function cancelClick(){
	window.parent.closeWindow('cmAction');
}

</script>
</head>
<body class="openWinBody">
	<div id="cmInfoList" class="edge10"></div>
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 pT0 noWidthCenter">
	         <li><a onclick="cancelClick()" id=cancelBt href="javascript:;" class="normalBtnBig"><span><i class="miniIcoWrong"></i><fmt:message bundle="${cmc}" key="CMC.button.close" /></span></a></li>
	     </ol>
	</div>
	
	
</body>
</html>