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
    module cmc
</Zeta:Loader>
<script type="text/javascript">
var policyNames = ${policyNames};
var tempStr = "@characterLimitedIn100@";
function showLimitedTips(id, str) {
	str = tempStr;
	var obj=document.getElementById(id);
	var x=getX(obj);
	var y=getY(obj);
	var l=x;var t=y-5;
	var popObj=document.getElementById("tips");
	if(popObj!=null) {
		popObj.innerHTML="<div class=\"tipcon\">"+str+"</div>";
		popObj.style.display="inline";
		var menu_h=popObj.offsetHeight;
		t-=menu_h;
		popObj.style.left=l+"px";
		popObj.style.top=t+"px";
		/*10000 > Ext.window的zIndex > 9000 ，为了防止弹出弹力的tootlip丢失，故设置一个较高的zindex ,modify by @bravin */
		popObj.style.zIndex = 100000;
	}
}
function hideTips() {
	document.getElementById("tips").style.display='none';
}
function setLoadBalTpl(tplName,policyType,disSections){
	$.ajax({
        url: '/cmc/loadbalance/addLoadBalPolicyTpl.tv',
        type: 'POST',
        cache:false,
        dataType:'json',
        data:{
            "policyName":tplName,
            "policyType":policyType,
            "disSections":disSections
        },
        success: function(data) {
            if(data.success){
                window.top.closeModalDlg();
                top.afterSaveOrDelete({
       				title: '@COMMON.tip@',
       				html: '<b class="orangeTxt">@resources/COMMON.saveSuccess@</b>'
       			});
            }else{
                window.top.showMessageDlg("@COMMON.tip@", "@operationFail@");
            }
        }
    });
}
    // ---------------------函数定义------------------------------
    function showPeriod(b){
       $("#period_tr").css("visibility",b?"visible":"hidden");
    }
    
    function initPeriodSel($s){
        var hours=['00','01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16','17','18','19','20','21','22','23','24'];
        var minuts=['00','30'];
        
        if($s.hasClass("sel_start")){
            $s.append("<option value='-1'>@COMMON.startTime@</option>");
        }else if($s.hasClass("sel_end")){
            $s.append("<option value='-1'>@COMMON.endTime@</option>");
        }
        
        for(var i=0;i<hours.length-1;i++){
            for(var j=0;j<minuts.length;j++){
                var v=hours[i]+":"+minuts[j];
                $s.append("<option value='"+v+"'>"+v+"</option>");
            }
        }
        $s.append("<option value='24:00'>24:00</option>");
    }
    
    function getDisSections(){
        var sections=[];
        $("#period_ctx").find("div").each(function(){
            var $sels=$(this).find("select");
            var startTime=$sels[0].value;
            var endTime=$sels[1].value;
            if(startTime!=-1 && endTime!=-1){
                sections.push(startTime+"-"+endTime);
            }
        });
        return sections;
    }
    function getDisSectionsToStr(){
        var sections='';
        $("#period_ctx").find("div").each(function(){
            var $sels=$(this).find("select");
            if($sels[0].value!=-1&&$sels[1].value!=-1){
                sections = sections+"@"+$sels[0].value+"-"+$sels[1].value;
            }
        });
        sections = sections.substring(1);
        return sections;
    }
    function checkDisSectionsStartEndTime(){
    	var result = true;
    	var sections=[];
        $("#period_ctx").find("div").each(function(){
            var $sels=$(this).find("select");
            var startTime=$sels[0].value;
            var endTime=$sels[1].value;
            var startTimeArr = startTime.split(':');
            var endTimeArr = endTime.split(':');
            var st = startTimeArr[0]*60+startTimeArr[1];
            var et = endTimeArr[0]*60+endTimeArr[1];
            //alert((parseInt(st)>=parseInt(et))+"v"+parseInt(st)+"/"+parseInt(et));
            if(parseInt(st)>=parseInt(et)){
            	result = false;//
            	return result;
            }
        });
        return result;
    }
    // -----------------------------------------------------------
    
    
   $(function(){
    $("#btn_add_period").click(function(){
        var $div=$('<div style="margin-bottom: 3px;"><select style="width:100px;" class="sel_start"></select>&nbsp; - &nbsp;<select style="width:100px;" class="sel_end"></select> &nbsp;<img src="/images/minus.png" align="absmiddle" style="cursor: pointer;"/></div>');
        $("#period_ctx").append($div);
        $div.find("img").click(function(){
            $div.remove();
        });
        $div.find("select").each(function(){
            initPeriodSel($(this));
        });
    });
    
    $("#period_ctx").find("select").each(function(){
        initPeriodSel($(this));
    });
    
    $("#policy_type").change(function(){
        if(this.value==3){
            showPeriod(true);
        }else{
            showPeriod(false);
        }
    });
    
    
    showPeriod(false);
    $("#tpl_name").focus();
});
 //不能输入特殊字符。是否为空或是含有特殊字符
function validateName(str){
 	var reg = /^[a-zA-Z0-9\u4e00-\u9fa5]+$/
 	if(str.indexOf(" ")>-1 || !reg.test(str)){
 		return true;
 	}else{
 		return false;
 	}
}
function okClick(){
    var tplName=$.trim($("#tpl_name").val());
    var policyType=$("#policy_type").val();
    var disSections=getDisSections();
    
    if(tplName==""){
        //window.top.showMessageDlg("@COMMON.tip@","@enterTplName@");
        $('#tpl_name').attr('tooltip','@characterLimitedIn100@');
        $('#tpl_name').focus();
        return;
    }else if(validateName(tplName)){
    	//return window.parent.showMessageDlg("@COMMON.tip@", "@characterLimitedIn100@","tip",function(){
			//$("#tpl_name").focus();
		//});
    	$('#tpl_name').attr('tooltip','@characterLimitedIn100@');
    	$('#tpl_name').focus();
    }else if(policyType==3&&disSections.length==0){
        //window.top.showMessageDlg("@COMMON.tip@","@enterTimeSection@,@beginTimeIslarger@");
        top.afterSaveOrDelete({
   				title: '@COMMON.tip@',
   				html: '<b class="orangeTxt">@enterTimeSection@,@beginTimeIslarger@</b>'
   		});
        return;
    }else{
    	var conflict = false;
    	var pN = policyNames.data;
    	$.each(pN,function(i){
    		if(tplName==pN[i]){
    			conflict = true;
    			//window.top.showMessageDlg("@COMMON.tip@","@ConflictWithTheName@");
    			$('#tpl_name').attr('tooltip','@ConflictWithTheName@');
    	    	$('#tpl_name').focus();
                return;
    		};
    	})
    	if(conflict){
    		return false;
    	}
    	var timeStr = "";
    	$.each(disSections,function(i){
    		timeStr +=disSections[i]+" ";
    	})
    	if(policyType==3&&!checkDisSectionsStartEndTime()){
			//window.top.showMessageDlg("@COMMON.tip@","@beginTimeIslarger@");
			top.afterSaveOrDelete({
   				title: '@COMMON.tip@',
   				html: '<b class="orangeTxt">@beginTimeIslarger@</b>'
   			});
    		return;
    	}
    	if(3!=policyType){
    		setLoadBalTpl(tplName,policyType,disSections);
    	}else{
    		timeStr = "@selectedTimeSection@:"+timeStr;
        	window.top.showOkCancelConfirmDlg("@COMMON.tip@", timeStr, function (type) {
	        	if(type=="ok"){
	        		setLoadBalTpl(tplName,policyType,getDisSectionsToStr());
	        	}
        	})
    	}
    	
    }
}
function closeWindow(){
    window.top.closeModalDlg();
}
</script>
<script type="text/javascript" src="/js/jquery/nm3kToolTip.js"></script>
</head>
<body class="openWinBody">
	<div class="openWinHeader">
	    <div class="openWinTip"></div>
	    <div class="rightCirIco pageCirIco"></div>
	</div>
	<div class="edgeTB10LR20 pT40">
	    <table class="zebraTableRows" cellpadding="0" cellspacing="0" border="0">
	        <tbody>
	            <tr>
	                <td class="rightBlueTxt" width="260">
	               	    @COMMON.name@:
	                </td>
	                <td>
	                	<input type="text" style="width:210px;" id="tpl_name" maxlength="100" class="normalInput"
						toolTip='@characterLimitedIn100@'  />
	                </td>
	            </tr>
	             <tr class="darkZebraTr">
	                <td class="rightBlueTxt">
	                	@policy@:
	                </td>
	                <td>
	                	<select style="width:215px;" id="policy_type" class="normalSel">
			                <option value="1">@policyEnabled@</option>
			                <option value="2">@policyDisabled@</option>
			                <option value="3">@disabedPeriod@</option>
			            </select>
	                </td>
	            </tr>
	            <tr id="period_tr" style="visibility:hidden;">
			        <td class="rightBlueTxt withoutBorderBottom" valign="top" >
			        	<p style="padding-top:3px">@timeSection@:</p>
			        </td>
			        <td  class="withoutBorderBottom">
				        <div style="height:210px;overflow: auto;" id="period_ctx">
				            <div style="margin-bottom: 3px;">
				            <select style="width:100px;" class="sel_start"></select>
				            &nbsp;-&nbsp;
				            <select style="width:100px;" class="sel_end"></select>&nbsp;
				            <img src="/images/add.png" align="absmiddle" style="cursor: pointer;" id="btn_add_period"/>
				            </div>
				        </div>
			        </td>
			    </tr>
	        </tbody>
	    </table>
	</div>
<Zeta:ButtonGroup>
		<Zeta:Button id="btnOK" onClick="okClick()" icon="miniIcoAdd">@COMMON.add@</Zeta:Button>
		<Zeta:Button onClick="closeWindow()" icon="miniIcoForbid">@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>
