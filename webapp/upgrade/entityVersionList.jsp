<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<Zeta:HTML>
	<head>
<%@include file="/include/ZetaUtil.inc"%>
<Zeta:Loader>
            library ext
            library jquery
            library zeta
            library FileUpload
            import js/raphael/raphael
            module network
            CSS js/webupload/webuploader
			IMPORT js/webupload/webuploader.min
        </Zeta:Loader>
<style type="text/css">
#versionSideArrow {
	width: 19px;
	height: 140px;
	overflow: hidden;
	position: absolute;
	right: 0px;
	top: 0px;
	z-index: 1000;
	cursor: pointer;
}

#arrow {
	position: absolute;
	top: 20px;
	left: 8px;
	width: 4px;
	height: 8px;
	overflow: hidden;
}

#versionSidePart {
	width: 380px;
	padding: 0px 0px;
	height: 100%;
	overflow: auto;
	background: #F4F4F4;
	position: absolute;
	top: 0;
	right: -400px;
	border-left: 1px solid #9a9a9a;
	z-index: 999;
	opacity: 0.9;
	filter: alpha(opacity = 90);
}

#showTxt {
	width: 350px;
	height: 160px;
	border: none;
}

.verticalTxt {
	width: 19px;
	height: 80px;
	overflow: hidden;
	top: 40px;
	position: absolute;
}
.webuploader-pick{ border:1px solid #ccc; border-radius: 3px;}
.btns .miniIcoArrUp{ width:16px; height: 16px; overflow:hidden; display:block; float:left; margin-right:2px;}
 
</style>
<script type="text/javascript">
	var uploader;
	var supportTypes = ${supportTypes};
	var pageSize = 10;
	var baseGrid;
	function query() {
		var versionName = $("#versionName").val();
		var deviceType = $("#deviceType").val();
		//加载store
		baseStore.reload({
			params : {
				versionName : versionName,
				typeId : deviceType
			}
		});
	}
	function manuRender(value, p, record) {
		return "<a class='yellowLink' href='javascript:;' onclick='viewEntityVersion(&quot;"
				+ value
				+ "&quot;)' >@resources/COMMON.view@</a>"
				+ " / "
				+ "<a class='yellowLink' href='javascript:;' onclick='deleteEntityVersion(&quot;"
				+ value + "&quot;)' >@resources/COMMON.deleteAction@</a>";
	}
	function nameRender(v, p, r) {
		return '<a href="javascript:;" style="padding-left:2px;" onclick="viewEntityVersion(\''
				+ v + '\')">' + v + '</a>';
	}
	function viewEntityVersion(value) {
		$("#showTxtTitle").text(value);
		$.ajax({
			url : '/upgrade/getEntityVersionProperty.tv',
			data : {
				versionName : value
			},
			type : "POST",
			dataType : 'text',
			success : function(text) {
				showHideSide("show");
				$("#showTxt").val(text);
			},
			error : function(response) {
				window.parent.showMessageDlg('@resources/COMMON.tip@', "@batchUpgrade.readVersionNoteFail@")
			},
			cache : false,
			complete : function(XHR, TS) {
				XHR = null
			}
		});
	}
	function deleteEntityVersion(value) {
		window.parent.showConfirmDlg('@resources/COMMON.tip@','@batchUpgrade.deleteVersion@', function(type) {
			if (type == 'no'){
				return;
				}
		$.ajax({
			url : '/upgrade/deleteEntityVersion.tv',
			data : {
				versionName : value
			},
			type : "POST",
			dataType : 'json',
			success : function(json) {
                $.each(json, function(i, jobId){
                    try {
                        top.removeTab("upgradeJob" + jobId)
                    } catch (e) {}
                });
                try {
                    top.frames['menuFrame'].refreshFn();
                } catch (e) {}
                query();
			},
			error : function(response) {
				window.parent.showMessageDlg('@resources/COMMON.tip@', "@batchUpgrade.deleteVersionFail@")
			},
			cache : false,
			complete : function(XHR, TS) {
				XHR = null
			}
		});
	});
	}

	Ext
			.onReady(function() {
				$.each(supportTypes,
						function(index, entityType) {
					if(entityType.typeId != 255){ //过滤unknown_onu
						$('#deviceType').append(
								String.format(
										'<option value="{0}">{1}</option>',
										entityType.typeId,
										entityType.displayName));
					}
						});
				var sm = new Ext.grid.CheckboxSelectionModel();
				var w = $(window).width() - 30;
				var baseColumns = [ {
					header : "<div class='txtCenter'>@batchUpgrade.versionNo@</div>",
					align : 'left',
					dataIndex : 'versionName',
					renderer : nameRender
				}, {
					header : "<div class='txtCenter'>@batchUpgrade.surportEntityType@</div>",
					align : 'left',
					dataIndex : 'typeDisplayNames'
				}, {
					header : "@batchtopo.createtime@",
					width : 145,
					align : 'center',
					fixed : true,
					dataIndex : 'createTimeStr'
				}, {
					header : "@resources/COMMON.opration@",
					width : 90,
					dataIndex : 'ip',
					fixed : true,
					dataIndex : 'versionName',
					renderer : manuRender
				} ];

				baseStore = new Ext.data.JsonStore({
					url : ('/upgrade/getEntityVersionList.tv'),
					root : 'data',
					totalProperty : 'rowCount',
					fields : [ 'versionName', 'typeDisplayNames',
							'propertyFileName', 'createTimeStr' ]
				});

				var baseCm = new Ext.grid.ColumnModel(baseColumns);
				var h = $(window).height() - 80;
				baseGrid = new Ext.grid.GridPanel({
					id : 'versionListGrid',
					title : "@batchUpgrade.surportVersion@",
					cls : 'normalTable',
					border : true,
					height : h,
					store : baseStore,
					margins : "0px 10px 10px 10px",
					cm : baseCm,
					region : 'center',
					viewConfig : {
						forceFit : true,
						hideGroupedColumn : true,
						enableNoGroups : true
					}
				});

				baseStore.load();

				new Ext.Viewport({
					layout : 'border',
					items : [ baseGrid, {
						region : 'north',
						contentEl : 'topPart',
						height : 50,
						border : false
					} ]
				});
				newCreateUpload();
				/* window.flash = new TopvisionUpload("chooseFile");

				flash.onSelect = function(obj) {
					fileSize = obj.size;
					chooseFileName = obj.name;
					flash.upload("/upgrade/upLoadFile.tv?chooseFileName="
							+ chooseFileName);
					window.top.showWaitingDlg("@batchUpgrade.uploadingVersionPacket@");
				};
				flash.onComplete = function(result) {
					result = Ext.util.JSON.decode(result);
					window.top.closeWaitingDlg("@batchUpgrade.uploadingVersionPacket@");
					if (result.state == "success") {
						window.parent.showMessageDlg('@resources/COMMON.tip@', "@batchUpgrade.addVersionPacketSuccess@")
					} else {
						window.parent.showMessageDlg('@resources/COMMON.tip@', "@batchUpgrade.addVersionPacketFail@")
					}
					query();
				}; */

				$("#versionSideArrow").click(function() { //展开，折叠;
					if ($("#arrow").hasClass("versionSideArrLeft")) { //展开
						showHideSide("show");
					} else { //折叠;
						showHideSide("hide");
					}
				});
				autoSetArrPosition();
				$(window).resize(function() {
					autoSetArrPosition();
				});

				var paper = Raphael(document.getElementById("verticalTxt"), 19,
						80), txt = paper.text(9, 30, '@batchUpgrade.versionNote@');
				txt.transform('0,0r90').attr({
					"fill" : "#0266B1",
					"font-weight" : "normal"
				});
			});

	function autoSetArrPosition() { //设置右侧箭头在屏幕中间;
		var h = $(window).height(), h2 = (h - 140) / 2;
		if (h2 < 0) {
			h2 = 0;
		}
		$("#versionSideArrow").css({
			top : h2
		});
	}

	function showHideSide(str) { //展开，折叠;
		switch (str) {
		case "show":
			$("#arrow").removeClass("versionSideArrLeft").addClass(
					"versionSideArrRight");
			$("#versionSideArrow").animate({
				right : 380
			});
			$("#versionSidePart").animate({
				right : 0
			});
			break;
		case "hide":
			$("#arrow").removeClass("versionSideArrRight").addClass(
					"versionSideArrLeft");
			$("#versionSideArrow").animate({
				right : 0
			});
			$("#versionSidePart").animate({
				right : -380
			});
			break;
		}
	}
	//创建上传控件;
	function newCreateUpload(){
		uploader = WebUploader.create({
		    swf: '/js/webupload/Uploader.swf',
		    server: '/upgrade/upLoadFile.tv',
		    pick: {
		    	id: '#picker',
		    	multiple : false
		    },
		    /* accept: {
		    	extensions: 'xls,xlsx'
		    }, */
		    resize: false,
		    duplicate: true,
		    fileNumLimit: 1,
		    fileSizeLimit: 300*1024*1024,
		    auto: true,
		    timeout: 0/* ,
		    runtimeOrder :'flash' */ 
		});
		uploader.on( 'beforeFileQueued', function( file ) {
			//必须先reset一下，否则上传文件个数大于限制，第二次点击浏览按钮，无法加入对列;
			uploader.reset();
			//console.log('beforeFileQueued')
		});
		//如果有的参数需要在这里上传(例如文件名)，可以将参数加入options.formData;
		uploader.on( 'fileQueued', function( file ) {
			//上传框中传入文件名+后缀;
			if(file && file.name){
				uploader.options.formData = {
			    	fileName: file.name
				}
			}
			//console.log('fileQueued');
		});
		//上传过程中触发，携带上传进度。
		uploader.on('uploadProgress', function( file, percentage ) {
			var progress = (percentage * 100).toFixed(0) + "%";
			showBodyMask("@batchUpgrade.uploadingVersionPacket@  " + progress);
		});
		//当文件上传成功时触发。
		uploader.on( 'uploadSuccess', function(file, result) {
			clearMask();
			if (result.state == "success") {
				window.parent.showMessageDlg('@resources/COMMON.tip@', "@batchUpgrade.addVersionPacketSuccess@")
			} else {
				window.parent.showMessageDlg('@resources/COMMON.tip@', "@batchUpgrade.addVersionPacketFail@")
			}
			query();
		});
		//当文件上传出错时触发
		uploader.on( 'uploadError', function( file ) {
			//console.log('uploadError上传出错');
			clearMask();
			top.showMessageDlg('@COMMON.tip@', '@UPLOAD.fail@');
		});
		uploader.on( 'error', function(err, file) {
			switch(err){
	    		case 'Q_TYPE_DENIED':
	    			top.showMessageDlg('@COMMON.tip@', '@UPLOAD.fileErrType@' ,null)
					//console.log('您选择的文件类型不符合规范');
					break;
	    		case 'Q_EXCEED_SIZE_LIMIT':
	    			top.showMessageDlg('@COMMON.tip@', '@UPLOAD.sizeLimit@' ,null)
	    			//console.log('您选择的文件大小超出了范围');	
	    			break;
			}
		});
	}
	function showBodyMask(str){
		Ext.getBody().mask(String.format('<label class="loadingMaskLabel">{0}</label>', str));
	}
	function clearMask(){
		Ext.getBody().unmask();
	}
</script>
	</head>
	<body class="whiteToBlack">
		<div id="topPart" class="edge10">
			<div class="formtip" id="tips" style="display: none"></div>
			<table class="queryTable">
				<tr>
					<td><loabel>@batchUpgrade.versionNo@:</loabel></td>
					<td><input type="text" class="normalInput" id="versionName" /></td>
					<td><loabel>@batchUpgrade.surportEntityType@:</loabel></td>
					<td><select class="normalSel w180" id="deviceType">
							<option value="-1">@sendConfig.pleaseChoose@</option>
					</select></td>
					<td><a id="query" href="javascript:query();" class="normalBtn"
						onclick=""><span><i class="miniIcoSearch"></i>@resources/COMMON.query@</span></a></td>

					<!-- <td class="rightBlueTxt">
						<a id="chooseFile" href="javascript:;" class="normalBtn"><span><i class="miniIcoArrUp"></i>@batchUpgrade.uploadVersion@</span></a>
					</td> -->
					<td>
						<div class="btns">
	              			<div id="picker"><i class="miniIcoArrUp"></i>@batchUpgrade.uploadVersion@</div>
	              		</div>
					</td>
				</tr>
			</table>
		</div>
		<div id="versionSidePart" style="padding: 0px;">
			<p id="showTxtTitle" class="pannelTit"
				style="padding: 13px 10px 0px 10px;">@sendConfig.tip@</p>
			<div class="edge10">
				<textarea id="showTxt" style="background: rgba(255, 255, 255, 0)" disabled></textarea>
			</div>
		</div>
		<div class="versionSideEnglish" id="versionSideArrow">
			<div class="versionSideArrLeft" id="arrow"></div>
			<div class="verticalTxt" id="verticalTxt"></div>
		</div>
	</body>
</Zeta:HTML>