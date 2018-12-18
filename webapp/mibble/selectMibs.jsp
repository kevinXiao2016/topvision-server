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
    module platform
</Zeta:Loader>
<style type="text/css">
#sequenceStep {padding-left: 405px;position: absolute;top: 330px;}
#mibs {width: 580px;height : 210px; overflow-y:scroll;}
button {display : in-line;}
#mibsTable td,#mibsTable tr {height:20px;}
</style>
<script type="text/javascript">
 	var mibList = ${mibArray}
 	var mibSelectedList = ${mibSelectedList}
 	//var cellTemplate = "<span><input class=mibItem type=checkbox value='{0}' {1}/></span><span> {0}</span>";
 	var cellTemplate = "<table><tr><td><input class=mibItem type=checkbox value='{0}' {1}/></td><td>{0}</td></tr></table>";
 	function resize(){
		$("#mibs").offset().top
		$("#mibs").offset().left
 	}

 	$(document).ready(function(){
 		var table = DOC.getElementById("mibsTable");
 	    var flag = 0;
 	    var tr;
 	    while(mibList.length>0){
 			if(flag % 3 == 0){
 				tr = table.insertRow(table.rows.length);
 				tr.style.valign='left';
 				tr.style.height ='20px';
 				//左边间隔
 				tr.insertCell(0).style.width = '2%';
 				flag = 0;
 			}
 		    var td = tr.insertCell(flag+1);
 		    var o = mibList.pop();
 		    if(mibSelectedList.length > 0 && mibSelectedList.toString().indexOf(o) != -1)
 		    	td.innerHTML = String.format(cellTemplate,o,"checked")
			else
				td.innerHTML = String.format(cellTemplate,o,"")
 		    flag++;
 	    }
 	});

 	function save(){
 		var mibList = new Array();
 		var checkbox = $(".mibItem:checked");
 		for(var x=0; x < checkbox.length; x++){
 			mibList.push( checkbox[x].value );
 		}
 		if(mibList.length > 4){
 			return window.parent.showMessageDlg(I18N.mibble.tip, I18N.mibble.plsNotOver4 );
 		}
 		$.ajax({
			url: '/mibble/saveSelectedMib.tv',data:{selectedMibs : mibList.join(",")},
			cache:false,
			success:function(){
					top.afterSaveOrDelete({
	       				title: "@COMMON.tip@",
	       				html: '<b class="orangeTxt">@mibble.operateOk@</b>'
	       			});
					var parentId = top.contentPanel.getActiveTab().id;
					try{					
						window.parent.getFrame(parentId).reloadMib();//有可能是cm Mib 也有可能是Mib
					}catch(err){}
					cancelClick()
			},error:function(){
				window.parent.showMessageDlg(I18N.mibble.tip, I18N.mibble.operateEr );
			}
 	 	})
 	}

 	function cancelClick(){
 		window.parent.closeWindow('modalDlg');
 	}
</script>
</head>
	<body class=openWinBody>
		<div class=formtip id=tips style="display: none"></div>
		<div class="openWinHeader">
			<div class="openWinTip">@mibble.mibList@</div>
			<div class="rightCirIco folderCirIco"></div>
		</div>
		<div class="edge10">
			<table class="mCenter">
				<tr>
					<td>
						<div id=mibs>
							<table id=mibsTable></table>
						</div>
					</td>
				</tr>
			</table>
		</div>
		<Zeta:ButtonGroup>
			<Zeta:Button id="nextStep" onClick="save()" icon="miniIcoData">@BUTTON.save@</Zeta:Button>
			<Zeta:Button onClick="cancelClick()" icon="miniIcoForbid">@BUTTON.cancel@</Zeta:Button>
		</Zeta:ButtonGroup>
	</body>
</Zeta:HTML>
