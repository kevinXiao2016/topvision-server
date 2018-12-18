(function(){
	function _listen_(){
		//modify by @bravin: 将事件注册改为直接绑定，避免事件处理器被多次绑定到同一个元素上
		//$(".zeta-password-field .zeta-password-icon").click(function(event){
		$(".zeta-password-field .nearInputBtn").click(function(event){
			var el = this.parentNode;
			var random = Math.random();
			var id = el.fieldId;
			var name = el.fieldName;
			
			
			el.inputType = el.inputType == 'text' ? 'password' : 'text';
			
			//不支持chrome,所以注释掉了这里，重写了一下;
			/*var value = $(el).find("input[type="+el.inputType+"]").hide().attr({"id":random},{"name":random}).val();
			$(el).find("input[type="+el.inputType+"]").show().attr({"id":id},{"name":name}).val(value);*/
			
			
			var $pw = $(el).find("input[type='password']");
			var $text = $(el).find("input[type='text']");
			var idAndName = new Object();
			switch(el.inputType){
				case "text":
					idAndName.id = $pw.attr("id");
					idAndName.name = ($pw.attr("name") != undefined) ? $pw.attr("name") : random;
					idAndName.value = $pw.val(); 
					var $disabled = $pw.attr("disabled");
					$pw.css("display","none").removeAttr("id").removeAttr("name");
					$text.css("display","block").attr("id",idAndName.id).attr("name",idAndName.name).val(idAndName.value).attr("disabled",$disabled);;
					$(this).find("i").attr("class","eyeOpenIco");
					break;
				case "password":
					idAndName.id = $text.attr("id");
					idAndName.name = ($text.attr("name") != undefined) ? $text.attr("name") : random;	
					idAndName.value = $text.val();
					var $disabled = $text.attr("disabled");
					$text.css("display","none").removeAttr("id").removeAttr("name");
					$pw.css("display","block").attr("id",idAndName.id).attr("name",idAndName.name).val(idAndName.value).attr("disabled",$disabled);
					$(this).find("i").attr("class","eyeCloseIco");
					break;
			
			}
			//this.src = "/js/zetaframework/passwordField/" + el.inputType + ".png";
		});
	}
	if(Ext.isIE){		
		window.attachEvent("onload",_listen_);
	}else{
		window.onload = _listen_;	
		//window.addEventListener("onload",_listen_);//不知道为什么chrome监听不到这里;所以改为window.onload;
	};
})();
