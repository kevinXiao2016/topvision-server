<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    LIBRARY EXT
    LIBRARY JQUERY
    LIBRARY ZETA
    MODULE  CMC
</Zeta:Loader>
<script type="text/javascript"
        src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources,com.topvision.ems.cmc.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript">
var baseStore=null;
var baseGrid=null;
function onRefreshClick() {
	baseStore.reload();
}

function okBtClick(){
}

function cancelClick() {
    window.top.closeWindow('previewEntityName');
}

function renderIp(value, p, record){
	if(record.data.ipStatus == 1){
		return '<span style="color:#f00">' + record.data.ip + '---IP Error' + '</span>';
	}else if(record.data.ipStatus == 2){
		return '<span style="color:#f00">' + record.data.ip + '---IP Error' + '</span>';
	}else{
		return record.data.ip;
	}
	
}

function renderMac(value, p, record){
	if(record.data.macStatus == 1){
		return '<span style="color:#f00">' + record.data.mac + '---MAC Error' + '</span>';
	}else if(record.data.macStatus == 2){
		return '<span style="color:#f00">' + record.data.mac + '---MAC Error' + '</span>';
	} else{
		return record.data.mac;
	}
	
}

function renderName(value, p, record){
	if(record.data.nameStatus == 1){
		return '<span style="color:#f00">' + record.data.name + '---Alias Error' + '</span>';
	}else if(record.data.nameStatus == 2){
		return '<span style="color:#f00">' + record.data.name + '---Alias Error' + '</span>';
	} else{
		return record.data.name;
	}
	
}

var data = null;//所有数据
var errorData = null;//错误数据
var showData = new Array(); //显示数据
var spareData = new Array(); //剩余数据
var totalCount;
Ext.onReady(function () {
	
	$.ajax({
		url:"entity/import/getPreviewEntityName.tv",
		type:"post",
		async:false,
		success:function (response){
			data = response.data;
			errorData = response.errorData;
			totalCount = response.count;
		},error: function(response) {
		}, cache: false
	});	
	
	var w = $(window).width() - 30;
	var h = $(window).height() - 100;
	var sm = new Ext.grid.CheckboxSelectionModel(); 
	var baseColumns = [
		{header: "Num", width: parseInt(w/10), sortable: true, align: 'center', dataIndex: 'id'},
	    {header: "Alias", width: parseInt(w/4), sortable: true, align: 'center', dataIndex: 'name', renderer: renderName},
		{header: "IP",  width: parseInt(w/3), sortable: false, align: 'center', dataIndex: 'ip' , renderer: renderIp},
		{header: "MAC", width: parseInt(w/3), sortable: false, align: 'center', dataIndex: 'mac', renderer: renderMac}		
	];

	baseStore = new Ext.data.JsonStore({
	    fields: ['id', 'ipStatus', 'nameStatus', 'macStatus', 'name','ip', 'mac']
	});
	
	var baseCm = new Ext.grid.ColumnModel(baseColumns);
	baseGrid = new Ext.grid.GridPanel({
		id: 'extbaseGridContainer', 
		cls: 'normalTable',
		height: 340,
		renderTo: "putGridPanel",
		border: true, 
		store: baseStore, 
		cm: baseCm,
		region:'center',
		viewConfig : {
			forceFit: true,hideGroupedColumn : true, enableNoGroups : true
		}
		});
	
	if(data.length < 25){
		showData = data;
	}else{
		showData = data.slice(0, 25)
		baseStore.loadData(showData);
		spareData = data.slice(25);
	}
	
	baseStore.loadData(showData);
});
var addData = null;
function showMore(){
	if(spareData.length > 25){
		addData = spareData.slice(0,25);
		showData = showData.concat(addData)
	    var length = showData.length - 25;
		
		var nowRecord = new Ext.data.Record({     
			id: addData[0].id,  
			name: addData[0].name,  
			ip: addData[0].ip, 
			mac: addData[0].mac
        });
		
		$.each(addData, function(index, data){
			//data -> record
			var record = new Ext.data.Record({     
				id: data.id,  
				name: data.name,  
				ip: data.ip, 
				mac: data.mac
	        });
			//record insert into grid
			baseStore.insert(length + index, record);
		})
		spareData = spareData.splice(25, spareData.length)
		baseGrid.getSelectionModel().selectRecords([nowRecord], false);
		return;
	}else if(spareData.length > 0){
		addData = spareData
		showData = showData.concat(spareData)
	    var length = showData.length - addData.length;
		
		var nowRecord = new Ext.data.Record({     
			id: addData[0].id,  
			name: addData[0].name,  
			ip: addData[0].ip, 
			mac: addData[0].mac
        });
		
		$.each(addData, function(index, data){
			//data -> record
			var record = new Ext.data.Record({     
				id: data.id,  
				name: data.name,  
				ip: data.ip, 
				mac: data.mac
	        });
			//record insert into grid
			baseStore.insert(length + index, record);
		})
		spareData = [];
		baseGrid.getSelectionModel().selectRecords([nowRecord], false);
		return;
	}else{
		window.parent.showMessageDlg(I18N.RECYLE.tip, "No more data");
	}
	//grid.getSelectionModel().selectRecords([record], false);
}
function showErrorData(){
	baseStore.loadData(errorData)
}
function showAllData(){
	if(data.length < 25){
		showData = data;
	}else{
		showData = data.slice(0, 25)
		baseStore.loadData(showData);
		spareData = data.slice(25);
	}
	
	baseStore.loadData(showData);
}
</script>
</head>
<body class="whiteToBlack">
	<div id="topPart">
		<Zeta:ButtonGroup>
			<Zeta:Button onClick="okBtClick()" icon="miniIcoSaveOK">Loading</Zeta:Button>
			<Zeta:Button onClick="showAllData()" id="allData">All</Zeta:Button>
			<Zeta:Button onClick="showErrorData()" id="BTC">Error</Zeta:Button>
		</Zeta:ButtonGroup>
	</div>	
	<div id="putGridPanel" class="mT10"></div>
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
	         <li><a href="javascript:showMore();" class="normalBtnBig"><span>More</span></a></li>
	         <li><a href="javascript:;" class="normalBtnBig"><span>Import</span></a></li>
	         <li><a href="javascript:cancelClick()" class="normalBtnBig"><span>@COMMON.cancel@</span></a></li>
	     </ol>
	</div>
	
</body>
</Zeta:HTML>
