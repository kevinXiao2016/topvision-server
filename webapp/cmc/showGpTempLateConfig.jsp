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
	module cmc
</Zeta:Loader>
<head>
<style type="text/css">
#w700{ width:700px; overflow:hidden; position: relative; top:0; left:0; height:400px;}
#w1400{ width:1400px; position:absolute; top:0; left:0;}
#step0, #step1{width:700px; overflow:hidden; position:absolute; top:0px; left:0px;}
#step1{ left:700px;}
.channelSelect{
	height: 21px;
    margin-left: 30px;
    width: 180px;}
.contentTable {
	border-collapse: collapse;
	width: 100%;
	white-space: nowrap;
	word-spacing: normal;
	word-break: keep-all;
}
.contentTable td,.contentTable th {
	border-top-width: 0;
	border-left-width: 0;
	border-right-width: 0;
	border-bottom: 1px solid #D6D6D6;
	height: 40px;
	padding: 4px 3px;
	vertical-align: middle;
	white-space: nowrap;
	word-spacing: normal;
	word-break: keep-all;
}
.contentTable td input,.contentTable td span {
	vertical-align: middle;
}
</style>

<script type="text/javascript">
	var pageSize = <%= uc.getPageSize() %>;
    var tempLateId = '<s:property value="tempLateId"/>';
	
	function cancelClick() {
		window.parent.closeWindow("showTemplateConfig");
	}
	
	var currentRecvMessageSize = 0;
	window.parent.addCallback("message",function(r){
		currentRecvMessageSize++;
		if(transacteEntities.length == currentRecvMessageSize){
			window.parent.showMessageDlg("@COMMON.tip@","@CMC.GP.configFinished@");
			cancelClick();
			currentRecvMessageSize = 0;
		}
	},"spectrumGroupConfig");
	
	function saveClick(){
		var selectArray =  grid.getSelectionModel().getSelections();
		var idArray = new Array();
		Ext.each(selectArray,function(item){
			idArray.push(item.data.entityId);
		});
		window.transacteEntities = idArray;
		var entityIds = idArray.join(",");
		
		var chnlGroup1 = $('#chnlGroup1').val();
		var chnlGroup2 = $('#chnlGroup2').val();
		var chnlGroup3 = $('#chnlGroup3').val();
		var chnlGroup4 = $('#chnlGroup4').val();
		
		//提交到后台进行处理
		window.top.showConfirmDlg('@COMMON.tip@', "@CMC.GP.configConfirm@", function(type) {
			if (type == 'no') {
				return;
			}else{
				window.top.showWaitingDlg("@COMMON.wait@", "@CMC.GP.onConfiging@");
				$.ajax({
					url : '/ccmtsspectrumgp/confSpectrumGpTempLate.tv',
					type : 'POST',
					data : "entityIds=" + entityIds +"&gpForUpChannel1="+chnlGroup1+"&gpForUpChannel2="+chnlGroup2+"&gpForUpChannel3="+chnlGroup3+"&gpForUpChannel4="+chnlGroup4 +"&tempLateId="+tempLateId+"&num=" + Math.random(),
					success : function() {
						store.reload();
					},
					error : function() {
						window.parent.showMessageDlg("@COMMON.tip@","@CMC.GP.configFailed@");
					},
					cache : false
				});
			}
		});
	}
	
	//初始化跳频组设置选择
	var	GROUP_OPTION_FMT = '<option value="{0}">{1}</option>';
	function initData(){
		$.ajax({
			url : '/ccmtsspectrumgp/loadTempRelationGp.tv?r=' + Math.random(),
			type : 'POST',
			data : {
				tempLateId : tempLateId
			},
			dataType : 'json',
			success : function(json) {
				$('select').append(String.format(GROUP_OPTION_FMT, 0, "@CMC.CHANNEL.cancelGp@"));
				for(var i=0; i<json.length;i++){
					var groupId = json[i].deviceGroupId;
					var emsGroupName = json[i].emsGroupName;
					$('select').append(String.format(GROUP_OPTION_FMT, groupId, emsGroupName));
				}
			},
			error : function(json) {
				window.top.showErrorDlg();
			},
			cache : false
		});
	}
	
	function renderOnlie(value, p, record){
		if (value) {
			return String.format('<img alt="{0}" title="{0}" src="../images/fault/trap_on.png" border=0 align=absmiddle>',
				'@resources/COMMON.online@');	
		} else {
			return String.format('<img alt="{0}" title="{0}" src="../images/fault/trap_off.png" border=0 align=absmiddle>',
				'@resources/COMMON.unreachable@');	
		}
	}
	
	function renderManagement(value, p, record){
		if (value) {
			return String.format('<img alt="{0}" title="{0}" src="../images/fault/online.gif" border=0 align=absmiddle>',
				'@resources/COMMON.manageble@');	
		} else {
			return String.format('<img alt="{0}" title="{0}" src="../images/fault/offline.gif" border=0 align=absmiddle>',
				'@resources/COMMON.unmanageble@');	
		}
	}
	
	function buildPagingToolBar() {
		pagingToolbar = new Ext.PagingToolbar({pageSize: pageSize, store:store, displayInfo:true,
			items: ["-", String.format("@COMMON.pagingTip@", pageSize), '-']});
		return pagingToolbar;
	}
	
	Ext.onReady(function() {
		var reader = new Ext.data.JsonReader({
		    totalProperty: "rowCount",
		    idProperty: "entityId",
		    root: "data",
	        fields: [
				{name: 'entityId'},
			    {name: 'name'},
			    {name: 'ip'},
			    {name: 'typeId'},
			    {name: 'typeName'},
			  	//在线状态
			    {name: 'state'},
			  	//管理状态
			    {name: 'status'},
			    {name: 'snapTime'}
	        ]
		});
Ext.override(Ext.grid.GridView,{  
		    
		    onRowSelect : function(row){  
		        this.addRowClass(row, "x-grid3-row-selected");  
		        var selected = 0;  
		        var len = this.grid.store.getCount();  
		        for(var i = 0; i < len; i++){  
		            var r = this.getRow(i);  
		            if(r){  
		               if( this.fly(r).hasClass('x-grid3-row-selected'))selected = selected + 1;  
		            }  
		        }  
		          
		        var hd = this.grid.getEl().select('div.x-grid3-hd-checker').first();     
		          
		        if (selected == len && !hd.hasClass('x-grid3-hd-checker-on')) {  
		             hd.addClass('x-grid3-hd-checker-on');   
		        }  
		    },  
		  
		    onRowDeselect : function(row){  
		        this.removeRowClass(row, "x-grid3-row-selected");  
		            var selected = 0;  
		            var len = this.grid.store.getCount();  
		            for(var i = 0; i < len; i++){  
		                var r = this.getRow(i);  
		                if(r){  
		                   if( this.fly(r).hasClass('x-grid3-row-selected'))selected = selected + 1;  
		                }  
		            }  
		            var hd = this.grid.getEl().select('div.x-grid3-hd-checker').first();     
		              
		            if (hd != null && selected != len && hd.hasClass('x-grid3-hd-checker-on')) {  
		                 hd.removeClass('x-grid3-hd-checker-on');   
		            }  
		    }  
		});  
		var sm = new Ext.grid.CheckboxSelectionModel(); 
		var columnModels = [sm,
       		{header: "@resources/COMMON.alias@",align:'center', width:80, sortable:true,dataIndex:'name'},
       	 	{header: "@network/NETWORK.typeHeader@", align:'center',width:50,sortable:true,dataIndex:'typeName'},
       	    {header: "@network/NETWORK.ipHeader@", align:'center',width:80, sortable:true,dataIndex: 'ip'},
       	    {header: "@network/NETWORK.onlineStatus@", align:'center',width:60,sortable:false,dataIndex: 'state',renderer: renderOnlie},
       	    {header: "@network/NETWORK.manageStatus@", align:'center',width:60,sortable:false,dataIndex: 'status',renderer: renderManagement}
       	];
		window.store = new Ext.data.Store({
			url : '/entity/loadEntitySnapList.tv?r=' + Math.random(),
			reader: reader,		
			remoteSort : false
		});
		
		store.on("load",function(){
	        setTimeout(function(){
	        	//得到表格的EL对象
	        	var gridEl = this.grid.getEl();
	        	//得到表格头部的全选CheckBox框
	    		var hd = gridEl.select('div.x-grid3-hd-checker');
	    		hd.removeClass('x-grid3-hd-checker-on');
	         },100)
		})
		
		window.grid = new Ext.grid.GridPanel({
			//title: "@CMC.GP.selectedDevice@",
			store : store,
			width: 680, 
			height: 310,
			border:true,
			bodyCssClass:'normalTable',
			columns: columnModels,
			sm: sm,
			bbar: buildPagingToolBar(),
			renderTo : "groupGrid",
			viewConfig: {
		        forceFit: true
			}
		});
			
		store.load({params:{start: 0, limit: pageSize}});
		
		//初始化下拉框选择
		initData()
		
		$("#nextBt").click(function(){
			//必须要选择设备
			var selectFlag = grid.getSelectionModel().hasSelection();
			if(selectFlag){
				$("#w1400").animate({left:-700},'fast');		
			}else{
				window.parent.showMessageDlg("@COMMON.tip@","@CMC.GP.selectDeviceTip@");
				return;
			}
		});//点击下一步;
		
		$("#prevBt").click(function(){
			$("#w1400").animate({left:0},'normal');		
		});//点击上一步;
	});
	
	function onMonuseDown(e, t){
		if(e.button === 0 && t.className == 'x-grid3-row-checker'){ // Only fire if left-click
			e.stopEvent();
			var row = e.getTarget('.x-grid3-row');

			// mouseHandled flag check for a duplicate selection (handleMouseDown) call
			if(!this.mouseHandled && row){
				//alert(this.grid.store.getCount());
				var gridEl = this.grid.getEl();//得到表格的EL对象
				var hd = gridEl.select('div.x-grid3-hd-checker');//得到表格头部的全选CheckBox框
				var index = row.rowIndex;
				if(this.isSelected(index)){
					this.deselectRow(index);
					var isChecked = hd.hasClass('x-grid3-hd-checker-on');
					//判断头部的全选CheckBox框是否选中，如果是，则删除
					if(isChecked){
						hd.removeClass('x-grid3-hd-checker-on');
					}
				}else{
					this.selectRow(index, true);
					//判断选中当前行时，是否所有的行都已经选中，如果是，则把头部的全选CheckBox框选中
					if(gridEl.select('div.x-grid3-row-selected').elements.length==gridEl.select('div.x-grid3-row').elements.length){
						hd.addClass('x-grid3-hd-checker-on');
					};
				}
			}
		}
		this.mouseHandled = false;
	}

	function onHdMouseDown(e, t){
		/**
		*大家觉得上面重写的代码应该已经实现了这个功能了，可是又发现下面这行也重写了
		*由原来的t.className修改为t.className.split(' ')[0]
		*为什么呢？这个是我在快速点击头部全选CheckBox时，
		*操作过程发现，有的时候x-grid3-hd-checker-on这个样式还没有删除或者多一个空格，结果导致下面这个判断不成立
		*去全选或者全选不能实现
		*/
		if(t.className.split(' ')[0] == 'x-grid3-hd-checker'){
		    e.stopEvent();
		    var hd = Ext.fly(t.parentNode);
		    var isChecked = hd.hasClass('x-grid3-hd-checker-on');
		    if(isChecked){
		        hd.removeClass('x-grid3-hd-checker-on');
		        this.clearSelections();
		    }else{
		        hd.addClass('x-grid3-hd-checker-on');
		        this.selectAll();
		    }
		}
	}

	Ext.override(Ext.grid.CheckboxSelectionModel,{
	    onMouseDown : onMonuseDown,
		onHdMouseDown : onHdMouseDown
	});
</script>
</head>
<body class="openWinBody">
<div id="w700">
	<div id="w1400">
		<div id="step0" style="margin: 10px 15px;">
			<div class="flagInfo">@CMC.GP.selectedDevice@</div>
			<div id="groupGrid"></div>
			
			<div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
			    <ol class="upChannelListOl pB0 pT20 noWidthCenter">
			        <li>
			            <a href="javascript:;" class="normalBtnBig" id="nextBt">
			                <span><i class="miniIcoArrRight"></i>@CMC.GP.nextStep@</span>
			            </a>
			        </li>
			        <li>
			            <a href="javascript:;" class="normalBtnBig" id="cancelBt" onclick="cancelClick()">
			                <span>@COMMON.cancel@</span>
			            </a>
			        </li>
			    </ol>
			</div>
		</div>
		<div id="step1">
			<div class="openWinHeader">
			    <div class="openWinTip">@CMC.GP.chnlGroupTip@</div>
			    <div class="rightCirIco wheelCirIco"></div>
			</div>
			<div class="edgeTB10LR20 pT30">
				<table class="contentTable" cellpadding="0" cellspacing="0" border="0">
				    <tbody>
				       <tr>
				           <td class="rightBlueTxt" width="200">@CMC.GP.upChannel1@:</td>
							<td>
								<select id="chnlGroup1" class="channelSelect" name="chnlGroup1"></select>
							</td>	
				       </tr>
				       <tr class="darkZebraTr">
					       	<td class="rightBlueTxt" width="200">@CMC.GP.upChannel2@:</td>
							<td>
								<select id="chnlGroup2" class="channelSelect" name="chnlGroup2" ></select>
							</td>	
				       </tr>
				       <tr>
					       	<td class="rightBlueTxt" width="200">@CMC.GP.upChannel3@:</td>
							<td>
								<select id="chnlGroup3" class="channelSelect" name="chnlGroup3" ></select>
							</td>	
						</tr>
				        <tr class="darkZebraTr">
				           <td class="rightBlueTxt" width="200">@CMC.GP.upChannel4@:</td>
							<td>
								<select id="chnlGroup4" class="channelSelect" name="chnlGroup4"></select>
							</td>	
				        </tr>
				     </tbody>
				 </table>
			</div> 
		    <div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
			    <ol class="upChannelListOl pB0 pT40 noWidthCenter">
			        <li>
			            <a href="javascript:;" class="normalBtnBig" id="prevBt">
			                <span><i class="miniIcoArrLeft"></i>@CMC.GP.prevStep@</span>
			            </a>
			        </li>
			        <li>
			            <a href="javascript:;" class="normalBtnBig" id="saveBt" onclick="saveClick()">
			                <span><i class="miniIcoSaveOK"></i>@COMMON.ok@</span>
			            </a>
			        </li>
			        <li>
			            <a href="javascript:;" class="normalBtnBig" id="cancelBt" onclick="cancelClick()">
			                <span>@COMMON.cancel@</span>
			            </a>
			        </li>
			    </ol>
			</div>
		</div>
	</div>
</div>
</body>
</Zeta:HTML>