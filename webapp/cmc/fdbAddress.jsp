<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    css css/white/disabledStyle
    MODULE cmc
</Zeta:Loader>
<script type="text/javascript">
    var cmId = '${cmId}';
    var cmIp = '${cmIp}';
	
	//关闭当前弹出框
 	function closeBtClick(){
 		window.parent.closeWindow('cmFdbAddress');
 	}
	
	//从设备刷新数据
	function refreshData(){
        dataStore.reload();
	}


	$(document).ready(function(){
		dataStore = new Ext.data.JsonStore({
			url : '/cmCpe/loadFdbAddress.tv',
			fields : ["fdbAddressString"],
			baseParams : {
				cmId : cmId,
                cmIp : cmIp
			}
		});
		
		colModel = new Ext.grid.ColumnModel([
		 	{header : "MAC",width : 80,align : 'center',dataIndex : 'fdbAddressString'}
		]);
		
		cmFdbAddressGrid =  new Ext.grid.GridPanel({
			id : 'cmFdbAddressGrid',
			title : "@TEMPL.cmFdbAddressList@",
			height : 390,
			border : true,
			cls : 'normalTable',
			store : dataStore,
			colModel : colModel,
			viewConfig : {
				forceFit : true
			},
			renderTo : 'contentGrid',
			sm : new Ext.grid.RowSelectionModel({
				singleSelect : true
			})
		});		
		dataStore.load();
	});
	
	//刷新Grid中的数据
	function refreshGridData(){
		dataStore.reload();
	};

	
</script>
</head>
<body class="openWinBody">
	<div class="edge10">
		<div id="contentGrid"></div>
	</div>
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 pT5 noWidthCenter">
	         <li><a id="refreshData" onclick='refreshData()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEquipment"></i>@COMMON.refresh@</span></a></li>
	         <li><a  onclick='closeBtClick()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.close@</span></a></li>
	     </ol>
	</div>

</body>
</Zeta:HTML>