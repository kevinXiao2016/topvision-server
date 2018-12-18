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
    module loopback
    import js.tools.ipText static
</Zeta:Loader>
<style type="text/css">
body,html{height:100%; overflow:hidden;}
.openLayerSubIp{ width:100%; height:100%; overflow:hidden; position:absolute; top:0;left:0;  display:none;}
.openLayerSubIpMain{ width:500px; height:220px; overflow:hidden; position:absolute; top:100px; left:140px; z-index:2;  background:#F7F7F7;}
.openLayerSubIpBg{ width:100%; height:100%; overflow:hidden; background:#F7F7F7; position:absolute; top:0;left:0; opacity:0.8; filter:alpha(opacity=85);}
</style>
<script type="text/javascript">
	//环回接口子IP索引取值集合
	var indexArray = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20];
	var entityId = '${entityId}';
	var interfaceIndex = '${interfaceIndex}';
	//关闭当前弹出框
	function closeBtClick(){
		window.parent.closeWindow('loopBackSubIpConfig');
	}
	
	//添加一条数据
	function addClick(){
		var itemIndex = $("#subIndexSelect").val();
		var ipAddress = getIpValue("priIp");
		var maskAddress = getIpValue("priMask");
		//对输入的ip地址和掩码进行校验
		if(checkInput("priIp","priMask")){
			//判断是否在同一个网段
			var isRepeat = false;
			dataStore.each(function(rec){
				if(checkSameSubSet(ipAddress,maskAddress,rec.data.loopbackSubIpAddr,rec.data.loopbackSubMask)){
					isRepeat = true;
					return false;
				}
			});
			if(isRepeat){
				window.parent.showMessageDlg("@COMMON.tip@", "@SUBIP.sameSegment@",null,function(){
					ipFocus("priIp",1);
				});
				return;
			}
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
			$.ajax({
				url : '/epon/loopback/addLBSubIp.tv',
				type : 'POST',
				data : {
					entityId : entityId,
					interfaceIndex : interfaceIndex,
					subIpIndex : itemIndex,
					ipAddress : ipAddress,
					maskAddress : maskAddress
				},
				success : function() {
					window.parent.showMessageDlg("@COMMON.tip@", "@LOOPBACK.addSuccess@");
					dataStore.reload();
				},
				error : function(json) {
					window.parent.showMessageDlg("@COMMON.tip@", "@LOOPBACK.addFailed@<br/>@LOOPBACK.failedReason@");
				},
				cache : false
			});
		}
	}
	
	//删除一条数据
	function deleteSubIp(index){
		window.parent.showConfirmDlg("@COMMON.tip@", "@SUBIP.deleteSubIpTip@", function(type) {
			if (type == 'no') {
				return;
			}
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
			$.ajax({
				url : '/epon/loopback/deleteLBSubIp.tv',
				type : 'POST',
				data : {
					entityId : entityId,
					interfaceIndex : interfaceIndex,
					subIpIndex : index
				},
				success : function() {
					window.parent.showMessageDlg("@COMMON.tip@", "@LOOPBACK.deleteSuccess@");
					dataStore.reload();
				},
				error : function(json) {
					window.parent.showMessageDlg("@COMMON.tip@", "@LOOPBACK.deleteFailed@");
				},
				cache : false
			});
		});
	}
	
	//从设备刷新数据
	function refreshData(){
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
		$.ajax({
			url : '/epon/loopback/refreshLBSubIp.tv',
			type : 'POST',
			data : {
				entityId : entityId
			},
			success : function() {
				window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchOk@");
				dataStore.reload();
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
			},
			cache : false
		});
	}
	//grid中操作栏处理
	function opeartionRender(value, cellmate, record){
		var index = record.data.loopbackSubIpSeqIndex;
		return String.format("<a href='javascript:;' onClick='showModifyLayer()'>@COMMON.modify@</a> / <a href='javascript:;' onClick='deleteSubIp({0})'>@COMMON.delete@</a>",index);
	}
	
	//显示修改页面
	function showModifyLayer(){
		var select = subIpGrid.getSelectionModel().getSelected();
		$('#indexSpan').text(select.data.loopbackSubIpSeqIndex);
		setIpValue("ipAddrChanged",select.data.loopbackSubIpAddr);
		setIpValue("ipMaskChanged",select.data.loopbackSubMask);
		openOpenLayer();
	}
	
	//修改环回接口子IP
	function modifySubIp(){
		var itemIndex = $('#indexSpan').text();
		var ipAddress = getIpValue("ipAddrChanged");
		var maskAddress = getIpValue("ipMaskChanged");
		var select = subIpGrid.getSelectionModel().getSelected();
		//对输入的ip地址和掩码进行校验
		if(checkInput("ipAddrChanged","ipMaskChanged")){
			//判断是否在同一个网段
			var isRepeat = false;
			dataStore.each(function(rec){
				//修改时排除与自己的比较
				if(rec.data.loopbackSubIpSeqIndex != select.data.loopbackSubIpSeqIndex){  
					if(checkSameSubSet(ipAddress,maskAddress,rec.data.loopbackSubIpAddr,rec.data.loopbackSubMask)){
						isRepeat = true;
						return false;
					}
				}
			});
			if(isRepeat){
				window.parent.showMessageDlg("@COMMON.tip@", "@SUBIP.sameSegment@",null,function(){
					ipFocus("ipAddrChanged",1);
				});
				return;
			}
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
			$.ajax({
				url : '/epon/loopback/modifyLBSubIp.tv',
				type : 'POST',
				data : {
					entityId : entityId,
					interfaceIndex : interfaceIndex,
					subIpIndex : itemIndex,
					ipAddress : ipAddress,
					maskAddress : maskAddress
				},
				success : function() {
					window.parent.showMessageDlg("@COMMON.tip@", "@LOOPBACK.updateSuccess@");
					dataStore.reload();
					closeOpenLayer();
				},
				error : function(json) {
					window.parent.showMessageDlg("@COMMON.tip@", "@LOOPBACK.updateFailed@<br/>@LOOPBACK.failedReason@");
				},
				cache : false
			});
		}
	}
	
	//初始化索引下拉框
	function initData(usableIndex){
		$("#subIndexSelect").empty();
		if(usableIndex.length > 0){
			$('#addInputLayer').fadeOut();
			$.each(usableIndex,function(i,item){
				$("#subIndexSelect").append(String.format("<option value='{0}'>{0}</option>",item));
			})
		}else{
			setIpValue("priIp","");
			setIpValue("priMask","");
			$('#addInputLayer').fadeIn();
		}
	}
	
	//对输入的ip地址和掩码进行校验
	function checkInput(ipIputId, maskInputId){
		var ipInput = getIpValue(ipIputId);
		var maskInput = getIpValue(maskInputId);
		///首先判断Ip输入是否完整
		if(!ipIsFilled(ipIputId)){
			window.parent.showMessageDlg("@COMMON.tip@", "@SUBIP.subIpInput@",null,function(){
				ipFocus(ipIputId,1);
			});
			return false;
		}else{
			//首先判断掩码输入是否完整
			if(!ipIsFilled(maskInputId)){
				window.parent.showMessageDlg("@COMMON.tip@", "@SUBIP.subMaskInput@",null,function(){
					ipFocus(maskInputId,1);
				});
				return false;
			}else{
				//主IP地址不能为组播、多播以及保留地址
				if(!checkIsNomalIp(ipInput)){
					window.parent.showMessageDlg("@COMMON.tip@", "@SUBIP.subIpLimit@",null,function(){
						ipFocus(ipIputId,1);
					});
					return false;
				}
				//判断掩码输入是否符合
				var maskArray = maskInput.split(".");
				if(maskArray[0] > 0){  //排除掩码地址为全为的情况
					if(!checkedIpMask(maskInput)){  
						window.parent.showMessageDlg("@COMMON.tip@", "@SUBIP.subMaskLimit@",null,function(){
							ipFocus(maskInputId,1);
						});
						return false;
					}
				}else{
					window.parent.showMessageDlg("@COMMON.tip@", "@SUBIP.subMaskLimit@",null,function(){
						ipFocus(maskInputId,1);
					});
					return false;
				}
			}
		}
		return true;
	}
	
	//检查是不是在同一个网段
	function checkSameSubSet(sIp, sMask, dIp, dMask){
		if(ipAAndipB(sIp,sMask) == ipAAndipB(dIp,sMask) || ipAAndipB(sIp,dMask) == ipAAndipB(dIp,dMask)){
			return true;
		}
		return false;
	}
	
	$(document).ready(function(){
		window.dataStore = new Ext.data.JsonStore({
			url : '/epon/loopback/loadLBSubIpList.tv',
			fields : ["loopbackSubIpSeqIndex", "loopbackSubIpAddr", "loopbackSubMask"],
			baseParams : {
				entityId : entityId,
				interfaceIndex : interfaceIndex
			},
			listeners : {
				load : function(s,records, options) {
					var newChangedList = indexArray.concat();
					$.each(records,function(i,record){
						if($.inArray(record.data.loopbackSubIpSeqIndex, newChangedList) > -1){
							newChangedList.remove( record.data.loopbackSubIpSeqIndex );
						}
					});
					initData(newChangedList);
		        } 
			}
		});
		
		window.colModel = new Ext.grid.ColumnModel([ 
		   	{header : "@SUBIP.subIpIndex@", width : 80, align : 'center', dataIndex : 'loopbackSubIpSeqIndex'}, 
		 	{header : "@SUBIP.subIpAddress@", width : 150, align : 'center', dataIndex : 'loopbackSubIpAddr'}, 
		 	{header : "@SUBIP.subIpMask@", width : 150, align : 'center', dataIndex : 'loopbackSubMask'}, 
		 	{header : "@COMMON.manu@", width : 120, align : 'center', dataIndex :'op', renderer : opeartionRender}
		]);
		
		window.subIpGrid =  new Ext.grid.GridPanel({
			id : 'subIPGrid',
			title : "@SUBIP.subIpList@",
			height : 325,
			border : true,
			cls : 'normalTable',
			store : dataStore,
			colModel : colModel,
			viewConfig : {
				forceFit : true
			},
			renderTo : 'contentGrid',
			sm : new Ext.grid.RowSelectionModel({
				singleSelect : true
			})
		});		
		dataStore.load();
		
		var priIpInput = new ipV4Input("priIp", "span1");
		var priMaskInput = new ipV4Input("priMask", "span2");
		
		var ipAddrChanged = new ipV4Input("ipAddrChanged", "span3");
		var ipMaskChanged = new ipV4Input("ipMaskChanged", "span4");
		setIpWidth("all", 180);
	});
	//打开修改时的遮罩层
	function openOpenLayer(){
		$("#openLayerSubIp").fadeIn();
	}
	//关闭修改遮罩层
	function closeOpenLayer(){
		$("#openLayerSubIp").fadeOut();
	}
	
</script>
</head>
<body class="openWinBody">
	<div class="edge10">
		<div class="yellowTip" style="padding-top:8px; padding-bottom:8px">
			@SUBIP.subIpConfigTip@
		</div>
		<table class="zebraTableRows" >
			<tr>
				<td class="withoutBorderBottom rightBlueTxt">@SUBIP.subIpIndex@:<font color=red>*</font></td>
				<td class="withoutBorderBottom">
					<select id="subIndexSelect" class='normalSel w50'></select>
				</td>
				<td class="withoutBorderBottom rightBlueTxt">@SUBIP.subIpAddress@:<font color=red>*</font></td>
				<td class="withoutBorderBottom"><span id="span1" class="modifiedFlag"></span></td>
				<td class="withoutBorderBottom rightBlueTxt">@SUBIP.subIpMask@:<font color=red>*</font></td>
				<td class="withoutBorderBottom"><span id="span2" class="modifiedFlag"></span></td>
				<td class="withoutBorderBottom"><a href="javascript:;" class="normalBtn" onclick="addClick()"><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a></td>
			</tr>
		</table>
		<div id="contentGrid"></div>
	</div>
	<div id="addInputLayer" style="width:100%; height:35px; background:#FFF; position: absolute; top:45px; left:0; opacity:0.8; filter:alpha(opacity=82); display:none;"></div>
	
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 noWidthCenter">
	         <li><a  onclick='refreshData()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
	         <li><a  onclick='closeBtClick()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
	     </ol>
	</div>
	
	<div class="openLayerSubIp" id="openLayerSubIp">
		<div class="openLayerSubIpMain">
			<div class="zebraTableCaption">
     			<div class="zebraTableCaptionTitle"><span>@SUBIP.updateSubIp@</span></div>
				     <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
				         <tbody>
				             <tr>
				                <td class="rightBlueTxt" width="160">
				                	@SUBIP.subIpIndex@:
								</td>
								<td>
									<span id="indexSpan"></span>
								</td>
							</tr>
							<tr class="darkZebraTr">
				                <td class="rightBlueTxt" width="160">
				                	@SUBIP.subIpAddress@:
								</td>
								<td>
									<span id="span3"></span>
								</td>
							</tr>
							<tr>
				                <td class="rightBlueTxt" width="160">
				                	@SUBIP.subIpMask@:
								</td>
								<td>
									<span id="span4"></span>
								</td>
							</tr>
						</tbody>
					</table>
					<div class="noWidthCenterOuter clearBoth">
					     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
					         <li><a href="javascript:;" class="normalBtnBig" onclick="modifySubIp()"><span><i class="miniIcoEdit"></i>@COMMON.modify@</span></a></li>
					         <li><a href="javascript:;" class="normalBtnBig" onclick="closeOpenLayer()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
					     </ol>
					</div>
				</div>
		</div>
		<div class="openLayerSubIpBg"></div>
	</div>
</body>
</Zeta:HTML>