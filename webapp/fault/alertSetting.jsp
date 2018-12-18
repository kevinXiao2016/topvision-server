<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    import js/customColumnModel
    module fault
    css css/white/disabledStyle
</Zeta:Loader>

<style type="text/css">
.td30Table td {
	height: 30px;
}

.alertFolderIcon {
	background-image: url(../images/fault/alertFolder.gif) !important;
	valign: middle;
	display: inline;
}

.alertLeafIcon {
	background-image: url(../images/fault/alertLeaf.gif) !important;
	valign: middle;
	display: inline;
}

.level1Icon {
	background-image: url(../images/fault/level1.gif) !important;
	valign: middle;
	display: inline;
}

.level2Icon {
	background-image: url(../images/fault/level2.gif) !important;
	valign: middle;
	display: inline;
}

.level3Icon {
	background-image: url(../images/fault/level3.gif) !important;
	valign: middle;
	display: inline;
}

.level4Icon {
	background-image: url(../images/fault/level4.gif) !important;
	valign: middle;
	display: inline;
}

.level5Icon {
	background-image: url(../images/fault/level5.gif) !important;
	valign: middle;
	display: inline;
}

.level6Icon {
	background-image: url(../images/fault/level6.gif) !important;
	valign: middle;
	display: inline;
}

#buttonDiv {
	position: absolute;
	width: 166px;
	height: 29px;
	left: 564px;
	top: 433px;
}

#actionDiv {
	position: absolute;
	width: 105px;
	height: 56px;
	left: 200px;
	top: 75px;
}

#actionDiv1 {
	position: absolute;
	width: 105px;
	height: 56px;
	left: 200px;
	top: 245px;
}

#panalSegment {
	position: absolute;
	left: 5px;
	top: 5px;
	width: 470px;
	height: 380px;
}

#availbleListField {
	position: absolute;
	left: 0px;
	top: 5px;
	width: 188px;
	height: 285px;
	margin-top: 0px;
}

#readyListField {
	position: absolute;
	left: 295px;
	top: 5px;
	width: 188px;
	height: 155px;
	margin-top: 0px;
}

#createdAlertGridField {
	position: absolute;
	left: 295px;
	top: 185px;
	width: 195px;
	height: 120px;
	margin-top: 0px;
}

.alertActionChoose {
	width: 100%;
	height: 100%;
	overflow: hidden;
	position: absolute;
	top: 0;
	left: 0;
	display: none;
}

.alertActionChooseMain {
	width: 560px;
	height: 280px;
	overflow: hidden;
	position: absolute;
	top: 100px;
	left: 120px;
	z-index: 11;
	background: #F7F7F7;
}

.alertActionChooseBg {
	width: 100%;
	height: 100%;
	overflow: hidden;
	background: #F7F7F7;
	position: absolute;
	top: 0;
	left: 0;
	z-index: 10;
	opacity: 0.8;
	filter: alpha(opacity = 85);
}

#chooseEmail {
	position: relative;
	top: 4px;
}

#chooseMobile {
	position: relative;
	top: 2px;
}

.chooseTrap {
	position: relative;
	top: 2px;
}

#emailWord {
	position: relative;
	left: 2px;
	top: 1px;
}

#mobileWord {
	position: relative;
	left: 2px;
}

#judgeEmail {
	position: relative;
	top: 1px;
}

#judgeMobile {
	position: relative;
	top: 1px;
}

fieldset {
	background-color: #ffffff;
}

#vailblePortListContainer {
	position: absolute;
}

#readyPortListContainer {
	position: absolute;
	width: 100%;
	height: 118px;
}

#createdAlertGridContainer {
	position: absolute;
	width: 97%;
	height: 88%;
}
/*
**
**  由于问题单[EMS-8569]故障管理告警策略管理清除策略无法显示内容
**  simpleStore.load的时候，发现对display:none的gridPanel没有作用。因此：
**  通过修改x-hide-display样式，将原本display:none的div改为block,但是显示在left:900px,的位置,导致你看不到
**
*/
#tabs .x-tab-panel-body {
	position: relative;
	top: 0px;
	left: 0px;
}

#basicMsg, #executor, #clearPly, #alertacn {
	position: absolute;
	top: 0px;
	left: 0px;
}

#basicMsg.x-hide-display {
	display: block;
	display: block !important;
	left: 800px;
}

#executor.x-hide-display {
	display: block;
	display: block !important;
	left: 800px;
}

#clearPly.x-hide-display {
	display: block;
	display: block !important;
	left: 800px;
}

#alertacn.x-hide-display {
	display: block;
	display: block !important;
	left: 800px;
}
</style>
<link rel="stylesheet" type="text/css" href="../css/jquery.treeview.css"></link>
<script type="text/javascript" src="../js/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="TipTextArea.js"></script>
<script type="text/javascript" src="../js/tools/treeBuilder.js"></script>
<script type="text/javascript"
	src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.fault.resources&prefix=&lang=<%=uc.getUser().getLanguage()%>"></script>
<script type="text/javascript">
	var emailEditorPower =<%=uc.hasPower("mailManagement")%>;
	var SMSEditorPower =<%=uc.hasPower("smsManagement")%>;
	Ext.BLANK_IMAGE_URL = '../images/s.gif';
	var userEmail = '${user.email }';
	var userMobile = '${user.mobile }';
	var userId = '${uc.userId}';
	var alertTypeLoader = null;
	var alertTypeRoot = null;
	var alertTypeId = 0;
	var tabs;
	var tree;
	var checkFlag = 0;
	var activeNum = '<s:property value = "activeNum" />';
	var columns = null;
	var store = null;
	var cm = null;
	var grid = null;
	var columns = null;
	var clearStore = null;
	var cm = null;
	var clearGrid = null;
	var selectedAlertTypeId;
	var alertActioncol = null;
	var alertActiontoolBar = null;
	var alertActionStore = null;
	var alertActiongrid = null;
	var alertActionsm = null;
	var emailFlag = 0;
	var SMSFlag = 0;
	var pageSize = 7;
	var confirmAlertTypeList, ///** 清除事件CACHE
	createAlertTypeList, ///事件来源CACHE
	heap ///可用的告警堆

	if (activeNum == "" || activeNum == null || activeNum == undefined) {
		activeNum = 0;
	} else {
		activeNum = parseInt(activeNum);
	}

	/**
	 * initialize page
	 */
	function initAlertUpdate() {
		if (Zeta$('smartUpdate').checked == false) {
			Zeta$('alarmTimes').style.display = 'none';
			Zeta$('updateLevel').style.display = 'none';
			Zeta$('alertCount').style.display = 'none';
			Zeta$('alertLevel').style.display = 'none';
		} else if (Zeta$('smartUpdate').checked == true) {
			Zeta$('alarmTimes').style.display = 'block';
			Zeta$('updateLevel').style.display = 'block';
			Zeta$('alertCount').style.display = 'block';
			Zeta$('alertLevel').style.display = 'block';
		}
	}
	/* var liFormatStr = '<li><span class="{0}"><a href="javascript:;" onclick="{1}" class="linkBtn" id="{2}">{3}</a></span></li>'; */

	function chooseRootAlert() {
		R.saveBt.setDisabled(true);
		Zeta$('alarmDesc').disabled = true;
		Zeta$('activeAlarm').disabled = true;
		Zeta$('level2').disabled = true;
	}

	function chooseLeafAlert(typeId) {
		R.saveBt.setDisabled(false);
		Zeta$('alarmDesc').disabled = false;
		Zeta$('activeAlarm').disabled = false;
		Zeta$('level2').disabled = false;
		$('#chooseEmail').attr('checked', false);
		$('#chooseMobile').attr('checked', false);

		alertTypeId = typeId;
		selectedAlertTypeId = typeId;
		try {
			initEventMessage(alertTypeId)
			initAllEventType()
			//initCreateAlertTypeList(alertTypeId)
			clearStore.loadData(confirmAlertTypeList)
			createAlertStore.loadData(createAlertTypeList)
		} catch (e) {
		}
		/////////////////////////////////////////////////////////////////////////////////////////////////////////
		$.ajax({
			url : '/fault/getAlertTypeOfUsersById.tv',
			type : 'GET',
			data : {
				alertTypeId : alertTypeId,
				userId : userId
			},
			success : function(json) {
				$("#name").text(json.name);
				Zeta$('alarmDesc').value = json.alarmDesc;
				Zeta$('level').selectedIndex = json.level - 1;
				Zeta$('activeAlarm').checked = json.active ? true : false;
				Zeta$('smartUpdate').checked = json.smartUpdate ? true : false;
				initAlertUpdate();
				checkFlag = json.smartUpdate ? 1 : 0;
				Zeta$('alarmTimes').value = json.alarmTimes ? json.alarmTimes
						: '';
				Zeta$('updateLevel').value = json.updateLevel;
				var elems = document.getElementsByName("actions");
		        for (var j = 0; j < elems.length; j++) {
		            elems[j].checked = false;
		        }
		        for (var i = 0; i < json.actions.length; i++) {
		            Zeta$('action' + json.actions[i].actionId).checked = true;
		        }
				//如果已经选择过这条告警类型的告警动作，显示出来
				if(json.actionChoose.indexOf("1")!=-1){
					$('#chooseEmail').attr('checked', true);
				}else{
					$('#chooseEmail').attr('checked', false);
				}
				if(json.actionChoose.indexOf("2")!=-1){
					$('#chooseMobile').attr('checked', true);
				}else{
                    $('#chooseMobile').attr('checked', false);
                }

				// 				alertActiongrid.getSelectionModel().clearSelections();
				// 				var count = alertActiongrid.getStore().getCount();
				// 				var array = new Array();
				// 				for (var i = 0; i < count; i++) {
				// 					for (var j = 0; j < json.actions.length; j++) {
				// 						var record = alertActiongrid.getStore().getAt(i);
				// 						if (json.actions[j].userId == record.get("userId")) {
				// 							array[j] = i;
				// 						}
				// 					}
				// 				}
				// 				alertActiongrid.getSelectionModel().selectRows(array);//如果有选择了此类告警的用户，显示打钩
				//如果是阈值告警，屏蔽级别设置和告警提升策略表单
				if (json.category == -50001 || json.category == -50002) {
					$("#tr_alertLevel").hide();
					$("#tr_alertUpPolicy").hide();
				} else {
					$("#tr_alertLevel").show();
					$("#tr_alertUpPolicy").show();
				}
			},
			error : function() {
				window.top.showErrorDlg();
			},
			dataType : 'json',
			cache : false
		});

	}
	//构造告警类型树结构
	function showAlertType() {
		judgeClick = false;
		//清空树
		$('ul#tree').empty();
		$.ajax({
			url : '../fault/loadAlertType.tv',
			type : 'POST',
			dataType : "json",
			async : 'false',
			success : function(jsonAlertType) {
				//循环输出各父节点
				$.each(jsonAlertType, function(index, rootAlert) {
					bulidTree(rootAlert, '');
				});
				//绑定事件
				$('a.linkBtn').each(function() {
					//首先清除绑定
					$(this).unbind();
					if ($(this).parent().attr('class') == 'folder') {
						$(this).bind('click', function() {
							chooseRootAlert();
						});
					} else {
						$(this).bind('click', function() {
							chooseLeafAlert($(this).attr("id"));
						});
					}
				});
				treeBasicHandle();
			},
			error : function(array) {
			},
			cache : false,
			complete : function(XHR, TS) {
				XHR = null
			}
		});
	}

	/**
	 * build alert tree
	 */
	function buildEntityTree() {
		var w = $(window).width() - 270;
		var h = $(window).height() - 60;
		if (w < 300) {
			w = 300;
		}
		if (h < 100) {
			h = 100;
		}

		//构建告警类型树
		showAlertType();
		/* alertTypeLoader = new Ext.tree.TreeLoader({dataUrl:'../fault/loadAlertType.tv'});
		tree = new Ext.tree.TreePanel({
		    el: 'alertTypeTree', useArrows: useArrows, animate: animCollapse, trackMouseOver: trackMouseOver,
		    border: true, autoScroll: true, lines: true, rootVisible: false, enableDD: false,
		    height : h, width: 230, padding:5,
		    loader: alertTypeLoader
		});
		alertTypeRoot = new Ext.tree.AsyncTreeNode({text: 'Alert Typer Tree', draggable:false, id: "alertTypeRootNode"});
		tree.setRootNode(alertTypeRoot);
		tree.render();
		alertTypeRoot.expand();
		
		tree.on("click", function (n) {
			if (n.attributes.superiorId == 0) { //root node
				Zeta$('saveBt').disabled = true;
				Zeta$('alarmDesc').disabled = true;
				Zeta$('activeAlarm').disabled = true;
				Zeta$('level2').disabled = true;
				return;
			}
		}); */
		tabs = new Ext.TabPanel({
			renderTo : 'tabs',
			width : w,
			height : h,
			activeTab : activeNum,
			frame : true,
			defaults : {
				autoHeight : true
			},
			listeners : {
				'tabchange' : function(o, p) {
					if ('clearPly' == p.getId()) {
						if (!grid) {
							bulidGrids()
						}
					}
				}
			},
			items : [ {
				id : 'basicMsg',
				contentEl : 'infoTab',
				title : "@RESOURCES/SYSTEM.baseInfo@"
			},
			// 			{
			// 				id : 'alertacn',
			// 				contentEl : 'alertTab',
			// 				title : "@ALERT.alertAction@"
			// 			}, 
			{
				id : 'executor',
				contentEl : 'actionTab',
				title : "@ALERT.doAction@"
			}, {
				id : 'clearPly',
				contentEl : 'clearTab',
				title : "@ALERT.clearPolicy@"
			} ]
		});
	}

	function saveClick() {
		var l = Zeta$('level').selectedIndex + 1;
		var arr = [];
		var elems = document.getElementsByName("actions");
		// 		var userId = new Array();
		for (var j = 0; j < elems.length; j++) {
			if (elems[j].checked) {
				arr[arr.length] = elems[j].value;
			}
		}
		var confirmAlertTypeString = '';
		for (var i = 0; i < confirmAlertTypeList.length; i++) {
			confirmAlertTypeString += confirmAlertTypeList[i][0] + ',';
		}

		var originEventTypeString = '';
		for (var i = 0; i < createAlertTypeList.length; i++) {
			originEventTypeString += createAlertTypeList[i][0] + ',';
		}
		var userAlertActionChooseEmail = false;
		var userAlertActionChooseMobile = false;
		if ($('#chooseEmail').attr("checked")
				&& !$('#chooseMobile').attr("checked")) {
			userAlertActionChooseEmail = true;
			userAlertActionChooseMobile = false;//邮件
		} else if (!$('#chooseEmail').attr("checked")
				&& $('#chooseMobile').attr("checked")) {
			userAlertActionChooseEmail = false;
			userAlertActionChooseMobile = true;//短信
		} else if ($('#chooseEmail').attr("checked")
				&& $('#chooseMobile').attr("checked")) {
			userAlertActionChooseEmail = true;
			userAlertActionChooseMobile = true;//邮件和短信
		}

		if (Zeta$('smartUpdate').checked) {
			var alermCount = parseInt(Zeta$('alarmTimes').value);
			if (!checkedInput() || alermCount < 2) {
				Zeta$("alarmTimes").focus();
				return;
			}
			if (Zeta$('updateLevel').value == 0) {
				top.afterSaveOrDelete({
					title : '@COMMON.tip@',
					html : '<b class="orangeTxt">@ALERT.chooseUpLevel@</b>'
				});
				//window.parent.showMessageDlg("@COMMON.tip@", "@ALERT.chooseUpLevel@");
				return;
			}
		}
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		$
				.ajax({
					url : 'saveAlertTypeOfUsers.tv',
					type : 'POST',
					data : {
						alertTypeId : alertTypeId,
						level : l,
						active : Zeta$('activeAlarm').checked,
						note : Zeta$('alarmDesc').value,
						alarmTimes : Zeta$('alarmTimes').value,
						smartUpdate : Zeta$('smartUpdate').checked,
						updateLevel : Zeta$('updateLevel').value,
						userId : userId,
						actionIds: arr,
						userAlertActionChooseEmail : userAlertActionChooseEmail,
						userAlertActionChooseMobile : userAlertActionChooseMobile,
						confirmAlertTypeString : confirmAlertTypeString,
						originEventTypeString : originEventTypeString
					},
					success : function(json) {
						if (json == 'success') {
							top
									.afterSaveOrDelete({
										title : '@COMMON.tip@',
										html : '<b class="orangeTxt">@ALERT.saveAlertConfigSuccess@</b>'
									});
							//window.location.reload();
						} else {
							window.parent.showMessageDlg("@COMMON.tip@",
									"@ALERT.saveAlertConfigFail@");
						}
					},
					error : function() {
						window.parent.showErrorDlg();
					},
					cache : false

				})
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		// 	$.ajax({url: 'saveAlertType.tv', type: 'POST', 
		// 	data: {
		// 		alertTypeId: alertTypeId, 
		// 		level: l, 
		// 		active: Zeta$('activeAlarm').checked, 
		// 		note: Zeta$('alarmDesc').value,
		// 		alarmTimes: Zeta$('alarmTimes').value,
		// 		smartUpdate: Zeta$('smartUpdate').checked,
		// 		updateLevel: Zeta$('updateLevel').value,
		// 		actionIds: arr,
		// 		confirmAlertTypeString : confirmAlertTypeString,
		// 		originEventTypeString : originEventTypeString
		// 	},
		// 	success: function(json) {
		// 		if(json == 'success'){
		// 			top.afterSaveOrDelete({
		// 		      title: '@COMMON.tip@',
		// 		      html: '<b class="orangeTxt">@ALERT.saveAlertConfigSuccess@</b>'
		// 		    });
		// 			window.location.reload();
		// 			//alertTypeLoader.load(alertTypeRoot)
		// 		}else{
		// 			window.parent.showMessageDlg("@COMMON.tip@", "@ALERT.saveAlertConfigFail@");
		// 		}
		// 	}, error: function() {
		// 		window.parent.showErrorDlg();
		// 	}, 
		// 	cache: false});
	}

	function addAlertType() {
	}

	function deleteAlertType() {
	}
	function doOnresize() {
		var w = $(window).width() - 300;
		var h = $(window).height() - 60;
		if (w < 300) {
			w = 300;
		}
		if (h < 100) {
			h = 100;
		}

		tabs.setWidth(w);
		tabs.setHeight(h);
		//tree.setHeight(h);
	}
	function closeClick() {
		window.top.closeWindow('modalDlgAlertSet');
	}
	function initSeverityData() {
		Ext.Ajax.request({
			url : '/fault/loadAllAlertLevel.tv?r=' + Math.random(),
			success : function(response) {
				var json = Ext.decode(response.responseText);
				var levels = json[0].children;
				for (var i = levels.length - 1; i >= 0; i--) {
					$('#level').append(
							'<option value="' + levels[i].level + '">'
									+ levels[i].text + '</option>');
					$('#updateLevel').append(
							'<option value="' + levels[i].level + '">'
									+ levels[i].text + '</option>');
				}
			},
			failure : function() {
				window.parent.showMessageDlg("@COMMON.tip@",
						"@ALERT.dataLoadFail@");
			}
		});
	}

	function initAllEventType() {
		$.ajax({
			url : '/fault/getAllEventTypeByTypeId.tv',
			method : 'POST',
			cache : false,
			dataType : 'json',
			success : function(result) {
				heap = result.data
				if (store) {
					store.loadData(result)
				}
			}
		})
	}

	/***
	 * 按照一个 事件分类 加载 清除队列
	 */
	function initEventMessage(alertTypeId) {
		if (!alertTypeId) {
			alertTypeId = 0;
		}
		$.ajax({
			url : "/fault/getRelationAlertByAlertTypeId.tv?alertTypeId="
					+ alertTypeId,
			dataType : "json",
			cache : false,
			async : false,
			success : function(json) {
				window.parent.closeWaitingDlg();
				confirmAlertTypeList = new Array();
				createAlertTypeList = new Array();
				/*** initialize clear quene ***/
				for (var i = 0; i < json.clear.length; i++) {
					var typeId = json.clear[i].typeId
					var displayName = json.clear[i].displayName
					confirmAlertTypeList[i] = new Array()
					confirmAlertTypeList[i][0] = typeId
					confirmAlertTypeList[i][1] = displayName
				}
				/* initialize create quene */
				for (var i = 0; i < json.origin.length; i++) {
					var typeId = json.origin[i].typeId
					var displayName = json.origin[i].displayName
					createAlertTypeList[i] = new Array()
					createAlertTypeList[i][0] = typeId
					createAlertTypeList[i][1] = displayName
				}
			},
			error : function() {
				window.parent.closeWaitingDlg();
				window.parent.showMessageDlg("@COMMON.tip@",
						"@ALERT.dataLoadFail@");
			}
		//END OF AJAX REQUEST
		});
	}

	/**
	 * 按照一个事件分类加载 告警来源 队列
	 * @deprecated
	 */
	function initCreateAlertTypeList(alertTypeId) {
		$.ajax({
			url : "/fault/getConfirmAlertByAlertTypeId.tv?alertTypeId="
					+ alertTypeId,
			dataType : "json",
			cache : false,
			async : false,
			success : function(json) {
				window.parent.closeWaitingDlg();
				createAlertTypeList = new Array();
				for (var i = 0; i < json.data.length; i++) {
					var typeId = json.data[i].typeId
					var displayName = json.data[i].displayName
					createAlertTypeList[i] = new Array()
					createAlertTypeList[i][0] = typeId
					createAlertTypeList[i][1] = displayName
				}
			},
			error : function() {
				window.parent.closeWaitingDlg();
				window.parent.showMessageDlg("@COMMON.tip@",
						"@ALERT.dataLoadFail@");
			}
		//END OF AJAX REQUEST
		});
	}

	Ext.onReady(function() {
		// 告警等级初始化
		initSeverityData();
		if (checkFlag == 0) {
			Zeta$('alarmTimes').style.display = 'none';
			Zeta$('updateLevel').style.display = 'none';
			Zeta$('alertCount').style.display = 'none';
			Zeta$('alertLevel').style.display = 'none';
		} else {
			Zeta$('alarmTimes').style.display = 'none';
			Zeta$('updateLevel').style.display = 'none';
			Zeta$('alertCount').style.display = 'block';
			Zeta$('alertLevel').style.display = 'block';
		}
		buildEntityTree();
	});

	function displayTooltip(proGrid) {
		var view = proGrid.getView();
		var store = proGrid.getStore();
		proGrid.tip = new Ext.ToolTip({
			boxMaxWidth : 140, 
			target : view.mainBody,
			delegate : '.x-grid3-row',
			trackMouse : true,
			renderTo : document.body,
			listeners : {
				beforeshow : function updateTipBody(tip) {
					var rowIndex = view.findRowIndex(tip.triggerElement);
					var record = store.getAt(rowIndex)
					tip.body.dom.innerHTML = record.data.displayName;
				}
			}
		});
	};

	//////////////////////////////////////////////////////////////////////////////////////////////////
	Ext.onReady(function() {
		if (userEmail == null || userEmail.length == 0) {
			userEmail = "--";
			$('#chooseEmail').attr('disabled', true);
		} else {
			$('#chooseEmail').attr('disabled', false);
		}
		if (userMobile == null || userMobile.length == 0) {
			userMobile = "--";
			$('#chooseMobile').attr('disabled', true);
		} else {
			$('#chooseMobile').attr('disabled', false);
		}
		$('#judgeEmail').html(userEmail);
		$('#judgeMobile').html(userMobile);

		alertActionsm = new Ext.grid.CheckboxSelectionModel();
		alertActioncol = [ alertActionsm, {
			header : "@ALERT.userName@",
			width : 172,
			sortable : true,
			align : 'center',
			dataIndex : "userName"
		}, {
			header : "@ALERT.acn@",
			width : 172,
			sortable : false,
			align : 'center',
			dataIndex : "choose",
			renderer : changeType
		}, {
			header : "@ALERT.edt@",
			width : 172,
			dataIndex : "editor",
			renderer : manuRender
		} ];
		alertActiontoolBar = [ {
			text : I18N.COMMON.refresh,
			iconCls : 'bmenu_refresh',
			handler : refreshServerAndList
		} ];
		alertActionStore = new Ext.data.JsonStore({
			url : '/fault/getAlertAboutUser.tv',
			root : "result",
			totalProperty : "num",
			idProperty : "userName",
			remoteSort : true,
			fields : [ "userId", "userName", "choose" ]
		});

		var config = CustomColumnModel.init('title', alertActioncol, {});
		var sortInfo = config.sortInfo || {
			field : 'userName',
			direction : 'ASC'
		};
		alertActionStore.setDefaultSort(sortInfo.field, sortInfo.direction);

		alertActiongrid = new Ext.grid.GridPanel({
			stripeRows : true,
			region : "center",
			renderTo : 'tableArea',
			bodyCssClass : "normalTable",
			width : 526,
			height : 257,
			columns : alertActioncol,
			listeners : {
				sortchange : function(alertActiongrid, sortInfo) {
					CustomColumnModel.saveSortInfo('title', sortInfo);
				}
			},
			sm : alertActionsm,
			store : alertActionStore,
			viewConfig : {
				forceFit : true
			},
			bbar : buildPagingToolBar()
		});
		alertActionStore.baseParams = {
			start : 0,
			limit : pageSize
		}
		alertActionStore.load();
		if(emailEditorPower){
			$('#checkServerEmail').html("<a href='javascript:jumpEmailServer();' class='normalBtn'><span><i class='miniIcoEdit'></i>@ALERT.setEmail@</span></a>");
		}
		if(SMSEditorPower){
			$('#checkServerMobile').html("<a href='javascript:jumpSMSServer();' class='normalBtn'><span><i class='miniIcoEdit'></i>@ALERT.setMobile@</span></a>");			
		}
	})

	function buildPagingToolBar() {
		var pagingToolbar = new Ext.PagingToolbar({
			id : 'extPagingBar',
			pageSize : pageSize,
			store : alertActionStore,
			displayInfo : true,
			items : [ "-", String.format(I18N.COMMON.displayPerPage, pageSize),
					'-' ]
		});
		return pagingToolbar;
	}
	/***
	 * 如果有权限就显示配置服务器按钮
	 */

	function jumpEmailServer() {
		window.top.createDialog('modalDlg', I18N.SYSTEM.mailServer, 800,
				500, 'param/showEmailServer.tv', null, true, true);
	}

	function jumpSMSServer() {
		window.top.createDialog('modalDlg', I18N.SYSTEM.smsServer, 600,
				370, 'param/showSmsServer.tv', null, true, true);
	}
	
	/***
	 * 刷新服务器和用户信息
	 */
	function refreshServerAndList() {
		$.ajax({
			url : "/fault/getAlertAboutUser.tv",
			success : function(map) {
				if (map.judge == 'success') {
					alertActionStore.reload();
				} else if (map.judge == 'fail') {
					window.parent.showMessageDlg(I18N.COMMON.tip,
							'@ALERT.GETFAIL@');
				}
			},
			error : function() {
			}
		});
		testServer();
	}

	function manuRender(v, m, r) {
		return String
				.format("<a href='javascript:;' onClick='chooseAction()'>@ALERT.editor@</a>");
	}

	function changeType(v, m, r) {
		var temp;
		switch (v) {
		case "Email":
			temp = "@ALERT.Eml@";
			break;
		case "Mobile":
			temp = "@ALERT.Mb@";
			break;
		case "unConfig":
			temp = "@ALERT.noConf@";
			break;
		case "Email&Mobile":
			temp = "@ALERT.emAndmb@";
			break;
		case "noAlert":
			temp = "@ALERT.noAlert@";
			break;
		}
		return temp;

	}

	/***
	 * 点击编辑按钮后的checkbox判断
	 */
	function chooseAction() {
		var sel = alertActiongrid.getSelectionModel().getSelected();
		var oneUserId = sel.get('userId');
		var isEmail;
		var isMobile;
		var alertchoose;
		$.ajax({
			url : "getOneUserActionCs.tv",
			data : {
				oneUserId : oneUserId
			},
			success : function(json) {
				if (json.judge == "success") {
					if (json.data[0].email != null && json.data[0].email != ""
							&& json.data[0].email.length > 0) {
						$('#checkEmail').attr('disabled', false);
						isEmail = true;
					} else {
						$('#checkEmail').attr('disabled', true);
						isEmail = false;
					}
					if (json.data[0].mobile != null
							&& json.data[0].mobile != ""
							&& json.data[0].mobile.length > 0) {
						$('#checkMobile').attr('disabled', false);
						isMobile = true;
					} else {
						$('#checkMobile').attr('disabled', true);
						isMobile = false;
					}

					alertchoose = alertActiongrid.getSelectionModel()
							.getSelected().get("choose");
					switch (alertchoose) {
					case "Mobile":
						$('#checkMobile').attr('checked', true);
						$('#checkEmail').attr('checked', false);
						break;
					case "Email":
						$('#checkEmail').attr('checked', true);
						$('#checkMobile').attr('checked', false);
						break;
					case "Email&Mobile":
						$('#checkEmail').attr('checked', true);
						$('#checkMobile').attr('checked', true);
						break;
					case "unConfig":
						$('#checkEmail').attr('checked', false);
						$('#checkMobile').attr('checked', false);
						break;
					case "noAlert":
						$('#checkEmail').attr('checked', false);
						$('#checkMobile').attr('checked', false);
						break;
					}
				}
			},
			error : function() {
			}
		});
		$('#alertACId').fadeIn();

	}

	function closeActionChoose() {
		$('#alertACId').fadeOut();
	}

	/***
	 * 告警方式编辑确认后的判断
	 */
	function confirmChange() {
		var isTrue = null;
		var sel = alertActiongrid.getSelectionModel().getSelected();
		var oneUserId = sel.get('userId');
		var myChoice = alertActiongrid.getSelectionModel().getSelected().get(
				'choose');
		if ($('#checkEmail').attr("checked")
				&& (!$('#checkMobile').attr("checked"))) {
			isTrue = "isEmail";
		} else if (!$('#checkEmail').attr("checked")
				&& (!$('#checkMobile').attr("checked"))) {
			if (myChoice != "unConfig") {
				isTrue = "isNone";
			} else
				isTrue = "isUnConfig";
		} else if (!$('#checkEmail').attr("checked")
				&& ($('#checkMobile').attr("checked"))) {
			isTrue = "isMobile";
		} else if ($('#checkEmail').attr("checked")
				&& ($('#checkMobile').attr("checked"))) {
			isTrue = "isBoth";
		}
		$.ajax({
			url : "modifyUserActionCs.tv",
			data : {
				isTrue : isTrue,
				oneUserId : oneUserId
			},
			success : function(json) {
				alertActionStore.reload();
			},
			error : function() {
			}
		});
		$('#alertACId').fadeOut();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 加载Heap
	 */
	function bulidGrids() {
		$.ajax({
			url : '/fault/getAllEventTypeByTypeId.tv',
			method : 'post',
			cache : false,
			dataType : 'json',
			success : function(result) {
				/** cache ths data */
				heap = result.data
				/** define heap store **/
				store = new Ext.data.JsonStore({
					data : result,
					root : 'data',
					fields : [ 'typeId', 'displayName' ]
				})
				store.setDefaultSort('typeId', 'ASC')

				columns = [ {
					id : 'readyColumn',
					header : "@ALERT.readyList@",
					width : 250,
					sortable : true,
					resizable : false,
					menuDisabled : true,
					align : 'left',
					dataIndex : 'displayName'
				} ];
				cm = new Ext.grid.ColumnModel(columns)
				grid = new Ext.grid.GridPanel({
					id : 'extGridContainerms',
					width : 180,
					height : 300,
					cm : cm,
					store : store,
					animCollapse : true,
					trackMouseOver : true,
					border : true,
					hideHeaders : true,
					autoScroll : true,
					autoExpandMax : 190,
					autoWidth : true,
					region : 'center',
					viewConfig : {
						enableRowBody : true,
						showPreview : false
					},
					renderTo : 'vailblePortListContainer',
					listeners : {
						'render' : displayTooltip
					},
					selModel : new Ext.grid.RowSelectionModel({
						singleSelect : true,
						listeners : {
							'selectionchange' : function() {
								if (grid.getSelectionModel().hasSelection())
									$("#toRight,#toRightCreate").attr(
											'disabled', false);
								else
									$("#toRight,#toRightCreate").attr(
											'disabled', true).mouseout();
								//判断移入移除操作是否可用 */
								if (clearGrid.getSelectionModel()
										.hasSelection())
									$("#toLeft,#toLeftCreate").attr('disabled',
											false);
								else
									$("#toLeft,#toLeftCreate").attr('disabled',
											true).mouseout();
							}
						}
					})
				});
			},
			error : function() {
			}
		})

		///****** initialize createAlertGrid quene ***//
		initEventMessage(0);
		///****** initialize createAlertStore quene ***//
		//initCreateAlertTypeList(0);

		columns = [ {
			id : 'clearColumn',
			header : "@ALERT.clearList@",
			width : 150,
			sortable : true,
			align : 'left',
			dataIndex : 'displayName'
		} ];
		cm = new Ext.grid.ColumnModel(columns);
		clearStore = new Ext.data.SimpleStore({
			data : confirmAlertTypeList,
			fields : [ 'typeId', 'displayName' ]
		});
		createAlertStore = new Ext.data.SimpleStore({
			data : createAlertTypeList,
			fields : [ 'typeId', 'displayName' ]
		});
		createAlertGrid = new Ext.grid.GridPanel({
			id : 'createAlertGrid',
			width : 180,
			height : 138,
			store : createAlertStore,
			cm : cm,
			animCollapse : true,
			trackMouseOver : true,
			border : true,
			hideHeaders : true,
			region : 'center',
			autoScroll : true,
			autoExpandColumn : "clearColumn",
			listeners : {
				'render' : displayTooltip,
				'rowdblclick' : function(clearGrid, rowIndex, e) {
					dropToHeap("create")
				}
			},
			viewConfig : {
				enableRowBody : true,
				showPreview : false,
				forceFit : true
			},
			renderTo : 'createdAlertGridContainer',
			selModel : new Ext.grid.RowSelectionModel({
				singleSelect : true,
				listeners : {
					'selectionchange' : function() {
						if (createAlertGrid.getSelectionModel().hasSelection())
							$("#toLeftCreate").attr('disabled', false);
						else
							$("#toLeftCreate").attr('disabled', true)
									.mouseout();
						//判断移入移除操作是否可用 */
						if (grid.getSelectionModel().hasSelection())
							$("#toRightCreate").attr('disabled', false);
						else
							$("#toRightCreate").attr('disabled', true)
									.mouseout();
					}
				}
			})
		});

		clearGrid = new Ext.grid.GridPanel({
			id : 'extGridContainer',
			width : 180,
			height : 108,
			store : clearStore,
			cm : cm,
			animCollapse : true,
			trackMouseOver : true,
			border : true,
			hideHeaders : true,
			region : 'center',
			autoScroll : true,
			autoExpandColumn : "clearColumn",
			viewConfig : {
				enableRowBody : true,
				showPreview : false,
				forceFit : true
			},
			renderTo : 'readyPortListContainer',
			selModel : new Ext.grid.RowSelectionModel({
				singleSelect : true,
				listeners : {
					'selectionchange' : function() {
						if (clearGrid.getSelectionModel().hasSelection())
							$("#toLeft").attr('disabled', false);
						else
							$("#toLeft").attr('disabled', true).mouseout();
						//判断移入移除操作是否可用 */
						if (grid.getSelectionModel().hasSelection())
							$("#toRight").attr('disabled', false);
						else
							$("#toRight").attr('disabled', true).mouseout();
					}
				}
			}),
			listeners : {
				'render' : displayTooltip,
				'rowdblclick' : function(clearGrid, rowIndex, e) {
					dropToHeap("clear")
				},
				'viewready' : function() {
					initEventMessage(selectedAlertTypeId)
					initAllEventType()
					//initCreateAlertTypeList(alertTypeId)
					clearStore.loadData(confirmAlertTypeList)
					createAlertStore.loadData(createAlertTypeList)
				}
			}
		});

		try {
			initEventMessage(alertTypeId)
			initAllEventType()
			//initCreateAlertTypeList(alertTypeId)
			clearStore.loadData(confirmAlertTypeList)
			createAlertStore.loadData(createAlertTypeList)
		} catch (e) {
		}
		/* var selectionModel = tree.getSelectionModel()
		if(selectionModel.getSelectedNode() != null && selectionModel.getSelectedNode().isLeaf () ) {
			var alertTypeId = selectionModel.getSelectedNode().attributes.typeId;
			try{
				initEventMessage(alertTypeId)
				initAllEventType()
				//initCreateAlertTypeList(alertTypeId)
				clearStore.loadData(confirmAlertTypeList)
				createAlertStore.loadData(createAlertTypeList)
			}catch(e){}

		} */

	}

	/**
	 * 把告警移除到 来源/清除  准备队列中
	 */
	function moveToReady(category) {
		var typeId, displayName
		switch (category) {
		case "create":
			try {
				typeId = grid.getSelectionModel().getSelected().data.typeId;
				displayName = grid.getSelectionModel().getSelected().data.displayName;
				moveToCreateReady(typeId, displayName);
			} catch (exp) {
				top
						.afterSaveOrDelete({
							title : '@COMMON.tip@',
							html : '<b class="orangeTxt">@ALERT.chooseClearEventType@</b>'
						});
				//window.parent.showMessageDlg("@COMMON.tip@", "@ALERT.chooseClearEventType@");
				return;
			}
			break
		case "clear":
			try {
				typeId = grid.getSelectionModel().getSelected().data.typeId;
				displayName = grid.getSelectionModel().getSelected().data.displayName;
				moveToClearReady(typeId, displayName);
			} catch (exp) {
				top
						.afterSaveOrDelete({
							title : '@COMMON.tip@',
							html : '<b class="orangeTxt">@ALERT.chooseClearEventType@</b>'
						});
				//window.parent.showMessageDlg("@COMMON.tip@", "@ALERT.chooseClearEventType@");
				return;
			}
			break
		}
		/** remove this event type from heap **/
		for (var i = 0; i < heap.length; i++) {
			if (heap[i].typeId == typeId) {
				heap.splice(i, 1)
				break
			}
		}
		/** reload the heap data **/
		store.loadData({
			data : heap
		})
	}

	/**
	 * private方法,把一个告警加入到 事件来源 准备队列中
	 */
	function moveToCreateReady(typeId, displayName) {
		createAlertTypeList.add([ typeId, displayName ])
		createAlertStore.loadData(createAlertTypeList)
	}

	/**
	 * private方法,把一个告警加入到 事件清除  准备队列中
	 */
	function moveToClearReady(typeId, displayName) {
		confirmAlertTypeList.add([ typeId, displayName ])
		clearStore.loadData(confirmAlertTypeList)
	}

	/**
	 * 把一个告警从准备队列中移除到 告警来源或者告警清除队列中
	 */
	function dropToHeap(category) {
		var typeId, displayName
		switch (category) {
		case "create":
			try {
				typeId = createAlertGrid.getSelectionModel().getSelected().data.typeId
				displayName = createAlertGrid.getSelectionModel().getSelected().data.displayName
				for (var i = 0; i < createAlertTypeList.length; i++) {
					if (createAlertTypeList[i][0] == typeId) {
						createAlertTypeList.splice(i, 1)
					}
				}
				createAlertStore.loadData(createAlertTypeList)
			} catch (exp) {
				top
						.afterSaveOrDelete({
							title : '@COMMON.tip@',
							html : '<b class="orangeTxt">@ALERT.chooseClearEventType@</b>'
						});
				//window.parent.showMessageDlg("@COMMON.tip@", "@ALERT.chooseClearEventType@");
				return;
			}
			break
		case "clear":
			try {
				typeId = clearGrid.getSelectionModel().getSelected().data.typeId
				displayName = clearGrid.getSelectionModel().getSelected().data.displayName
				for (var i = 0; i < confirmAlertTypeList.length; i++) {
					if (confirmAlertTypeList[i][0] == typeId) {
						confirmAlertTypeList.splice(i, 1)
					}
				}
				clearStore.loadData(confirmAlertTypeList)
			} catch (exp) {
				top
						.afterSaveOrDelete({
							title : '@COMMON.tip@',
							html : '<b class="orangeTxt">@ALERT.chooseClearEventType@</b>'
						});
				//window.parent.showMessageDlg("@COMMON.tip@", "@ALERT.chooseClearEventType@");
				return;
			}
			break
		}
		heap.push({
			typeId : typeId,
			displayName : displayName
		})
		store.loadData({
			data : heap
		})
	}

	function checkedInput() {
		var num = $("#alarmTimes").val()
		var reg = /^([0-9]{1,4})+$/
		if (reg.exec(num) && parseInt(num) > 0)
			return true
		else
			return false
	}
	$(function() {
		var t = new TipTextArea({
			id : "alarmDesc",
			tipsId : "tipTextArea",
			maxLength : 256
		});
		t.init();
	})
</script>
	</head>
	<body class="openWinBody" style="width: 100%; height: 100%; overflow: hidden;">
		<div class="alertActionChoose" id="alertACId">
			<div class="alertActionChooseMain">
				<div class="zebraTableCaption">
					<div class="zebraTableCaptionTitle">
						<span class="blueTxt"><label class="blueTxt">@ALERT.ActionChoose@</label></span>
					</div>
					<table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
						<tbody>
							<tr>
								<td><input type=checkbox id="checkEmail">@ALERT.byEmail@</input></td>
							</tr>
							<tr>
								<td><input type=checkbox id="checkMobile">@ALERT.byMobile@</input></td>
							</tr>

						</tbody>
					</table>
					<div class="noWidthCenterOuter clearBoth">
						<ol class="upChannelListOl pB0 pT20 noWidthCenter">
							<li><a href="javascript:;" id='confirmChange' class="normalBtnBig" onclick="confirmChange()"><span><i
										class="miniIcoEdit"></i>@Alert.confirm@</span></a></li>
							<li><a href="javascript:;" class="normalBtnBig" onclick="closeActionChoose()"><span><i
										class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
						</ol>
					</div>
				</div>
			</div>
			<div class="alertActionChooseBg"></div>
		</div>
		<div class="edge10">
			<div class=formtip id=tips style="display: none"></div>
			<table cellspacing=0 cellpadding=0 width="100%" border="0" rules="none">
				<tr>
					<td width=230px valign="top">
						<div id="alertTypeTree" class="clear-x-panel-body threeF0Bg" style="border: 1px solid #ccc;">
							<div class="putTree" id="putTree" style="height: 353px;">
								<div style="width: 100%; overflow: hidden;">
									<ul id="tree" class="filetree">
									</ul>
								</div>
							</div>
						</div>
					</td>
					<td><div style="width: 8px; height: 1px; overflow: hidden;"></div></td>
					<td valign="top">
						<div id="tabs">
							<div id="infoTab" class="x-hide-display">
								<table cellspacing="0" cellpadding="0" border="0" rules="none" class="td30Table mT10">
									<tr>
										<td width=90px style="padding-left: 5px;" class="blueTxt">@RESOURCES/COMMON.name@:</td>
										<td><div id="name" class="w300"></div></td>
									</tr>
									<tr>
										<td style="padding-left: 5px;" class="blueTxt"><label for="activeAlarm">@ALERT.activate@:</label></td>
										<td><input type=checkbox id="activeAlarm" /></td>
									</tr>
									<tr id="tr_alertLevel">
										<td width=70px style="padding-left: 5px;" class="blueTxt" height=24px>@RESOURCES/EVENT.levelHeader@:</td>
										<td height=24px>
											<div id="level2">
												<select id="level" style="width: 302px;" class="normalSel"></select>
											</div>
										</td>
									</tr>
									<tr>
										<td width=70px style="padding-left: 5px;" class="blueTxt">@RESOURCES/COMMON.description@:</td>
										<td><textarea rows=5 id="alarmDesc" style="width: 300px; overflow: auto; height: 70px;"
												class="normalInput"></textarea></td>
									</tr>
									<tr>
										<td width=70px style="padding-left: 5px;" class="blueTxt"></td>
										<td colspan=2 width=300px><div id="tipTextArea"></div></td>
									</tr>
									<tr id="tr_alertUpPolicy">
										<td style="padding-left: 5px;" class="blueTxt"><label for="updateEnable">@ALERT.alertUpPolicy@:</label></td>
										<td><input type=checkbox id="smartUpdate" onclick="initAlertUpdate()" /></td>
									</tr>
									<tr>
										<td width=70px style="padding-left: 5px;" class="blueTxt"><label id="alertCount">@ALERT.AlertNum@:</label></td>


										<td><input type=text id="alarmTimes" value="" style="width: 50px;" maxlength=4 class="normalInput"
											toolTip='@ALERT.2to9999integer@' /></td>
									</tr>
									<tr>
										<td width=70px style="padding-left: 5px;" class="blueTxt"><label id="alertLevel">@ALERT.alertLevel@:</label></td>
										<td><div id="level2">
												<select id="updateLevel" style="width: 150px;" class="normalSel">
													<option value="0">@ALERT.chooseUpLevel@</option>
												</select>
											</div></td>
									</tr>
								</table>
							</div>
							<div id="timeTab" class="x-hide-display"></div>

							<div id="alertTab" class="x-hide-display">
								<div id="buttonArea" style="height: 35px; overflow: auto; padding-left: 15px; padding-top: 10px;">
									<a id="freshbutton" href="javascript:refreshServerAndList();" class="normalBtn"><span><i
											class='miniIcoRefresh'></i>@ALERT.refresh@</span></a>
								</div>
								<dl id="alertword" class="legent r10" style="position: absolute; z-index: 2; right: 20px; top: 10px;">
								</dl>
								<div id="tableArea" style="z-index: 2;"></div>
							</div>
						</div>

						<div id="actionTab" class="x-hide-display">
							<div style="height: 300px; overflow: auto; padding-left: 15px; padding-top: 10px;">
								<table>
									<s:iterator value="actions">
										<tr>
											<td><input type=checkbox name="actions" class="chooseTrap" value="<s:property value="actionId"/>"
												id="action<s:property value="actionId"/>" /><label for="action<s:property value="actionId"/>">&nbsp;&nbsp;<s:property
														value="name" /></label> [<s:if test="actionTypeId==1">@RESOURCES/SYSTEM.email@</s:if> <s:elseif
													test="actionTypeId==2">@RESOURCES/EVENT.SMS@</s:elseif> <s:elseif test="actionTypeId==3">@RESOURCES/EVENT.snmpTrap@</s:elseif>
												<s:else>@RESOURCES/EVENT.sound@</s:else>]</td>
										</tr>
									</s:iterator>
									<tr>
										<td><input type=checkbox id="chooseEmail" /> <span id="emailWord">@ALERT.Eml@(</span><span
											id="judgeEmail"></span><span>)</span></td>
										<td id="checkServerEmail">
										</td>
									</tr>
									<tr>
										<td><input type=checkbox id="chooseMobile" /> <span id="mobileWord">@ALERT.Mb@(</span><span
											id="judgeMobile"></span><span>)</span></td>
										<td id="checkServerMobile">
										</td>
									</tr>
								</table>
							</div>
						</div>

						<div id="clearTab" class="x-hide-display">
							<div style="height: 300px; overflow: auto; padding-left: 5px; padding-top: 10px;">
								<div id='panalSegment'>
									<div id="availbleListField" style="width: 200px; height: 345px;">
										<p>
											<b class="blueTxt pL5">@ALERT.eventType@</b>
										</p>
										<table>
											<tr>
												<td>
													<div id='vailblePortListContainer'></div>
												</td>
											</tr>
										</table>
									</div>
									<div id="readyListField">
										<p>
											<b class="blueTxt pL5">@ALERT.eventSource@</b>
										</p>
										<div id='createdAlertGridContainer'></div>
									</div>
									<div id="createdAlertGridField">
										<p>
											<b class="blueTxt pL5">@ALERT.clearEvent@</b>
										</p>
										<div id='readyPortListContainer'></div>
									</div>
								</div>
								<div id="actionDiv">
									<a id="toRightCreate" href="javascript:moveToReady('create');" class="normalBtn"><span>@ALERT.moveIn@&gt;&gt;</span></a>
									<div class="clearBoth pT3">
										<a id="toLeftCreate" href="javascript:dropToHeap('create');" class="normalBtn"><span>&lt;&lt;@ALERT.moveOut@</span></a>
									</div>
								</div>

								<div id="actionDiv1">
									<a id="toRight" href="javascript:moveToReady('clear');" class="normalBtn"><span>@ALERT.moveIn@&gt;&gt;</span></a>
									<div class="clearBoth pT3">
										<a id="toLeft" href="javascript:dropToHeap('clear');" class="normalBtn"><span>&lt;&lt;@ALERT.moveOut@</span></a>
									</div>
								</div>
							</div>
						</div>
						</div>
					</td>
				</tr>

			</table>

			<Zeta:ButtonGroup>
				<Zeta:Button id="saveBt" onClick="saveClick()" icon="miniIcoData" disabled="true">@ALERT.saveConfig@</Zeta:Button>
				<Zeta:Button id="cancelBt" onClick="closeClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
			</Zeta:ButtonGroup>

		</div>
	</body>
</Zeta:HTML>
