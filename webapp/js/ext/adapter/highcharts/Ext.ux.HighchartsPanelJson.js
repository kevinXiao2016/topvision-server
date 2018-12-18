/**
* See http://www.extjs.com/forum/showthread.php?t=93669
*/
Ext.ns('Ext.ux');

Ext.ux.HighchartPanelJson = Ext.extend(Ext.Panel, {

    /**
     * Chart object
     */
    //chart: null,

    initComponent: function() {
        // mask the chart (unmask is done with a defer inside renderChart)
        this.on('afterlayout', this.showLoading, this);
        // render the chart
        this.on('afterlayout', this.renderChart, this);

        /**
         * Use a JSON Store for hold chart series data instead of using the
         * highcharts addSeries and remove methods
         *
         * NOTE: to use this you _must_ have id defined for each series
         *
         * to add a new series use the store.add() method. 
         * remember u need to create a new record and the arg to add is an array
         * EG
         *  var rec = new store.record({id:'test',name:'test',data:[1,2,3,4,5,6]});
         *  store.add([rec]);//add an array of records to store
         *
         * The 'add' listener in the store will then add the series to the chart for you
         * Same to remove a line, use the store.remove() method and the listener in the 
         * store will remove the line from the chart
         */
        this.store = new Ext.data.JsonStore({
            record: new Ext.data.Record.create(['id','name','color','selected','visible','type','options','xAxis','yAxis','data'])
            ,fields: [
                {name: 'id', type: 'string'}
                ,{name: 'name', type: 'string'}
                ,{name: 'color', type: 'string'}
                ,{name: 'selected', type: 'boolean'}
                ,{name: 'visible', type: 'boolean'}
                ,{name: 'type', type: 'string'}
                ,{name: 'options'} // config object
                ,{name: 'xAxis'} // config object
                ,{name: 'yAxis'} // config object
                ,{name: 'data'} // array
            ]
            ,listeners: {
                /**
                 * Remove the series from the chart if it exists
                 */
                remove: function(s,r,i) {
                    // remove series from grpah when removed from store
                    if (undefined != this.chart) {
                        this.chart.get(r.get('id')).remove();
                    }
                }
                /**
                 * Add the series to the chart if it does not exist
                 */
                ,add: function(s,rows,o) {
                    var i, l;
                    if (undefined != this.chart) {
                        for (i=0,l=rows.length;i<l;i++) {
                            var r = rows[i];
                            if (this.chart.get(r.get('id')) == null) {
                                var rObj = {};
                                if (r.get('id') != '') {
                                    rObj.id = r.get('id');
                                } else {
                                    rObj.id = '';
                                }
                                if (r.get('name') != '') {
                                    rObj.name = r.get('name');
                                } else {
                                    rObj.name = '';
                                }
                                if (r.get('data').length > 0) {
                                    rObj.data = r.get('data');
                                } else {
                                    rObj = [];
                                }
                                if (r.get('color') != '') {
                                    rObj.color = r.get('color');
                                }
                                if (typeof r.get('selected') == 'boolean') {
                                    rObj.selected = r.get('selected');
                                }
                                if (typeof r.get('visible') == 'boolean') {
                                    rObj.visible = r.get('visible');
                                }
                                if (r.get('type') != '') {
                                    rObj.type = r.get('type');
                                }
                                if (typeof r.get('options') == 'object') {
                                    rObj.options = r.get('options');
                                }
                                if (typeof r.get('xAxis') == 'object') {
                                    rObj.xAxis = r.get('xAxis');
                                }
                                if (typeof r.get('yAxis') == 'object') {
                                    rObj.yAxis = r.get('yAxis');
                                }
                                this.chart.addSeries(rObj);
                            }
                        }
                    }
                }
                ,scope: this
            }
        });
        /**
         * Copy pre-existing series into the local store
         */
        this.copySeries = function (isInitial) {
            var series = [];
            if (this.store.getCount() > 0) {
                // remove old records
                this.store.removeAll();
                this.store.commitChanges();
            }
            if (! isInitial) {
                // add records from existing chart
                series = this.chart.series;
            } else {
                // add records from initial config
                series = this.chartConfig.series;
            }
            // loop through series and apply that data to the store
            if  (series == null) {
                series = [];
            }
            for (var i = 0, len = series.length; i < len; i++) {
                var rdata = [], s = series[i];
                for (var c = 0, dlen = s.data.length; c < dlen; c++) {
                    rdata.push([s.data[c].x,s.data[c].y]);
                }
                var rec = {id:'',name:s.name,data:rdata};
                if (s.id !== undefined) {
                    rec.id = s.id;
                }
                if (s.options  !== undefined && s.options.id !== undefined) {
                    rec.id = s.options.id;
                }
                if (s.options  !== undefined && typeof s.options == 'object') {
                    Ext.apply(rec.options,s.options);
                }
                if (s.color    !== undefined && s.color != '') {
                    rec.color = s.color;
                }
                if (s.selected !== undefined) {
                    rec.selected = (s.selected) ? true:false;
                }
                if (s.type     !== undefined && s.type != '') {
                    rec.type  = s.type;
                }
                if (s.visible  !== undefined) {
                    rec.visible = (s.visible) ? true:false;
                }
                if (s.xAxis    !== undefined && typeof s.xAxis == 'object') {
                    Ext.apply(rec.xAxis, s.xAxis);
                }
                if (s.yAxis    !== undefined && typeof s.yAxis == 'object') {
                    Ext.apply(rec.yAxis,s.yAxis);
                }
                this.store.loadData(rec,true);
            }
            this.store.commitChanges();
        };
        /**
         * Load series from store into array for chart constructor
         **/
        this.getAllSeries = function() {
            var series = [];
            if (this.store.getCount() > 0) {
                this.store.each(function(r){
                    var rObj = {};
                    if (r.get('id') != '') {
                        rObj.id = r.get('id');
                    } else {
                        rObj.id = '';
                    }
                    if (r.get('name') != '') {
                        rObj.name = r.get('name');
                    } else {
                        rObj.name = '';
                    }
                    if (r.get('data').length > 0) {
                        rObj.data = r.get('data');
                    } else {
                        rObj = [];
                    }
                    if (r.get('color') != '') {
                        rObj.color = r.get('color');
                    }
                    if (typeof r.get('selected') == 'boolean') {
                        rObj.selected = r.get('selected');
                    }
                    if (typeof r.get('visible') == 'boolean') {
                        rObj.visible = r.get('visible');
                    }
                    if (r.get('type') != '') {
                        rObj.type = r.get('type');
                    }
                    if (typeof r.get('options') == 'object') {
                        rObj.options = r.get('options');
                    }
                    if (typeof r.get('xAxis') == 'object') {
                        rObj.xAxis = r.get('xAxis');
                    }
                    if (typeof r.get('yAxis') == 'object') {
                        rObj.yAxis = r.get('yAxis');
                    }
                    this.push(rObj);
                },series);
            }
            return series;
        };

        this.showLoading = function() {
            //this.getEl().mask('Building graph...','x-mask-loading');
            return null;
        };
        this.hideLoading = function() {
            //this.getEl().unmask();
            return null;
        };

        Ext.ux.HighchartPanelJson.superclass.initComponent.call(this);
    }// eo initComponent
    ,showLoading: function() {
        //this.getEl().mask('Building graph...','x-mask-loading');
        return null;
    }//eo showLoading
    ,hideLoading: function() {
        //this.getEl().unmask();
        return null;
    }//eo hideLoading
    /**
     * Renders the chart
     */
    ,renderChart: function() {
        /**
         * Create a somewhat limited copy of existing series data
         * for re-applying after re-rendering
         */
//    	debugger;
        if (undefined != this.chart) {
        	/*
        	    * 当这个组件作为portal的一个panel，进行拖动的时候，直接返回，不再销毁以前的组件
        	    * 目前不报错，拖动功能正常，不知道是否有其他影响，modify by lzs at 2013.11.05
        	    * 补充：当拖动时，portal的源列、拖动对象 都不会触发这个方法，但是拖动目标列的组件会循环触发afterlayout事件，从而触发这个方法
        	    * 所以，要求Portal的所有列宽都相等，不然拖动后不重新渲染，会导致显示不完整
        	    */
        	return;
            this.copySeries(false);
        } else {
            // initial chart construction so we need series data
            // from this.chartConfig
            this.copySeries(true);
          
        }

        /**
         * This will recreate the chart when the layout has changed.
         * The highchart lib should have functions like resize(w,h).
         * since it doesnt have these functions yet, we have to do it this
         * dirty way.
         */
        this.removeChart();

        var currConfig = this.chartConfig;

        /**
         * Add series which exist in store to the Chart Config Series
         */
        currConfig.series = this.getAllSeries();

        Ext.apply(currConfig.chart, {
            renderTo: this.body.dom
            // To add event to the chart place them in the chart config section
            //,events: {
            //    load: function() {
            //        //scope is the chart
            //    }
            //}
        });

        this.chart = new Highcharts.Chart(currConfig);

        /**
         * Hide the loading/building mask
         */
        (function(){this.hideLoading();}).defer(1000,this);
    }// eo renderChart

    ,removeChart: function() {
        if (this.chart) {
            this.chart.remove();
            this.chart = null;
        }
    }
    /*
    destroy: function() {
        if(this.chart) {
            this.chart.options.chart.renderTo = null;
            delete this.chart;
        }

        Ext.ux.HighchartPanel.superclass.destroy.call(this);
    }
     */
});

Highcharts.Chart.prototype.remove = function () {

    /**
     * Clear certain attributes from the element
     * @param {Object} d
     */
    function purge(d) {
        var a = d.attributes, i, l, n;
        if (a) {
            l = a.length-1;
            for (i = l; i >= 0; i -= 1) {
                n = a[i].name;
                //if (typeof d[n] !== ‘object’) {
                if (n == 'coords') {
                    //d.parentNode.removeChild(d);
                    d[n] = '0,0,0,0';
                } else if (typeof d[n] != 'object') {
                    d[n] = null;
                }
            }
        }
        a = d.childNodes;
        if (a) {
            l = a.length;
            for (i = 0; i < l; i += 1) {
                purge(d.childNodes[i]);

            }
        }

    }

    // get the container element
//    var container = this.imagemap.parentNode;
   /*
    * 原来是上一行代码，但是当这个组件作为portal的一个panel，进行拖动的时候，上一行代码报错，所以修改成下一行
    * 目前不报错，拖动功能正常，不知道是否有其他影响，modify by lzs at 2013.11.05
    */
    var container = this.container;

    // purge potential leaking attributes
    purge(container);

    // remove the HTML
    container.innerHTML = '';
};

Ext.reg('highchartpaneljson', Ext.ux.HighchartPanelJson);