function createDialog(id,title,width,height,url){
	var win = new Ext.Window({id: id, title: title, width: width, height: height,
		border :false,
		bodyBorder  :false,
		modal: true, closable:   true ,
		plain:true, resizable: false, stateful: false, 
		html: '<iframe width=100% height=100% frameborder=0 src="' + url + '"></iframe>'});
	win.show();
}

function showMessageDlg(title, msg, type,fn) {
	var icon = (type == "error" ? Ext.MessageBox.ERROR : (type == "question" ? Ext.MessageBox.QUESTION : Ext.MessageBox.INFO));
	Ext.MessageBox.show({title: title, msg: msg, buttons: Ext.MessageBox.OK, icon: icon,fn:fn});
}

function showIptext(){
	createDialog("iptext","@demo.ipInput@  -  references/demo_ip.jsp",700,300,"references/demo_ip.jsp");
	
}
function showUpload(){
	createDialog("upload","@demo.fileUpload@  - references/demo_upload.jsp",600,370,"references/demo_upload.jsp");
}
function showPassword(){
	createDialog("password","@demo.passIpt@  - references/demo_password.jsp",700,300,"references/demo_password.jsp");
}
function showExceptionHnd(){
	createDialog("password","@demo.exp@  - references/demo_exception.jsp",600,370,"references/demo_exception.jsp");
}
/***********************************************
				弹出框demo示例
***********************************************/
function showPopNewWindow(){
	window.parent.createDialog("popNewWindow", '@demo.newLink@',  600, 370, "references/demo_popWindow.jsp", null, true, true);
}
function showValidate(){
	window.parent.createDialog("popNewWindow", '@demo.clientValidate@ - references/demo_validate.jsp',  600, 700, "references/demo_validate.jsp", null, true, true);
}
function showStep() {
	window.parent.createDialog("modalDlg", '@report/report.createReportTask@', 660, 510, "/references/demo_step.jsp", null, true, true);
}
function showGridPanel(){
	location.href = ("/references/demo_gridPanel.jsp");
}

function showGridPanel2(){
	location.href = ("/references/demo_gridPanel2.jsp");
}
function showTablePanel(){
	location.href = ("/references/demo_tabTable.jsp");
}
