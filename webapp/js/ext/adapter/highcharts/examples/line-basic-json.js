Ext.onReady(function(){
    var globalInc = 999;
    var addLine = function(c) {
        c = c || globalInc;
        var store = Ext.getCmp('thechart').store;
        var add = {
            id: 'randomdata'+c,
            name: 'Random data'+c,
            data: (function() {
                // generate an array of random data
                var data = [], time = (new Date()).getTime(), i;
                for (i = -101; i <= 0; i++) {
                    data.push({
                        x: time + i * 1000,
                        y: Math.random()
                    });
                }
                return data;
            })()
        };
        var rec = new store.record(add);
        //Ext.getCmp('thechart').chart.addSeries(add,true);
        //store.loadData(add,true);
        store.add([rec]);
        store.commitChanges();
        globalInc++;
    };
    var graphWin = new Ext.Window({
        title: 'Resizeable Graph Window [resize redraws graph using stored data]',
        resizeable: true,
        width: 800,
        height: 450,
        items: [
            {
                xtype: 'button',
                text: 'Add line',
                handler: function() {
                    addLine();
                }
            },
            {
                xtype: 'button',
                text: 'Remove 1st line',
                handler: function() {
                    var store = Ext.getCmp('thechart').store;
                    if (store.getCount() > 0) {
                        store.removeAt(0);
                        store.commitChanges();
                    }
                }
            },
            new Ext.ux.HighchartPanelJson({
                titleCollapse: true,
                layout:'fit',
                border: true,
                id: 'thechart',
                chartConfig: {
                    chart: {
                        id: 'thechart',
                        defaultSeriesType: 'line',
                        margin: [50, 150, 60, 80]
                    },
                    title: {
                        text: 'Random generated data',
                        style: {
                            margin: '10px 100px 0 0' // center it
                        }
                    },
                    xAxis: {
                        type: 'datetime'
                        ,title: {
                            text: 'Month'
                        }
                    },
                    yAxis: {
                        title: {
                            text: 'Random number'
                        },
                        plotLines: [
                            {
                                value: 0,
                                width: 1,
                                color: '#808080'
                            }
                        ]
                    },
                    tooltip: {
                        formatter: function() {
                            return '<b>'+ this.series.name +'</b><br/>'+
                                this.x +': '+ this.y;
                        }
                    },
                    legend: {
                        layout: 'vertical',
                        style: {
                            left: 'auto',
                            bottom: 'auto',
                            right: '10px',
                            top: '100px'
                        }
                    },
                    series: [
                        {
                    	    id: 'rand0',
                            name: 'Random data',
                            data: (function() {
                                // generate an array of random data
                                var data = [], time = (new Date()).getTime(), i;
                                for (i = -101; i <= 0; i++) {
                                    data.push({
                                        x: time + i * 1000,
                                        y: Math.random()
                                    });
                                }
                                return data;
                            })()
                        }
                    ]
                }
            })
        ]
    });
    graphWin.show();
    (function(){
        for (var cnt=-11;cnt<=0;cnt++) {
            addLine(cnt);
        
        }
    }).defer(1,this);
});