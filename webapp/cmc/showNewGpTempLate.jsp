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
	css cmc.showNewGpTempLate
</Zeta:Loader>
<head>

<script type="text/javascript">
	var groupModifyFlag = '<s:property value="groupModifyFlag"/>';
	var groupIds = [[1],[2],[3],[4],[5],[6],[7],[8],[9],[10],[11],[12],[13],[14],[15],[16],[17],[18],[19],[20],[21],[22],[23],[24],[25],[26],[27],[28],[29],[30],[31],[32]];
	var groupTemplateJson =  ${groupTemplateJson}? ${groupTemplateJson} : new Array();
    var emsCcmtsSpectrumGpListJson = ${emsCcmtsSpectrumGpListJson}? ${emsCcmtsSpectrumGpListJson} : new Array();
    var tempLateId = '<s:property value="tempLateId"/>';
	var grid = null;
	var store = null;
	var toolbar= null;
	var groupData = new Array();
	var groupIdStore = null;
	
	//待添加的第二备份调制方式选项
	var optionObj = [
	  	{priority:30,optionString:'<option value="1" priority="30">QPSK-Fair-Scdma</option>'},
	  	{priority:25,optionString:'<option value="2" priority="25">QAM16-Fair-Scdma</option>'},
	  	{priority:15,optionString:'<option value="3" priority="15">QAM64-Fair-Scdma</option>'},
	  	{priority:10,optionString:'<option value="4" priority="10">QAM256-Fair-Scdma</option>'},
	  	{priority:29,optionString:'<option value="5" priority="29">QPSK-Good-Scdma</option>'},
	  	{priority:24,optionString:'<option value="6" priority="24">QAM16-Good-Scdma</option>'},
	  	{priority:14,optionString:'<option value="7" priority="14">QAM64-Good-Scdma</option>'},
	  	{priority:9,optionString:'<option value="8" priority="9">QAM256-Good-Scdma</option>'},
	  	{priority:13,optionString:'<option value="9" priority="13">QAM64-Best-Scdma</option>'},
	  	{priority:10,optionString:'<option value="10" priority="9">QAM256-Best-Scdma</option>'},
	  	{priority:29,optionString:'<option value="11" priority="29">QPSK-Good-Atdma</option>'},
	  	{priority:24,optionString:'<option value="12" priority="24">QAM16-Good-Atdma</option>'},
	  	{priority:14,optionString:'<option value="13" priority="14">QAM64-Good-Atdma</option>'},
	  	{priority:9,optionString:'<option value="14" priority="9">QAM256-Good-Atdma</option>'}
	  ];
	
	//在选择第一备份调制方式时更改第二备份调制方式 的可选项
	function changeThirdOption(){
		//先获取第一备份调制方式的值
		var firstPriority = $("#chnlSecondaryProf option:selected").attr("priority");
		//删除第二备份调制方式下所有的选项(除了无备份调制方式外)
		$("#chnlTertiaryProf option:gt(0)").remove();
		//给第二备份调制方式添加可选项
		$.each(optionObj,function(){
			if(this.priority > firstPriority){
				$(this.optionString).appendTo("#chnlTertiaryProf");
			}
		}); 
	}
	
	function cancelClick() {
		//window.parent.closeWindow("showNewGpTemplate");
		window.parent.closeFrame();
	}
	
	function initData(){
		if(groupModifyFlag==0){//add new Template 
			changeThirdOption();
		}
		if(groupModifyFlag==1){ // modify Template
			$('#templateName').val(groupTemplateJson.templateName);
			$("input[name='globalAdminStatus'][value="+groupTemplateJson.globalAdminStatus+"]").attr("checked",true);
			$('#snrQueryPeriod').val(groupTemplateJson.snrQueryPeriod);
			$('#hopHisMaxCount').val(groupTemplateJson.hopHisMaxCount);
			$('#chnlSecondaryProf').val(groupTemplateJson.chnlSecondaryProf);
			changeThirdOption();
			$('#chnlTertiaryProf').val(groupTemplateJson.chnlTertiaryProf);
			for(i=0;i<emsCcmtsSpectrumGpListJson.length;i++){
				groupData[i] = new Array();
				groupData[i][0] = emsCcmtsSpectrumGpListJson[i].emsGroupId;
				groupData[i][1] = emsCcmtsSpectrumGpListJson[i].deviceGroupId;
				groupData[i][2] = emsCcmtsSpectrumGpListJson[i].emsGroupName;
				groupData[i][3] = emsCcmtsSpectrumGpListJson[i].adminStatus;
				groupData[i][4] = emsCcmtsSpectrumGpListJson[i].hopPeriod;
				groupData[i][5] = emsCcmtsSpectrumGpListJson[i].maxHopLimit;
				groupData[i][6] = emsCcmtsSpectrumGpListJson[i].groupPolicy;
				groupIds.remove(emsCcmtsSpectrumGpListJson[i].deviceGroupId);
			}
			//加载所选的全局跳频组数据
			store.loadData(groupData);
			//加载可选的设备跳频组Id数据
			groupIdStore.loadData(groupIds);
		}
	}
	//当添加与删除全局跳频组时,改变可选的设备跳频组Id
	function changGroupIds(selectIds,flag){
		if(flag==1){ // add
			for(var i=0;i<groupIds.length;i++){
				if(groupIds[i][0]==selectIds){
					groupIds.remove(selectIds);
				}
			}
		}else if(flag==2){ //delete
			var id = [selectIds];
			groupIds.push(id);
		}
		Ext.getCmp("groupIdCombo").reset();
		groupIdStore.loadData(groupIds);
		Ext.getCmp("groupIdCombo").collapse();
	}
	
	//添加全局跳频组
	function addGroup(){
		var selectEmsGp = Ext.getCmp('emsGroupCombo').getValue();
		var selectGpId = Ext.getCmp('groupIdCombo').getValue();
		if(selectEmsGp == null || selectEmsGp == ''){
			alert(0)
			top.showMessageDlg("@COMMON.tip@","@CMC.GP.chooseGroup@");
			return;
		}
		if(selectGpId == null || selectGpId == ''){
			top.showMessageDlg("@COMMON.tip@","@CMC.GP.chooseGroupId@");
			return;
		}
		if(groupData.length >= 32){
			top.showMessageDlg("@COMMON.tip@","@CMC.GP.groupLimit@");
			return;
		}
		var selectArray = new Array();
		comBoStore.each(function(record){
			if(record.data.emsGroupId==selectEmsGp){
				selectArray.push(selectEmsGp);
				selectArray.push(selectGpId);
				selectArray.push(record.data.emsGroupName)
				selectArray.push(record.data.adminStatus)
				selectArray.push(record.data.hopPeriod)
				selectArray.push(record.data.maxHopLimit)
				selectArray.push(record.data.groupPolicy)
			}
		}); 
		groupData.push(selectArray);
		store.loadData(groupData);
		changGroupIds(selectGpId,1);
	}
	
	//删除全局跳频组
	function deleteGroup(groupId){
		window.top.showConfirmDlg("@COMMON.tip@", "@CMC.GP.deleteGpConfirm@", function(type) {
			if (type == 'no') {
				return;
			}
			var select = grid.getSelectionModel().getSelected();
			store.remove(select);
			changGroupIds(groupId,2);
			for(var i=0;i<groupData.length;i++){
				if(groupData[i][1]==groupId){
					groupData.remove(groupData[i]);
				}
			}
		});
	}
	
	function saveClick(){
		var templateName = $('#templateName').val();
		if(templateName==null || templateName==''){
			$('#templateName').focus();
			return;
		}
		var globalAdminStatus = $("input[name='globalAdminStatus']:checked").val();
		var snrQueryPeriod = $('#snrQueryPeriod').val();
		if(!checkSnrQueryPeriodValue(snrQueryPeriod)){
			$("#snrQueryPeriod").focus();
			return;
		}
		var hopHisMaxCount = $('#hopHisMaxCount').val();
		if(!checkHopHisMaxCountValue(hopHisMaxCount)){
			$("#hopHisMaxCount").focus();
			return;
		}
		var chnlTertiaryProf = $('#chnlTertiaryProf').val();
		var chnlSecondaryProf = $('#chnlSecondaryProf').val();
		if(chnlTertiaryProf == 65536 && chnlSecondaryProf != 65536 || chnlTertiaryProf != 65536 && chnlSecondaryProf == 65536){
			top.showMessageDlg("@COMMON.tip@","@CMC.CHANNEL.cancelAndApply@");
			return;
		}
		var selectGroups = new Array();
		store.each(function(record){
			selectGroups.push(record.data.emsGroupId+"_"+record.data.groupId);
		});
		var selectGpString = selectGroups.join(",");
		
		if(groupModifyFlag==0){  //新增
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
			$.ajax({
				url : '/ccmtsspectrumgp/addSpectrumGpTempLate.tv',
				type : 'POST',
				data : {
					templateName : templateName,
					globalAdminStatus : globalAdminStatus,
					snrQueryPeriod : snrQueryPeriod,
					hopHisMaxCount : hopHisMaxCount,
					chnlTertiaryProf : chnlTertiaryProf,
					chnlSecondaryProf : chnlSecondaryProf,
					selectGpString : selectGpString
				},
				success : function() {
					top.showMessageDlg("@COMMON.tip@","@CMC.GP.addTempSuccess@");
					top.getFrame("gpTemplateManage").store.reload();
					cancelClick();
				},
				error : function() {
					top.showMessageDlg("@COMMON.tip@","@CMC.GP.addTempFaild@");
				},
				cache : false
			});
		}else if(groupModifyFlag==1){ //更改
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@");
			$.ajax({
				url : '/ccmtsspectrumgp/modifySpectrumGpTempLate.tv',
				type : 'POST',
				data : {
					tempLateId : tempLateId,
					templateName : templateName,
					globalAdminStatus : globalAdminStatus,
					snrQueryPeriod : snrQueryPeriod,
					hopHisMaxCount : hopHisMaxCount,
					chnlTertiaryProf : chnlTertiaryProf,
					chnlSecondaryProf : chnlSecondaryProf,
					selectGpString : selectGpString
				},
				success : function() {
					top.showMessageDlg("@COMMON.tip@","@CMC.GP.updateTempSuccess@");
					top.getFrame("gpTemplateManage").store.reload();
					cancelClick();
				},
				error : function() {
					top.showMessageDlg("@COMMON.tip@","@CMC.GP.updateTempFaild@");
				},
				cache : false
			});
		}
	}
	//SnrQueryPeriod值校验
	function checkSnrQueryPeriodValue(v) {
		var reg1 = /^([0-9])+$/;
		if (!reg1.exec(v)|| (v > 360) || v < 5) {
			return false;
		}else{
			return true;
		}
	}
	//HopHisMaxCount值校验
	function checkHopHisMaxCountValue(v) {
		var reg1 = /^([0-9])+$/;
		if (!reg1.exec(v)|| (v > 32) || v < 1) {
			return false;
		}else{
			return true;
		}
	}
	
	function statusRender(value, cellmate, record){
		var adminStatus = record.data.adminStatus;
		var imgStr;
		if(adminStatus ==1){
			imgStr = "<img src = '/images/speOn.png'/>";
		}else{
			imgStr = "<img src = '/images/speOff.png'/>";
		}
		return imgStr;
	}
	
	function policyRender(value, cellmate, record){
		var groupPolicy = record.data.groupPolicy;
		var policyStr;
		if(groupPolicy == 1){
			policyStr = "@CMC.GP.freqOnly@";
		}else if(groupPolicy == 2){
			policyStr = "@CMC.GP.widthOnly@";
		}else if(groupPolicy == 3){
			policyStr = "@CMC.GP.modulationOnly@";
		}else if(groupPolicy == 4){
			policyStr = "@CMC.GP.freqWidthOnly@";
		}else if(groupPolicy == 5){
			policyStr = "@CMC.GP.priority@";
		}
		return policyStr;
	}
	
	function opeartionRender(value, cellmate, record){
		var groupId = record.data.groupId;
		return String.format(
				"<img src='/images/delete.gif' title='@COMMON.del@'" + 
				"onclick='deleteGroup({0})' style='cursor:pointer;'/>",groupId);
	}
	
	Ext.onReady(function() {
		//创建顶部toolbar
		new Ext.Toolbar({
			renderTo : "topToolbar",
			items : [ 
	            {text : '@CMC.GP.back@', iconCls : 'bmenu_back', handler : function(){ parent.closeFrame();} },'-',
	            {text : '@COMMON.save@', iconCls : 'bmenu_saveOk', handler : saveClick} 
			]
		});
		
		toolbar = [ {
			xtype : "label",
			cls:'blueTxt',
			text : "@CMC.GP.globalGroup@@COMMON.maohao@"
		},
		"<input type='text' style='height:20px;width:200px;' id='emsGroup'/>",
		"-",{
			xtype : "label",
			cls:'blueTxt',
			text : "@CMC.GP.groupId@@COMMON.maohao@"
		},
		"<input type='text' style='height:20px;width:120px;' id='deviceGpId'/>",
		"-", {
			text : "@CMC.GP.addGroup@",
			iconCls : 'bmenu_new',
			handler : addGroup
		} ]
		
		var cm = [
			{
				header : '@CMC.GP.groupId@',
				width : 60,
				sortable : true,
				dataIndex : 'groupId'
			},
			{
				header : '@CMC.GP.groupName@',
				width : 160,
				sortable : false,
				dataIndex : 'emsGroupName'
			},
			{
				header : '@CMC.GP.adminStatus@',
				width : 90,
				sortable : false,
				dataIndex : 'adminStatus',
				renderer : statusRender
			},
			{
				header : '@CMC.GP.hopPeriod@',
				width : 77,
				sortable : false,
				dataIndex : 'hopPeriod'
			},
			{
				header : '@CMC.GP.maxHopLimit@',
				width : 87,
				sortable : false,
				dataIndex : 'maxHopLimit'
			},
			{
				header : '@CMC.GP.hopPolicy@',
				width : 110,
				sortable : false,
				dataIndex : 'groupPolicy',
				renderer : policyRender
			},
			{
				header : '@COMMON.manu@',
				width : 70,
				sortable : false,
				dataIndex : 'op',
				renderer : opeartionRender
			} ];

		store =  new Ext.data.SimpleStore({
			data: groupData,
			fields :  ['emsGroupId','groupId','emsGroupName','adminStatus','hopPeriod','maxHopLimit','groupPolicy']	
		});

		grid = new Ext.grid.GridPanel(
			{
			title: "@CMC.GP.selectedGroup@",
			store : store,			
			border: true,
			margins: '0px 10px 10px 10px',
			region: 'center',
			columns : cm,
			bodyCssClass:'normalTable',
			tbar : new Ext.Toolbar({
				items : toolbar
			}),
			viewConfig:{
	      		forceFit: true
	      	}
			//renderTo : "groupGrid"
		});
		
		new Ext.Viewport({
			 layout: 'border',
			 items:[grid,{
		    	 region: 'north',
		         border: false,		       
		         contentEl: 'topPart',
		         height:150
		    }/* ,{
		    	 region: 'south',
		         border: false,		 
		         cls:'clear-x-panel-body',
		         contentEl: 'bottomPart',
		         height:70
		    } */]
		});
		
		//构建可选的全局跳频组下拉框
		window.comBoStore = new Ext.data.JsonStore({
	    	url : '/ccmtsspectrumgp/loadAllGlobalGroup.tv',
	    	//root : 'data',
	    	fields :  ['emsGroupId','emsGroupName','adminStatus','hopPeriod','maxHopLimit','groupPolicy']
	    });
		var GroupCombo = new Ext.form.ComboBox({
		    store: comBoStore,
		    valueField: 'emsGroupId',
		    id : 'emsGroupCombo',
		    displayField:'emsGroupName',
		    editable: false,
		    mode: 'local',
		    triggerAction: 'all',
		    selectOnFocus:true,
		    forceSelection:true,
		    emptyText:'@CMC.GP.chooseGroupTip@',
		    applyTo: 'emsGroup'
		});
		comBoStore.load();
		
		//构建可选的设备跳频组Id下拉框
		groupIdStore = new Ext.data.SimpleStore({
			data: groupIds,
			fields :  ['groupId'],
			idProperty:'groupId',
			sortInfo: {
			    field: 'groupId',
			    direction: 'ASC' 
			}
		});
		window.groupIdCombo = new Ext.form.ComboBox({
		    store: groupIdStore,
		    valueField: 'groupId',
		    displayField:'groupId',
		    id:'groupIdCombo',
		    editable: false,
		    mode: 'local',
		    triggerAction:"all",
		    emptyText:'@CMC.GP.chooseGpIdTip@',
		    selectOnFocus:true,
		    forceSelection:true,
		    applyTo: 'deviceGpId'
		});
		
		//初始化数据
		initData();
		
	});
	
</script>
</head>
<body class="whiteToBlack">
	<div id="topPart">
		<div id="topToolbar"></div>
		<div id="baseInfo" class="edge10">
			<table id="baseInfoTable" class="dataTableRows" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">
			     <thead>
			         <tr>
			             <th colspan="6" class="txtLeftTh">@text.baseInfo@</th>
			         </tr>
			     </thead>
				<tbody>
					<tr>
					<td class="rightBlueTxt" width="150">@CMC.GP.templateName@@COMMON.maohao@</td>
					<td>						
						<input  type="text" id="templateName" class="normalInput w160" toolTip="@CMC.GP.templateNameTip@" />
					</td>
					<td class="rightBlueTxt" width="150">@CMC.GP.globalAdminStatus@@COMMON.maohao@</td>
					<td>
						<input type="radio" name="globalAdminStatus" value="1"/>@CMC.GP.yes@ &nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="globalAdminStatus" value="2" checked="checked"/>@CMC.GP.no@
					</td>
					<td class="rightBlueTxt" width="150">@CMC.GP.snrQueryPeriod@@COMMON.maohao@</td>
					<td>
						<input type="text" id="snrQueryPeriod" class="normalInput w160" toolTip="@CMC.GP.queryPeriodTip@" value="5" maxlength="3"/>@CALENDAR.Sec@ 
					</td>
				</tr>
				<tr>
					<td class="rightBlueTxt" width="150">@CMC.GP.hopHisMaxCount@@COMMON.maohao@</td>
					<td>
						<input type="text" id="hopHisMaxCount" class="normalInput w160" toolTip="@CMC.GP.maxCountTip@" value="16" maxlength="2"/>
					</td>
					<td class="rightBlueTxt" width="150">@CMC.CHANNEL.secondProf@@COMMON.maohao@</td>
					<td>
						<select id="chnlSecondaryProf" name="chnlSecondaryProf" class="normalSel w160" onchange="changeThirdOption()">
								<option value="65536">@CMC.CHANNEL.delChnlProf@</option>
								<option value="1" priority="30">QPSK-Fair-Scdma</option>
								<option value="2" priority="25">QAM16-Fair-Scdma</option>
								<option value="3" priority="15">QAM64-Fair-Scdma</option>
								<option value="4" priority="10">QAM256-Fair-Scdma</option>
								<option value="5" priority="29">QPSK-Good-Scdma</option>
								<option value="6" priority="24">QAM16-Good-Scdma</option>
								<option value="7" priority="14">QAM64-Good-Scdma</option>
								<option value="8" priority="9">QAM256-Good-Scdma</option>
								<option value="9" priority="13">QAM64-Best-Scdma</option>
								<option value="10" priority="9">QAM256-Best-Scdma</option>
								<option value="11" priority="29">QPSK-Good-Atdma</option>
								<option value="12" priority="24">QAM16-Good-Atdma</option>
								<option value="13" priority="14">QAM64-Good-Atdma</option>
								<option value="14" priority="9">QAM256-Good-Atdma</option>
						</select>
					</td>
					<td class="rightBlueTxt" width="150">@CMC.CHANNEL.thirdProf@@COMMON.maohao@</td>
					<td>
						<select id="chnlTertiaryProf" name="chnlTertiaryProf" class="normalSel w160" >
								<option value="65536">@CMC.CHANNEL.delChnlProf@</option>
						</select>
					</td>
				</tr>
				</tbody>
			</table>
		</div>
		
		<!-- <div class="flagInfo mL10">@CMC.GP.selectedGroup@</div> -->
	</div>
	
	
	<%-- <div id="bottomPart">
		<div class="noWidthCenterOuter clearBoth"  id="buttonPanel">
		    <ol class="upChannelListOl pB0 pT10 noWidthCenter">
		        <li>
		        	<a href="javascript:;" class="normalBtnBig" onclick="saveClick()">
		                <span><i class="miniIcoSaveOK"></i>@COMMON.ok@</span>
		            </a>
		        </li>
		        <li>
		            <a href="javascript:;" class="normalBtnBig" onclick="cancelClick()">
		                <span>@COMMON.cancel@</span>
		            </a>
		        </li>
		    </ol>
		</div>
	</div> --%>
</body>
</Zeta:HTML>