<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
 <Zeta:Loader>
	library ext
	library zeta
	library jquery
	module mobile
</Zeta:Loader>
<head>
<script>
	//<!--初始化-->
	function addClick() {
		  window.top.createDialog('addMobileDeviceType', '@text.addMobileDeviceType@', 600, 370,
		    '/mobile/showAddMobileDeviceType.tv', null, true, true);
	}
	function refreshClick() {
		onRefreshClick();
	}
	$(function() {
		sm = new Ext.grid.CheckboxSelectionModel({
			header : '<div class="x-grid3-hd-checker"></div>',
			singleSelect : true
		});
		var cm = [
			//sm,
			{ header : '@mobileConfig.deviceType@', width : 120, sortable : false, dataIndex : 'deviceType',renderer: renderIsDefault },
			{ header : '@mobileConfig.corporation@', width : 120, sortable : false, dataIndex : 'corporation' ,renderer: renderIsDefault},
			{ header : '@mobileConfig.frequency@', width : 120, sortable : false, dataIndex : 'frequency' ,renderer: renderIsDefault},
			{ header : '@mobileConfig.powerlevel@', width : 120, sortable : false, dataIndex : 'powerlevel',renderer: renderIsDefault },
			{ header : '@mobileConfig.operation@', width : 150, sortable : false  ,renderer: renderoPeration}
		];


	    store = new Ext.data.JsonStore({
	        url: '/mobile/mobileDeviceTypeList.tv',
	        root: 'data',
	        fields: ['typeId','deviceType', 'corporation','frequency', 'powerlevel','isDefault'] 
	    });
	    var toolbar = [
	            {text: '@COMMON.add@', iconCls: 'bmenu_new  ',handler: addClick} ,
	            {text: '@COMMON.refresh@', iconCls: 'bmenu_refresh',handler: refreshClick}
	        ];

		grid = new Ext.grid.GridPanel(
			{
			store : store,
			border:true,
			animCollapse : false,
			columns : cm,
			sm : sm,
			region: 'center',
			bodyCssClass:'normalTable',
	        tbar: new Ext.Toolbar({
	            cls:'mobileDeviceConfigToolBar',items:toolbar
	        }),
			viewConfig: {
		        forceFit: true
			},
	        border: false
		});
		 new Ext.Viewport({
		    layout: 'border',
		    items: [
		      grid
		    ]
		  });

		store.load();
	});

	function modifyMobileDeviceType() {
	  var sm = grid.getSelectionModel();
	  record = sm.getSelected();
	  var typeId = record.data.typeId;
	  var deviceType = record.data.deviceType;
	  var corporation = record.data.corporation;
	  var frequency = record.data.frequency;
	  var powerlevel = record.data.powerlevel;
	  window.top.createDialog('modifyMobileDeviceType', '@text.modifyMobileDeviceType@', 600, 370,
	    '/mobile/showModifyMobileDeviceType.tv?typeId=' + typeId + '&deviceType=' + deviceType
	    + '&corporation=' + corporation + '&frequency=' + frequency + '&powerlevel=' + powerlevel, null, true, true);
	}

	
	function del() {  
		var sm = grid.getSelectionModel();
		record = sm.getSelected();
		var typeId = record.data.typeId;
		$.ajax({
			url : '/mobile/delMobileDeviceType.tv',
			type : 'post',
			data: {
		      typeId:typeId
		    },
			success : function(response) {
				top.afterSaveOrDelete({
	   				title: '@RECYLE.tip@',
	   				html: '<b class="orangeTxt">' + '@text.modifySuccessTip@' + '</b>'
	   			});
				if(window.parent.getFrame("mobileDeviceConfigDlg")!= undefined){
					window.parent.getFrame("mobileDeviceConfigDlg").onRefreshClick();
				}
			},
			error : function(response) {
				window.parent.showMessageDlg('@RECYLE.tip@',
						'@text.modifyFailureTip@');
			},
			cache : false
		});
	}
	
	function setDefault() {  
		var sm = grid.getSelectionModel();
		record = sm.getSelected();
		var typeId = record.data.typeId; 
		$.ajax({
			url : '/mobile/setDefaultMobileDeviceType.tv',
			type : 'post',
			data: {
		      typeId:typeId
		    },
			success : function(response) {
				top.afterSaveOrDelete({
	   				title: '@RECYLE.tip@',
	   				html: '<b class="orangeTxt">' + '@text.modifySuccessTip@' + '</b>'
	   			});
				if(window.parent.getFrame("mobileDeviceConfigDlg")!= undefined){
					window.parent.getFrame("mobileDeviceConfigDlg").onRefreshClick();
				}
			},
			error : function(response) {
				window.parent.showMessageDlg('@RECYLE.tip@',
						'@text.modifyFailureTip@');
			},
			cache : false
		});
		 
	}
	
	function renderIsDefault(value, p, record) {
		  var isDefault = record.data.isDefault;
		  return isDefault ? '<b>'+value+'</b>' :  value;		  
	}

	function renderoPeration(value, p, record) {
		  var typeId = record.data.typeId;
		  var isDefault = record.data.isDefault;
		  var setDefalut = isDefault ? '' :  ' / <a href="#" class=my-link onclick=\'setDefault("' + typeId + '");\'>@COMMON.setDefault@</a>';
		  
		  var html =  '<a href="#" class=my-link onclick=\'modifyMobileDeviceType();\'>@COMMON.modify@</a> / ' + 
		  '<a href="#" class=my-link onclick=\'del();\'>@COMMON.delete@</a>' + setDefalut ;
		  return html;
		  
	}

	function onRefreshClick() {
		store.reload();
	}

</script>
</head>
<body class="openWinBody">
	
</body>
</Zeta:HTML>