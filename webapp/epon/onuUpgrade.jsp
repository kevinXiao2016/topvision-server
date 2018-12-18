<%@ page language="java" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@include file="/include/ZetaUtil.inc"%>
    <Zeta:Loader>
	    library ext
	    library jquery
	    library zeta
	    module epon
	</Zeta:Loader>
    <link rel="stylesheet" type="text/css" href="/epon/multiselect/jquery.multiSelect.css"/>
    <script type="text/javascript" src="/epon/multiselect/jquery.bgiframe.min.js"></script>
    <script type="text/javascript" src="/epon/multiselect/jquery.multiSelect.js"></script>
    
    <script type="text/javascript">
   	    var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
    	var entityId = <%=request.getParameter("entityId")%>
    	var slotNo = <%=request.getParameter("slotNo")%>;
    	var slotPonArray = new Array();
    	var ponIndexArray = new Array();
    	var ponIndexs;
    	var onuListGrid;
    	var onuListStore;
    	
    	Ext.onReady(function() {
    		// 获取所有PON口
    		Ext.Ajax.request({
    			url: '/onu/loadSlotPonRelation.tv?r=' + Math.random,
    			params: {
    				entityId: entityId
    			},
    			success: function(response) {
    				var result = Ext.decode(response.responseText);
    				var tmpResult;
    				$.each(result, function(i, n) {
    					slotPonArray[i] = new Array();
    					slotPonArray[i] = [n.slotRealIndex, n.portRealIndex, n.portIndex];
    					if(n.slotRealIndex == 0){
    						n.slotRealIndex = 1;
    					}
    					if (tmpResult) {
    						if (tmpResult.slotRealIndex != n.slotRealIndex) {
    							if (slotNo == n.slotRealIndex) {
    								$('#slots').append('<option value="' + n.slotRealIndex + '" selected>' + n.slotRealIndex + '</option>');
    							} else {
    								$('#slots').append('<option value="' + n.slotRealIndex + '">' + n.slotRealIndex + '</option>');
    							}
    						}
    					} else {
    						if (slotNo == n.slotRealIndex) {
    							$('#slots').append('<option value="' + n.slotRealIndex + '" selected>' + n.slotRealIndex + '</option>');
    						} else {
    							$('#slots').append('<option value="' + n.slotRealIndex + '">' + n.slotRealIndex + '</option>');
    						}
    					}
    					tmpResult = n;
    					// PON口初始化
    					if (slotNo == n.slotRealIndex) {
    						$('#pons').append('<option value="' + n.portIndex + '" selected>' + n.portRealIndex + '</option>');
    						ponIndexArray[ponIndexArray.length] = n.portIndex;
    					}
    				});
    				ponIndexs = ponIndexArray.toString();
    				initMultiSelect();
    				// slot-pon级联操作
    				$('#slots').change(function() {
    					// 动态修改pon端口号
    					$('#pons').empty();
    					$('#pons').css('width', 184);
    					ponIndexArray = new Array();
    					$.each(slotPonArray, function(i, n) {
							if (n[0] == $('#slots').val()) {
								$('#pons').append('<option value="' + n[2] + '" selected>' + n[1] + '</option>');
								ponIndexArray[ponIndexArray.length] = n[2];
							}
    					});
    					ponIndexs = ponIndexArray.toString();
    					initMultiSelect();
    				});
    				// 首次进入页面时，获取可升级下级设备列表。
    				onuListStore = new Ext.data.Store({
		 			proxy: new Ext.data.HttpProxy({
		 				url: '/onu/loadAvailableUpgradeOnuList.tv?entityId='+entityId+'&ponIndexs=' + ponIndexs + '&onuType=' + $('#onuType').val() + '&r=' + Math.random()
		 			}),
		 			reader: new Ext.data.JsonReader({
		 				}, Ext.data.Record.create([{
		 					name: 'ponName'
		 				}, {
		 					name: 'onuIndex'
		 				}, {
		 					name: 'onuUniqueIdentification'
		 				}, {
		 					name: 'onuType'
		 				}, {
		 					name: 'onuPreType'
		 				}, {
		 					name: 'typeId'
		 				},{
		 					name: 'onuSoftwareVersion'
		 				},{
		 					name: 'topOnuHardwareVersion'
		 				}]))
			 		});
			 		onuListStore.load();
			 		var sm = new Ext.grid.CheckboxSelectionModel();
			 		onuListGrid = new Ext.grid.GridPanel({
			 			title: "@SERVICE.onuDeviceList@",
			 			renderTo: 'onuListGrid',
			 			//width: 750,
			 			height: 340,
			 			autoScroll: true,
			 			columns: [sm, {
			 				header: "@SERVICE.belongPon@" , dataIndex: 'ponName', width: 90, align: 'center'
			 			}, {
			 				header: "MAC | SN" , dataIndex: 'onuUniqueIdentification', width: 130, align: 'center'
			 			}, {
			 				header: "@SERVICE.onuDeviceType@" , dataIndex: 'onuPreType', width: 110, align: 'center', renderer: function(value,para2,record) {
			 					var onuTypeDescr = '';
			 					switch (value) {
			 						case 33:
			 							onuTypeDescr = 'PN8621';
			 							break;
			 						case 34:
			 							onuTypeDescr = 'PN8622';
			 							break;
			 						case 36:
			 							onuTypeDescr = 'PN8624';
			 							break;
			 						case 37:
			 							onuTypeDescr = 'PN8625';
			 							break;
			 						case 38:
			 							onuTypeDescr = 'PN8626';
			 							break;
			 						case 40:
			 							onuTypeDescr = 'PN8640';
			 							break;
			 						case 65:
			 							onuTypeDescr = 'PN8641';
			 							break;
			 						case 68:
			 							onuTypeDescr = 'PN8644';
			 							break;
			 						case 71:
			 							onuTypeDescr = 'PN8647';
			 							break;
			 						case 241:
			 							//alert(arguments[2].data.typeId)
			 							if(record.data.typeId == 30001){
			 								onuTypeDescr = 'CC8800A';
			 							}else{
			 								onuTypeDescr = 'CC8800C-A';
			 							}
			 							break;
			 						case 244:
			 							onuTypeDescr = 'CC8800E(强集中)';
			 							break;
			 						case 246:
			 							onuTypeDescr = 'CC8800C-E(强集中)';
			 							break;
			 						case 247:
			 							onuTypeDescr = 'CC8800D-E(强集中)';
			 							break;
			 						case 248:
			 							onuTypeDescr = 'CC8800C-10G(强集中)';
			 							break;
			 						case 255:
			 							onuTypeDescr = "@SERVICE.unkownType@";
			 							break;
			 						default:
			 							onuTypeDescr = "@SERVICE.unkownType@";
			 					}
			 					return onuTypeDescr
			 				}
			 			}, {
			 				header: "@SERVICE.sftwVersion@" , dataIndex: 'onuSoftwareVersion', width: 150, align: 'center'
			 			},{
			 				header: "@SERVICE.setHaw@", dataIndex: 'topOnuHardwareVersion', width: 150, align: 'center'
			 			}],
			 			sm: sm,
			 			store: onuListStore,
			 			viewConfig: { forceFit:true}
			 		});
    			}
    		});
    	});
    	function initMultiSelect() {
    		// 初始化多选列表
    		$("#pons").multiSelect({
	 			selectAllText: "@SERVICE.selectAll@" ,
	 			noneSelected: "@SERVICE.selectUdtPon@" ,
	 			oneOrMoreSelected: "@SERVICE.selectedPon@" ,
	 			listHeight: 200
	 		}, function() {
	 			ponIndexs = $('#pons').selectedValuesString();
	 		});
    	}
    	
    	//查询可用的Onu
    	function searchAvailableOnu() {
    		onuListStore.proxy.conn.url = '/onu/loadAvailableUpgradeOnuList.tv?entityId='+entityId+'&ponIndexs=' + ponIndexs + '&onuType=' + $('#onuType').val() + '&r=' + Math.random();
			onuListStore.load({
				callback: function(records) {
					if (records && records.length === 0) {
						window.parent.showMessageDlg("@COMMON.tip@", "@SERVICE.selectedPonNoOnu@")
					}
				}
			});
    	}
    	
    	//查看Onu升级历史记录
    	function onuUpgradeHistory() {
    		window.parent.createDialog('onuUpgradeHistory', "@SERVICE.onuUdtHistory@" , 800, 500, '/epon/onuUpgradeHistory.jsp?entityId=' + entityId, null, true, true);
    	}
    	
    	//进入onu升级页面
    	function onuUpgrade() {
    		if($("#onuUpgrade").hasClass("disabledAlink")){
    			return;
    		}
    		var sm = onuListGrid.getSelectionModel();
    		if (sm != null && sm.hasSelection()) {
    			var onuIndexs = new Array();
				var selections = sm.getSelections();
				for (var i = 0; i < selections.length; i++) {
					onuIndexs[i] = selections[i].data.onuIndex;
				}
    		} else {
    			window.parent.showMessageDlg("@COMMON.tip@", "@SERVICE.selectUdtOnu@" )
    			return;
    		}
			window.parent.createDialog('onuUpgradeFileUpload', "@SERVICE.selectUdtFile@" , 600, 370, '/onu/onuUpgradeFileUpload.tv?topOnuUpgradeSlotNum=' + $("#slots").val() + 
				'&topOnuUpgradeOnuType=' + $("#onuType").val() + '&topOnuUpgradeFileName=9&topOnuUpgradeMode=4&topOnuUpgradeOnuList=' + onuIndexs.toString() +
				'&topOnuUpgradeOperAction=1' + '&entityId=' + entityId + '&r=' + Math.random(), null, true, true);
    	}
    	
    	function cancelClick() {
    		window.parent.closeWindow('onuUpgrade');
    	}
    	
    	function authLoad(){
    		if(!operationDevicePower){
    			//$("#onuUpgrade").attr("disabled",true);
    			$("#onuUpgrade").addClass("disabledAlink");
    		}
    	}
    </script>
</head>
<body class="openWinBody" onload="authLoad()">  
<div class="edge10 pT20">
	<table>
		<tr>
			<td class="blueTxt" width="45">@EPON.slotNum@:</td>
			<td>
				<select id="slots" style="width: 50px">
				</select>
			</td>
			<td class="blueTxt pL10">@SERVICE.ponNum@:</td>
			<td>
				<select id="pons" style="width: 150px" multiple>
				</select>
			</td>
			<td class="blueTxt pL10">@SERVICE.onuDeviceType@:</td>
			<td>
				<select id="onuType" style="width: 140px">
					<option value="33">PN8621</option>
					<option value="34">PN8622</option>
					<option value="36">PN8624</option>
					<option value="37">PN8625</option>
					<option value="38">PN8626</option>
					<option value="40">PN8628</option>
					<option value="65">PN8641</option>
					<option value="68">PN8644</option>
					<option value="71">PN8647</option>
					<% if(uc.hasSupportModule("cmc")){%>
						<option value="241">CC8800A/C-A</option>
						<option value="244">CC8800E(强集中)</option>
						<option value="246">CC8800C-E(强集中)</option>
						<option value="247">CC8800D-E(强集中)</option>
						<option value="248">CC8800C-10G(强集中)</option>
					<% } %>
					<option value="255">@SERVICE.otherType@</option>
				</select>
			</td>
			<td width="10px"></td>
			<td>
				<a id='searchAvailableOnu' href="javascript:;" class="normalBtn"  onclick="searchAvailableOnu()"><span><i class="miniIcoSearch"></i>@COMMON.query@</span></a>
			</td>
		</tr>
	</table>
	
	<div id="onuListGrid"  class="normalTable mT10"></div>
</div>
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl noWidthCenter pB0 pT0">
	         <li><a id='onuUpgradeHistory' href="javascript:;" class="normalBtnBig"  onclick="onuUpgradeHistory()"><span><i class="miniIcoInfo"></i>@SERVICE.udtHistory@</span></a></li>
	         <li><a id='onuUpgrade' href="javascript:;" class="normalBtnBig"  onclick="onuUpgrade()"><span><i class="miniIcoArrUp"></i>@SERVICE.upgrade@</span></a></li>
	         <li><a id='cancelBt' href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
	     </ol>
	</div>
	
</body>
</Zeta:HTML>