<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@include file="/include/cssStyle.inc"%>
<link rel="stylesheet" type="text/css" href="/css/white/disabledStyle.css">
<div class="edge10">
	<div class="normalTable" id="server_grid"></div>
	<div class="pT10">
		<table class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
		     <thead>
		         <tr>
		             <th colspan="2" class="txtLeftTh" id="addServer_l"></th>
		         </tr>
		     </thead>
		     <tbody>
		         <tr>
		             <td class="" width="140"> 
		             	<span class="lable_span"></span> 
		             </td>
		             <td>
		             	<input id="insertSyslogIp" type="text" class="normalInput w400 floatLeft" /> 
		             	<a onclick="insertSyslogServer()" id="insertButton" href="javascript:;" class="nearInputBtn"><span></span></a>
		             </td>		           
		         </tr>
		     </tbody>
		</table>
		<div class="noWidthCenterOuter clearBoth">
		     <ol class="upChannelListOl pB0 pT80 noWidthCenter">
		         <li><a id="loadData" onclick="getEntityData()" href="javascript:;" class="normalBtnBig"><span></span></a></li>
		         <li><a id="cancel" href="javascript:;" class="normalBtnBig" onclick="cancleClick()"><span></span></a></li>
		     </ol>
		</div>
	</div>
</div>
<script type="text/javascript">
	var server_store = null;
	var server_grid = null;
	var entityId = entityId;
	var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
	var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
	//除去字符串前后的空格
	function trim(s) {
		s = s.replace(/^\s+/, "");
		return s.replace(/\s+$/, "");
	}

	//验证输入的IP地址是否合法，不能为广播地址，多播地址或全0
	function validate() {
		var reg;
		var ip = trim($("#insertSyslogIp").val());

		//验证IP地址格式是否合法
		reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
		if (!reg.test(ip)) {
			window.parent.showMessageDlg(I18N.CMC.title.tip,
					I18N.syslog.ipError1);
			$("#insertSyslogIp").focus();
			return false;
		}
		//验证是否为全0
		if (ip == "0.0.0.0") {
			window.parent.showMessageDlg(I18N.CMC.title.tip,
					I18N.syslog.ipError2);
			$("#insertSyslogIp").focus();
			return false;
		}
		//验证是否为多播,多播地址范围为224.0.0.0～239.255.255.255
		reg = /(\d{1,3})/;
		var ipPart1 = reg.exec(ip)[0];
		if (224 <= ipPart1 && ipPart1 < 240) {
			window.parent.showMessageDlg(I18N.CMC.title.tip,
					I18N.syslog.ipError3);
			$("#insertSyslogIp").focus();
			return false;
		}
		//验证是否为广播，广播地址的限制规则为：
        //必须为A,B,C类地址中的一个
        //屏蔽如下IP段： 191.255.0.0       192.0.0.0-192.0.0.255       223.255.255.0 
        var ip1 = ip.split(".")[0];
        var ip2 = ip.split(".")[1];
        var ip3 = ip.split(".")[2];
        if((1<=ip1 && ip1<=126) || (128<=ip1 && ip1<=223)){
            if((ip=="191.255.0.0") || ((ip1==192)&&(ip2==0)&&(ip3==0)) || (ip=="223.255.255.0") ){
                return window.parent.showMessageDlg(I18N.CMC.title.tip,
                        I18N.syslog.ipError4, "tip", function() {
                            $("#insertSyslogIp").focus();
                        });
            }
        }else{
            return window.parent.showMessageDlg(I18N.CMC.title.tip,
                    I18N.syslog.ipError5, "tip", function() {
                        $("#insertSyslogIp").focus();
                    });
        }
		return true;
	}

	function addOper(value, p, record) {
		var str;
		if(operationDevicePower){
			str = String.format("<a href='javascript:;' onclick='modifySyslogServer(\"{0}\", \"{1}\")'>"+ I18N.CMC.label.edit + "</a>  / " 
                    + "<a href='javascript:;' type='image' onclick='deleteSyslogServer({0})' >"+ I18N.CMC.label.deleteText + "</a>", 
                    record.get("topCcmtsSyslogServerIndex"), record.get("topCcmtsSyslogServerIpAddr"));
		}else{
			str = "<span class='disabledTxt'>"+ I18N.CMC.label.edit + "</span>  / " + "<span class='disabledTxt'>"+ I18N.NETWORK.deleteMenuItem + "</span>";
		}
		return str;
	}

	function getEntityData() {
		window.top.showWaitingDlg(I18N.CMC.tip.waiting, I18N.CMC.title.refreshDataFromEntity);
		$.ajax({
			url : '/cmc/getEntitySyslogServerData.tv',
			type : 'POST',
			data : {entityId : entityId},
			success : function() {
				//window.parent.closeWaitingDlg();
				window.parent.closeWaitingDlg();
				top.afterSaveOrDelete({
   					title: I18N.COMMON.tip,
   					html: '<b class="orangeTxt">' + I18N.CMC.tip.refreshDataFromEntitySuccess + '</b>'
   				});
				server_store.reload();
			},
			error : function() {
				window.parent.closeWaitingDlg();
				window.parent.showErrorDlg();
			},
			cache : false,
			complete : function(XHR, TS) {
				XHR = null
			}
		});
	}

	function cancleClick() {
		window.parent.closeWindow("syslogManagement");
	}

	//获取所有行的index
	function getAllIndex() {
		var indexs = new Array();
		var rowCount = server_grid.getStore().getCount();
		for ( var i = 0; i < rowCount; i++) {
			var record = server_grid.getStore().getAt(i);
			//获取数据中的项
			indexs[i] = record.data.topCcmtsSyslogServerIndex;
		}
		return indexs;
	}

	function insertSyslogServer() {
		if (!validate()) {return;}
		//判断该ip是否以存在于store中
		var count = server_store.getCount(), curIp = '';
		for(var i=0; i< count; i++){
			curIp = server_store.getAt(i).data.topCcmtsSyslogServerIpAddr;
			if(curIp===trim($("#insertSyslogIp").val())){
				window.parent.showMessageDlg(I18N.CMC.title.tip, I18N.syslog.serverIpExisted);
				return false;
			}
		}
		
		//获取所有行的index
		var indexs = getAllIndex();
		if (indexs.length >= 5) {
			window.parent.showMessageDlg(I18N.CMC.title.tip, I18N.syslog.serverMax5);
			return false;
		}
		var topCcmtsSyslogServerIpAddr = $("#insertSyslogIp").val();
		window.top.showWaitingDlg(I18N.CMC.tip.waiting, I18N.syslog.insertSyslogServer);
		$.ajax({
			url : '/cmc/addSyslogServer.tv',
			type : 'POST',
			data : {
				entityId : entityId,
				topCcmtsSyslogServerIpAddr : topCcmtsSyslogServerIpAddr,
				indexs : indexs.join(",")
			},
			success : function() {
				window.parent.closeWaitingDlg();
				//清空输入框的值
				$("#insertSyslogIp").val("");
				server_store.reload();
			},
			error : function() {
				window.parent.closeWaitingDlg();
				window.parent.showMessageDlg(I18N.CMC.title.tip, I18N.CMC.text.addFailure);
				server_store.reload();
			},
			cache : false,
			complete : function(XHR, TS) {
				XHR = null
			}
		});
	}

	function deleteSyslogServer(topCcmtsSyslogServerIndex) {
		window.top.showOkCancelConfirmDlg(I18N.CMC.title.tip, I18N.syslog.deleteSyslogServer, function(confirm) {
			if (confirm == "ok") {
				window.top.showWaitingDlg(I18N.CMC.tip.waiting, I18N.syslog.deleteSyslogServer);
				$.ajax({
					url : '/cmc/deleteSyslogServer.tv',
					type : 'POST',
					data : {
						entityId : entityId,
						topCcmtsSyslogServerIndex : topCcmtsSyslogServerIndex
					},
					success : function() {
						window.parent.closeWaitingDlg();
						//window.parent.showMessageDlg(I18N.CMC.title.tip,I18N.CMC.text.delsuccess);
						top.afterSaveOrDelete({
   							title: I18N.CMC.title.tip,
   							html: '<b class="orangeTxt">' + I18N.CMC.text.delsuccess + '</b>'
   						});
						server_store.reload();
					},
					error : function() {
						window.parent.closeWaitingDlg();
						window.parent.showMessageDlg(I18N.CMC.title.tip, I18N.CMC.text.delfailed);
						server_store.reload();
					},
					cache : false,
					complete : function(XHR, TS) {
						XHR = null
					}
				});
			}
		});
	}
	
	function ipRenderer(v) {
        return top.IpUtil.convertToDisplayFormat(v);
    }
	
	function modifySyslogServer(index, ip){
		//获取已经存在的syslog server ip
		var existedIps = [];
		for(var i=0; i< server_store.getCount(); i++){
			existedIps[existedIps.length] = server_store.getAt(i).data.topCcmtsSyslogServerIpAddr;
		}
		var url = '/cmc/modifyCmcSyslogServer.jsp?entityId='+ entityId + "&index=" + index + "&ip=" + ip;
		if(existedIps.length){
			url += "&existedIps=" + existedIps.join(',');
		}
        var win = window.top.createDialog('modifySyslogServer', I18N.syslog.syslogServer, 600, 370, url, null, true, true);
        win.on("close", function(){
            server_store.reload();
        });
   }

	//国际化处理
	function I18NConfig() {
		$("#addServer_l").text(I18N.syslog.addSyslogServer);
		$(".lable_span").text(I18N.syslog.ipAddress_maohao);
		$("#insertButton").find("span").html('<i class="miniIcoAdd"></i>'+ I18N.syslog.add);
		$("#loadData span").html('<i class="miniIcoEquipment"></i>' + I18N.CMC.title.refreshDataFromEntity);
		$("#cancel span").html('<i class="miniIcoWrong"></i>' + I18N.cmc.mdfDisabled);
	}

	Ext.onReady(function() {
		I18NConfig();

		var server_cm = new Ext.grid.ColumnModel([ {
			header : '',
			dataIndex : 'topCcmtsSyslogServerIndex',
			align : "center",
			hidden : true,
			resizable : false
		}, {
			header : I18N.syslog.ipAddress,
			dataIndex : 'topCcmtsSyslogServerIpAddr',
			align : "center",
			width : 448,
			resizable : false,
			renderer: ipRenderer
		}, {
			header : I18N.syslog.ipPort,
			dataIndex : 'topCcmtsSyslogServerIpPort',
			align : "center",
			hidden : true,
			resizable : false
		}, {
			header : I18N.CHANNEL.operation,
			dataIndex : 'operator',
			align : "center",
			width : 160,
			resizable : false,
			renderer : addOper
		} ]);
		server_store = new Ext.data.JsonStore(
				{
					url : 'cmc/getSyslogServerList.tv',
					baseParams : {entityId : entityId},
					root : 'data',
					totalProperty : 'cmcSyslogServerNumber',
					fields : [ 'topCcmtsSyslogServerIndex',
							'topCcmtsSyslogServerIpAddr',
							'topCcmtsSyslogServerIpPort' ]
				});
		server_store.load();

		server_grid = new Ext.grid.EditorGridPanel({
			store : server_store,
			height : 200,
			renderTo : 'server_grid',
			cm : server_cm,
			bodyCssClass: 'normalTable',
			viewConfig:{
				forceFit:true				
			},
			loadMask : true
		});

		//给输入框添加事件
		$("#insertSyslogIp").bind('focus blur click', function(event) {
			var msg = I18N.syslog.ipRule;
			if (event.type == 'focus') {
				_inputFocus(event.target.id, msg);
			}
			;
			if (event.type == 'blur') {
				_inputBlur(this);
			}
			;
			if (event.type == 'click') {
				_clearOrSetTips(this);
			}
			;
		});
		authLoad();
	});
	
	function authLoad(){
		if(!operationDevicePower){
			$("#insertSyslogIp").attr("disabled",true);
			$("#insertButton").attr("disabled",true);
		}
		
	    if(!refreshDevicePower){
	        $("#loadData").attr("disabled",true);
	    }
	}
</script>

