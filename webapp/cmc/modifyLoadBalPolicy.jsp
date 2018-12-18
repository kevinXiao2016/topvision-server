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
<style type="text/css">
	.goDownSpan{ position:relative; top:5px;}
</style>
<script type="text/javascript">
var operationDevicePower = <%=uc.hasPower("operationDevice")%>;

    var _entityId="${entityId}";
    var _policyId="${policyId}";
    var _policyType="${policyType}";
    var _disSections="${disSections}".split(",");
    
    // ---------------------函数定义------------------------------
   
    function updateLoadBalPol(_entityId,_policyId,policyType,disSectionsStr){
   	 $.ajax({
            url: '/cmc/loadbalance/modifyLoadBalPolicy.tv',
            type: 'POST',
            cache:false,
            dataType:'json',
            data:{
                "entityId":_entityId,
                "policyId":_policyId,
                "policyType":policyType,
                "disSections":disSectionsStr
            },
            success: function(data) {
                R.btnOK.setDisabled( false );
                $("#img_loading").hide();
                if(data.success){
                    closeWindow();
                }else{
        			if(data.msg!=undefined&&data.msg!=""){
        				window.parent.showMessageDlg("@COMMON.tip@", data.msg);
        			}else{
                        window.top.showMessageDlg("@COMMON.tip@", "@operationFail@");        				
        			}
                }
            }
        });
   }    
    // 加载策略模板
    function loadPolicyTpls(){
        $.ajax({
            url: '/cmc/loadbalance/getLoadBalPolicyTplList.tv',
            type: 'POST',
            cache:false,
            dataType:'json',
            success: function(res) {
                $.each(res.data,function(){
                    $("#policy_tpl").append("<option value='"+this.policyId+"' policytype='"+this.policyType+"' dissections='"+this.originalDisSections+"'>"+this.policyName+"</option>");
                });
                $("#policy_tpl").change(function(){
                    if(this.value!='-1'){
                        var pType=$(this).find("option:selected").attr("policytype");
                        var disSections=$(this).find("option:selected").attr("dissections").split(",");
                        $("#policy_type").val(pType);
                        if(pType=='3'){// 周期性无效
                            loadSections(disSections);
                            showPeriod(true);
                        }else{
                            showPeriod(false);
                        }
                    }
                });
            }
        });
    }
    
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
            if($sels[0].value!=-1&&$sels[1].value!=-1){
                sections.push($sels[0].value+"-"+$sels[1].value);
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
    function appendSection(startTime,endTime){
        var $div=$('<div style="margin-bottom: 3px;"><select class="sel_start normalSel w100"></select>&nbsp; - &nbsp;&nbsp;<select class="sel_end normalSel w100"></select> &nbsp;<img src="/images/minus.png" align="absmiddle" style="cursor: pointer;" class="nm3kTip" nm3kTip="@COMMON.delete@"  /></div>');
        $("#period_ctx").append($div);
        $div.find("img").click(function(){
            $div.remove();
            if( $("#nm3kTip").length == 1 ){
            	$("#nm3kTip").css({display:"none"});
            }
        });
        $div.find("select").each(function(){
            initPeriodSel($(this));
        });
        
        if(startTime&&endTime){
            $div.find(".sel_start").val($.trim(startTime));
            $div.find(".sel_end").val($.trim(endTime));
        }
    }
    
    function loadSections(disSetions){
        $("#period_ctx").children("div[class!=first]").remove();
        for(var i=0;i<disSetions.length;i++){
            var section=disSetions[i].split('-');
            var startTime=$.trim(section[0]);
            var endTime=$.trim(section[1]);
            if(i==0){
                $("#period_ctx").find(".sel_start").val(startTime);
                $("#period_ctx").find(".sel_end").val(endTime);
            }else{
                appendSection(startTime,endTime);
            }
        }
    }
    
    // -----------------------------------------------------------
$(function(){
    $("#btn_add_period").click(function(){
        appendSection();
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
    
    
    $("#policy_type").val(_policyType);
    if(_policyType=='3'){
        loadSections(_disSections);
        showPeriod(true);
    }
    loadPolicyTpls();
    R.btnOK.setDisabled( !operationDevicePower );
});

function okClick(){
    var policyType=$("#policy_type").val();
    var disSections=getDisSections();
    
    if(policyType==3&&disSections.length==0){
        window.top.showMessageDlg("@COMMON.tip@","@enterTimeSection@");
        return;
    }else{
        
    	if(!checkDisSectionsStartEndTime()){
			window.top.showMessageDlg("@COMMON.tip@","@beginTimeIslarger@");
    		return;
    	}else{
    		R.btnOK.setDisabled( true );
            $("#img_loading").show();
    		updateLoadBalPol(_entityId,_policyId,policyType,getDisSectionsToStr);	
    	}
    }
}
function closeWindow(){
    window.top.closeWindow('modifyLoadBalPolicyWin');    
}
</script>
</head>
<body class="openWinBody">
	<div class=formtip id=tips style="display: none"></div>
	<div class="openWinHeader">
		<div class="openWinTip">@loadbalance.mdfLoadBalancePolicy@</div>
		<div class="rightCirIco folderCirIco"></div>
	</div>
	<div class="edge10">
		<table class="mCenter zebraTableRows" >
		    <tr>
		        <td class="rightBlueTxt"  width="180">@tpl@:</td>
		        <td>
		        <select class="normalSel w220" id="policy_tpl">
		            <option value='-1'></option>
		        </select>
		        </td>
		    </tr>
		    <tr class="darkZebraTr">
		        <td class="rightBlueTxt">@policy@:</td>
		        <td>
		            <select class="normalSel w220" id="policy_type">
		                <option value="1">@policyEnabled@</option>
		                <option value="2">@policyDisabled@</option>
		                <option value="3">@disabedPeriod@</option>
		            </select>
		        </td>
		    </tr>
		    <tr id="period_tr" style="visibility:hidden;">
		        <td valign="top" class="rightBlueTxt withoutBorderBottom">
		        	<span class="goDownSpan">@timeSection@:</span>
		        </td>
		        <td class="withoutBorderBottom">
			        <div style="height:80px;overflow: auto;" id="period_ctx">
			            <div style="margin-bottom: 3px;" class="first">
				            <select class="sel_start normalSel w100"></select>&nbsp; - &nbsp;
				            <select class="sel_end normalSel w100"></select>&nbsp;
				            <img src="/images/add.png" align="absmiddle" style="cursor: pointer;" id="btn_add_period" class="nm3kTip" nm3kTip="@COMMON.add@" />
			            </div>
			        </div>
		        </td>
		    </tr>
		</table>
	</div>
	<Zeta:ButtonGroup>
		<Zeta:Button id="btnOK" onClick="okClick()" icon="miniIcoEdit">@COMMON.modify@</Zeta:Button>
		<Zeta:Button onClick="closeWindow()" icon="miniIcoForbid" >@COMMON.cancel@</Zeta:Button>
	</Zeta:ButtonGroup>
</body>
</Zeta:HTML>
