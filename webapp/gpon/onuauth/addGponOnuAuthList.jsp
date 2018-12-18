<%-- <%@ page language="java" contentType="text/html;charset=UTF-8"%>
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
    import js/nm3k/nm3kPickDate
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
	var lineComboBox, srvComboBox, baseStore;
	
	function closeClick() {
		window.parent.closeWindow('addGponOnuAuth');
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

	function renderGponAuthMode(value, p, record) {
		if (value == 1) {
			return "@OnuAuth.snAuth@";
		} else if (value == 2) {
			return "@OnuAuth.snPassAuth@";
		} else if (value == 3) {
			return "@OnuAuth.loidAuth@";
		} else if (value == 4) {
			return "@OnuAuth.loidPassAuth@";
		} else if (value == 5) {
			return "@OnuAuth.passAuth@";
		} else if (value == 6) {
			return "@OnuAuth.autoAuth@";
		} else if (value == 7) {
			return "@OnuAuth.mixAuth@";
		}
	}

	$(function() {
		lineComboBox = new Ext.form.ComboBox({
			emptyText: '@COMMON.select@',
			mode: 'remote',
			width:204,
			editable:false,
			triggerAction: 'all',
			valueField : 'gponLineProfileId',
			displayField : 'gponLineProfileName',//gponLineProfileName
			applyTo: 'lineProfileId',
			store : new Ext.data.JsonStore({   
			    url:"/gpon/profile/loadLineProfileList.tv?entityId=@{entityId}@",
			    autoLoad:true,
			    fields: ["gponLineProfileId", {name:"gponLineProfileName",convert:function(v,r){return v+"("+r.gponLineProfileId+")";}}]
			})
		 });
		 
		srvComboBox = new Ext.form.ComboBox({
			emptyText: '@COMMON.select@',
			mode: 'remote',
			width:204,
			cls : 'test',
			editable:false,
			triggerAction: 'all',
			valueField : 'gponSrvProfileId',
			displayField : 'gponSrvProfileName',
			applyTo: 'srvProfileId',
			store : new Ext.data.JsonStore({   
				url:"/gpon/profile/loadServiceProfileList.tv?entityId=@{entityId}@",
				autoLoad:true,
				fields: ["gponSrvProfileId",{name:"gponSrvProfileName",convert:function(v,r){return v+"("+r.gponSrvProfileId+")";}}]
			})
		 });
		
		var w = $(window).width() - 30;
		var baseColumns = [ 
		{
			header : "Id",
			width : 60,
			align : 'center',
			dataIndex : 'num'
		}, {
			header : "@EPON/ONU.belongOlt@",
			width : 100,
			align : 'center',
			dataIndex : 'entityName',
			renderer:function(value,m,record){
				return '<a href="#" onclick="showEntityDetail(\''+record.data.entityId+'\',\''+value+'\');">'+value+'</a>';
		}, {
			header : "SN",
			width : 100,
			align : 'center',
			dataIndex : 'serialNumber',
			renderer : renderAuthMode
		}, {
			header : '@COMMON.password@',
			width : 100,
			align : 'center',
			dataIndex : 'password',
			hidden: true
		}, {
			header : 'LOID',
			width : 100,
			align : 'center',
			dataIndex : 'loid',
			hidden: true
		}, {
			header : 'LOID@COMMON.password@',
			width : 100,
			align : 'center',
			dataIndex : 'loidPassword',
			hidden: true
		}, {
			header : "@onuAuth.authMode@",
			width : 60,
			align : 'center',
			dataIndex : 'ponOnuAuthenMode',
			renderer : renderGponAuthMode
		}];

		baseStore = new Ext.data.JsonStore({
			url : ('/gpon/onuauth/getGponOnuAutoFindByIds.tv'),
			root : 'data',
			remoteSort : true,
			fields : [ 'recordId', 'entityId', 'onuIndex', 'ponIndex', 'entityName',
					'autoFindTime', 'loid', 'serialNumber', 'password', 'loidPassword',
					'softwareVersion', 'hardwareVersion, ponOnuAuthenMode' ]
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
		/* buildEntityTypeSelect(); */
	})

</script>
	</head>
	<body class="openWinBody">
		<div id="w850">
			<div id="w1700">
				<div id="step0">
					<div id="topPart" class="edge10">
						<table cellspacing="0" cellpadding="0" border="0" class="topSearchTable">
							<tr class="darkZebraTr">
								<td class="rightBlueTxt">@COMMON.required@@OnuAuth.lineProfile@@COMMON.maohao@</td>
								<td>
									<div style="width:154px;">
										<input type="text" id="lineProfileId" readonly="readonly" class="normalInput w150"  maxlength="4"/>
									</div>
								</td>
								<td class="rightBlueTxt">@COMMON.required@@OnuAuth.srvProfile@@COMMON.maohao@</td>
								<td>
									<div style="width:194px;">
										<input type="text" id="srvProfileId" readonly="readonly" class="normalInput w190"  maxlength="4" />
									</div>
								</td>
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
</Zeta:HTML> --%>
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
    module platform
    module gpon
</Zeta:Loader>
<style type="text/css">
    #w800{ width:790px; overflow:hidden; position: relative; top:0; left:0; height:440px;}
    #w1600{ width:1600px; position:absolute; top:0; left:0;}
    #step0, #step1{width:800px; overflow:hidden; position:absolute; top:0px; left:0px;}
    #step1{ left:800px;}
    .mainWindow{ height:380px;}
</style>
<script type="text/javascript">
//上一页;
function prevPage(){
    $("#w1600").animate({left:0},"fast");
}
//下一页;
function nextPage(){
    $("#w1600").animate({left:-800},"fast");
}
//取消;
function cancelClick() {
    window.top.closeWindow("modalDlg");
}
</script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip">
	       <p><b class="orangeTxt">@GPON.chooseTemplate@</b></p>
	       <p><span id="newMsg">@GPON.chooseBusAndLineTemplate@</span></p>
	    </div>
	    <div class="rightCirIco pageCirIco">
	    </div>
	</div>
    <div id="w800">
        <div id="w1600">
            <div id="step0">
                <div class="noWidthCenterOuter clearBoth">
                    <div class="mainWindow">
                        Step1 的内容
                    </div>
                    <ol class="upChannelListOl pB0 pT20 noWidthCenter">
                        <li>
                            <a onclick="nextPage()" href="javascript:;" class="normalBtnBig">
                                <span><i class="miniIcoArrRight"></i>@COMMON.next@</span>
                            </a>
                        </li>
                        <li>
                            <a onclick="cancelClick()" href="javascript:;" class="normalBtnBig">
                                <span><i class="miniIcoForbid"></i>@COMMON.cancel@</span>
                            </a>
                        </li>
                    </ol>
                </div>
            </div>
            <div id="step1">
                <div class="noWidthCenterOuter clearBoth">
                    <div class="mainWindow">
                        Step2 的内容
                    </div>
                    <ol class="upChannelListOl pB0 pT20 noWidthCenter">
                        <li>
                            <a onclick="prevPage()" href="javascript:;" class="normalBtnBig">
                                <span><i class="miniIcoArrLeft"></i>@COMMON.prev@</span>
                            </a>
                        </li>
                        <li>
                            <a onclick="" href="javascript:;" class="normalBtnBig">
                                <span><i class="miniIcoData"></i>@COMMON.save@</span>
                            </a>
                        </li>
                        <li>
                            <a onclick="cancelClick()" href="javascript:;" class="normalBtnBig">
                                <span><i class="miniIcoForbid"></i>@COMMON.cancel@</span>
                            </a>
                        </li>
                    </ol>
                </div>
            </div>
        </div>
    </div>
</body>
</Zeta:HTML>