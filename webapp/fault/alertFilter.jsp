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
            module fault
            CSS css/white/disabledStyle
            CSS css/jquery.treeview
            IMPORT js/jquery/jquery.treeview
            IMPORT js/tools/treeBuilder
        </Zeta:Loader>
        <style type="text/css">
            #putTree{ height:345px; padding-top:5px; padding-left:10px; overflow:auto; border:1px solid #ccc; background:#fff;}
            .putLinks a{ color:#069;}
            .putLinks a:hover{ color:#f60;}
        </style>
        <script type="text/javascript">
	        var onuLevelArray = {
	            0: '@fault/Alert.default@',
	            1: '@fault/Alert.importantONU@',
	            2: '@fault/Alert.commonMDU@',
	            3: '@fault/Alert.commonSFU@'
	        };
	        var action = '${action}';
	        var alertFilter = {};
	        var alertFilterJson = '${alertFilterJson}';
	        var error = false;
	        if(alertFilterJson) {
		        try{
		        	alertFilter = JSON.parse(alertFilterJson);
		        }catch(e) {
		        	error = true;
		        	window.top.showMessageDlg("@COMMON.tip@", "@fault/Alert.errorTip@");
		        }
	        }
        
            $(function(){
		        if(error){
		        	return;
		        }
		        // 初始化
		        alertFilter.name && $('#nameInput').val(alertFilter.name);
		        alertFilter.ip && $('#nameInput').val(alertFilter.name);
		        if(alertFilter.name) {
		        	$('#nameInput').val(alertFilter.name);
		        }
		        if(alertFilter.ip) {
		        	$('#ipBox').attr('checked', 'checked');
                    $('#ipInput').removeAttr('disabled').val(alertFilter.ip);
                }
		        if(alertFilter.onuLevel!== null && alertFilter.onuLevel !== undefined && alertFilter.onuLevel !== -1) {
		        	$('#levelBox').attr('checked', 'checked');
                    $('#onuLevel').removeAttr('disabled').val(alertFilter.onuLevel);
                }
                showAlertType();
                bindCkBox();
            });
            //构造告警类型树结构
            function showAlertType(){
                //清空树
                $('ul#tree').empty();
                $.ajax({
                    url: '/fault/loadAlertTypeFilter.tv',
                    type: 'POST',
                    dataType:"json",
                    async: 'false',
                    success: function(jsonAlertType) {
                        //循环输出各父节点
                        $.each(jsonAlertType, function(index,rootAlert){
                            bulidTree(rootAlert, '');
                        });
                        treeBasicHandle();
                        
                        //TODO 如果是编辑，初始化选中状态
                        alertFilter.typeIds && selectNodes(alertFilter.typeIds.split(','));
                    }, error: function(array) {
                    }, cache: false,
                    complete: function (XHR, TS) { XHR = null }
                });
            }
            //保存;
            function saveClick() {
                //验证名称
                var name = $('#nameInput').val();
                if(!V.isAnotherName(name)){
                    return $('#nameInput').focus();
                }
                
                //如果勾选了IP，校验IP规则
                var data = {
                    'alertFilter.name': name
                };
                
                // 如果是编辑
                if(action==='edit') {
                	data['alertFilter.filterId'] = alertFilter.filterId;
                }
                
                var ip = $('#ipInput').val();
                if($('#ipBox').prop('checked')) {
                    if(!top.IpUtil.isIpAddress(ip)) {
                        return $('#ipInput').focus();
                    }
                    data['alertFilter.ip'] = ip;
                }
                
                //如果勾选了服务等级
                if($('#levelBox').prop('checked')) {
                    data['alertFilter.onuLevel'] = $('#onuLevel').val();
                }
                //获取需要过滤的告警
                var typeIds = getSelectedAlertType();
                if(typeIds.length === 0) {
                    return window.top.showMessageDlg("@COMMON.tip@", "@fault/Alert.choseAlertFilter@");
                }
                data['alertFilter.typeIds'] = typeIds.join(',');
                
                //生成告警描述
                var note = "";
                if($('#ipBox').prop('checked') && $('#levelBox').prop('checked')) {
                    note = String.format("@fault/Alert.filtering1@", ip, onuLevelArray[$('#onuLevel').val()]);
                } else if($('#ipBox').prop('checked') && !$('#levelBox').prop('checked')) {
                    note = String.format("@fault/Alert.filtering2@", ip);
                } else if(!$('#ipBox').prop('checked') && $('#levelBox').prop('checked')) {
                    note = String.format("@fault/Alert.filtering3@", onuLevelArray[$('#onuLevel').val()]);
                } else if(!$('#ipBox').prop('checked') && !$('#levelBox').prop('checked')) {
                    note = "@fault/Alert.filtering0@";
                }
                data['alertFilter.note'] = note;
                
                //typeIdsStr
                var url = action==='add' ? '/fault/addAlertFilter.tv' : '/fault/modifyAlertFilter.tv';
                
                $.ajax({
                    url: url,
                    type: 'POST',
                    data: data,
                    dataType:"json",
                    async: 'false',
                    success: function() {
                        cancelClick()
                    }, error: function() {
                    	
                    }, cache: false,
                    complete: function (XHR, TS) { XHR = null }
                });
            }
            //取消;
            function cancelClick() {
                top.closeWindow("modalDlg");
            }
            //全选;
            function selectAll(b){
                if(b){
                    $('#tree').find(':checkbox').attr({checked: 'checked'});
                }else{
                    $('#tree').find(':checkbox').removeAttr('checked');
                }
            }
            function bindCkBox(){
                $('#ipBox').click(function(){
                    if($(this).prop('checked')){
                        $('#ipInput').removeAttr('disabled').focus();
                    }else{
                        $('#ipInput').attr({disabled: 'disabled'});
                    }
                });
                
                $('#levelBox').click(function(){
                    if($(this).prop('checked')){
                        $('#onuLevel').removeAttr('disabled');
                    }else{
                        $('#onuLevel').attr({disabled: 'disabled'});
                    }
                });
            }
            function getSelectedAlertType() {
                //获取勾选的节点
                var alertTypeList = [];
                var selectNodes = $('#tree').find(':checkbox:checked');
                $.each(selectNodes, function(index, node) {
                    alertTypeList.push(node.id);
                })
                return alertTypeList;
            }
            function selectNodes(typeIds) {
            	$.each(typeIds, function(index, typeId) {
            		selectNode(typeId);
            	});
            }
            function selectNode(typeId) {
            	$('#tree').find(':checkbox[id=' + typeId + ']').attr({checked: 'checked'});
            }
        </script>
    </head>
    <body class="openWinBody">
        <div class="edgeTB10LR20 pT20">
            <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
                <tbody>
                    <tr>
                        <td class="rightBlueTxt" colspan="2">@fault/ALERT.name@@COMMON.maohao@<label class=red-tip >*</label></td>
                        <td colspan="4">
                            <input id="nameInput" type="text" class="w180 normalInput" tooltip="@COMMON.anotherName@" />
                        </td>
                    </tr>
                    <tr class="darkZebraTr">
                        <td width="30" align="right">
                            <input type="checkbox" id="ipBox" />
                        </td>
                        <td class="rightBlueTxt" width="20">IP@COMMON.maohao@</td>
                        <td>
                            <input id="ipInput" type="text" class="w180 normalInput" disabled='disabled' tooltip="@fault/Alert.enterRightIP@" />
                        </td>
                        <td width="20" align="right">
                            <input type="checkbox" id="levelBox" />
                        </td>
                        <td class="rightBlueTxt" width="80">@fault/Alert.serviceLevel@@COMMON.maohao@</td>
                        <td>
                            <select class="normalSel w180" id="onuLevel" disabled="disabled">
                                <option value="0">@fault/Alert.default@</option>
                                <option value="1">@fault/Alert.importantONU@</option>
                                <option value="2">@fault/Alert.commonMDU@</option>
                                <option value="3">@fault/Alert.commonSFU@</option>
                            </select>
                        </td>
                    </tr>
                </tbody>
            </table>
            <p class="pT10 pB5">@fault/Alert.choseAlertFilter@<label class=red-tip >*</label></p>
            <div id="putTree">
                <ul id="tree" class="filetree">
                    
                </ul>
            </div>
            <p class="pT5 putLinks"><a href="javascript:;" class="pR10" onclick="selectAll(true)">@fault/Alert.choseAll@</a> <a href="javascript:;" class="pR10" onclick="selectAll(false)">@fault/Alert.choseNone@</a></p>
            <div class="noWidthCenterOuter clearBoth">
                <ol class="upChannelListOl pB0 pT10 noWidthCenter">
                    <li>
                        <a href="javascript:;" class="normalBtnBig" onclick="saveClick()">
                            <span><i class="miniIcoData"></i>@COMMON.save@</span>
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