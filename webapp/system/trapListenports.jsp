<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<Zeta:HTML>
<%@include file="/include/ZetaUtil.inc"%>
 <Zeta:Loader>
	library ext
	library jquery
	library zeta
	module platform
    import js.tools.ipText
</Zeta:Loader>
<head>
<script>
	//To identify the data in Grid is changed or not
	var changeFlag = false;
	
	//show the delete picture
	var manuRenderer = function(value, cellmeta, record) {
		return "<a href='javascript:;' onclick='deleteTrapPorts()' >@COMMON.del@</a>";
	}
	//delete trapListenports from the Grid
	function deleteTrapPorts(){
		var select = trapGrid.getSelectionModel().getSelected();
		dataStore.remove(select);
		changeFlag = true;
	}
	
	//check the add port input 
	function checkTrapPort() {
		var reg = /^[1-9]\d*$/;
		var value = $.trim($("#trapPort").val());
		
		if(!value){
			/* top.afterSaveOrDelete({
		      title: '@sys.tip@',
		      html: '<b class="orangeTxt">@sys.addPortInput@</b>'
		    }); */
			window.parent.showMessageDlg("@sys.tip@","@sys.addPortInput@",null,function(){
				$("#trapPort").focus();
			});
			return false;
		}else if (reg.exec(value) && parseInt(value) <= 65535 && parseInt(value) >= 1) {
			return true;
		} else {
			$("#trapPort").focus();
			return false;
		}
	}

	//add trapListenPorts to Grid
	function addClick() {
		if(checkTrapPort()){
			var portValue = $("#trapPort").val();
			var addList = [ {port : portValue} ];

			if (dataStore.getCount() < 5) {
				var flag = false;
				dataStore.each(function(record){
					if(record.data.port == portValue){
						flag = true;
						return false;
					}
				});
				if(flag){
					/* top.afterSaveOrDelete({
				      title: '@sys.tip@',
				      html: '<b class="orangeTxt">@sys.repeatTip@</b>'
				    }); */
					window.parent.showMessageDlg("@sys.tip@", "@sys.repeatTip@",null,function(){
						$("#trapPort").focus();
					});
					return;
				}else {
					dataStore.loadData(addList, true);
					changeFlag = true;
				}
			} else {
				window.parent.showMessageDlg("@sys.tip@", "@sys.totalTip@");
				return;
			}
		}else{
			return;
		}
		
	}
	
	//save the trapListenports config
	var saveFlag = false;
	function okClick() {
		var result = new Array;
		dataStore.each(function(rec) {
			if (rec.data.port)
				result.push(rec.data.port);
		});
		var savedData = result.join(",");
		
		window.top.showWaitingDlg("@sys.waiting@", "@sys.saveWaiting@");
		saveFlag = true;
		
		$.ajax({
			url : 'saveTrapListenports.tv',
			type : 'POST',
			data : "trapListenports=" + savedData,
			success : function(json) {
				window.top.closeWaitingDlg();
				top.afterSaveOrDelete({
			      title: '@sys.tip@',
			      html: '<b class="orangeTxt">@sys.saved@</b>'
			    });
				//window.parent.showMessageDlg("@sys.tip@","@sys.saved@");
				cancelClick();
			},
			error : function(json) {
				window.top.closeWaitingDlg();
				window.top.showErrorDlg();
			},
			cache : false
		});

	}

	//close the dialog                                                                                                                                   
	function cancelClick() {
		if(changeFlag && !saveFlag){
			window.parent.showConfirmDlg("@sys.tip@", "@sys.closeConfirm@" , function(type) {
				if (type == 'no') {
					return;
				}else{
					window.top.closeWindow('modalDlg');
				}
			});
		}else{
			window.top.closeWindow('modalDlg');
		}
	}

	Ext.onReady(function() {
		window.dataStore = new Ext.data.JsonStore({
			url : '/system/loadTrapListenports.tv',
			fields : [ 'port' ]
		});
		//load data from proxy
		dataStore.load();

		window.colModel = new Ext.grid.ColumnModel([ {
			header : "@sys.listenPorts@",
			width : 190,
			align : 'center',
			sortable : false,
			dataIndex : 'port'
		}, {
			header : "@sys.action@",
			width : 190,
			align : 'center',
			sortable : false,
			dataIndex : 'id',
			renderer : manuRenderer
		} ]);

		window.trapGrid = new Ext.grid.GridPanel({
			id : 'trapPortGrid',
			renderTo : 'trapListenportsGrid',
			height : 200,
			cls : 'normalTable',
			store : dataStore,
			colModel : colModel,
			viewConfig : {
				forceFit : true
			},
			sm : new Ext.grid.RowSelectionModel({
				singleSelect : true
			})

		});
	});
</script>
</head>
<body class="openWinBody">
	<div class=formtip id=tips style="display: none"></div>
	<div class="edge10">
		<p class="pB10 blueTxt" id="content_tip">@sys.trapContentTip@</p>	
		<div id="trapListenportsGrid"></div>
		<div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
		    <ol class="upChannelListOl pB0 pT20 noWidthCenter">
		    	<li class="pT8">
		    		<label for="trapPort">@sys.listenPorts@:</label>
		    	</li>
		    	<li class="pT4">
		    		<input style="width: 80px;" class="normalInput"
					id="trapPort" name="trapPort" maxlength="5"
					toolTip="@sys.trapPortFocus@" />
		    	</li>
		        <li>
		            <a href="javascript:;" class="normalBtnBig" onclick="addClick()"  id="addPort">
		                <span>
		                    <i class="miniIcoAdd">
		                    </i>
		                    @sys.add@
		                </span>
		            </a>
		        </li>
		        <li>
		            <a href="javascript:;" class="normalBtnBig"  onclick="okClick()">
		                <span>
		                    <i class="miniIcoData">
		                    </i>
		                    @sys.save@
		                </span>
		            </a>
		        </li>
		        <li>
		            <a href="javascript:;" class="normalBtnBig" onclick="cancelClick()">
		                <span>
		                	<i class="miniIcoForbid">
		                    </i>
		                    @sys.cancel@
		                </span>
		            </a>
		        </li>
		    </ol>
		</div>
	</div>
	
</body>
</Zeta:HTML>