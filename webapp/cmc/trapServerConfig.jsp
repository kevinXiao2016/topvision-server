<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
 <Zeta:Loader>
	library ext
	library jquery
	library zeta
	module cmc
	css css/white/disabledStyle
</Zeta:Loader>
<head>
<script>
	var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
	var entityId = '<s:property value="entityId"/>';
	//验证输入的IP地址是否合法，不能为广播地址，多播地址或全0
	function ipCheck(){
		var reg;
		var ip = $.trim($("#serverIPInput").val());
		
		//验证IP地址格式是否合法
		reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
		if (!reg.test(ip)) {
			window.parent.showMessageDlg("@CMC.title.tip@", "@syslog.ipError1@",null,function(){
				$("#serverIPInput").focus();
			});
			return false;
		}
		//验证是否为全0
		if (ip == "0.0.0.0") {
			window.parent.showMessageDlg("@CMC.title.tip@", "@syslog.ipError2@",null,function(){
				$("#serverIPInput").focus();
			});
			return false;
		}
		//验证是否为多播,多播地址范围为224.0.0.0～239.255.255.255
		reg = /(\d{1,3})/;
		var ipPart1 = reg.exec(ip)[0];
		if (224 <= ipPart1 && ipPart1 < 240) {
			window.parent.showMessageDlg("@CMC.title.tip@", "@syslog.ipError3@",null,function(){
				$("#serverIPInput").focus();
			});
			return false;
		}
		//验证是否为广播，广播地址的限制规则为:
		//必须为A,B,C类地址中的一个
		//屏蔽如下IP段: 191.255.0.0       192.0.0.0-192.0.0.255       223.255.255.0 
		var ip1 = ip.split(".")[0];
		var ip2 = ip.split(".")[1];
		var ip3 = ip.split(".")[2];
		if((1<=ip1 && ip1<=126) || (128<=ip1 && ip1<=223)){
			if((ip=="191.255.0.0") || ((ip1==192)&&(ip2==0)&&(ip3==0)) || (ip=="223.255.255.0") ){
				return window.parent.showMessageDlg("@CMC.title.tip@",
						"@syslog.ipError4@", "tip", function() {
							$("#serverIPInput").focus();
						});
			}
		}else{
			return window.parent.showMessageDlg("@CMC.title.tip@",
					"@syslog.ipError5@", "tip", function() {
						$("#serverIPInput").focus();
					});
		}
		return true;
	}
	
	//添加trapServer
	function addServerClick(){
		var trapServerIP = $.trim($('#serverIPInput').val());
		//进行IP地址校验
		if(ipCheck()){
			//最多只能添加5个
			if (dataStore.getCount() < 5) {
				var isRepeat = false;
				dataStore.each(function(rec){
					if(rec.data.topCcmtsTrapServerIpAddr == trapServerIP){
						isRepeat = true;
						return false;
					}
				});
				if(isRepeat){
					window.parent.showMessageDlg("@COMMON.tip@", "@cmc.trapServerRepeat@",null,function(){
						$("#serverIPInput").focus();
					});
					return;
				}
				//获得使用过的index
				var indexArray = new Array;
				dataStore.each(function(record){
					if(record.data.topCcmtsTrapServerIndex != null){
						indexArray.push(record.data.topCcmtsTrapServerIndex);
					}
				});
				window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@",'ext-mb-waiting');
				$.ajax({
					url : '/cmc/addTrapServer.tv?r=' + Math.random(),
					type : 'POST',
					data : {
						entityId : entityId,
						trapServerIp : trapServerIP,
						usedIndex: indexArray
					},
					dataType : 'json',
					success : function(json) {
						if (json.success) {
							window.parent.closeWaitingDlg();
							//window.parent.showMessageDlg("@COMMON.tip@", "@cmc.addTrapServerSuccess@");
							top.afterSaveOrDelete({
   								title: '@COMMON.tip@',
   								html: '<b class="orangeTxt">@cmc.addTrapServerSuccess@</b>'
   							});
							dataStore.reload();
						} else {
							window.parent.showMessageDlg("@COMMON.tip@","@cmc.addTrapServerFailed@", null,function() {
								$('#serverIPInput').focus();
							});
							return;
						}
					},
					error : function(json) {
						window.top.showErrorDlg();
					},
					cache : false
				});
			} else {
				window.parent.showMessageDlg("@COMMON.tip@", "@cmc.trapServerLimit@");
				return;
			}
		}
	}
	
	function ipRenderer(v) {
        return top.IpUtil.convertToDisplayFormat(v);
    }

	//show the delete picture
	var manuRenderer = function(value, cellmeta, record) {
	    if(operationDevicePower){
			return "<a href='javascript:;' onclick='deleteTrapServer()' >@COMMON.delete@</a>";
	    }else{
	        return "@COMMON.delete@";
	    }
	}

	//删除trapServer
	function deleteTrapServer() {
		var select = trapServerGrid.getSelectionModel().getSelected();
		//var trapServerIP = select.data.topCcmtsTrapServerIpAddr;
		var trapServerIndex = select.data.topCcmtsTrapServerIndex;
		window.parent.showConfirmDlg("@COMMON.tip@", "@cmc.trapServerDelete@", function(type) {
			if (type == 'no') {
				return;
			}
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@",'ext-mb-waiting');
			//实现后台的删除操作
			$.ajax({
				url : '/cmc/deleteTrapServer.tv?r=' + Math.random(),
				type : 'POST',
				data : {
					entityId : entityId,
					//trapServerIp : trapServerIP,
					trapServerIndex:trapServerIndex
				},
				dataType : 'json',
				success : function(json) {
					if(json.success){
						//window.parent.showMessageDlg("@COMMON.tip@", "@cmc.delTrapServerSuccess@");
						window.parent.closeWaitingDlg();
						top.afterSaveOrDelete({
   								title: '@COMMON.tip@',
   								html: '<b class="orangeTxt">@cmc.delTrapServerSuccess@</b>'
   						});
						dataStore.remove(select);
					}else{
						window.parent.showMessageDlg("@COMMON.tip@","@cmc.delTrapServerFailed@");
						return;
					}
				},
				error : function(json) {
					window.top.showErrorDlg();
				},
				cache : false
			});
		});
	}

	function refreshTrapServer() {
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@", 'ext-mb-waiting');
		$.ajax({
			url : '/cmc/refreshTrapServerFromFacility.tv?r=' + Math.random(),
			data : {
				entityId : entityId
			},
			dataType : 'json',
			success : function(json) {
				if (json.success) {
					//window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchOk@");
					window.parent.closeWaitingDlg();
					top.afterSaveOrDelete({
							title: '@COMMON.tip@',
							html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
					});
					window.location.href=window.location.href;
				} else {
					window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
				}
			},
			error : function() {
				window.top.showErrorDlg();
			}
		});
	}
	
	//close the dialog
	function cancelClick() {
		window.parent.closeWindow('trapServerConfig');
	}
	
	

	Ext.onReady(function() {
		window.dataStore = new Ext.data.JsonStore({
			url : '/cmc/loadTrapServer.tv',
			baseParams : {
				entityId : entityId
			},
			fields :  [
					   {name: 'topCcmtsTrapServerIpAddr'},
					   {name: 'topCcmtsTrapServerIndex'}
					  ]
		});
		//load data from proxy
		dataStore.load();

		window.colModel = new Ext.grid.ColumnModel([ {
			header : "@cmc.trapServerIpAddress@",
			width : 310,
			align : 'center',
			sortable : true,
			dataIndex : 'topCcmtsTrapServerIpAddr',
			renderer: ipRenderer
		}, {
			header : "@COMMON.manu@",
			align : 'center',
			sortable : false,
			dataIndex : 'id',
			renderer : manuRenderer
		} ]);

		window.trapServerGrid = new Ext.grid.GridPanel({
			id : 'trapServerGrid',
			renderTo : 'configGrid',
			height : 220,
			viewConfig:{
				forceFit:true
			},
			store : dataStore,
			colModel : colModel,
			sm : new Ext.grid.RowSelectionModel({
				singleSelect : true
			})
		});
		
		//操作权限控制
		if(!operationDevicePower){
		    $("#serverIPInput").attr("disabled",true);
		    $("#addServer").attr("disabled",true);
		}
	    if(!refreshDevicePower){
	        $("#refreshBtn").attr("disabled",true);
	    }
	});
</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">@cmc.trapServerIpToolTip@</div>
	    <div class="rightCirIco wheelCirIco"></div>
	</div>
	
	<div class="edge10 pT20">
		<ul id="addTrapServer" class="leftFloatUl">
			<li class="blueTxt pT3">
				<label for="serverIPInput">@cmc.trapServerIpAddress@:</label>
			</li>
			<li>
				<input class="normalInput" style="width: 200px;" id="serverIPInput" tooltip="@cmc.trapServerIpToolTip@" />
			</li>
			<li>
				<a id="addServer" onclick="addServerClick()" href="javascript:;" class="normalBtn"><span><i class="miniIcoAdd"></i>@BUTTON.add@</span></a>
			</li>
		</ul>
		<div id="configGrid" class="normalTable pT10 clearBoth"></div>
		<div id="buttonPanel" class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT40 noWidthCenter">
		         <li><a id="refreshBtn" onclick="refreshTrapServer();" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
		         <li><a onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
		     </ol>
		</div>
	</div>
</body>
</Zeta:HTML>