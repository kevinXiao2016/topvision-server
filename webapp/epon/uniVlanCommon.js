var selectedIndex;
var buttonFlag = false;
function okClick(type) {
    if (type == "trans") {
    	var beforeId = $("#beforeTransId").val();
        var afterId = $("#afterTransId").val();
        /*if(parseInt(beforeId) == parseInt(afterId)){
        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.vlanSameTip2)
        	return ;
        }*/
        if (transType == "add") {
            if(transData.length >= 8){
            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.ruleOver8 )
            }else{
 	            if(!checkedInput("beforeTransId")){
 	     	   		Zeta$("beforeTransId").focus();
 	     		 	return;
 	     	 	}
 	           if(!checkedInput("afterTransId")){
	     	   		Zeta$("afterTransId").focus();
	     		 	return;
	     	 	}
	 	       window.top.showWaitingDlg(I18N.COMMON.wait,  I18N.VLAN.addingNewRule)
	 	          $.ajax({
	 	 	        url: 'epon/vlan/addVlanTranslationRule.tv',
	 	 	        type: 'POST',
	 	 	        data: "entityId=" + entityId + "&uniId=" + uniId + "&vlanIndex=" + beforeId + "&translationNewVid=" + afterId +"&num=" + Math.random(),
	 	 	        dataType:"text",
	 	 	        success: function(text) {
	 	 		        if(text == 'success'){
	 	 		        	$("#beforeTransId").val("");
	 	 		            $("#afterTransId").val("");
	 	 		            $("#beforeTransId").focus();
	 	 		            $("#transSubmit").attr("value", I18N.COMMON.confirm)
	 	 		            $("#transSubmit").attr("disabled", true);
	 	 	            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.addNewRuleOk)
	 	 	            	transData[transData.length] = [beforeId,afterId];
	 	 	            	beforeTransText = "";
	 	 					afterTransText = "";
	 	 	            	transStore.loadData(transData); 	            	
	 	 	            	transGrid.selModel.selectRow(transData.length - 1, true);
	 	 		        }else{
	 	 		        	window.parent.showMessageDlg(I18N.COMMON.tip, text, 'error');
	 	 			    }
	 	 	        }, error: function(text) {
	 	 	        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.addNewRuleError,'error');
	 	 	    }, cache: false
	 	 	    });
           }
        } else if (transType == "modify1" || transType == "modify2") {
        	if(!checkedInput("afterTransId")){
     	   		Zeta$("afterTransId").focus();
     		 	return;
     	 	}
        	 window.top.showWaitingDlg(I18N.COMMON.wait, I18N.VLAN.mdfingTransRule)
	          $.ajax({
	 	        url: 'epon/vlan/updateVlanTranslationRule.tv',
	 	        type: 'POST',
	 	        data: "entityId=" + entityId + "&uniId=" + uniId + "&vlanIndex=" + beforeId + "&translationNewVid=" + afterId +"&num=" + Math.random(),
	 	        dataType:"text",
	 	        success: function(text) {
	 		        if(text == 'success'){
	 		        	$("#beforeTransId").val("");
	 		            $("#afterTransId").val("");
	 		            $("#beforeTransId").focus();
	 		            $("#transSubmit").attr("value", I18N.COMMON.confirm)
	 		            $("#transSubmit").attr("disabled", true);
	 	            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.mdfTransRuleOk)
	 	            	// TODO 修改转换规则
	 	            	window.location.reload();
	 		        }else{
	 		        	window.parent.showMessageDlg(I18N.COMMON.tip, text, 'error');
	 			    }
	 	        }, error: function(text) {
	 	        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.mdfTransRuleError,'error')
	 	    }, cache: false
	 	    });
        }
    } else if (type == "trunk") {
        if(trunkData.length >= 8){
            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.ruleOver8)
        }else{
        	 var trunkId = $("#trunkId").val();
        	 if(!checkTrunkId()||parseInt(trunkId) > 4094 || parseInt(trunkId) < 1){
        	    Zeta$("trunkId").focus();
        	    return;
        	 }
        	var trunkDataTmp = trunkData.slice(0);
        	trunkDataTmp[trunkDataTmp.length] = [trunkId];
	        window.top.showWaitingDlg(I18N.COMMON.wait, I18N.VLAN.udtingRule)
	       	$.ajax({
	       	    url: 'epon/vlan/updateVlanTrunkRule.tv',
	    	    type: 'POST',
	    	    data: "uniId=" + uniId + "&entityId=" + entityId + "&trunkList=" + trunkDataTmp.toString()+"&num=" + Math.random(),
	    	    dataType:"text",
	    	    success: function(text) {
		    		if(text == 'success'){
		    	        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.udtTrunkOk)
		    	        trunkData[trunkData.length] = [trunkId];
		    	        trunkStore.loadData(trunkData);
				        trunkGrid.selModel.selectRow(trunkData.length - 1);
				        $("#trunkSubmit").attr("disabled", true);
				        $("#trunkId").val("");
				        $("#trunkId").focus();
		    		}else{
		    		    window.parent.showMessageDlg(I18N.COMMON.tip, text, 'error')
		    		}
	    	    }, error: function(text) {
	    	        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.udtError ,'error')
	    	    }, cache: false
	    	});
        }
    }else if(type == "tag"){
    	 var pvId = $("#pvid").val();
    	 if(!checkedInput("pvid")||pvId > 4094 || pvId < 1){
    	    Zeta$("pvid").focus();
    	    return;
    	 }
    	 window.top.showWaitingDlg(I18N.COMMON.wait, I18N.VLAN.udtingMark )
	       	$.ajax({
	       	    url: 'epon/vlan/updatePortVlanAttribute.tv',
	    	    type: 'POST',
	    	    data: "uniId=" + uniId + "&vlanPVid=" + pvId +"&entityId="+ entityId +"&num=" + Math.random(),
	    	    dataType:"text",
	    	    success: function(text) {
		    		if(text == 'success'){
		    	        window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.udtMarkOk )
		    	        $("#tagSubmit").attr("disabled", false);
		    		}else{
		    		    window.parent.showMessageDlg(I18N.COMMON.tip, text, 'error')
		    		    //$("#pvid").val("");
		    		    $("#pvid").focus();
		    		}
	    	    }, error: function(text) {
	    	        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.udtError ,'error')
	    	    }, cache: false
	    	});
    }
}
function filterChanger2(id, array, store) {
	//store:aggrSvlanstore、aggrCvlanSore、qinqSvlanstore、qinqCvlanSore
	//id: 对应的input的id
	//array:二维数组来存放store对应的data
	jQuery("#"+id).keyup(function(){
		var data = new Array();
		text = jQuery("#" + id).val();
		//添加按钮的disabled验证
		if(text == null || text== "" || text == 'undefined'){
			$("#"+id.substring(0,4)+"Submit").attr("disabled", true);
		}else{
			$("#"+id.substring(0,4)+"Submit").attr("disabled", false);
		}
		//CVLANinput的disabled验证
		if(id=="aggregationSvlanId"){
			if(text == null || text== "" || text == 'undefined'){
				$("#aggregationCvlanId").attr("disabled", true);
			}else{
				$("#aggregationCvlanId").attr("disabled", false);
			}
        }//end 验证
		var j = 0;
		jQuery.each(array,function(i,n){
			var tmp1 = "" + n[0];
			var tmp2 = "" + n[1];
			if (text == null || text == "" || tmp1.startWith(text)) {
				var tmp = new Array();
				tmp[i] = new Array();
				data[i] = new Array();
				tmp[i][0] = tmp1;
				tmp[i][1] = tmp2;
				data[j] = tmp[i];
				j++;	
            }
		});
		data.length = j;
		j = 0;	//empty j
		store.loadData(data);		
		if (data.length == 0) {
			//没有SVLAN显示，就默认为0，也不显示CVLAN
            if(id=="aggregationSvlanId"){
				aggrSvid = 0;
				aggrCvlanStore.loadData(aggrCvlanData[aggrSvid]);
				aggrSvid = $("#aggregationSvlanId").val();
            }  
        }else if(data.length != 0 ){
        	if(id=="aggregationSvlanId"){
    			aggrSvid = Ext.getCmp("aggrSvlanGrid").getStore().getAt(0).data.aggrSvlanId;
    			aggrCvlanStore.loadData(aggrCvlanData[aggrSvid]);
    		}
        }
        //jQuery("#"+id).unbind("keyup");        
	});	
}
function checkedSelectedRow(id,grid,name) {
	if(grid.getSelectionModel().getSelected()!=null && grid.getSelectionModel().getSelected()!='undefined'){
		var record = grid.getSelectionModel().getSelected();
		var tmpId;
		if(name == "aggr"){
			tmpId = record.get('aggrSvlanId');
		}else if(name == "qinq"){
			tmpId = record.get('qinqSvlanId');
		}
		if(id == tmpId){
			return true;
		}else{
			return false;
		}		
	}else{
		return false;
	}
}
function filterChanger(id, array, store) {
    jQuery("#" + id).keyup(function() {
        var text = jQuery("#" + id).val();
        var data = [];
        jQuery.each(array, function(i, n){
            var tmp = "" + n;
            if (text == null || text == "" || tmp.startWith(text)) {
                data[data.length] = [tmp];
            }
        });
        store.loadData(data);
    });
}
//str变红匹配
String.prototype.redMatchWith = function(str) {
    if (str == null || str == "" || this.length == 0 || str.length > this.length) {
        return false;
    }
    if (this.substr(0) == str) {
        return true;
    } else {
        return false;
    }
}
//模糊匹配
String.prototype.startWith = function(str) {
    if (str == null || str == "" || this.length == 0 || str.length > this.length) {
        return false;
    }
    if (this.substr(0, str.length) == str) {
        return true;
    } else {
        return false;
    }
}
Array.prototype.remove = function(from, to) {
    var rest = this.slice((to || from) + 1 || this.length);
    this.length = from < 0 ? this.length + from : from;
    return this.push.apply(this, rest);
};
Array.prototype.removeObj = function(obj) {
	var index = this.indexOf(obj);
    if(index != -1){
        this.splice(index, 1);
        return true;
    }
    return false;
};
function stopPropagation(e) {
    e = e || window.event;
    if(e.stopPropagation) { //W3C阻止冒泡方法
        e.stopPropagation();
    } else {
        e.cancelBubble = true; //IE阻止冒泡方法
    }
}
//保存模式
function saveModeClick(){
	var selectId = $("#vlanMode").val();
	 window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.VLAN.cfmUseNewMode , function(type) {
		 if (type == 'no') {
             return;
         }
		 window.top.showWaitingDlg(I18N.COMMON.wait, I18N.VLAN.switchingMode )
		 $.ajax({
		        url: 'epon/vlan/updatePortVlanAttribute.tv',
		        type: 'POST',
		        data: "uniId=" + uniId + "&entityId="+ entityId +"&vlanMode=" + selectId + "&num=" + Math.random(),
		        dataType:"text",
		        success: function(text) {
			        if(text == 'success'){
			        	vlanMode = selectId;
		            	window.top.showWaitingDlg(I18N.COMMON.wait, I18N.VLAN.settingRule )
			           	$.ajax({
			           	        url: 'epon/vlan/changePortVlanMode.tv',
			           	        type: 'POST',
			           	        data: "uniId=" + uniId + "&entityId=" + entityId + "&vlanMode=" + vlanMode +"&num=" + Math.random(),
			           	        dataType:"text",
			           	        success: function(text) {
			           		        if(text == 'success'){
			           		        	if(selectId == 1){
			           		        		$("#tagSubmit").attr("disabled", false)
			           		        	}
			           		        	$("#refreshSubmit"+selectId).attr("disabled", false)
			           		        	$("#discription").show();
			           		        	$("#modeChange").hide();   		 
			           	            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.switchOk)
			           	            	window.location.reload();
			           	            	shinning('showMessage');
			           		        }else{
			           		        	window.parent.showMessageDlg(I18N.COMMON.tip, text, 'error')
			           			    }
			           	        }, error: function(text) {
			           	        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.switchError ,'error')
			           	    }, cache: false
			           	 });
			        }else{
			        	window.parent.showMessageDlg(I18N.COMMON.tip, text, 'error')
				    }
		        }, error: function(text) {
		        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.switchError  ,'error')
		    }, cache: false
		 }); 
	 });
}
//取消模式应用
function cancelModeClick(){
	$("#vlanMode").val(vlanMode);
	$("#discription").show();
	shinning('showMessage');
	$("#modeChange").hide(); 
	show_configDiv(1);
}
//单个VID的input输入验证（transAfter、transBefore）
function checkedInput(id){
	$("#"+id).css("border", "1px solid #8bb8f3");
    var reg = /^([0-9]{1,4})+$/;
    if (reg.exec($("#"+id).val()) && parseInt($("#"+id).val()) <= 4094 && parseInt($("#"+id).val()) > 0) {
        return true;
    } else {
        $("#"+id).css("border", "1px solid #ff0000");
        //$("#"+id).focus();
        return false;
    }
}
//从设备刷新数据方法
function refreshClick(mode){
	if(mode == "trans"){
		window.top.showWaitingDlg(I18N.COMMON.wait, I18N.VLAN.fetchingUV)
		 $.ajax({
		        url: 'epon/vlan/refreshVlanTranlationRule.tv',		        
		        data: "uniId=" + uniId + "&entityId=" + entityId + "&num=" + Math.random(),
		        dataType:"text",
		        success: function(text) {
			        if(text == 'success'){
		            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.fetchUVOk )
		            	window.location.reload();
			        }else{
			        	window.parent.showMessageDlg(I18N.COMMON.tip, text, 'error');
				    }
		        }, error: function(text) {
		        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.fetchUVError ,'error');
		    }, cache: false
		    });
	}
	if(mode == "trunk"){
		window.top.showWaitingDlg(I18N.COMMON.wait, I18N.VLAN.fetchingUV)
		 $.ajax({
		        url: 'epon/vlan/refreshVlanTrunkRule.tv',
		        type: 'POST',
		        data: "uniId=" + uniId + "&entityId=" + entityId + "&num=" + Math.random(),
		        dataType:"text",
		        success: function(text) {
			        if(text == 'success'){
		            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.fetchUVOk)
		            	window.location.reload();
			        }else{
			        	window.parent.showMessageDlg(I18N.COMMON.tip, text, 'error');
				    }
		        }, error: function(text) {
		        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.fetchUVError ,'error');
		    }, cache: false
		    });
	}
	if(mode == "tag"){
		window.top.showWaitingDlg(I18N.COMMON.wait, I18N.VLAN.fetchingUV)
		 $.ajax({
		        url: 'epon/vlan/refreshPortVlanAttribute.tv',
		        type: 'POST',
		        data: "uniId=" + uniId + "&entityId=" + entityId + "&num=" + Math.random(),
		        dataType:"text",
		        success: function(text) {
			        if(text == 'success'){
		            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.fetchUVOk);
		            	window.location.reload();
			        }else{
			        	window.parent.showMessageDlg(I18N.COMMON.tip, text, 'error');
				    }
		        }, error: function(text) {
		        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.fetchUVError ,'error');
		    }, cache: false
		    });
	}
	if(mode == "aggr"){
		window.top.showWaitingDlg(I18N.COMMON.wait, I18N.VLAN.fetchingUV)
		 $.ajax({
		        url: 'epon/vlan/refreshVlanAggregationRuleList.tv',
		        type: 'POST',
		        data: "uniId=" + uniId + "&entityId=" + entityId + "&num=" + Math.random(),
		        dataType:"text",
		        success: function(text) {
			        if(text == 'success'){
		            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.fetchUVOk)
		            	window.location.reload();
			        }else{
			        	window.parent.showMessageDlg(I18N.COMMON.tip, text, 'error')
				    }
		        }, error: function(text) {
		        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.fetchUVError ,'error')
		    }, cache: false
		    });
	}
}