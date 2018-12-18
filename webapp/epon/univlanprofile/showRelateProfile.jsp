<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module univlanprofile
    import js.tools.ipText static
    css css/white/disabledStyle
</Zeta:Loader>
<style type="text/css">
body,html{height:100%; overflow:hidden;}
.openLayerProfile{ width:100%; height:100%; overflow:hidden; position:absolute; top:0;left:0;  display:none;}
.openLayerProfileMain{ width:560px; height:280px; overflow:hidden; position:absolute; top:100px; left:120px; z-index:2;  background:#F7F7F7;}
.openLayerProfileBg{ width:100%; height:100%; overflow:hidden; background:#F7F7F7; position:absolute; top:0;left:0; opacity:0.8; filter:alpha(opacity=85);}
</style>
<script type="text/javascript">
	var entityId = '${uniBindInfo.entityId}';
	var currentProfile = '${uniBindInfo.bindProfileId}';
	var uniIndex = '${uniBindInfo.uniIndex}';
	var bindProfAttr = '${uniBindInfo.bindProfAttr}';
	var uniId = '${uniId}';
	
	//关闭当前弹出框
 	function closeBtClick(){
		//两个版本的UNI口VLAN信息使用相同的页面,在此进行判断以决定是从哪个页面过来的
		if(window.top.frames["frameuniVlanBindInfo"] != null){
			window.top.frames["frameuniVlanBindInfo"].doRefresh();
		}else if(window.top.frames["frameuniPortVlan"] != null){
			window.top.frames["frameuniPortVlan"].doRefresh();
		}
 		window.parent.closeWindow('relateprofile');
 	}
	//从设备刷新数据
	function refreshData(){
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.fetching@");
		$.ajax({
			url : '/epon/univlanprofile/refreshProfileData.tv',
			type : 'POST',
			data : {
				entityId : entityId
			},
			success : function() {
				//window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchOk@");
				top.afterSaveOrDelete({
	       	      	title: "@COMMON.tip@",
	       	      	html: '<b class="orangeTxt">@COMMON.fetchOk@</b>'
	       	    });
				window.location.reload();
			},
			error : function(json) {
				window.parent.showMessageDlg("@COMMON.tip@", "@COMMON.fetchBad@");
			},
			cache : false
		});
	}
	//模板Vlan模式转换
	function modeRenderer(value, cellmate, record){
		switch(value){
			case 0 : return "@PROFILE.modeNone@"; break;
			case 1 : return "@PROFILE.modeTransparent@"; break;
			case 2 : return "@PROFILE.modeTag@"; break;
			case 3 : return "@PROFILE.modeTranslate@"; break;
			case 4 : return "@PROFILE.modeAgg@"; break;
			case 5 : return "@PROFILE.modeTrunk@"; break;
		}
	}
	//grid中操作栏处理
	function opeartionRender(value, cellmate, record){
		var index = record.data.profileId;
		var mode = record.data.profileMode;
		if(operationDevicePower){
			if(mode > 0){
				if(currentProfile == index){
					return String.format(" <a href='javascript:;' onClick='unBindProfile()'>@PROFLIE.unBinding@</a>");
				}else{
					return String.format(" <a href='javascript:;' onClick='replaceBindProfile({0})'>@PROFILE.binding@</a>",index);
				}
			}else{
				return "--";
			}
		}else{
			return '--';
		}
	}
	
	//绑定模板页面
	function replaceBindProfile(index){
		//如果已经绑定了模板,修改绑定时提示用户
		if(currentProfile > 0){
			window.top.showConfirmDlg("@COMMON.tip@", "@PROFLIE.uniProfileTip@", function(type) {
				if (type == 'no') {
					return;
				}
				changeBindProfile(index);
			});
		}else{
			changeBindProfile(index);
		}
	}
	
	function changeBindProfile(profileIndex){
		window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
		$.ajax({
			url:"/epon/univlanprofile/replaceBindProfile.tv",type:'post',cache:false,
			data:{
				entityId : entityId,
				uniIndex: uniIndex,
				uniId : uniId,
				profileIndex: profileIndex,
				bindProfAttr: bindProfAttr
			},success:function(){
				top.afterSaveOrDelete({
	       	      	title: "@COMMON.tip@",
	       	      	html: '<b class="orangeTxt">@PROFLIE.bindSuccess@</b>'
	       	    });
				currentProfile = profileIndex;
				/* //由于“关联模板”入口有2个，如果底部tabpanel页面是UNI业务配置，关联后则需要刷新底部页面;
				var subPageId = top.getActiveFrameId(),
				    subPageArr = subPageId.split("-");
				if(subPageArr.length === 2){
					if(subPageArr[0] == "uniserviceconfig" && typeof(top.getActiveFrame().reload) == 'function'){
						top.getActiveFrame().reload();
					}
				} */
				refreshSubPage();
				window.location.reload();
			},error:function(){
				window.top.showMessageDlg("@COMMON.tip@", "@PROFLIE.bindFailed@");
			}
		})
	}
	
	//解除该模板的绑定
	function unBindProfile(){
		//只有离散配置才能解绑定
		if(bindProfAttr == 2){
			window.top.showWaitingDlg("@COMMON.wait@", "@COMMON.saving@", 'ext-mb-waiting');
			$.ajax({
				url:"/epon/univlanprofile/unBindUniProfile.tv",
				type:'post',
				cache:false,
				data:{
					entityId : entityId,
					uniId : uniId,
					uniIndex: uniIndex
				},success:function(){
					top.afterSaveOrDelete({
		       	      	title: "@COMMON.tip@",
		       	      	html: '<b class="orangeTxt">@PROFILE.unBindSuccess@</b>'
		       	    });
					currentProfile = 0;
					refreshSubPage();
					window.location.reload();
				},error:function(){
					window.parent.showMessageDlg("@COMMON.tip@", "@PROFILE.unBindFailed@");
				}
			})
		}else{
			window.top.showMessageDlg("@COMMON.tip@", "@UNIVLAN.defaultConfigTip@");
			return;
		}
	}
	function refreshSubPage(){
		//由于“关联模板”入口有2个，如果底部tabpanel页面是UNI业务配置，关联后则需要刷新底部页面;
		var subPageId = top.getActiveFrameId(),
		    subPageArr = subPageId.split("-");
		if(subPageArr.length === 2){
			if(subPageArr[0] == "uniserviceconfig" && typeof(top.getActiveFrame().reload) == 'function'){
				top.getActiveFrame().reload();
			}
		}
	}
	
	$(document).ready(function(){
		window.dataStore = new Ext.data.JsonStore({
			url : '/epon/univlanprofile/loadUniVlanProfileList.tv',
			fields : ["profileId", "profileName", "profileRefCnt","profileMode"],
			baseParams : {
				entityId : entityId
			},
			listeners : {
				load : function(s,records, options) {
					$.each(records,function(i,record){
						var $profileId = record.data.profileId;
						if($profileId == currentProfile){
							uniVlanProfileGrid.getSelectionModel().selectRow(i);
						}
					}); 
		        } 
			}
		});
		//WIN.sm = new Ext.grid.CheckboxSelectionModel(); 
		window.colModel = new Ext.grid.ColumnModel([ /* sm, */
		 	{header : "@PROFILE.id@",width : 50,align : 'center',dataIndex : 'profileId'}, 
		 	{header : "@PROFILE.name@",width : 160,align : 'center',dataIndex : 'profileName'}, 
		 	{header : "@PROFILE.bindCount@",width : 100,align : 'center',dataIndex : 'profileRefCnt'},
		 	{header : "@PROFILE.vlanMode@",width : 100,align : 'center',dataIndex : 'profileMode', renderer : modeRenderer}, 
		 	{header : "@COMMON.manu@",width : 100, align : 'center',dataIndex :'op', renderer : opeartionRender}
		]);
		
		window.uniVlanProfileGrid =  new Ext.grid.GridPanel({
			id : 'uniVlanProfileGrid',
			title : "@PROFILE.profileList@",
			height : 390,
			border : true,
			cls : 'normalTable',
			store : dataStore,
			colModel : colModel,
			viewConfig : {
				forceFit : true
			},
			renderTo : 'contentGrid',
			sm : new Ext.grid.RowSelectionModel({
				singleSelect : true
			})
		});		
		dataStore.load();
		
	});
	
	//刷新Grid中的数据
	function refreshGridData(){
		dataStore.reload();
	};
	
</script>
</head>
<body class="openWinBody">
	<div class="edge10">
		<div id="contentGrid"></div>
	</div>
	<div class="noWidthCenterOuter clearBoth">
	     <ol class="upChannelListOl pB0 pT5 noWidthCenter">
	         <li><a  onclick='refreshData()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoEquipment"></i>@COMMON.fetch@</span></a></li>
	         <li><a  onclick='closeBtClick()' href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@COMMON.cancel@</span></a></li>
	     </ol>
	</div>
	</div>
	
	</div>
</body>
</Zeta:HTML>