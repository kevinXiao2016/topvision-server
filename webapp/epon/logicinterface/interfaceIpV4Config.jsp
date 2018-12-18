<%@ page language="java" contentType="text/html;charset=utf-8"%>
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
	    module logicinterface
	</Zeta:Loader>
	<style type="text/css">
	#sidePart p{ padding-bottom:10px; line-height:1.4em;}
	</style>
	<script type="text/javascript">
	var cm,grid,store,tbar;
	var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
	var entityId = '${entityId}';
	var interfaceIndex = '${logicInterface.interfaceIndex}';
	var adminStatus = '${logicInterface.interfaceAdminStatus}',
		operaStatus = '${logicInterface.interfaceOperateStatus}';
		
	function createStatusImg(renderId, value, tipArray){
		var imgStr = '<img nm3ktip="{0}" class="nm3kTip" src="/images/fault/trap_{1}.png" border="0" align="absmiddle">'
		if(value == 1){
			imgStr = String.format(imgStr, tipArray[1],'on');
		}else{
			imgStr = String.format(imgStr, tipArray[0],'off');
		}
		$("#"+renderId).html(imgStr);	
	}	
			
	function initLayout(){
        var viewPort = new Ext.Viewport({
            layout : "border",
            cls    : "nm3kViewPort canNotDrag",
            items: [{
                region : "north",
                height : 35,
                border : false,
                items  : [{
                    xtype : "toolbar",
                    items : [{
                        text : "@COMMON.back@",
                        cls  : "mL10",
                        iconCls : "bmenu_arrLeft",
                        handler : function(){
                        	var activeFrame = top.getActiveFrame();
                        	try{
                        		activeFrame.closeOpenLayer();
                        	}catch(e){
                        		
                        	}
                        }
                    }]
                }]
            },{
                cls    : "obliqueFringeBg",
                region : "west",
                border : false,
                split    : true,
                width  : 200,
                minWidth : 200,
                maxWidth : 200,
                autoScroll : true,
                contentEl  : "sidePart"
            },grid]
        });
    };
    $(function(){
    	createStatusImg('putAdminStatus',adminStatus, ['@interface.adminStatusDown@','@interface.adminStatusUp@']);
    	createStatusImg('putOperateStatus',operaStatus, ['DOWN','UP']);
    	
    	tbar = new Ext.Toolbar({
			items: [{
	        	xtype : 'tbspacer',
	        	width : 10
	        },{
	        	text  :'@BUTTON.add@',
	        	iconCls : 'bmenu_new',
	        	handler : addInterfaceIp 
			},'-',{
	        	text  :'@COMMON.fetch@',
	        	iconCls : 'bmenu_equipment',
	        	handler : refreshIpV4Config 
			}]
    	});
    	cm = new Ext.grid.ColumnModel([
			{header: "IP", flex: 60, dataIndex: 'ipV4Addr',sortable:true},
			{header: "@interface.mask@", width: 60, dataIndex: 'ipV4NetMask',sortable:true},
			{header: "@interface.type@", width: 60, dataIndex: 'ipV4AddrType',renderer:renderIpType,sortable:true},
			{header: "@interface.opera@", width: 60, renderer : manuRender}
		]);
    	store = new Ext.data.JsonStore({
		    url: '/epon/logicinterface/loadInterfaceIpV4Config.tv',
		    root: 'data', 
		    remoteSort: true,
		    fields: ['entityId','ipV4ConfigIndex','ipV4Addr','ipV4NetMask','ipV4AddrType']
		});
    	grid = new Ext.grid.GridPanel({
		    stripeRows:true,
		    border: false,
		    bodyCssClass: 'normalTable',
	   		region: "center",
		    id: 'extGridContainer', 
		    viewConfig:{ forceFit: true },
			store: store,
			tbar : tbar,
			cm: cm
		});
		store.load({
			params:{
				entityId:entityId,
			    ipV4ConfigIndex:interfaceIndex
			}
		});
        initLayout();
    });//end document.ready;
	
    function refreshIpV4Config(){
    	window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/logicinterface/refreshInterfaceIpV4Config.tv',
			type : 'POST',
			data : {
				entityId : entityId
			},
			success : function() {
				top.afterSaveOrDelete({
	       	      	title: "@COMMON.tip@",
	       	      	html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
	       	    });
				store.reload();
			},
			error : function(json) {
				top.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
			},
			cache : false
		});
    }
    
    function addInterfaceIp(){
    	window.top.createDialog('addInterfaceIp', "@COMMON.add@ IP", 600, 370, '/epon/logicinterface/addInterfaceIpV4View.tv?entityId='+entityId + '&ipV4ConfigIndex=' + interfaceIndex);
    }
    
    function deleteFn(entityId,ipV4ConfigIndex,ipV4Addr){
		top.showConfirmDlg("@COMMON.tip@", "@interface.deleteIpConfirm@", function(type) {
			if (type == 'no') {
				return;
		}
		window.top.showWaitingDlg('@COMMON.wait@', '@interface.deleteIp@', 'ext-mb-waiting');
		$.ajax({
			url : '/epon/logicinterface/deleteInterfaceIpV4.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				ipV4ConfigIndex : ipV4ConfigIndex,
				ipV4Addr : ipV4Addr
			},
			success : function() {
				top.afterSaveOrDelete({
	       	      	title: "@COMMON.tip@",
	       	      	html: '<b class="orangeTxt">@interface.delIpSuc@</b>'
	       	    });
				store.reload();
			},
			error : function(json) {
				top.showMessageDlg("@COMMON.tip@", "@interface.delIpFail@");
			},
			cache : false
		});
		});
	}
    function reloadData(){
    	store.reload();
    }
    
    function manuRender(v,m,r){
		var str1 = String.format("<a href='javascript:;'  onClick='editFn({0},{1},{2})'>@COMMON.edit@</a>",
		    		r.data.entityId, r.data.ipV4ConfigIndex,r.data.ipV4Addr),
		    str2 = String.format('<a href="javascript:;"  onClick="deleteFn({0},{1},\'{2}\')">@COMMON.delete@</a>',r.data.entityId, r.data.ipV4ConfigIndex,r.data.ipV4Addr)
			return str2;
	}
    
    function renderIpType(value,p,record){
    	if(value == 0){
    		return '@interface.priType@';
    	}else{
    		return '@interface.subType@';
    	}
    }
	</script>
</head>
<body class="whiteToBlack">
	<div id="sidePart" class="edge10">
        <p class="wordBreak"><label class="blueTxt">@interface.name@: </label>${logicInterface.interfaceName}</p>
        <p class="wordBreak"><label class="blueTxt">@interface.desc@: </label>${logicInterface.interfaceDesc}</p>
        <p class="wordBreak"><label class="blueTxt">@interface.adminStatus@: </label><span id="putAdminStatus"></span></p>
        <p class="wordBreak"><label class="blueTxt">@interface.operaStatus@: </label><span id="putOperateStatus"></span></p>
        <p class="wordBreak"><label class="blueTxt">@interface.mac@: </label>${logicInterface.interfaceMac}</p>
    </div>
</body>
</Zeta:HTML>