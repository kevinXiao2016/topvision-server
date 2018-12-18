<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<link rel="stylesheet" type="text/css" href="../css/jquery.treeview.css"></link>
<Zeta:Loader>
	library Jquery
	library ext
	library zeta
	module network
	import js/jquery/jquery.treeview
    import js/jquery/jquery.wresize
    import js.nm3k.menuNewTreeTip
</Zeta:Loader>
<style type="text/css">
.topoFolderIcon20 {
	background-image: url(../images/network/region.gif) !important;
	background-repeat: no-repeat;
	padding: 1px 0 1px 17px;
	valign: middle;
	display: block;
}
</style>
<script type="text/javascript">
function manuRender(value, p, record) {
	var jobId = record.data.jobId;
	str = "<a href='javascript:;' onClick='deleteJob(" + jobId +  ")'>@resources/COMMON.delete@</a> "
    return String.format(str);
}
	
$(function(){
	var w = $(window).width() - 230;
	var baseColumns = [
	           	    {header: "@batchUpgrade.jobName@",  width: 0.3 * w, align: 'center', dataIndex: 'name'},
	           	 	{header: "@batchUpgrade.upgradeVersion@",  width: 0.7 * w, align: 'center', dataIndex: 'versionName'},
	           	 	{header: "@batchtopo.operate@", width: 200, dataIndex: 'jobId', renderer : manuRender}
	           	];
	
	baseStore = new Ext.data.JsonStore({
	    url: ('/upgrade/getUpgradeJobList.tv'),
	    root: 'data',
	    totalProperty: 'rowCount',
	    fields: ['jobId', 'name', 'versionName']
	});
	
	var baseCm = new Ext.grid.ColumnModel(baseColumns);
	var h = $(window).height() - 90;
	baseGrid = new Ext.grid.GridPanel({
		id: 'extbaseGridContainer', 
		cls: 'normalTable',
		border: true, 
		height: h - 50,
		renderTo: "grid",
		store: baseStore, 
		margins: "0px 10px 10px 10px",
		cm: baseCm,
		region:'center',
		viewConfig : {
			forceFit: true,hideGroupedColumn : true, enableNoGroups : true
		}
		});
	
	baseStore.load();
	
})

function deleteJob(jobId) {
	window.parent.showConfirmDlg('@resources/COMMON.tip@','@batchUpgrade.confirmDeleteJob@', function(type) {
		if (type == 'no'){return;}
		//执行删除操作
		$.ajax({
			url: '/upgrade/deleteUpgradeJob.tv',
	    	type: 'POST',
	    	data: {jobId : jobId},
	    	dataType:"json",
	   		success: function(json) {
	   			baseStore.load();
	   			parent.frames["menuFrame"].buildUpgradeJobTree();
	   		}, error: function(json) {
	   			window.parent.showMessageDlg('@resources/COMMON.tip@', "@batchUpgrade.deleteUpgradeJobFail@");
			}, cache: false,
			complete: function (XHR, TS) { XHR = null }
		}); 
	});
}

function cancelClick() {
	window.top.closeWindow("deleteJob");
}

Ext.override(Ext.grid.CheckboxSelectionModel,{
    onMouseDown : onMonuseDown,
	onHdMouseDown : onHdMouseDown
});

function onMonuseDown(e, t){
	if(e.button === 0 && t.className == 'x-grid3-row-checker'){ // Only fire if left-click
		e.stopEvent();
		var row = e.getTarget('.x-grid3-row');

		if(!this.mouseHandled && row){
			var gridEl = this.grid.getEl();//得到表格的EL对象
			var hd = gridEl.select('div.x-grid3-hd-checker');//得到表格头部的全选CheckBox框
			var index = row.rowIndex;
			if(this.isSelected(index)){
				this.deselectRow(index);
				var isChecked = hd.hasClass('x-grid3-hd-checker-on');
				if(isChecked){
					hd.removeClass('x-grid3-hd-checker-on');
				}
			}else{
				this.selectRow(index, true);
				if(gridEl.select('div.x-grid3-row-selected').elements.length==gridEl.select('div.x-grid3-row').elements.length){
					hd.addClass('x-grid3-hd-checker-on');
				};
			}
		}
	}
	this.mouseHandled = false;
}

function onHdMouseDown(e, t){
	if(t.className.split(' ')[0] == 'x-grid3-hd-checker'){
	    e.stopEvent();
	    var hd = Ext.fly(t.parentNode);
	    var isChecked = hd.hasClass('x-grid3-hd-checker-on');
	    if(isChecked){
	        hd.removeClass('x-grid3-hd-checker-on');
	        this.clearSelections();
	    }else{
	        hd.addClass('x-grid3-hd-checker-on');
	        this.selectAll();
	    }
	}
}
</script>
</head>
<body class="openWinBody" onkeydown="">
	<div class="openWinHeader">
	    <div class="openWinTip">
	    	@batchUpgrade.deleteJobTip@
	    </div>
	    <div class="rightCirIco earthCirIco"></div>
	</div>
	<div id="grid" style="padding:5px"></div>
	<div class="noWidthCenterOuter clearBoth">
			     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
			         <li><a  onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@resources/COMMON.close@</span></a></li>
			     </ol>
			</div>
</body>
</Zeta:HTML>
