<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module epon
    import js/customColumnModel
    import tools/cmdUtil
    import network.entityAction
    import epon.resourcesOlt
    import js.tools.authTool
</Zeta:Loader>
<script type="text/javascript"
	src="/include/i18n.tv?modulePath=com.topvision.ems.network.resources,com.topvision.ems.resources.resources,com.topvision.ems.epon.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<style>
.enableIconClass {background: url( /images/checked.gif ) no-repeat;}
.disableIconClass {background: url( /images/unchecked.gif ) no-repeat;}
</style>
<script type="text/javascript">
//var data = ${allIp};
var saveColumnId = 'oltlist';//保存表头数据的id;
window.onerror = function(){return true;}
var googleSupported = <%= SystemConstants.getInstance().getBooleanParam("googleSupported", false) %>;
var pageSize = <%= uc.getPageSize() %>;
var dispatcherInterval = <%= SystemConstants.getInstance().getLongParam("entitySnap.refresh.interval", 60000L) %>;
var dispatcherTimer = null;
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower = <%=uc.hasPower("refreshDevice")%>;
var googleMapPower = <%=uc.hasPower("googleMap")%>;
var topoGraphPower = <%=uc.hasPower("topoGraph")%>;
$(function(){
	//设备重启, 重载store ;
	var olt_reboot = top.PubSub.on(top.OltTrapTypeId.OLT_REBOOT, function(data){
		setEntityState(data, false);
		//trapStoreReload();
	});
	//设备启动，重载store;
	var olt_start = top.PubSub.on(top.OltTrapTypeId.OLT_START, function(data){
		setEntityState(data, true);
		//trapStoreReload()
	});
	
	 window.onunload = function(){
		top.PubSub.off(top.OltTrapTypeId.OLT_REBOOT, olt_reboot);
		top.PubSub.off(top.OltTrapTypeId.OLT_START, olt_start);
	};
});

function setEntityState(data, state) {
    if(store) {
        var ip = data.ip || data.host;
        // 根据ip找到对应的行，置为下线
        for (var i = 0, len=store.getCount(); i < len; i++) {
            if (store.getAt(i).get('ip') === ip) {
                store.getAt(i).set('state', state);
                store.getAt(i).commit();
                break;
            }
        }
    }
}

function onSeachClick() {
	/* 
	var name = $("#name").val();
	var ip = $("#ip").val();
	store.on("beforeload",function(){
        store.baseParams={entityName: name,ip: ip ,start:0,limit:pageSize};
	});
    store.load({params: {entityName: name,ip: ip ,start:0,limit:pageSize}});
     */
	store.baseParams={
		start: 0,
		limit: pageSize,	
		entityName: $('#searchEntity').val(),
		deviceType: $('#deviceType').val(),
		onlineStatus: $('#onlineStatus').val()
	}
	store.load();
}
</script>
	</head>
<body class=whiteToBlack>
		<!-- <div id="search" style="padding-left: 10px;">
			<table width=100% cellspacing=0 cellpadding=0>
				<tr>
					<td height=50px valign=middle style="pading-top: 10px;">
						<table width=100% cellspacing=0 cellpadding=0>
							<tr>
								<td style="width: 240px;">@OLT.entityIp@: <input
									type="text" name="name" id="ip"
									style="width: 190px; height: 20px;" />
								</td>
								<td style="width: 280px;">@ENTITYSNAP.basicInfo.alias@: <input
									type="text" name="name" id="name"
									style="width: 190px; height: 20px;" />
								</td>
								<td align=left>
									<button type="button" class=BUTTON75 onclick="onSeachClick()">@COMMON.query@</button>&nbsp;
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div> -->
		<div  id="grid"></div>
	</body>
</Zeta:HTML>
