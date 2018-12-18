<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    PLUGIN  LovCombo
    module network
    PLUGIN DropdownTree
</Zeta:Loader>
<style>
html, body{height:100%;}
#query-container{height:60px;padding-top: 15px;}
.queryTable td.rightBlueTxt{padding: 2px 0 2px 10px;}
</style>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script> 
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;

var folderId = "";
var ip = "";
var mac = "";
var typeId = "";
var store = null;
var btn2;
var grid;
var regionTree;

function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:store,
       displayInfo: true, items: ["-", String.format('@COMMON.displayPerPage@', pageSize), '-'
                            ]});
   return pagingToolbar;
}

function resize(){
	   $('#grid-container').height($('body').height()-$('#query-container').outerHeight());
	   grid.setSize($('#grid-container').width(),$('#grid-container').height());
}

function throttle(fn, delay){
	   var timer = null;
	   return function(){
		   var context = this, args = arguments;
		   clearTimeout(timer);
		   timer = setTimeout(function(){
			   fn.apply(context, args);
		   }, delay);
	   }
}

$(window).bind('resize', function(e){
	   throttle(resize, 50)();
});

function queryClick(){
	mac = $("#mac").attr("value")
	ip = $("#ip").attr("value")
	typeId = Ext.getCmp("entityTypeSelect").getCheckedValue();
	folderId = regionTree.getSelectedIds();
	
	store.setBaseParam('folderId', folderId);
	store.setBaseParam('mac', mac);
	store.setBaseParam('ip', ip);
	store.setBaseParam('typeId', typeId);	
	store.setBaseParam('start', 0);	
	store.setBaseParam('limit', pageSize);
	
	if(ip != null && ip != "" && !validateIp(ip)){
		$("#ip").focus();
		return;
	}
	if(mac != null && mac != "" && !validateMac(mac)){
		$("#mac").focus();
		return;
	}
	
	store.load();
	btn2.enable();
	}

function exportEntity() {
	if (typeof(folderId) == "undefined") { 
		folderId = ''; 
		} 
	
	if(ip != null && ip != "" && !validateIp(ip)){
		$("#ip").focus();
		return;
	}
	if(mac != null && mac != "" && !validateMac(mac)){
		$("#mac").focus();
		return;
	}
	window.location.href="/export/exportEntityToExcel.tv?mac=" + mac + "&folderId=" + folderId + "&ip=" + 
													ip + "&typeId=" + typeId; 
}
function areaRender(value,metadata,record){
	metadata.attr = 'ext:qtip="' + value +'"';
	return value;
}

Ext.onReady(function() {
		Ext.QuickTips.init();
		var DeviceType = Ext.data.Record.create([
               {name: 'id', type: 'int'},
               {name: 'name', type: 'string'}
      	]);
      	var typeList = [];
      	var deviceTypeStore = new Ext.data.Store({
      		proxy: new Ext.data.MemoryProxy(typeList),
      		reader: new Ext.data.ArrayReader({}, DeviceType)
      	});
		var entityTypeCombox = new Ext.ux.form.LovCombo({
			fieldLabel : "@batchTopo.entityType@",
			id : 'entityTypeSelect',
			renderTo : "entityTypeSelectDiv",
			hideOnSelect : true,
			editable : false,
			width : 202,
			store : deviceTypeStore,
			/* store : new Ext.data.JsonStore({
				url : "/entity/export/loadEntityType.tv",
				root : 'data',
				autoLoad : true,
				fields : [ "typeId", "displayName" ]
			}), */
			valueField: 'id',
			displayField: 'name',
			triggerAction : 'all',
			emptyText : '@COMMON.select@' +　"@batchTopo.entityType@",
			mode : 'local',
			showSelectAll : true,
			beforeBlur : function() {
			}
		});
		
		//获取所有的子设备类型
		$.get('/entity/loadSubEntityType.tv', {
			type: 1
		},function(map){
			var list = map.entityTypes;
			$.each(list, function(index, entityType){
				typeList.append([entityType.typeId, entityType.displayName]);
			})
			deviceTypeStore.load();
			entityTypeCombox.selectAll();
		})

		var w = document.body.clientWidth;
		var h = document.body.clientHeight;

		var columns = [ {
			header : "@resources/COMMON.manageIp@",
			width : w / 9,
			fixed : true,
			sortable : false,
			align : 'center',
			dataIndex : 'ip'
		}, {
			header : '<div class="txtCenter">@COMMON.alias@</div>',
			width : parseInt(w / 9),
			sortable : false,
			align : 'left',
			dataIndex : 'name'
		}, {
			header : '<div class="txtCenter">@folder.type20@</div>',
			width : parseInt(w / 9),
			sortable : false,
			align : 'left',
			dataIndex : 'folderName'
		} , {
			header : '<div class="txtCenter">@NAMEEXPORT.sysName@</div>',
			width : parseInt(w / 9),
			sortable : false,
			align : 'left',
			dataIndex : 'sysName'
		}, {
			header : "<div class='txtCenter'>@batchTopo.entityType@</div>",
			width : w / 9,
			sortable : false,
			align : 'left',
			dataIndex : 'typeName'
		},  {
			header : "MAC",
			width : w / 9,
			fixed : true,
			sortable : false,
			align : 'center',
			dataIndex : 'mac'
		},  {
			header : '<div class="txtCenter">@COMMON.contact@</div>',
			width : w / 9,
			fixed : true,
			sortable : false,
			align : 'center',
			dataIndex : 'contact'
		},  {
			header : '<div class="txtCenter">@COMMON.location@</div>',
			width : w / 9,
			fixed : true,
			sortable : false,
			align : 'center',
			dataIndex : 'location'
		},  
		{
			header : '<div class="txtCenter">@COMMON.note@</div>',
			width : w / 9,
			fixed : true,
			sortable : false,
			align : 'center',
			dataIndex : 'note'
		}
		];
		store = new Ext.data.JsonStore({
			url : '/export/loadEntity.tv',
			root : 'data',
			totalProperty : 'rowCount',
			remoteSort : true,
			fields : [ 'name', 'sysName', 'typeName', 'ip', 'mac', 'folderName','contact','location','note']
		});

		grid = new Ext.grid.GridPanel({
			stripeRows : true,
			region : "center",
			bodyCssClass : 'normalTable',
			id : 'extbaseGridContainer',
			store : store,
			columns : columns,
			loadMask: true,
			renderTo: 'grid-container',
			autoScroll: false,
			   viewConfig: {
				   forceFit: true
			   },
			margins: '0px 22px 0px 5px',
			bbar : buildPagingToolBar()
		});

		btn2 = new Ext.SplitButton({
			renderTo : "btn2",
			text : "@BUTTON.export@",
			handler : function(){this.showMenu()},
			iconCls : "bmenu_exportWithSubMin",
			menu : new Ext.menu.Menu({
				items : [{text : "EXCEL",   handler : exportEntity}]
			})
		});
		btn2.disable();
		
		regionTree = $('#region_tree').dropdowntree({
			width: 200
		}).data('nm3k.dropdowntree');
		regionTree.checkAllNodes();
		
		resize();
		
	});

//比较两个ip的大小,如果大于，返回1，等于返回0，小于返回-1  
function compareIP(ipBegin, ipEnd)  
{  
    var temp1;  
    var temp2;    
    temp1 = ipBegin.split(".");  
    temp2 = ipEnd.split(".");     
    for (var i = 0; i < 4; i++)  
    {  
        if (parseInt(temp1[i]) > parseInt(temp2[i]))  
        {  
            return 1;  
        }  
        else if (parseInt(temp1[i]) < parseInt(temp2[i]))  
        {  
            return -1;  
        }  
    }  
    return 0;     
}  

function validateIp(ip){
	var ipArray = ip.split("-")
	if(ipArray.length != 2){
		return false;
	}else{
		ip1 = ipArray[0]
		ip2 = ipArray[1]
		if(!validateIPaddress(ip1) || !validateIPaddress(ip2) || (compareIP(ip1, ip2) != -1)){
			return false;
		}
		return true;
	}
	
	return true
}
function validateMac(mac){
	var macArray = mac.split("-")
	if(macArray.length != 2){
		return false;
	}else{
		mac1 = macArray[0]
		mac2 = macArray[1]
		if(!validateMacAddress(mac1) || !validateMacAddress(mac2)){
			return false;
		}
		mac1 = Validator.formatMac(mac1);
		mac2 = Validator.formatMac(mac2);
		if(mac1 >= mac2){
			return false;
		}
	}
	return true;
}

function validateMacAddress(macaddr)
{	
   if(macaddr.split(':').length != 6){
	   return false;  
   }
   var reg1 = /[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}/;
   if (reg1.test(macaddr)) {
      return true;
   } else {
      return false;
   }
}

function validateIPaddress(ipaddress)   
{  
 if (/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(ipaddress))  
  {  
    return (true)  
  }  
    return (false)  
}  
</script>
</head>
<body class="whiteToBlack" >
	<div id="query-container">
	<div class=formtip id=tips style="display: none"></div>
		<table class="queryTable">
		    <tr> 
		    	<td  class="rightBlueTxt w80">@batchTopo.entityType@:</td>
		    	<td>
		    		<div id="entityTypeSelectDiv"></div>
		    	</td>
		    	<td  class="rightBlueTxt w80">@folder.type20@:</td>
		    	<td><div id="region_tree"></div></td>
		    	
		    	<td rowspan="2">
				<ol class="upChannelListOl pB0 pL5">
					<li>
						<a id="btn1" href="javascript:queryClick();" class="normalBtn"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a>
					</li>
					<li>
						<div id="btn2"></div>
					</li>
				</ol>
			</td>
		   	</tr>
			<tr>
				<td  class="rightBlueTxt w80">@NAMEEXPORT.ipSection@:</td>
				<td><input type=text class="normalInput" style="width: 200px" id="ip" toolTip="@NAMEEXPORT.ipSectionTip@"/></td>
				<td  class="rightBlueTxt w80">@NAMEEXPORT.macSection@:</td>
				<td><input type=text class="normalInput" style="width: 280px" id="mac" toolTip="@NAMEEXPORT.macSectionTip@"/></td>
			</tr>
		</table>
	</div>
	<div id="grid-container"></div>
</body>
</Zeta:HTML>
