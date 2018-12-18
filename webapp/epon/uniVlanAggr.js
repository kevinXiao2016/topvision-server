var cvids = new Array();//所有的CVLAN？未使用过
var svids = new Array();//所有的SVLAN？未使用过
var aggrSvlanGrid;//PANEL
var aggrCvlanGrid;//PANEL
var aggrSvlanStore;//STORE
var aggrCvlanStore;//STORE
var aggrSvlanData = new Array();//svlan的数据源
var aggrCvlanData = new Array();//cvlan的数据源
var aggrSvid = 0; //临时变量，传参用。
aggrCvlanData[0] = new Array();//
var aggrSvlanText;
var aggrCvlanText;

function loadAggrData(){
	 //聚合SVLANgrid
    var aggrStore = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({url: '/epon/vlan/loadVlanAggregationRuleList.tv?uniId=' + uniId + '&r=' + Math.random()}),
        reader: new Ext.data.ArrayReader({id: 0}, Ext.data.Record.create([{name: 'aggrSvlanIdList'}, {name: 'aggrCvlanIdList'}]))
    });
    aggrStore.load({
        callback: function(records, options, success) {
            // 数据加载完毕更新数组中的值
            $.each(records, function(i, n) {
                aggrSvlanData[i] = n.json[0];
                aggrCvlanData[n.json[0][0]] = new Array();
                for (var j = 0; j < n.json[1].length; j++) {
                    aggrCvlanData[n.json[0][0]][j] = new Array();
                    aggrCvlanData[n.json[0][0]][j][0] = n.json[1][j];//cvlan这个二维数组的第二维的第一个值存的是svlan
                }
            });
    		// 初始时选中第一行
            aggrSvid = aggrSvlanData[0];
            aggrSvlanStore = new Ext.data.SimpleStore({data: aggrSvlanData, fields: ["aggrSvlanId"]});
    		
		    //聚合SVLANgrid		
            aggrSvlanGrid = new Ext.grid.GridPanel({
                cls:"normalTable edge10",stripeRows:true,region: "center",bodyCssClass: 'normalTable',
                id:'aggrSvlanGrid',
                renderTo: 'aggrSvlan_grid',
                width:248,
                title:"@VLAN.aggregatedVlan@",
                height:150,
                border: false,
                sm : new Ext.grid.RowSelectionModel({singleSelect :true}),
                autoScroll:true,
                columns:[{
                    header: I18N.VLAN.aggVlan + " ID",width:140, dataIndex: "aggrSvlanId", align: "center", renderer: aggrSvlanChangeRed
                },{
                    header: I18N.COMMON.manu ,width:82, dataIndex: "id", align: "center", renderer: function(value, cellmeta, record) {
                    	if(operationDevicePower){
                    		return "<img src='/images/delete.gif' onclick='deleteSvlanAggrRule(" + record.data.aggrSvlanId + ")'/>"
                    	}else{
                    		return "<img src='/images/deleteDisable.gif'/>"
                    	}
                	}
                }],
                store: aggrSvlanStore,
                listeners:{
                    "click" : {
                        fn : aggrGridClick,
                        scope : this
                    },
                    'viewready':{
                        fn : aggrReady,
                        scope : this
                    }}
            });
            aggrSvlanGrid.getSelectionModel().on('rowselect', function(grid, rowIndex, e) {
                var tmpId = grid.selections.get(0).data.aggrSvlanId;
                selectedIndex = getAggrSvlanIndex(tmpId);
            });
			jQuery("#aggregationSvlanId").one("focus", function() {
            	jQuery("#aggregationSvlanId").keyup(function() {
                    var data = new Array();
                    aggrSvlanText = jQuery("#aggregationSvlanId").val();//在监听的事件发生时更新text
                    var j=0;
                    $("#aggregationCvlanId").val("");
                    //非空验证
                    if(aggrSvlanText==null || aggrSvlanText=="" || aggrSvlanText=='undefined'){
                        //为空，灰掉CVLANinput
                    	$("#aggregationCvlanId").attr("disabled", true);
                    }else{
                        //激活  CVLANinput
                        $("#aggregationCvlanId").attr("disabled", false);
	            	    //输入验证
	            	    if(!checkedInput("aggregationSvlanId")){
							//灰掉CVLANinput和submit按钮
							$("#aggregationCvlanId").attr("disabled", true);
							$("#aggrSubmit").attr("disabled", true);
							$("#aggrSubmit").mouseout();
							return;
	                	}
                    }
                	//循环刷选
                    jQuery.each(aggrSvlanData, function(i, n) {
                        var tmp = "" + n[0];
                        if (aggrSvlanText == null || aggrSvlanText == "" || tmp.startWith(aggrSvlanText)) {
                            data[j] = n;
                            j++;
                        }
                    });
                    SvlanShowLength = j;
                    j = 0;	//empty j
                    aggrSvlanStore.loadData(data);
                    aggrSelectFirstRow();
                    aggrCvlanReload();
            	});
            });
		    //聚合CVLAN
            aggrCvlanStore = new Ext.data.SimpleStore({data: aggrCvlanData[aggrSvid], fields: ["aggrCvlanId"]});
            aggrCvlanGrid = new Ext.grid.GridPanel({
                cls:"normalTable edge10",stripeRows:true,region: "center",bodyCssClass: 'normalTable',
                renderTo: 'aggrCvlan_grid',
                width:248,
                height:150,
                title:"<p id=aggrlegend>@VLAN.aggregatedVlanList@</p>",
                border: false,
                autoScroll:true,
                columns:[{
                    header: I18N.VLAN.aggVlanBef + " ID" , width:140,dataIndex: "aggrCvlanId",align: "center", renderer: aggrCvlanChangeRed
                },{
                    header: I18N.COMMON.manu ,width:82,dataIndex: "id",align: "center",renderer: function(value, cellmeta, record) {
                    	if(operationDevicePower){
                    		return "<img src='/images/delete.gif' onclick='deleteCvlanAggrRule("+ aggrSvid + "," + record.data.aggrCvlanId + ")'/>";
                    	}else{
                    		return "<img src='/images/deleteDisable.gif'/>";
                    	}
                	}
                }],
                store: aggrCvlanStore
            })
            aggrCvlanGrid.getSelectionModel().on('rowselect', function(grid, rowIndex, e) {
                var tmpId = grid.selections.get(0).data.aggrCvlanId;
                selectedIndex = getAggrCvlanIndex(tmpId);
            })
            jQuery("#aggregationCvlanId").one("focus", function() {
            	jQuery("#aggregationCvlanId").keyup(function() {
                    var data = new Array()
                    aggrCvlanText = jQuery("#aggregationCvlanId").val()//在监听的事件发生时更新text
                    text = $("#aggregationCvlanId").val()//在监听的事件发生时更新text
                    var j = 0
                    //非空验证
                    if(aggrCvlanText==null || aggrCvlanText=="" || aggrCvlanText=='undefined'){
                        //为空，灰掉添加按钮
                    	$("#aggrSubmit").attr("disabled", true)
                    	$("#aggrSubmit").mouseout()
                    }else{
                        //激活 添加按钮
                    	if(vlanMode == 3){
                    		$("#aggrSubmit").attr("disabled", false)
                    	}
	            	    //输入验证
	            	    if(!checkedInput("aggregationCvlanId")){
							//灰掉submit按钮
							$("#aggrSubmit").attr("disabled", true)
							$("#aggrSubmit").mouseout()
							return
	                	}
                    }
                	//循环刷选
                    jQuery.each(aggrCvlanData[aggrSvid], function(i, n) {
                        var tmp = "" + n[0];
                        if (aggrCvlanText == null || aggrCvlanText == ""||tmp.startWith(text)) {
                        	data[j] = new Array();
                            data[j][0] = n[0];
                            j++;
                        }
                    });
                    j = 0;	//empty j
                    aggrCvlanStore.loadData(data);
            	});
            });
        }
    });
}
function deleteSvlanAggrRule(aggrSvid) {
	window.parent.showConfirmDlg(I18N.COMMON.tip, String.format(I18N.VLAN.cfmDelVlanAggRule , aggrSvid) , function(type) {
        if (type == 'no') {
            return
        }
        //var aggrSvlanDataTmp = aggrSvlanData.slice().remove(selectedIndex);
        window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.VLAN.delingAggRule , aggrSvid), 'ext-mb-waiting');
        $.ajax({
	        url: 'epon/vlan/deleteVlanAggregationRuleList.tv',
	        data: "uniId=" + uniId +"&aggrSvid=" + aggrSvid +"&entityId="+ entityId +"&num=" + Math.random(),
	        dataType:"text",
	        success: function(text) {
		        if(text == 'success'){
	            	window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.delAggRuleOk , aggrSvid))
	            	if(aggrSvlanGrid.getSelectionModel().getSelected()){
	            		var tmpI = aggrSvlanStore.indexOf(aggrSvlanGrid.getSelectionModel().getSelected());
	            		aggrSvlanData.splice(tmpI,1);
	            		aggrCvlanData[aggrSvid] = new Array();
	            		aggrSvlanStore.loadData(aggrSvlanData);
	            		aggrSvlanGrid.selModel.selectRow(0, true);
	            		aggrCvlanReload();
	            	}
	            	$("#aggrSubmit").attr("disabled", true);
	            	$("#aggrSubmit").mouseout();
				    $("#aggregationCvlanId").val("");
				    $("#aggregationCvlanId").focus();
		        }else{
		        	window.parent.showMessageDlg(I18N.COMMON.tip, text, 'error');
			    }
	        }, error: function(text) {
	        	window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.delAggRuleError , aggrSvid) ,'error');
	    }, cache: false
	    });
    });
}
function deleteCvlanAggrRule(svidAggrId, cvidAggrId) {
	window.parent.showConfirmDlg(I18N.COMMON.tip, String.format(I18N.VLAN.cfmDelAggRule , cvidAggrId , svidAggrId), function(type) {
        if (type == 'no') {
            return;            
        }
        var aggrCvlanDataTmp = aggrCvlanData[svidAggrId].slice();
        aggrCvlanDataTmp.splice(selectedIndex,1);
        //alert(aggrCvlanDataTmp);
        //aggrCvlanData[svidAggrId].remove(selectedIndex);
        window.top.showWaitingDlg(I18N.COMMON.wait,  String.format(I18N.VLAN.delingAggRule , svidAggrId), 'ext-mb-waiting');
        if(aggrCvlanDataTmp.length > 0){
        $.ajax({
	        url: 'epon/vlan/modifyVlanAggregationRuleList.tv',
	        type: 'POST',
	        data: "uniId=" + uniId + "&entityId=" + entityId +"&aggrSvid=" + svidAggrId + "&aggrCvids=" + aggrCvlanDataTmp.toString() +"&num=" + Math.random(),
	        dataType:"text",
	        success: function(text) {
		        if(text == 'success'){
	            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.udtAggRuleOk)
	            	//delete aggrCvlanData[svidAggrId][cvidAggrId];
	            	aggrCvlanData[svidAggrId].splice(selectedIndex,1);	            	
	            	aggrCvlanStore.loadData(aggrCvlanData[svidAggrId]);
			        $("#aggrSubmit").attr("disabled", true);
			        $("#aggrSubmit").mouseout();
			        $("#aggregationCvlanId").val("");
			        $("#aggregationCvlanId").focus();
		        }else{
		        	window.parent.showMessageDlg(I18N.COMMON.tip, text, 'error');
			    }
	        }, error: function(text) {
	        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.udtAggRuleError,'error');
	    }, cache: false
	    })
        }else{
        	$.ajax({
    	        url: 'epon/vlan/deleteVlanAggregationRuleList.tv',
    	        data: "entityId=" +entityId+ "&uniId=" + uniId +"&aggrSvid=" + aggrSvid + "&num=" + Math.random(),
    	        dataType:"text",
    	        success: function(text) {
    		        if(text == 'success'){
    	            	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.delAggRuleOk2 )
    	            	aggrSvlanData.removeObj(aggrSvid);
    	            	aggrSvlanStore.loadData(aggrSvlanData);
    	            	aggrCvlanData[svidAggrId].splice(selectedIndex,1);
    	            	aggrCvlanStore.loadData(aggrCvlanData[svidAggrId]);
    	            	aggrSelectFirstRow();    	            	
    	            	$("#aggrSubmit").attr("disabled", true);
    	            	$("#aggrSubmit").mouseout();
    				    $("#aggregationCvlanId").val("");
    				    $("#aggregationCvlanId").focus();
    		        }else{
    		        	window.parent.showMessageDlg(I18N.COMMON.tip, text, 'error');
    			    }
    	        }, error: function(text) {
    	        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.delAggRuleError2 ,'error')
    	    }, cache: false
    	    });
        }
    });
}
function onAggrGridRendered(){
	  setTimeout(function(){
		var model = Ext.getCmp("aggrSvlanGrid").getSelectionModel();
		model.selectFirstRow();
		aggrGridClick();
	  },200)
}
function aggrGridClick(){
	var record = aggrSvlanGrid.getSelectionModel().getSelected();
	if(record != null || record != undefined){
		aggrSvid = record.get('aggrSvlanId');
		$("#aggrlegend").text(String.format(I18N.VLAN.aggregatedVlanList2 , aggrSvid ))
		aggrCvlanStore.loadData(aggrCvlanData[aggrSvid]);
		$("#aggregationSvlanId").val(aggrSvid);
	}
}
function aggrReady(){
	aggrSelectFirstRow();
	aggrGridClick();
    SvlanShowLength = aggrSvlanData.length;
}
function aggrSelectFirstRow() {
    var model = Ext.getCmp("aggrSvlanGrid").getSelectionModel();
    model.selectFirstRow();
    //aggrGridClick();
}
function aggrSvlanChangeRed(value, a, b, c) {
    if (value.toString().redMatchWith($("#aggregationSvlanId").val())) {
        $("#aggrSubmit").attr("disabled", true);
        $("#aggrSubmit").mouseout();
        aggrSvlanGrid.selModel.selectRow(0, true);
        return "<font color='red'>" + value + "</font>";
    } else {
        return value;
    }
}

function aggrCvlanChangeRed(value, a, b, c) {
    if (value.toString().redMatchWith($("#aggregationCvlanId").val())) {
        $("#aggrSubmit").attr("disabled", true);
        $("#aggrSubmit").mouseout();
        return "<font color='red'>" + value + "</font>";
    } else {
        return value;
    }
}
function getAggrSvlanIndex(aggrSvlanId) {
    var tmpIndex = -1;
    for (var i = 0; i < aggrSvlanData.length; i++) {
        if (aggrSvlanData[i][0] == aggrSvlanId) {
            tmpIndex = i;
            break;
        }
    }
    return tmpIndex;
}
function getAggrCvlanIndex(aggrCvlanId) {
    var tmpIndex = -1;
    for (var i = 0; i < aggrCvlanData.length; i++) {
        if (aggrCvlanData[aggrSvid][i][0] == aggrCvlanId) {
            tmpIndex = i;
            break;
        }
    }
    return tmpIndex;
}
function aggrCvlanReload(){
	if(aggrSvlanGrid.getSelectionModel().getSelected()==null || aggrSvlanGrid.getSelectionModel().getSelected()=='undefined'){
		aggrSvid = 0;
		$("#aggrlegend").text(I18N.VLAN.aggVlanNull);
	    aggrCvlanStore.loadData(aggrCvlanData[aggrSvid]);
	}else{
	    var record = aggrSvlanGrid.getSelectionModel().getSelected();
	    aggrSvid = record.get('aggrSvlanId');
	    if (aggrSvid == null || aggrSvid == 'undefined' || aggrSvid == "") {
		    //防止空指针
	        aggrSvid = 0;
	        aggrCvlanStore.loadData(aggrCvlanData[aggrSvid]);
	    }else{
		    $("#aggrlegend").text(String.format(I18N.VLAN.aggregatedVlanList2 ,aggrSvid ))
		    aggrCvlanStore.loadData(aggrCvlanData[aggrSvid])
	    }
	}	
}
function aggrAdd(){
	  var totalCvlan = new Array()
	  for(var i = 0;i<aggrCvlanData.length;i++ ){
		  if(!(!!aggrCvlanData[i]))continue
		  for(var j=0;j<aggrCvlanData[i].length;j++){
			  //alert(!!aggrCvlanData[i][j]);
			  if(!!aggrCvlanData[i][j] &&'undefined'!=aggrCvlanData[i][j]){
				  totalCvlan.add(aggrCvlanData[i][j])
			  }else{				  
				  continue
			  }
		  }
	  }
	  if ($("#aggregationCvlanId").val() == null || $("#aggregationCvlanId").val() == 'undefined' || $("#aggregationCvlanId").val() == "") {
	        $("#aggrSubmit").attr("disabled", true)
	        $("#aggrSubmit").mouseout()
	    } else {
	    	if(totalCvlan.contains($("#aggregationCvlanId").val()))
			{
				// 已存在相同的CVLAN 提示不能更新
				// 提示后循环结束
				window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.alreadyAggredVlanTip , $("#aggregationCvlanId").val()))
				$("#aggrSubmit").attr("disabled", true);
				$("#aggrSubmit").mouseout();
			    $("#aggregationCvlanId").val("");
			    $("#aggregationCvlanId").focus();
			    aggrGridClick();
				return;
			}
	    	aggrSvid = parseInt($("#aggregationSvlanId").val());
	        textCid = $("#aggregationCvlanId").val();
	        if(parseInt(aggrSvid) == parseInt(textCid)){
	        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.vlanSameTip)
		  		return;
	        }
			if(aggrSvlanData.contains($("#aggregationSvlanId").val())){
				//更新操作
				if(getTheVlanNum() > 7){
			  		  window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.ruleOver8)
			  		  return;
			  	}
		        var aggrCvlanDataTmp = aggrCvlanData[aggrSvid].slice(0);
		        aggrCvlanDataTmp[aggrCvlanData[aggrSvid].length] = [textCid];
		        // 验证通过
		       	window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.VLAN.addingAggVlanRule , aggrSvid ), 'ext-mb-waiting');
				 $.ajax({
				        url: 'epon/vlan/modifyVlanAggregationRuleList.tv',
				        type: 'POST',
				        data: "uniId=" + uniId + "&entityId=" + entityId + "&aggrSvid=" + aggrSvid + "&aggrCvids=" + aggrCvlanDataTmp.toString() + "&num=" + Math.random(),
				        dataType:"text",
				        success: function(text) {
					        if(text == 'success'){
					        	window.parent.closeWaitingDlg();
					        	aggrSvlanStore.loadData(aggrSvlanData);
					        	aggrCvlanData[aggrSvid].add([textCid]);
					        	aggrCvlanStore.loadData(aggrCvlanData[aggrSvid]);
					        	//SvlanShowLength = aggrSvlanData.length;
					            aggrSvlanGrid.selModel.selectRow(aggrSvlanGrid.getStore().find('aggrSvlanId',$("#aggregationSvlanId").val()),true);
					        	//aggrSvlanGrid.getSelectionModel().selectLastRow();
						        window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.alreadyAggredVlan , textCid, $("#aggregationSvlanId").val()))
					        }else{
					        	//未成功删除添加的aggrSvid数组
					        	aggrSvlanData.remove(aggrSvlanData.length - 1);
					        	window.parent.showMessageDlg(I18N.COMMON.tip, text, 'error');
						    }
				        }, error: function(text) {
				        	//未成功删除添加的aggrSvid数组
				        	aggrSvlanData.remove(aggrSvlanData.length - 1);
				        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.addAggRuleError ,'error')
				    }, cache: false
				    });
			}else{
				//新增操作
				aggrCvlanData[aggrSvid] = new Array();
				if(getTheVlanNum() > 7){
			  		  window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.ruleOver8 )
			  		  return;  
			  	}
	            aggrSvlanData[aggrSvlanData.length] = [$("#aggregationSvlanId").val()];
		        var aggrCvlanDataTmp = aggrCvlanData[aggrSvid].slice(0);
		        aggrCvlanDataTmp[aggrCvlanData[aggrSvid].length] = [textCid];
		        // 验证通过
		       	window.top.showWaitingDlg(I18N.COMMON.wait, String.format(I18N.VLAN.addingAggVlanRule , aggrSvid), 'ext-mb-waiting')
				 $.ajax({
				        url: 'epon/vlan/addVlanAggregationRuleList.tv',
				        type: 'POST',
				        data: "uniId=" + uniId + "&entityId=" + entityId + "&aggrSvid=" + aggrSvid + "&aggrCvids=" + aggrCvlanDataTmp.toString() + "&num=" + Math.random(),
				        dataType:"text",
				        success: function(text) {
					        if(text == 'success'){
					        	window.parent.closeWaitingDlg();
					        	aggrSvlanStore.loadData(aggrSvlanData);
					        	aggrCvlanData[aggrSvid][aggrCvlanData[aggrSvid].length] = aggrCvlanDataTmp;;
					        	//aggrCvlanData[aggrSvid] = textCid;
					        	window.location.reload();
					        	aggrCvlanStore.loadData(aggrCvlanData[aggrSvid]);
					        	aggrSvlanGrid.getSelectionModel().selectLastRow();
						        window.parent.showMessageDlg(I18N.COMMON.tip, String.format(I18N.VLAN.alreadyAggredVlan , textCid ,$("#aggregationSvlanId").val() ))
					        }else{
					        	//未成功删除添加的aggrSvid数组
					        	aggrSvlanData.remove(aggrSvlanData.length - 1);
					        	window.parent.showMessageDlg(I18N.COMMON.tip, text, 'error');
						    }
				        }, error: function(text) {
				        	//未成功删除添加的aggrSvid数组
				        	aggrSvlanData.remove(aggrSvlanData.length - 1);
				        	window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.addAggRuleError ,'error');
				    }, cache: false
				});
			}
			$("#aggrSubmit").attr("disabled", true);
			$("#aggrSubmit").mouseout();
		    $("#aggregationCvlanId").val("");
		    $("#aggregationCvlanId").focus();
        	//新增带SVLAN时使用下面两行代码
            //aggrCvlanData[aggrSvid] = new Array();
            //aggrSvlanData[aggrSvlanData.length] = [$("#aggregationSvlanId").val()];
            //aggrSvlanStore.loadData(aggrSvlanData);
            //SvlanShowLength = aggrSvlanData.length;
            //aggrSvlanGrid.selModel.selectRow(SvlanShowLength - 1, true);
	}	    
}
function getTheVlanNum(){
	var num = 0;
	for(var x in aggrCvlanData){
		if(x && aggrCvlanData[x]){
			for(var y in aggrCvlanData[x]){
				if(y && aggrCvlanData[x][y] && !isNaN(aggrCvlanData[x][y])){
					num++;
				}
			}
		}
	}
	return num;
}