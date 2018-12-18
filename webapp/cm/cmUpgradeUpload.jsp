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
		    CSS css/white/disabledStyle
		    IMPORT cm/javascript/progressBarScript
		    
		    CSS js/webupload/webuploader
			IMPORT js/webupload/webuploader.min
		</Zeta:Loader>
		<style type="text/css">
		.uploadBar{z-index:101;}
		</style>
		<script type="text/javascript">
			var entityId = '${entityId}';
			var flash;
			var fileSize = 0;
			Ext.onReady(function() {
				newCreateUpload();
				
				/* flash = new TopvisionUpload("chooseFile");
	            flash.onSelect = function(obj){
	            	var fileSize = obj.size;
	                var chooseFileName = obj.name;
	                var length = chooseFileName.length;
	                if($("#savingName").val() == ""){
	                	$("#savingName").val(chooseFileName);
	                }
	                //单个文件不能大于20M
	                var maxSize = 20*1024*1024;
	                if(fileSize > maxSize){
	                	top.showMessageDlg('@COMMON.tip@', '@SERVICE.fileSize@');
	                	return;
	                }
	                $("#fileInput").val(chooseFileName);
	            }
	            flash.onComplete =function(result){
	                if(result == "success"){
	                	show(window.entityId, {
	                		callback : function(){
	                			Ext.getBody().unmask();
	                			top.frames['modalFrame'].refreshData();
	    	                	cancelclick();		
	                		}
	                	});
	                    return;
	                }else if(result == "fileSizeExceed"){
	                	Ext.getBody().unmask();
	                	top.showMessageDlg('@COMMON.tip@', '@SERVICE.allfileSize@');
	                	return;
	                }else{
	                	Ext.getBody().unmask();
	                	top.showMessageDlg('@COMMON.tip@', '@SERVICE.uploadFail@');
	                	return;
	                }
	            } */
			});//document.ready;
			function cancelclick(){
				window.parent.closeWindow('uploadfile');
			}
			/* function uploadClick(){
				var reg = /^[a-zA-Z0-9.()_-]{1,31}$/,
				    $savingName = $("#savingName"),
				    $fileInput = $("#fileInput");
				
				//验证保存名;
				if(!reg.test($savingName.val())){
					$savingName.focus();
					return;
				}
				//验证上传控件输入框;
				if($fileInput.val() == ""){
					$("#chooseFile").fadeOut(function(){
						$(this).fadeIn();
					});
					return;
				}
				Ext.getBody().mask('<label class="loadingMaskLabel">@COMMON.uploading@</label>');
                var url = String.format('/cmupgrade/uploadCcmtsFile.tv?entityId={0}&versionFileName={1}', 
                		window.entityId, $savingName.val());
                flash.upload(url);
			} */
			
			//创建上传控件;
			function newCreateUpload(){
				uploader = WebUploader.create({
				    swf: '/js/webupload/Uploader.swf',
				    server: '/cmupgrade/uploadCcmtsFile.tv',
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
				    fileSizeLimit: 20*1024*1024,
				    auto: false,
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
						$('#fileInput').val(file.name);
						var $saveName = $("#savingName");
						if($saveName.val() == ""){
							$saveName.val(file.name);
						}
					}
					//console.log('fileQueued');
				});
				//上传过程中触发，携带上传进度。
				uploader.on('uploadProgress', function( file, percentage ) {
					//console.log('uploadProgress:' + percentage)
				});
				//当文件上传成功时触发。
				uploader.on( 'uploadSuccess', function(file, result) {
					//拿到返回值，再去做其它事情;
					var result = result.result;
					
					if(result == "success"){
	                	show(window.entityId, {
	                		callback : function(){
	                			Ext.getBody().unmask();
	                			top.frames['modalFrame'].refreshData();
	    	                	cancelclick();		
	                		}
	                	});
	                    return;
	                }else if(result == "fileSizeExceed"){
	                	clearMask();
	                	top.showMessageDlg('@COMMON.tip@', '@SERVICE.allfileSize@');
	                	return;
	                }else{
	                	clearMask();
	                	top.showMessageDlg('@COMMON.tip@', '@SERVICE.uploadFail@');
	                	return;
	                }
				});
				//当文件上传出错时触发
				uploader.on( 'uploadError', function( file ) {
					//console.log('uploadError上传出错');
					clearMask();
					top.showMessageDlg('@COMMON.tip@', '@UPLOAD.fail@');
				});
				//不管成功或者失败，文件上传完成时触发。
				//这里虽然可以获取successNum和FailNum,但是也不一定准确，只能证明上传文件是成功的，不代表excel里面的数据是有用的;
				/*uploader.on( 'uploadComplete', function( file ) {
					console.log('uploadComplete')
					var json = uploader.getStats();
					if(json && json.successNum){
						console.log( String.format('成功上传{0}个文件', json.successNum) )
					}
					if(json && json.uploadFailNum){
						console.log( String.format('上传失败{0}个文件', json.uploadFailNum) )
					}
				});*/
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
			//导入;
			function startUpload(){
				var reg = /^[a-zA-Z0-9.()_-]{1,31}$/,
				    $savingName = $("#savingName"),
				    $fileInput = $("#fileInput");
				
				//验证保存名;
				if(!reg.test($savingName.val())){
					$savingName.focus();
					return;
				}
				//验证上传控件输入框;
				if($fileInput.val() == ""){
					$(".btns").fadeOut(function(){
						$(this).fadeIn();
					});
					return;
				}
				
				var fileName = $("#fileInput").val();
				if( fileName ){
				    showBodyMask('@UPLOAD.loading@');
				    uploader.options.formData = {
				    	entityId: window.entityId,
				    	versionFileName: $savingName.val()
					}
				    uploader.upload();
				} else {
					window.parent.showMessageDlg('@COMMON.tip@', '@NAMEIMPORT.chooseFile@');
				}
			}
		</script>
	</head>
	<body class="openWinBody">
		<div class="openWinHeader borderBorderNone">
		    <div class="openWinTip">
		    	<p>@tip.pleaseUpload@</p>
		    </div>
		    <div class="rightCirIco pageCirIco"></div>
		</div>
		<div class="uploadBar">
			<div class="uploadBarInner">
				<div class="uploaderBarInnerRight"></div>
			</div>
		</div>
		<div class="edgeTB10LR20 pT40">
		    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
		        <tbody>
		            <tr>
		                <td class="rightBlueTxt" width="200">
		                	@SERVICE.savingName@
		                </td>
		                <td>
		                    <input type="text" class="normalInput w200" id="savingName" 
		                    	tooltip="@SERVICE.destFileTip@" />
		                </td>
		            </tr>
		            <tr class="darkZebraTr">
		                <td class="rightBlueTxt">
		                	@SERVICE.selectFile@
		                </td>
		                <td>
		                	<input id="fileInput" class="normalInput floatLeft" type="text" disabled="disabled" 
		                		placeholder="@SERVICE.fileSize@" />
		                	<%-- <a id="chooseFile" href="javascript:;" class="nearInputBtn">
		                		<span>@COMMON.browse@...</span>
		                	</a> --%>
		                	<div class="btns">
                    			<div id="picker">@COMMON.browse@...</div>
                    		</div>
		                </td>
		            </tr>
		        </tbody>
		    </table>
		    <div class="noWidthCenterOuter clearBoth">
		        <ol class="upChannelListOl pB40 pT40 noWidthCenter">
		        	<li>
		        		<a onclick="startUpload()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrUp"></i>@COMMON.upload@</span></a>
		        	</li>
		            <%-- <li>
		            	<a href="javascript:;" class="normalBtnBig" onclick="uploadClick()">
		            		<span><i class="miniIcoData"></i>@COMMON.upload@</span>
		            	</a>
		            </li> --%>
		            <li>
		            	<a href="javascript:;" class="normalBtnBig" onclick="cancelclick()">
		            		<span><i class="miniIcoForbid"></i>@COMMON.cancel@</span>
		            	</a>
		            </li>
		        </ol>
		    </div>
		</div>
	</body>
</Zeta:HTML>