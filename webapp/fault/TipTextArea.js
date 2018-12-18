/* 文本框限制文件;
**
**
**      var t = new TipTextArea({
**			id : "one",
**			tipsId : "tip1"
**		});
**		t.init();
**
*/

function TipTextArea(obj){
	this.id = obj.id;
	this.tipsId = obj.tipsId ? obj.tipsId : null;
	this.maxLength = (obj.maxLength) ? (obj.maxLength) : $("#"+ this.id).attr("maxLength");	
}
TipTextArea.prototype.init = function(){
	$("#"+ this.id).bind("keyup", this, this.keyUpFn);
}
TipTextArea.prototype.constructor = "TipTextArea";

TipTextArea.prototype.keyUpFn = function(e){
	var len = $(this).val().length;
	var maxLen = e.data.maxLength;
	if(len > maxLen){
		var textEareValue = $(this).val().substr(0,maxLen);
		$(this).val(textEareValue);
		len = maxLen;
	};
	
	if(e.data.tipsId != null){
		$("#" + e.data.tipsId).text(String.format(Nm3K.TipTextArea,maxLen,len));
	}
}