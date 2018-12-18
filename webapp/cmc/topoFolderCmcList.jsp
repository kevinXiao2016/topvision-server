<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
	<%@include file="../include/ZetaUtil.inc"%>
	<Zeta:Loader>
		library ext
	    library jquery
	    library zeta
	    module cmc
	</Zeta:Loader>
	<style type="text/css">
		body,html{ height:100%;}
	</style>
	<script type="text/javascript">
		Ext.QuickTips.init();
		var pageSize = <%= uc.getPageSize() %>;
		var grid = null;
		var store = null;
		var pagingToolbar = null;
		var folderId = '${folderId}';
		var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
		$(function(){
			var sm = new Ext.grid.CheckboxSelectionModel(); 
			var cm =  new Ext.grid.ColumnModel([
			    sm,
			    {id:"name",header:'<div class="txtCenter">'+'@COMMON.alias@'+'</div>',dataIndex:'name',width: 140,align:'left',sortable:true,renderer:function(value, p, record){
			    	return '<a href="#" onclick="showEntityDetail(\''+record.data.entityId+'\',\''+record.data.name+'\',\''+record.data.type+'\');">'+value+'</a>';
			    }},
			    {id:"mac",header:'<div class="txtCenter">MAC</div>',dataIndex:'mac',align:'left',width: 150,sortable:true,renderer:renderMac},
			    {id:"deviceIp",header:'<div class="txtCenter">@resources/COMMON.uplinkDevice@</div>',dataIndex:'uplinkDevice',align:'left',width:120,sortable:true},
			    {id:"state",header:'@IPTOPO.onlineStatus@',dataIndex:'state',align:'center',width: 60,renderer:renderOnlie},
			    {id:"createTime",header:'@IPTOPO.createTime@',dataIndex:'createTime',align:'center',width: 130,sortable:true},
			    {id:"lastTime",header:'@IPTOPO.lastTime@',dataIndex:'lastTime',align:'center',width: 100,sortable:true},
			    {id:"folderName",header:'<div class="txtCenter">@IPTOPO.deviceArea@</div>',dataIndex:'folderName',align:'left',width: 120,sortable:true,renderer:areaRender},
			    {id:"addr",header:'@IPTOPO.location@',dataIndex:'addr',align:'center',width: 80,sortable:true},
			    {id:"sysUpTime",header:'@IPTOPO.onlineTime@',dataIndex:'sysUpTime',align:'center',width: 150,sortable:true,renderer:renderSysUpTime},
			    {header: "@COMMON.manu@", width:100,fixed:true, dataIndex: 'entityId',renderer : manuRender}
			]);
		   
			 var tbarArr = [{text: '@IPTOPO.deviceResert@', id: 'refreshAllCm_button', iconCls: 'bmenu_restart',disabled:!operationDevicePower, handler: restartCmcList}];
			 <% if(uc.hasSupportModule("cmc")){ %>
			 	tbarArr.push({text: '@IPTOPO.lastSevenDaysCmts@', id: 'sevenOnline', iconCls: 'miniIcoView', handler: lastSevenOnline})
			 <% } %>
			 var tbar = new Ext.Toolbar({
				 items: tbarArr
			 });
			store = new Ext.data.JsonStore({
			    url: '/cmc/loadDeviceListByFolder.tv',
			    root: 'data',
			    totalProperty: 'rowCount',
			    remoteSort: false,
			    fields: ['entityId','ip','uplinkDevice','type','mac','name','state','createTime','lastTime','sysUpTime','folderId','folderName',
			             'totalNum','onlineNum','dt']
			});
		         
		   	grid = new Ext.grid.GridPanel({
		   		stripeRows:true,
		   		region: 'center',
		   		bodyCssClass: 'normalTable',
		   		border:false,
		   		store: store,
		   		cm : cm,
		   		sm : sm,
		   		tbar: tbar,
		   		bbar: buildPagingToolBar(),
		   		viewConfig:{
		   			forceFit: false
		   		}
		   	});
		  	//默认选中根结点并加载所有的数据
			loadData(folderId);
		  	
			new Ext.Viewport({
			    layout: 'border',
			    items: [grid]
			});
		});//end document.ready;
		
		function areaRender(value,metadata,record){
			metadata.attr = 'ext:qtip="' + value +'"';
			return value;
		}
		
		//显示实时信息
		function showRealTimeInfo(cmcId,name){
		    top.addView('entity-realTime' + cmcId, name, 'entityTabIcon',
		            '/cmc/showCmcRealTimeData.tv?cmcId=' + cmcId);
		}
		
		function renderMac(value, p, record){
			var mac = record.data.mac;
			var totalNum = record.data.totalNum || 0;
			var onlineNum = record.data.onlineNum || 0;
			if(totalNum == null || totalNum == ""){
				totalNum = 0;
			}
			if(onlineNum == null || onlineNum == ""){
				onlineNum = 0;
			}
			
			if(totalNum == onlineNum){
				return mac +'(<span class="greenTxt">'+ onlineNum +'</span>/<span class="redTxt">'+totalNum+'</span>)';
			}else{
				return mac +'(<span class="differentNun">'+ onlineNum +'/'+totalNum+'</span>)';
			}
		};
		
		function renderOnlie(value, p, record){
			if (value) {
				return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/fault/trap_on.png" border=0 align=absmiddle>',
					"@COMMON.online@");	
			} else {
				return String.format('<img nm3kTip="{0}" class="nm3kTip" src="../images/fault/trap_off.png" border=0 align=absmiddle>',
					"@resources/COMMON.unreachable@");	
			}
		};
		
		function renderSysUpTime(value, p, record) {
		    if (value == "NoValue") {
		        return "<img src='/images/canNot.png' class='nm3kTip' nm3kTip='@PERF.mo.notCollected@' />";
		    }
		    var baseTime = 100;
		    var time = parseInt(record.data.sysUpTime / baseTime);
		    var timeString;
		    if (record.data.state == 1) {
		        timeString = arrive_timer_format(time)
		    } else {
		        timeString = '@network/label.deviceLinkDown@';
		    }
		    return timeString;
		}
		
		function manuRender(v,m,r){
			var cmcId = r.data.entityId;
			var name = r.data.name;
			//var folderId = r.data.folderId;
			//return String.format("<a href='javascript:;' onclick='showEditPage({0},\"{1}\")'>@COMMON.edit@</a> / <a href='javascript:;' onclick='showCmList({0},\"{1}\")'>@IPTOPO.cmList@</a>", cmcId, name );
			return String.format("<a href='javascript:;' onclick='showCmList({0},\"{1}\")'>@IPTOPO.cmList@</a>", cmcId, name );
		}
		
		function showEditPage(cmcId, name){
			window.top.createDialog('editDeviceInfo', '@IPTOPO.editDevice@' , 600, 370, '/cmc/showEditDevice.tv?cmcId='+cmcId+"&deviceName="+name, null, true, true)
		}
		
		function restartCmcList(){
			var sm = grid.getSelectionModel();
			if (sm != null && sm.hasSelection()) {
				 top.showConfirmDlg('@COMMON.tip@', '@CCMTS.confirmResetCcmts@', function(type) {
					if (type == "no") {
						return;
					}
					var selections = sm.getSelections();
					var cmcIds = [];
					for (var i = 0; i < selections.length; i++) {
						cmcIds[i] = selections[i].data.entityId;
					}
					top.showWaitingDlg('@COMMON.waiting@', '@IPTOPO.onReserting@', 'ext-mb-waiting');
					$.ajax({
						url:"/cmc/batchRestartCmc.tv?cmcIdList=" + cmcIds.join(","), 
						type:'POST',
						dataType:"text",
						success:function (text) {
							if(text == 'success'){
								top.showMessageDlg("@COMMON.tip@", "@IPTOPO.resertSuccess@");
							}else{
								top.showMessageDlg("@COMMON.tip@", "@IPTOPO.resertFailed@");
							}
						}, error:function () {
							top.showMessageDlg("@COMMON.tip@", "@IPTOPO.resertFailed@");
						},
			            cache:false
					});
					});
			} else {
				top.showMessageDlg("@COMMON.tip@", "@IPTOPO.chooseDevice@");
			}
		}
		
		function modifyTopoFolder(){
			var sm = grid.getSelectionModel();
			if (sm != null && sm.hasSelection()) {
				var selections = sm.getSelections();
				var cmcIds = [];
				for (var i = 0; i < selections.length; i++) {
					cmcIds[i] = selections[i].data.entityId;
				}
				window.top.createDialog('modifyTopoFolder', '@IPTOPO.updateArea@' , 600, 370, '/cmc/showModifyTopFolder.tv?cmcIdList=' + cmcIds.join(","), null, true, true)
			}else{
				top.showMessageDlg("@COMMON.tip@", "@IPTOPO.chooseDevice@");
			}
		}
		
		function lastSevenOnline(){
			store.setBaseParam("folderId", folderId);
			store.setBaseParam("sevenDay", true);
			store.load({params:{start: 0, limit: pageSize}});
		}
		
		function loadData(folderId){
			store.setBaseParam("folderId", folderId);
			store.setBaseParam("sevenDay", false);
			store.load({params:{start: 0, limit: pageSize}});
		}
		
		function buildPagingToolBar() {
		    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:store,
		       displayInfo: true, items: ["-", String.format('@COMMON.displayPerPage@', pageSize), '-'
		                       		]});
		   return pagingToolbar;
		}
		
		function arrive_timer_format(s) {
		    var t
		    if (s > -1) {
		        hour = Math.floor(s / 3600);
		        min = Math.floor(s / 60) % 60;
		        sec = Math.floor(s) % 60;
		        day = parseInt(hour / 24);
		        if (day > 0) {
		            hour = hour - 24 * day
		            t = day + "@resources/COMMON.D@" + hour + "@resources/COMMON.H@"
		        } else {
		            t = hour + "@resources/COMMON.H@"
		        }
		        if (min < 10) {
		            t += "0"
		        }
		        t += min + "@resources/COMMON.M@"
		        if (sec < 10) {
		            t += "0"
		        }
		        t += sec + "@resources/COMMON.S@"
		    }
		    return t
		}
		function showEntityDetail(cmcId,title,type){
		    top.addView('entity-' + cmcId,   title  , 'entityTabIcon',
		            '/cmc/showCmcPortal.tv?cmcId=' + cmcId+"&productType="+type);	 
		}
		//显示CM列表
		function showCmList(cmcId,name){
			 top.addView('realtimeCmList','@realtimeCmList@', 'apListIcon',
			            '/realtimecmlist/showRealtimeCmList.tv?cmcId=' + cmcId, null, true);
		}
		
		function refresh(){
			store.reload();
		}
	</script>
</head>

<body class="bodyWH100percent bodyGrayBg">
	
</body>
</Zeta:HTML>
