/**
 * 
 * 版本自适应菜单，支持通过版本说明JSON对象描述内容来动态显示菜单项
 * 
 *      说明：
 *      1.JSON数据的格式：{function1：true, function2:false, function3:{subfunction1: "value1", subfunction2: ""}}
 *      2.功能名称、子功能名称需要与Menu中的menuItem的ID相同，对于menu中存在的Item但是在JSON数据中不存在对应名称或子功能名称
 *      的项，则不对该Item做版本自适应，相反的，如果在JSON中存在Function但是不在menu中存在对应Item项的也不进行版本自适应。
 *      3.在创建Menu时，填的Menu的Item的配置需要包含所用功能项的Item
 * 
 * Created by huangdongsheng @date 2013/10/25
 * 
 */
(function (window, Ext){
    var AutoFunMenu = function (options){
        /**
         * Ext.menu.Menu对象，使用options进行创建，options与Menu的配置项一致
         */
        this.menu = new Ext.menu.Menu(options);
    }
    /**
     * 私有方法，根据funcObj来改变Menu显示的内容
     * funcObj: 上面说明中提到的JSON对象
     * callback： 通过JSON对象中子对象的参数值来判断功能是否支持，
     *              方法如：function checkParamSupport(paramValue){return false}
     */
    AutoFunMenu.prototype._changeShowFunction = function(funcObj, callback){
        if(!callback || !(callback instanceof Function)){
            callback = function (val){
                return val == true || val == "true";
            }
        }
        
        versionControlCheck(this)
        
        return
        
        for(var o in funcObj){
            var cmp = this.findById(o);
            if(!cmp){
                continue;
            }
            var f = funcObj[o];
            if(f == true){
                cmp.show();
            }else if(typeof f == "object"){
                cmp.show();
                if(!cmp.menu){
                    continue;
                }
                for(var sub in f){
                    var subCmp = this.findById(sub);
                    if(!subCmp){
                        continue;
                    }
                    if(callback(f[sub])){
                        subCmp.show();
                    }else{
                        subCmp.hide();
                    }
                }
            }else{
                cmp.hide();
            }
        }
        
        function versionControlCheck(curMenu) {
        	// 如果当前菜单需要检查版本控制，并且发现不支持，则无需继续其子菜单检测（如果有子菜单）
        	var vcid = curMenu.vcid;
        	if(vcid) {
        		var support = top.VersionControl.support(vcid, funcObj);
        		if(!support) {
        			curMenu.hide();
        		}
        	}
        	
        	if(curMenu.menu) {
        		curMenu.menu.items.each(function(item) {
        			versionControlCheck(item)
                })
        	} 
        }
    }
    
    /**
     * 通过ID查找MenuItem
     */
    AutoFunMenu.prototype.findById = function (id){
        return this._findById(this.menu, id);
    }
    
    /**
     * 通过ID，递归查找menuItem
     */
    AutoFunMenu.prototype._findById= function (menu, id){
        var cmp = menu.findById(id);
        if(cmp){
            return cmp;
        }
        var subMenus = menu.findByType("menuitem");
        if(!subMenus){
            return null;
        }
        for(var i = 0; i < subMenus.length; i ++){
            var subMenu = subMenus[i].menu
            if(!subMenu){
                continue;
            }
            cmp = this._findById(subMenu, id);
            if(cmp){
                break;
            }
        }
        return cmp;
    }
    /**
     * 将menu显示到point指定的位置
     * point： 数组，存放位置的X值和Y值, [X, Y]
     * funcObj: 上面说明中提到的JSON对象
     * callback： 通过JSON对象中子对象的参数值来判断功能是否支持，
     *              方法如：function checkParamSupport(paramValue){return false}
     */
    AutoFunMenu.prototype.show = function (point, funcObj, callback){
    	this.menu.showAt(point);
        if(funcObj){
            this._changeShowFunction(funcObj, callback);
        }
    }
    window.AutoFunMenu = AutoFunMenu;
})(window, Ext);