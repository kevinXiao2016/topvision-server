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
	
	//show delete picture
	var manuRenderer = function(value, cellmeta, record) {
		return "<a href='javascript:;' onclick='deleteSyslogPorts()' >@COMMON.del@</a>";
	}
	//delete syslogListenports from Grid
	function deleteSyslogPorts(){
		var select = syslogGrid.getSelectionModel().getSelected();
		dataStore.remove(select);
		changeFlag = true;
	}
	
	//check the add port input 
	function checkTrapPort() {
		var reg = /^[1-9]\d*$/;
		var value = $.trim($("#syslogPort").val());
		
		if(!value){
			/* top.afterSaveOrDelete({
		      title: '@sys.tip@',
		      html: '<b class="orangeTxt">@sys.addPortInput@</b>'
		    }); */
			window.parent.showMessageDlg("@sys.tip@","@sys.addPortInput@",null,function(){
				$("#syslogPort").focus();
			});
			return false;
		}
		if (reg.exec(value) && parseInt(value) <= 65535 && parseInt(value) >= 1) {
			return true;
		} else {
			$("#syslogPort").focus();
			return false;
		}
	}

	//add syslogListenports to Grid
	function addPort(){
		if(checkTrapPort()){
			var portValue = $("#syslogPort").val();
			var addRecord = [{port:portValue}];
			
			if(dataStore.getCount() < 5){
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
						$("#syslogPort").focus();
					});
					return;
				}else{
					dataStore.loadData(addRecord,true);
					changeFlag = true;
				}
			}else{
				window.parent.showMessageDlg("@sys.tip@", "@sys.totalTip@");
				return;
			}
		}else{
			return;
		}
		
	}
	 
	//save the syslogListenports config
	var saveFlag = false;
	function okClick(){
		var result = new Array();
		dataStore.each(function(rec){
			if(rec.data.port){
				result.push(rec.data.port);
			}
		});
		var savedData = result.join(",");
		
		window.top.showWaitingDlg("@sys.waiting@", "@sys.saveWaiting@");
		saveFlag = true;
		$.ajax({
			url : 'saveSyslogListenports.tv',
			type : 'POST',
			data : "syslogListenports=" + savedData,
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
			window.parent.showConfirmDlg("@sys.tip@", "@sys.closeConfirm@", function(type) {
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
			url : '/system/loadSyslogListenports.tv',
		    fields: ['port']
		});
		//load data from proxy
		dataStore.load();
	
		window.colModel = new Ext.grid.ColumnModel([ 
		    {header : "@sys.listenPorts@",width : 190,align: 'center',sortable : false,dataIndex : 'port'}, 
		    {header : "@sys.action@",width : 190,align: 'center',sortable : false,dataIndex : 'id',renderer: manuRenderer
		    }
		]);
		
		window.syslogGrid = new Ext.grid.GridPanel({
			id : 'syslogPortGrid',
			renderTo: 'syslogListenportsGrid',
			height: 200,
			cls : 'normalTable',
			store: dataStore,
			colModel: colModel,
			viewConfig : {
				forceFit : true
			},
			sm: new Ext.grid.RowSelectionModel({singleSelect: true})
			
		});	
		
	});
	
</script>
</head>
<body class="openWinBody">
	<div class="edge10">
		<p  id="content_tip" class="pB10 blueTxt">@sys.syslogContentTip@</p>
		<div id="syslogListenportsGrid"></div>
		<div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
		    <ol class="upChannelListOl pB0 pT20 noWidthCenter">
		    	<li class="pT8">
		    		<label for="syslogPort">@sys.listenPorts@:</label>
		    	</li>
		    	<li class="pT4">
		    		<input style="width: 80px;" id="syslogPort" name="syslogPort" class="normalInput"
					 toolTip="@sys.syslogPortFocus@" />
		    	</li>
		        <li>
		            <a href="javascript:;" class="normalBtnBig"  id="addPort"  onclick="addPort()">
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
