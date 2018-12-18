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
    module iproute
    import js.tools.ipText static
    css css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
body,html{height:100%; overflow:hidden;}
.openLayerIpRoute{ width:100%; height:100%; overflow:hidden; position:absolute; top:0;left:0;  display:none;}
.openLayerIpRouteMain{ width:500px; height:320px; overflow:hidden; position:absolute; top:50px; left:140px; z-index:2;  background:#F7F7F7;}
.openLayerIpRouteBg{ width:100%; height:100%; overflow:hidden; background:#F7F7F7; position:absolute; top:0;left:0; opacity:0.8; filter:alpha(opacity=85);}
</style>
<script type="text/javascript">
	var pageSize = <%= uc.getPageSize() %>;
	var entityId = '${entityId}';
	var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
	var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
	
	//关闭弹出框
	function closeBtClick(){
		window.parent.closeWindow('ipRouteConfig');
	}
	//校验路由距离的输入
	function checkDistance(id){
		var distanceInput = $("#" +id).val().trim();
		var reg = /^[1-9]\d*$/; 
		if(reg.exec(distanceInput) && distanceInput>=1 && distanceInput<=255){
			return true;
		}else{
			$("#" +id).focus();
			return false;
		}
	}
	//添加静态路由
	function addIpRoute(){
		var destIp = getIpValue("destIpInput");
		var destMask = getIpValue("destMaskInput");
		var nextHop = getIpValue("nextHopInput");
		var distance = $('#routeDistance').val();
		var track = $('#trackSelect').val();
		//对添加时的用户输入进行校验
		if(destIp != '0.0.0.0' || destMask != '0.0.0.0' ){
			//先验证目的IP的输入
			if(ipIsFilled("destIpInput")){
				if(!checkIsNomalIp(destIp)){
					window.parent.showMessageDlg("@COMMON.tip@", "@IPROUTE.destIpLimit@",null,function(){
						ipFocus("destIpInput",1);
					});
					return;
				}
			}else{
				window.parent.showMessageDlg("@COMMON.tip@", "@IPROUTE.destIpInput@",null,function(){
					ipFocus("destIpInput",1);
				});
				return;
			}
			//对目的IP掩码进行校验
			if(ipIsFilled("destMaskInput")){
				//掩码不能为全0
				if(destIp != '0.0.0.0' && destMask == "0.0.0.0"){
					window.parent.showMessageDlg("@COMMON.tip@", "@IPROUTE.destIpZeroLimit@",null,function(){
						ipFocus("destMaskInput",1);
					});
					return; 
				}else{
					if(!checkedIpMask(destMask)){
						window.parent.showMessageDlg("@COMMON.tip@", "@IPROUTE.destIpTip@",null,function(){
							ipFocus("destMaskInput",1);
						});
						return;
					}
				}
			}else{
				window.parent.showMessageDlg("@COMMON.tip@", "@IPROUTE.destIpMaskTip@",null,function(){
					ipFocus("destMaskInput",1);
				});
				return;
			}
		}
		
		//对下一跳地址进行校验
		if(ipIsFilled("nextHopInput")){
			if(!checkIsNomalIp(nextHop)){
				window.parent.showMessageDlg("@COMMON.tip@", "@IPROUTE.nextHopLimit@",null,function(){
					ipFocus("nextHopInput",1);
				});
				return;
			}
		}else{
			window.parent.showMessageDlg("@COMMON.tip@", "@IPROUTE.nextHopTip@",null,function(){
				ipFocus("nextHopInput",1);
			});
			return;
		}
		//对输入的路由距离进行校验
		if(!checkDistance("routeDistance")){
			return;
		}	
		//对添加的进行重复验证
		var isRepeat = false;
		dataStore.each(function(rec){
			if(ipAAndipB(destIp,destMask) == rec.data.ipAddressDst && destMask == rec.data.ipMaskDst && nextHop == rec.data.nextHop ){
				isRepeat = true;
				return false;
			}
		});
		if(isRepeat){
			window.parent.showMessageDlg("@COMMON.tip@", "@IPROUTE.existsTip@",null,function(){
				ipFocus("destIpInput",1);
			});
			return;
		}
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/iproute/addIpRouteConfig.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				destIpAddr : destIp,
				destMaskAddr : destMask,
				nextHopIndex : nextHop,
				ipRouteDistance : distance,
				ipRouteTrack : track
			},
			success : function() {
				//window.parent.showMessageDlg("@COMMON.tip@", "@IPROUTE.addSuccess@");
				top.afterSaveOrDelete({
					title: '@COMMON.tip@',
			        html: '<b class="orangeTxt">@IPROUTE.addSuccess@</b>'
			    });
				closeAddLayer();
				dataStore.reload({
			  		callback : function() {
			  			var opIndex = dataStore.findBy(function(record, id) {   
						    return record.get('ipAddressDst') == ipAAndipB(destIp,destMask) && record.get('ipMaskDst') == destMask && record.get('nextHop') == nextHop;   
						});
			  			ipRouteGrid.getSelectionModel().selectRow(opIndex);
			        }
				});
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@IPROUTE.addFailed@");
			},
			cache : false
		});
	}
	//从设备刷新数据
	function refreshData(){
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/iproute/refreshIpRouteConfig.tv',
			type : 'POST',
			data : {
				entityId : entityId
			},
			success : function() {
				//window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchOk@");
				top.afterSaveOrDelete({
					title: '@COMMON.tip@',
			        html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
			    });
				dataStore.reload();
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
			},
			cache : false
		});
	}
	//显示添加页面
	function showAddLayer(){
		//最多只能添加1024条
		if(dataStore.getCount() < 1024){
			setIpValue("destIpInput", "");
			setIpValue("destMaskInput", "");
			setIpValue("nextHopInput", "");
			$('#routeDistance').val(1);
			$('#trackSelect').val(0);
			$("#addOpenLayer").fadeIn();
		}else{
			window.parent.showMessageDlg("@COMMON.tip@", "@IPROUTE.totalLimit@");
			return;
		}
	}
	//关闭添加页面
	function closeAddLayer(){
		$("#addOpenLayer").fadeOut();
	}
	//将track值转换为track名称
	function trackRender(value, cellmate, record){
		switch(value){
			case 0 : return "@IPROUTE.noBinding@"; break;
			case 1 : return "@IPROUTE.bfdBinding@"; break;
			case 2 : return "@IPROUTE.icmpBinding@"; break;
			case 3 : return "@IPROUTE.noBinding@"; break;
		}
	}
	//构建Grid操作栏
	function opeartionRender(value, cellmate, record){
		if(operationDevicePower){
			return String.format("<a href='javascript:;' onClick='showModifyLayer()'>@COMMON.modify@</a> / <a href='javascript:;' onClick='deleteIpRoute()'>@COMMON.delete@</a>");
		}else{
			return '--'
		}
	}
	//显示修改页面
	function showModifyLayer(){
		var select = ipRouteGrid.getSelectionModel().getSelected();
		$('#destUpdate').text(select.data.ipAddressDst);
		$('#maskUpdate').text(select.data.ipMaskDst);
		$('#nextHopUpdate').text(select.data.nextHop);
		$('#updateDistance').val(select.data.distance);
		var $track = select.data.track;
		if($track == 0 || $track == 3){
			$('#udpateTrack').val(3);
		}else{
			$('#udpateTrack').val($track);
		}
		$('#updateOpenLayer').fadeIn();
	}
	//关闭修改页面
	function closeUpdateLayer(){
		$('#updateOpenLayer').fadeOut();
	}
	//修改静态路由
	function modifyIpRoute(){
		var destIp = $('#destUpdate').text();
		var destMask = $('#maskUpdate').text();
		var nextHop = $('#nextHopUpdate').text();
		var newDistance = $('#updateDistance').val();
		var newTrack = $('#udpateTrack').val();
		//对输入的路由距离进行校验
		if(!checkDistance("updateDistance")){
			return;
		}	
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/iproute/modifyIpRouteConfig.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				destIpAddr : destIp,
				destMaskAddr : destMask,
				nextHopIndex : nextHop,
				ipRouteDistance : newDistance,
				ipRouteTrack : newTrack
			},
			success : function() {
				//window.parent.showMessageDlg("@COMMON.tip@", "@IPROUTE.updateSuccess@");
				top.afterSaveOrDelete({
					title: '@COMMON.tip@',
			        html: '<b class="orangeTxt">@IPROUTE.updateSuccess@</b>'
			    });
				closeUpdateLayer();
				dataStore.reload({
			  		callback : function() {
			  			var opIndex = dataStore.findBy(function(record, id) {   
						    return record.get('ipAddressDst') == destIp && record.get('ipMaskDst') == destMask && record.get('nextHop') == nextHop;   
						});
			  			ipRouteGrid.getSelectionModel().selectRow(opIndex);
			        }
				});
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@IPROUTE.updateFailed@");
			},
			cache : false
		});
	}
	
	//删除静态路由
	function deleteIpRoute(){
		var select = ipRouteGrid.getSelectionModel().getSelected();
		window.parent.showConfirmDlg("@COMMON.tip@", "@IPROUTE.deleteConfirm@", function(type) {
			if (type == 'no') {
				return;                      
			}
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
			$.ajax({
				url : '/epon/iproute/deleteIpRouteConfig.tv',
				type : 'POST',
				data : {
					entityId : entityId,
					destIpAddr : select.data.ipAddressDst,
					destMaskAddr : select.data.ipMaskDst,
					nextHopIndex : select.data.nextHop
				},
				success : function() {
					//window.parent.showMessageDlg("@COMMON.tip@", "@IPROUTE.deleteSuccess@");
					top.afterSaveOrDelete({
						title: '@COMMON.tip@',
				        html: '<b class="orangeTxt">@IPROUTE.deleteSuccess@</b>'
				    });
					dataStore.reload();
				},
				error : function(json) {
					window.parent.showMessageDlg("@COMMON.tip@", "@IPROUTE.deleteFailed@");
				},
				cache : false
			});
		});
	}
	
	//构建分页工具栏
	function buildPagingToolBar() {
	   var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:dataStore,
	       displayInfo: true, items: ["-", String.format("@COMMON.displayPerPage@", pageSize), '-']});
	   return pagingToolbar;
	}
	
	$(document).ready(function(){
		window.dataStore = new Ext.data.JsonStore({
			url : '/epon/iproute/loadIpRouteList.tv',
			fields : ["ipAddressDst", "ipMaskDst", "nextHop","distance","track"],
			root : 'data',
			totalProperty: 'rowCount',
			baseParams : {
				entityId : entityId
			}
		});
		
		window.colModel = new Ext.grid.ColumnModel([ 
		   	{header : "@IPROUTE.destIp@",width : 120,align : 'center',dataIndex : 'ipAddressDst'}, 
		 	{header : "@IPROUTE.destIpMask@",width : 120,align : 'center',dataIndex : 'ipMaskDst'}, 
		 	{header : "@IPROUTE.nextHop@",width : 120,align : 'center',dataIndex : 'nextHop'}, 
		 	{header : "@IPROUTE.distance@",width : 60,align : 'center',dataIndex : 'distance'}, 
		 	{header : "@IPROUTE.trackBinding@",width : 80,align : 'center',dataIndex : 'track', renderer : trackRender}, 
		 	{header : "@COMMON.manu@",width : 120, align : 'center',dataIndex :'op', renderer : opeartionRender}
		]);
		
		window.ipRouteGrid =  new Ext.grid.GridPanel({
			id : 'ipRouteGrid',
			title : "@IPROUTE.ipRouteList@",
			height : 390,
			border : true,
			cls : 'normalTable',
			store : dataStore,
			colModel : colModel,
			bbar : buildPagingToolBar(),
			viewConfig : {
				forceFit : true
			},
			renderTo : 'contentGrid',
			sm : new Ext.grid.RowSelectionModel({
				singleSelect : true
			})
		});		
		dataStore.load({params: {start: 0, limit: pageSize}});
		
		var destIpInput = new ipV4Input("destIpInput", "destSpan");
		var destMaskInput = new ipV4Input("destMaskInput", "maskSpan");
		var nextHopInput = new ipV4Input("nextHopInput", "nextHopSpan");
		setIpWidth("all", 180);
		if(!operationDevicePower){
			$("#addIpRoute").attr("disabled",true);
			$("#modifyIpRoute").attr("disabled",true);
			$("#addLayer").attr("disabled",true);
		}
	    if(!refreshDevicePower){
	        $("#refreshData").attr("disabled",true);
	    }
	})

</script>
</head>
<body class="openWinBody">
	<div class="edge10">
		<div id="contentGrid"></div>
	</div>
	
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 pT5 noWidthCenter">
	         <li><a id="refreshData" onclick='refreshData()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
	         <li><a  onclick='showAddLayer()' id='addLayer' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoAdd"></i>@IPROUTE.addIpRoute@</span></a></li>
	         <li><a  onclick='closeBtClick()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
	     </ol>
	</div>
	
	<div class="openLayerIpRoute" id="addOpenLayer">
		<div class="openLayerIpRouteMain">
			<div class="zebraTableCaption pT20">
     			<div class="zebraTableCaptionTitle"><span class="blueTxt"><label class="blueTxt">@IPROUTE.addIpRoute@</label></span></div>
				     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				         <tbody>
				             <tr>
				                <td class="rightBlueTxt" width="150">
				                	@IPROUTE.destIp@:
								</td>
								<td>
									<span id="destSpan"></span>
								</td>
							</tr>
							<tr class="darkZebraTr">
				                <td class="rightBlueTxt" width="150">
				                	@IPROUTE.destIpMask@:
								</td>
								<td>
									<span id="maskSpan"></span>
								</td>
							</tr>
							<tr>
				                <td class="rightBlueTxt" width="150">
				                	@IPROUTE.nextHop@:
								</td>
								<td>
									<span id="nextHopSpan"></span>
								</td>
							</tr>
							<tr>
				                <td class="rightBlueTxt" width="150">
				                	@IPROUTE.distance@:
								</td>
								<td>
									<input type="text" id="routeDistance" class="normalInput w180" maxlength="3" toolTip="@IPROUTE.distanceTip@"/>
								</td>
							</tr>
							<tr>
				                <td class="rightBlueTxt withoutBorderBottom" width="150">
				                	@IPROUTE.trackBinding@:
								</td>
								<td class="withoutBorderBottom">
									<select id="trackSelect" class="normalSel w180">
										<option value="0" selected>@IPROUTE.noBinding@</option>
										<option value="1">@IPROUTE.bfdBinding@</option>
										<!-- <option value="2">@IPROUTE.icmpBinding@</option> -->
										<!-- <option value="3">@IPROUTE.deleteBinding@</option> -->
									</select>
								</td>
							</tr>
						</tbody>
					</table>
					<div class="yellowTip" style="padding-top:8px; padding-bottom:8px;padding-left:80px">
     					<b class="orangeTxt">@IPROUTE.destIpResult@</b>
     				</div>
					<div class="noWidthCenterOuter clearBoth">
					     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
					         <li><a href="javascript:;" class="normalBtnBig" id='addIpRoute' onclick="addIpRoute()"><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></li>
					         <li><a href="javascript:;" class="normalBtnBig" onclick="closeAddLayer()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
					     </ol>
					</div>
				</div>
		</div>
		<div class="openLayerIpRouteBg"></div>
	</div>
	
	<div class="openLayerIpRoute" id="updateOpenLayer">
		<div class="openLayerIpRouteMain">
			<div class="zebraTableCaption">
     			<div class="zebraTableCaptionTitle"><span class="blueTxt"><label class="blueTxt">@IPROUTE.updateIpRoute@</label></span></div>
			     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
			         <tbody>
			             <tr>
			                <td class="rightBlueTxt" width="150">
			                	@IPROUTE.destIp@:
							</td>
							<td>
								<span id="destUpdate"></span>
							</td>
						</tr>
						<tr class="darkZebraTr">
			                <td class="rightBlueTxt" width="150">
			                	@IPROUTE.destIpMask@:
							</td>
							<td>
								<span id="maskUpdate"></span>
							</td>
						</tr>
						<tr>
			                <td class="rightBlueTxt" width="150">
			                	@IPROUTE.nextHop@:
							</td>
							<td>
								<span id="nextHopUpdate"></span>
							</td>
						</tr>
						<tr>
			                <td class="rightBlueTxt" width="150">
			                	@IPROUTE.distance@:
							</td>
							<td>
								<input type="text" id="updateDistance" class="normalInput w180" maxlength="3" toolTip="@IPROUTE.distanceTip@"/>
							</td>
						</tr>
						<tr>
			                <td class="rightBlueTxt" width="150">
			                	@IPROUTE.trackBinding@:
							</td>
							<td>
								<select id="udpateTrack" class="normalSel w180">
									<!-- <option value="0">@IPROUTE.noBinding@</option> -->
									<option value="1">@IPROUTE.bfdBinding@</option>
									<!-- <option value="2">@IPROUTE.icmpBinding@</option> -->
									<option value="3">@IPROUTE.noBinding@</option>
								</select>
							</td>
						</tr>
					</tbody>
				</table>
				<div class="noWidthCenterOuter clearBoth">
				     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
				         <li><a href="javascript:;" class="normalBtnBig" id='modifyIpRoute' onclick="modifyIpRoute()"><span><i class="miniIcoEdit"></i>@COMMON.modify@</span></a></li>
				         <li><a href="javascript:;" class="normalBtnBig" onclick="closeUpdateLayer()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
				     </ol>
				</div>
			</div>
		</div>
		<div class="openLayerIpRouteBg"></div>
	</div>
</body>
</Zeta:HTML>