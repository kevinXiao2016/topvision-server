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
    module epon
    css css/white/disabledStyle
</Zeta:Loader>
<title>@SERVICE.fileMgmt@</title>
<script type="text/javascript" src="/include/i18n.tv?modulePath=com.topvision.ems.epon.resources,com.topvision.ems.network.resources&prefix=&lang=<%=uc.getUser().getLanguage() %>"></script>
<script type="text/javascript" src="/js/tools/authTool.js"></script>
<script type="text/javascript">
var pageSize = <%= uc.getPageSize() %>;
var entityId = '${entityId}';
var ftpServiceEnable = '${ftpServiceEnable}'
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;
var refreshDevicePower= <%=uc.hasPower("refreshDevice")%>;
var fileNamePathArray = new Array();
var grid = null;
var store = null;
var selections = null ;
function onRefreshClick() {
	store.reload();
}
function renderOperation(value, p, record){ 
  if(record.get('fileAttribute')=='1'){
	  if(operationDevicePower){		  
	      //var str ="<image title='" +I18N.COMMON.del +"' src='image/fileDelete.gif' onclick='deleteFile(\""+record.get('fileName')+"\",\""+record.get('filePath')+"\")' />&nbsp;&nbsp;&nbsp;&nbsp;<image title='" +I18N.COMMON.download +"' src='../images/download.gif'  onclick='downLoad(\""+record.get('fileName')+"\",\""+record.get('filePath')+"\")' />";
	      var str = "<a href='javascript:;' onclick='deleteFile(\""+record.get('fileName')+"\",\""+record.get('filePath')+"\")'>"+ I18N.COMMON.del +"</a> / <a onclick='downLoad(\""+record.get('fileName')+"\",\""+record.get('filePath')+"\")' >"+ I18N.COMMON.download +"</a>";
	  }else{
		  var str = '<span class="cccGrayTxt">'+ I18N.COMMON.del +'</span> / <span class="cccGrayTxt">'+ I18N.COMMON.download +'</span>';
	  }
   return str;
  }
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

function refreshData(){
	store.reload();
}

function ftpServiceNoEnable(){
	 window.parent.showMessageDlg(I18N.COMMON.tip, I18N.EPON.ftpNotStart ,null)
}

function renderFileSize(value, metadata , record){ 
  var str = record.get("fileSize")
  if(str>1024&&str<1024*1024){
     str = DecimalNum(str/1024) + 'KB'
  }else if(str>1024*1024&&str<1024*1024*1024){
     str = DecimalNum(str/(1024*1024)) + 'MB'
  }else if(str>1024*1024*1024){
     str = DecimalNum(str/(1024*1024*1024)) + 'GB'
  }else {
	 str = str + 'B'
  }
  metadata.attr = String.format('@SERVICE.fileSizeIs@', record.get("fileSize"))  
  return str
}
function closeWindow(){  
  	window.parent.closeWindow('fileManage');
}
var upform = new Ext.form.FormPanel({
    labelAlign:'right',
    labelWidth:100,
    frame:true,
    url:'ftpValidate.tv',
    defaultType:'textfield',
    items:[{
            fieldLabel: '@SERVICE.plsIptUsername@',
            name:'userName'
        },{
      	    fieldLabel: '@SERVICE.plsIptPwd@',
            name:'passWord'
        }]
}) ;
var downform = new Ext.form.FormPanel({
    labelAlign:'right',
    labelWidth:100,
    frame:true,
    url:'epon/ftpValidate.tv',
    defaultType:'textfield',
    items:[{
            fieldLabel: '@SERVICE.plsIptUsername@' ,
            name:'userName'
        },{
            fieldLabel: '@SERVICE.plsIptPwd@' ,
            name:'passWord'
        }]
});
var downWin = new Ext.Window({
    el:'downWindow',
    title: '@SERVICE.ftpAuth@' ,
    width:300,
    height:130,
    items:[downform],
    closeAction:'hide',
    modal : true ,
    buttons:[{
        text: I18N.COMMON.submit,
        handler:function(){ 
        downform.getForm().submit({
            success:function(downform,action){
                window.location.href="/config/downFile.jsp?fileName="+selections[0].get("filePath")+"/"+selections[0].get("fileName");
            },
            failure:function(){
                Ext.Msg.alert('Error', I18N.SERVICE.pwdNotExist )
            }
        });
    }}]
});
var upWin = new Ext.Window({
    el:'upWindow',
    title: '@SERVICE.ftpAuth@' ,
    width:300,height:130,
    id:'upWin',
    items:[upform],
    modal : true ,
    closeAction:'hide',
    buttons:[{
       text: I18N.COMMON.submit,
       handler:function() { 
            upform.getForm().submit({        
            success:function(upform,action){
                upWin.hide();	
          		window.parent.createDialog("uploadfile", I18N.SERVICE.plsSelectFile ,400,300,"epon/upLoadFile.tv", null, true, true);
            },
            failure:function(){
                Ext.Msg.alert('Error', I18N.SERVICE.pwdNotExist )
            }
        });
    }}]
});
function deleteFile(fileName,filePath){
	window.top.showOkCancelConfirmDlg(I18N.COMMON.tip, I18N.SERVICE.cfmDelFile , function (type) {
	   if(type == "ok"){
            window.top.showWaitingDlg(I18N.COMMON.wait, I18N.SERVICE.deleting)
	   //window.parent.closeWaitingDlg();
			jQuery.ajax({
				cache:false,
	    		url: "/epon/deleteFile.tv?entityId="+entityId+"&fileFileName="+fileName+"&filePath="+filePath+"&c="+Math.random(),
				dataType : "text",
				success : function(text) {		 
					window.parent.closeWaitingDlg();
					if (text != null && text != "deleteOK") {  
						window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.deleteError , 'error');
					} else {
						top.nm3kRightClickTips({
		    				title: I18N.COMMON.tip,
		    				html: I18N.SERVICE.deleteOk
		    			});
                        //window.parent.showMessageDlg(I18N.COMMON.tip, I18N.SERVICE.deleteOk);
		                store.load();
				    }
				}, error: function(text) {
					window.parent.closeWaitingDlg();
					window.parent.showMessageDlg(I18N.SERVICE.error, I18N.SERVICE.deleteError , 'error')
				}
			});
		}
	});
}
function upLoad(){
   if(ftpServiceEnable=='true'){
        var filePathArray = new Array();	
        for(var i=0 ; i<store.getCount(); i++){
            var record = store.getAt(i);
            if(filePathArray.indexOf(record.get('filePath'))==-1){
            	filePathArray.push(record.get('filePath'));
                }
    	    }
        var fileNameArray = new Array();	
        for(var i=0 ; i<store.getCount(); i++){
            var record = store.getAt(i);
            // 原先认为所有的文件不能重复，其实对于不同的文件夹，当然可以存在相同的文件
            //   if(fileNameArray.indexOf(record.get('fileName'))==-1){
            	fileNameArray.push(record.get('filePath')+record.get('fileName'));
            //       }
    	   }
    	window.parent.createDialog("uploadfile", I18N.SERVICE.plsSelectFile , 600,350,"epon/upLoadFile.tv?fileNameArray="+fileNameArray+"&filePath="+filePathArray+"&entityId="+entityId, null, true, false,function(){
    	   		window.parent.stopProgress();
    		});	
    }else{
        ftpServiceNoEnable();
    }
}

function downLoad(fileName,filePath){   
    if(ftpServiceEnable=='true'){
	    window.top.showWaitingDlg(I18N.COMMON.wait, I18N.SERVICE.downloading);
      	jQuery.ajax({
				cache:false,
				url: "epon/controlOltUpLoad.tv?entityId="+entityId+"&fileFileName="+fileName+"&filePath="+filePath+"&c="
                    +Math.random(),
				dataType : "text",
				success : function(text) {	 
					var textResult = text.split(':')[0];
					window.parent.closeWaitingDlg();	 
					if (textResult != null && textResult != "downLoadSuccess") {  
						window.parent.showMessageDlg(I18N.COMMON.tip, text, 'error')
					} else {
						window.location.href="/epon/downFile.tv?fileFileName="+text.split(':')[1]+"&filePath="+filePath+"&fileFtpServerFileName="+text.split(':')[2];   
				}
				}, error: function(text) {
					window.parent.closeWaitingDlg();
					window.parent.showMessageDlg(I18N.SERVICE.error, I18N.SERVICE.setEr , 'error')
				}
		});
    }else{
    	ftpServiceNoEnable();
    }
}
 
function buildPagingToolBar() {
    var pagingToolbar = new Ext.PagingToolbar({id: 'extPagingBar', displayInfo: true, 
        items: ["-", 
            String.format(I18N.SERVICE.pageBarTip, 100)
        ]
    });
    return pagingToolbar;
}
Ext.onReady(function () {    	
	Ext.QuickTips.init();
	var w = Ext.getBody().getWidth();
	var h = $(window).height();
	if(h<200){h=200;}
	h = h<200?200:h;
	var cm = new Ext.grid.ColumnModel([
		{header: '@SERVICE.filePath@', dataIndex:'filePath',align:"center"},
		{header: '@SERVICE.fileName@', dataIndex:'fileName',align:"center"},
		{header: '@SERVICE.fileSize@', dataIndex:'fileSize',align:"center",sortable:true, renderer:renderFileSize},
		{header: '@SERVICE.fileMdfTime@', dataIndex:'strFileModifyTime',align:"center"},
		{header: '@SERVICE.fileManu@', dataIndex:'fileAttribute',align:"center",renderer:renderOperation}	    
	   ]);
	store = new Ext.data.GroupingStore({
		proxy : new Ext.data.HttpProxy({
			url: 'epon/fileShow.tv?entityId='+entityId,
			method: 'POST'
		}),
		reader: new Ext.data.JsonReader({  
		  	root: 'data',
		  	totalProperty: 'totalProperty',   
		  	fields:['filePath', 'fileName', 'fileSize','strFileModifyTime','fileAttribute']
		}),
		remoteSort: false, //是否从后台排序
		groupField: 'filePath',
		sortInfo:{field:'fileSize',direction:'ASC'}   
	});
	
	var toolbar = [
		{text: I18N.COMMON.refresh, iconCls: 'bmenu_refresh', handler: onRefreshClick}
	];
	grid = new Ext.grid.GridPanel({
		height:h-62, 
		renderTo: 'grid',
	      	view: new Ext.grid.GroupingView({
	      	enableGroupingMenu : false, forceFit: true, 
	          groupTextTpl: '{text}(<b><font color=red>{[values.rs.length > 1 ?values.rs.length-1:""]}</font></b>     {[values.rs.length > 1 ? I18N.EPON.fileGroupTip  : I18N.EPON.fileNoGroupTip ]})'
	      	}),
	      	store: store,
	      	loadMask:{msg : I18N.COMMON.loading},    
	      	cm: cm
	});
	store.load({callback : function(records, options, success) {
		if(!operationDevicePower){
			R.addButton.setDisabled(true);
		}
	    if(!refreshDevicePower){
	        R.refreshData.setDisabled(true);
	    }
	}});
});
</script>
</head>
<body class="openWinBody">
	<div class="edge10">
		<div id="grid" class="normalTable"></div>
		<Zeta:ButtonGroup>
			<Zeta:Button id="refreshData" onClick="refreshData()" icon="miniIcoEquipment">@COMMON.fetch@</Zeta:Button>
			<Zeta:Button id="addButton" onClick="upLoad()" icon="miniIcoArrUp">@BUTTON.upload@</Zeta:Button>
			<Zeta:Button id="closeButton" onClick="closeWindow()" icon="miniIcoForbid">@BUTTON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</div>
	<div id="downWindow"></div>
	<div id="upWindow"></div>
</body>
</Zeta:HTML>
