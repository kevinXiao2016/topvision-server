<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
    <head>
        <%@include file="/include/ZetaUtil.inc"%>
        <Zeta:Loader>
            library ext
            library jquery
            library zeta
            module pnmp
        </Zeta:Loader>
        <script type="text/javascript">
        	var pageId = '${pageId}';
        	
            //取消;
            function cancelClick() {
                top.closeWindow("showAddHighMonitorCm");
            }
            
            //检验网管中是否存在该Mac
            function checkMac(){
            	var cmMac = $("#cmMac").val();
            	if(!Validator.isMac(cmMac)){
            		$("#cmMac").focus();
            		return;
            	}
            	$.ajax({
					url : '/pnmp/monitor/checkCmMac.tv',
					type : 'post',
					data : {
						cmMac: cmMac
					},
					dataType : "json",
					success : function(response) {
						var $mainTable = $('#mainTable'),
							$tr1 = $mainTable.find('tbody:eq(0) tr'),
						    $tb2 = $mainTable.find('tbody:eq(1)'),
						    $tb3 = $mainTable.find('tbody:eq(2)'),
						    $tb4 = $mainTable.find('tbody:eq(3)'),
						    $name1 = $("#putCmtsName"),
						    $name2 = $("#cmtsName"),
						    $ip1 = $("#putCmtsIp"),
						    $ip2 = $("#cmtsIp"),
							$location1 = $("#putCmtsLocation"),
							$location2 = $("#cmtsLocation");
						
						$tr1.removeClass('withoutBorderBottom');
						if(response.message == "success"){
							$tb2.css({visibility: 'visible'});
							$tb3.css({visibility: 'visible'});
							$tb4.css({visibility: 'visible'});
							$name1.text('@pnmp.cmcName@@COMMON.maohao@');
							$name2.text(response.alias);
							$ip1.text('@pnmp.cmcIp@@COMMON.maohao@');
							$ip2.text(response.ipAddress);
							$location1.text('@pnmp.cmcLocation@@COMMON.maohao@');
							$location2.text(response.location);
						}else{
							$tb2.css({visibility: 'visible'});
							$tb3.css({visibility: 'hidden'});
							$tb4.css({visibility: 'hidden'});
							$name1.text('');
							$name2.text('@pnmp.cmMacNotInEMS@');
							$ip1.text('');
							$ip2.text('');
							$location1.text('');
							$location2.text('');
						}
					},
					error : function(response) {
						window.parent.showMessageDlg('@COMMON.error@','@pnmp.checkMacError@');
					},
					cache : false
				});
            }
            
            //添加Mac
            function addCmMac(){
            	var cmMac = $("#cmMac").val();
            	if(!Validator.isMac(cmMac)){
            		$("#cmMac").focus();
            		return;
            	}
            	$.ajax({
					url : '/pnmp/monitor/addHighMonitorCm.tv',
					type : 'post',
					data : {
						cmMac: cmMac
					},
					dataType : "json",
					success : function(response) {
						if(response.code == 1){
							top.afterSaveOrDelete({
								title : '@COMMON.tip@',
								html : '<b class="orangeTxt">@resources/COMMON.addSuccess@</b>'
							});
							window.parent.getFrame(pageId).reloadAfterAddMac();
							cancelClick();
						}else if(response.code == 0){
							window.parent.showMessageDlg('@COMMON.error@','@pnmp.cmMacNotNull@');
						}else if(response.code == 2){
							window.parent.showMessageDlg('@COMMON.error@','@pnmp.cmMacDuplicate@');
						}else if(response.code == 3){
							window.parent.showMessageDlg('@COMMON.error@','@pnmp.cmMacNotInEMS@');
						}else if(response.code == 4){
							window.parent.showMessageDlg('@COMMON.error@','@pnmp.cmMacFull@');
						}else{
							window.parent.showMessageDlg('@COMMON.error@','@pnmp.addHighMonitorError@');
						}
					},
					error : function(response) {
						window.parent.showMessageDlg('@COMMON.error@','@pnmp.addHighMonitorError@');
					},
					cache : false
				});
            }
        </script>
    </head>
    <body class="openWinBody">
        <div class="openWinHeader">
            <div class="openWinTip">
                <p>@pnmp.inputMAC@</p>
            </div>
            <div class="rightCirIco pageCirIco"></div>
        </div>
        <div class="edgeTB10LR20 pT60">
            <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0" id="mainTable">
                <tbody>
                    <tr class="withoutBorderBottom">
                        <td class="rightBlueTxt" width="160">CM MAC@COMMON.maohao@</td>
                        <td style="width: 180px;">
                            <input id="cmMac" type="text" class="w180 normalInput" toolTip='@pnmp.inputlegalMAC@'/>
                        </td>
                        <td>
                        	<a href="javascript:checkMac();" class="normalBtn">
                        		<span><i class="miniIcoSearch"></i>@pnmp.checkMac@</span>
                        	</a>
                        </td>
                    </tr>
                </tbody>
                <tbody style="visibility: hidden;">
                    <tr class="darkZebraTr">
                        <td id="putCmtsName" class="rightBlueTxt"></td>
                        <td colspan='2' id="cmtsName"></td>
                    </tr>
                </tbody>
                <tbody style="visibility: hidden;">
                    <tr>
                        <td id="putCmtsIp"  class="rightBlueTxt"></td>
                        <td colspan='2' id="cmtsIp"></td>
                    </tr>
                </tbody>
                <tbody style="visibility: hidden;">
                    <tr class="darkZebraTr">
                        <td id="putCmtsLocation" class="rightBlueTxt"></td>
                        <td colspan='2' id="cmtsLocation"></td>
                    </tr>
                </tbody>
            </table>
            <div class="noWidthCenterOuter clearBoth">
                <ol class="upChannelListOl pB0 pT30 noWidthCenter">
                    <li>
                        <a href="javascript:;" class="normalBtnBig" onclick="addCmMac()">
                            <span><i class="miniIcoData"></i>@COMMON.add@</span>
                        </a>
                    </li>
                    <li>
                        <a href="javascript:;" class="normalBtnBig" onclick="cancelClick()">
                            <span><i class="miniIcoForbid"></i>@COMMON.cancel@</span>
                        </a>
                    </li>
                </ol>
            </div>
        </div>
    </body>
</Zeta:HTML>