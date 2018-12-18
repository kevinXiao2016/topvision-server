<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    LIBRARY ext
    LIBRARY jquery
    LIBRARY zeta
    MODULE epon
    css css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
.containerTop{ width:100%; height:400px; overflow:hidden; position:relative; border-bottom:1px solid #ccc;}
</style>
<script type="text/javascript">
var entityId = <s:property value="entityId"/>;
//1:一级弹出页面，父页面:oltfaplate；2:二级弹出页面，父页面:aclport
var aclPortJspFlag = '${aclPortJspFlag}' != "" ? parseInt('<s:property value="aclPortJspFlag"/>') : 1;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
//aclList
var showMoreFlag = 0;
var listDataSplit = new Array();	//	listData过多时，listData的一部分
var everyShowNum = 80;	//每次显示的条数
var listGrid;
var listStore;
var listData = new Array();//[[aclId,ruleNo,property,portNum,descr]]
var aclListParam = ${aclListParam};//[aclId,ruleNo,property,portNum,aclId,ruleNo,property,portNum]
aclListParam = (aclListParam.join()=="false") ? new Array() : aclListParam;
var aclListDescr = ${aclListDescr};//[descr1,descr2]
aclListDescr = (aclListDescr.join()=="false") ? new Array() : aclListDescr;
var aclFlagList = new Array();//用于记录当前页面的ACL的ID列表，添加的时候进行判断
var portLeaveNum = new Array();//用于记录ACL的绑定端口数
var ruleLeaveNum = 0;//用于记录当前选中的ACL的规则数

//rule
var ruleGrid;
var ruleStore;
var ruleData = new Array();
var ruleListParam = ${aclRuleParam};
ruleListParam = (ruleListParam.join()=="false") ? new Array() : ruleListParam;
var ruleMatchStr = [I18N.ACL.originMac, I18N.ACL.targetMac, I18N.ACL.outerVlan , I18N.ACL.innerVlan, I18N.ACL.outerPri, 
	I18N.ACL.innerCos,I18N.ACL.outerTpid , I18N.ACL.innerTpid, I18N.ACL.etherType, I18N.ACL.originIp , I18N.ACL.targetIp , 
	I18N.ACL.ipProType , I18N.ACL.ipMessageType,'DSCP','TOS', I18N.ACL.originPort , I18N.ACL.targetPort ]
//port
var portGrid;
var portStore;
var portData = new Array();//索引号是aclId  [[],[],[[aclId,portIndex,deraction],[aclId,portIndex,deraction]],[],[],[],[]]
var portListParam = ${aclListPortParam};
portListParam = (portListParam.join("")=="false") ? new Array() : portListParam;
var aclIdFlag = 0;
portData[0] = new Array();

//other
var showMoreLoadingFlag = false;

/************
 * 公共方法**
 **********/
//查看ACLport页面
function showAclPort(){
	var selectRow = portGrid.getSelectionModel().getSelected();
	if(selectRow!=null && selectRow!=undefined){
		var portIndex = selectRow.get('portIndex');
		var portLoc = getLocationByIndex(portIndex);
		var tmpDirection = selectRow.get('direction');
		window.parent.closeWindow('aclPort');
		window.parent.createDialog("aclPort",  I18N.COMMON.port + ':' + portLoc + I18N.ACL.bundAclList , 640, 450, "/epon/acl/showAclPort.tv?entityId="+entityId+  "&portIndex=" + portIndex + "&direction=" + tmpDirection + "&aclPortJspFlag=1", null, true, true);
	}
}
//通mib过Index获得num
function getNum(index, s){
    var num;
	switch (s)
	{
	case 1: num = (index & 0xFF000000) >> 24;//SLOT
 		break;
	case 2: num = (index & 0xFF0000) >> 16;//PON/SNI
 		break;
 	case 3: num = (index & 0xFF00) >> 8;//ONU
 		break;
 	case 4: num = index & 0xFF;//UNI
 		break;
 	}
 	return num;
 }
 //通过index获得location
function getLocationByIndex(index){
	var tmp = index.toString(16)
	var t = tmp.substring(0, tmp.length-4) + "" + tmp.substring(tmp.length-2, tmp.length)
 	t = parseInt(t, 16)
 	var loc = getNum(t, 1) + "/" + getNum(t, 2)
 	if(getNum(t, 3) != 0 && getNum(t, 4) != 0){
 		loc = loc + ":" + getNum(t, 3) + "/" + getNum(t, 4)
	}
	return loc
}
//冒泡阻止
function preventBubble(e) {
	e = e || window.event;
    if (e.stopPropagation) {
        e.stopPropagation()           //火狐阻止冒泡
    } else {
        e.cancelBubble = true         //IE阻止冒泡
    }
}
//查看ACLrule页面
function showAclRule(s, from){
	var selectAclRuleRow = ruleGrid.getSelectionModel().getSelected();
	var ruleId = 1;
	if(selectAclRuleRow != null && selectAclRuleRow != 'undefined'){
		ruleId = selectAclRuleRow.data.ruleId;
	}
	if(from == 1){ //from 为1表示是点击listgrid中的新建规则
		//新建时的aclIndex要从listGrid中取
		var record = listGrid.getSelectionModel().getSelected();
		aclIdFlag = record.data.aclIndex;
	}
	if(portLeaveNum[aclIdFlag]==0){
		var modifyFlag;//0:新建   1:修改     9:查看
		if(s == 0){
			modifyFlag = 0;
			window.parent.createDialog("aclRule", I18N.ACL.ruleList + ':'+'<font color=red>'+ aclIdFlag +'</font>', 1000, 500, 
					"/epon/acl/showAclRule.tv?entityId="+entityId+ "&aclIndex=" + aclIdFlag+"&modifyFlag="+modifyFlag, null, true, true);
		}else if(s == 9){
			modifyFlag = 9;
			window.parent.createDialog("aclRule", I18N.ACL.ruleList + ':'+'<font color=red>'+ aclIdFlag +'</font>', 1000, 500, 
					"/epon/acl/showAclRule.tv?entityId="+entityId+ "&aclIndex=" + aclIdFlag+"&modifyFlag="+modifyFlag+"&aclRuleIndex="+ruleId, null, true, true);
		}else if(s == 1 && selectAclRuleRow != null && selectAclRuleRow != 'undefined'){
			modifyFlag = 1;
			window.parent.createDialog("aclRule", I18N.ACL.ruleList + ':'+'<font color=red>'+ aclIdFlag +'</font>', 1000, 500, 
					"/epon/acl/showAclRule.tv?entityId="+entityId+ "&aclIndex=" + aclIdFlag +"&modifyFlag="+modifyFlag+"&aclRuleIndex="+ruleId, null, true, true);
		}
	}else{
		modifyFlag = 9;
		window.parent.createDialog("aclRule", I18N.ACL.ruleList + ':'+'<font color=red>'+ aclIdFlag +'</font>', 1000, 500, 
				"/epon/acl/showAclRule.tv?entityId="+entityId+ "&aclIndex=" + aclIdFlag+"&modifyFlag="+modifyFlag+"&aclRuleIndex="+ruleId, null, true, true);
	}
}
//获取当前ACLlist对应的RulelistParam
function getRuleParam(){
	$.ajax({
        url: '/epon/acl/loadAclRule.tv',
        data: "entityId=" + entityId + "&aclIndex=" + aclIdFlag,async:false,
        success: function(json) {
        	//ruleListParam = Ext.decode(json);
        	ruleListParam = json;
        	loadRuleData();
    		ruleStore.loadData(ruleData);
        }, error: function() {
        	window.parent.showMessageDlg(I18N.COMMON.tip,  I18N.ACL.ruleErrorRefresh)
    },cache: false
    });
}
//新建ACL
function addAclClick(aclId){
	if(aclId==null || aclId=="" || aclId==undefined || isNaN(aclId) || parseInt(aclId)>4000 || parseInt(aclId)<1){
		var tempId = $("#aclIndexSearch").val();
       	var tempText = $("button.x-btn-text","#aclIdSearch").text().substring(0,2);
       	var tempFlag = $("button.x-btn-text","#aclIdSearch").attr("disabled");
       	if(tempFlag || tempText == I18N.COMMON.query ){
       		aclId = 0;
        }else{
			aclId = tempId;
        }
	}
	window.parent.createDialog("aclAdd",  I18N.ACL.addAcl , 600, 370, "/epon/acl/showAclAdd.tv?entityId="+entityId+
			"&tempAclId="+aclId, null, true, true);
}

/**********************
 * Grid加载、数据加载**
 ********************/
//ACLlist
function loadListData(){
	var tmpL = aclListParam.length;
	for(var i=0; 4*i<tmpL; i++){
		listData[i] = new Array();
		listData[i][0] = aclListParam[4*i];
		listData[i][1] = aclListParam[4*i + 1];
		listData[i][2] = aclListParam[4*i + 2];
		listData[i][3] = aclListParam[4*i + 3];
		listData[i][4] = aclListDescr[i];
		portData[listData[i][0]] = new Array();
		aclFlagList[i] = listData[i][0];
		portLeaveNum[listData[i][0]] = 0;
	}
	if(listData[0]!=null && listData[0]!=undefined){
		aclIdFlag = listData[0][0];
	}
	if(tmpL > 1200){
		listDataSplit = listData.slice(0, everyShowNum);
	}else{
		listDataSplit = listData.slice(0);
	}
	showMoreFlag = 1;
}

function loadListGrid(){
	listStore = new Ext.data.SimpleStore({
		data : listDataSplit,
		idProperty: "aclIndex",
		fields: ['aclIndex','ruleNum','priority','portNum','descr']
	});
	listGrid = new Ext.grid.GridPanel({
		id : 'ListGrid',
		renderTo : 'listGridDiv',
		store : listStore,
		height : 318,
		frame : false,
		autoScroll : true,
		border : false,
		viewConfig :{
			forceFit:true
		},
		bodyCssClass : 'normalTable',
		selModel : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		columns: [{
				header: "ACL ID",
				dataIndex: 'aclIndex',
				align: 'center',
				sortable: showMoreFlag == 0,
				width: 40,
				renderer : function(value, cellmeta, record) {
					if(value == -1){
						return I18N.ACL.more
					}else{
						return value;
					}
				}
			},{
				header: I18N.COMMON.desc,
				dataIndex: 'descr',
				align: 'center',
				width: 40,
				hidden : true
			},{
				header: I18N.ACL.ruleNum,
				dataIndex: 'ruleNum',
				align: 'center',
				sortable: showMoreFlag == 0,
				width: 40
			},{
				header: I18N.ACL.priority,
				dataIndex: 'priority',
				align: 'center',
				sortable: showMoreFlag == 0,
				width: 40
			},{
				header: I18N.COMMON.manu,
				dataIndex: 'id',
				align: 'center',
				width: 105,
				renderer : function(value, cellmeta, record) {
					if(operationDevicePower){
						if(record.data.aclIndex == -1){
							//return "<img src='/images/moredown.gif' onclick='showMoreAcl()' title='@ACL.showMoreAcl@' />";
							return "<a href='javascript:;' onclick='showMoreAcl()'>@ACL.showMoreAcl@</a>";
						}else if(portData[parseInt(record.data.aclIndex)].length == 0){
							//return String.format("<img src='/images/edit.gif' onclick='editAcl(\"{0}\");' title='@COMMON.edit@' />   /   <img src='/images/delete.gif' onclick='deleteAclList(\"{0}\")'/>",record.data.aclIndex);
							return String.format("<a href='javascript:;' onclick='editAcl(\"{0}\")'>@COMMON.edit@</a>  / <a href='javascript:;' onclick='showAclRule(0, 1)'>@ACL.addRule@</a> /  <a href='javascript:;' onclick='deleteAclList(\"{0}\")'>@COMMON.delete@</a>",record.data.aclIndex);
						}else{
							//return "<img src='/images/noDelete.gif' title=" + I18N.ACL.delAllAclFirst + " />";
							return "--";
						}
					}else{
						if(record.data.aclIndex == -1){
							return "<img src='/images/edit.gif' title='@COMMON.edit@' /><img src='/images/moredown.gif' title="+ I18N.ACL.showMoreAcl +" />";
						}else if(portData[parseInt(record.data.aclIndex)].length == 0){
							return "<img src='/images/noDelete.gif'/>";
						}else{
							return "<img src='/images/noDelete.gif' title=" + I18N.ACL.delAllAclFirst + " />";
						}
					}
				}
			}],
		listeners: {
			'click': {
				fn : listClick,
				scope : this
			},
			'viewready': function(){
				listGrid.getSelectionModel().selectRow(0,true);
				changeAclList();
				if(aclListParam.length > 1200){
					var record = new Ext.data.Record();
					record.data = {aclIndex: -1, ruleNum: (listData.length - everyShowNum*showMoreFlag) + I18N.ACL.unit ,
									priority: 'ACL', portNum: 0, descr: showMoreFlag}
					listStore.add(record)
				}
			}
		},
		bbar: new Ext.Toolbar({
			scope : this,   
	 	    items :[  
				{
					text : "  ACL ID:",
					tooltip:'ACL ID',
					disabled:!operationDevicePower
				},{
		 	        xtype:"textfield", 
		 	        id:"aclIndexSearch",
		 	        disabled:!operationDevicePower,
		            width:60,
		            enableKeyEvents:true,
		         	listeners : {
		            	'keyup' : function(){
		            		aclIndexSearchKeyup();
			       		},
			       		'blur' : function(){
			       			aclIndexSearchKeyup();
				       	}
		       		}
	 	        },'-',{
	 	        xtype:"button",
	 	        text : I18N.ACL.queryAcl,
				id : "aclIdSearch",
				disabled:!operationDevicePower,
	 	 	    width : 70,
	 	 	   	listeners : {
	 	          	'click' : function(){
		 	          	var tempId = $("#aclIndexSearch").val();
		 	          	var tempText = $("button.x-btn-text","#aclIdSearch").text();
		 	          	if(tempId==null || tempId=="" || tempId==undefined){
		 	          		tempId = 0;
			 	        }
		 	          	var reg = /^([0-9][0-9]{0,3})+$/ig;
	            		if(!reg.exec(tempId) || parseInt(tempId)>4000 || parseInt(tempId) < 1){
							return;
		            	}
		 	          	if(tempText == I18N.ACL.addAcl){
		 	          		addAclClick(tempId);
			 	        }else if(tempText == I18N.ACL.queryAcl && tempId != 0){
			 	        	listStore.filterBy(function(record){
			 					return record.get('aclIndex') == tempId;
			 				});
			 				for(var x=0; x<listData.length; x++){
								if(listData[x][0] == tempId){
									var tmpData = new Array();
									tmpData.push(listData[x]);
									listStore.loadData(tmpData);
									break;
								}
				 			}
			 				listGrid.getSelectionModel().selectRow(0,true);
			 				listClick();
				 	    }
	 	       		}
	 	       }
	 		}]
	 	})
	});
}
function afterAddAcl(){
	$("#aclIndexSearch").val("");
	aclIndexSearchKeyup();
	setTimeout(function(){
		listGrid.getSelectionModel().selectRow(0,true);
	}, 200);
}
function showMoreAcl(){
	if(showMoreLoadingFlag){
		return;
	}
	showMoreLoadingFlag = true;
	$("#gridLoadingText").show();
	if(operationDevicePower){
		$("#addRuleBt").css("disabled", true);
	}
	setTimeout(function(){
		listStore.remove(listGrid.getSelectionModel().getSelected());
		var tmpListData = listData.slice(everyShowNum*showMoreFlag, showMoreFlag*everyShowNum*2);
		var recordList = new Array();
		for(var x=0; x<tmpListData.length; x++){
			var record = new Ext.data.Record();
			record.data = {aclIndex: tmpListData[x][0], ruleNum: tmpListData[x][1], priority: tmpListData[x][2],
							portNum: tmpListData[x][3], descr: tmpListData[x][4]};
			recordList.push(record);
			listDataSplit.push(tmpListData[x]);
		}
		listStore.add(recordList);
		showMoreFlag *= 2;
		if(listData.length - everyShowNum*showMoreFlag > 0){
			var record = new Ext.data.Record();
			record.data = {aclIndex: -1, ruleNum: (listData.length - everyShowNum*showMoreFlag) + I18N.COMMON.ge,
					priority: 'ACL', portNum: 0, descr: showMoreFlag};
			listStore.add(record);
		}
		$("#gridLoadingText").hide();
		$("#addRuleBt").css("disabled", false);
		listGrid.getSelectionModel().selectRow((everyShowNum*showMoreFlag - 2)/2,true);
		var selectRow = listGrid.getSelectionModel().getSelected();
		if(selectRow!=null && selectRow!=undefined){
			changeAclList();
		}else{
			$("#aclIndexText").html("ACL:");
			$("#aclIndexText").attr("disabled",true);
			$("#descr").val("");
			$("#descr").attr("disabled",true);
			$("#aclRuleNum").html("<font color=red></font> / 16");
			$("#aclRuleNum").attr("disabled",true);
			$("#aclPriority").val(1);
			$("#aclPriority").attr("disabled",true);
			portStore.loadData(portData[aclIndex]);
			ruleData = new Array();
			ruleStore.loadData(ruleData);
		}
		showMoreLoadingFlag = false;
	}, 20);
}
function aclIndexSearchKeyup(){
	var tempId = $("#aclIndexSearch").val();
	if(tempId.length > 4){
		tempId = tempId.substring(0,4);
		$("#aclIndexSearch").val(tempId);
	}
	$("button.x-btn-text","#aclIdSearch").attr("disabled",false);
	if(tempId!=null && tempId!="" && tempId!=undefined){
		var reg = /^([1-9][0-9]{0,3})+$/ig;
		if(!reg.exec(tempId) || parseInt(tempId)>4000){
			$("button.x-btn-text","#aclIdSearch").attr("disabled",true);
			return;
    	}
		var isSearch = false;
    	for(var k=0; k<aclFlagList.length; k++){
			if(aclFlagList[k] == parseInt(tempId)){
				isSearch = true;
				break;
			}
        }
        if(isSearch){
        	$("button.x-btn-text","#aclIdSearch").text(I18N.ACL.queryAcl)
        }else{
        	$("button.x-btn-text","#aclIdSearch").text(I18N.ACL.addAcl)
        	listStore.filterBy(function(record){
				return true;
			});
	    }
	}else{
		$("button.x-btn-text","#aclIdSearch").text(I18N.ACL.addAcl)
		if(listData.length > 300){
			listDataSplit = listData.slice(0, everyShowNum);
		}else{
			listDataSplit = listData.slice(0);
		}
		showMoreFlag = 1;
    	listStore.loadData(listDataSplit);
    	if(listData.length - everyShowNum*showMoreFlag > 0){
    		var record = new Ext.data.Record()
    		record.data = {aclIndex: -1, ruleNum: (listData.length - everyShowNum*showMoreFlag) + I18N.COMMON.ge,
    				priority: 'ACL', portNum: 0, descr: showMoreFlag}
    		listStore.add(record)
    	}
    	var nn = listStore.findBy(function(record){
			return record.data.aclIndex == aclIdFlag
        })
    	var sm = listGrid.getSelectionModel()
    	sm.selectRow(nn)
    	if(sm.selections.items.length == 0){
			sm.selectFirstRow()
        }
    }
}
function listClick(){
	var selectedRow = listGrid.getSelectionModel().getSelected();
	if(selectedRow!=null && selectedRow!=undefined){
		if(selectedRow.data.aclIndex != -1){
			aclIdFlag = parseInt(selectedRow.data.aclIndex);
			portStore.loadData(portData[aclIdFlag]);
			changeAclList();
		}else{
			showMoreAcl();
		}
	}
}
function editAcl(aclIndex){
	var record =  listGrid.getSelectionModel().getSelected();
	var $priority = record.data.priority;
	var $desc = record.data.descr;
	window.parent.createDialog("editAcl", "@ACL.modifyAcl@" , 600, 370, "/epon/acl/showAclEdit.tv?entityId="+entityId+
			"&aclIndex="+aclIndex+"&ruleLeaveNum="+ruleLeaveNum+"&priority="+$priority+"&descr=" + $desc, null, true, true);
}

function deleteAclList(aclIndex){
	window.parent.showConfirmDlg(I18N.COMMON.tip , I18N.ACL.cfmDelAclList, function(type) {
		if (type == 'no'){return;}
		var params = {
			entityId : entityId,
			aclIndex : aclIndex
		};
		var url = '/epon/acl/deletAclList.tv?r=' + Math.random();
		window.top.showWaitingDlg(I18N.COMMON.wait ,  I18N.ACL.delingAcl + aclIndex + ' ...', 'ext-mb-waiting');
		Ext.Ajax.request({
			url : url,
			success : function(response) {
				if(response.responseText != 'success'){
                    window.parent.showMessageDlg("@COMMON.tip@" , "@SERVICE.deleteError@");
					return;
				}
				top.afterSaveOrDelete({
	       	        title: "@COMMON.tip@",
	       	        html: '<b class="orangeTxt">'+ I18N.ACL.deletAcl + ':'+ aclIndex + I18N.EPON.ok + ' !' +'</b>'
	       	    });
				//window.parent.showMessageDlg(I18N.COMMON.tip , I18N.ACL.deletAcl + ':'+ aclIndex + I18N.COMMON.ok + ' !')
				var rowNum = -1;
				for(var j=0; j<listData.length; j++){
					if(listData[j][0] == aclIndex){
						rowNum = j
					}
				}
				if(rowNum == -1){
					location.reload()
				}else{
					aclFlagList.splice(rowNum, 1)
					listData.splice(rowNum, 1)
					listDataSplit.splice(rowNum, 1)
					listStore.remove(listGrid.getSelectionModel().getSelected())
					listGrid.getSelectionModel().selectRow(0,true)
					var selectRow = listGrid.getSelectionModel().getSelected()
					if(selectRow!=null && selectRow!=undefined){
						changeAclList()
					}else{
						$("#aclIndexText").html("ACL:");
						$("#aclIndexText").attr("disabled",true);
						$("#descr").val("");
						$("#descr").attr("disabled",true);
						$("#aclRuleNum").html("<font color=red></font> / 16");
						$("#aclRuleNum").attr("disabled",true);
						$("#aclPriority").val(1);
						$("#aclPriority").attr("disabled",true);
						portStore.loadData(portData[aclIndex]);
						ruleData = new Array();
						ruleStore.loadData(ruleData);
					}
				}
			},
			failure : function() {
				window.parent.showMessageDlg("@COMMON.tip@" , "@SERVICE.deleteError@");
			},
			params : params
		});
	});
}
//ACL rule
function loadRuleData(){
	ruleLeaveNum = 0;
	ruleData = new Array();
	for(var k=0; k<ruleListParam.length; k++){
		ruleData[k] = new Array();
		ruleLeaveNum++;
		ruleData[k][0] = ruleListParam[k].topAclRuleListIndex;
		ruleData[k][1] = ruleListParam[k].topAclRuleIndex;
		var tempMatchList = ruleListParam[k].topMatchedFieldSelectionSymbol;
		ruleData[k][2] = "";
		ruleData[k][3] = "";
		for(y=0; y<tempMatchList.length; y++){
			ruleData[k][2] = ruleData[k][2] + "|" + ruleMatchStr[tempMatchList[y]];
			switch (tempMatchList[y])
				{
				case 0 : ruleData[k][3] = ruleData[k][3] + "|" + I18N.ACL.originMac + ":"+ruleListParam[k].topMatchedSrcMac + I18N.ACL.originMac + "| mask:"+ruleListParam[k].topMatchedSrcMacMask;
					break;
				case 1 : ruleData[k][3] = ruleData[k][3] + "|" + I18N.ACL.targetMac + ":"+ruleListParam[k].topMatchedDstMac + I18N.ACL.targetMac + "| mask:"+ruleListParam[k].topMatchedDstMacMask;
					break;
				case 2 : ruleData[k][3] = ruleData[k][3] + "|" + I18N.ACL.outerVlan + ":" +ruleListParam[k].topMatchedStartSVid + " - " +ruleListParam[k].topMatchedEndSVid;
					break;
				case 3 : ruleData[k][3] = ruleData[k][3] + "|" + I18N.ACL.innerVlan + ":" +ruleListParam[k].topMatchedStartCVid + " - " +ruleListParam[k].topMatchedEndCVid;
					break;
				case 4 : ruleData[k][3] = ruleData[k][3] + "|" + I18N.ACL.outerVlanPrio  + ruleListParam[k].topMatchedOuterCos;
					break;
				case 5 : ruleData[k][3] = ruleData[k][3] + "|" + I18N.ACL.innerVlanPrio + ruleListParam[k].topMatchedInnerCos;
					break;
				case 6 : ruleData[k][3] = ruleData[k][3] + "|" + I18N.ACL.outerVlan + " TPID:" +ruleListParam[k].topMatchedOuterTpid;
					break;
				case 7 : ruleData[k][3] = ruleData[k][3] + "|" + I18N.ACL.innerVlan + " TPID:" +ruleListParam[k].topMatchedInnerTpid;
					break;
				case 8 : ruleData[k][3] = ruleData[k][3] + "|" + I18N.ACL.etherType + ":" +ruleListParam[k].topMatchedEthernetType;
					break;
				case 9 : ruleData[k][3] = ruleData[k][3] + "|" + I18N.ACL.originIp + ":" +ruleListParam[k].topMatchedSrcIP+ I18N.ACL.originIp + "| mask:" +ruleListParam[k].topMatchedSrcIPMask;
					break;
				case 10 : ruleData[k][3] = ruleData[k][3] + "|" + I18N.ACL.targetIp + ":" +ruleListParam[k].topMatchedDstIP+ I18N.ACL.targetIp  + "| mask:" +ruleListParam[k].topMatchedDstIPMask;
					break;
				case 11 : ruleData[k][3] = ruleData[k][3] + "|" + I18N.ACL.ipMessageFormate +ruleListParam[k].topMatchedL3ProtocolClass;//IPv4、IPv6等
					break;
				case 12 : ruleData[k][3] = ruleData[k][3] + "|" + I18N.ACL.ipMessageType + ":" +ruleListParam[k].topMatchedIpProtocol;//TCP、UDP、IGMP等
					break;
				case 13 : ruleData[k][3] = ruleData[k][3] + "|" + "DSCP:" +ruleListParam[k].topMatchedDscp;
					break;
				case 14 : ruleData[k][3] = ruleData[k][3] + "|" + "TOS:" +ruleListParam[k].topMatchedTos;
					break;
				case 15 : ruleData[k][3] = ruleData[k][3] + "|" + I18N.ACL.originPort + ":" +ruleListParam[k].topMatchedStartSrcPort+ " - " +ruleListParam[k].topMatchedEndSrcPort;
					break;
				case 16 : ruleData[k][3] = ruleData[k][3] + "|" + I18N.ACL.targetPort + ":" +ruleListParam[k].topMatchedStartDstPort+ " - " +ruleListParam[k].topMatchedEndDstPort;
					break;
				default : ruleData[k][3] = ruleData[k][3] + "";
				}
		}
		if(ruleData[k][2] != "" && ruleData[k][3] != ""){
			ruleData[k][2] = ruleData[k][2].substring(1);
			ruleData[k][3] = ruleData[k][3].substring(1);
		}else if(ruleData[k][2] == "" && ruleData[k][3] == ""){
			ruleData[k][2] = I18N.ACL.matchAll;
			ruleData[k][3] = I18N.ACL.matchAll;
		}else{
			ruleData[k][2] = I18N.ACL.errorMatch;
			ruleData[k][3] = I18N.ACL.errorMatch;
		}
		ruleData[k][4] = "";
		ruleData[k][5] = "";
		var tmpLL = ruleListParam[k].topAclActionList.length;
		for(var j=0; j<tmpLL; j++){
			switch (ruleListParam[k].topAclActionList[j])
				{
				case 0 : ruleData[k][4] = ruleData[k][4]+ "|"+ I18N.ACL.setExecMode;
					ruleData[k][5] = ruleData[k][5]+ "|"+ I18N.ACL.denyThrough;
					break;
				case 1 : ruleData[k][4] = ruleData[k][4]+ "|"+ I18N.ACL.setExecMode;
					ruleData[k][5] = ruleData[k][5]+ "|"+ I18N.ACL.allowThrough;
					break;
				case 2 : ruleData[k][4] = ruleData[k][4]+ "|"+ I18N.ACL.setInDirecRate;
					ruleData[k][5] = ruleData[k][5]+ "|"+ I18N.ACL.indirectRate + ":"+ruleListParam[k].topAclActionParameterValueList[0];
					break;
				case 3 : ruleData[k][4] = ruleData[k][4]+ "|"+ I18N.ACL.setMirrorPort;
					var mirrorSlotNo = ruleListParam[k].topAclActionParameterValueList[1];
					var mirrorPortNo = ruleListParam[k].topAclActionParameterValueList[2];
					var mirrorOnuNo = ruleListParam[k].topAclActionParameterValueList[3];
					var mirrorUniNo = ruleListParam[k].topAclActionParameterValueList[4];
					if(mirrorOnuNo==255 && mirrorUniNo==255){
						ruleData[k][5] = ruleData[k][5]+ "|"+ I18N.ACL.mirrorPort +mirrorSlotNo+"/"+mirrorPortNo;
					}else{
						ruleData[k][5] = ruleData[k][5]+ "|"+ I18N.ACL.mirrorPort +mirrorSlotNo+"/"+mirrorPortNo+":"+mirrorOnuNo+"/"+mirrorUniNo;
					}
					break;
				case 4 : ruleData[k][4] = ruleData[k][4]+ "|"+ I18N.ACL.setQuene ;
					ruleData[k][5] = ruleData[k][5]+ "|"+ I18N.ACL.quene + ":"+ruleListParam[k].topAclActionParameterValueList[5];
					break;
				case 5 : ruleData[k][4] = ruleData[k][4]+ "|"+ I18N.ACL.setVlanCos;
					ruleData[k][5] = ruleData[k][5]+ "|"+ I18N.ACL.innerCos + ":"+ruleListParam[k].topAclActionParameterValueList[6];
					break
				case 6 : ruleData[k][4] = ruleData[k][4]+ "|"+ I18N.ACL.set + "DSCP";
					ruleData[k][5] = ruleData[k][5]+ "|"+"DSCP:"+ruleListParam[k].topAclActionParameterValueList[7];
					break;
				case 7 : ruleData[k][4] = ruleData[k][4]+ "|"+ I18N.ACL.set + "TOS";
					ruleData[k][5] = ruleData[k][5]+ "|"+"TOS:"+ruleListParam[k].topAclActionParameterValueList[8];
					break;
				case 8 : ruleData[k][4] = ruleData[k][4]+ "|"+ I18N.ACL.addOuterVlan;
					ruleData[k][5] = ruleData[k][5]+ "|"+ I18N.ACL.outerVlan + ":"+ruleListParam[k].topAclActionParameterValueList[9];
					ruleData[k][5] = ruleData[k][5]+ "("+ I18N.ACL.priority +":" + ruleListParam[k].topAclActionParameterValueList[12] + ")";
					break;
				case 9 : ruleData[k][4] = ruleData[k][4]+ "|"+ I18N.ACL.setOutVlanCos;
					ruleData[k][5] = ruleData[k][5]+ "|"+ I18N.ACL.outerCos + ":"+ruleListParam[k].topAclActionParameterValueList[10];
					break;
				case 10 : ruleData[k][4] = ruleData[k][4]+ "|"+ I18N.ACL.setOutVlanTpid;
					ruleData[k][5] = ruleData[k][5]+ "|"+ I18N.ACL.outerTpid + ":"+ruleListParam[k].topAclActionParameterValueList[11];
					break;
				case 11 : ruleData[k][4] = ruleData[k][4]+ "|"+ I18N.ACL.copyToCpu;
					ruleData[k][5] = ruleData[k][5]+ "|"+ I18N.ACL.copyToCpu;
					break;
				case 12 : 
					break;
				default : ruleData[k][4] = "";
					ruleData[k][5] = "";
				}
		}
		if(ruleData[k][4]!="" && ruleData[k][5]!=""){
			ruleData[k][4] = ruleData[k][4].substring(1);
			ruleData[k][5] = ruleData[k][5].substring(1);
		}else{
			ruleData[k][4] = "--";
			ruleData[k][5] = "--";
		}
	}//END for循环
}
function loadRuleGrid(){
	ruleStore = new Ext.data.SimpleStore({
		data : ruleData,
		fields: ['aclIndex','ruleId','match','matchParam','action','actionParam']
	});
	ruleGrid = new Ext.grid.GridPanel({
		id : 'RuleGrid',
		renderTo : 'ruleGridDiv',
		store : ruleStore,
		title : '@ACL.ruleList@',
		height : 195,
		frame : false,
		autoScroll : true,
		border : true,
		viewConfig:{
			forceFit:true
		},
		cls : 'normalTable',
		/* 
		selModel : new Ext.grid.RowSelectionModel({
			singleSelect : true,
			listeners : {
				selectionchange : function(){
					if(portLeaveNum[aclIdFlag] == 0 && ruleGrid.getSelectionModel().getSelected()){
						if(operationDevicePower){
							$("#modifyBt").attr("disabled", false);
						}
					}else{
						$("#modifyBt").attr("disabled", true);
					}
				}
			} 
		}),
		 */
		columns: [{
				header: "ACL ID",
				dataIndex: 'aclIndex',
				align: 'center',
				width: 50
			},{
				header: I18N.ACL.rule + " ID",
				dataIndex: 'ruleId',
				align: 'center',
				sortable: true,
				width: 50
			},{
				header: I18N.ACL.matchManner,
				dataIndex: 'match',
				align: 'center',
				sortable: true,
				width: 70
			},{
				header: I18N.ACL.matchParam,
				dataIndex: 'matchParam',
				align: 'center',
				width: 400,
				sortable: true,
				hidden : true
			},{
				header: I18N.ACL.execAction,
				//dataIndex: 'action',
				dataIndex: 'actionParam',
				align: 'center',
				sortable: true,
				width: 170
			},{
				header: I18N.ACL.execActionParam,
				dataIndex: 'actionParam',
				align: 'center',
				width: 400,
				sortable: true,
				hidden : true
			},{
				header: I18N.COMMON.manu,
				dataIndex: 'id',
				align: 'center',
				width: 60,
				renderer : function(value, cellmeta, record) {
					if(operationDevicePower){
						if(portData[aclIdFlag].length == 0){
							return " <a href='javascript:;' onClick='showAclRule(1, 2);'>@COMMON.modify@</a> /  <a href='javaScript:;' onclick='deleteAclRule(\"" + record.data.aclIndex+ "\",\""+ 
										record.data.ruleId + "\")'>@COMMON.delete@</a>";
						}else{
							return "<a href='javascript:;' onClick='showAclRule(9, 2);'>@COMMON.view@</a>"
						}
					}else{
						if(portData[aclIdFlag].length == 0){
							return "<img src='/images/noDelete.gif'/>"
						}else{
							return "<img src='/images/noDelete.gif' title="+ I18N.ACL.manuTip +" />"
						}
					}
				}
			}]
	});
}

//不再使用的方法
function ruleContext(grid,rowIndex,e){
	grid.selModel.selectRow(rowIndex,true);
    preventBubble(e);
    e.preventDefault();
	if(portLeaveNum[aclIdFlag]==0){
		Ext.getCmp("ruleMenu").findById("ruleId1").setDisabled(false);
	}else{
		Ext.getCmp("ruleMenu").findById("ruleId1").setDisabled(true);
	}
	ruleMenu.showAt(e.getXY());
}
//不再使用的方法
var ruleMenu = new Ext.menu.Menu({
    id:'ruleMenu',
    enableScrolling: false,
    items:[{
        id:'ruleId1',
        text: I18N.ACL.mdfAclRule,
        handler : showAclRule
    },{
        id:'ruleId2',
        text: I18N.ACL.showDetail,
        handler : function(){
            showAclRule(1);
        }
    }]
});

function deleteAclRule(aclIndex,ruleId){
	window.parent.showConfirmDlg(I18N.COMMON.tip , I18N.ACL.cfmDelRule , function(type) {
		if (type == 'no'){return;}
		var params = {
			entityId : entityId,
			aclIndex : aclIndex,
			ruleNo : ruleId
		};
		var url = '/epon/acl/deletAclRule.tv?r=' + Math.random();
		window.top.showWaitingDlg(I18N.COMMON.wait , I18N.ACL.delingAcl + aclIndex + I18N.ACL.deRule + ruleId + ' ...', 'ext-mb-waiting');
		Ext.Ajax.request({
			url : url,
			success : function(response) {
				if(response.responseText != 'success'){
					if(response.responseText.split(":")[0] == "no response"){
                        window.parent.showMessageDlg(I18N.COMMON.tip, "cannot connect to device!");
                    }else{
						window.parent.showMessageDlg(I18N.COMMON.tip , "@SERVICE.deleteOk@");
                    }
					return;
				}
				window.parent.showMessageDlg(I18N.COMMON.tip , I18N.ACL.deletAcl + ':'+ aclIndex + I18N.ACL.deRule + ruleId + I18N.COMMON.ok + ' !')
				ruleLeaveNum--
				$("#aclRuleNum").html("<font color=red>"+ ruleLeaveNum +"</font> / 16")
				var rowNum = -1
				for(var j=0; j<ruleData.length; j++){
					if(ruleData[j][1] == ruleId){
						rowNum = j
					}
				}
				if(rowNum == -1){
					cancelClick()
					window.parent.createDialog("aclList", I18N.ACL.aclCfgMgmt , 700, 500, "/epon/acl/showAclList.tv?entityId="+
							entityId , null, true, true)
				}else{
					for(var i=rowNum; i<ruleData.length-1; i++){
						ruleData[i] = ruleData[i + 1]
					}
					ruleData.length = ruleData.length - 1
					ruleStore.loadData(ruleData)
				}
				var listRowNum = -1;
				for(var x=0; x<listData.length; x++){
					if(listData[x][0] == aclIndex){
						listRowNum = x
					}
				}
				if(listRowNum == -1){
					window.parent.getWindow("aclList").location.reload()
				}else{
					listData[listRowNum][1] = ruleLeaveNum
					listStore.loadData(listDataSplit)
					listGrid.getSelectionModel().selectRow(listRowNum,true)
				}
			},
			failure : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip , response.responseText)
			},
			params : params
		});
	});
}

//ACL port
function loadPortData(){
	for(var j=0; j<portListParam.length; j++){
		var tempFlag = 0;
		if(portListParam[j][0]!=null && portListParam[j][0]!=undefined){
			tempFlag = portListParam[j][0]
			portData[tempFlag] = new Array()
		}
		for(var i=0; 2*i+1<portListParam[j].length; i++){
			portData[tempFlag][i] = new Array()
			portData[tempFlag][i][0] = tempFlag
			portData[tempFlag][i][1] = portListParam[j][2*i + 1]
			portData[tempFlag][i][2] = portListParam[j][2*i + 2]
			portLeaveNum[tempFlag]++
		}
	}
}
function loadPortGrid(){
	portStore = new Ext.data.SimpleStore({
		data : portData[aclIdFlag],
		fields: ['aclIndex','portIndex','direction']
	});
	portGrid = new Ext.grid.GridPanel({
		id : 'PortGrid',
		renderTo : 'portGridDiv',
		store : portStore,
		title : '@ACL.bundAclPort@',
		height : 140,
		frame : false,
		autoScroll : true,
		border : true,
		cls : 'normalTable',
		viewConfig:{
			forceFit:true
		},
		selModel : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),
		columns: [{
				header: "ACL ID",
				dataIndex: 'aclIndex',
				align: 'center',
				sortable: true,
				width: 45
			},{
				header: I18N.COMMON.port ,
				dataIndex: 'portIndex',
				align: 'center',
				sortable: true,
				width: 55,
				renderer : function(value, cellmeta, record) {
					var portLoc = getLocationByIndex(record.data.portIndex);
					return portLoc;
				}
			},{
				header: I18N.ACL.direct ,
				dataIndex: 'direction',
				align: 'center',
				sortable: true,
				width: 45,
				renderer : function(value, cellmeta, record) {
					if(record.data.direction == 0){
						return I18N.ACL.indirect
					}else if(record.data.direction == 1){
						return I18N.ACL.outdirect
					}
				}
			},{
				header: I18N.COMMON.manu ,
				dataIndex: 'id',
				align: 'center',
				width: 35,
				renderer : function(value, cellmeta, record) {
					if(operationDevicePower){
						var manuStr = "<a href='javascript:;' onClick='showAclPort({0},{1},{2})'>@COMMON.modify@</a> / " + 
									  "<a href='javascript:;' onClick='deleteAclPort({0},{1},{2})'>@COMMON.delete@</a>";
									  
						return String.format(manuStr, record.data.aclIndex, record.data.portIndex, record.data.direction);
						/* return "<a href='javaScript:;' onclick='deleteAclPort(\"" + record.data.aclIndex + "\",\""+ 
							record.data.portIndex + "\",\"" + record.data.direction + "\")'>@COMMON.delete@</a>" */
					}else{
						return "--"
					}
				}
			}]
		/* 
		,listeners: {
			'rowcontextmenu': function(grid,rowIndex,e){
				portContext(grid,rowIndex,e)
	        },
			'click' : function(){
				if(operationDevicePower){
					if(portLeaveNum[aclIdFlag] == 0 && ruleGrid.getSelectionModel().getSelected()){
						$("#modifyBt").attr("disabled",false)
					}else{
						$("#modifyBt").attr("disabled",true)
					}
				}
			}
		} */
	});
}

//不再使用的方法
function portContext(grid,rowIndex,e){
	grid.selModel.selectRow(rowIndex,true);
    preventBubble(e);
    e.preventDefault();
	portMenu.showAt(e.getXY());
	if(portLeaveNum[aclIdFlag] == 0 && ruleGrid.getSelectionModel().getSelected()){
		$("#modifyBt").attr("disabled",false);
	}else{
		$("#modifyBt").attr("disabled",true);
	}
}
//不再使用的方法
var portMenu = new Ext.menu.Menu({
    id:'portMenu',
    enableScrolling: false,
    items:[{
        id:'id1',
        text: I18N.ACL.removeBund ,
        handler : deleteAclPort
    },{
        id:'id2',
        text: I18N.ACL.mdfBundAcl ,
        handler : showAclPort
    }]
});
function portClick(){
	var selectedRow = portGrid.getSelectionModel().getSelected();
	if(selectedRow!=null && selectedRow!=undefined){
		var aclIndexTemp = selectedRow.get('aclIndex');
	}
}
function deleteAclPort(aclIndex,portIndex,direction){
	window.parent.showConfirmDlg(I18N.COMMON.tip , I18N.ACL.cfmRemoveBund , function(type) {
		if (type == 'no'){return;}
		var params = {
			entityId : entityId,
			portIndex : portIndex,
			direction : direction,
			portAclId : aclIndex
		};
		var url = '/epon/acl/deleteAclPort.tv?r=' + Math.random();
		window.top.showWaitingDlg(I18N.COMMON.wait , I18N.ACL.removingBund , 'ext-mb-waiting');
		Ext.Ajax.request({
			url : url,
			success : function(response) {
				if(response.responseText != 'success'){
					if(response.responseText.split(":")[0] == "no response"){
                        window.parent.showMessageDlg(I18N.COMMON.tip, "cannot connect to device!");
                    }else{
						window.parent.showMessageDlg(I18N.COMMON.tip , response.responseText);
                    }
					return;
				}
				window.parent.showMessageDlg(I18N.COMMON.tip , I18N.ACL.removeBundOk )
				portLeaveNum[aclIndex]--;
				var rowNum = -1;
				for(var i=0; i<portData[aclIndex].length; i++){
					if(portData[aclIndex][i][1]==portIndex && portData[aclIndex][i][2]==direction){
						rowNum = i;
					}
				}
				for(var x=rowNum; x<portData[aclIndex].length-1; x++){
					portData[aclIndex][x] = portData[aclIndex][x + 1];
				}
				portData[aclIndex].length = portData[aclIndex].length - 1;
				portStore.loadData(portData[aclIndex]);
				if(portLeaveNum[aclIndex] == 0 || portData[aclIndex].length == 0){
					listStore.loadData(listDataSplit);
					var listRowNum = -1;
					for(var y=0; y<listData.length; y++){
						if(listData[y][0] == aclIndex){
							listRowNum = y;
						}
					}
					if(listRowNum == -1){
						cancelClick();
						window.parent.createDialog("aclList", I18N.ACL.aclCfgMgmt , 700, 500, "/epon/acl/showAclList.tv?entityId="+
								entityId , null, true, true);
					}else{
						listGrid.getSelectionModel().selectRow(listRowNum,true);
						changeAclList();
					}
				}
			},
			failure : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip , response.responseText);
			},
			params : params
		});
	});
}

/*****************************
 * onReady及界面通用逻辑处理**
 ***************************/
Ext.onReady(function(){
	loadListData();
	loadPortData();
	loadListGrid();
	loadRuleData();
	loadRuleGrid();
	loadPortGrid();
	modifyByPort();
	//$("#aclIndexSearch").focus();
});	//end of Ext.onReady

//更改ACLList右边的区域,保证要有ACLlist的行被选中
function changeAclList(){
	var selectRow = listGrid.getSelectionModel().getSelected();
	if(selectRow){
		var tmpAclIndex = selectRow.get('aclIndex');
		if(tmpAclIndex < 1){
			return;
		}
		var tmpRuleNum = selectRow.get('ruleNum');
		if(tmpRuleNum > 16){
			return;
		}
		var tmpPriority = selectRow.get('priority');
		var tmpPortNum = selectRow.get('portNum');
		var tmpDescr = selectRow.get('descr');
		ruleLeaveNum = tmpRuleNum;
		$("#aclIndexText").html("ACL:" + tmpAclIndex);
		$("#descr").val(tmpDescr);
		$("#aclRuleNum").html("<font color=red>"+ tmpRuleNum + "</font> / 16");
		$("#aclPriority").val(tmpPriority);
		aclIdFlag = tmpAclIndex;
		if(portData[aclIdFlag]!=null && portData[aclIdFlag]!=undefined){
			portStore.loadData(portData[aclIdFlag]);
		}
		modifyByPort();
		getRuleParam();
	}
}
function modifyByPort(){
	if(portLeaveNum[aclIdFlag] == 0){
		if(operationDevicePower){
			$("#aclPriority").attr("disabled",false);
			$("#descr").attr("disabled",false);
			$("#addRuleBt").attr("disabled",false);
			if(ruleGrid.getSelectionModel().getSelected()){
				$("#modifyBt").attr("disabled",false);
				return;
			}
		}
	}else{
		$("#aclPriority").attr("disabled",true);
		$("#descr").attr("disabled",true);
		$("#addRuleBt").attr("disabled",true);
		$("#saveBt").attr("disabled",true);
	}
	$("#modifyBt").attr("disabled",true);
	//操作权限控制------------------------
	var ids = new Array()
	ids.add("aclPriority");
	ids.add("descr");
	ids.add("addRuleBt");
	ids.add("addAclBt");
	//ids.add("refreshBt");
	operationAuthInit(operationDevicePower,ids);
	//-----------------------------------
}
function aclParamChange(s){
	var selectRow = listGrid.getSelectionModel().getSelected();
	if(selectRow!=null && selectRow!=undefined){
		var reg = /^([a-z._\(\)\[\]\-\s\d])+$/ig;
		$("#descr").attr("border","1px solid #8bb8f3");
		if($("#descr").val() == "" || $("#descr").val().length > 63 || !reg.exec($("#descr").val())){
			$("#descr").attr("border","1px solid #FF0000");
			$("#saveBt").attr("disabled",true);
			return;
		}
		$("#saveBt").attr("disabled",false);
		if(selectRow.get('descr')!=$("#descr").val() || selectRow.get('priority')!=$("#aclPriority").val()){
			return;
		}
	}
	$("#saveBt").attr("disabled",true);
}

function cancelClick(){
	if(aclPortJspFlag == 2 && window.parent.getWindow("aclPort")){
		var parent = window.parent.getWindow("aclPort").body.dom.firstChild.contentWindow;
		var portIndex = parent.portIndex;
		var portLoc = getLocationByIndex(portIndex);
		var direction = parent.direction;
		window.parent.closeWindow('aclPort');
		window.parent.createDialog("aclPort", I18N.COMMON.port + ':' + portLoc + I18N.ACL.bundAclList, 640, 450, "/epon/acl/showAclPort.tv?entityId="+entityId+ "&portIndex=" + portIndex + "&direction=" + direction + "&aclPortJspFlag=0", null, true, true);
	}
	window.parent.closeWindow('aclList');
}
function refreshClick(){
	window.parent.showConfirmDlg("@COMMON.tip@" , I18N.ACL.clearUnSavedInfo, function(type) {
		if (type == 'no'){return;}
		var params = {
			entityId : entityId
		};
		var url = '/epon/acl/refreshAcl.tv?r=' + Math.random();
		window.top.showWaitingDlg(I18N.COMMON.wait ,I18N.ACL.fetchingAclInfo, 'ext-mb-waiting');
		Ext.Ajax.request({
			url : url,
			timeout : 600000, 
			success : function(response) {
				if(response.responseText != 'success'){
					window.parent.showMessageDlg("@COMMON.tip@", "@ACL.fetchAclInfoError@");
					return
				}
				top.afterSaveOrDelete({
	       	       title: "@COMMON.tip@",
	       	       html: '<b class="orangeTxt">@ACL.fetchAclInfo@</b>'
	       	    });
				//window.parent.showMessageDlg("@COMMON.tip@", I18N.ACL.fetchAclInfo)
				window.location.reload();
			},
			failure : function() {
				window.parent.showMessageDlg("@COMMON.tip@", "@ACL.fetchAclInfoError@");
			},
			params : params
		});
	});
}

function authLoad(){
    if(!refreshDevicePower){
        $("#refreshBt").attr("disabled",true);
    }
}

</script>
</head>
<body class="openWinBody" onload="authLoad()">
	<div class="containerTop">
		<div id="leftPart" class="left-LR threeF0Bg" style="width:300px;">
			<p class="pannelTit" id="sidePartTit">@ACL.aclList@</p>
			<div id="listGridDiv" style="border-bottom:1px solid #ccc;"></div>
			<div class="noWidthCenterOuter clearBoth" >
			     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
			         <li><a id="addAclBt" onclick="addAclClick()" href="javascript:;" class="normalBtn"><span><i class="miniIcoAdd"></i>@ACL.addAcl@</span></a></li>
			         <%-- <li><a id=addRuleBt onclick="showAclRule(0)" href="javascript:;" class="normalBtn"><span><i class="miniIcoAdd"></i>@ACL.addRule@</span></a></li> --%>
			     </ol>
			</div>
		</div>
		<div class="line-LR" id="line" style="cursor:default; left:300px;"></div>
				
		<div class="right-LR threeFeBg" style="margin-left:308px;">
			<p class="pannelTit" id="aclIndexText">ACL:</p>
			<div class="edge10">
				<div id="portGridDiv"></div>
			</div>
			<div class="pL10 pR10">
				<div id="ruleGridDiv"></div>
				<%-- 
				<div class="noWidthCenterOuter clearBoth">
				     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
				         <li><a id=addRuleBt onclick="showAclRule(0)" href="javascript:;" class="normalBtn"><span><i class="miniIcoAdd"></i>@ACL.addRule@</span></a></li>
				         <li><a id=modifyBt onclick="showAclRule()" href="javascript:;" class="normalBtn"><span><i class="miniIcoEdit"></i>@ACL.mdfRule@</span></a></li>
				     </ol>
				</div>
				 --%>
			</div>
		</div>		 
	</div>
	
	<div class="noWidthCenterOuter clearBoth">
	     <ol id=cancelBt class="upChannelListOl pB0 pT10 noWidthCenter">
	     	 <li><a id="refreshBt" href="javascript:;" class="normalBtnBig" onclick="refreshClick()"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
	         <li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()" ><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
	     </ol>
	</div>
<div id=gridLoadingText 
	style="display:none;position:absolute;z-index:10000;left:25;top:363;width:185;height:22;background-color:#ffffbe;text-align:center;">
	@COMMON.loading@
</div>
<!-- 开始页面布局 -->

</body>
</Zeta:HTML>