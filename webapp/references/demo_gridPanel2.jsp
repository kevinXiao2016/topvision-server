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
	var toolbar = [
	       	    {text: "@COMMON.create@", iconCls: 'bmenu_new',cls:'mL10'},
	       	    '-',
	       	    '@COMMON.alias@:',
	       	    {xtype:'textfield', id:'searchEntity'},
	       	    {xtype: 'tbspacer', width: 3},
	       		{text: "@COMMON.find@", iconCls: 'bmenu_find'}, 
	       		'-',
	       		{text: "@COMMON.refresh@", iconCls: 'bmenu_refresh'}];
	var grid = new Ext.grid.GridPanel({
		stripeRows:true,
		region: "center",
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
		bodyCssClass: 'normalTable',
		border:false,
		store: store,
		cm : cm,
		sm : sm,
		tbar: toolbar,
		viewConfig:{
			forceFit: true
		}		
	});
	
	var viewPort = new Ext.Viewport({
	     layout: "border",
	     items: [	grid
	             ]
	}); 
});
</script>
</body>
</Zeta:HTML>
