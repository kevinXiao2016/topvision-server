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
            IMPORT js/utils/IpUtil
            CSS css/white/disabledStyle
        </Zeta:Loader>
        <script type="text/javascript">
        	var type = '${type}';
        	var entityId = '${entityId}';
        	var relayMode;
        	$(function(){
       			initVifSelect();
       			initGroupSelect();
        		//编辑;
        		if(type == 'edit'){
        			var vifIndex = '${vifIndex}';
        			var opt60StrIndex = '${opt60StrIndex}';
        			$('#btn').html('<span><i class="miniIcoEdit"></i>@COMMON.edit@</span>');
        			//填入相应的数据;
        			initEditData(vifIndex,opt60StrIndex);
        			
        		//添加;	
        		}else{
        			$('#btn').html('<span><i class="miniIcoAdd"></i>@COMMON.add@</span>');
        		}
        	});//end document.ready;
            //取消;
            function cancelClick() {
                top.closeWindow("modalDlg");
            }
            function saveFn(){
            	var vifIndex = $('#vifIndex option:selected').text();
            	var opt60StrIndex = $("#opt60StrIndex").val();
            	if(relayMode == 2){
            		//Option60模式
            		var reg = /^[a-zA-Z0-9-\s_:;\/(),.'"*&^%$#@!=+|?<>\[\]\{\}\\`~]+$/
	                if (opt60StrIndex.length > 32||(opt60StrIndex.length!=0&&!reg.test(opt60StrIndex))) {
	                    $('#opt60StrIndex').focus();
	                    return false;
	                }
            	}
            	var agentAddress = $("#agentAddress").val();
            	var groupIndex = $("#groupIndex").val();
            	//编辑;
        		if(type == 'edit'){
        			window.top.showWaitingDlg("@COMMON.wait@", "@oltdhcp.modifyRelay@....", 'ext-mb-waiting');
        		    $.ajax({
        		        url:"/epon/oltdhcp/modifyOltDhcpVif.tv",
        		        method:"post",
        		        data : {
            				entityId : entityId,
            				vifIndex : vifIndex,
            				opt60StrIndex : opt60StrIndex,
            				vifAgentAddr : agentAddress,
            				vifServerGroup : groupIndex
            			},
        		        cache: false,
        		        dataType:'text',
        		        success:function (text) {
        		            window.top.closeWaitingDlg();
        		            top.afterSaveOrDelete({
        		                   title: '@COMMON.tip@',
        		                   html: '<b class="orangeTxt">@oltdhcp.modifyRelay@@oltdhcp.success@！</b>'
        		            });
        		            cancelClick();
        		        },error:function(){
        		        	window.top.closeWaitingDlg();
        		            window.parent.showMessageDlg("@COMMON.tip@", "@oltdhcp.modifyRelay@@oltdhcp.fail@！");
        		        }
        		    });
        			
        		//添加;	
        		}else{
        			window.top.showWaitingDlg("@COMMON.wait@", "@oltdhcp.addRelay@....", 'ext-mb-waiting');
        		    $.ajax({
        		        url:"/epon/oltdhcp/addOltDhcpVif.tv",
        		        method:"post",
        		        data : {
            				entityId : entityId,
            				vifIndex : vifIndex,
            				opt60StrIndex : opt60StrIndex,
            				vifAgentAddr : agentAddress,
            				vifServerGroup : groupIndex
            			},
        		        cache: false,
        		        dataType:'text',
        		        success:function (text) {
        		            window.top.closeWaitingDlg();
        		            top.afterSaveOrDelete({
        		                   title: '@COMMON.tip@',
        		                   html: '<b class="orangeTxt">@oltdhcp.addRelay@@oltdhcp.success@！</b>'
        		            });
        		            cancelClick();
        		        },error:function(){
        		        	window.top.closeWaitingDlg();
        		            window.parent.showMessageDlg("@COMMON.tip@", "@oltdhcp.addRelay@@oltdhcp.fail@！");
        		        }
        		    });
        			
        		}
            }
            function initVifSelect(){
            	$.ajax({
        			url : '/epon/oltdhcp/loadOltVifList.tv',
        			type : 'POST',
        			data : {
        				entityId : entityId
        			},
        			dateType : 'json',
        			async: false,
        			success : function(json) {
        				if (json) {
        					var vifArray = json.data;
                			for(var i=0;i<vifArray.length;i++){
                				var str = '<option value="{0}" text="{1}">{1}</option>'.format(vifArray[i].interfaceIndex, vifArray[i].interfaceId);
                				$("#vifIndex").append(str);
                			}
                			vifChange();
        				}
        			},
        			cache : false
        		});
            }
            function initGroupSelect(){
            	$.ajax({
        			url : '/epon/oltdhcp/loadOltDhcpGroupList.tv',
        			type : 'POST',
        			async: false,
        			data : {
        				entityId : entityId
        			},
        			dateType : 'json',
        			success : function(json) {
        				if (json) {
        					var groups = json.data;
        					$("#groupIndex").append("<option value=0>@oltdhcp.notBind@</option>");
                			for(var i=0;i<groups.length;i++){
                				$("#groupIndex").append("<option value='"+groups[i].topOltDhcpServerGroupIndex+"'>"+groups[i].topOltDhcpServerGroupIndex+"</option>");
                			}
        				}
        			},
        			cache : false
        		});
            }
            
            function vifChange(){
            	var interfaceIndex = $("#vifIndex").val();
            	var vifIndex = $('#vifIndex option:selected').text();
            	$.ajax({
        			url : '/epon/oltdhcp/loadOltVifIpList.tv',
        			type : 'POST',
        			async: false,
        			data : {
        				entityId : entityId,
        				interfaceIndex : interfaceIndex
        			},
        			dateType : 'json',
        			success : function(json) {
        				if (json) {
        					$("#agentAddress").empty();
        					vifIpArray = json.data;
        					for(var i=0;i<vifIpArray.length;i++){
       		    				$("#agentAddress").append("<option value='"+vifIpArray[i].ipV4Addr+"'>"+vifIpArray[i].ipV4Addr+"</option>");
        	    			}
        				}
        			},
        			cache : false
        		});
            	$.ajax({
        			url : '/epon/oltdhcp/loadOltDhcpVlan.tv',
        			type : 'POST',
        			data : {
        				entityId : entityId,
        				vlanIndex : vifIndex
        			},
        			dateType : 'json',
        			async: false,
        			success : function(json) {
        				if (json) {
        					relayMode = json.topOltDhcpVLANRelayMode;
        					if(relayMode == 2){
        						//Option60 最多10条relay
        						if(type == 'add'){
	        						if(json.option60Count < 10){
	        							$("#btn").attr("disabled",false);
	        						}else{
	        							$("#btn").attr("disabled",true);
	        						}
        						}
        						$("#opt60StrTr").show();
        					}else{
        						//Standard 最多1条relay
        						if(type == 'add'){
	        						if(json.option60Count < 1){
	        							$("#btn").attr("disabled",false);
	        						}else{
	        							$("#btn").attr("disabled",true);
	        						}
        						}
        						$("#opt60StrIndex").val("");
        						$("#opt60StrTr").hide();
        					}
        				}
        			},
        			cache : false
        		});
            }
            
            function initEditData(vifIndex,opt60StrIndex){
            	$.ajax({
        			url : '/epon/oltdhcp/loadOltDhcpVif.tv',
        			type : 'POST',
        			async: false,
        			data : {
        				entityId : entityId,
        				vifIndex : vifIndex,
        				opt60StrIndex : opt60StrIndex
        			},
        			dateType : 'json',
        			success : function(json) {
        				if (json) {
        					var vifIndex = json.topOltDhcpVifIndex;
        					$("#vifIndex").find("option[text='"+ vifIndex +"']").attr("selected",true);
        					vifChange();
        					$("#vifIndex").attr("disabled",true);
        					$("#opt60StrIndex").val(json.opt60StrDisplay);
        					$("#opt60StrIndex").attr("disabled",true);
        					$("#agentAddress").val(json.topOltDhcpVifAgentAddr);
        					$("#groupIndex").val(json.topOltDhcpVifServerGroup);
        				}
        			},
        			cache : false
        		});
            }
        </script>
    </head>
    <body class="openWinBody">
        <div class="openWinHeader">
            <div class="openWinTip">@oltdhcp.addRelayTip@</div>
            <div class="rightCirIco pageCirIco"></div>
        </div>
        <div class="edgeTB10LR20 pT20">
            <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td class="rightBlueTxt" width="210">@oltdhcp.vlanifid@@COMMON.maohao@</td>
                    <td>
                        <select id="vifIndex" onchange="vifChange();" class="w180 normalSel"></select>
                    </td>
                </tr>
                <tr id= "opt60StrTr" class="darkZebraTr">
                    <td class="rightBlueTxt">@oltdhcp.option60str@@COMMON.maohao@</td>
                    <td>
                        <input id="opt60StrIndex" type="text" class="w180 normalInput"  maxlength="32" toolTip='@oltdhcp.option60strTip@'/>
                    </td>
                </tr>
                <tr>
                    <td class="rightBlueTxt">@oltdhcp.agentAddr@@COMMON.maohao@</td>
                    <td>
                        <select id= "agentAddress" class="w180 normalSel"></select>
                    </td>
                </tr>
                <tr class="darkZebraTr">
                    <td class="rightBlueTxt">@oltdhcp.dhcpGroup@@COMMON.maohao@</td>
                    <td>
                        <select id="groupIndex" class="w180 normalSel"></select>
                    </td>
                </tr>
            </table>
        </div>
            <div class="noWidthCenterOuter clearBoth">
                <ol class="upChannelListOl pB0 pT20 noWidthCenter">
                    <li>
                        <a href="javascript:;" class="normalBtnBig" id="btn" onclick="saveFn()">
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