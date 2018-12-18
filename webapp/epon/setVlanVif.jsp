<%@ page language="java" contentType="text/html;charset=utf-8"%>
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
    import js.tools.ipText static
</Zeta:Loader>
<script type="text/javascript">
var operationDevicePower= <%=uc.hasPower("operationDevice")%>;
//数据初始化
var entityId = '${entityId}';
var vlanIndex = '${vlanIndex}';
var vlanVifPriIp = ${vlanVifPriIpJson} ? ${vlanVifPriIpJson} : new Array();
var vlanVifSubIp = ${vlanVifSubIpJson} ? ${vlanVifSubIpJson} : new Array();
var vlanVifSubIpGridData = new Array();
var vlanVifSubIpGridStore = null;
var vlanVifSubIpGrid = null;

Ext.onReady(function(){
	loadVlanVifPriInput();
    loadVlanVifSubGrid();
});

function loadVlanVifPriInput(){
	var priIpInput = new ipV4Input("priIp", "span1");
	if(vlanVifPriIp[0] != null ){
		priIpInput.setValue(vlanVifPriIp[0].topOltVifPriIpAddr);
	}else{
		priIpInput.setValue("");
	}
	var priMaskInput = new ipV4Input("priMask", "span2");
	if(vlanVifPriIp[0] != null){
		priMaskInput.setValue(vlanVifPriIp[0].topOltVifPriIpMask);
	}else{
		priMaskInput.setValue("");
	}
    setIpWidth("all", 180);
    setIpBgColor("all", "white");
}

function loadVlanVifSubGrid(){
	initVlanVifSubIpData();
	vlanVifSubIpGridStore = new Ext.data.SimpleStore({
		data : vlanVifSubIpGridData,
		fields : ['topOltVifSubIpSeqIdx','topOltVifSubIpAddr','topOltVifSubIpMask']
	});
	vlanVifSubIpGrid = new Ext.grid.GridPanel({
	    stripeRows:true,region: "center",bodyCssClass: 'normalTable',
		id : 'vlanVifSubIpGrid',
		renderTo : 'subIpGrid',
		store : vlanVifSubIpGridStore,
		viewConfig:{ forceFit:true },
		height : 150,
		selModel : new Ext.grid.RowSelectionModel({singleSelect : true}),
		columns: [{
					header: I18N.VLAN.subIpSeqIdx ,
					dataIndex: 'topOltVifSubIpSeqIdx',
					sortable:true
			     },{
					header: I18N.VLAN.subIpAddr ,
					dataIndex: 'topOltVifSubIpAddr',
					sortable:true
		        },{
					header: I18N.VLAN.subIpMask ,
					dataIndex: 'topOltVifSubIpMask',
					sortable:true
			    },{
					header: I18N.COMMON.manu ,
					dataIndex: 'id',
					renderer : function(value, cellmeta, record) {
						if(operationDevicePower){
							return "<input type='image' src='/images/edit.gif'  onclick='modifyVlanVifSubIp(\""+record.data.topOltVifSubIpSeqIdx+"\",\""+record.data.topOltVifSubIpAddr+"\",\""+record.data.topOltVifSubIpMask+"\")'>&nbsp;&nbsp;&nbsp;&nbsp;<input type='image' src='/images/delete.gif'  onclick='deleteVlanVifSubIp(\""+record.data.topOltVifSubIpSeqIdx+"\")'>";
						}else{
							return "<input type='image' src='/images/editDisable.gif'>&nbsp;&nbsp;&nbsp;&nbsp;<input type='image' src='/images/deleteDisable.gif'>";
						}
					}
				}]
	});
}

function initVlanVifSubIpData(){
	if(vlanVifSubIp!=null&&vlanVifSubIp!=""){
		for(i=0;i < vlanVifSubIp.length ; i++){
			vlanVifSubIpGridData[i]= new Array();
			vlanVifSubIpGridData[i][0] = vlanVifSubIp[i].topOltVifSubIpSeqIdx;
			vlanVifSubIpGridData[i][1] = vlanVifSubIp[i].topOltVifSubIpAddr;
			vlanVifSubIpGridData[i][2] = vlanVifSubIp[i].topOltVifSubIpMask;
		}
	}
}

//修改主ip
function modifyVlanVifPriIp(){
	var priIp = getIpValue("priIp");
	var priMask = getIpValue("priMask");
    showWaitingDlg(I18N.COMMON.wait, I18N.VLAN.mdfingMasterIP , 'ext-mb-waiting');
	Ext.Ajax.request({
		url:"/epon/vlan/modifyVlanVifPriIp.tv?entityId="+entityId+"&vlanIndex="+vlanIndex+"&topOltVifPriIpAddr="+priIp+"&topOltVifPriIpMask="+priMask,
		method:"post",
		//async: false,
		success:function(response){
			if(response.responseText == "success"){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.mdfMasterIPOk );
			}else{
				window.parent.showMessageDlg(I18N.COMMON.tip,I18N.VLAN.mdfMasterIPEr);
			}
		},failure:function (response) {
            window.parent.showMessageDlg(I18N.COMMON.tip,  I18N.VLAN.mdfMasterIPEr );
        }})
}

//删除vlan虚接口
function deleteVlanVif(){
	window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.VLAN.confirmDelVI, function(type) {
        if (type == 'no') {
            return;
        }
        showWaitingDlg(I18N.COMMON.wait, I18N.VLAN.delingVI , 'ext-mb-waiting');
    	Ext.Ajax.request({
    		url:"/epon/vlan/deleteVlanVif.tv?entityId="+entityId+"&vlanIndex="+vlanIndex,
    		method:"post",
    		//async: false,
    		success:function(response){
    			if(response.responseText == "success"){
    				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.delVIOk )
    				window.parent.getFrame("entity-" + entityId).updataVlanVifFlag(vlanIndex);
    				cancelClick();
    			}else{
    				window.parent.showMessageDlg(I18N.COMMON.tip,I18N.VLAN.delVIEr);
    			}
    		},failure:function (response) {
                window.parent.showMessageDlg(I18N.COMMON.tip,  I18N.VLAN.delVIEr )
           }})
	})
}

//添加子Ip
function addVlanVifSubIp(){
	//最多只能添加20个子Ip
	if(vlanVifSubIpGridStore.getCount() < 20){
   		window.parent.createDialog("addVlanVifSubIp", I18N.VLAN.addSubIP , 600, 235,
   			"/epon/vlan/addVlanVifSubIpJsp.tv?entityId="+entityId+"&vlanIndex="+vlanIndex+"&vlanVifFlag=2", null, true, true);	
	}else{
		window.parent.showMessageDlg("@COMMON.tip@", "@VLAN.subIpLimit@");
		return;
	}
}

//删除子ip
function deleteVlanVifSubIp(index){
	window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.VLAN.confirmDelSubVI , function(type) {
        if (type == 'no') {
            return;
    }
    showWaitingDlg(I18N.COMMON.wait, I18N.VLAN.delingSubIP , 'ext-mb-waiting');
	Ext.Ajax.request({
		url:"/epon/vlan/deleteVlanVifSubIp.tv?entityId="+entityId+"&vlanIndex="+vlanIndex+"&topOltVifSubIpSeqIdx="+index,
		method:"post",
		//async: false,
		success:function(response){
			if(response.responseText == "success"){
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.delSubIPOk );
				/* for( i = 0 ; i< vlanVifSubIpGridData.length ; i++ ){
					if(index == vlanVifSubIpGridData[i][0]){
						vlanVifSubIpGridData.remove(vlanVifSubIpGridData[i]);
						break;
				    }
				}
				vlanVifSubIpGridStore.loadData(vlanVifSubIpGridData); */
				loadVlanVifSubIp();
			}else{
				window.parent.showMessageDlg(I18N.COMMON.tip,I18N.VLAN.delSubIPEr);
			}
		},failure:function (response) {
            window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.delSubIPEr );
        }})
	})
}

function loadVlanVifSubIp(){
    $.ajax({
        url: '/epon/vlan/loadVlanVifSubIp.tv',
        type: 'POST',
        async: false,
        data: "entityId=" + entityId +"&vlanIndex="+vlanIndex+"&num=" + Math.random(),
        dataType:"json",
        success: function(json) {
			var a = vlanVifSubIpGridData;
			var b = vlanVifSubIpGridStore;
			a = new Array();
			for(i=0;i < json.length ; i++){
				a[i]= new Array();
				a[i][0] = json[i].topOltVifSubIpSeqIdx;
				a[i][1] = json[i].topOltVifSubIpAddr;
				a[i][2] = json[i].topOltVifSubIpMask;
			}
			b.loadData(a);
        }, error: function(json) {
    }, cache: false
    });
}

//修改子ip
function modifyVlanVifSubIp(index,ip,mask){
   	window.parent.createDialog("modifyVlanVifSubIp", I18N.VLAN.mdfSubIP , 600, 230,
   			"/epon/vlan/modifyVlanVifSubIpJsp.tv?entityId="+entityId+"&vlanIndex="+vlanIndex+"&topOltVifSubIpSeqIdx="+index+"&topOltVifSubIpAddr="+ip+"&topOltVifSubIpMask="+mask+"&vlanVifFlag=3", null, true, true);	
}

function refreshClick(){
	window.parent.showConfirmDlg(I18N.COMMON.tip, I18N.VLAN.willClearUnSavedInfo , function(type) {
		if (type == 'no'){return;}
		var params = {
			entityId : entityId
		};
		var url = '/epon/vlan/refreshVlanVif.tv?&r=' + Math.random();
		window.top.showWaitingDlg(I18N.COMMON.wait, I18N.VLAN.fetchingUI , 'ext-mb-waiting');
		Ext.Ajax.request({
			url : url,
			timeout : 600000, 
			success : function(response) {
				if(response.responseText != 'success'){
					window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.fetchAgainError);
					return;
				}
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.fetchingUIOk );
				window.location.reload();
			},
			failure : function() {
				window.parent.showMessageDlg(I18N.COMMON.tip, I18N.VLAN.fetchAgainError);
			},
			params : params
		});
	});
}

//公用方法
function showWaitingDlg(title, icon, text, duration) {
	window.top.showWaitingDlg(title, icon, text, duration);
}

function cancelClick() {
    window.parent.closeWindow('setVlanVif');
}

function authLoad(){
	var ids = new Array();
	//ids.add("fetch");
	ids.add("mdfMasterIp");
	ids.add("addSubIp");
	ids.add("delVi");
	operationAuthInit(operationDevicePower,ids);
	//控制ip框
	if(!operationDevicePower){
		setIpEnable("span1",false);
		setIpEnable("span2",false);
	}
}
</script>
</head>
	<body class=openWinBody onload="authLoad()">
		<div class="edge10">
			<div class="zebraTableCaption">
				<div class="zebraTableCaptionTitle"><span>@VLAN.viMasterIP@</span></div>
				<table class="zebraTableRows" >
					<tr>
						<td class="withoutBorderBottom rightBlueTxt">@VLAN.masterIP@:<font color=red>*</font></td>
						<td class="withoutBorderBottom"><span id="span1" class="modifiedFlag"></span></td>
						<td class="withoutBorderBottom rightBlueTxt">@VLAN.masterIPMask@:<font color=red>*</font></td>
						<td class="withoutBorderBottom"><span id="span2" class="modifiedFlag"></span></td>
					</tr>
				</table>
			</div>
		</div>

		<div class="edge10">
			<div class="zebraTableCaption">
				<div class="zebraTableCaptionTitle">
					<span>@VLAN.viSubIP@</span>
				</div>
				<div id=subIpGrid></div>
			</div>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button onClick="refreshClick()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
			<Zeta:Button onClick="modifyVlanVifPriIp()">@VLAN.mdfMasterIP@</Zeta:Button>
			<Zeta:Button onClick="addVlanVifSubIp()">@VLAN.addSubIP@</Zeta:Button>
			<Zeta:Button onClick="deleteVlanVif()">@VLAN.delVI@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoCancel">@COMMON.close@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>
