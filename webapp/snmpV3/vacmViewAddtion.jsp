<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module snmpV3
    import snmpV3.javascript.Adapter
</Zeta:Loader>
<script type="text/javascript">
  var entityId = ${entityId};
  var thisStep = 0;
  var useExistedGroup = true;
  var snmpViewName = "${view.snmpViewName}";
  var snmpViewSubtree = "${view.snmpViewSubtree}";
  var snmpViewMask = "${view.snmpViewMask}";
  var snmpViewMode = "${view.snmpViewMode}";
/***********************************
                             关闭页面
 ************************************/
function closeHandler(){
    window.parent.closeWindow('viewAdditionWizard');
}

$(document).ready(function(){
	if(snmpViewName != ""){
		$("#viewName").val(snmpViewName).attr();
		$("#subtree").val(subtreeMerge(snmpViewSubtree,snmpViewMask));
		$("#viewMode").val(snmpViewMode);
		$("#nextBt").html(I18N.COMMON.modify);
		$("#viewHead").html(I18N.VIEW.modifyView);
		$("#viewName").attr("disabled",true);
	}
});

function subtreeMerge(tree,mask){
    var re = [];
    mask = parseInt(mask, 16).toString(2);
    tree = tree.split(".");
    var len = tree.length;
    var m = mask.length;
    for(var a=0; a<len; a++){
        if(a < m && mask.substring(a, a + 1) == '0'){
            re.push("*");
            continue;
        }
        re.push(tree[a]);
    }
    return re.join(".");
}

/////验证subtree格式是否满足要求
function checkSubtreeMask(string){
	var reg = /^[\d*.]+$/ig;
	var reg2 = /^[.*]{0,1}[.*]+$/ig;
	if(!reg.exec(string) ||  string.length > 127 || string.indexOf("..") > -1 || reg2.exec(string)){
		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VIEW.wrongSubtree, "tip", function(){
	        $("#subtree").focus();
	    });
	    return false;
	}
	var tmp = string.split("*.");
	for(var a=0; a<tmp.length; a++){
	    if(tmp[a].indexOf("*") > -1){
	        if(a == tmp.length - 1 && tmp[a].indexOf("*") == tmp[a].length - 1){
	            continue;
	        }
	        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VIEW.wrongSubtree2 , 'tip', function(){
	            $("#subtree").focus();
	        });
	        return false;
	    }
	}
	tmp = string.split(".*");
	for(a=0; a<tmp.length; a++){
	    if(tmp[a].indexOf("*") > -1){
	    	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VIEW.wrongSubtree2 ,'tip', function(){
	            $("#subtree").focus();
	        });
	        return false;
	    }
	}
	tmp = string.split(".");
    for(a=0; a<tmp.length; a++){
    	if(typeof tmp[a] == 'string' && tmp[a].length > 10){
            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VIEW.subtreeNodeLong ,'tip', function(){
                $("#subtree").focus();
            });
            return false;
        }
    }
	
	return true;
}

///////////////////// 立即添加视图  //////////////////////////////
function saveHandler(){
    var _viewName = $("#viewName").val();
    var _subtree = $("#subtree").val();
    var _viewMode = $("#viewMode").val();
    if(!_viewName){
    	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VIEW.plsIptViewName ,'error',function(){
            $("#viewName").focus();
        });
    	return;
    }
    if(!_subtree){
         window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VIEW.wrongSubtree2 ,'error',function(){
             $("#subtree").focus();
         });
         return;
    }
    if(!checkSubtreeMask(_subtree)){
        return;
    }
    var rear = _subtree.substring(_subtree.length-1);
    if(rear == "."){
    	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VIEW.filterWrong ,'error',function(){
            $("#subtree").focus();
        });
    	return;
    }
    var data = {};
    data.entityId = entityId;
    data.snmpViewName = _viewName;
    data.snmpViewMode = _viewMode;
    if(snmpViewName != ""){//修改
    	data.snmpPrvSubtree = snmpViewSubtree;
        data.snmpViewSubtree = _subtree;
        window.top.showWaitingDlg(I18N.COMMON.wait, I18N.VIEW.mdfingView, 'ext-mb-waiting');
    	$.ajax({
            url : '/snmp/modifySnmpV3View.tv',cache:false,
            data : data,
            success : function(){
                window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VIEW.mdfViewOk );
                closeHandler.apply(this);
            },error : function(){
                window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VIEW.mdfViewEr );
            }
        });
    }else{//新建
    	data.snmpViewSubtree = _subtree;
    	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.VIEW.creatingView, 'ext-mb-waiting');
    	$.ajax({
            url : '/snmp/addView.tv',cache:false,
            data : data,
            success:function(){
                window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VIEW.createViewOk );
                closeHandler.apply(this);
            },error : function(){
                window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VIEW.createViewEr);
            }
        });    	
    }
}
</script>
</head>
<body class="openWinBody" >
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@VIEW.viewTip@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
    <!-- GROUP CONFIG LAYOUT -->
    	<div class="edge10">
		<form onsubmit="return false;">
			<table class="mCenter zebraTableRows" >
            <tr>
                <td class="w220 rightBlueTxt">@VIEW.viewName@</td>
                <td class="valueClazz">
                   <input type="text" maxlength="31" id="viewName" class="normalInput" tooltip="@VIEW.viewNameTip@" />
                </td>
            </tr>
            <tr class="darkZebraTr">
                <td class="rightBlueTxt" >@VIEW.filterCondition@</td>
                <td class="valueClazz">
                   <input type="text" id="subtree" class="normalInput" tooltip="@VIEW.filterConditionTip@" />
                </td>
            </tr>
            <tr>
                <td class="rightBlueTxt" >@VIEW.execAction@</td>
                <td class="valueClazz">
                   <select id="viewMode"  class="normalSel">
                       <option value="1">@VIEW.include@</option>
                       <option value="2">@VIEW.exclude@</option>
                   </select>
                </td>
            </tr>
        </table>
        </form>
     </div>
    <p style="margin-left: 100px;">@VIEW.viewNotation@</p>
    
    <Zeta:ButtonGroup>
		<Zeta:Button onClick="saveHandler()" icon="miniIcoAdd">@COMMON.add@</Zeta:Button>
		<Zeta:Button onClick="closeHandler()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>
