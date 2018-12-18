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
            IMPORT js/nm3k/Nm3kSwitch
            css css/white/disabledStyle
        </Zeta:Loader>
        <script type="text/javascript">
	        var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
	    	var refreshDevicePower = <%=uc.hasPower("refreshDevice")%>;
        	var entityId = '${entityId}';
        	var opt82Switch;
        	var imgBtn
            $(function(){
            	initData();
            	imgBtn = new Nm3kSwitch("putSwitch", opt82Switch,{
	            	yesNoValue: [1, 2],
	        		afterChangeCallback: function(selectValue){
	        			setOption82Switch(selectValue);
	        		}
            	});
            	imgBtn.init();
            	setOption82Switch(imgBtn.getValue());
            	
            	
            });//end document.ready;
            function authLoad() {
            	//权限控制
            	if(!refreshDevicePower) {
            		$("#refreshBtn").attr("disabled",true);
            	}
				if(!operationDevicePower) {
					$("#saveBtn").attr("disabled",true);
            	}
            }
            
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
        					opt82Switch = json.topOltDhcpOpt82Enable;
        					$("#opt82Policy").val(json.topOltDhcpOpt82Policy);
        					$("#opt82Format").val(json.topOltDhcpOpt82Format);
        				}
        			},
        			error : function() {},
        			cache : false
        		});
            }
            
            function setOption82Switch(selectValue){
            	if(selectValue == 1){
            		$("#opt82Policy").attr("disabled",false)
					$("#opt82Format").attr("disabled",false)
            	}else{
            		$("#opt82Policy").attr("disabled",true)
					$("#opt82Format").attr("disabled",true)
            	}
            }
            
            function checkOpt82Format(opt82Format) {
         		var reg = /^[^%]*((\%|\%0*[0-9]|\%h)\((AccessNodeIdentifier|ANI_rack|ANI_frame|ANI_slot|ANI_subslot|ANI_port|ONU_ID|ONU_NO|ONU_LOID|SVLAN|CVLAN)\))?(\/(\%|\%[0-9]|\%0[0-9]|\%h)\((AccessNodeIdentifier|ANI_rack|ANI_frame|ANI_slot|ANI_subslot|ANI_port|ONU_ID|ONU_NO|ONU_LOID|SVLAN|CVLAN)\))*[^%]*$/;
         		var regChinese = /^[a-zA-Z0-9-\s_:;\/(),.'"*&^%$#@!=+|?<>\[\]\{\}\\`~]+$/;
             	
				if(opt82Format.length > 127||opt82Format.length == 0){
					return false;
				}
				// /%(关键字)可以通过正则校验
				if(opt82Format.indexOf("/%") == 0){
					return false;
				}
				if(!reg.test(opt82Format)||!regChinese.test(opt82Format)) {
					return false;
				}
				return true;
         	}
            
            function save(){
            	var opt82Enable = imgBtn.getValue();
            	var opt82Policy = $("#opt82Policy").val();
				var opt82Format = $("#opt82Format").val();
             	
             	if(!checkOpt82Format(opt82Format)) {
             		$('#opt82Format').focus();
                    return false;
             	}
             	
            	window.top.showWaitingDlg("@COMMON.wait@", "@oltdhcp.modifyOption82@....", 'ext-mb-waiting');
    		    $.ajax({
    		        url:"/epon/oltdhcp/modifyOltDhcpOption82Cfg.tv",
    		        method:"post",
    		        data : {
        				entityId : entityId,
        				opt82Enable : opt82Enable,
        				opt82Policy : opt82Policy,
        				opt82Format : opt82Format
        			},
    		        dataType:'text',
    		        success:function (text) {
    		            window.top.closeWaitingDlg();
    		            top.afterSaveOrDelete({
    		                   title: '@COMMON.tip@',
    		                   html: '<b class="orangeTxt">@oltdhcp.modifyOption82@@oltdhcp.success@！</b>'
    		            });
    		            //window.location.reload();
    		        },error:function(){
    		        	window.top.closeWaitingDlg();
    		            window.top.showMessageDlg("@COMMON.tip@", "@oltdhcp.modifyOption82@@oltdhcp.fail@！");
    		        }
    		    });
            }
            
            function refresh(){
            	window.top.showWaitingDlg("@COMMON.wait@", "@oltdhcp.refreshOption82@....", 'ext-mb-waiting');
    		    $.ajax({
    		        url:"/epon/oltdhcp/refreshOltDhcpOption82Cfg.tv",
    		        method:"post",
    		        data : {
        				entityId : entityId
        			},
    		        dataType:'text',
    		        success:function (text) {
    		            window.top.closeWaitingDlg();
    		            top.afterSaveOrDelete({
    		                   title: '@COMMON.tip@',
    		                   html: '<b class="orangeTxt">@oltdhcp.refreshOption82@@oltdhcp.success@！</b>'
    		            });
    		            window.location.reload();
    		        },error:function(){
    		        	window.top.closeWaitingDlg();
    		            window.top.showMessageDlg("@COMMON.tip@", "@oltdhcp.refreshOption82@@oltdhcp.fail@！");
    		        }
    		    });
            }
        </script>
    </head>
    <body class="grayBody pL10 pR10" onload="authLoad()">
        <table class="dataTableRows zebra" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
        	<thead>
        		<tr>
        			<th colspan="3" class="txtLeftTh">@oltdhcp.option82Config@</th>
        		</tr>
        	</thead>
        	<tbody>
        		<tr>
        			<td class="rightBlueTxt w200">@oltdhcp.option82Switch@@COMMON.maohao@</td>
        			<td id="putSwitch">
        			
        			</td>
        		</tr>
        		<tr>
        			<td class="rightBlueTxt">Option82@oltdhcp.labelProcessStrategy@@COMMON.maohao@</td>
        			<td>
        				<select id ="opt82Policy" class="normalSel w200">
        					<option value="1">@oltdhcp.insert@</option>
        					<option value="2">@oltdhcp.replace@</option>
        				</select>
        			</td>
        		</tr>
        		<tr>
        			<td class="rightBlueTxt">@COMMON.required@Option82@oltdhcp.label@@COMMON.maohao@</td>
        			<td>
        				<textarea rows="5" id ="opt82Format" class="normalInput w400 mT5 mB5" style="height: 120px" maxlength="127" toolTip='@oltdhcp.labelTip@'></textarea>
        			</td>
        			<td>@oltdhcp.option82Tip@</td>
        		</tr>
        	</tbody>
        </table>
        <ol class="upChannelListOl pB0 pT20 noWidthCenter">
		    <li><a id="refreshBtn" href="javascript:;" onclick="refresh()" class="normalBtnBig"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
		    <li><a id="saveBtn" href="javascript:;" onclick="save()"class="normalBtnBig"><span><i class="miniIcoSave"></i>@COMMON.apply@</span></a></li>
		</ol>
    </body>
</Zeta:HTML>