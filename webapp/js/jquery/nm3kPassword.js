/*		var pass1 = new nm3kPassword({
			id : "pass1",
			renderTo : "test1",
			toolTip : "提示信息",
			width : 200,
			value : 'abc',
			firstShowPassword : true,
			disabled : true,
			maxlength : 16
		})
		pass1.init();*/


	//密码框切换明码;
	function nm3kPassword(obj){
		this.id = obj.id;
		this.name = obj.id;//id和name是一样的;		
		this.renderTo = obj.renderTo;		
		this.firstShowPassword = false;
		if(obj.toolTip){
			this.toolTip = obj.toolTip;
		}
		if(obj.width){
			this.width = obj.width;
		}
		if(obj.firstShowPassword && obj.firstShowPassword == true){
			this.firstShowPassword = true;
		}			
		if(obj.value){
			this.value = obj.value;
		}		
		if(obj.disabled && obj.disabled == true){
			this.disabled = obj.disabled;
		}
		if(obj.maxlength){
			this.maxlength = obj.maxlength;
		}
	};
	nm3kPassword.prototype.constructor = "nm3kPassword";
	nm3kPassword.prototype.init = function(){
		var str = '';
		str += '<div>';
		str +=    '<input type="text" class="normalInput floatLeft" style="ime-mode:disabled" onpaste="return false"/>';
		str +=	  '<input type="password" class="normalInput floatLeft" style="display:none" />';
		str +=	  '<a href="javascript:;" class="nearInputBtn"><span><i class="eyeOpenIco"></i></span></a>';
		str +=    '<div style="clear:both; width:1px; height:0px; overflow:hidden;"></div>'
		str += '</div>';		
		var $outer = $("#"+this.renderTo);
		$outer.html(str);
		var $text = $outer.find(":text");
		var $pass = $outer.find(":password")
		if(this.toolTip){
			$outer.find(":input").attr("toolTip",this.toolTip);	
		}
		if(this.width){
			$outer.find(":input").css("width",this.width)			
		}
		if(this.maxlength){
			$outer.find(":input").attr("maxlength",this.maxlength);
		}
		if(this.firstShowPassword){
			$pass.attr("id",this.id);
			$pass.attr("name",this.id).css("display","block");
			$text.css("display","none");
			$outer.find("a span i").removeClass("eyeOpenIco").addClass("eyeCloseIco");
		}else{			
			$text.attr("id",this.id);
			$text.attr("name",this.id);
		}
		if(this.value){
			$text.val(this.value);
			$pass.val(this.value);
		}
		if(this.disabled){
			$text.addClass("normalInputDisabled").attr("disabled",true);
			$pass.addClass("normalInputDisabled").attr("disabled",true);
		};
		$outer.find("a.nearInputBtn").bind("click",this,this.iClick);		
	};//end init;
	
	//点击切换;
	nm3kPassword.prototype.iClick = function(e){
		var iCls = $(this).find("i").attr("class");
		var $text = $(this).parent().find(":text");
		var $pass = $(this).parent().find(":password")
		var showInput; //现在是显示的input,将要把它变不可见;
		var hiddenInput; 
		switch(iCls){
			case "eyeOpenIco":					
				$(this).find("i").removeClass("eyeOpenIco").addClass("eyeCloseIco");
				showInput = $text;
				hiddenInput = $pass;
			break;
			case "eyeCloseIco":
				$(this).find("i").removeClass("eyeCloseIco").addClass("eyeOpenIco");
				showInput = $pass;
				hiddenInput = $text;
			break;
		};//end switch;
			
		var theId = showInput.attr("id");
		var theName = showInput.attr("name");					
		var theVal = showInput.val();
		
		showInput.removeAttr("id").removeAttr("name").css("display","none");
		hiddenInput.attr({"id":theId, "name":theName}).val(theVal).css("display","block");
		if(showInput.hasClass("normalInputRed")){
			showInput.removeClass("normalInputRed");
			hiddenInput.addClass("normalInputRed");
		};//end if;
	};//end iClick
	
	//设置disabled属性;
	nm3kPassword.prototype.setDisabled = function(para){
		var $outer = $("#"+this.renderTo);
		switch(para){
			case true:
				$outer.find(":input").attr("disabled",true);				
				$outer.find(":input").each(function(){
					if(!$(this).hasClass("normalInputDisabled")){
						$(this).addClass("normalInputDisabled");
					}
				})
				break;
			case false:
				$outer.find(":input").attr("disabled",false);
				$outer.find(":input").removeClass("normalInputDisabled");
				break;
				
		}
	}
	nm3kPassword.prototype.getValue = function(v){
		return $("#" + this.id).val();
	}
	nm3kPassword.prototype.setValue = function(v){
		$("#" + this.renderTo).find(":input").val(v);
	}
	nm3kPassword.prototype.setToolTip = function(tip){
		$("#" + this.renderTo).find(":input").attr('tooltip', tip);
	}
	nm3kPassword.prototype.setMaxlength = function(max){
		$("#" + this.renderTo).find(":input").attr('maxlength', max);
	}
	
	
	
	