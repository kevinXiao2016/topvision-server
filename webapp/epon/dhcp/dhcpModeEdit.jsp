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
            module oltdhcp
            CSS css/white/disabledStyle
        </Zeta:Loader>
        <script type="text/javascript">
        	var entityId = '${entityId}';
        	var vlanIndex = '${vlanIndex}';
        	Ext.onReady(function(){
        		loadData();
        	});//end document.ready;
        	
        	function loadData(){
        		$.ajax({
        			url : '/epon/oltdhcp/loadOltDhcpVlan.tv',
        			type : 'POST',
        			data : {
        				entityId : entityId,
        				vlanIndex : vlanIndex
        			},
        			dateType : 'json',
        			success : function(json) {
        				if (json) {
        					$('#topOltDhcpVLANIndex').val(json.topOltDhcpVLANIndex);
        					$('#connectName').val(json.connectName);
        					$('#topOltDhcpVLANMode').val(json.topOltDhcpVLANMode);
        					modeSelectChanged();
        					if(json.topOltDhcpVLANMode == 2){
	        					$('#topOltDhcpVLANRelayMode').val(json.topOltDhcpVLANRelayMode);
        					}
        				}
        			},
        			error : function() {
        			},
        			cache : false
        		});
        	}
        	
        	function modeSelectChanged(){
        		var mode = $("#topOltDhcpVLANMode").val();
        		if(mode == 1){
        			$("#relayModeTr").hide();
        		}else{
        			$("#relayModeTr").show();
        		}
        	}
        	
        	function saveClick(){
        		var vlanMode = $("#topOltDhcpVLANMode").val();
        		var relayMode = $("#topOltDhcpVLANRelayMode").val();
        		window.top.showWaitingDlg("@COMMON.wait@", "@oltdhcp.modifyMode@....", 'ext-mb-waiting');
        		$.ajax({
        	        url:"/epon/oltdhcp/modifyOltDhcpVlan.tv",
        	        data: {
        	            entityId: entityId,
        	            vlanIndex: vlanIndex,
        	            vlanMode: vlanMode,
        	            relayMode: relayMode
        	        },
        	        method:"post",cache: false,dataType:'text',
        	        success:function (text) {
        	            window.top.closeWaitingDlg();
        	            top.afterSaveOrDelete({
        	                   title: '@COMMON.tip@',
        	                   html: '<b class="orangeTxt">@oltdhcp.modifyMode@@oltdhcp.success@！</b>'
        	            });
        	            cancelClick();
        	        },error:function(){
        	            window.parent.showMessageDlg("@COMMON.tip@", "@oltdhcp.modifyMode@@oltdhcp.fail@！");
        	        }
        	    });
        	}
        	
            //取消;
            function cancelClick() {
                top.closeWindow("modalDlg");
            }
        </script>
    </head>
    <body class="openWinBody">
        <div class="openWinHeader">
            <div class="openWinTip">
                <p>@oltdhcp.modifyDhcpMode@</p>
            </div>
            <div class="rightCirIco pageCirIco"></div>
        </div>
        <div class="edgeTB10LR20 pT40">
            <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
                <tbody>
                    <tr>
                        <td class="rightBlueTxt" width="180">VLAN ID</td>
                        <td>
                            <input id="topOltDhcpVLANIndex" type="text" class="w180 normalInput" disabled="disabled" />
                        </td>
                    </tr>
                    <tr class="darkZebraTr">
                        <td class="rightBlueTxt" width="180">@oltdhcp.mode@</td>
                        <td>
                            <select id = "topOltDhcpVLANMode" onchange="modeSelectChanged();" class="w180 normalSel">
                            	<option value="1">snooping</option>
                            	<option value="2">relay</option>
                            </select>
                        </td>
                    </tr>
                    <tr id = "relayModeTr">
                        <td class="rightBlueTxt" width="180">@oltdhcp.relayMode@</td>
                        <td>
                            <select id = "topOltDhcpVLANRelayMode" class="w180 normalSel">
                            	<option value="1">standard</option>
                            	<option value="2">option60</option>
                            </select>
                        </td>
                    </tr>
                </tbody>
            </table>
            <div class="noWidthCenterOuter clearBoth">
                <ol class="upChannelListOl pB0 pT40 noWidthCenter">
                    <li>
                        <a href="javascript:;" class="normalBtnBig" onclick="saveClick();">
                            <span><i class="miniIcoEdit"></i>@COMMON.edit@</span>
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