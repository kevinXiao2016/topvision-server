<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<Zeta:HTML>
<head>
<%@include file="../include/ZetaUtil.inc"%>
<Zeta:Loader>
	library ext
	library jquery
	library zeta
    module epon
</Zeta:Loader>
<script type="text/javascript">
var entityId = '<s:property value="entityId"/>';
var modifyFlag = '<s:property value="modifyFlag"/>';
var mvlanId = '<s:property value="mvlanId"/>';
var mvlanName = '<s:property value="mvlanName"/>';
var mvlanChName = '<s:property value="mvlanChName"/>';
var mvlanProxyList = '<s:property value="mvlanProxyList"/>';
var maxProxyNum = '<s:property value="maxProxyNum"/>';
var authority = '<s:property value="authority"/>';
var singlePreviewTime = '<s:property value="singlePreviewTime"/>';
var previewTotalTime = '<s:property value="previewTotalTime"/>';
var resetPreviewTime = '<s:property value="resetPreviewTime"/>';
var previewCount = '<s:property value="previewCount"/>';
var previewInterval = '<s:property value="previewInterval"/>';
var igmpProxyParaTables = ${igmpProxyParaTables};
function doOnload(){
	if(modifyFlag == 1){
      $("#mvlanId").val(mvlanId);
      $("#mvlanName").val(mvlanName);
      $("#mvlanChName").val(mvlanChName);
      $("#maxProxyNum").val(maxProxyNum);
      $("#authority").val(authority);
      //$("#mvlanProxyList").val(mvlanProxyList);
      $("#singlePreviewTime").val(singlePreviewTime);
      $("#previewTotalTime").val(previewTotalTime);
      $("#resetPreviewTime").val(resetPreviewTime);
      $("#previewCount").val(previewCount);
      $("#previewInterval").val(previewInterval);
	}else if(modifyFlag == 0){
      $("#maxProxyNum").val(64);
      $("#authority").val(1);
      $("#singlePreviewTime").val(60);
      $("#previewTotalTime").val(120);
      $("#resetPreviewTime").val(24);
      $("#previewCount").val(2);
      $("#previewInterval").val(60);
	}
}
function showWaitingDlg(title, icon, text, duration) {
	window.top.showWaitingDlg(title, icon, text, duration);
}
function saveClick() {
	mvlanId = $("#mvlanId").val();
	mvlanName = $("#mvlanName").val();
	mvlanChName = $("#mvlanChName").val();
	//proxyList = $("#mvlanProxyList").val();
	proxyList = getChecked();
	maxProxyNum = $("#maxProxyNum").val();
	authority = $("#authority").val();
	singlePreviewTime = $("#singlePreviewTime").val();
	previewTotalTime = $("#previewTotalTime").val();
	resetPreviewTime = $("#resetPreviewTime").val();
	previewCount = $("#previewCount").val();
	previewInterval = $("#previewInterval").val();
	
	if(!checkMvlanId()){
		return ;
	}
	if(!checkMvlanName()){
		Zeta$("mvlanName").focus();
		return ;
	}
	if(mvlanChName.length>150){
		Zeta$("mvlanChName").focus();
		return ;
	}
	if(!checkMaxProxyNum()){
		Zeta$("maxProxyNum").focus();
		return ;
	}
	if(!checkSinglePreviewTime()){
		Zeta$("singlePreviewTime").focus();
		return ;
	}
	if(!checkPreviewCount()){
		Zeta$("previewCount").focus();
		return ;
	}
	if(!checkPreviewTotalTime()){
		Zeta$("previewTotalTime").focus();
		return ;
	}
	if(!checkResetPreviewTime()){
		Zeta$("resetPreviewTime").focus();
		return ;
	}
	if(!checkPreviewInterval()){
		Zeta$("previewInterval").focus();
		return ;
	}
	if(modifyFlag == 0){
		var a = window.parent.getWindow("showIgmpMvlan").body.dom.firstChild.contentWindow.mvlan;
		var flag = false;//标志cmIndex是否重名
		if(a!=null&&a!=""){
			for(i=0;i<a.length;i++){
				if(a[i].cmIndex == mvlanId){
					flag = true;
					}
			}
		}
		if(flag){
			window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.mvlanexist )
			return ;
		}
	    showWaitingDlg(I18N.COMMON.wait, I18N.IGMP.addingMvlanTempl , 'ext-mb-waiting')
		Ext.Ajax.request({
			url:"addMvlan.tv",
			method:"post",
			success:function(response){
				    if(response.responseText == "success"){
						window.parent.closeWaitingDlg();
						window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.addMvlanTemplOk )
						var a = window.parent.getWindow("showIgmpMvlan").body.dom.firstChild.contentWindow.mvlanData;
						a.unshift([mvlanId,mvlanName,mvlanChName,proxyList,maxProxyNum,authority,singlePreviewTime,previewCount,previewTotalTime,resetPreviewTime,previewInterval,""]);
						var b = window.parent.getWindow("showIgmpMvlan").body.dom.firstChild.contentWindow.mvlanStore;
						b.loadData(a);
						window.parent.closeWindow('addMvlan');
					 }else{
						window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.addMvlanTemplError)
					 }
			},failure:function (response) {
	            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.addMvlanTemplError)
	        },params : {
	  			entityId: entityId,
	  			mvlanId:mvlanId,
	  			mvlanName:mvlanName,
	  			mvlanChName:mvlanChName,
	  			mvlanProxyList:proxyList,
	  			maxProxyNum:maxProxyNum,
	  			authority:authority,
	  			singlePreviewTime:singlePreviewTime,
	  			previewTotalTime:previewTotalTime,
	  			resetPreviewTime:resetPreviewTime,
	  			previewCount:previewCount,
	  			previewInterval:previewInterval
	  		}})
		}else if(modifyFlag == 1){
		    showWaitingDlg(I18N.COMMON.wait, I18N.IGMP.mdfingMvlanTempl , 'ext-mb-waiting')
			Ext.Ajax.request({
				url:"modifyMvlan.tv",
				success:function(response){
					if(response.responseText == "success"){
						window.parent.closeWaitingDlg()
						window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.mdfMvlanTemplOk)
						var a = window.parent.getWindow("showIgmpMvlan").body.dom.firstChild.contentWindow.mvlanData
						var flagNum = -1
						for(var k=0; k<a.length; k++){
							if(mvlanId == a[k][0]){
								//a[k][0] = mvlanId;
								a[k][1] = mvlanName;
								a[k][2] = mvlanChName;
								a[k][3] = proxyList;
								a[k][4] = maxProxyNum;
								a[k][5] = authority;
								a[k][6] = singlePreviewTime;
								a[k][7] = previewCount;
								a[k][8] = previewTotalTime;
								a[k][9] = resetPreviewTime;
								a[k][10] = previewInterval;
								flagNum = k
							}
						}
						var b = window.parent.getWindow("showIgmpMvlan").body.dom.firstChild.contentWindow.mvlanStore
						b.loadData(a)
						window.parent.closeWindow('addMvlan')
					}else{
						window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.mdfMvlanTemplError )
					}
				},failure:function (response) {
		            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.IGMP.mdfMvlanTemplError)
		        },params : {
		  			entityId: entityId,
		  			mvlanId:mvlanId,
		  			mvlanName:mvlanName,
		  			mvlanChName:mvlanChName,
		  			mvlanProxyList:proxyList,
		  			maxProxyNum:maxProxyNum,
		  			authority:authority,
		  			singlePreviewTime:singlePreviewTime,
		  			previewTotalTime:previewTotalTime,
		  			resetPreviewTime:resetPreviewTime,
		  			previewCount:previewCount,
		  			previewInterval:previewInterval
		  		}})
			}
}
function cancelClick() {
    window.parent.closeWindow('addMvlan')
}
function checkMvlanId(){
	var reg0 = /^([0-9])+$/;
	var mvlanId = $("#mvlanId").val()
	if(mvlanId == "" || mvlanId == null||mvlanId>2000||mvlanId==0){
		return false
	}else{
		if(reg0.exec(mvlanId)){
			return true
		}else{
			return false
		}
	}
}
function checkMvlanName(){
	var reg1 = /^[a-zA-Z0-9/_]+$/;
	var mvlanName = $("#mvlanName").val();
	if(mvlanName == "" ||mvlanName == null||mvlanName.length>256){
		return false
	}else{
		if(reg1.exec(mvlanName)){
			return true
		}else{
			return false
		}
	}
}
function checkMaxProxyNum(){
	var reg0 = /^([0-9])+$/
	var maxProxyNum = $("#maxProxyNum").val()
	if(maxProxyNum == "" || maxProxyNum == null||maxProxyNum>64){
		return false
	}else{
		if(reg0.exec(maxProxyNum)){
			return true
		}else{
			return false
		}
	}
}
function checkSinglePreviewTime(){
	var reg0 = /^([0-9])+$/
	var singlePreviewTime = $("#singlePreviewTime").val()
	if(singlePreviewTime == "" || singlePreviewTime == null||singlePreviewTime<30||singlePreviewTime>60){
		return false
	}else{
		if(reg0.exec(singlePreviewTime)){
			return true
		}else{
			return false
		}
	}
}
function checkPreviewCount(){
	var reg0 = /^([0-9])+$/;
	var previewCount = $("#previewCount").val();
	if(previewCount == "" || previewCount == null||previewCount<1||previewCount>5){
		return false;
	}else{
		if(reg0.exec(previewCount)){
			return true;
		}else{
			return false;
		}
	}
}
function checkPreviewTotalTime(){
	var reg0 = /^([0-9])+$/;
	var previewTotalTime = $("#previewTotalTime").val();
	if(previewTotalTime == "" || previewTotalTime == null||previewTotalTime<30||previewTotalTime>300){
		return false;
	}else{
		if(reg0.exec(previewTotalTime)){
			return true;
		}else{
			return false;
		}
	}
}
function checkResetPreviewTime(){
	var reg0 = /^([0-9])+$/;
	var resetPreviewTime = $("#resetPreviewTime").val();
	if(resetPreviewTime == "" || resetPreviewTime == null||resetPreviewTime<1||resetPreviewTime>25){
		return false;
	}else{
		if(reg0.exec(resetPreviewTime)){
			return true;
		}else{
			return false;
		}
	}
}
function checkPreviewInterval(){
	var reg0 = /^([0-9])+$/;
	var previewInterval = $("#previewInterval").val();
	if(previewInterval == "" || previewInterval == null||previewInterval<30||previewInterval>60){
		return false;
	}else{
		if(reg0.exec(previewInterval)){
			return true;
		}else{
			return false;
		}
	}
}
Ext.onReady(function(){
	$("#mvlanId").focus()
})
</script>
<style type="text/css">
#proxysContainer{
		position: absolute;
		left:360px;
		top: 75px;
		width: 230px;
	}
</style>
</head>
<body  class=openWinBody onload="doOnload();">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@IGMP.createMvlanTempl@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	
	<div class="edge10">
		<table class="dataTableRows zebra">
			<tr>
				<td class="rightBlueTxt" style="width: 133px;"><label for="ip">@IGMP.controlMvlanTemplId@:</label></td>
				<td>
					<s:if test="modifyFlag == 1">
						<input type="text" id="mvlanId" disabled class="normalInput" />
					</s:if>
					<s:else>
						<input type="text" id="mvlanId" tooltip="@IGMP.mvlanTemplIdTip@" class="normalInput" />
					</s:else></td>
			</tr>
			<tr class="darkZebraTr">
				<td class="rightBlueTxt"><label for="ip">@IGMP.templName@:</label></td>
				<td><input type="text" id="mvlanName"
					tooltip="@IGMP.templNameTip@" class="normalInput" /></td>
			</tr>
			<tr height=20px style="display: none">
				<td class="rightBlueTxt"><label for="ip">@IGMP.templAlias@:</label></td>
				<td><input type="text" id="mvlanChName"
					tooltip="@IGMP.templChNameTip@" class="normalInput" /></td>
			</tr>
			<tr class="darkZebraTr">
				<td class="rightBlueTxt"><label for="ip">@IGMP.maxChn@:</label></td>
				<td><input type="text" id="maxProxyNum" maxlength=2
					tooltip="@IGMP.maxProxyTip@" class="normalInput" /></td>
			</tr>
			<tr>
				<td class="rightBlueTxt"><label for="ip">@COMMON.authority@:</label></td>
				<td><select id="authority" class="normalSel" style="width:129px;">
						<option value="1">@COMMON.allow@</option>
						<option value="2">@IGMP.preview@</option>
						<option value="3">@COMMON.forbid@</option>
				</select></td>
			</tr>
			<tr class="darkZebraTr">
				<td class="rightBlueTxt"><label for="ip">@IGMP.previewTime@:</label></td>
				<td><input type="text" class="normalInput w133"
					id="singlePreviewTime" tooltip="@IGMP.previewTimeTip@" />@COMMON.S@
				</td>
			</tr>
			<tr>
				<td class="rightBlueTxt"><label for="ip">@IGMP.previewCount@:</label></td>
				<td><input type="text" id="previewCount"
					tooltip='@IGMP.previewCountTip@' class="normalInput" /></td>
			</tr>
			<tr class="darkZebraTr">
				<td class="rightBlueTxt"><label for="ip">@IGMP.previewTotalTime@:</label></td>
				<td><input class="normalInput w133" type="text"
					id="previewTotalTime" tooltip="@IGMP.previewTotalTimeTip@" />@COMMON.S@
				</td>
			</tr>
			<tr>
				<td class="rightBlueTxt"><label for="ip">@IGMP.previewResetTick@:</label></td>
				<td><input type="text" class="normalInput w133"
					id="resetPreviewTime" tooltip="@IGMP.previewResetTickTip@" />@COMMON.H@
				</td>
			</tr>
			<tr class="darkZebraTr">
				<td class="rightBlueTxt"><label for="ip">@IGMP.previewInterval@:</label></td>
				<td><input class="normalInput" type="text"
					class="normalInput w133" id="previewInterval"
					tooltip="@IGMP.previewIntervalTip@" />@COMMON.S@</td>
			</tr>
		</table>
	</div>

	<div id="proxysSegment">
		<div id="proxysContainer"></div>
	</div>

	<Zeta:ButtonGroup>
		<Zeta:Button onClick="saveClick()" icon="miniIcoSaveOK">@COMMON.save@</Zeta:Button>
		<Zeta:Button onClick="cancelClick()">@COMMON.close@</Zeta:Button>
	</Zeta:ButtonGroup>

		<script>
		$(function(){
			window.proxyListTemp = igmpProxyParaTables.data;
			var packag = mvlanProxyList.split(",");
			var proxyListData = new Array();
			for(var i =0;i<proxyListTemp.length;i++){
				proxyListData[i] = new Array();
				var flag = false;
				for(var j=0;j<packag.length;j++){
					if(proxyListTemp[i].proxyIndex == packag[j]){
						flag = true;
						break;
					}
				}
				proxyListData[i][0] = flag;
				proxyListData[i][1] = proxyListTemp[i].proxyIndex;
				proxyListData[i][2] = proxyListTemp[i].proxyName;
				proxyListData[i][3] = proxyListTemp[i].proxyChName;
			}
			window.proxyStore = new Ext.data.SimpleStore({
				 //proxy: new Ext.data.MemoryProxy(igmpProxyParaTables.data),
				 data : proxyListData,
				 //root:'data',
       	 	 	 fields :[
                    {name:'checked',type:"boolean"},
                    'proxyIndex',
        	 		'proxyName',
        	 		'proxyChName'
      	 		 ]
			});
			window.proxysGrid = new Ext.grid.GridPanel({
			    stripeRows:true,
		   		region: "center",
		   		bodyCssClass: 'normalTable',
				id:'proxysGrid',
				renderTo:'proxysContainer',
				border:true,
				viewConfig:{forceFit: true},
				sm:new Ext.grid.RowSelectionModel({
					singleSelect:false,
					listeners:{
						'beforerowselect' : function(s,r,re){		
							return false;
						}
					}
				}),
				cm:new Ext.grid.ColumnModel([
  					{header: I18N.IGMP.joinTempl ,dataIndex:'checked',align: 'center',sortable: false,resizable: true,menuDisabled :true,
   						renderer : function(value, cellmeta, record,rowIndex) {
   							var proxyId = record.get("proxyIndex");
   							//-----checked--------//
   							if(!value){
   								return "<img src='/epon/image/unchecked.gif' proxyId="+proxyId+ " class=isCheckedClass   checked=false onclick='switchChecked(this);' />";
   							}else{
   								return "<img src='/epon/image/checked.gif' proxyId="+proxyId+ " class=isCheckedClass checked=true onclick='switchChecked(this);' />";
   							}
   						}
   					},
					{header: I18N.IGMP.mvlanId ,dataIndex:'proxyIndex',align: 'center',sortable: false,resizable: true,menuDisabled :true},
					{header: I18N.IGMP.mvlanName,dataIndex:'proxyName', align: 'center',sortable: false,resizable: true,menuDisabled :true},
					{header: I18N.IGMP.mvlanAlias ,dataIndex:'proxyChName', align: 'center',sortable: false,resizable: true,menuDisabled :true,hidden:true}
				]),
				store:proxyStore,
       			autoScroll: true,
       			height:275,
       			listeners:{
       				'viewready':function(){
       					$("#searchProxy").keyup(function(){
       						searchProxy();
       					});
       				}
       			},
				bbar: new Ext.Toolbar({   
		       		scope : this,   
		       		items :[ 
			            	{ 
		              			text : I18N.IGMP.queryChannel ,   
		              			width : 60,
		              			handler: searchProxy
			           		},   
	    	           '-',   
	    	           {  
	    	             xtype:"textfield", 
	    	             id:"searchProxy",
	    	             width:100
		    	       }  
		       		]   
 	    		})  				
			});
		//---END OF DOCUMENT READY---//  
		});
		
		
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
	/**
	 * 查询proxy
	 */
	function searchProxy(){
		var text = $("#searchProxy").val();
		if(!!!text){
			window.proxyStore.filterBy(function(){
				return true;
			});
			return;
		}
		window.proxyStore.filterBy(function(record){
			if(record.get("proxyName").toString().startWith(text)){
				return true;
			}else if(record.get("proxyChName").toString().startWith(text)){
				return true;
			}else if(record.get("proxyIndex").toString().startWith(text)){
				return true;
			}else{
				return false;
			}
		});
		
	}	
	/*
	 * 修改checked状态
	 */	
	function switchChecked(o){
		if(eval($(o).attr("checked"))){
			$(o).attr({
				checked:'false',
				src:'/epon/image/unchecked.gif'
			}) 
		}else{
			$(o).attr({
				checked:'true',
				src:'/epon/image/checked.gif'
			}) 
		}
	}
	function getChecked(){
		var result = new Array();
		$.each($(".isCheckedClass"),function(i,o){
			switch(eval($(o).attr("checked"))){
				case true:
					var proxyId = $(o).attr("proxyId");
					result.add(proxyId);
					break;
				case false:
					break;
			}
		})
		return result.toString();
	}
	</script>
</body>
</Zeta:HTML>