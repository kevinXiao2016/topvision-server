<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<%@include file="/include/cssStyle.inc"%>
	<head>
<Zeta:Loader>
    library Jquery
    library Ext
    library Zeta
    module  epon
    plugin DropdownTree
    import js/nm3k/nm3kPickDate
    import epon/addOnuAuthList
</Zeta:Loader>
<style type="text/css">
#w850 {
	width: 845px;
	overflow: hidden;
	position: relative;
	top: 0;
	left: 0;
	height: 450px;
}

#w1700 {
	width: 1700px;
	position: absolute;
	top: 0;
	left: 0;
}

#step0,#step1 {
	width: 850px;
	overflow: hidden;
	position: absolute;
	top: 0px;
	left: 0px;
}

#step1 {
	left: 850px;
}
</style>
<script type="text/javascript"
	src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>

<script type="text/javascript">
	var baseGrid;
	var pageSize =<%=uc.getPageSize()%>;
	var entityIds = '${entityIds}'
	var onuIndexs = '${onuIndexs}'
	var entityTypes = ${entityTypes};

	function closeClick() {
		window.parent.closeWindow('addOnuAuth');
	}

	function save() {
		window.parent.showWaitingDlg("@COMMON.wait@", '@onuAuth.adddingAuth@',
				'waitingMsg', 'ext-mb-waiting');
		var onuAuthRecord = new Array();
		//var authType = Zeta$('authType').value;
		var authType = 1;
		var authAction = Zeta$('action').value;
		var onuPreType = Zeta$('entityType').value;
		baseStore.each(function(record) {
			if(authAction == 2){
				record.data.onuId = record.data.onuId + 128;
			}
			record.data.authType = authType;
			record.data.authAction = authAction;
			record.data.onuPreType = onuPreType;
			record.data.recordId = record.id
			if(record.data.authMode > 3){
				record.set("result", '@onuAuth.authModeError@')
			}else{
				onuAuthRecord.push(record.data);
			}
				})
		$.ajax({
			url : '/epon/onuauth/addOnuAuthList.tv',
			type : 'POST',
			data : {
				addAuthListJson : Ext.encode(onuAuthRecord),
				entityIds : entityIds
			},
			dateType : 'json',
			success : function(response) {
				window.parent.closeWaitingDlg();
				var successArray = new Array();   
				var failArray = new Array();   
				successArray = response.successResult.split("&");   
				failArray = response.failResult.split("&");     
			    for (i = 0; i < successArray.length ; i++)   
			    {   
			    	if(successArray[i] != ''){
			    		var record = baseStore.getById(successArray[i])
						record.set("result", '@VLAN.batchAddSuc@')
			    	}
			    }   
			    for (i = 0; i < failArray.length ; i++)   
			    {   
			    	if(failArray[i] != ''){
			    		var record = baseStore.getById(failArray[i])
						record.set("result", '<b class="redTxt">' + '@VLAN.batchAddFailed@' + '</b>')
			    	}
			    }   
				    
				baseGrid.getColumnModel().setHidden(4, false);
				top.afterSaveOrDelete({
					title : I18N.COMMON.tip,
					html : '<b class="orangeTxt">' + '@onuAuth.addAuthComplete@' + '</b>'
				});
				$("#saveClick").hide();
				if (window.parent.getFrame("onuAuthManage") != null) {
					window.parent.getFrame("onuAuthManage").refreshOltGrid();
				}
				if (window.parent.getFrame("onuAuthFail") != null) {
					window.parent.getFrame("onuAuthFail").refreshClick();
				}
			},
			error : function() {
				top.afterSaveOrDelete({
					title : I18N.COMMON.tip,
					html : '<b class="orangeTxt">' + '@onuAuth.addAuthFail@'
							+ '</b>'
				});
			},
			cache : false
		});
	}

	$(function() {
		var w = $(window).width() - 30;
		var baseColumns = [ 
		{
			header : "Id",
			width : 60,
			align : 'center',
			dataIndex : 'num'
		}, {
			header : "MAC",
			width : 100,
			align : 'center',
			dataIndex : 'mac'
		}, {
			header : "@ONU.belongOlt@",
			width : 100,
			align : 'center',
			dataIndex : 'manageIp'
		} , /* , {
			header : "@onuAutoUpg.pro.onuLocation@",
			width : 60,
			align : 'center',
			dataIndex : 'onuIndex',
			renderer : renderIndex
		} */{
			header : "@onuAuth.authMode@",
			width : 60,
			align : 'center',
			dataIndex : 'authMode',
			renderer : renderAuthMode
		}, {
			header : '@onuAuth.authResult@',
			width : 120,
			align : 'center',
			dataIndex : 'result',
			hidden: true
		}];

		baseStore = new Ext.data.JsonStore({
			url : ('/epon/onuauth/getOnuAuthFailByIds.tv'),
			root : 'data',
			remoteSort : true,
			totalProperty : 'rowCount',
			fields : [ 'recordId', 'entityId', 'onuIndex', 'ponId', 'mac',
					'sn', 'password', 'authMode', 'authAction', 'ponIndex',
					'manageIp', 'ponIndex', 'authType', 'onuPreType', 'onuId' ]
		});

		var baseCm = new Ext.grid.ColumnModel(baseColumns);
		var h = $(window).height() - 100;
		baseGrid = new Ext.grid.GridPanel({
			id : 'extbaseGridContainer',
			cls : 'normalTable',
			border : true,
			height : h,
			renderTo : "grid",
			store : baseStore,
			margins : "0px 0px 10px 10px",
			cm : baseCm,
			region : 'center',
			viewConfig : {
				forceFit : true,
				hideGroupedColumn : true,
				enableNoGroups : true
			}
		});

		baseStore.load({
			params : {
				entityIds : entityIds,
				onuIndexs : onuIndexs,
				start : 0,
				limit : pageSize
			},
			callback : function(r){
				baseStore.each(function(r,i){
					r.set('num',i+1);
				})
			}
		});
		buildEntityTypeSelect();
	})

</script>
	</head>
	<body class="openWinBody">
		<div id="w850">
			<div id="w1700">
				<div id="step0">
					<div id="topPart" class="edge10">
						<table cellspacing="0" cellpadding="0" border="0" class="topSearchTable">
							<tr>
								<td class="rightBlueTxt w60">@onuAuth.authType@@COMMON.maohao@</td>
								<td><input type="text" id="authType" name="alias"
									value="@onuAuth.mac@" class="normalInput" style="width: 100px;" disabled/></td>
								<%-- <td><select id="authType" name="authType"
										class="normalSel" style="width: 80px;" disabled
										onchange="authTypeChange()">
											<option value="1">@onuAuth.mac@</option>
											<option value="2">@onuAuth.sn@</option>
									</select></td> --%>
								<td class="rightBlueTxt w130">@onuAuth.preType@@COMMON.maohao@</td>
									<td><select id="entityType" name="entityType"
										class="normalSel macClass" style="width: 100px;">
											<option value="0">NONE</option>
									</select></td>
								<td class="rightBlueTxt w50">@onuAuth.action@@COMMON.maohao@</td>
									<td><select id="action" name="action"
										class="normalSel" style="width: 60px;">
											<option value="1">@onuAuth.permit@</option>
											<option value="2">@onuAuth.reject@</option>
									</select></td>
							</tr>
						</table>
					</div>
					<div id="grid" style="padding: 5px"></div>
					<div class="edgeTB10LR20 pT5">
						<div class="noWidthCenterOuter clearBoth">
							<ol class="upChannelListOl pB0 p130 noWidthCenter">
								<li id="saveClick"><a onclick="save()" href="javascript:;"
									class="normalBtnBig"><span><i class="miniIcoSaveOK"></i>@onuAuth.addAuth@</span></a></li>
								<li><a onclick="closeClick()" href="javascript:;"
									class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
							</ol>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</Zeta:HTML>