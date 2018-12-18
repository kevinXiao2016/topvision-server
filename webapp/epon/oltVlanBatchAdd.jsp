<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module epon
</Zeta:Loader>
<style type="text/css">
.vlanIdCell{border:1px solid #8bb8f3;width:150;margin-left:25;}
.rowClazz{border-top:1px solid #8bb8f3;height:35px;padding-top:5px;}
.selClass{width:125px;}
#vlanTable{overflow:scroll; width: 100%; height: 300px;}
td,input,select{text-align:center;}
.tableHeader{background-color: #ccc; }
#batchAddArea{
	height: 260px;
	overflow:auto;
	border: 1px solid #ccc;
}
.zebraTableRows td{
	padding: 0;
	padding-top: 2px;
}
.zebraTableRows .upChannelListOl{
	padding-bottom: 2px;
}
</style>
<script type="text/javascript">
var entityId = '<s:property value="entityId"/>';
var vidListStr = '<s:property value = "vidListStr"/>';
var vidList = vidListStr ? changeToArray(vidListStr) : new Array();
var oldList = ${vidListJson};
if(oldList.join("") == "false"){
	oldList = new Array();
}else{
	for(var a=0; a<oldList.length; a++){
		oldList[a] = parseInt(oldList[a]);
	}
}
var isAdding = false;
var isFirstAdding = true;

var selAllTmp = 1;
var nameAllTmp = "VLAN_#ID#";
var wrongNameList = new Array();

//添加成功和失败的标记
var batchSuc = 0;
var batchFailed = 0;

function initData(){
	if(oldList.length > 0){
		for(var x=0; x<vidList.length; x++){
			if(oldList.indexOf(parseInt(vidList[x])) > -1){
				vidList.splice(x, 1);
				x--;
			}
		}
	}
}
function lazyAddDiv(list){
	if(list.length > 30){
		setTimeout(function(){
			for(var i=0; i<30; i++){
				addDiv(list[i]);
			}
			list.splice(0, 30);
			lazyAddDiv(list);
		}, 100);
	}else if(list.length > 0){
		setTimeout(function(){
			addDiv(list[0]);
			list.splice(0, 1);
			lazyAddDiv(list);
		}, 25);
	}
}

var vlanOption = [];
vlanOption[vlanOption.length] = "<option value=1>always flooding</option>";
vlanOption[vlanOption.length] = "<option value=2>unknown flooding</option>";
vlanOption[vlanOption.length] = "<option value=3>no flooding</option>";

//构造待添加列表中的各行数据
function addDiv(rowNum){
	var str = "<tr id=div" + rowNum + " onmouseover='over(" + rowNum + ")' onmouseout='out(" + rowNum + ")'></tr>";
	$("#vlanTable").append(str);
	var p = $("#div" + rowNum);
	str = "<td width='150'>" + rowNum + "</td>";
	str += String.format("<td width='160'><input id=name{0} onblur='inputKeyup({0})' onkeyup='inputKeyup({0})' class='vlanIdCell' " +
			"maxlength=31 value='{1}' title='@VLAN.batchInputTip@' /></td>",
			rowNum, nameAllTmp.replace(/[#][I][D][#]/g, rowNum) );
	str += "<td width='180'><select class=selClass id=sel" + rowNum + ">";
	str += vlanOption[selAllTmp];
	for(var a=0; a<3; a++){
		if(a != selAllTmp){
			str += vlanOption[a];
		}
	}
	str += "</select></td>";
	str += String.format("<td><div class='noWidthCenterOuter clearBoth'><ol class='upChannelListOl noWidthCenter'><li><a href='javascript:;' class='normalBtn'  id=bt{0} onclick='btClick({0})'><span>{1}</span></a></li></ol></div></td>", 
			rowNum, '@COMMON.delete@');
	p.append(str);
}
function btClick(v){
	$("#div" + v).attr("disabled", true).mouseout().slideUp(500);
	setTimeout(function(){
		$("#div" + v).stop().remove();
		vidList.splice(vidList.indexOf(parseFloat(v)), 1);
	}, 300);
}
function inputKeyup(v){
	var p = $("#name" + v);
	var name = p.val();
	var n = wrongNameList.indexOf(v);
	if(checkVlanName(name)){
		p.css("border", "1px solid #8bb8f3");
		if(n > -1){
			wrongNameList.splice(n, 1);
		}
	}else{
		p.css("border", "1px solid #ff0000");
		if(n == -1){
			wrongNameList.push(v);
		}
	}
}
function over(v){
	$("#div" + v).css("background-color", "#f1fefe");
}
function out(v){
	$("#div" + v).css("background-color", "transparent");
}
Ext.onReady(function(){
	initData();
	var tmpList = vidList.slice(0);
	lazyAddDiv(tmpList);
	
	$("#continueBt").addClass("disabledAlink");
});
function checkVlanName(v){
	var reg1 = /^([a-zA-Z0-9_\s\d]{1})+$/;
	if(v == "" || v == null || v.length > 31){
		return false;
	}else{
		return reg1.exec(v);
	}
}
//解析逗号和连字符组成的字符串为数组
function changeToArray(v){
	var re = new Array();
	var t = v.split(",");
	var tl = t.length;
	for(var i=0; i<tl; i++){
		var tt = t[i];
		var ttI = tt.indexOf("-");
		if(ttI > 0){
			var ttt = tt.split("-");
			if(ttt.length == 2){
				var low = parseFloat(parseFloat(ttt[0]) > parseFloat(ttt[1]) ? ttt[1] : ttt[0]);
				var tttl = Math.abs(parseFloat(ttt[0]) - parseFloat(ttt[1]));
				for(var u=0; u<tttl + 1; u++){
					re.push(low + u);
				}
			}
		}else if(ttI == -1){
			re.push(parseFloat(tt));
		}
	}
	var rel = re.length;
	if(rel > 1){
		var o = {};
		for(var k=0; k<rel; k++){
			o[re[k]] = true;
		}
		re = new Array();
		for(var x in o){
			if (x > 0 && o.hasOwnProperty(x)) {
				re.push(parseFloat(x)); 
			}
		}
		re.sort(function(a, b){
			return a - b;
		});
	}
	return re;
}

//开始添加
function saveClick(){
	//点击开始添加后,隐藏开始添加按钮,显示中止添加按钮
	$("#stopAdd").css({display:'block'});
	$("#startAdd").css({display:'none'});
	//$("#saveBt").find("span").text(I18N.VLAN.batchContinueAdd);
	
	var vidL = vidList.length;
	//如果当前没有需要添加的,就退出添加页面
	if(vidL == 0){
		cancelClick();
		return;
	}
	if(wrongNameList.length > 0){
		$("#name" + wrongNameList[0]).focus();
		return;
	}
	isAdding = true;
	isFirstAdding = false;
	//开始添加后,在按钮栏显示 添加VLAN中,请不要关闭本页面  文本
	$("#theTips").html("<b id=noCloseTipFont class='orangeTxt'>" + I18N.VLAN.batchAddingTip + "</b>").css({display:"block"});
	var leftPos = (800 - $("#theTips").width()) / 2;
	$("#theTips").css("left",leftPos);//设置提示框居中;	
	if($("#saveBt").find("span").text() == I18N.VLAN.batchReAdd || $("#saveBt").find("span").text() == I18N.VLAN.batchContinueAdd){
		$(".failedFontClass").remove();
		//$("#failFont").text("0");
		batchFailed = 0;
	}else {
		$("#saveBt").find("span").text(I18N.VLAN.batchContinueAdd);
		//开始添加后,在上方显示添加成功数目和添加失败数目
		$("#successTip").html(I18N.VLAN.batchSuc + ":<b id=sucFont style='font:18px Georgia,Arial;margin-right:30px; color:green;'>"+batchSuc+"</b>" +
				I18N.VLAN.batchFailed + ":<b style='font:18px Georgia,Arial; color:red;' id=failFont color=red>"+batchFailed+"</b>")
	}
	var tmpList = vidList.slice(0);
	addVlanMgmt(tmpList);
}
function addVlanMgmt(list){
	if(list.length > 0 && isAdding){   //如果有需要添加的并且isAdding为true 时就执行添加动作
		addVlan(list);
	}else{
		 //如果没有需要添加的或者 isAdding为false时,先将 添加VLAN中,请不要关闭本页面 提示移除
		$("#noCloseTipFont").remove();
		$("#theTips").css("display","none");
		
		if(vidList.length > 0){
			//如果还有需要添加的(对应添加失败的情况),将添加按钮上的文字改为 重新添加
			var str = I18N.VLAN.batchReAdd;
			if(!isAdding){
				//如果是点击了 中止添加,将添加按钮上的文字改为 继续添加
				str = I18N.VLAN.batchContinueAdd;
			}
			//将更改文字后的添加按钮显示
			$("#saveBt").find("span").text(str).end().css("display","block");
		}
		isAdding = false;
		$("#stopAdd").css({display:'none'});
	}
}
//提前后台进行操作
function addVlan(list){
	//取出当前要添加的vlan序号
	var v = list[0];
	//移除当前要修改的一条记录最后一列中的按钮;
	$("#div" + v).find("td:last").empty();
	//修改待添加列表中对应vlan后的提示信息为 正在添加
	$("#div" + v).find("td:last").focus().html(String.format("<font id=tmpFont style='margin-left:20px;'>@VLAN.batchAdding@</font>", v));
	Ext.Ajax.request({
		url:"/epon/vlan/addOltVlan.tv?entityId=" + entityId + "&vlanIndex=" + v + "&oltVlanName=" + $("#name" + v).val() +
			"&topMcFloodMode=" + $("#sel" + v).val(),
		method:"post",
		success:function(response){
		    if(response.responseText == "success"){
		    	addVlanSuccess(list);		    	
			}else{				
				addVlanFailed(response.responseText, list);
			}
		},failure:function (response) {			
			addVlanFailed(response.responseText, list);
	    }
	});
}
//添加失败的处理
function addVlanFailed(response, list){
	var v = list[0];  //取出当前添加失败的vlan序号
	$("#tmpFont").remove();  //将之前 正在添加 的提示信息去掉
	//将对应列表中的vlan后的提示信息改为 添加失败
	$("#div" + v).find("td:last").html("<font class=failedFontClass  color=red>" + 
			I18N.VLAN.batchAddFailed + "</font>"); 
	//将添加失败数加1
	batchFailed = batchFailed + 1;
	$("#failFont").text(batchFailed);
	//剔除添加失败的
	list.splice(0, 1);
	//继续添加下一个vlan
	addVlanMgmt(list);
}

//添加成功的处理
function addVlanSuccess(list){
	//取出当前添加成功的vlan序号
	var v = list[0];
	//将之前正在添加的提示信息去掉
	$("#tmpFont").remove();
	//将对应列表中的vlan后的提示信息改为 添加成功
	$("#div" + v).find("td:last").html("<font color=green>" + I18N.VLAN.batchAddSuc + "</font>");
	//更改添加成功的vlan项的输入框的样式
	$("#name" + v).focus(function(){$(this).blur();}).css("border", "1px solid #cccccc")
		.blur(function(){$(this).css("border", "1px solid #cccccc")});
	//更改添加成功的vlan项的下拉框为不可用
	$("#sel" + v).attr("disabled", true);
	//将添加成功数加1
	batchSuc = batchSuc + 1;
	$("#sucFont").text(batchSuc);
	//从原始添加列表中删除添加成功的
	vidList.splice(vidList.indexOf(parseInt(v)), 1);
	//从当前添加列表剔除添加成功的
	list.splice(0, 1);
	//继续添加下一个vlan
	addVlanMgmt(list);
}

//点击取消时的操作
function cancelClick() {
	if(isAdding){  //如果正在添加中,就需要用户进行确认是否退出
		window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.VLAN.batchCloseTip, function(type) {
			if (type == 'no'){return;}
			_cancekClick();
		});
	}else{
		_cancekClick();
	}
}
function _cancekClick(){
	isAdding = false;
	//window.parent.getFrame("entity-" + entityId).location.reload();
    window.parent.closeWindow('oltVlanBatchAdd');
}
function allInputChange(){
	var v = $("#allInput").val();
	var t = v.replace(/[#][I][D][#]/g, "1111");
	var vidL = vidList.length;
	if(checkVlanName(t) && vidL > 0){
		var tmpList = vidList.slice(0);
		nameAllTmp = v;
		$("#name" + tmpList[0]).val(v.replace(/[#][I][D][#]/g, tmpList[0]));
		tmpList.splice(0, 1);
		_allInputChange(tmpList, v);
	}
}
function _allInputChange(list, v){
	var ll = list.length;
	if(ll > 0){
		setTimeout(function(){
			ll = ll > 100 ? 100 : ll;
			for(var a=0; a<ll; a++){
				var p = $("#name" + list[a]);
				if(p){
					p.val(v.replace(/[#][I][D][#]/g, list[a]));
				}else{
					return;
				}
			}
			list.splice(0, ll);
			_allInputChange(list, v);
		}, 100);
	}
}
function allInputEnter(){
	var key = window.event.keyCode;
	if(key == 13){
		allInputChange();
	}
}
function allInputKeyup(){
	var v = $("#allInput").val();
	var t = v.replace(/[#][I][D][#]/g, "1111");
	$("#allInput").css("border", "1px solid #" + (checkVlanName(t) ? "8bb8f3" : "ff0000"));
}
function allSelectChange(){
	var v = $("#allSelect").val();
	selAllTmp = parseInt(v) - 1;
	var tmpList = vidList.slice(0);
	_allSelectChange(tmpList, v);
}
function _allSelectChange(list, v){
	var ll = list.length;
	if(ll > 0){
		setTimeout(function(){
			ll = ll > 100 ? 100 : ll;
			for(var a=0; a<ll; a++){
				var p = $("#sel" + list[a]);
				if(p){
					p.val(v);
				}else{
					return;
				}
			}
			list.splice(0, ll);
			_allSelectChange(list, v);
		}, 100);
	}
}
function endClick(){
	isAdding = false;
	//点击中止添加后,隐藏中止添加按钮,显示继续添加按钮
	$("#stopAdd").css("display","none");
	$("#startAdd").css("display","block");
}
</script>
</head>
<body class=openWinBody>
		<div class="openWinHeader">
			<div class="openWinTip">				
				<p id="successTip">@VLAN.batchAddVlan@</p>
			</div>
			<div class="rightCirIco pageCirIco"></div>
		</div>
		<div class="edge10">
			<div class="">
				<table class="zebraTableRows" width="100%">
				
					<tr class="tableHeader">
						<td width="150"><div style="width:150px; overflow:hidden;">VLAN ID <br /> @VLAN.batchManu1@</div></td>
						<td width="160">
							<div style="width:160px; overflow:hidden;">@VLAN.batchName@<br />
								<input id="allInput" title='@VLAN.batchNameInstead@'
								value='VLAN_#ID#' maxlength="31" onkeydown='allInputEnter()' onkeyup='allInputKeyup()' onchange="allInputChange()" /> 
							</div>
						</td>
						<td width="180">
							<div style="width:180px; overflow:hidden;">
								@VLAN.batchMvlanMode@<br /><select id="allSelect" class="selClass" onchange="allSelectChange()">
									<option value=2>unknown flooding</option>
									<option value=1>always flooding</option>
									<option value=3>no flooding</option>
								</select>
							</div>
						</td>
						<td>@COMMON.manu@ <br> @VLAN.batchManu2@</td>
					</tr>
				</table>
			</div>
			<div id="batchAddArea">
				<table class="zebraTableRows" id="vlanTable" style="height:auto;">
				</table>
			</div>
		</div>
	<div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
	    <ol class="upChannelListOl pB0 pT10 noWidthCenter">
	        <li id="stopAdd" style="display:none;">
	            <a href="javascript:;" class="normalBtnBig" onClick="endClick()"  id="endBt">
	                <span>@VLAN.batchStopAdd@</span>
	            </a>
	        </li>
	        <li id="startAdd">
	            <a href="javascript:;" class="normalBtnBig" onClick="saveClick()"  id="saveBt">
	                <span>@VLAN.batchStartAdd@</span>
	            </a>
	        </li>	        
	        <li>
	            <a href="javascript:;" class="normalBtnBig" onclick="cancelClick()" id="cancelBt" >
	                <span>
	                    @COMMON.close@
	                </span>
	            </a>
	        </li>
	    </ol>
	</div>
	<span id="theTips" style="position:absolute; top:200px; padding:5px; border:1px solid #ccc; left:0px; text-align:center; display:none; background:#fff;"></span>
</body>
</Zeta:HTML>