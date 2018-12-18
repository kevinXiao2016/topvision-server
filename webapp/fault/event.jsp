<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    plugin DateTimeField
    module fault
    import js/nm3k/nm3kPickDate
</Zeta:Loader>

<style type="text/css">
label {
	color: #0267B7;
}

</style>
<script type="text/javascript" src="/js/nm3k/Nm3kLevelSelect.js"></script>
<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.fault.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/zetaframework/jquery.autocomplete.min.js"></script>
<script type="text/javascript">
var entityId = 3000000;
var type = 1
var ipNameList = ${ipNameJson};
var pageSize = <%= uc.getPageSize() %>;
<%
	boolean clearevent = uc.hasPower("clearevent");
%>
var clearevent = <%=uc.hasPower("clearevent")%>;
var eventId = '<s:property value="eventId"/>';
var eventTypeId = '<s:property value="eventTypeId"/>';

//开始时间与结束时间控件
var stTime,etTime;
//设置默认查询一周的数据
var currentTime = Ext.util.Format.date(new Date(), 'Y-m-d H:i:s');
var lastWeek = new Date(); 
lastWeek.setTime(lastWeek.getTime()-(7*24*60*60*1000));
lastWeek = Ext.util.Format.date(lastWeek, 'Y-m-d H:i:s');

function query() {
	var startTime = Ext.util.Format.date(window.stTime.getValue(), 'Y-m-d H:i:s');
	var endTime = Ext.util.Format.date(window.etTime.getValue(), 'Y-m-d H:i:s');
	store.baseParams={
        startTime: startTime,
		endTime: endTime,
		host:$('#deviceIp').val(),
		eventType: $('#eventType').val(),
		message: $('#eventReason').val(),
        start:0,
        limit:pageSize
    };
	store.load({
		callback: function(records) {
			if (records && records.length == 0) {
				top.nm3kAlertTips({
					title:I18N.COMMON.tip,
					html:'<b class="orangeTxt">' + I18N.ALERT.noEventData + '</b>',
					okBtnTxt:I18N.COMMON.ok
				});
				//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ALERT.noEventData)
			}else{
				top.nm3kAlertTipsDie();//如果有这个弹出框,那么先删除;
			}
		}
	})
}
function getLasteventId() {
	var model =  grid.getSelectionModel()
	if (model != null) {
		if (model.hasPrevious()) {
			model.selectPrevious(false)
		} else {
			model.selectLastRow(false)
		}
		var record = model.getSelected()
		return record.data.eventId
	}
	return 0
}
function getNexteventId() {
	var model =  grid.getSelectionModel()
	if (model != null) {
		if (model.hasNext()) {
			model.selectNext(false)
		} else {
			model.selectFirstRow(false)
		}
		var record = model.getSelected()
		return record.data.eventId
	}
	return 0
}
function printClick() {
	var wnd = window.open()
	showPrintWnd(Zeta$('event-div'), wnd.document)
	var print = function() {
		wnd.print()
		wnd.close()
	}
	if(isFirefox) {
		wnd.setTimeout(print, 500)
	} else {
		print()
	}	
}
function showPrintWnd(obj, doc) {
	if(doc == null) {
		var wnd = window.open()
		doc = wnd.document
	} else {
		doc.open()
	}
	doc.write('<html>')
	doc.write('<head>')
	doc.write('<title>' + I18N.EVENT.eventViewer + '[<s:property value="entity.ip"/>]</title>')
	doc.write('<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />')
	doc.write('<link rel="stylesheet" type="text/css" href="/css/print.css"/>')
	Zeta$NoHeaderAndFooter(doc)
	doc.write('</head>')
	doc.write('<body style="margin:50px;"><center>')
	doc.write(obj.innerHTML)
	doc.write('</center></body>')
	doc.write('</html>')
	doc.close()
}
function onRefreshClick() {
	store.reload()
}
//在点击左侧事件类型时的刷新，此时的刷新应该只带事件类型参数
function doRefresh(){
	store.baseParams = {};
	store.setBaseParam("eventType", eventTypeId);
	store.load({
		params:{start:0, limit: pageSize}
	});
};

function doOnResize() {
	var w = document.body.clientWidth - 30
	var h = document.body.clientHeight - 100
	grid.setWidth(w)
	grid.setHeight(h)
}
function eventTypeChanged(obj) {
	if (obj.options[obj.selectedIndex].value == 1) {
		location.href = '/event/showEntityeventJsp.tv?module=6&type=1&entityId=' + entityId + '&entityType=olt'
	} else if (obj.options[obj.selectedIndex].value == 2) {
		location.href = '/event/showEntityeventJsp.tv?module=6&type=2&entityId=' + entityId + '&entityType=olt'
	} else if (obj.options[obj.selectedIndex].value == 3) {
		location.href = '/event/showEntityHistoryeventJsp.tv?module=6&type=3&entityId=' + entityId + '&entityType=olt'
	} else if (obj.options[obj.selectedIndex].value == 4) {
		location.href = '/event/showEntityHistoryeventJsp.tv?module=6&type=4&entityId=' + entityId + '&entityType=olt'
	}
}
function pageBoxChanged(obj) {
	var p = obj.options[obj.selectedIndex].value;
	if(p == pageSize) return
	pageSize = parseInt(p)
	pagingToolbar.pageSize = pageSize
	pagingToolbar.doLoad(0)
}

function onClearAlarmClick() {
	var sm = grid.getSelectionModel()
	if (sm != null && sm.hasSelection()) {
		window.parent.showTextAreaDlg(I18N.ALERT.clearEvent, I18N.COMMON.description + ':', function (type, text) {
			if (type == "cancel") {
				return
			}
			var eventIds = []
			var selections = sm.getSelections()
			for (var i = 0; i < selections.length; i++) {
				eventIds[i] = selections[i].data.eventId
			}
			Ext.Ajax.request({url:"/fault/clearevent.tv", method:"post", success:function (response) {
				onRefreshClick()
			}, failure:function () {
			}, params:{eventIds: eventIds, message:text}})
		})
	} else {
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ALERT.chooseEventRecord)
	}
}
function onConfirmAlarmClick() {
	var sm = grid.getSelectionModel()
	var selections = sm.getSelections()
	for (var i = 0; i < selections.length; i++) {
		 if(selections[i].data.status == 1){
			 window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ALERT.eventConfirmed)
			 return
			 }
	}
	if (sm != null && sm.hasSelection()) {
		window.parent.showTextAreaDlg(I18N.ALERT.confirmEvent, I18N.COMMON.description + ':', function (type, text) {
		if (type == "cancel") {
			return
		}
		var eventIds = []
		var selections = sm.getSelections()
		for (var i = 0; i < selections.length; i++) {
			eventIds[i] = selections[i].data.eventId
		}
		Ext.Ajax.request({url:"/fault/confirmevent.tv", method:"post",
			success:function (response) {
				onRefreshClick()
			}, failure:function () {
			}, params:{eventIds: eventIds, message: text}})
	})
	} else {
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ALERT.chooseEvent)
	}
}
function buildPagingToolBar() {
	pagingToolbar = new Ext.PagingToolbar({
		id: 'extPagingBar', pageSize: pageSize, store:store, displayInfo:true,
		items:['-', String.format(I18N.COMMON.displayPerPage, pageSize)]
	});
	return pagingToolbar;
}

/**
 * desc renderer
 */
function renderNote(value, p, record) {
	return "<span class='pL5'>" + record.data.message + "</span>" || "<span class='pL5'>-</span>"
}

function renderStatus(value, p, record) {
	if (record.data.status == '1') {
		return String.format('<img hspace=5 src=\"/images/fault/confirm.png\" border=0 align=absmiddle title="{0}">', I18N.FAULT.confirmed);
	} else {
		return String.format('<img hspace=5 src=\"/images/fault/unconfirm.png\" border=0 align=absmiddle title="{0}">', I18N.FAULT.unconfirmed);	
	}
}

function getLasteventId() {
	var model =  grid.getSelectionModel();
	if (model != null) {
		if (model.hasPrevious()) {
			model.selectPrevious(false);
		} else {
			model.selectLastRow(false);
		}
		var record = model.getSelected();
		return record.data.eventId;
	}
	return 0;
}
function getNexteventId() {
	var model =  grid.getSelectionModel();
	if (model != null) {
		if (model.hasNext()) {
			model.selectNext(false);
		} else {
			model.selectFirstRow(false);
		}
		var record = model.getSelected();
		return record.data.eventId;
	}
	return 0;
}

var	EVENT_TYPE_OPTION_FMT = '<option value="{0}" title="{1}" {2}>{1}</option>';
var	NOT_SELECTED = "";
var	SELECTED = " selected";
function initData() {
	// 初始化查询条件
	$.ajax({
		url: '/fault/getEventTypeTree.tv',
		cache:false,
		dataType:'json',
		success: function(response) {
			var all = {text:"@COMMON.all@", value:"0",child:[]};
			response.unshift(all);
			var s = new Nm3kLevelSelect({
		         id : "eventType",
		         renderTo : "putSel",
		         width : 150,
		         subWidth : 400,
		         firstSelectValue : eventTypeId,
		         dataArr : response
		    })
		    s.init(); 
		}
	});
}

function rendererHandler(value,c,record){
    if (clearevent) {
        return String.format("<a href='javascript:;' onClick='deleteRecord({0});'>@COMMON.delete@</a>", record.id);
    } else {
        return "";
    }
}

function viewCmtsSnap(cmcId, manageIp) {
	var recNum = grid.getStore().find("entityId",cmcId);//grid.getSelectionModel().getSelected()
	var record = grid.getStore().getAt(recNum);
    window.parent.addView('entity-' + cmcId, record.data.host.split("[")[1].split("]")[0], 'entityTabIcon',
            '/cmts/showCmtsPortal.tv?cmcId=' + cmcId);
}

function renderDevice(v,c,r){
	var typeId = r.data.entityType;
	if(EntityType.isCmtsType(typeId)){
		var formatStr = "<a href='#' onclick='viewCmtsSnap(\"{0}\",\"{1}\")'>{2}</a>";
		return String.format(formatStr, r.data.entityId, r.data.cmtsIp, v);
	}else{
		var $source = r.data.host;
		if( $source.indexOf("[") > -1 ){
			return "<a href='#' onclick='showDevice("+r.data.entityId+")'>"+v+"</a>"
		}else{
			return v;
		}
	}
}

function showDevice(id,ip){
	var record = grid.getSelectionModel().getSelected()
	var $host = record.data.host.split("[")[1];		
	var $name = $host.substring(0, $host.length-1);
	var typeId = record.data.entityType;
	if(EntityType.isOltType(typeId)){//olt
		window.top.addView('entity-' + id,  $name ,
				'entityTabIcon', 'portal/showEntitySnapJsp.tv?entityId=' + id);
	}else if(EntityType.isCcmtsType(typeId)){//8800b
		window.top.addView('entity-' + id,  $name ,
				'entityTabIcon', 'portal/showEntitySnapJsp.tv?entityId=' + id);
	}else{
		//未知类型设备，需要单独做页面
		return ;
	}
}

function deleteRecord(id){
	window.parent.showConfirmDlg(I18N.COMMON.tip,I18N.ALERT.confirmDeleteEvent,function(text){
		if("yes" == text){
			$.ajax({
				url : '/fault/deleteEvent.tv',cache:false,method:'post',
				data : {eventId : id},
				success:function(){
					store.reload()
					//window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ALERT.deleteSuccess);
					top.afterSaveOrDelete({
		   				title: I18N.COMMON.tip,
		   				html: '<b class="orangeTxt">' + I18N.ALERT.deleteSuccess +'</b>'
		   			});
				},error:function(){
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ALERT.deleteFail);
				}
			})
		}
	})
}

//批量删除事件
function batchDeleteEvent(){
	var sm = grid.getSelectionModel();
	var selections = sm.getSelections();
	if (sm != null && sm.hasSelection()) {
		window.parent.showConfirmDlg(I18N.COMMON.tip,I18N.ALERT.deleteSelectedEvent,function(text){
			if("yes" == text){
				var len = selections.length;
				var eventIdArray = [];
				for (var i = 0; i < len; i++) {
					eventIdArray[i] = selections[i].data.eventId;
				}
				$.ajax({
					url : '/fault/batchDeleteEvent.tv',
					cache:false,
					method:'post',
					data : {
						eventIds : eventIdArray.join(",")
					},
					success:function(){
						store.reload();
						top.afterSaveOrDelete({
					        title: '@COMMON.tip@',
					        html: '<b class="orangeTxt">' + I18N.ALERT.deleteSuccess + '</b>'
					    });
					},error:function(){
						window.parent.showMessageDlg(I18N.COMMON.tip, I18N.ALERT.deleteFail);
					}
				})
			}else{
				return;
			}
		});
	}else{
		window.parent.showMessageDlg("@COMMON.tip@", "@ALERT.chooseEvent@");
	}
}

var store = null;
var grid = null;
Ext.onReady(function () {
	Ext.Ajax.timeout = 600000; 
	Ext.override(Ext.grid.GridView,{  
	    
	    onRowSelect : function(row){  
	        this.addRowClass(row, "x-grid3-row-selected");  
	        var selected = 0;  
	        var len = this.grid.store.getCount();  
	        for(var i = 0; i < len; i++){  
	            var r = this.getRow(i);  
	            if(r){  
	               if( this.fly(r).hasClass('x-grid3-row-selected'))selected = selected + 1;  
	            }  
	        }  
	          
	        var hd = this.grid.getEl().select('div.x-grid3-hd-checker').first();     
	          
	        if (selected == len && !hd.hasClass('x-grid3-hd-checker-on')) {  
	             hd.addClass('x-grid3-hd-checker-on');   
	        }  
	    },  
	  
	    onRowDeselect : function(row){  
	        this.removeRowClass(row, "x-grid3-row-selected");  
	            var selected = 0;  
	            var len = this.grid.store.getCount();  
	            for(var i = 0; i < len; i++){  
	                var r = this.getRow(i);  
	                if(r){  
	                   if( this.fly(r).hasClass('x-grid3-row-selected'))selected = selected + 1;  
	                }  
	            }  
	            var hd = this.grid.getEl().select('div.x-grid3-hd-checker').first();     
	              
	            if (hd != null && selected != len && hd.hasClass('x-grid3-hd-checker-on')) {  
	                 hd.removeClass('x-grid3-hd-checker-on');   
	            }  
	    }  
	});  
	initData();
	
	//时间控件创建
	var current = new Date();
	current.setHours(23);
	current.setMinutes(59);
	current.setSeconds(59);
	window.stTime = new Ext.ux.form.DateTimeField({
		width:150,
		value : lastWeek,
		maxValue: current,
		editable: false,
		renderTo:"startTime",
		emptyText:'@performance/Tip.pleaseEnterTime@',
	    blankText:'@performance/Tip.pleaseEnterTime@',
	    format : 'Y-m-d H:i:s',  //日期格式
	    listeners: {
	    	"select": function(){
	    		//修改结束时间的最小值限制
	    		etTime.setMinValue(stTime.getValue());
	    		//如果此时开始时间小于等于结束时间，则去掉所有的错误提示
				var startTime = stTime.value;
	    		if(!etTime.value) return;
				var	endTime = etTime.value;
	    		if(startTime<=endTime){
	    			stTime.removeClass('x-form-invalid');
	    			etTime.removeClass('x-form-invalid');
	    		}
			}
		}
	});
	window.etTime = new Ext.ux.form.DateTimeField({
		width:150,
		value : currentTime,
		minValue:stTime.getValue(),
		editable: false,
		renderTo:"endTime",
		emptyText:'@performance/Tip.pleaseEnterTime@',
	    blankText:'@performance/Tip.pleaseEnterTime@',
	    format : 'Y-m-d H:i:s',  //日期格式
	    listeners: {
			"select": function(){
				//修改结束时间的最大值限制
				stTime.setMaxValue(etTime.getValue());
				//如果此时开始时间小于等于结束时间，则去掉所有的错误提示
				if(!stTime.value) return;
				var startTime = stTime.value;
				var endTime = etTime.value;
	    		if(startTime<=endTime){
	    			stTime.removeClass('x-form-invalid');
	    			etTime.removeClass('x-form-invalid');
	    		}
			}
		}
	});
	//构造查询方式
	nm3kPickData({
		startTime : stTime,
		endTime : etTime,
		searchFunction : query
	})
	
	var sm = new Ext.grid.CheckboxSelectionModel(); 
	var columnModels = [
		sm,
		{header: '<div class="txtCenter">@ALERT.eventReason@</div>', width: 300, sortable: false, menuDisabled : true, dataIndex: 'message', align: 'left' ,renderer:renderNote},
		{header: '<div class="txtCenter">'+I18N.SYSTEM.type+'</div>', width: 220, sortable: false, align: 'left', dataIndex: 'name'},	
		{header: '<div class="txtCenter">'+I18N.EVENT.sourceHeader+'</div>', width: 150, sortable: false,menuDisabled : true, align: 'left', dataIndex: 'host',renderer:renderDevice},
		{header: I18N.EVENT.timeHeader, width: 160, sortable: false,menuDisabled : true, align: 'center', dataIndex: 'createTime'},
		{header: "@COMMON.manu@", width: 80, sortable: false, align: 'center',renderer : rendererHandler}
	];
    store = new Ext.data.JsonStore({
	    url: '/fault/queryEventList.tv',
	    idProperty: "eventId",
	    root: 'data', totalProperty: 'rowCount',remoteSort: false,
	    fields: ['eventId','message', 'name' , 'source' , 'host' , 'createTime','entityId','entityType','cmcMac']
	})
    //store.setDefaultSort('createTime', 'desc');
    
	grid = new Ext.grid.GridPanel({
		cls:"normalTable edge10",stripeRows:true,region: "center",
		title: "@Event.eventList@",bodyCssClass: 'normalTable',
		store: store, cm: new Ext.grid.ColumnModel(columnModels),viewConfig:{forceFit: true},
		sm: sm,
		bbar: buildPagingToolBar()
	});
	
	store.setBaseParam("eventType", eventTypeId);
	store.setBaseParam("startTime", lastWeek);
	store.setBaseParam("endTime", currentTime);
	store.load({
			params:{start:0, limit: pageSize},
			callback : function(records, options, success) {
	            $("#deviceIp").autocomplete(ipNameList,{width:260, scroll:true});
	            if (records && records.length > 0) {
	            	top.nm3kAlertTipsDie();//如果有这个弹出框,那么先删除;
				}
	        }
	});
    
    var viewPort = new Ext.Viewport({
	     layout: "border",
	     items: [
         	{region:'north',
	 		contentEl :'topPart',
	 		height: 80,
	 		border: false,
	 		cls:'clear-x-panel-body'
         	},grid
         ]
	}); 
});

function onMonuseDown(e, t){
	if(e.button === 0 && t.className == 'x-grid3-row-checker'){ // Only fire if left-click
		e.stopEvent();
		var row = e.getTarget('.x-grid3-row');

		// mouseHandled flag check for a duplicate selection (handleMouseDown) call
		if(!this.mouseHandled && row){
			//event(this.grid.store.getCount());
			var gridEl = this.grid.getEl();//得到表格的EL对象
			var hd = gridEl.select('div.x-grid3-hd-checker');//得到表格头部的全选CheckBox框
			var index = row.rowIndex;
			if(this.isSelected(index)){
				this.deselectRow(index);
				var isChecked = hd.hasClass('x-grid3-hd-checker-on');
				//判断头部的全选CheckBox框是否选中，如果是，则删除
				if(isChecked){
					hd.removeClass('x-grid3-hd-checker-on');
				}
			}else{
				this.selectRow(index, true);
				//判断选中当前行时，是否所有的行都已经选中，如果是，则把头部的全选CheckBox框选中
				if(gridEl.select('div.x-grid3-row-selected').elements.length==gridEl.select('div.x-grid3-row').elements.length){
					hd.addClass('x-grid3-hd-checker-on');
				};
			}
		}
	}
	this.mouseHandled = false;
}

function onHdMouseDown(e, t){
	/**
	*大家觉得上面重写的代码应该已经实现了这个功能了，可是又发现下面这行也重写了
	*由原来的t.className修改为t.className.split(' ')[0]
	*为什么呢？这个是我在快速点击头部全选CheckBox时，
	*操作过程发现，有的时候x-grid3-hd-checker-on这个样式还没有删除或者多一个空格，结果导致下面这个判断不成立
	*去全选或者全选不能实现
	*/
	if(t.className.split(' ')[0] == 'x-grid3-hd-checker'){
	    e.stopEvent();
	    var hd = Ext.fly(t.parentNode);
	    var isChecked = hd.hasClass('x-grid3-hd-checker-on');
	    if(isChecked){
	        hd.removeClass('x-grid3-hd-checker-on');
	        this.clearSelections();
	    }else{
	        hd.addClass('x-grid3-hd-checker-on');
	        this.selectAll();
	    }
	}
}

function resizeWindow(e){
	window.location.reload();
}

Ext.override(Ext.grid.CheckboxSelectionModel,{
    onMouseDown : onMonuseDown,
	onHdMouseDown : onHdMouseDown
});


</script>
</head>
	<body class="whiteToBlack">
		<div id="topPart">
			<div class="edgeAndClearFloat">
				<table class="topSearchTable">
					<tr>
						<td>
							<table width=100% cellpadding="0" border="0"
								style="border-collapse: collapse;">
								<tr>
									<td width="80" class="rightBlueTxt"><label>@ALERT.eventType@:</label></td>
									<td id="putSel"></td>
									<td width="80" class="rightBlueTxt"><label>@ALERT.eventReason@:</label></td>
									<td><input id="eventReason" type="text" class="normalInput w150" /></td>
									<td width="80" class="rightBlueTxt"><label>@RESOURCES/MAIN.relaDevice@:</label></td>
									<td><input id="deviceIp" type="text" class="normalInput" />
									</td>
									<td width=200 colspan=2 rowspan="2" style="padding-left: 40px;">
										<ol class="upChannelListOl pB0">
											<%-- <Zeta:Button id="btn1" onClick="query();">@COMMON.query@</Zeta:Button> --%>
											<li>
												<a id="btn1" href="javascript:;" class="normalBtn normalBtnWithDateTime" onclick="query()"><span><i class="miniIcoSearch"></i><b>@COMMON.query@</b></span></a>
											</li>
											<li>
												<a id="btn2" href="javascript:;" class="normalBtn" onclick="batchDeleteEvent()"><span><i class="miniIcoClose"></i>@COMMON.delete@</span></a>
											</li>
										</ol>
									</td>
								</tr>
								<tr>
									<td class="rightBlueTxt"><label>@ALERT.beginTime@:</label></td>
									<td>
										<div id="startTime"></div>
									</td>
									<td class="rightBlueTxt"><label>@ALERT.endTime@:</label></td>
									<td>
										<div id="endTime"></div>
									</td>
									<td colspan="2"></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td><div id="event-div"></div></td>
					</tr>
				</table>

			</div>
		</div>
	</body>
</Zeta:HTML>