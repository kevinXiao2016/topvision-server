<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
		<title></title>
		<%@include file="/include/ZetaUtil.inc"%>
		<Zeta:Loader>
			library Jquery
		    library ext
		    library zeta
		    library FileUpload
		    module CMC
		    css css/white/disabledStyle
		</Zeta:Loader>
		<script type="text/javascript">
			var entityId = '${entityId}';
			var cm,columnModels,sm,store,grid,flash;
			var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
			var refreshDevicePower = <%=uc.hasPower("refreshDevice")%>;
			Ext.onReady(function() {
				columnModels = [
	                {header: "<div class=txtCenter>@CMC.title.filename@</div>", width:100, dataIndex: "topCcmtsSfFileName", align: "left", sortable: true},
	                {header: "@CM.fileSize@", width:50, dataIndex: "topCcmtsSfFileSize", align: "center", sortable: true, renderer: formatSize},
	                {header: "<div class=txtCenter>@resources/COMMON.manu@</div>", width:25, dataIndex: "cmcId", align: "center", sortable: false, renderer: manuRender}
	            ];
	            cm = new Ext.grid.ColumnModel({
	                defaults : {
	                    menuDisabled : false
	                },
	                columns: columnModels
	            });
	            store = new Ext.data.JsonStore({
	                url: String.format("/cmupgrade/loadCcmtsFile.tv?entityId={0}", window.entityId),
	                root: "data",
	                totalProperty: "rowCount",
	                remoteSort: true,
	                fields: ["topCcmtsSfFileName", "topCcmtsSfFileSize", "cmcId", "entityId"]
	            });
	            grid = new Ext.grid.GridPanel({
	                cm: cm,
	                cls:"normalTable edge10",
	                store: store,
	                height: 400, 
	                loadMask: true,
	            	renderTo: 'putGrid',
	                stripeRows:true,
	                bodyCssClass: "normalTable",
	                viewConfig: { forceFit: true }
	            });
	            store.load();
			});//document.ready;
			function manuRender(v, m, r){
				if(operationDevicePower){
				    return String.format('<a href="javascript:;" onclick="deleteClick(\'{0}\')">@COMMON.delete@</a>', r.get('topCcmtsSfFileName'));
				}
			}
			//删除;
			function deleteClick(fileName){
				window.top.showOkCancelConfirmDlg( '@COMMON.tip@', '@platform/ftp.confirmDelete@', function(type){
					if(type == "ok"){
						var url = String.format('/cmupgrade/deleteCcmtsFile.tv?entityId={0}&fileName={1}', window.entityId, fileName); 
						$.ajax({
							url: url,
							type: 'POST',
							async: false,
							cache: false,
							dataType: 'json', 
							success: function(json) {
								store.reload();
							},
							error: function() {
								window.top.showErrorDlg();
							}
						});
					}
				})
			}
			//取消;
			function cancelClick() {
			    window.top.closeWindow("modalDlg");
			}
			function refreshData(){
				store.reload();
			}
			//格式化文件大小
			function formatSize(value, p, record){
				var str = value;
				if(str>1024&&str<1024*1024){
					str = DecimalNum(str/1024) + 'KB'
				}else if(str>1024*1024&&str<1024*1024*1024){
					str = DecimalNum(str/(1024*1024)) + 'MB'
				}else if(str>1024*1024*1024){
					str = DecimalNum(str/(1024*1024*1024)) + 'GB'
				}else {
					str = str + 'B'
				}
				return str;
			}
			function DecimalNum(text){
				text = text+"";
				var result = null;
				if(text.indexOf("\.")!=-1){
			       var integer = text.split('\.')[0];
			       if(text.split('\.')[1].length >= 2){
			           var decimal = text.split('\.')[1].substring(0,2);
			           result = integer+"."+decimal;
			       }else{
			    	   result = integer+"."+text.split('\.')[1];
			           }
				}else{
			       result = text
				}
				return result
			} 
			//点击上传;
			function uploadClick(){
				top.createDialog("uploadfile", '@CMC.label.selectFile@', 600, 370, "cmupgrade/showCmUpgradeUpload.tv?entityId="+entityId, null, true, false, function(){
	    	   		//window.parent.stopProgress();
	    		});	
			}
			function authLoad(){
				if(!operationDevicePower){
					$("#chooseFile").attr("disabled", true)
				}
				if(!refreshDevicePower){
					$("#refresh").attr("disabled", true)
				}
			}
		</script>
	</head>
	<body class="openWinBody" onload="authLoad();">
		<div id="putGrid"></div>
		<ol class="upChannelListOl pB0 pT0 noWidthCenter">
		    <li><a href="javascript:;" class="normalBtnBig" id="refresh" onclick="refreshData()"><span><i class="miniIcoEquipment"></i>@resources/COMMON.fetch@</span></a></li>
		    <li><a href="javascript:;" class="normalBtnBig" id="chooseFile" onclick="uploadClick()"><span><i class="miniIcoArrUp"></i>@resources/COMMON.upload@</span></a></li>
		    <li><a href="javascript:;" class="normalBtnBig" onclick="cancelClick()"><span><i class="miniIcoForbid"></i>@resources/COMMON.cancel@</span></a></li>
		</ol>
		<input id="fileInput" type="hidden" />
	</body>
</Zeta:HTML>