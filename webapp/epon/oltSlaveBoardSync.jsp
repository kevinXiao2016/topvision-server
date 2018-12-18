<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
	library jquery
	library zeta
    module epon
</Zeta:Loader>
    <script type="text/javascript">
    	var entityId = <%=request.getParameter("entityId")%>;
    	function cancelClick() {
    		window.parent.closeWindow('oltSlaveBoardSync');
    	}
    	function syncSlaveBoard() {
    		var syncAction = $('input:radio[name="sync"]:checked').val();
    		var message = syncAction == 1?I18N.SERVICE.cfmSync:I18N.SERVICE.cfmSynConfig;
    		var waitMessage = syncAction == 1?I18N.SERVICE.softSyncing:I18N.SERVICE.configSyncing;
    		window.parent.showConfirmDlg(I18N.COMMON.tip, message , function(type) {
       			if (type == 'no') {return;}
       			window.top.showWaitingDlg(I18N.COMMON.wait, waitMessage , 'ext-mb-waiting');
       			$.ajax({
       				url: '/epon/syncSlaveBoard.tv?r=' + Math.random(),
       				type: 'POST',
       				data: "entityId=" + entityId + "&syncAction=" + syncAction,
       				success: function(text) {
           				if(text == 'success'){
           					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.syncTip );
           					window.parent.closeWindow('oltSlaveBoardSync');
               			}else if(text == 'syncInprogress'){
               				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.syncTip2 );
                   		}else{
                   			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.syncEr );
                       	}
       				}, error: function(text) {
       					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.syncEr );
       				},cache: false
       			});
       		});
    	}
    </script>
</head>
<body class="openWinBody">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@SERVICE.syncTypeSelect@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	
	<div class="edge10">
			<table class="mCenter zebraTableRows" >
			<tr>
				<td class="w120 txtRight"><input id="syncApp" name="sync" type="radio" value="1" checked/></td>
				<td><label for="syncApp">@SERVICE.syncSoft@</label></td>
			</tr>
			<tr>
				<td class="txtRight"><input id="syncConfig" name="sync" type="radio" value="2" /></td>
				<td><label for="syncConfig">@SERVICE.syncCfg@</label></td>
			</tr>
		</table>
	</div>
	<Zeta:ButtonGroup>
			<Zeta:Button onClick="syncSlaveBoard()" icon="miniIcoTwoArr">@SERVICE.sync@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
</body>
</Zeta:HTML>