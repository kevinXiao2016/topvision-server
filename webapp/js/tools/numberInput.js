/*  
 * 数字调节输入框类(NumberInput) 
 *   
 * @param {string}         id           生成的input输入框id与name 
 * @param {string|object}  parentEl     父标签 
 * @param {string}         defaultNum   默认数字 
 * @param {string}         minNum       最小数字 
 * @param {object}         maxNum       最大数字  
 */  
var NumberInput = function(id, parentEl, defaultNum, minNum, maxNum){  
    this.id = id;  
    this.parentObj = typeof parentEl == "string" ? document.getElementById(parentEl) : parentEl;  
    this.defaultNum = defaultNum;  
    this.minNum = minNum;  
    this.maxNum = maxNum;  
    this.create();  
}  
  
NumberInput.prototype = {  
    auto : false,  
    autoT : null,  
    target : null,  
    create : function(){  
        var div = document.createElement("div");  
        div.className = "number_input";  
        this.parentObj.appendChild(div);  
        var that = this;  
        var input = document.createElement("input");  
        div.appendChild(input);  
        input.type = "text";  
        input.id = input.name = this.id;  
        input.value = this.defaultNum;  
        this.addEvent(input, "keydown", function(e){that.changeNum(e,input)});  
        this.addEvent(input, "keyup", function(e){that.checkNum(e,input)});
        this.target = input;  
        var span = document.createElement("span");  
        var sup = document.createElement("sup");  
        var sub = document.createElement("sub"); 
        div.appendChild(span);
        span.appendChild(sup);
        span.appendChild(sub);
        this.addEvent(sup, "mouseover", function(){this.className = 'on';});
        this.addEvent(sup, "mouseout", function(){this.className = '';});
        this.addEvent(sup, "mousedown", function(){that.autoPlus();});
        this.addEvent(sub, "mouseover", function(){this.className = 'on';});  
        this.addEvent(sub, "mouseout", function(){this.className = '';});
        this.addEvent(sub, "mousedown", function(){that.autoMinus();}); 
        this.addEvent(document, "mouseup", function(){  
            if(that.auto){  
                clearInterval(that.autoT);  
                this.auto = false;  
            }  
        });  
    },  
    changeNum : function(e,el){
        var e = e || window.event;  
        var code = e.which ? e.which : e.keyCode;
        if(code < 48 || code > 57){  
            if(code == 38){  
                this.plusNum();  
            }else if(code == 40){  
                this.minusNum();  
            }else if(code == 13){  
                this.enterNum();  
            }else if(code == 8 || code == 37 || code == 39 || (code >= 96 && code <= 105) || code == 9){  
                  
            }else{  
                window.event ? e.returnValue = false : e.preventDefault();  
                return;  
            }  
        }else{
        	if(el.value==0){
        		el.value="";
        	}
        }
    },
    checkNum : function(e,el){
    	if(el.value>this.maxNum){
    		el.value = this.maxNum
    	}else if(el.value < this.minNum){
    		el.value = this.minNum
    	}
    },
    plusNum : function(){  
        var num = parseInt(this.target.value);  
        if(!/\d+/.test(num)){  
            num = this.minNum - 1;  
        }  
        if(num >= this.maxNum){  
            return;  
        }  
        num++;  
        this.target.value = num;  
        this.target.focus();  
    },  
    minusNum : function(){  
        var num = parseInt(this.target.value);  
        if(!/\d+/.test(num)){  
            num = this.maxNum + 1;  
        }  
        if(num <= this.minNum){  
            return;  
        }  
        num--;  
        this.target.value = num;  
        this.target.focus();  
    },  
    enterNum : function(){  
        var num = parseInt(this.target.value);  
        if(!/\d+/.test(num) || num < this.minNum || num > this.maxNum){  
            num = this.minNum;  
        }  
        this.target.value = num;  
        this.func();  
    },  
    autoPlus : function(){  
        var that = this;  
        clearInterval(this.autoT);  
        this.autoT = setInterval(function(){that.plusNum()}, 50);  
        this.auto = true;  
    },  
    autoMinus : function(){  
        var that = this;  
        clearInterval(this.autoT);  
        this.autoT = setInterval(function(){that.minusNum()}, 50);  
        this.auto = true;  
    },  
    addEvent : function(obj, ev, handler){  
        if(window.attachEvent){  
            obj.attachEvent("on" + ev, handler);  
        }else if(window.addEventListener){   
            obj.addEventListener(ev, handler, false);  
        }  
    }  
}  
