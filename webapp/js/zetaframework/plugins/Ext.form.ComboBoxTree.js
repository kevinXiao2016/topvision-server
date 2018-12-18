/**
 * Ext.form.ComboBoxTree
 *
 * (C) Topvision, 2013-5-24
 *
 * authro: Bravin
 */
Ext.form.ComboBoxTree = Ext.extend(Ext.form.ComboBox, {
    store: new Ext.data.SimpleStore({ fields: [], data: [[]] }),
    editable: false,
    shadow: false,
    mode: 'local',
    triggerAction: 'all',
    selectedClass: '',
    onSelect: null,
    canCollapse: true,
    constructor: function (_cfg) {
        if (_cfg == null) {
            _cfg = {};
        }
        Ext.apply(this, _cfg);
        this.treerenderid = Ext.id();
        this.tpl = String.format('<tpl for="."><div style="height:200px"><div id="ext-combobox-tree{0}"></div></div></tpl>', this.treerenderid);
        Ext.form.ComboBoxTree.superclass.constructor.apply(this, arguments);
        if (this.tree) {
            var cmb = this;
            this.tree.on('click', function (node) {
                cmb.canCollapse = true;
                if (Ext.isFunction(cmb.onSelect)) {
                    cmb.onSelect(cmb, node);
                } else {
                    cmb.setValue(node.text);
                    if (cmb.hiddenField) {
                        cmb[cmb.hiddenField] = node.id;
                    }
                }
                cmb.collapse();
            });
            //以下事件，让combobox能正常关闭
            this.tree.on('expandnode', function () { cmb.canCollapse = true; });
            this.tree.on('beforeload', function () { cmb.canCollapse = false; });
            this.tree.on('beforeexpandnode', function () { cmb.canCollapse = false; });
            this.tree.on('beforecollapsenode', function () { cmb.canCollapse = false; });
        }
        this.on('expand', this.expandHandler, this);
        this.on('collapse', this.collapseHandler, this);
    },
    expandHandler: function expand() {
        this.canCollapse = true;
        if (this.tree) {
            this.tree.render('ext-combobox-tree' + this.treerenderid);
            this.canCollapse = true;
            this.tree.getRootNode().expand();

        }
    },
    collapseHandler: function collapse() {
        if (!this.canCollapse) {
            this.expand();
        }
    }

});
Ext.reg('combotree', Ext.form.ComboBoxTree);