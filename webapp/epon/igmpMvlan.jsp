<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module epon
</Zeta:Loader>
<script type="text/javascript">
/**************
 *变量定义区****		
 *************/
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var entityId = <s:property value="entityId"/>;
//从server传过来的json对象
//var mvlan = ${jsonstring};
var mvlan = ${cmPackageObject} ? ${cmPackageObject} : new Array();
//var activePon = ${actionPonListObject} ? ${actionPonListObject} : new Array();
var mvlanStore;
var mvlanGrid;
var mvlanDate;
var modifyFlag;
var mvlanData = new Array();

/**************
 *初始化区****		
 *************/
/**
 * 初始化初始数据
 */
function initData(){
	for(i=0;i<mvlan.length;i++){
		mvlanData[i]= new Array();
		mvlanData[i][0] = mvlan[i].cmIndex;
		mvlanData[i][1] = mvlan[i].cmName;
		mvlanData[i][2] = mvlan[i].cmChName;
		if(mvlan[i].cmProxyListNum.length == 1){
			mvlanData[i][3] = mvlan[i].cmProxyListNum[0];
		}else if(mvlan[i].cmProxyListNum.length > 1){
			for(k=0;k<mvlan[i].cmProxyListNum.length;k++){
				if(k==0){
					mvlanData[i][3] = mvlan[i].cmProxyListNum[k];
				}else{
					mvlanData[i][3] = mvlanData[i][3] + "," +mvlan[i].cmProxyListNum[k];
				}
			}
		}else if(mvlan[i].cmProxyListNum.length == 0){
			mvlanData[i][3] = "";
		}
		mvlanData[i][4] = mvlan[i].maxRequestChannelNum;
		mvlanData[i][5] = mvlan[i].multicastUserAuthority;
		mvlanData[i][6] = mvlan[i].singlePreviewTime;
		mvlanData[i][7] = mvlan[i].previewCount;
		mvlanData[i][8] = mvlan[i].totalPreviewTime;
		mvlanData[i][9] = mvlan[i].previewResetTime;
		mvlanData[i][10] = mvlan[i].previewInterval;
/* 		if(activePon[i]!=null&&activePon[i]!=""){
			for(j=0;j<activePon[i].groupPortIndexList.length;i++){
				mvlanData[i][10] = mvlanData[i][10] + IndexToString(activePon[i].groupPortIndexList[j])+" ";
			}
		} */
	}
	//mvlanData = [[1,'name1','3,4,5,6',30,1,0,0,0,0,'4/1,4/2,4/3'],[2,'name2','13,14,15,16',30,2,60,4,300,12,'4/5,4/7,4/8'],[3,'name3','7,8,9,10',30,3,0,0,0,0,'4/1,4/2,4/3']];
}
function IndexToString(idx){
	var index = idx.toString(16);
	var slot = parseInt(index.substring(0,1),16);
	var port = parseInt(index.substring(2,3),16);
	var tempString = slot+"/"+port;
	return tempString;	
}
Ext.onReady(function() {
	initData();//初始数据
	if (mvlanGrid==null || mvlanGrid==undefined){
		mvlanStore = new Ext.data.SimpleStore({
			data : mvlanData,
			fields : ['mvlanId','mvlanName','mvlanChName','mvlanProxyList','maxProxyNum','authority','singlePreviewTime','singlePreviewCount','previewTotalTime','resetPreviewTime','previewInterval']
		});
		mvlanGrid = new Ext.grid.GridPanel({
		    stripeRows:true,
	   		region: "center",
	   		bodyCssClass: 'normalTable',
			id : 'mvlanGrid',
			renderTo : 'mvlanGridPanel',
			store : mvlanStore,
			height : 410,
			frame : false,
			viewConfig:{forceFit: true},
			autoScroll : true,
			border : true,
			selModel : new Ext.grid.RowSelectionModel({
				singleSelect : true
			}),
			columns: [{
					header: I18N.IGMP.templId,
					dataIndex: 'mvlanId',
					align: 'center',
					sortable:true
				},{
					header: I18N.IGMP.templName,
					dataIndex: 'mvlanName',
					align: 'center',
					sortable:true
				},{
					header: I18N.IGMP.templAlias,
					dataIndex: 'mvlanChName',
					align: 'center',
					hidden: true,
					sortable:true
				},{
					header: I18N.IGMP.mvlanList,
					dataIndex: 'mvlanProxyList',
					align: 'center',
					sortable:true
				},{
					header: I18N.IGMP.maxChn,
					dataIndex: 'maxProxyNum',
					align: 'center',
					sortable:true
				},{
					header: I18N.COMMON.authority,
					dataIndex: 'authority',
					align: 'center',
					sortable:true,
					renderer : function(value, cellmeta, record) {
						if(value == 1){
							return "<font color=green>"+I18N.COMMON.allow+"</font>"
						}else if(value == 2){
							return "<font color=blue>"+I18N.IGMP.preview+"</font>"
						}else if(value == 3){
							return "<font color=red>"+I18N.COMMON.forbid+"</font>"
						}
					}
				},{
					header: I18N.IGMP.previewTime,
					dataIndex: 'singlePreviewTime',
					align: 'center',
					sortable:true,
					renderer : function(value){
						return value + I18N.COMMON.S
					}
				},{
					header: I18N.IGMP.previewCount,
					dataIndex: 'singlePreviewCount',
					align: 'center',
					sortable:true
				},{
					header: I18N.IGMP.previewTotalTime,
					dataIndex: 'previewTotalTime',
					align: 'center',
					sortable:true,
					renderer : function(value){
						return value + I18N.COMMON.S
					}
				},{
					header: I18N.IGMP.previewResetTick,
					dataIndex: 'resetPreviewTime',
					align: 'center',
					sortable:true,
					renderer : function(value){
						return value + I18N.COMMON.H
					}
				},{
					header: I18N.IGMP.previewInterval,
					dataIndex: 'previewInterval',
					align: 'center',
					sortable:true,
					renderer : function(value){
						return value + I18N.COMMON.S
					}
				}, {
					header: I18N.COMMON.manu,
					dataIndex: 'id',
					align: 'center',
					sortable:true,
					renderer : function(value, cellmeta, record) {
						if(operationDevicePower){
							return "<img src='/images/edit.gif' onclick='modifyIgmpMvlan();' title='@COMMON.edit@' /><img src='/images/delete.gif' onclick='deleteMvlan(\"" + record.data.mvlanId + "\")' title='@COMMON.delete@'/>";
						}else{
							return "<img src='/images/deleteDisable.gif'/>";
						}
					}
				}]
		});
		showMvlanGrid = false;
	}else{
		mvlanStore.loadData(mvlanData);
	}	
});
//冒泡阻止
function preventBubble(e) {
	e = e || window.event;
    if (e.stopPropagation) {
        e.stopPropagation();           //火狐阻止冒泡
    } else {
        e.cancelBubble = true;         //IE阻止冒泡
    }
}

function modifyIgmpMvlan() {
	modifyFlag = 1;
	var rowObject = mvlanGrid.getSelectionModel().getSelected();
	var mvlanId = rowObject.get("mvlanId");
	var mvlanName = rowObject.get("mvlanName");
	var mvlanChName = rowObject.get("mvlanChName");
	var mvlanProxyList = rowObject.get("mvlanProxyList");
	var maxProxyNum = rowObject.get("maxProxyNum");
	var authority = rowObject.get("authority");
	var singlePreviewTime = rowObject.get("singlePreviewTime");
	var previewTotalTime = rowObject.get("previewTotalTime");
	var resetPreviewTime = rowObject.get("resetPreviewTime");
	var previewCount = rowObject.get("singlePreviewCount");
	var previewInterval = rowObject.get("previewInterval");
	window.top.createDialog("addMvlan",  I18N.IGMP.mdfTempl , 630, 460,
			"/epon/igmp/addMvlanJsp.tv?entityId="+entityId+"&mvlanId="+mvlanId+"&mvlanChName="+mvlanChName+
			"&modifyFlag="+modifyFlag+"&mvlanName="+mvlanName+
			"&mvlanProxyList="+mvlanProxyList+"&maxProxyNum="+maxProxyNum+
			"&authority="+authority+"&singlePreviewTime="+singlePreviewTime+
			"&previewTotalTime="+previewTotalTime+"&resetPreviewTime="+resetPreviewTime+"&previewCount="+previewCount+"&previewInterval="+previewInterval, null, true, true);	
}

/**************
 *数据操作区****		
 *************/
function deleteMvlan(mvlanId) {
	window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.IGMP.cfmDelTempl , function(type) {
		if (type == 'no'){return;}
		window.top.showWaitingDlg(I18N.COMMON.wait, I18N.IGMP.deletingTempl, 'ext-mb-waiting');
		Ext.Ajax.request({
			url:"/epon/igmp/deleteMvlan.tv?entityId="+entityId+"&mvlanId="+mvlanId,
			method:"post",
			success:function(response){
				    if(response.responseText == "success"){
						window.parent.closeWaitingDlg();
						window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.delTemplOk)
						var selectedRow = mvlanGrid.getSelectionModel().getSelected();
						var flagNum = -1;
						for(var i=0; i<mvlanData.length; i++){
							if(mvlanData[i][0] == mvlanId){
								flagNum = i;
							}
						}
						if(flagNum > -1){
							mvlanData.splice(flagNum,1);
							mvlanStore.loadData(mvlanData);
						}
						window.location.reload();
					 }else{
						 window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.delTemplError)
					 }
			},failure:function (response) {
	            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.delTemplError)
	        }})
	})
}

function cancelClick(){
	window.parent.closeWindow('showIgmpMvlan');
}

function addClick(){
	modifyFlag = 0;
	window.top.createDialog('addMvlan', I18N.IGMP.createMvlanTempl, 630, 460, '/epon/igmp/addMvlanJsp.tv?entityId='+entityId+"&modifyFlag="+modifyFlag, null, true, true);
}
function refreshClick(){
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.IGMP.fetching);
	 $.ajax({
	        url: '/epon/igmp/refreshMvlan.tv',
	        type: 'POST',
	        data: "&entityId=" + entityId + "&num=" + Math.random(),
	        dataType:"text",
	        success: function(text) {
		        if(text == 'success'){
	            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.fetchTemplInfoOk);
	            	window.location.reload();
		        }else{
		        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.fetchTemplInfoError);
			    }
	        }, error: function(text) {
	        	window.parent.showMessageDlg(I18N.COMMON.tip,  I18N.IGMP.fetchTemplInfoError);
	    }, cache: false
	});
}

function authLoad(){
	if(!operationDevicePower){
	    R.saveBt.setDisabled(true);
	}
}
</script>
</head>
<body class=openWinBody onload="authLoad()">
	<div class="edge10">
		<div id="mvlanGridPanel"></div>
	</div>
	<Zeta:ButtonGroup>
		<Zeta:Button onClick="refreshClick()" icon="miniIcoSaveOK">@COMMON.fetch@</Zeta:Button>
		<Zeta:Button id="saveBt" onClick="addClick()">@IGMP.createTempl@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()">@COMMON.close@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>