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
            IMPORT js/components/segmentButton/SegmentButton
        </Zeta:Loader>
        <script type="text/javascript">
        	var entityId = '${entityId}';
        	var portProtIndex = '${portProtIndex}';
        	var portTypeIndex = '${portTypeIndex}';
        	var slotIndex = '${slotIndex}';
        	var portIndex = '${portIndex}';
        	var tab;
        	var tab2;
            //取消;
            function cancelClick() {
                top.closeWindow("modalDlg");
            }
            $(function(){
            	var data = [{
            	    name: 'true',
            	    value: '1'
            	},{
            	    name: 'false',
            	    value: '2'
            	}];
            	tab = new SegmentButton('putPort1', data, {
            	    callback: function(value){
            	    	//当级联口为true时，信任口隐藏;
            	        if(value == '1'){
            	        	$('#trustPort').css({display: 'none'});
            	        	$('.jsHide').css({display: ''});
            	        }else{
            	        	$('#trustPort').css({display: ''});
            	        	$('.jsHide').css({display: 'none'});
            	        };
            	    }
            	});
            	tab.init();
            	
            	tab2 = new SegmentButton('putPort2', data, {
            	    callback: function(value){
            	        //alert(value);
            	    }
            	});
            	tab2.init();
            	
            	initData();
            }); //end document.ready;
            
            function initData(){
            	$.ajax({
        			url : '/epon/oltdhcp/loadOltDhcpPort.tv',
        			type : 'POST',
        			data : {
        				entityId : entityId,
        				portProtIndex : portProtIndex,
        				portTypeIndex : portTypeIndex,
        				slotIndex : slotIndex,
        				portIndex : portIndex
        			},
        			dateType : 'json',
        			async: false,
        			success : function(json) {
        				if (json) {
        					$("#portType").val(json.portTypeName)
        					$("#portType").attr("disabled",true);
        					$("#portNo").val(json.portName);
        					$("#portNo").attr("disabled",true);
        					tab.setValue(json.topOltDhcpPortCascade)
        					if(json.topOltDhcpPortCascade == 1){
        						$("#portTrans").val(json.topOltDhcpPortTrans);
        					}else{
        						tab2.setValue(json.topOltDhcpPortTrust);
        					}
        				}
        			},
        			error : function() {},
        			cache : false
        		});
            }
            
            function editFn(){
            	var portCascade = tab.getValue();
            		portTrans = $("#portTrans").val();
            		portTrust = tab2.getValue();
            	window.top.showWaitingDlg("@COMMON.wait@", "@oltdhcp.editPort@....", 'ext-mb-waiting');
        	    $.ajax({
        	        url:"/epon/oltdhcp/modifyOltDhcpPort.tv",
        	        method:"post",
        	        data : {
        				entityId : entityId,
        				portProtIndex : portProtIndex,
        				portTypeIndex : portTypeIndex,
        				slotIndex : slotIndex,
        				portIndex : portIndex,
        				portCascade : portCascade,
        				portTrans : portTrans,
        				portTrust : portTrust
        			},
        	        dataType:'text',
        	        success:function (text) {
        	            window.top.closeWaitingDlg();
        	            top.afterSaveOrDelete({
        	                   title: '@COMMON.tip@',
        	                   html: '<b class="orangeTxt">@oltdhcp.editPort@@oltdhcp.success@！</b>'
        	            });
        	            cancelClick();
        	        },error:function(){
        	        	window.top.closeWaitingDlg();
        	            window.parent.showMessageDlg("@COMMON.tip@", "@oltdhcp.editPort@@oltdhcp.fail@！");
        	        }
        	    });
            }
        </script>
    </head>
    <body class="openWinBody">
        <div class="openWinHeader">
            <div class="openWinTip">
                <p>@oltdhcp.editPort@</p>
            </div>
            <div class="rightCirIco wheelCirIco"></div>
        </div>
        <div class="edgeTB10LR20 pT100">
            <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
            	<tbody>
	                <tr>
	                    <td class="rightBlueTxt" width="140">@oltdhcp.portType@@COMMON.maohao@</td>
	                    <td width="190">
	                        <input id="portType" type="text" class="w180 normalInput" />
	                    </td>
	                    <td class="rightBlueTxt" width="140">@oltdhcp.portNo@@COMMON.maohao@</td>
	                    <td>
	                        <input id="portNo" type="text" class="w180 normalInput" />
	                    </td>
	                </tr>
	                <tr class="darkZebraTr">
	                    <td class="rightBlueTxt" width="140">@oltdhcp.cascadePort@@COMMON.maohao@</td>
	                    <td width="190" id="putPort1" colspan="3">
	                        
	                    </td>
                	</tr>
                </tbody>
                <tbody id="trustPort">
	                <tr>
	                    <td class="rightBlueTxt" width="140">@oltdhcp.trustPort@@COMMON.maohao@</td>
	                    <td colspan="3" id="putPort2">
	                        
	                    </td>
	                </tr>
	            </tbody>
	            <tbody class="jsHide">
	            	<tr>
	            		<td class="rightBlueTxt" width="140">@oltdhcp.cascadeProcess@@COMMON.maohao@</td>
	                    <td colspan="3">
                        	<select id="portTrans" class="w180 normalSel">
                        		<option value="1">transparent</option>
                        		<option value="2">captured</option>
                        	</select>
	                    </td>
	                </tr>
	            </tbody>
            </table>
        </div>
            <div class="noWidthCenterOuter clearBoth">
                <ol class="upChannelListOl pB0 pT110 noWidthCenter">
                    <li>
                        <a href="javascript:;" class="normalBtnBig" onclick="editFn();">
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