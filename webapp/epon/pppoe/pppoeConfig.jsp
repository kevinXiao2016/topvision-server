<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
    <head>
        <%@include file="../../include/ZetaUtil.inc"%>
        <Zeta:Loader>
            library ext
            library jquery
            library zeta
            module oltdhcp
            css css/white/disabledStyle
        </Zeta:Loader>
        <script type="text/javascript">
        	var entityId = '${entityId}';
        	var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
        	var refreshDevicePower = <%=uc.hasPower("refreshDevice")%>;
            $(function(){
            	initData();
            });//end document.ready;
            
            function initData(){
            	$.ajax({
        			url : '/epon/oltdhcp/loadOltDhcpGlobalCfg.tv',
        			type : 'POST',
        			data : {
        				entityId : entityId
        			},
        			async: false,
        			dateType : 'json',
        			success : function(json) {
        				if (json) {
        					$("#pppoEPlusPolicy").val(json.topOltPPPoEPlusPolicy);
        					$("#pppoEPlusFormat").val(json.topOltPPPoEPlusFormat);
        				}
        			},
        			error : function() {},
        			cache : false
        		});
            }
            
            function refresh(){
            	window.top.showWaitingDlg("@COMMON.wait@", "@oltdhcp.refreshPppoeCfg@....", 'ext-mb-waiting');
    		    $.ajax({
    		        url:"/epon/oltdhcp/refreshOltPppoeCfg.tv",
    		        method:"post",
    		        data : {
        				entityId : entityId
        			},
    		        dataType:'text',
    		        success:function (text) {
    		            window.top.closeWaitingDlg();
    		            top.afterSaveOrDelete({
    		                   title: '@COMMON.tip@',
    		                   html: '<b class="orangeTxt">@oltdhcp.refreshPppoeCfg@@oltdhcp.success@！</b>'
    		            });
    		            window.location.reload();
    		        },error:function(){
    		        	window.top.closeWaitingDlg();
    		            window.parent.showMessageDlg("@COMMON.tip@", "@oltdhcp.refreshPppoeCfg@@oltdhcp.fail@！");
    		        }
    		    });
            }
            
            function checkPppoEPlusFormat(pppoEPlusFormat) {
            	var reg = /^[^%]*((\%|\%0*[0-9]|\%h)\((AccessNodeIdentifier|ANI_rack|ANI_frame|ANI_slot|ANI_subslot|ANI_port|ONU_ID|ONU_NO|ONU_LOID|SVLAN|CVLAN)\))?(\/(\%|\%[0-9]|\%0[0-9]|\%h)\((AccessNodeIdentifier|ANI_rack|ANI_frame|ANI_slot|ANI_subslot|ANI_port|ONU_ID|ONU_NO|ONU_LOID|SVLAN|CVLAN)\))*[^%]*$/;
				var regChinese = /^[a-zA-Z0-9-\s_:;\/(),.'"*&^%$#@!=+|?<>\[\]\{\}\\`~]+$/;
             	
				if(pppoEPlusFormat.length > 127||pppoEPlusFormat.length == 0){
					return false;
				}
				// /%(关键字)可以通过正则校验
				if(pppoEPlusFormat.indexOf("/%") == 0){
					return false;
				}
				if(!reg.test(pppoEPlusFormat)||!regChinese.test(pppoEPlusFormat)) {
					return false;
				}
				return true;
         	}
            
            function save(){
            	var pppoEPlusPolicy = $("#pppoEPlusPolicy").val();
            	var pppoEPlusFormat = $("#pppoEPlusFormat").val();
             	if (!checkPppoEPlusFormat(pppoEPlusFormat)) {
                    $('#pppoEPlusFormat').focus();
                    return false;
                }
            	window.top.showWaitingDlg("@COMMON.wait@", "@oltdhcp.modifyPppoeCfg@....", 'ext-mb-waiting');
    		    $.ajax({
    		        url:"/epon/oltdhcp/modifyOltPppoeCfg.tv",
    		        method:"post",
    		        data : {
        				entityId : entityId,
        				pppoEPlusPolicy : pppoEPlusPolicy,
        				pppoEPlusFormat : pppoEPlusFormat
        			},
    		        dataType:'text',
    		        success:function (text) {
    		            window.top.closeWaitingDlg();
    		            top.afterSaveOrDelete({
    		                   title: '@COMMON.tip@',
    		                   html: '<b class="orangeTxt">@oltdhcp.modifyPppoeCfg@@oltdhcp.success@！</b>'
    		            });
    		            //window.location.reload();
    		        },error:function(){
    		        	window.top.closeWaitingDlg();
    		            window.top.showMessageDlg("@COMMON.tip@", "@oltdhcp.modifyPppoeCfg@@oltdhcp.fail@！");
    		        }
    		    });
            }
            
            function authLoad() {
            	//权限控制
            	if(!refreshDevicePower) {
            		$("#refreshBtn").attr("disabled",true);
            	}
				if(!operationDevicePower) {
					$("#saveBtn").attr("disabled",true);
            	}
            }
        </script>
    </head>
    <body class="grayBody pL10 pR10" onload="authLoad()">
        <table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
        	<thead>
        		<tr>
        			<th colspan="3" class="txtLeftTh">@oltdhcp.pppoeConfig@</th>
        		</tr>
        	</thead>
        	<tbody>
        		<tr>
        			<td class="rightBlueTxt w200">PPPoE+@oltdhcp.labelProcessStrategy@@COMMON.maohao@</td>
        			<td>
        				<select id="pppoEPlusPolicy" class="normalSel w200">
        					<option value="1">@oltdhcp.insert@</option>
        					<option value="2">@oltdhcp.replace@</option>
        				</select>
        			</td>
        		</tr>
        		<tr>
        			<td class="rightBlueTxt" valign="top"><span class="pT5 displayBlock">@COMMON.required@PPPoE+@oltdhcp.label@@COMMON.maohao@</span></td>
        			<td>
        				<textarea rows="5" id="pppoEPlusFormat" class="normalInput w400 mT5 mB5" style="height: 120px" maxlength="127" toolTip='@oltdhcp.labelTip@'></textarea>
        			</td>
        			<td>@oltdhcp.option82Tip@</td>
        		</tr>
        	</tbody>
        </table>
        <ol class="upChannelListOl pB0 pT20 noWidthCenter">
		    <li><a id="refreshBtn" href="javascript:;" class="normalBtnBig" onclick="refresh();"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
		    <li><a id="saveBtn" href="javascript:;" class="normalBtnBig" onclick="save();"><span><i class="miniIcoSave"></i>@COMMON.apply@</span></a></li>
		</ol>
    </body>
</Zeta:HTML>