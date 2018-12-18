/*!
 * Ext JS Library 3.0.3
 * Copyright(c) 2006-2009 Ext JS, LLC
 * licensing@extjs.com
 * http://www.extjs.com/license
 */
/**
 * @class Ext.ux.TabCloseMenu
 * @extends Object 
 * Plugin (ptype = 'tabclosemenu') for adding a close context menu to tabs.
 * 
 * @ptype tabclosemenu
 */
var closeManyTabInformed = true;
Ext.ux.TabCloseMenu = function(){
	closeTabText = Extux.TabCloseMenu.closeTabText;
	closeOtherTabText =  Extux.TabCloseMenu.closeOtherTabText;
	closeAllTabText =  Extux.TabCloseMenu.closeAllTabText;
	confirmText =  Extux.TabCloseMenu.confirmText;
	closeAllMsg =  Extux.TabCloseMenu.closeAllMsg;
	closeOtherMsg =  Extux.TabCloseMenu.closeOtherMsg;
    var tabs, menu, ctxItem;
    this.init = function(tp){
        tabs = tp;
        tabs.on('contextmenu', onContextMenu);
    };

    function onContextMenu(ts, item, e){
        if(!menu){ // create context menu on first right click
            menu = new Ext.menu.Menu({            
            items: [{
                id: tabs.id + '-close',
                text: closeTabText,
                handler : function(){
                    tabs.remove(ctxItem);
                }
            },{
                id: tabs.id + '-close-others',
                text: closeOtherTabText,
                handler : function(){
					if (closeManyTabInformed && tabs.items.length > 2) {
						Ext.MessageBox.confirm(confirmText, closeOtherMsg, function(type) {
							if (type == 'yes') {
			                    tabs.items.each(function(item){
			                        if(item.closable && item != ctxItem){
			                            tabs.remove(item);
			                        }
			                    });
							}
						});
					} else {
	                    tabs.items.each(function(item){
	                        if(item.closable && item != ctxItem){
	                            tabs.remove(item);
	                        }
	                    });
					}
                }
            },{
                id: tabs.id + '-close-all',
                text: closeAllTabText,
                handler : function(){
					if (closeManyTabInformed && tabs.items.length > 2) {
						Ext.MessageBox.confirm(confirmText, closeAllMsg, function(type) {
							if (type == 'yes') {
			                    tabs.items.each(function(item){
			                        if(item.closable){
			                            tabs.remove(item);
			                        }
			                    });
							}
						});
					} else {
	                    tabs.items.each(function(item){
	                        if(item.closable){
	                            tabs.remove(item);
	                        }
	                    });
					}
                }
            }]});
        }
        ctxItem = item;
        var items = menu.items;
        items.get(tabs.id + '-close').setDisabled(!item.closable);
        var disableOthers = true;
        tabs.items.each(function(){
            if(this != item && this.closable){
                disableOthers = false;
                return false;
            }
        });
        items.get(tabs.id + '-close-others').setDisabled(disableOthers);
        items.get(tabs.id + '-close-all').setDisabled(disableOthers);
	e.stopEvent();
        menu.showAt(e.getPoint());
    }
};

Ext.preg('tabclosemenu', Ext.ux.TabCloseMenu);
