<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://www.topvision.com/zetaframework" prefix="Zeta"%>
<!DOCTYPE html>
<Zeta:HTML>
<head>
<%@include file="/include/ZetaUtil.inc"%>
<link rel="stylesheet" type="text/css" href="/css/white/disabledStyle.css">
<Zeta:Loader>
    library ext
    library jquery
    library zeta
    module cmc
    import /js/json2
    css css.white.disabledStyle
</Zeta:Loader>
<script type="text/javascript" src="/js/jquery/jquery.fakeUpload.js"></script>
<script type="text/javascript" src="/js/jQueryFileUpload/ajaxfileupload.js"></script>
<style type="text/css">
</style>
<script type="text/javascript">
var cmcId = '${cmcId}';
var fileName = '';

var UPDATE_STATUS = {
	0: {
		message: 'Ready',
		percent: 0
	},
	1: {
        message: 'Download start.',
        percent: 5
    },
    2: {
        message: 'Download OK.',
        percent: 20
    },
    3: {
        message: 'Agent get upgrade cmd.',
        percent: 25
    },
    4: {
        message: 'Send write request.',
        percent: 30
    },
    5: {
        message: 'Send firmware file.',
        percent: 40
    },
    6: {
        message: 'Firmware sending...',
        percent: 50
    },
    7: {
        message: 'CMC write start.',
        percent: 60
    },
    8: {
        message: 'CMC writing...',
        percent: 80
    },
    9: {
        message: 'CMC write OK.',
        percent: 100
    },
    10: {
        message: 'Dor upgrade Successfully',
        percent: 0
    }
};

var ERROR_CODE = {
	"0": 'Ready',
	"-1": 'Operation not support.',
	"-2": 'CMC info get fail.',
	"-3": 'CMC not online.',
	"-4": 'No dor online.',
	"-5": 'General Err.',
	"-6": 'IPC Err.',
	"-7": 'Dor is upgrading',
	"-8": 'Invalid param.',
	"-9": 'Download firmware failed',
	"-10": 'Open firmware failed',
	"-11": 'Firmware file size invalid.',
	"-12": 'Firmware file name invalid.',
	"-13": 'Memory alloc fail.',
	"-14": 'Request send to CMC failed.',
	"-15": 'Send data to CMC failed.',
	"-16": 'CMC write failed.',
    "-17": 'Upgrage dor firmware failed.'
};

/**
 * 上传升级文件
 */
function uploadFile() {
	//TODO 校验文件
	
	top.showWaitingDlg('@sys.waiting@', '@platform/Common.uploading@');
    $.ajaxFileUpload({
        url: '/cmc/optReceiver/upgrade.tv',
        secureuri: false,
        fileElementId: 'localFile',
        data: {
        	cmcId: cmcId,
        	fileName: fileName
        },
        dataType: 'json',
        success: function (data, status){
        	//此时只是表示正确接收文件，正在更新，需要实时获取进度
        	window.upgrageInterval =  setInterval(function() {
        		getUpgradeProgress();
        	}, 1000);
        }, error: function (data, status, e){
        	top.closeWaitingDlg();
        	top.showMessageDlg('@COMMON.tip@', '@platform/Common.uploadFailed@');
        	// add by fanzidong, 修改EMS-13611，升级成功/失败后应该重载当前页面
            location.reload();
        },cache: false,
        complete: function (XHR, TS) {
        	XHR = null ;
        }
    });
}

/**
 * 获取实时升级进度
 */
function getUpgradeProgress() {
	$.ajax({
        url: '/cmc/optReceiver/getUpgradeProgress.tv',
        data: {
            cmcId: cmcId
        },
        dataType: 'json',
        success: function(json) {
        	if(json.progress < 0) {
        		top.closeWaitingDlg();
        		//升级失败
        		top.showMessageDlg('@COMMON.tip@', ERROR_CODE[json.progress]);
        		clearInterval(upgrageInterval);
        		// add by fanzidong, 修改EMS-13611，升级成功/失败后应该重载当前页面
        		location.reload();
        	} else if(json.progress == 10) {
        		top.closeWaitingDlg();
        		//升级成功
        		top.afterSaveOrDelete({
                    title: '@COMMON.tip@',
                    html: '@opt.updateSuccessTip@' //Please retrieve new dor data 10 seconds later!
                });
        		clearInterval(upgrageInterval);
        		// add by fanzidong, 修改EMS-13611，升级成功/失败后应该重载当前页面
        		location.reload();
        	} else {
        		//升级中
        	}
        },
        error: function(data) {
        	top.showMessageDlg('@COMMON.tip@', '@platform/Common.uploadFailed@');
            clearInterval(upgrageInterval);
            // add by fanzidong, 修改EMS-13611，升级成功/失败后应该重载当前页面
            location.reload();
        }
    });
}

/**
 * 退出页面
 */
function cancelclick() {
	top.closeWindow("modalDlg");
}

$(function() {
	$("#localFile").fakeUpload("init",{    
        "tiptext": '@opt.selectUpgrade@' ,
        "width": 300,
        "btntext": '@opt.select@',
        "checkfn":function(filePath,name){
            //获取文件名
            var reg = /[^\\]+\.[\w]+$/;
            fileName = reg.exec(filePath);
            return true;
        }
    }); 
})
</script>
</head>
<body>
    <div class="openWinHeader">
        <div class="openWinTip">
            <p><b class="orangeTxt"></b></p>
        </div>
        <div class="rightCirIco upArrCirIco"></div>
    </div>
    
    <div class="edgeTB10LR20 ">
         <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
             <tbody>
                <tr>
                    <td class="rightBlueTxt" width="100">@opt.upgradeFile@@COMMON.maohao@</td>
                    <td>
                       <span id="localFile" name="localFile"></span>
                    </td>
                 </tr>
             </tbody>
         </table>
     </div>
     
     <div class="noWidthCenterOuter clearBoth pT40">
         <ol class="upChannelListOl pB0 noWidthCenter">
            <li><a  id="uploadBT" onclick="uploadFile()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoArrUp"></i>@opt.upgrade@</span></a></li>
            <li><a onclick="cancelclick()" href="javascript:;" class="normalBtnBig"><span><i class="miniIcoForbid"></i>@BUTTON.cancel@</span></a></li>
         </ol>
    </div>
</body>
</Zeta:HTML> 
