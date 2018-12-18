var page = {
	loadTab2 : false,    //是否已经加载了第二页，如果加载了，今后切换不再需要再次加载;
	editLevel   : null,  //要改变哪个告警的声音;
	upFileName : ''      //上传的文件名;
};
			
/* 创建当前配置table */
function createConfigTable(arr){
	var str = '';
	    str += '<table class="dataTable" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">';
	    str += '<thead>';
	    str +=     '<tr>';
	    str +=         '<th width="100">@ALERT.level@</th>';
	    str +=         '<th>@ALERT.fileName@</th>';
	    str +=         '<th>@ALERT.description@</th>';
	    str +=         '<th width="120">@COMMON.opration@</th>';
	    str +=     '</tr>';
	    str += '</thead>';
	    str += '<tbody>';
	for(var i=0,len=arr.length; i<len; i++){
		if(i%2 == 0){
		  str +=     '<tr class="light">';
		}else{
		  str +=     '<tr>';
		}
		var levelImg = String.format('<span class="{0}" style="margin-left:10px;">{1}</span>', 'level'+arr[i].levelId+'Icon', arr[i].name);
		str +=         '<td>'+ levelImg +'</td>';
		str +=         '<td>'+ arr[i].soundName +'</td>';
		str +=         '<td>'+ arr[i].soundDesc +'</td>';
		var operating = String.format('<a href="javascript:;" onclick="changeSound({0}, \'{1}\')">@COMMON.edit@</a> / <a href="javascript:;" onclick="playSound(\'{2}\')">@ALERT.listen@</a>', arr[i].levelId, arr[i].name, arr[i].soundName);
		str +=         '<td align="center">'+ operating +'</td>';
		str +=     '</tr>';
	}
	    str += '</tbody>';
	    str += '</table>';
	$("#putConfigTable").empty().html(str);
};
/* 打开修改告警级别声音界面 */
function changeSound(levelId, levelName){
	$("#changeSoundPage").stop(true,true).fadeIn();
	$('#levelName').text(levelName);
	
	//获取对应的可选择声音列表
	$.get('fault/fetchSoundForAlertSelect.tv', {
		levelId: levelId
	}, function(sounds){
		//标记当前待修改告警等级
		page.editLevel = levelId;
		createSelTable(sounds);
		$("#changeSoundSave").removeAttr("disabled");
	})
}
/* 关闭选择页面 */
function closeChangeSoundPage(){
	$("#changeSoundPage").stop(true,true).fadeOut();
}
/* 更改后，保存 */
function saveChange(){
	var changeId = $("#putSelSoundTable .blueBg").attr("name");
	
	//保存对告警等级对应声音的修改
	$.post('/fault/editLevelSound.tv',{
		levelId: page.editLevel,
		soundId: changeId
	}, function(){
		top.afterSaveOrDelete({
			title : "@COMMON.tip@",
			html : '@ALERT.saveSuccess@'
		});
		//刷新当前配置列表;
		$.get('/fault/fetchAllAlertLevels.tv', function(alertLevels){
			createConfigTable(alertLevels);
			//修改的那一行闪烁;
			$("#putConfigTable .dataTable tbody").find("tr").eq(page.editLevel - 1).addClass('flashTr');
		});
		
		//关闭选择页面
		closeChangeSoundPage();
	})
};

/* 点击当前配置下的编辑按钮。创建table */
function createSelTable(arr){
	var str = '<table class="dataTable" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="rows">';
	    str += '<thead>';
	    str +=     '<tr>';
	    str +=         '<th width="20"></th>';
	    str +=         '<th>@ALERT.fileName@</th>';
	    str +=         '<th>@ALERT.description@</th>';
	    str +=         '<th>@COMMON.opration@</th>';
	    str +=     '</tr>';
	    str += '</thead>';
	    str += '<tbody>';
	for(var i=0,len=arr.length; i<len; i++){
		if(i%2 == 0){
		  if(arr[i].selected === true){
		  	str +=     '<tr class="light blueBg" name="'+ arr[i].id +'">';
		  }else{
			str +=     '<tr class="light" name="'+ arr[i].id +'">';  
		  }
		}else{
	      if(arr[i].selected === true){	
		    str +=     '<tr class="blueBg" name="'+ arr[i].id +'">';
	      }else{
			str +=     '<tr name="'+ arr[i].id +'">';  
		  }
		}
		var singleBoxStr = renderSingleBox(arr[i].selected);
		str +=         '<td align="center">'+ singleBoxStr +'</td>';
		str +=         '<td>'+ arr[i].name +'</td>';
		str +=         '<td>'+ arr[i].description +'</td>';
		var newStr = String.format('<td align="center"><a href="javascript:;" onclick="playSound(\'{0}\')">@ALERT.listen@</a></td>',arr[i].name);
		str +=     newStr;
		str +=     '</tr>';
	}
	    str += '</tbody>';
	    str += '</table>';
	$("#putSelSoundTable").empty().html(str);
};

/* 点击声音文件管理，加载所有声音的列表 */
function createSoundTable(arr){
	var str = '<table class="dataTable" width="100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#DFDFDF" rules="all">';
	    str += '<thead>';
	    str +=     '<tr>';
	    str +=         '<th width="160">@ALERT.fileName@</th>';
	    str +=         '<th>@ALERT.description@</th>';
	    str +=         '<th width="160">@COMMON.opration@</th>';
	    str +=     '</tr>';
	    str += '<thead>';
	    str += '<tbody>';
	for(var i=0,len=arr.length; i<len; i++){
		if(i%2 == 0){
		  str +=     '<tr class="light">';
		}else{
		  str +=     '<tr>';
		}
		str +=         '<td>'+ arr[i].name +'</td>';
		str +=         '<td>'+ arr[i].description +'</td>';
		if(arr[i].deletable == 0){
			str +=         '<td align="center" name="'+ arr[i].id +'"><span class="cccGrayTxt">@COMMON.delete@</span> / <a href="javascript:;" class="editDes">@ALERT.editDescription@</a> / <a href="javascript:;" onclick="playSound(\''+ arr[i].name +'\')">@ALERT.listen@</a></td>';
		}else if(arr[i].deletable == 1){
			str +=         '<td align="center" name="'+ arr[i].id +'"><a href="javascript:;" class="delSound">@COMMON.delete@</a> / <a href="javascript:;" class="editDes">@ALERT.editDescription@</a> / <a href="javascript:;" onclick="playSound(\''+ arr[i].name +'\')">@ALERT.listen@</a></td>';	
		}
		str +=     '</tr>';
	}
	    str += '</tbody>';
	    str += '</table>';
	$("#putSoundTable").empty().html(str);
};
function renderSingleBox(b){
	var str = '';
	if(b === true){
		str += '<a href="javascript:;" class="singleBox singleBoxSelected"></a>';
	}else{
		str += '<a href="javascript:;" class="singleBox"></a>';
	}
	return str;
};

/*显示上传按钮*/
function showUploadPage(){
	$("#uploadPage").stop(true,true).fadeIn();
};
/* 关闭 */
function closeWin(){
	top.closeWindow("alertSound");
};
/* 关闭上传页面 */
function closeUploadPage(){
	$("#uploadPage").stop(true,true).fadeOut();
};

/*播放声音*/
function playSound(name){
	var $musicPlayer = $("#musicPlayer"),
		$putMusic = $("#putMusic"),
	    srcStr = '../sounds/' + name;
	$musicPlayer.attr("src",srcStr);
	try{
		$musicPlayer.get(0).play();
	}catch(err){
		$("#putMusic").html('<embed id="chime" name="chime" hidden=true autostart=true src="' + srcStr + '">');
	}
};
function checkSoundIsUsed(str){
	var flag = false;
	$("#putConfigTable tbody").find("tr").each(function(){
		var str2 = $(this).find("td:eq(1)").text();
		if(str2 == str){
			flag = true;
			return false;
		}
	});
	return flag;
}


$(function(){
	$("#soundFile").fakeUpload("init",{    
		"tiptext":'@ALEER.pleaseSelectSound@' ,
		"width":195,
		"btntext":'@ALERT.select@',
		"checkfn":function(filePath,name){
			//获取文件名
			var reg = /[^\\]+\.[\w]+$/;
			$('#file_decr').val(reg.exec(filePath));
			page.upFileName = (reg.exec(filePath))[0]; //全局变量保存上传的文件名，后期做验证;
			$("#soundFile").find("input[type='text']").val(filePath);
			return true;
		}
	}); 
	
	/* 创建当前配置table */
	//获取告警等级对应声音文件，并组装table
	$.get('/fault/fetchAllAlertLevels.tv', function(alertLevels){
		createConfigTable(alertLevels);
	});
	
	//对选项卡的点击;
	$("#outerTab a").click(function(){
		var $me = $(this),
		    clickIndex = $(this).parent().index();
		if(clickIndex === 0){$("#putConfigTable .dataTable tbody tr").removeClass("flashTr");}//chrome下，切换会闪烁，这是不对的;
		if($me.hasClass("tabSel")){ return;}
		$("#outerTab a").removeClass("tabSel");
		$me.addClass("tabSel");
		$(".tabBody").css({display: 'none'});
		$(".tabBody").eq(clickIndex).stop(true,true).fadeIn();
		if(!page.loadTab2){
			page.loadTab2 = true;
			createSoundTable(allSound);
		}
	});
	
	//对当前配置，文件的选择点击;
	$("#putSelSoundTable").delegate("a.singleBox","click",function(){
		var $me = $(this)
		    $clickTr = $me.parent().parent(),
		    clickIndex = $clickTr.index();
		if($me.hasClass("singleBoxSelected")){ return;}
		 
		$("#putSelSoundTable table tbody tr").removeClass("blueBg");
		$("#putSelSoundTable a.singleBox").removeClass("singleBoxSelected");
		$me.addClass("singleBoxSelected");
		$me.parent().parent().addClass("blueBg");
	});
	
	//对声音文件管理板块点击;
	$("#putSoundTable").delegate("a","click",function(){
		var $me = $(this),
		    clickIndex = $me.parent().parent().index();
		//修改描述按钮;
		if($me.hasClass("editDes")){
			var $des = $("#putSoundTable tbody").find("tr:eq("+ clickIndex +")").find("td:eq(1)"),
			    desValue = $des.text();
			if($des.find(":text").length > 0){return;}
			var editHtmlStr = '<ul class="leftFloatUl"><li><input type="text" class="normalInput" value="{0}" name="{0}" toolTip="@COMMON.anotherName@" /></li><li><a href="javascript:;" class="normalBtn editConfirm"><span>@COMMON.confirm@</span></a></li><li><a href="javascript:;" class="normalBtn cancelEdit"><span>@COMMON.cancel@</span></a></li></ul>';
			var editHtml = String.format(editHtmlStr, desValue);
			$des.html(editHtml);
			$des.find(":text").focus();
		};
		//删除按钮;
		if($me.hasClass("delSound")){
			//通过文件名，来判断当前告警是否正在使用这个声音，如果正在使用，则不能删除;
			var sName = $me.parent().parent().find("td:first").text();
			if( checkSoundIsUsed(sName) ){ 
				top.afterSaveOrDelete({
					title : '@COMMON.tip@',
					html : '@COMMON.soundIsUse@'
				});
				return;
			}
			var delId = $me.parent().attr("name");
			//TODO 确认是否删除逻辑请自行添加
			$.post('/fault/deleteAlertSound.tv',{
				soundId: delId
			}, function(ret){
				//TODO 请自行更新表格
				if(ret.success){
					$.get('/fault/fetchAllSounds.tv', function(allSound){
						createSoundTable(allSound);
					});
					top.afterSaveOrDelete({
						title : '@COMMON.tip@',
						html : '@ALERT.deleteSuccess@'
					});
				}else{
					alert('@ALERT.delFail@'+ret.msg);
				}
			});
		};
		//编辑的确定按钮;
		if($me.hasClass("editConfirm")){
			var $clickTr = $me.parent().parent().parent().parent(),
			    newClickIndex = $clickTr.index(),
			    newValue = $clickTr.find(":text").val(),
			    editId = $clickTr.find("td:last").attr("name");
			
			if( !V.isAnotherName(newValue) ){//验证不通过;
				$clickTr.find(":text").focus();
			}else{
				//修改对应声音的描述信息
				$.post('/fault/editSoundDescription.tv', {
					soundId: editId,
					description: newValue
				}, function(){
					$me.parent().parent().parent().html(newValue);
				})
			}
		};
		//取消编辑;
		if($me.hasClass("cancelEdit")){
			var $me = $(this),
				$td = $me.parent().parent().parent(),
				oldValue = $td.find(":text").attr("name");
			$td.html(oldValue);
		}
	});
	
	//验证上传文件的文件名;
	function testFileName(para){
		$(para).stop(true,true).animate({
			opacity:0
		},function(){
			$(this).animate({opacity:1});
		})
	}
	
	//上传文件
	$('#uploadFile').bind('click', function(){
		//上传文件名后缀验证;
		var len = page.upFileName.length;
		if(len > 4){
			var lastTxt = (page.upFileName).slice(len-4, len);
			if( !(lastTxt == ".mp3") ){
				testFileName('#topTip2');
				return;
			}
		}else{
			testFileName('#topTip2');
			return;
		}
		if( !V.isAlertSoundName(page.upFileName.slice(0,len-4)) ){
			testFileName('#topTip');
			return;
		}
		//验证描述;
		var $des = $("#file_decr"),
		    desValue = $des.val();
		if ( !V.isAnotherName(desValue) ){
			$des.focus();
			return;
		}
		//TODO 需要验证文件名和文件大小，不能重名
		//重名校验URL：/fault/checkDuplicateSoundName.tv ,文件名：soundName
		var flag = false;
		$.ajax({
		  url: "/fault/checkDuplicateSoundName.tv",
		  data : {soundName : page.upFileName},
		  async: false,
		  success: function(json){
		    if(json.isDuplicate){ //如果存在同名文件;
		    	top.afterSaveOrDelete({
					title : '@COMMON.tip@',
					html : '@ALERT.sameName@'
				});
		    	flag = true;
		    }
		  }
		});
		if(flag){ return;}
		//上传
		$.ajaxFileUpload({
			url:'/fault/upLoadAlertSound.tv',
			secureuri:false,
			fileElementId:'soundFile',
			data: {description:$('#file_decr').val()},
			dataType: 'json',
			success: function (data, status){
				if(data.success){
					//TODO 上传成功后更新表格自行处理
					top.afterSaveOrDelete({
						title : '@COMMON.tip@',
						html : '@ALERT.uploadSuccess@'
					});
					//清空文件
					clearUpload();
					closeUploadPage();
					$.get('/fault/fetchAllSounds.tv', function(allSound){
						createSoundTable(allSound);
					});
				}else{
					top.afterSaveOrDelete({
						title : '@COMMON.tip@',
						html : data.error
					});
					//清空文件
					clearUpload();
				}
				
	      	}, error: function (data, status, e){
	      		alert('@ALERT.uploadFail@');
	        },cache: false,
			complete: function (XHR, TS) {XHR = null ;}
		});
	});
	
	function clearUpload(){
		$("#soundFile").find("input[type=file]").val('');
		$("#soundFile").find("input[type=text]").val('');
		$('#file_decr').val('');
	}
	
});//end document.ready;