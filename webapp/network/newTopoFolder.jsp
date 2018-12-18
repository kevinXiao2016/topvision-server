<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<link rel="stylesheet" type="text/css" href="../css/jquery.treeview.css"></link>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.resources.resources,com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<Zeta:Loader>
	css css.main
	css css.reset
    css css/white/disabledStyle
	library Jquery
	library ext
	library zeta
    module network
    import js/jquery/jquery.treeview
    import js/jquery/jquery.wresize
    import js.nm3k.menuNewTreeTip
</Zeta:Loader>
<style type="text/css">
#w800{width: 790px; overflow: hidden; position: relative; top: 0; left: 0; height:450px; background:#F7F7F7;}
#w3200{ position:absolute; width:3200px; height:450px; top:0px; left:0px;}
#step0,#step1{ width:800px; position:absolute; top:0px; left:0px;}
#step1{ left:800px;}
.stepSidePart{ position: absolute; top:0px; left:0px; width:166px; overflow:hidden; border-right:1px solid #CDCDCD; height:450px;}
.stepSidePartDl{ padding:5px; position:absolute; top:0px; left:0px; z-index:3;}
.stepSidePartDl dd{ height:60px; padding:20px;}
.stepSidePartDl dt{ height:10px; overflow:hidden; margin:5px 0px; width:100%;}
#moveBox{ position:absolute; top:10px;left:12px; width:143px; height:94px; z-index:2;} 
.mainPart{ margin:0px 0px 0px 167px; height:450px; overflow:hidden; position:relative; top:0px; left:0px;}
#h1350{ width:100%; height:1350px; position:absolute; top:0px; left:0px;}
#rightStep0,#rightStep1,#rightStep2{width:100%;height:450px; overflow: hidden;}
#putTopoTree{ height:330px; padding:10px; background: #fff; border:1px solid #ccc; overflow:auto;}
.bottomPutBackBtn{ position: absolute; bottom:10px; left:10px;}
#putResultTree{padding:10px; height:300px; border:1px solid #ccc; background:#fff; overflow:auto;}

#map_tree a.linkBtn{ padding:3px; color:#069;}
#map_tree a.linkBtn:hover{ background:#39c; color:#fff;}
#map_tree a.selectedTree{ background:#069; color:#fff;}
#map_tree a.selectedTree:hover{ background:#069; color:#fff;}
#map_tree span.folder{ color:#333;}
#map_tree span.folder:hover{ color:#000;}

</style>
<script type="text/javascript" src="../js/jquery/nm3kToolTip.js"></script>
<script type="text/javascript" src="../fault/TipTextArea.js"></script>
<script type="text/javascript" src="../js/tools/treeBuilder.js"></script>
<script type="text/javascript" src="../js/ofc/swfobject.js"></script>
<style type="text/css">
.topoFolderIcon {
	background-image: url(../images/network/topoFolder.gif) !important;
}

.topoFolderIcon1 {
	background-image: url(../images/network/topoicon.gif) !important;
}

.topoFolderIcon5 {
	background-image: url(../images/network/subnet.gif) !important;
}

.topoFolderIcon6 {
	background-image: url(../images/network/cloudy16.gif) !important;
}

.topoFolderIcon7 {
	background-image: url(../images/network/href.png) !important;
}

.topoFolderIcon20 {
	background-image: url(../images/network/region.gif) !important;
	background-repeat: no-repeat;
	padding: 1px 0 1px 17px;
	valign: middle;
	display: block;
}
</style>
<script type="text/javascript">
//不选，默认添加到默认地域下
var selectedFolderId = 10;
var liFormatStr = '<li id="{2}"><span class="{0}"><a href="javascript:;" onclick="{1}" class="linkBtn" id="{2}">{3}</a></span></li>';
$(function(){	
	var t = new TipTextArea({
		id : "note"
	});
	t.init();	 
	
});//end document.ready;

Ext.BLANK_IMAGE_URL = '../images/s.gif'; 	
var tree = null;

/**
 * 构建地域树结构
 */
function buildFolderTree(){
	//清空树
	$('ul#tree').empty();
	$.ajax({
		url: '../topology/loadTopoFolder.tv',
    	type: 'POST',
    	dataType:"json",
    	async: 'false',
   		success: function(json) {
			bulidTree(json, '');
			bulidTree(json, '', 'map_tree');
			treeExpand();
			$("#map_tree").treeview({ 
				animated: "fast",
				control:"#sliderOuter"
			});	//end treeview;
			//为树绑定事件
			$('ul#tree').find('a.linkBtn').each(function(){
   				$(this).bind('click', function(){
   					selectedFolderId = $(this).attr("id");
   				});
   			});
			$('ul#map_tree').find('a.linkBtn').each(function(){
   				$(this).bind('click', function(){
   					var $this = $(this)
   					stepObj.selectedTreeId = $this.attr("id");
   					$("#selectedEare").text($this.text());
   				});
   			});
			
			
   		}, error: function(array) {
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}

Ext.onReady(function(){
	buildFolderTree();
	//为名称输入框绑定事件
	$("input#name").bind('blur',function(){
		if(isFolderNameExist($(this).val())){
			$("#nameExist").show();
			$("input#name").addClass('normalInputRed');
		}else{
			$("#nameExist").hide();
			$("input#name").removeClass('normalInputRed');
		}
	});
	
});
function doOnload() {
	Zeta$('name').focus();
}

/**
 * 地域名称重复性校验
 */
function isFolderNameExist(name){
	var result = false;
	//遍历地域树
	$('a.linkBtn').each(function(){
		if(this.innerHTML == name){
			result =  true;
		}
	});
	return result;
}

//添加地域
function okClick() {
	//获取名称和描述
	var name = $("input#name").val();
	var note = $("#note").val();
	//名称必须为1-24位的合法字符
	var reg = /^[a-zA-Z\d\u4e00-\u9fa5-_]{1,24}$/;
	var reg2 = /^[a-zA-Z\d\u4e00-\u9fa5-_]{0,128}$/;
	if(!reg.test($.trim(name))){
		$("input#name").focus();
		return;
	}
	if(!reg2.test($.trim(note))){
		$("#note").focus();
		return;
	}
	//对名称进行重复性校验
	if(isFolderNameExist(name)){
		$("#nameExist").show();
		$("input#name").addClass('normalInputRed');
		$("input#name").focus();
		return;
	}
	if(note.length > 128){
		$("#note").focus();
	}
	$("#addBtn").attr({"disabled":"disabled"});
	//添加地域
	$.ajax({
		url: 'createTopoFolder.tv',
    	type: 'POST',
    	data: {name : name, note : note, superiorId : selectedFolderId, displayType : 20},
    	dataType:"json",
   		success: function() {
   			$("#addBtn").removeAttr("disabled");
   			top.afterSaveOrDelete({
   				title: '@COMMON.tip@',
   				html: '<b class="orangeTxt">@resources/topo.virtualDeviceList.addSuccess@</b>'
   			});
   			//重置当前页面数据
   			$("input#name").val('').focus();
   			$("#note").val('');
   			selectedFolderId = 10;
   			buildFolderTree();
   			//刷新父页面的树结构
   			try{
   				parent.frames["menuFrame"].refreshTree();
   			}catch(err){}
   		}, error: function() {
   			$("#addBtn").removeAttr("disabled");
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
	return
	
	
}
function cancelClick() {
	window.top.closeWindow("modalDlg");
}
function addEnterKey() {
	if (event.keyCode==KeyEvent.VK_ENTER) {
		okClick();
	}
}
function gotoAndPlay(para){
	$("#h1350").animate({top:para})
	
}
//从地图新建;
function mapCreate(){
	$("#w3200").animate({left:-800});	
}
//返回
function backNormalCreate(){
	$("#w3200").animate({left:0});
}
//左侧方块移动;
function leftBoxMove(para){
	$("#moveBox").animate({top:para});
}

var regionStrs = [];
var stepObj = {};

function initData(){
	for(var i=0; i<dataArr.length; i++){//遍历省份;
		var o1 = {}
		o1.name = dataArr[i].text;
		o1.id = (3300 + i*1100).toString();	
		o1.parentId = "-1";
		stepObj.dabase.push(o1);
		var arr2 = dataArr[i].level2;
		
		for(var j=0; j<arr2.length; j++){//遍历市;
			var o2 = {};
			o2.name = arr2[j].text;
			o2.id = (Number(o1.id) * 100 + j + 1).toString();
			o2.parentId = o1.id;			
			stepObj.dabase.push(o2);
			var arr3 = arr2[j].level3;			
			for(var k=0; k<arr3.length; k++){//遍历区;
				var o3 = {};
				o3.name = arr3[k];				
				o3.id = (Number(o2.id) * 100 + k +1).toString();
				o3.parentId = o2.id;
				stepObj.dabase.push(o3);
			}
		}
	}
	$.each(stepObj.dabase, function(index, region){
		regionStrs.push(String.format(regionFormatStr, region.name, region.id, region.parentId));
	});
}

var regionFormatStr = "name:\"{0}\",id:\"{1}\",parentId:\"{2}\"";

//initData();
             
//加入市级和区级;
function handleArea(){
	//先在第一个里面循环，得到是哪个省;
	var str = '<ul id="newTree" class="filetree"><li><span class="folder">'+ stepObj.selectedName +'</span><ul>';
	for(var i=0; i<dataArr.length; i++){
		if(dataArr[i].text == stepObj.selectedName){
			var arr2 = dataArr[i].level2;
			for(var j=0; j<arr2.length; j++){//循环市区;
				str += '<li><span class="topoFolderIcon20">'+ arr2[j].text +'</span><ul>';
				var arr3 = arr2[j].level3;
				for(var k=0; k<arr3.length; k++){//循环区;
					str += '<li><span class="topoFolderIcon20">'+ arr3[k] +'</span></li>';
				};//end for;
				str += '</ul></li>';
			};//end for;
		}
	};//end for;
	str += '</ul></li></ul>';
	$("#putResultTree").empty().html(str);
	$("#newTree").treeview({ 
		animated: "fast"
	});	//end treeview;
};//end handleArea;
             
//加入市级;
function handleCity(){
	var str = '<ul id="newTree" class="filetree"><li><span class="folder">'+ stepObj.selectedName +'</span><ul>';
	//先在第一个里面循环，得到是哪个省;
	for(var i=0; i<dataArr.length; i++){
		if(dataArr[i].text == stepObj.selectedName){
			var arr2 = dataArr[i].level2;
			for(var j=0; j<arr2.length; j++){//循环市区;
				str += '<li><span class="topoFolderIcon20">'+ arr2[j].text +'</span></li>';
			};//end for;
		}
	};//end for;
	str += '</ul></li></ul>';
	$("#putResultTree").empty().html(str);
	$("#newTree").treeview({ 
		animated: "fast"
	});	//end treeview;
};//end function;

function addToTopoFolder(){
	var selectedObj = {};
	if($("#jsLevel2").is(':disabled') && $("#jsLevel3").is(':disabled')){
		//直接把区域加到数据库即可
		selectedObj.name = stepObj.selectedName;
		selectedObj.parentId = -1;
		selectedObj.children = [];
	}else if($("#jsLevel2").is(':disabled') && !$("#jsLevel3").is(':disabled')){
		//需要判断区级是否勾选
		if($("#jsLevel3").is(':checked')){
			//加入下面的区
			//遍历省，找到该市
			for(var i=0; i<dataArr.length; i++){
				if(dataArr[i].text==stepObj.parentName){
					$.each(dataArr[i].level2, function(index, city){
						if(city.text==stepObj.selectedName){
							//定位到该市
							selectedObj.name = stepObj.selectedName;
							selectedObj.parentId = -1;
							selectedObj.children = new Array();
							//遍历该市下的区，组装数据
							$.each(city.level3, function(index, region){
								var regionObj = {};
								regionObj.name = region;
								regionObj.parentId = city.text;
								regionObj.children = [];
								selectedObj.children.push(regionObj);
							})
						}
					})
				}
			}
		}else{
			//只加入该市
			selectedObj.name = stepObj.selectedName;
			selectedObj.parentId = -1;
			selectedObj.children = [];
		}
	}else{
		selectedObj.name = stepObj.selectedName;
		selectedObj.parentId = -1;
		selectedObj.children = new Array();
		//需要判断区级是否勾选
		if($("#jsLevel3").is(':checked')){
			//加入该省下的所有市和区
			for(var i=0; i<dataArr.length; i++){
				if(dataArr[i].text==stepObj.selectedName){
					//定位到该省
					//遍历市
					var cityArray = new Array();
					$.each(dataArr[i].level2, function(index, city){
						var currentCity = {};
						currentCity.name = city.text;
						currentCity.parentId = dataArr[i].text;
						currentCity.children = new Array();
						//遍历该市下的区，加入
						$.each(city.level3, function(index, region){
							var regionObj = {};
							regionObj.name = region;
							regionObj.parentId = city.text;
							regionObj.children = [];
							currentCity.children.push(regionObj);
						})
						//将该市加入该省下
						selectedObj.children.push(currentCity);
					});
				}
			}
		}else if(!$("#jsLevel3").is(':checked') && $("#jsLevel2").is(':checked')){
			//加入该省下的所有市即可
			for(var i=0; i<dataArr.length; i++){
				if(dataArr[i].text==stepObj.selectedName){
					//遍历市
					var cityArray = new Array();
					$.each(dataArr[i].level2, function(index, city){
						var currentCity = {};
						currentCity.name = city.text;
						currentCity.parentId = dataArr[i].text;
						currentCity.children = [];
						//将该市加入该省下
						selectedObj.children.push(currentCity);
					})
				}
			}
		}else if(!$("#jsLevel3").is(':checked') && !$("#jsLevel2").is(':checked')){
			selectedObj.name = stepObj.selectedName;
			selectedObj.parentId = -1;
			selectedObj.children = [];
		}
	}
	
	//需要向后台传递加入地域的ID，及所选的结构
	batchAddRegion(stepObj);
}

function batchAddRegion(stepObj){
	$.ajax({
		url: 'batchCreateFolder.tv',
    	type: 'POST',
    	data: {stepObj:regionStrs.join(";")},
    	dataType:"json",
   		success: function() {
   			/* top.afterSaveOrDelete({
   				title: 'message',
   				html: '<b class="orangeTxt">add success</b>'
   			});
   			//重置当前页面数据
   			$("input#name").val('').focus();
   			$("#note").val('');
   			selectedFolderId = 10;
   			buildFolderTree();
   			//刷新父页面的树结构
   			parent.frames["menuFrame"].refreshTree(); */
   		}, error: function() {
		}, cache: false,
		complete: function (XHR, TS) { XHR = null }
	});
}
              
//处理数据，传进来的一定是第2级别;              
function handleDataSecond(){
	var str = '<ul id="newTree" class="filetree"><li><span class="folder">'+ stepObj.selectedName +'</span><ul>';
	//先在第一个里面循环，得到是哪个省;
	for(var i=0; i<dataArr.length; i++){
		if(dataArr[i].text == stepObj.parentName){
			var tempArr = dataArr[i].level2;
			for(var j=0; j<tempArr.length; j++){//再循环内部，看是哪个市;
				if(tempArr[j].text == stepObj.selectedName){
					var arr3 = tempArr[j].level3;
					for(var k=0; k<arr3.length; k++){
						str += '<li><span class="topoFolderIcon20">'+ arr3[k] +'</span></li>';
					};//end for;
				}
			};//end for;
		}	
	};//end for;
	str += '</ul></li></ul>';
	$("#putResultTree").empty().html(str);
	
	$("#newTree").treeview({ 
		animated: "fast"
	});	//end treeview;
};//end function; 
</script>
</head>
<body class="openWinBody" onkeydown="addEnterKey(event)">
	<div id="w800">
		<div id="w3200">
			<div id="step0">
				<div class="openWinHeader">
				    <div class="openWinTip">
				    	<p><b class="orangeTxt">@NETWORK.topoFolder@</b></p>
				    	<p><span id="newMsg">@topo.newTopoFolder.tipMsg@</span></p>
				    </div>
				    <div class="rightCirIco earthCirIco"></div>
				</div>
				<form id="folderForm">
					<div class="edgeTB10LR20">
					    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
					        <tbody>
					            <tr>
					                <td class="rightBlueTxt" width="200">
					                    <label for="name">@resources/COMMON.nameHeader@:
												<font color=red>*</font>
										</label>
					                </td>
					                <td width="360">
					                    <input style="width: 334px" id=name
											name="topoFolder.name" value='' class="normalInput" type=text
											maxlength=24 toolTip='@topo.newTopoFolder.nameTip@' />
					                </td>
					                <td><span id="nameExist" style="display:none;" class="orangeTxt">@NETWORK.topoFolderNameExists@</span></td>
					            </tr>
					            <tr class="darkZebraTr">
					                <td class="rightBlueTxt">
					                   <label for="note">@resources/COMMON.description@: </label>
					                </td>
					                <td>
					                   <textarea style="width: 330px; height:60px;" id="note" toolTip='@topo.newTopoFolder.descTip@'
												name="topoFolder.note" rows=3 class="normalInput" maxLength="128"
												></textarea>
										<p id="tip1">@topo.newTopoFolder.tipLenght@</p>
					                </td>
					                <td></td>
					            </tr>
					           
					        </tbody>
					    </table>
					    <p class="pT10 pB5">@topo.newTopoFolder.position@:</p>
					    <div id="topoTree" style=" height: 130px;border: 1px solid #CCC;overflow: auto;" class="threeFeBg edge5">
					    	<div>
								<ul id="tree" class="filetree">
								</ul>
							</div>
					    </div>
					    <div class="noWidthCenterOuter clearBoth">
						     <ol class="upChannelListOl pB0 pT10 noWidthCenter">
						         <li><a id="addBtn" onclick="okClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoAdd"></i>@COMMON.add@</span></a></li>
						         <li><a  onclick="cancelClick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
						     </ol>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</Zeta:HTML>
