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
var pageSize = <%= uc.getPageSize() %>;
var entityId = <s:property value="entityId"/>;
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
var cdrListJson = null;
var json = null;
var store = null;
var grid = null;
function cancelClick(){
	window.parent.closeWindow('igmpHistory');
}
Ext.onReady(function(){
	var cm = new Ext.grid.ColumnModel([
	{id:"sequenceIndex", header: I18N.IGMP.recordId, align: 'center', dataIndex: 'sequenceIndex', width: 50,sortable:true},
	{id:"topControlledMcCdrSlotNum", header: I18N.IGMP.slotId , align: 'center', dataIndex: 'topControlledMcCdrSlotNum', width: 50,sortable:true},
	{id:"topControlledMcCdrPonNum", header: I18N.IGMP.ponId,align: 'center', dataIndex: 'topControlledMcCdrPonNum', width: 60,sortable:true},
   	{id:"topControlledMcCdrOnuAuthNum", header: I18N.IGMP.onuId, align: 'center',dataIndex: 'topControlledMcCdrOnuAuthNum', width: 50,sortable:true},
   	{id:"topControlledMcCdrUniNum", header: I18N.IGMP.uniId,align: 'center', dataIndex: 'topControlledMcCdrUniNum', width: 70,sortable:true},
   	{id:"cdrIgmpReqTypeString", header: I18N.IGMP.reqMvlanType, align: 'center',dataIndex: 'cdrIgmpReqTypeString', sortable:true},
   	{id:"cdrIgmpReqTimeString", header: I18N.IGMP.reqTime, align: 'center',dataIndex: 'cdrIgmpReqTimeString', sortable:true},
	{id:"cdrIgmpReqChannel", header: I18N.IGMP.reqChannel,align: 'center', dataIndex: 'cdrIgmpReqChannel', width: 60,sortable:true},
	{id:"cdrIgmpReqRightString", header: I18N.IGMP.reqAuthority, align: 'center',dataIndex: 'cdrIgmpReqRightString', width: 60,sortable:true},
	{id:"cdrIgmpReqResultString", header: I18N.IGMP.reqResult, align: 'center',dataIndex: 'cdrIgmpReqResultString', width: 60,sortable:true},
	{id:"cdrLeaveTypeString", header: I18N.IGMP.leavePattern, align: 'center',dataIndex: 'cdrLeaveTypeString', width: 60,sortable:true},
	{id:"cdrRecordTimeString", header: I18N.IGMP.recordTime, align: 'center',dataIndex: 'cdrRecordTimeString', width: 60,sortable:true}
]);
    cm.defaultSortable = true;	
    store = new Ext.data.JsonStore({
        url: '/epon/igmp/loadCdrList.tv?entityId=' + entityId +'&num=' + Math.random(),
        root: 'data',    
        totalProperty: 'totalProperty',       
        remoteSort: false,
        fields: [
                 {name:'sequenceIndex'},{name:'topControlledMcCdrSlotNum'},{name:'topControlledMcCdrPonNum'},
                 {name:'topControlledMcCdrOnuAuthNum'},{name:'topControlledMcCdrUniNum'},{name:'cdrIgmpReqTypeString'},
                 {name:'cdrIgmpReqTimeString'},{name:'cdrIgmpReqChannel'},{name:'cdrIgmpReqRightString'},
                 {name:'cdrIgmpReqResultString'},{name:'cdrLeaveTypeString'},{name:'cdrRecordTimeString'}
              ]
   }); 
    store.load({params:{start:0, limit: pageSize}});
	 grid = new Ext.grid.GridPanel({
	     stripeRows:true,
	   	  region: "center",
	   	  bodyCssClass: 'normalTable',
		  height: 410,
          store: store, 
          cm: cm,
          bbar: buildPagingToolBar(),
          viewConfig: {forceFit: true,enableRowBody: true, showPreview: false},
		  renderTo: "cdrGrid"
     });
});
function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', pageSize: pageSize,store:store,
       displayInfo: true, items: ["-", String.format(I18N.COMMON.pagingTempl, pageSize), '-']});
   return pagingToolbar;
}
function refreshClick(){
	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.IGMP.fetching)
	 $.ajax({
	        url: '/epon/igmp/refreshIgmpHistory.tv',
	        type: 'POST',
	        data: "&entityId=" + entityId + "&num=" + Math.random(),
	        dataType:"text",
	        success: function(text) {
		        if(text == 'success'){
	            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.fetchCallHistOk)
	            	window.location.reload();
		        }else{
		        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.fetchCallHistError)
			    }
	        }, error: function(text) {
	        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.fetchCallHistError);
	    }, cache: false
	    });
}
</script>
</head>
<body class=openWinBody>
	<div class="edge10">
		<div id="cdrGrid"></div>
	</div>
	<Zeta:ButtonGroup>
		<Zeta:Button onClick="refreshClick()" icon="miniIcoSaveOK">@COMMON.fetch@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()">@COMMON.close@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>