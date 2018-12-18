/**
 * 模拟<input type="file" />
 * Author:dengl
 */
(function($){
    var DEFAULT_OPTS={
            "name":"", //type="file"控件的name属性值
            "tiptext":"",   //提示信息
            "width":150,    //text文本框宽度
            "btnclass":"BUTTON75",
            "btntext":"Browse...",
            "checkfn":function(filePath,name){//文件检查
                return true;
            }
    };
    
    /**
     * 初始化
     * @param $t
     * @param opts
     * @returns
     */
    function init($t,opts){
        var _fakeUpload=$t.data('fakeUpload');
        var _w=parseInt(opts.width);
        if(!_fakeUpload){
            $t.css({"position":"relative","display":"inline-block","top":0,"left":0});
            $t.append('<input class="normalInputDisabled floatLeft" type="text" style="width:'+_w+'px;color: #BBBBBB;" disabled="disabled" value="'+opts.tiptext+'"/>'
                    +'<a href="javascript:;" class="nearInputBtn fakeUploadBtn75"><span style="width:40px;">'+ opts.btntext +'</span></a>'
                    +'<input type="file" name="'+opts.name+'" size="1" style="width:60px; height:30px;position:absolute;top:0;right:0px;z-index:2;opacity:0.01;filter:alpha(opacity:1);" />'
                    );
                    //+'<span style="background-color:black;z-index:3;position:absolute;left:0px;display:inline-block;opacity:0;filter:alpha(opacity:0);">&nbsp;</span>');
           /* $t.find("span").css({
                width:_w+10,
                height:23
            });*/
            $(".fakeUploadBtn75").bind("click",function(){
            	//$(this).next().click();
            })
            
            _bindAction($t,opts);
            
            // 为html标签绑定一些数据
            $t.data('fakeUpload',{opts:opts});
        }
    }
    
    /**
     * 获取文件路径
     */
    function getPath($t){
        var _fakeUpload=$t.data('fakeUpload');
        if(_fakeUpload){
            var v=$t.find("input[type=text]").val();
            return v==_fakeUpload.opts.tiptext?"":v;
        }
        
        return "";
    }
    
    /**
     * 绑定事件
     * @param $t
     * @param opts
     * @returns
     */
    function _bindAction($t,opts){
        var $file=$t.find("input[type=file]");
        var $txt=$t.find("input[type=text]");
        var $btn=$t.find("input[type=button]");
        
        $file.unbind(".fakeUpload").bind("change.fakeUpload",function(){
            if(opts.checkfn(this.value,$(this).attr("name"))){
                if(!this.value){
                    $txt.css({"color":"#BBBBBB"});
                    $txt.val(opts.tiptext);
                }else{
                    $txt.css({"color":""});
                    $txt.val(this.value);
                }
            }
        }).bind("mouseenter.fakeUpload",function(){
            $btn.addClass("BUTTON_OVER75");
        }).bind("mouseleave.fakeUpload",function(){
            $btn.removeClass("BUTTON_OVER75");
        }).bind("mousedown.fakeUpload",function(){
            $btn.addClass("BUTTON_PRESSED75");
        }).bind("mouseup.fakeUpload",function(){
            $btn.removeClass("BUTTON_PRESSED75");
        });
    }
    
    $.fn.fakeUpload=function(m,opts){
        opts=opts||{};
        switch(m){
        case 'init':
            return this.each(function(){
                var $t=$(this);
                var _opts=$.extend({},DEFAULT_OPTS,opts,{
                    name:$t.attr("name"),
                    btnclass:$t.attr("btnclass"),
                    tiptext:$t.attr("tiptext"),
                    width:$t.attr("width"),
                    btntext:$t.attr("btntext")
                });
                $t.removeAttr("name").removeAttr("btnclass").removeAttr("tiptext").removeAttr("width").removeAttr("btntext");
                init($t,_opts);
            });
        case 'getPath':
            return getPath(this);
        }
    };
})(jQuery);