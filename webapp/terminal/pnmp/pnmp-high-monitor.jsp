<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jQuery
    library zeta
    module pnmp
    import js/highcharts-4.1.5/highcharts
    import js/highcharts-4.1.5/highcharts-more
    import terminal/pnmp/js/pnmpUtil
    import terminal/pnmp/js/pnmp-high-monitor
</Zeta:Loader>

<style type="text/css">
.badColor {
	color: #E83C23;
}
.marginalColor {
	color: #f38900;
}
.healthColor {
	color: #449d44;
}
</style>

<script type="text/javascript">
var pageSize = <%=uc.getPageSize()%>;
var sm, store, grid;
var thresholdMap = {};
var cmSignalThresholdMap = {};

function addCm() {
	window.parent.createDialog("showAddHighMonitorCm", '@pnmp.addCm@', 600, 400, "/pnmp/monitor/showAddHighMonitorCm.tv?pageId=" + window.parent.getActiveFrameId(), null, true, true);
}

//根据条件查询
function simpleQuery(){
	/* var record = grid.getSelectionModel().getSelected(); */
	var cmcName = $("#cmcName").val();
	var cmMac = $("#cmMac").val();
	var cmAddress = $("#cmAddress").val();
	store.baseParams = {
		cmcName: cmcName,
		cmMac: cmMac,
		cmAddress: cmAddress
	};
	//在执行完相关操作后去掉grid表头上的复选框选中状态
	store.load({
    	callback: function(){
    		disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
		}
    });
}

// 跳转历史记录页面
function showHistoryData(){
	var CM = getSelectedCM();
	if(CM){
		window.top.addView('cmhistorydata-'+CM.cmMac, CM.cmMac + '-@pnmp.cmhistorydata@','icoI3', '/pnmp/cmdata/showHistoryData.tv?cmMac='+CM.cmMac,null,true);
	}
}

// 删除高频队列中的CM
function removeCm(){
	var CM = getSelectedCM();
	window.parent.showConfirmDlg("@COMMON.tip@", "@resources/COMMON.confirmDelete@@COMMON.wenhao@", function(button, text) {
		if (button == "yes") {
			$.ajax({
				url : '/pnmp/monitor/removeHighMonitorCm.tv',
				type : 'post',
				data : {
					cmMac: CM.cmMac
				},
				dataType : "text",
				success : function(response) {
					if(response == "success"){
						//在执行完相关操作后去掉grid表头上的复选框选中状态
			        	store.load({
			            	callback: function(){
			            		disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
			        		}
			            });
						top.afterSaveOrDelete({
							title : '@COMMON.tip@',
							html : '<b class="orangeTxt">@platform/Common.deleteSuccess@</b>'
						});
					}else{
						window.parent.showMessageDlg('@COMMON.error@','@platform/Common.deleteFailed@');
					}
				},
				error : function(response) {
					window.parent.showMessageDlg('@COMMON.error@','@platform/Common.deleteFailed@');
				},
				cache : false
			});
		}
	});
}

//批量删除高频队列中的CM
function batchRemoveCm(){
	if(sm){
		if(sm.getSelections().length==0){
	        window.top.showMessageDlg(I18N.COMMON.tip, "@pnmp.selectCM@");
	        return;
	    }
		
		window.parent.showConfirmDlg("@COMMON.tip@", "@resources/COMMON.confirmDelete@@COMMON.wenhao@", function(button, text) {
			if (button == "yes") {
				var rs=sm.getSelections();
			    var cmMacs=[];
			    for(var i = 0; i < rs.length; i++){
			    	cmMacs[i]=rs[i].data.cmMac;
	    		}
		
			$.ajax({
		        url: '/pnmp/monitor/batchRemoveHighMonitorCm.tv',
		        type: 'post',
		        dataType:"json",
		        data:{
		        	'cmMacs':cmMacs.join("$")
		        },
		        cache: false, 
		        success: function(response) {
		        	//在执行完相关操作后去掉grid表头上的复选框选中状态
		        	store.load({
		            	callback: function() {
		            		disabledToolbarBtn(grid.getSelectionModel().getSelections().length);
		        		}
		            });
		        }, 
		        error: function(response) {
		        	window.parent.showMessageDlg('@COMMON.error@','@Common.deleteSuccess@');
		        }
		    });
		}});
	}
}
</script>
	</head>
	<body class="bodyGrayBg">
		<div id="query-container">
			<table class="queryTable">
				<tr>
					<td class="rightBlueTxt">@pnmp.cmcName@@COMMON.maohao@</td>
					<td><input id="cmcName" class="normalInput w200" /></td>
					<td class="rightBlueTxt w100">CM MAC@COMMON.maohao@</td>
					<td><input id="cmMac" class="normalInput w200" /></td>
					<td class="rightBlueTxt w100">@pnmp.cmcAddress@@COMMON.maohao@</td>
					<td><input id="cmAddress" class="normalInput w200" /></td>
					<td><a id="simple-query" href="javascript:simpleQuery();"
						class="normalBtn" onclick=""><span><i
								class="miniIcoSearch"></i>@COMMON.query@</span></a></td>
				</tr>
			</table>
		</div>
	</body>
</Zeta:HTML>