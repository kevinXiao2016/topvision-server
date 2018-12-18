var cvids = new Array()
var svids = new Array()
var tabs
var tips
// 当前的输入内容
var text = ""
var SvlanShowLength = 0
// CVID-MODE
var cvidModeData
var cvidModeStore
var cvidModeGrid
// SVID-MODE
var svidModeData
var svidModeStore
var svidModeGrid

var searchShowFlag = false
var exactFlag = false
// 当前选中的grid的行号
var selectedIndex
var selectedCIndex
//是否已经loadSvid、Cvid
var svidLoadFlag = false
var cvidLoadFlag = false

var vlanModeString = [I18N.VLAN.notCfg , I18N.VLAN.ponTranslate , I18N.VLAN.portAggr , I18N.VLAN.ponTransparent, 'PON QinQ', 
                      I18N.VLAN.llidTranslate,  I18N.VLAN.llidAggr, 'LLID Trunk', 'LLID QinQ']
if(vlanConfigType == 'pon'){
	Ext.Ajax.timeout = 60000;
}else if(vlanConfigType == 'llid'){
	Ext.Ajax.timeout = 120000
}

function loadScvidData(){
	var url = '/epon/vlan/loadPonSvidModeRela.tv';
	var params = {entityId : entityId, ponId : ponId}
	$.ajax({
		url : url,dataType:"json",cache:false,
		success : function(records) {
			svids = new Array();
			$.each(records, function(i, n) {
            	if(n.onuMacAddress == '00:00:00:00:00:00'){
            		n.onuMacAddress = ""
            	}
                svids[i] = new Array()
                svids[i] = [n.svid, n.vlanMode, n.onuMacAddress]
            });
			var url = '/epon/vlan/loadPonCvidModeRela.tv'
			var params = {ponId : ponId}
			$.ajax({
				url : url,dataType:'json',cache:false,
				success : function(records) {
					cvids = new Array();
					$.each(records, function(i, n) {
		            	if(n.onuMacAddress == '00:00:00:00:00:00'){
		            		n.onuMacAddress = "";
		            	}
		                cvids[i] = new Array();
		                cvids[i] = [n.cvid, n.vlanMode, n.onuMacAddress];
		            });
					reloadScvidStore();
				},
				error : function(response) {
				},
				data : params
			});
		},
		data : params
	});
}
function loadSvidStore(){
	// SVID有输入时进行过滤
	var tmpList = new Array()
	var tempL = svids.length
	if($('#SvidSearchId').val().length > 0){
		for(var x=0; x<tempL; x++){
			if((svids[x][0] + "").startWith($('#SvidSearchId').val() + "")){
				tmpList.push(svids[x])
			}
		}
	}else if($('#SvidSearchId').val() == ""){
		tmpList = svids.slice(0)
	}
	for(var $i=tmpList.length; $i>0;$i--){
		var $c =  tmpList[$i-1];
		if( $c[1] == 0 ){
			tmpList.splice( $i-1, 1);
		}
	}
	if(tmpList.length > 500){
		tmpList.length = 200;
		tmpList.push([I18N.VLAN.hasSvids, parseFloat(tempL - 200), ""]);
	}
	svidModeStore.loadData(tmpList);
    svidLoadFlag = true;
}
function loadCvidStore(){
	// CVID有输入时进行过滤
	var tmpList = new Array();
	var tempL = cvids.length;
	if($('#CvidSearchId').val().length > 0){
		for(var x=0; x<tempL; x++){
			if((cvids[x][0] + "").startWith($('#CvidSearchId').val() + "")){
				tmpList.push(cvids[x])
			}
		}
	}else if($('#CvidSearchId').val() == ""){
		tmpList = cvids.slice(0)
	}
	if(tmpList.length > 500){
		tmpList.length = 200;
		tmpList.push([I18N.VLAN.hasCvids, parseFloat(tempL - 200), ""]);
	}
	cvidModeStore.loadData(tmpList)
    cvidLoadFlag = true
}
function loadCsvidMode() {
	// CVID-MODE列表
    cvidModeStore = new Ext.data.SimpleStore({
    	data : cvids,
		fields: ['cvid','vlanMode','onuMacAddress'],
        sortInfo: {field: 'cvid', direction: 'ASC'}
    })

    cvidModeGrid = new Ext.grid.GridPanel({
    	stripeRows:true,
   		region: "center",
   		bodyCssClass: 'normalTable',
   		viewConfig:{forceFit: true},
        renderTo: 'cvidGrid',
        height: 280,
        width: 205,
        border: false,
        autoScroll:true,
        columns: [{header: 'CVID', dataIndex: 'cvid', width: 75}, 
                  {header: '@VLAN.vlanMode@' , dataIndex: 'vlanMode', width: 70,  renderer: function(value) {
		            	if(parseFloat(value) > 200){
		            		return value + I18N.COMMON.item
		            	}else{
		            		return vlanModeString[value]
		            	}
                  }},
                  {header: 'Mac', dataIndex: 'onuMacAddress', width: 120, hidden:true}],
        sm : new Ext.grid.RowSelectionModel({singleSelect: true, handleMouseDown: Ext.emptyFn}),
        store : cvidModeStore
    })
    
    // SVID-MODE列表
    svidModeStore = new Ext.data.SimpleStore({
    	data : svids,
		fields: ['svid','vlanMode','onuMacAddress'],
    	sortInfo: {field: 'svid', direction: 'ASC'}
    })

    svidModeGrid = new Ext.grid.GridPanel({
    	stripeRows:true,
   		region: "center",
   		bodyCssClass: 'normalTable',
   		viewConfig:{forceFit: true},
        renderTo: 'svidGrid',
        height: 280,
        width: 205,
        border: false,
        autoScroll: true,
        //loadMask : {msg : 'loading...'},
        columns: [{
            header: 'SVID', dataIndex: 'svid', width: 75, align: 'center'
        }, {
            header: I18N.VLAN.vlanMode , dataIndex: 'vlanMode', width: 70, align: 'center', renderer: function(value) {
            	if(parseFloat(value) > 200){
            		return "" + value + I18N.COMMON.item;
            	}else{
            		return vlanModeString[value];
            	}
            }
        },{
            header: 'Mac', dataIndex: 'onuMacAddress', width: 120, align: 'center', hidden:true
        }],
        sm: new Ext.grid.RowSelectionModel({singleSelect: true, handleMouseDown: Ext.emptyFn}),
        store: svidModeStore
    })
    loadScvidData()
}

function cvidModeStoreReload() {
	cvidModeStore.loadData(new Array());
	var tmpF = true;
	setTimeout(function(){
		if(tmpF){
			$("#loadingDiv").show();
		}
	}, 500);
	// 重新初始化
	var url = '/epon/vlan/loadPonCvidModeRela.tv';
	var params = {ponId : ponId};
	$.ajax({
		url : url,dataType:"json",cache:false,
		success : function(records) {
			cvids = new Array();
			$.each(records, function(i, n) {
            	if(n.onuMacAddress == '00:00:00:00:00:00'){
            		n.onuMacAddress = "";
            	}
                cvids[i] = new Array();
                cvids[i] = [n.cvid, n.vlanMode, n.onuMacAddress];
            });
			loadCvidStore();
            tmpF = false;
            $("#loadingDiv").hide();
		},
		error : function(response) {
		},
		data : params
	});
}

function svidModeStoreReload() {
	svidModeStore.loadData(new Array());
	var tmpF = true;
	setTimeout(function(){
		if(tmpF){
			$("#loadingDiv").show();
		}
	}, 500);
	// 重新初始化
	var url = '/epon/vlan/loadPonSvidModeRela.tv';
	var params = {entityId : entityId, ponId : ponId};
	$.ajax({
		url : url,dataType:"json",cache:false,
		success : function(records) {
			svids = new Array();
			$.each(records, function(i, n) {
            	if(n.onuMacAddress == '00:00:00:00:00:00'){
            		n.onuMacAddress = "";
            	}
                svids[i] = new Array();
                svids[i] = [n.svid, n.vlanMode, n.onuMacAddress];
            });
			loadSvidStore();
            tmpF = false;
            $("#loadingDiv").hide();
		},
		error : function(response) {
		},
		data : params
	});
}

function cvidKeyup(){
	text = $("#CvidSearchId").val();
    var json = new Array();
    var j = 0;
    $.each(cvids, function(i, n) {
        var tmp = "" + n[0];
        if (text == null || text == "" || tmp.startWith(text)) {
        	json[j] = new Array();
        	json[j] = [n[0], n[1], n[2]];
            j++;
        }
    });
    if(j > 500){
    	json.length = 200;
    	json.push([I18N.VLAN.hasCvids , parseFloat(j - 200), ""]);
    }
    cvidModeStore.loadData(json);
}
function svidKeyup(){
	text = $("#SvidSearchId").val();
    var data = new Array();
    var j = 0;
    $.each(svids, function(i, n) {
        var tmp = "" + n[0];
        if (text == null || text == "" || tmp.startWith(text)) {
            data[j] = new Array();
            data[j] = [n[0], n[1], n[2]];
            j++;
        }
    });
    
    for(var $i=data.length; $i>0;$i--){
		var $c =  data[$i-1];
		if( $c[1] == 0 ){
			data.splice( $i-1, 1);
		}
	}
    
    if(data.length > 500){
    	data.length = 200;
    	data.push([I18N.VLAN.hasSvids, parseFloat(j - 200), ""]);
    }
    svidModeStore.loadData(data);
}
function tabNumChange(i){
	if(i=='x1'){
		$("#shuoming").text("@VLAN.trans@");
	}else if(i=='x2'){
		$("#shuoming").text("@VLAN.aggregate@");
	}else if(i=='x3'){
		$("#shuoming").text("Trunk");
	}else if(i=='x4'){
		$("#shuoming").text("QinQ");
	}
}

//解析逗号和连字符组成的字符串为数组
function changeToArray(){
	var re = new Array();
	var t = text.split(",");
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
					re.push(parseFloat(low) + u);
				}
			}else if(ttt.length == 1){
				re.push(ttt[0] ? parseFloat(ttt[0]) : parseFloat(ttt[1]));
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

// str变红匹配
String.prototype.redMatchWith = function(str) {
    if (str == null || str == "" || this.length == 0 || str.length > this.length) {
        return false;
    }
    if (this.substr(0) == str) {
        return true;
    } else {
        return false;
    }
    return true;
}
// str字符串匹配，用于单个输入
String.prototype.startWith = function(str) {
    if (str == null || str == "" || this.length == 0 || str.length > this.length) {
        return false;
    }
    if (this.substr(0, str.length) == str) {
        return true;
    } else {
        return false;
    }
    return true;
}
// list模糊匹配，用于无-的输入时
String.prototype.matchWith = function(array) {
    if (array.length == 0) {
        return false;
    }
    for (var i = 0; i < array.length; i++) {
        if (array[i] == this.substr(0, array[i].toString().length)) {
            return true;
        }
    }
    return false;
}
// list精确匹配，用于输入有-的时候
String.prototype.exactMatchWith = function(array) {
    if (array.length == 0) {
        return false;
    }
    for (var i = 0; i < array.length; i++) {
        if (array[i] == this.substr(0)) {
            return true;
        }
    }
    return false;
}
// QinQ的CVLAN数据的匹配start、end
String.prototype.qinqStartMatch = function(str) {
    if (str == null || str == "" || this.length == 0) {
        return false;
    }
    if (parseFloat(str) <= parseFloat(this.substr(0))) {
        return true;
    } else {
        return false;
    }
    return false;
}
String.prototype.qinqEndMatch = function(str) {
    if (str == null || str == "" || this.length == 0) {
        return false;
    }
    if (parseFloat(str) >= parseFloat(this.substr(0))) {
        return true;
    } else {
        return false;
    }
    return false;
}
Array.prototype.remove = function(from, to) {
    var rest = this.slice((to || from) + 1 || this.length);
    this.length = from < 0 ? this.length + from : from;
    return this.push.apply(this, rest);
}
function stopPropagation(e) {
    e = e || window.event;
    if (e.stopPropagation) { // W3C阻止冒泡方法
        e.stopPropagation();
    } else {
        e.cancelBubble = true; // IE阻止冒泡方法
    }
}

// 单个VID的input输入验证（transAfter、transBefore、qinqStart、qinqEnd、aggrSvlan、qinqSvlan）
function checkedInput(id){
	$("#"+id).css("border", "1px solid #8bb8f3");
	var tmpV = $("#"+id).val();
	if(tmpV == null || tmpV == "" || tmpV == undefined){
		return true;
	}else{
	    var reg = /^([0-9]{1,4})+$/;
	    if (reg.exec(tmpV) && parseFloat(tmpV) > 0 && parseFloat(tmpV) <= 4094) {
	    	if(tmpV.toString().indexOf('0') == 0){
	    		var tmp = parseFloat(tmpV);
	    		$("#"+id).val(tmp);
	    	}
	        return true;
	    } else {
	        $("#"+id).css("border", "1px solid #ff0000");
	        $("#"+id).focus();
	        return false;
	    }
	}
}
// 支持，-的输入框的输入验证（aggrCvlan\trunk）
function checkedInputList(id){
	$("#"+id).css("border", "1px solid #8bb8f3");
	text = $("#"+id).val();
	if(($("#"+id).val()==null) || ($("#"+id).val()=="") || ($("#"+id).val()==undefined)){
		return true;
	}else{
	    var reg = /^([0-9]{1,4}[,-]{0,1})+$/;
	    if (reg.exec($("#"+id).val())) {
	    	var textTmp = text.replace(new RegExp('-', 'g'), ',');
	    	var textTmpArray = textTmp.split(',');
	    	for (var i = 0; i < textTmpArray.length; i++) {
	    		if (!textTmpArray[i] || parseFloat(textTmpArray[i]) > 4094 || parseFloat(textTmpArray[i]) == 0) {
	    			$("#"+id).css("border", "1px solid #ff0000");
			        $("#"+id).focus();
	    			return false;
	    		}
			}
	        return true;
	    } else {
	        $("#"+id).css("border", "1px solid #ff0000");
	        $("#"+id).focus();
	        return false;
	    }
	}
}

// 从设备更新数据
function getDataFormDevice() {
	var $sm,$selected;
	if( typeof llidTree != 'undefined' ){
		$sm = llidTree.getSelectionModel();
		$selected = $sm.getSelectedNode();
	}
    var at,tipNum;
    if(is8602G=='true'){
    	if (tabs) {
    		var idx=tabs.getActiveTab().itemId.substring(1);
    		if(idx>2){
    			at = idx-2;
    		}else{
    			at = idx- 1;
    		}
            tipNum = tips.getActiveTab().itemId.substring(1) - 1;
        } else {
            at = '0';
            tipNum = '0';
        }
    }else{
    	if (tabs) {
            at = tabs.getActiveTab().itemId.substring(1) - 1;
            tipNum = tips.getActiveTab().itemId.substring(1) - 1;
        } else {
            at = '0';
            tipNum = '0';
        }
    }
    
    var tmpStr = I18N.VLAN.cfmFetchAgain
    if(vlanConfigType == 'llid'){
    	tmpStr = I18N.VLAN.waitTooLong
    }
	window.top.showConfirmDlg(I18N.COMMON.tip, tmpStr, function(type) {
        if (type == 'no') {
            return;
        }
        if(vlanConfigType == 'pon'){
        	Ext.Ajax.timeout = 1800000;
        }else if(vlanConfigType == 'llid'){
        	Ext.Ajax.timeout = 3000000;
        }
        window.top.showWaitingDlg(I18N.COMMON.wait, I18N.VLAN.fetchingAgain, 'ext-mb-waiting');
        $.ajax({
			url: '/epon/vlan/refreshVlanDataFromOlt.tv',cache:false,
		    success: function() {
                if (vlanConfigType == 'pon') {
                	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.fetchAgainOk)
                    window.location.href = '/epon/vlan/showPonVlanConfig.tv?tabNum=' + at  +'&tipNum='+tipNum + '&entityId=' + entityId + '&ponId=' + ponId + '&currentId=' + currentId; 
                    Ext.Ajax.timeout = 60000;
                } else {
                	$.ajax({
                		url: '/epon/vlan/refreshVlanLlidListFromOlt.tv?r=' + Math.random(),
             		    success: function() {
             		    	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.fetchAgainOk)
             		    	window.location.href = '/epon/vlan/showPonVlanLlidConfig.tv?tabNum=' + at  +'&tipNum='+tipNum + '&entityId=' + entityId + '&ponId=' + ponId + '&currentId=' + currentId + "&treenodeid="+$selected.id;
             		    	Ext.Ajax.timeout = 120000;
             		    },
             		    error: function(){
           		   			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.fetchAgainError)
             		    },
           		   		data: {entityId : entityId, ponId : ponId}
                	});
             	}
		    },
		    error: function(){
		   		window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.fetchAgainError)
		    },
		   	data: {entityId : entityId}
		});
    });
}
//移出蒙版
function removeMask(){
	$("#loadingMask").animate({
		opacity:0.5
	},{
		speed: 'slow',
		complete :function(){
			$("#loadingMask").animate({
				opacity:0
			},{
				speed: 'slow',
				complete :function(){
					$("#loadingMask").css("zIndex",1);
					$("#loadingMask").hide();
				}
			});
		}
	});
}
function reloadScvidStore(){
	var a = tips.getActiveTab().itemId;
	if(a == 't2'){
		// 重新加载CVID数据
		svidLoadFlag = false;
		loadCvidStore();
	}else if(a == 't3'){
		// 重新加载SVID数据
		cvidLoadFlag = false;
		loadSvidStore();
	}else{
		svidLoadFlag = false;
		cvidLoadFlag = false;
	}
}
function changeToString(list){
	var re = new Array();
	if(list.length > 1){
		list.sort(function(a, b){
			return a - b;
		});
		var f = 0;
		var n = 0;
		var ll = list.length;
		for(var i=1; i<ll; i++){
			if(list[i] == list[i - 1] + 1){
				n++;
			}else{
				re = _changeToString(re, f, n, list);
				f = i;
				n = 0;
			}
			if(i == ll - 1){
				re = _changeToString(re, f, n, list);
			}
		}
	}else if(list.length == 1){
		re.push(list[0]);
	}
	return re;
}
function _changeToString(re, f, n, list){
	if(n == 0){
		re.push(list[f]);	
	}else if(n == 1){
		re.push(list[f]);
		re.push(list[f + 1]);
	}else{
		re.push(list[f] + "-" + list[f + n]);
	}
	return re;
}