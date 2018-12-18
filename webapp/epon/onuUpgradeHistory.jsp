<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@include file="/include/ZetaUtil.inc"%>
	 <Zeta:Loader>
	    library ext
	    library jquery
	    library zeta
	    module epon
	</Zeta:Loader>
	
	<title>@SERVICE.onuUdtHistory@</title>
	<style type="text/css">
	.normalTable table a:hover font{ color:#fff;}
	</style>
	<script type="text/javascript">
		var entityId = <%=request.getParameter("entityId")%>;
		var onuUpgradeHistoryGrid;
		var onuUpgradeHistoryStore;
		Ext.onReady(function() {
			onuUpgradeHistoryStore = new Ext.data.Store({
				proxy: new Ext.data.HttpProxy({
					url: '/onu/loadOnuUpgradeHistory.tv?entityId=' + entityId + '&r=' + Math.random()
				}),
				reader: new Ext.data.JsonReader({
					}, Ext.data.Record.create([{
						name: 'onuUpgradeId'
					}, {
						name: 'topOnuUpgradeSlotNum'
					}, {
						name: 'topOnuUpgradeOnuType'
					}, {
						name: 'topOnuUpgradeFileName'
					}, {
						name: 'upgradeTimeString'
					}, {
						name: 'upgradeOnuList'
					}]))
			});
			onuUpgradeHistoryStore.load();
			onuUpgradeHistoryGride = new Ext.grid.GridPanel({
				renderTo: 'onuUpgradeHistoryGrid',
				//width: 840,
				height: 330,
				autoScroll: true,
				columns: [{
					header: "@SERVICE.udtSN@" , dataIndex: 'onuUpgradeId', width: 80, align: 'center', sortable: true
				}, {
					header: "@EPON.slotNum@" , dataIndex: 'topOnuUpgradeSlotNum', width: 80, align: 'center'
				}, {
					header: "@SERVICE.onuDeviceType@" , dataIndex: 'topOnuUpgradeOnuType', width: 100, align: 'center', renderer: function(value) {
	 					var onuTypeDescr = ''
	 					switch (value) {
	 						case 33:
	 							onuTypeDescr = 'PN8621';
	 							break;
	 						case 34:
	 							onuTypeDescr = 'PN8622';
	 							break;
	 						case 35:
	 							onuTypeDescr = 'PN8623';
	 							break;
	 						case 36:
	 							onuTypeDescr = 'PN8624';
	 							break;
	 						case 37:
	 							onuTypeDescr = 'PN8625';
	 							break;
	 						case 38:
	 							onuTypeDescr = 'PN8626';
	 							break;
	 						case 65:
	 							onuTypeDescr = 'PN8641';
	 							break;
	 						case 68:
	 							onuTypeDescr = 'PN8644';
	 							break;
	 						case 71:
	 							onuTypeDescr = 'PN8647';
	 							break;
	 						case 81:
	 							onuTypeDescr = 'PN8651';
	 							break;
	 						case 82:
	 							onuTypeDescr = 'PN8652';
	 							break;
	 						case 83:
	 							onuTypeDescr = 'PN8653';
	 							break;
	 						case 84:
	 							onuTypeDescr = 'PN8654';
	 							break;
	 						case 241:
	 							onuTypeDescr = 'CC8800A/C-A';
	 							break;
	 						case 255:
	 							onuTypeDescr = "@SERVICE.unkownType@";
	 							break;
	 						default:
	 							onuTypeDescr = "@SERVICE.unkownType@";
	 					}
	 					return onuTypeDescr;
	 				}
				}, {
					header: "@SERVICE.udtFile@" , dataIndex: 'topOnuUpgradeFileName', width: 200, align: 'center'
				},  {
					header: "@SERVICE.upgradeRecordTime@" , dataIndex: 'upgradeTimeString', width: 140, align: 'center'
				},/* {
					header: "@SERVICE.udtType@" , dataIndex: 'topOnuUpgradeFileType', width: 70, align: 'center', renderer: function(value) {
						var upgradeType = '';
						switch (value) {
	 						case 9:
	 							upgradeType = 'Boot';
	 							break;
	 						case 10:
	 							upgradeType = 'Config';
	 							break;
	 						case 11:
	 							upgradeType = 'Firmware';
	 							break;
	 						case 12:
	 							upgradeType = 'App';
	 							break;
	 						case 13:
	 							upgradeType = 'CombinedPkg';
	 							break;
	 						default:
	 							onuTypeDescr = 'unknown';
	 					}
	 					return upgradeType;
					}
				}, */ {
					header: "@SERVICE.onuDeviceList@" ,  dataIndex: 'upgradeOnuList', width: 240, align: 'center', renderer:function(value){
						return '<a href="javascript:;" class="linkList">'+ value +'</a>';
					}
				}],
				sm: new Ext.grid.RowSelectionModel({singleSelect: true}),
				store: onuUpgradeHistoryStore,
				viewConfig: { forceFit:true}
			});
		});
		function cancelClick() {
			window.parent.closeWindow('onuUpgradeHistory')
		}
		$(function(){
			$("#onuUpgradeHistoryGrid").delegate(".linkList","click",function(){
				var greenArr = [],redArr=[];//记录成功的和失败共有多少个;
				var $font = $(this).find("font"); 
				if( $font.length > 5 ){//数目大于5则显示弹出层;
					$font.each(function(){
						var $me = $(this);
						switch($me.attr("color")){
							case "green":
								greenArr.push($me.text());
							break;
							case "red":
								redArr.push($me.text())
							break;
						}
					});
					if( $("#openLayer").length != 1 ){ //如果没有弹出层，先创建一个;
						$("body").append('<div id="openLayer" style="width:100%; height:386px; background:#fff; position:absolute; top:62px; left:0px; overflow:auto;"></div>');
					}else{
						$("#openLayer").css({display:"block"});
					}
					$("#openLayer").empty();
					var domStr = '<div class="edge10"><a href="javascript:;" class="normalBtn" onclick="backFn()"><span><i class="miniIcoArrLeft"></i>@COMMON.back@</span></a>';
						if( greenArr.length > 0 ){
							domStr += '<p class="clearBoth pL5 pT20 f14 greenTxt"><b>@COMMON.updateSuccess@ ('+ greenArr.length +')</b></p>';
							domStr += '<div class="edge10 pB0">';
							for(var i=0; i<greenArr.length; i++){
								domStr += '<span style="width:80px; display:block; float:left; padding-bottom:3px;">'+ greenArr[i] +'</span>';
							}
							domStr += '</div>';
						}
						if( redArr.length > 0 ){
							domStr += '<p class="clearBoth pL5 pT20 f14 redTxt"><b>@COMMON.updateFail@ ('+ redArr.length +')</b></p>';
							domStr += '<div class="edge10 pB0">';
							for(var i=0; i<redArr.length; i++){
								domStr += '<span style="width:80px; display:block; float:left; padding-bottom:3px;">'+ redArr[i] +'</span>';
							}
							domStr += '</div>';
						}
					    domStr += '</div>';
					$("#openLayer").append(domStr);
				}
			});
		})
		function backFn(){
			$("#openLayer").fadeOut();
		}
	</script>
</head>
<body class="openWinBody">	
<div class="openWinHeader">
    <div class="openWinTip">@SERVICE.udtTip@</div>
    <div class="rightCirIco pageCirIco"></div>
</div>

<div class="edge10">
	<div id="onuUpgradeHistoryGrid" class="normalTable"></div>
</div>
	
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl noWidthCenter pB0 pT0">
	         <li><a id='cancelBt' href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span>@COMMON.close@</span></a></li>
	     </ol>
	</div>
</body>
</Zeta:HTML>