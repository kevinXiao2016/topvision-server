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
        </Zeta:Loader>
        <script type="text/javascript">
        	var type = '${type}';
        	var entityId = '${entityId}';
        	var groupIndex;
        	$(function(){
        		//编辑;
        		if(type == 'edit'){
        			$('#btn').html('<span><i class="miniIcoEdit"></i>@COMMON.edit@</span>');
        			//填入相应的数据;
        			groupIndex = '${groupIndex}';
        			initData();
        		//添加;	
        		}else{
        			$('#btn').html('<span><i class="miniIcoAdd"></i>@COMMON.add@</span>');
        			var availableGroupId = '${availableGroupId}';
        			var ids = availableGroupId.split(",");
        			for(var i=0;i<ids.length;i++){
        				$("#groupIndex").append("<option value='"+ids[i]+"'>"+ids[i]+"</option>");
        			}
        		}
        	});//end document.ready;
        	
        	//验证IP组;
        	function validateIpGroup(value){
        		var arr = value.split(/[,]/),
        		    flag = true;
        		
        		//去重复;
        		arr = $.unique(arr);
        		if(arr.length > 5){ return false;}
        		$.each(arr, function(i, v){
        			//去除非法ip
        			if(!IpUtil.isIpv4Address(v) || !IpUtil.isValidDeviceIp(v)){
        				flag = false;
        				return false;
        			}
        		});
        		return flag;
        	}
        	//保存;
        	function saveFn(){
        		var ipGroup = $('#ipGroup').val();
        		if(!validateIpGroup(ipGroup)){
        			$('#ipGroup').focus();
        			return;
        		}
        		var groupIndex = $("#groupIndex").val();
        		if(type == 'edit'){
        			window.top.showWaitingDlg("@COMMON.wait@", "@oltdhcp.modifyGroup@....", 'ext-mb-waiting');
        		    $.ajax({
        		        url:"/epon/oltdhcp/modifyOltDhcpGroup.tv",
        		        method:"post",
        		        data : {
            				entityId : entityId,
            				groupIndex : groupIndex,
            				serverIpsDisplay : ipGroup
            			},
        		        cache: false,
        		        dataType:'text',
        		        success:function (text) {
        		            window.top.closeWaitingDlg();
        		            top.afterSaveOrDelete({
        		                   title: '@COMMON.tip@',
        		                   html: '<b class="orangeTxt">@oltdhcp.modifyGroup@@oltdhcp.success@！</b>'
        		            });
        		            cancelClick();
        		        },error:function(){
        		        	window.top.closeWaitingDlg();
        		            window.parent.showMessageDlg("@COMMON.tip@", "@oltdhcp.modifyGroup@@oltdhcp.fail@！");
        		        }
        		    });
        		}else{
        			window.top.showWaitingDlg("@COMMON.wait@", "@oltdhcp.addGroup@....", 'ext-mb-waiting');
        		    $.ajax({
        		        url:"/epon/oltdhcp/addOltDhcpGroup.tv",
        		        method:"post",
        		        data : {
            				entityId : entityId,
            				groupIndex : groupIndex,
            				serverIpsDisplay : ipGroup
            			},
        		        cache: false,
        		        dataType:'text',
        		        success:function (text) {
        		            window.top.closeWaitingDlg();
        		            top.afterSaveOrDelete({
        		                   title: '@COMMON.tip@',
        		                   html: '<b class="orangeTxt">@oltdhcp.addGroup@@oltdhcp.success@！</b>'
        		            });
        		            cancelClick();
        		        },error:function(){
        		        	window.top.closeWaitingDlg();
        		            window.parent.showMessageDlg("@COMMON.tip@", "@oltdhcp.addGroup@@oltdhcp.fail@！");
        		        }
        		    });
        		}
        	}
            //取消;
            function cancelClick() {
                top.closeWindow("modalDlg");
            }
            
            function initData(){
            	$.ajax({
        			url : '/epon/oltdhcp/loadOltDhcpGroup.tv',
        			type : 'POST',
        			data : {
        				entityId : entityId,
        				groupIndex : groupIndex
        			},
        			dateType : 'json',
        			success : function(json) {
        				if (json) {
        					$("#groupIndex").append("<option value='"+json.topOltDhcpServerGroupIndex+"'>"+json.topOltDhcpServerGroupIndex+"</option>");
        					$("#groupIndex").attr("disabled",true);
        					$("#ipGroup").val(json.serverIpsDisplay);
        				}
        			},
        			error : function() {
        			},
        			cache : false
        		});
            }
        </script>
    </head>
    <body class="openWinBody">
        <div class="openWinHeader">
            <div class="openWinTip">
                <p>@oltdhcp.groupTip@</p>
            </div>
            <div class="rightCirIco pageCirIco"></div>
        </div>
        <div class="edgeTB10LR20 pT30">
            <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
                <tbody>
                    <tr>
                        <td class="rightBlueTxt" width="180">@COMMON.required@@oltdhcp.groupId@@COMMON.maohao@</td>
                        <td>
                            <select id="groupIndex" class="w200 normalSel"></select>
                        </td>
                    </tr>
                    <tr class="darkZebraTr">
                        <td class="rightBlueTxt" width="180">@COMMON.required@@oltdhcp.serverIp@@COMMON.maohao@</td>
                        <td>
                            <textarea id="ipGroup" tooltip="@oltdhcp.groupTip@" rows="3" style="height:80px" class="w200 normalInput"></textarea>
                        </td>
                    </tr>
                </tbody>
            </table>
            <div class="noWidthCenterOuter clearBoth">
                <ol class="upChannelListOl pB0 pT40 noWidthCenter">
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