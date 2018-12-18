<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<Zeta:Loader>
	library jquery
	library ext
	module demo
	import js.jquery.dragMiddle
</Zeta:Loader>
</head>
<body class="whiteToBlack">
	
	<div id="topPart">	
		<div id="header">
			<div class="ultab">
				<ul>					
					<li><a href="#">@demo.snap@</a></li>
					<li class="secondTabLine"></li>
					<li><a href="#">@demo.configInfo@</a></li>
					<li class="secondTabLine"></li>
					<li><a href="#">@demo.panel@</a></li>
					<li class="secondTabLine"></li>
					<li><a href="#">@demo.downlink@</a></li>
					<li class="secondTabLine"></li>
					<li class="selected"><a href="#">@demo.entityAlert@</a></li>
					<li class="secondTabLine"></li>
					<li><a href="#">@demo.perfDisplay@</a></li>				      
				</ul>
			</div>
		</div>
		
		<div class="edgeAndClearFloat">
			<table width="1000" cellpadding="0" cellspacing="0" border="0" rules="none" class="topSearchTable">
				<tr>
					<td class="rightBlueTxt w70">@demo.alertLv@:</td>
					<td>
						<select id="alertSeverity" class="normalSel w132">
							<option value="all"></option>
						</select>
					</td>
					<td class="rightBlueTxt w70">@demo.alertType@:</td>
					<td >
						<select id="alertType" class="normalSel w132">
							<option value="all"></option>
						</select>
					</td>
					<td class="rightBlueTxt w70">@demo.alertReason@:</td>
					<td>
						<input id="alertReason" type="text" class="normalInput w130">
					</td>
					<td rowspan="2">
						<ol class="upChannelListOl pB0">
							<li>
								<a id="btn1" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a>
							</li>
							<li>
								<a id="btn2" href="javascript:;" class="normalBtnBig"><span>@COMMON.confirm@</span></a>
							</li>
							<li>
								<a id="btn3" href="javascript:;" class="normalBtnBig"><span>@COMMON.clear@</span></a>
							</li>
						</ol>
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt">@demo.st@:</td>
					<td>
						<input id="startTime" type="text" class="normalInput w130" toolTip="@demo.isoDate@" />
					</td>
					<td class="rightBlueTxt">@demo.et@:</td>
					<td>
						<input id="endTime" type="text" class="normalInput w130" toolTip="@demo.isoDate@" />
					</td>
					<td class="rightBlueTxt">
						@demo.ch@:
					</td>
					<td>
						<select class="normalSel w132">
							
						</select>
					</td>
				</tr>
			</table>
		</div>
	</div>
	
	<dl class="legent r10" style="position:absolute; z-index:2; right:10px; top:120px;">
		<dt class="mR5">@demo.alertChart@:</dt>
		<dd class="mR2"><img src="/images/fault/level6.png" border="0" alt="" /></dd>
		<dd class="mR10">@demo.critical@</dd>
		<dd class="mR2"><img src="/images/fault/level5.png" border="0" alt="" /></dd>
		<dd class="mR10">@demo.major@</dd>
		<dd class="mR2"><img src="/images/fault/level4.png" border="0" alt="" /></dd>
		<dd class="mR10">@demo.main@</dd>
		<dd class="mR2"><img src="/images/fault/level3.png" border="0" alt="" /></dd>
		<dd class="mR10">@demo.minor@</dd>
		<dd class="mR2"><img src="/images/fault/level2.png" border="0" alt="" /></dd>
		<dd class="mR10">@demo.warn@</dd>
		<dd class="mR2"><img src="/images/fault/level1.png" border="0" alt="" /></dd>
		<dd>@demo.tipInfo@</dd>			
	</dl>
<script type="text/javascript">
Ext.onReady(function(){
	var sm = new Ext.grid.CheckboxSelectionModel();
	var cm = new Ext.grid.ColumnModel([
		sm,
		{header:"@demo.num@", dataIndex:"id", width:20,sortable:true, align:"center"},
		{header:"@COMMON.name@", dataIndex:"name", align:"center"},
		{header:"@demo.sex@", dataIndex:"sex",width:60, align:"center"},
		{header:"@demo.desc@", dataIndex:"descn", id:"descn", align:"center"},
		{header:"@demo.birth@", dataIndex:"birthday", width:80,type:"date",renderer:Ext.util.Format.dateRenderer("Y年m月d日"), align:"center"},
		{header:"@demo.bg@", dataIndex:"bgColor", align:"center"},
		{header:"@COMMON.manu@", dataIndex:"js", width:80, align:"center"}		
	]);//end cm;
	
	
	
	var data = [];//end data;
	for(var i=0;i<100;i++){
		var arr = new Array();
		arr.push(i);
		arr.push("<div style='text-align:left'><a href='#'>@demo.alignLeft@ "+ i +"</a></div");
		arr.push("@demo.THR@");
		arr.push("@demo.FOUR@");
		arr.push("@demo.FIRTH@");
		arr.push("@demo.SIX@");
		arr.push("<a href='javascript:;'>@demo.view@</a>");
		data.push(arr);
	}	
	var store = new Ext.data.Store({
		proxy: new Ext.data.MemoryProxy(data),
		reader: new Ext.data.ArrayReader({},[
				{name: "id"},
				{name: "name"},
				{name: "sex"},
				{name: "descn"},
				{name: "birthday"},
				{name: "bgColor"},
				{name: "js"}
			])
			//,sortInfo: {field:"birthday", direction: "ASC"}
	});
	store.load();
	
	var grid = new Ext.grid.GridPanel({
		cls:"normalTable edge10",
		stripeRows:true,
		region: "center",
		title: "@demo.cal@",
		enableColumnMove: false,
		enableColumnResize: true,
		bbar: new Ext.PagingToolbar({
			pageSize: 10,
			store: store,
			displayInfo: true,
			displayMsg: "@demo.message@",
			emptyMsg: "@demo.noRecord@",
			cls: 'extPagingBar'
		}),
		store: store,
		cm : cm,
		sm : sm,
		viewConfig:{
			forceFit: true
		}		
	});
	
	var viewPort = new Ext.Viewport({
	     layout: "border",
	     items: [grid,
          	{region:'north',
 	 		height:110,
 	 		contentEl :'topPart',
 	 		border: false,
 	 		cls:'clear-x-panel-body',
 	 		autoScroll: true
          	}
          ]
	}); 
	
});
</script>
</body>
</Zeta:HTML>
