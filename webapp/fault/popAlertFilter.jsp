<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/cssStyle.inc"%>
<Zeta:Loader>
	css css.main
	library ext
	library jquery
	library zeta
    module fault
</Zeta:Loader>
<%@include file="../include/tabPatch.inc"%>
<link rel="stylesheet" type="text/css" href="../css/jquery.treeview.css"></link>
<script type="text/javascript" src="../js/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../js/tools/treeBuilder.js"></script>
<script type="text/javascript" src="/js/tools/ipText.js"></script>
<script type="text/javascript" src="TipTextArea.js"></script>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resource"/>
<fmt:setBundle basename="com.topvision.ems.fault.resources" var="fault"/>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.fault.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<style type="text/css">
body,html{ height:100%;}
#combineOpenLayer{ width:576px; height:298px; border:1px solid #C3C6CA; position:absolute; top:49px; left:10px; z-index:999; display:none; background-color: white}
#ipFilterOpenLayer{ width:576px; height:298px; border:1px solid #C3C6CA; position:absolute; top:49px; left:10px; z-index:999; display:none; background-color: white}
#type-div{
	width:450px; 
	height: 200px; 
	overflow: auto;
	border: 1px solid rgb(200, 200, 200);
}
.ipTextField input {
	ime-mode: disabled;
	width: 45px;
	border: 0px;
	text-align: center;
}
.vertical-middle{
	vertical-align: middle;
}
.vertical-middle span, .vertical-middle a, .vertical-middle input{
	vertical-align: middle;
}
</style>
<script type="text/javascript">

var addingFlag = 0;

Ext.BLANK_IMAGE_URL = '../images/s.gif';
function minLevelChanged(obj) {
	$.ajax({url: 'saveMinLevel.tv',
		data: {level: obj.options[obj.selectedIndex].value},
		success: function(json) {
			//setTimeout("document.getElementById('view').style.visibility = 'visible'","500");
			//setTimeout("document.getElementById('view').style.visibility = 'hidden'","2000");
			top.afterSaveOrDelete({
   				title: '@COMMON.tip@',
   				html: '<b class="orangeTxt">@ALERT.saveConfigToolTip@</b>'
   			});
		}, error: function() {
			//setTimeout("document.getElementById('viewfail').style.visibility = 'visible'","500");
			//setTimeout("document.getElementById('viewfail').style.visibility = 'hidden'","2000");
			top.afterSaveOrDelete({
   				title: '@COMMON.tip@',
   				html: '<b class="orangeTxt">@ALERT.saveConfigFailToolTip@</b>'
   			});
		}, cache: false});
}
function filterActivedChanged(obj) {
	$.ajax({url: 'saveFilterActived.tv',
		data: {filterActived: obj.checked},
		success: function(json) {
			//setTimeout("document.getElementById('view').style.visibility = 'visible'","500");
			//setTimeout("document.getElementById('view').style.visibility = 'hidden'","2000");
			top.afterSaveOrDelete({
   				title: '@COMMON.tip@',
   				html: '<b class="orangeTxt">@ALERT.saveConfigToolTip@</b>'
   			});
		}, error: function() {
			//setTimeout("document.getElementById('viewfail').style.visibility = 'visible'","500");
			//setTimeout("document.getElementById('viewfail').style.visibility = 'hidden'","2000");
			top.afterSaveOrDelete({
   				title: '@COMMON.tip@',
   				html: '<b class="orangeTxt">@ALERT.saveConfigFailToolTip@</b>'
   			});
		}, cache: false});
}

function onRemoveClick() {
	window.top.showConfirmDlg(I18N.COMMON.tip, I18N.RESOUCES.removeEntityMsg, function(type) {
		if (type == 'no') {return;}
		$.ajax({url: '../network/removeNetworkEntity.tv?', type: 'POST',
		data:{'networkEntity.entityId':getSelecteData().entityId},
		success: function() {store.reload();},
		error: function() {},
		dataType: 'plain', cache: false});
	});
}

var combinateStore;
var combinateGrid;
var ipStore;
var ipGrid;
var typeTree;

//updated by @flackyang 2013-09-29 改为不使用内部弹框的,而是使用新div层遮盖的方式
var ipDlg = null;
function addIpClick() {
	/* 
	if (ipDlg == null) {
		ipDlg = new Ext.Window({
			title: I18N.ALERT.addIpFilter, 
			width: 300, 
			height: 220, 
			modal: true, 
			closable: false, 
			plain: false, 
			resizable: false, 
			shadow: true,
			contentEl: 'segment-div'
		});
	} 
	*/
	/* var startIp = new ipV4Input("startIp","span1");
	startIp.width(190);
	startIp.height(18);
	//startIp.bgColor("white");
	setIpValue("startIp","");
	$("#alarmFilterDesc").val("");
	//setIpText('startIp', '');
	//Zeta$('alarmFilterDesc').value = '';
	ipDlg.show();
	setTimeout(function() {
		ipFocus("startIp",1);
		//Zeta$('startIp1').focus();
	}, 500); */
	var startIp = new ipV4Input("startIp","span1");
	startIp.width(240);
	startIp.height(18);
	setIpValue("startIp","");
	setTimeout(function() {
		ipFocus("startIp",1);
	}, 500);
	$("#alarmFilterDesc").val("");
	$("#ipFilterOpenLayer").fadeIn();
}

function deleteIpClick() {
	var sm = ipGrid.getSelectionModel();
	if(!sm.hasSelection()){
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.ALERT.chooseDeleteIpFilter);
	}
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections();
		var filterIds = [];
		for (var i = 0; i < selections.length; i++) {
			filterIds[i] = selections[i].data.filterId;
		}
		$.ajax({url: 'deleteAlertFilter.tv', type: 'GET',
			data:{filterIds: filterIds},
			success: function() {
				for (var i = selections.length - 1; i >= 0; i--) {
					ipStore.remove(selections[i]);
				}
			},
			error: function() {
				window.top.showErrorDlg();
			},
			dataType: 'plain', cache: false
		});
	}
}
//updated by @flackyang 2013-09-29 改为不使用内部弹框的,而是使用新div层遮盖的方式
function closeIpDlg() {
	/* if (ipDlg != null) {
		ipDlg.hide();
	} */
	$("#ipFilterOpenLayer").fadeOut();
}
//添加Ip过滤器
function addIp() {
	if(addingFlag > 0){
		setTimeout(function(){
			if(addingFlag > 0){
				addingFlag = 0;
			}
		}, 4000);
		return;
	}
	addingFlag++;
	var ip1 = getIpValue('startIp');
	var note = Zeta$('alarmFilterDesc').value;
	if (!ipIsFilled("startIp")) {
		window.top.showMessageDlg(I18N.COMMON.tip,I18N.WorkBench.ipIsInvalid,null,function(){
			ipFocus("startIp",1);
		});
		return;
	}
	for(var i=0,len=ipStore.data.length;i<len;i++){
		var ip = ipStore.getAt(i).data.ip;
		if(ip1 == ip){
			window.top.showMessageDlg(I18N.COMMON.tip, I18N.ALERT.ipFilterExist);
			closeIpDlg();
			return;
		}
	}
	$.ajax({url: 'addAlertFilter.tv', type: 'GET',
		data:{'alertFilter.ip': ip1, 'alertFilter.type': 0, 'alertFilter.note': note},
		success: function(json) {
			closeIpDlg();
			ipStore.reload();
			addingFlag = 0;
		},
		error: function() {
			closeIpDlg();
			window.top.showErrorDlg();
			addingFlag = 0;
		},
		dataType: 'json', cache: false});
}

//updated by @flackyang 2013-09-29 改为不使用内部弹框的,而是使用新div层遮盖的方式
var combinateDlg =  null;
function addCombinedClick() {
	/* 
	if (combinateDlg == null) {
		combinateDlg = new Ext.Window({title: I18N.ALERT.addCombineFilter, width: 300, height: 170, 
			modal: true, closable: true, plain: false, resizable: false, shadow: true,
			contentEl: 'combination-div'
		});
	}
	var combinateIp = new ipV4Input("combinateIp","span2");
	combinateIp.width(190);
	combinateIp.setValue("");
	combinateIp.height(18);
	combinateIp.bgColor("white");
	//setIpText('combinateIp', '');
	combinateDlg.show();
	setTimeout(function() {
		//Zeta$('combinateIp1').focus();
		ipFocus("combinateIp",1);
	}, 500); 
	*/
	var combinateIp = new ipV4Input("combinateIp","span2");
	combinateIp.width(190);
	combinateIp.setValue("");
	combinateIp.height(18);
	setTimeout(function() {
		//Zeta$('combinateIp1').focus();
		ipFocus("combinateIp",1);
	}, 500);
	$("#combineOpenLayer").fadeIn();
}

function deleteCombinedClick() {
	var sm = combinateGrid.getSelectionModel();
	if(!sm.hasSelection()){
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.ALERT.chooseDeleteFilter);
	}
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections();
		var filterIds = [];
		for (var i = 0; i < selections.length; i++) {
			filterIds[i] = selections[i].data.filterId;
		}
		$.ajax({url: 'deleteAlertFilter.tv', type: 'GET',
			data:{filterIds: filterIds},
			success: function() {
				for (var i = selections.length - 1; i >= 0; i--) {
					combinateStore.remove(selections[i]);
				}
			},
			error: function() {
				window.top.showErrorDlg();
			},
			dataType: 'plain', cache: false});
	}
}
//添加组合过滤器
function addCombinateFilter() {
	/* 
	var el = Zeta$('combinateType');
	var type = el.options[el.selectedIndex].value; 
	*/
	var type = $("#combinateType").val();
	var ip1 = getIpValue('combinateIp');
	if (!checkedIpValue(ip1)) {
		window.top.showMessageDlg(I18N.COMMON.tip,I18N.WorkBench.ipIsInvalid,null,function(){
			ipFocus("combinateIp",1);
		});
		return;
    }
	if (ip1 == '0.0.0.0' || ip1 == '127.0.0.1') {
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.WorkBench.ipNotLike + ip1 + '!');
		return;
	}
	for(var i=0,len=combinateStore.data.length;i<len;i++){
		var ip = combinateStore.getAt(i).data.ip;
		var typeId = combinateStore.getAt(i).data.typeId
		if(type == typeId&& ip1 == ip){
			window.top.showMessageDlg(I18N.COMMON.tip, I18N.ALERT.combineFilterExist);
			closeCombinateDlg();
			return;
		}
	}
	//combinateStore
	if (ip1 == '') {
		window.top.showMessageDlg(I18N.COMMON.tip, I18N.ALERT.pleaseEnterIpAddress);
		return;
	}
	$.ajax({url: 'addAlertFilter.tv', type: 'POST',
		data:{'alertFilter.ip': ip1, 'alertFilter.type': 3, 'alertFilter.typeId': type},
		success: function(json) {
			closeCombinateDlg();
			combinateStore.reload();
		},
		error: function() {
			closeCombinateDlg();
			window.top.showErrorDlg();
		},
		dataType: 'json', cache: false});
}

//updated by @flackyang 2013-09-29 改为不使用内部弹框的,而是使用新div层遮盖的方式
function closeCombinateDlg() {
	/* if (combinateDlg != null) {
		combinateDlg.hide();
	} */
	$("#combineOpenLayer").fadeOut();
}

function deleteTypeClick() {
	var sm = ipGrid.getSelectionModel();
	if (sm != null && sm.hasSelection()) {
		var selections = sm.getSelections();
		var filterIds = [];
		for (var i = 0; i < selections.length; i++) {
			filterIds[i] = selections[i].data.filterId;
		}
		$.ajax({url: 'deleteAlertFilter.tv', type: 'GET',
			data:{filterIds: filterIds},
			success: function() {
				for (var i = selections.length - 1; i >= 0; i--) {
					ipStore.remove(selections[i]);
				}
			},
			error: function() {
				window.top.showErrorDlg();
			},
			dataType: 'plain', cache: false});
	}
}
function cancelClick() {
	window.top.closeWindow('modalDlg');
}

function saveTypeFilter() {
    var nodeIds = new Array();
    $('.alertCbx').each(function(){
    	var $cbx = $(this);
    	if($cbx.is(":checked")){
    		if($cbx.next().attr("id")!=null && $cbx.next().attr("id")!=''){
    			nodeIds.push($cbx.next().attr("id"));
    		}
    	}
    });
	$.ajax({url: 'saveAlertTypeFilter.tv', type: 'POST',
		data: {typeIds: nodeIds},
		success: function() {
			//window.top.showMessageDlg(I18N.COMMON.tip, I18N.ALERT.saveAlertTypeFilterSuccess);
			top.afterSaveOrDelete({
   				title: I18N.COMMON.tip,
   				html: '<b class="orangeTxt">' + I18N.ALERT.saveAlertTypeFilterSuccess + '</b>'
   			});
		},
		error: function() {
			window.top.showErrorDlg();
		},
		dataType: 'plain', cache: false});
}

/* var liFormatStr = '<li class="vertical-middle"><span class="{0}"><a href="javascript:;" class="linkBtn" id="{1}">{2}</a></span></li>';
var liFormatStrWithCbx = '<li class="vertical-middle"><span class="{0}"><input class="alertCbx" id="{1}" type="checkbox" {3}/><a href="javascript:;" class="linkBtn" id="{1}">{2}</a></span></li>'; */
//构造告警类型树结构
function showAlertType(){
	//清空树
	$('ul#tree').empty();
	$.ajax({
		url: '../fault/loadAlertTypeFilter.tv',
    	type: 'POST',
    	dataType:"json",
    	async: 'false',
   		success: function(jsonAlertType) {
   			//循环输出各父节点
   			$.each(jsonAlertType, function(index,rootAlert){
   				bulidTree(rootAlert, '');
   				/* var rootStr = String.format(liFormatStr, 'folder', rootAlert.id, rootAlert.text);
   				var $li = $(rootStr);
   				$li.appendTo($("ul#tree"));
   				//只要children不为空，则输出其子节点
   				var children_1 = rootAlert.children;
   				if((children_1!=null && children_1!='')){
   					var $ul = $('<ul></ul>').appendTo($li);
   					$.each(children_1, function(index,secondAlert){
   						if(secondAlert.iconCls=='alertFolderIcon'){
   							secondAlert.iconCls = 'folder';
   						}
   						//需要判断其是否为叶子结点
   						var isLeaf = true;
   						var children_2 = secondAlert.children;
   						if((children_2!=null && children_2!='')){
   							isLeaf = false;
   						}
   						var secondStr = "";
   						if(isLeaf){
   							var checked = '';
   							if(secondAlert.checked==true){
   								checked = 'checked="checked"';
   							}
   							secondStr = String.format(liFormatStrWithCbx, secondAlert.iconCls, secondAlert.id, secondAlert.text, checked);
   						}else{
	   						secondStr = String.format(liFormatStr, secondAlert.iconCls, secondAlert.id, secondAlert.text);
   						}
   						var $secondLi = $(secondStr);
   						$secondLi.appendTo($ul);
   						//部分二级告警下存在三级告警
   						if(!isLeaf){
   							var $ul_2 = $('<ul></ul>').appendTo($secondLi);
   							$.each(children_2, function(index,thirdAlert){
   								if(thirdAlert.iconCls=='alertFolderIcon'){
   									thirdAlert.iconCls = 'folder';
   		   						}
   								var thirdStr = String.format(liFormatStrWithCbx, thirdAlert.iconCls, thirdAlert.id, thirdAlert.text);
   								var $thirdLi = $(thirdStr);
   		   						$thirdLi.appendTo($ul_2);
   							});
   						}
   					});
   				} */
   			});
	   		
   			//$("#typeFilterTab").addClass("x-hide-display");
   		}, error: function(array) {
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

Ext.onReady(function(){
	ipStore = new Ext.data.JsonStore({
	    url: 'loadAlertFilter.tv?filterType=0',
	    root: 'data', totalProperty: 'rowCount', remoteSort: false,
	    fields: ['filterId', 'ip', 'typeName', 'note']
	});
    ipGrid = new Ext.grid.GridPanel({
        renderTo: 'ip-div',
    	width: 450,
    	height: 190,
    	border: true,
		animCollapse: animCollapse,
		trackMouseOver: trackMouseOver,
		//autoExpandColumn: 'note',
		store: ipStore,
        cm: new Ext.grid.ColumnModel([
            {header: I18N.COMMON.ip, width: 140, sortable: true, dataIndex: 'ip'},
            {id: 'note', header: I18N.SYSTEM.note, width: 260, sortable: false, dataIndex: 'note'}
        ])		
    });

    //构造类型过滤器中的告警树
    showAlertType();
    
	/* var treeLoader = new Ext.tree.TreeLoader({dataUrl: "../fault/loadAlertTypeFilter.tv"});
	typeTree = new Ext.tree.TreePanel({el: "type-div",
		useArrows: useArrows, animate: animCollapse, trackMouseOver: trackMouseOver, 
		border: true, autoScroll: true, lines: true, rootVisible: false, enableDD: false,
		width: 450,
    	height: 190,
		loader: treeLoader
	});
	var eventCategoryRoot = new Ext.tree.AsyncTreeNode({text: 'Alert Viewer',
		draggable: false, id: "alertType"});
	typeTree.setRootNode(eventCategoryRoot);
	typeTree.render();
	eventCategoryRoot.expand(); */

    var columnModels = [
		{header: I18N.COMMON.ip, width: 140, sortable: true, dataIndex: 'ip'},
    	{id: 'typeName', header: I18N.ALERT.alertType, width: 260, sortable: false, dataIndex: 'typeName'}
	];
	combinateStore = new Ext.data.JsonStore({
	    url: 'loadAlertFilter.tv?filterType=3',
	    root: 'data', totalProperty: 'rowCount', remoteSort: false,
	    fields: ['filterId', 'ip', 'typeName','typeId', 'note']
	});
    combinateGrid = new Ext.grid.GridPanel({
    	renderTo: 'combined-div',
    	width: 450,
    	height: 190,
    	border: true,
		animCollapse: animCollapse, trackMouseOver: trackMouseOver,
		//autoExpandColumn: 'typeName',
		store: combinateStore,
        columns: columnModels
    });
    
	var w = $(window).width() - 20;
	var h = $(window).height() - 70;
    var tabs = new Ext.TabPanel({
        width: w,
        height: h,
        activeTab: 0,
        frame: true,
        items:[
        	{id: 'tab1', contentEl: 'generalTab', title: I18N.ALERT.generalParam, listeners: {activate: handleActivate}},
            {id: 'tab2', contentEl: 'ipFilterTab', title: I18N.ALERT.ipFilter, listeners: {activate: handleActivate}},
            {id: 'tab3', contentEl: 'typeFilterTab', title: I18N.ALERT.alertTypeFilter, listeners: {activate: handleActivate2}},
            {id: 'tab4', contentEl: 'combinedTab', title: I18N.ALERT.combineFilter, listeners: {activate: handleActivate}}
        ]
    });
    tabs.render('tabs');
    tabs.on('tabchange', function(panel, tab) {
    	if(tab.id == 'tab2') {
    		ipStore.load();	
    	} else if (tab.id == 'tab4') {
    		combinateStore.load(); 
    	}
    });       

	var el = Zeta$('minLevel');
	var options = el.options;
	for (var i = options.length - 1; i >= 0; i--) {
		if (options[i].value == <s:property value="level"/>) {
			el.selectedIndex = i;
			break;	
		}
	}

    Zeta$('buttonPanel').style.display = '';
});

//在切换Tab页时关闭添加的页面    added by @flackyang 2013-09-29
function handleActivate(){
	closeCombinateDlg();
	closeIpDlg();
}
function handleActivate2(){
	//在第一次点击时生成树结构的一些基本动画效果
	if(!handleActivate2.flag){
		handleActivate2.flag = true;
		treeBasicHandle();
	}
	closeCombinateDlg();
	closeIpDlg();
}


$(function(){
	var t = new TipTextArea({
		id : "alarmFilterDesc",
		tipsId : "tipTextArea",
		maxLength:128
	});
	t.init();
	
})
</script>	  
</head>
<body class="openWinBody">
	<div id="tabs" class="edge10">
		<div id="generalTab" class="x-hide-display">
		  	<table cellspacing=10>
			 	<tr>
					<td>
						<div id=view
							style="visibility: hidden; position: absolute; top: 2px; left: 200px; width: 250px; height: 10px; z-index: 9999;">
			 				<table width="80" border="1" bordercolor="#FFFFCC"	cellspacing="0" cellpadding="0" height="30" bgcolor="#FFFFCC">
								<tr>
									<td></td>
									<td align="center"><font color="#000000"><fmt:message key="ALERT.saveConfigToolTip" bundle="${fault}"/></font></td>
								</tr>
							</table>
						</div>
						<div id=viewfail
							style="visibility: hidden; position: absolute; top: 2px; left: 200px; width: 250px; height: 10px; z-index: 9999;">
			 				<table width="80" border="1" bordercolor="#FFFFCC"	cellspacing="0" cellpadding="0" height="30" bgcolor="#FFFFCC">
								<tr>
									<td></td>
									<td align="center"><font color="#000000"><fmt:message key="ALERT.saveConfigFailToolTip" bundle="${fault}"/></font></td>
								</tr>
							</table>
						</div>
					</td>
				</tr>
				<tr><td width=40px><img src="../images/icon-info.gif" border=0 align="absmiddle" /></td><td>
				<input id="filterActived" type=checkbox <s:if test="filterActived">checked</s:if> onclick="filterActivedChanged(this)" /><label for="filterActived">&nbsp;&nbsp;<fmt:message key="ALERT.activateAlertFilter" bundle="${fault}"/></label>
				</td></tr>
				<tr><td></td><td style="padding-left:20px;"><fmt:message key="ALERT.ipFilterComment" bundle="${fault}"/></td></tr>
				<tr><td></td><td style="padding-left:20px;"><fmt:message key="ALERT.typeFilterComment" bundle="${fault}"/></td></tr>
				<tr><td></td><td style="padding-left:20px;"><fmt:message key="ALERT.combineFilterComment" bundle="${fault}"/></td></tr>
			
				<tr><td width=40px style="padding-top:10px;"><img src="../images/icon-info.gif" border=0 align="absmiddle" /> </td><td style="padding-left:5px;"><br><fmt:message key="ALERT.currentSystemAlertLevel" bundle="${fault}"/>:</td></tr>
				<tr><td></td><td style="padding-left:22px;">
					<select id="minLevel" style="width:150px;" onchange="minLevelChanged(this);">
					<option value="6"><fmt:message key="WorkBench.emergencyAlarm" bundle="${resource}"/></option>
					<option value="5"><fmt:message key="WorkBench.seriousAlarm" bundle="${resource}"/></option>
					<option value="4"><fmt:message key="WorkBench.mainAlarm" bundle="${resource}"/></option>
					<option value="3"><fmt:message key="WorkBench.minorAlarm" bundle="${resource}"/></option>
					<option value="2"><fmt:message key="WorkBench.generalAlarm" bundle="${resource}"/></option>
					<option value="1"><fmt:message key="WorkBench.message" bundle="${resource}"/></option>
					</select>
				</td></tr>			
		  	</table>
		</div>
		<div id="ipFilterTab" class="x-hide-display">
			<table>
			<tr><td style="padding-top:10px;padding-left:10px;" colspan=2><fmt:message key="ALERT.filterAlertComment0fIp" bundle="${fault}"/></td></tr>
			<tr><td style="padding:10px;"><div id="ip-div"></div></td>
			<td align=right style="padding-right:10px;">
				<a href="javascript:addIpClick();" class="normalBtn"><span><i class="miniIcoAdd"></i><fmt:message key="COMMON.add" bundle="${resource}"/></span></a><br></br>
				<a href="javascript:deleteIpClick();" class="normalBtn"><span><i class="miniIcoClose"></i><fmt:message key="MAIN.deleteFolder" bundle="${resource}"/></span></a>
			</td></tr>		
		  	</table>    
		</div>
		<div id="typeFilterTab" class="x-hide-display">
		  	<table>
			<tr><td style="padding-top:12px;padding-left:10px;" colspan=2><fmt:message key="ALERT.filterAlertComment0fBelow" bundle="${fault}"/></td></tr>
			<tr>
				<td style="padding:10px;">
					<div id="type-div">
						<ul id="tree" class="filetree">
						</ul>
					</div>
				</td>
				<td align=right style="padding-right:10px;">
					<a href="javascript:saveTypeFilter();" class="normalBtn">
						<span><i class="miniIcoData"></i>@BUTTON.save@</span>
					</a>
				</td>
			</tr>		
		  	</table>
		</div>    
		<div id="combinedTab" class="x-hide-display">
		  	<table>
			<tr><td style="padding-top:10px;padding-left:10px;" colspan=2><fmt:message key="ALERT.filterAlertComment0fSourceAndType" bundle="${fault}"/></td></tr>
			<tr><td style="padding:10px;"><div id="combined-div"></div></td>
			<td align=right style="padding-right:10px;">
				<a href="javascript:addCombinedClick();" class="normalBtn"><span><i class="miniIcoAdd"></i><fmt:message key="COMMON.add" bundle="${resource}"/></span></a><br></br>
				<a href="javascript:deleteCombinedClick();" class="normalBtn"><span><i class="miniIcoClose"></i><fmt:message key="MAIN.deleteFolder" bundle="${resource}"/></span></a>
			</td></tr>			
		  	</table>
	   	</div>
	</div>
	
	<div id="ipFilterOpenLayer">
		<div class="openWinHeader">
		    <div class="openWinTip">@ALERT.addIpFilter@</div>
		    <div class="rightCirIco wheelCirIco"></div>
		</div>
		<div class="edgeTB10LR20 pT30">
			<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		        <tbody>
		        	<tr>
		        		<td class="rightBlueTxt" width="160"><fmt:message key="COMMON.ip" bundle="${resource}"/>:<font color=red>*</font></td>
		        		<td><span id="span1"></span></td>
					</tr>
					<tr class="darkZebraTr">
						<td class="rightBlueTxt" width="160"><fmt:message key="COMMON.note1" bundle="${resource}"/></td>
						<td>
							<!-- <textarea rows=2 id="alarmFilterDesc" style="width:190px;overflow:auto;"></textarea> -->
							<textarea id="alarmFilterDesc" name="alarmFilterDesc" rows=3 class="normalInput"  style="width:240px;height:40px;overflow:auto;" ></textarea>
						</td>
					</tr>	
					<tr>
						<td class="rightBlueTxt" width="160"></td>
						<td><div id="tipTextArea"></div></td>
					</tr>
				</tbody>
			</table>
			<div class="noWidthCenterOuter clearBoth"  id="combineButtonPanel">
			    <ol class="upChannelListOl pB0 pT30 noWidthCenter">
				<li><a href="javascript:addIp();" class="normalBtn"><span><i class="miniIcoData"></i>@BUTTON.save@</span></a></li>
			    <li><a href="javascript:closeIpDlg();" class="normalBtn"><span><i class="miniIcoForbid"></i>@BUTTON.cancel@</span></a></li>
			</ol>
			</div>
		</div>
	</div>

	<div id="combineOpenLayer">
		<div class="openWinHeader">
		    <div class="openWinTip">@ALERT.addCombineFilter@</div>
		    <div class="rightCirIco wheelCirIco"></div>
		</div>
		<div class="edgeTB10LR20 pT40">
			<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		        <tbody>
		        	<tr>
		        		<td class="rightBlueTxt" width="160"><fmt:message key="COMMON.ip" bundle="${resource}"/>: <font color=red>*</font></td>
		        		<td><span id="span2"></span></td>
					</tr>
					<tr class="darkZebraTr">
						<td class="rightBlueTxt" width="160"><fmt:message key="ALERT.alertType" bundle="${fault}"/>: <font color=red>*</font></td>
						<td>
							<select id="combinateType" class="normalSel w190">
							    <s:iterator value="combinateAlertTypes">
								    <s:if test="%{category < 0}">
								    	<option value="<s:property value="typeId"/>"><s:property value="displayName"/></option>
								    </s:if>
							    </s:iterator>
							</select>
						</td>
					</tr>	
				</tbody>
			</table>
			<div class="noWidthCenterOuter clearBoth"  id="combineButtonPanel">
			    <ol class="upChannelListOl pB0 pT40 noWidthCenter">
			       <li><a href="javascript:addCombinateFilter();" class="normalBtn"><span><i class="miniIcoData"></i>@BUTTON.save@</span></a></li>
			    <li><a href="javascript:closeCombinateDlg();" class="normalBtn"><span><i class="miniIcoForbid"></i>@BUTTON.cancel@</span></a></li>
			    </ol>
			</div>
		</div>
	</div>
	
	<div id="buttonPanel" class="noWidthCenterOuter clearBoth pB0">
	     <ol class="upChannelListOl pB0 pT10 noWidthCenter">  
	         <li><a href="javascript:cancelClick();" class="normalBtnBig"><span><i class="miniIcoWrong"></i><fmt:message key="COMMON.off" bundle="${resource}"/></span></a></li>
	     </ol>
	</div>
	
</body>
</Zeta:HTML>
