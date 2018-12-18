/**
 *
 * @file		performanceMajor.js
 * @package		Performance
 * @version		1.0
 * @author 		Fan Zidong
 * 
 * 存放性能采集相关页面的一些公用方法
 */

/**
 * 采集管理页面的查询验证方法
 */
function queryValidate(){
	/*if($("#perfType").val()==""){
		//如果性能指标未选择，则采集周期必须为空
		if($("#collectCycle").val()!=""){
			window.parent.showMessageDlg('@COMMON.tip@', "@Tip.pleaseCheckCycle@");
			return false;
		}
	}else{*/
		//如果采集指标已选择，则采集周期必须为1-1440的整数
	var reg = /^\d{1,4}$/;
	var value = $("#collectCycle").val();
	if($("#collectCycle").val()!=""){
		if(!(reg.test(value) && value>=1 && value<=1440)){
			$("#collectCycle").focus();
			return false;
		}
	}
	return true;
}

/**
 * 返回Frame的父页面
 */
function backToFrameParent() {
	parent.closeFrame();
}

/**
 * 保持保存为全局配置页面的顶部和中间部分表格对齐
 */
function setTopTableWidth(){
	var td1 = $("#middlePartTable tbody tr:eq(0) td:eq(0)").outerWidth();
	var td2 = $("#middlePartTable tbody tr:eq(0) td:eq(1)").outerWidth();
	var td3 = $("#middlePartTable tbody tr:eq(0) td:eq(2)").outerWidth();
	var td4 = $("#middlePartTable tbody tr:eq(0) td:eq(3)").outerWidth();
	
	$("#topPartTable th:eq(0)").width(td1);
	$("#topPartTable th:eq(1)").width(td2);
	$("#topPartTable th:eq(2)").width(td3);		
	$("#topPartTable th:eq(3)").width(td4);	
};//end setTopTableWidth;

/**
 * 分组的展开或收起
 */
function groupExpandOrCollapse(){
	$(".groupTitle").each(function(){
		var $groupTitle = $(this);
		$groupTitle.bind("click", function(){
			//根据图片是-还是+，来判断操作
			if($groupTitle.children("td:first").hasClass("groupTitle_open")){
				//此时此group是展开的，关闭
				$groupTitle.children("td:first").removeClass("groupTitle_open").addClass("groupTitle_close");
				$groupTitle.parent().children(".groupContent").hide();
			}else{
				$groupTitle.children("td:first").removeClass("groupTitle_close").addClass("groupTitle_open");
				$groupTitle.parent().children(".groupContent").show();
			}
		});
	});
}

/**
 * 为主页面中的各种checkbox绑定合适的事件
 */
function checkboxBindEventOfMainPage(){
	//分组的全选checkbox的事件绑定
	$(".allSelectCheck").each(function(){
		var $allSelectCheck = $(this);
		$allSelectCheck.bind("click", function(event){
			if($allSelectCheck.attr("checked")){
				$allSelectCheck.parents("tbody").find(".perfCheckbox").attr("checked",true).val(1);
				$allSelectCheck.parents("tbody").find(".numberInput").attr("readonly", false).removeClass("disabledInput");
			}else{
				$allSelectCheck.parents("tbody").find(".perfCheckbox").attr("checked",false).val(0);
				$allSelectCheck.parents("tbody").find(".numberInput").attr("readonly", true).addClass("disabledInput").val(0);
			}
			event.stopPropagation();
		});
	});
	//为每个性能指标后面的checkbox绑定事件
	$(".perfCheckbox").each(function(){
		var $perfCheckbox = $(this);
		$perfCheckbox.bind("click", function(event){
			if($perfCheckbox.attr("checked")){
				$perfCheckbox.val(1).parent().prev().children(".numberInput").attr("readonly", false).removeClass("disabledInput");
				//判断该组的所有checkbox是否都选中，如果都选中，则勾选全选checkbox
				var $tbody = $perfCheckbox.parents("tbody");
				if($tbody.find(".perfCheckbox").length == $tbody.find(".perfCheckbox:checked").length){
					$perfCheckbox.parents("tbody").find(".allSelectCheck").attr("checked",true);
				}
			}else{
				$perfCheckbox.val(0).parent().prev().children(".numberInput").attr("readonly", true).addClass("disabledInput").val(0);
				$perfCheckbox.parents("tbody").find(".allSelectCheck").attr("checked",false);
			}
			event.stopPropagation();
		});
	});
}

/**
 * 保存为全局配置页面的checkbox事件绑定
 */
function saveAsGolbalCbxBindEvent(){
	$(".selectGroupCbx").each(function(){
		$(this).bind("click", function(){
			if($(this).attr("checked")){
				$(this).parents("tbody").find(".saveAsGolbalPerfCbx").attr("checked", true).val(1);
			}else{
				$(this).parents("tbody").find(".saveAsGolbalPerfCbx").attr("checked", false).val(0);
			}
		});
	});
	
	$(".saveAsGolbalPerfCbx").each(function(){
		$(this).bind("click", function(){
			if($(this).attr("checked")){
				$(this).val(1);
				if($(this).parents("tbody").find(".saveAsGolbalPerfCbx").length == $(this).parents("tbody").find(".saveAsGolbalPerfCbx:checked").length){
					$(this).parents("tbody").find(".selectGroupCbx").attr("checked", true);
				}
			}else{
				$(this).val(0);
				$(this).parents("tbody").find(".selectGroupCbx").attr("checked", false);
			}
		});
	});
	
}

/**
 * 全局配置页面的checkbox事件绑定
 */
function golbalCbxBindEvent(){
	//分组的全选checkbox的事件绑定
	$(".allSelectCheck").each(function(){
		$(this).bind("click", function(event){
			if($(this).attr("checked")){
				$(this).parents("tbody").find(".perfCheckbox").attr("checked",true).val(1);
			}else{
				$(this).parents("tbody").find(".perfCheckbox").attr("checked",false).val(0);
			}
			event.stopPropagation();
		});
	});
	//为每个性能指标后面的checkbox绑定事件
	$(".perfCheckbox").each(function(){
		var $perfCheckbox = $(this);
		$perfCheckbox.bind("click", function(event){
			if($perfCheckbox.attr("checked")){
				$perfCheckbox.val(1);
				if($(this).parents("tbody").find(".perfCheckbox").length == $(this).parents("tbody").find(".perfCheckbox:checked").length){
					$(this).parents("tbody").find(".allSelectCheck").attr("checked", true);
				}
			}else{
				$perfCheckbox.val(0);
				$(this).parents("tbody").find(".allSelectCheck").attr("checked",false)
			}
		});
	});
}

/**
 * 遍历input框的值，根据其值是否为0，来决定正确的样式
 */
function traversalInput(){
	$(".numberInput").each(function(){
			var $input = $(this);
			if($input.val()==0){
				$input.addClass("disabledInput");
				$input.parent().next().children(".perfCheckbox").attr("checked", false);
			}else{
				$input.removeClass("disabledInput");
				$input.parent().next().children(".perfCheckbox").attr("checked", true);
			}
		});
}

/**
 * 不分页展示列表
 */
function showAllClick(){
	bbar.unbind(store);
	store.load({params: {start:0,limit:-1}});
	bbar.hide().setHeight(0);
}

/**
 * 切换到分页展示模式
 */
function showCurrentPageClick(){
	bbar.bind(store);
	store.load({params: {start:0,limit:pageSize}});
	bbar.show().setHeight(30);
}

/**
 * 采集周期的renderer函数
 * @param value
 * @param p
 * @param record
 * @returns
 */
function collectRender(value, p, record){
	var str = null;
	if(value==0){
		str = '<img src="/images/performance/off.png">';
	}else{
		str = value;
	}
	return str;
}

/**
 * 返回主页面
 */
function back(){
	//清空编辑框中的值及其属性
	$(".numberInput").val("").attr("disabled", false).removeClass("disabledInput");
	$(".perfCheckbox").attr("checked", false);
	$("#modifyDiv").hide();
	store.reload();
}

function alternateRowColors(){
	$("#middlePartTable tbody").each(function(){
		$(this).find("tr:even").addClass('evenTr');		
	})
}

/**
 * 创建批量修改的按钮组
 * @returns {Ext.SplitButton}
 */
function createBatchConfigButton(){
	 return new Ext.SplitButton({
     	text: '@Tip.batchConfig@',
     	iconCls: 'bmenu_manage',
     	menu: new Ext.menu.Menu({
	       	    items: [{
	       	        text:'@Tip.singlePerf@',
	       	        handler: batchModifyOnePerf
	       	    }
	       	    /*,{
	       	        text:'@Tip.groupPerf@',
	       	        handler: batchModifyGroupPerf
	       	    },{
	       	        text:'@Tip.allPerf@',
	       	        handler: batchModifyAllPerf
	       	    }*/
	       	    ]
     	})
	})
}

/**
 * 创建显示指标数目切换的按钮组
 * @returns {Ext.SplitButton}
 */
function createColumnChangesButton(){
	 return new Ext.SplitButton({
     	text: '@Tip.displayPerf@',
     	menu: new Ext.menu.Menu({
	       	    items: [{
	       	        text:'@Tip.defaultPerf@',
	       	        handler: showDefault5Perf
	       	    },{
	       	        text:'@Tip.allPerf@',
	       	        handler: showAllPerf
	       	    }]
     	})
	})
}

/**
 * 创建显示模式的按钮组
 * @returns {Ext.SplitButton}
 */
function createDisplayModeButton(){
	 return new Ext.SplitButton({
     	text: '@Tip.displayMode@',
     	menu: new Ext.menu.Menu({
	       	    items: [{
	       	        text:'@Tip.paginating@',
	       	        handler: showCurrentPageClick
	       	    },{
	       	        text:'@Tip.NoPaginating@',
	       	        handler: showAllClick
	       	    }]
     	})
	})
}

/**
 * 创建统一设置时间的按钮组
 * @returns {Ext.SplitButton}
 */
function createSetTimeButton(){
	 return new Ext.SplitButton({
     	text: '@Tip.UnitedConfigCycle@',
     	iconCls: 'bmenu_clock', 
     	handler: function(){this.showMenu()},
     	menu: new Ext.menu.Menu({
	       	    items: [{
	       	        text:'@Tip.5Min@',
	       	        handler: set5ToAll
	       	    },{
	       	        text:'@Tip.10Min@',
	       	        handler: set10ToAll
	       	    },{
	       	        text:'@Tip.15Min@',
	       	        handler: set15ToAll
	       	    },{
	       	        text:'@Tip.30Min@',
	       	        handler: set30ToAll
	       	    },{
	       	        text:'@Tip.60Min@',
	       	        handler: set60ToAll
	       	    }]
     	})
	})
}


/**
 * 将页面中的所有性能指标采集周期的值设为5分钟
 */
function set5ToAll(){
	$(".numberInput").val(5);
}

/**
 * 将页面中的所有性能指标采集周期的值设为10分钟
 */
function set10ToAll(){
	$(".numberInput").val(10);
}

/**
 * 将页面中的所有性能指标采集周期的值设为15分钟
 */
function set15ToAll(){
	$(".numberInput").val(15);
}

/**
 * 将页面中的所有性能指标采集周期的值设为30分钟
 */
function set30ToAll(){
	$(".numberInput").val(30);
}

/**
 * 将页面中的所有性能指标采集周期的值设为60分钟
 */
function set60ToAll(){
	$(".numberInput").val(60);
}

/**
 * 验证性能指标采集周期的值是否是1-1440之间的正整数
 */
function validatePerfValue(){
	var reg = /^\d{1,4}$/;
	var result = true;
	$(".numberInput").each(function(){
		var value = $(this).val();
		if(reg.test(value) && value>=1 && value<=1440){
		}else{
			$(this).focus();
			result = false;
		}
	});
	return result;
}

function setSelectedPerfNames(para){
	$("#selectedPerfNames").val(para);
}

/**
 * 输入框ToolTip提示
 * id：el.id
 */
function _inputFocus(id, msg) {
	var obj=document.getElementById(id);
	if($("#"+id).attr("readonly")){
		return;
	}
	var popObj=document.getElementById("tips");
	if(popObj!=null) {
		popObj.innerHTML="<div class=\"tipcon\">"+msg+"</div>";
		popObj.style.display="inline";
		//定位tooltips
		var left = getX(obj);
		//var top = getY(obj)-obj.offsetHeight-popObj.offsetHeight;
		var top = getY(obj)-obj.offsetHeight;
		popObj.style.left=left+"px";
		popObj.style.top=top+"px";
		/*10000 > Ext.window的zIndex > 9000 ，为了防止弹出弹力的tootlip丢失，故设置一个较高的zindex ,modify by @bravin */
		popObj.style.zIndex = 100000;
	}
}

function _inputBlur(obj) {
	if (typeof obj != 'object' && typeof obj == 'string') {
		obj = document.getElementById(obj);
	};
	clearOrSetTips(obj);
	document.getElementById("tips").style.display='none';
}

function _clearOrSetTips(obj, msg) {
	if(!obj) return;
	if(obj.value!="") {
		if(obj.value==msg)obj.value="";
	} else obj.value=msg ? msg : '';
}
