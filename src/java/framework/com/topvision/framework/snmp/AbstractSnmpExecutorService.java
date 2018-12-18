/***********************************************************************
 * $Id: AbstractSnmpExecutorService.java,v1.0 Aug 25, 2016 5:28:05 PM $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.snmp;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.smi.VariableBinding;
import org.springframework.beans.factory.annotation.Value;

import com.topvision.framework.common.TimeCostRecord;
import com.topvision.framework.exception.engine.EngineThreadException;
import com.topvision.framework.exception.engine.SnmpException;

/**
 * @author Victor
 * @created @Aug 25, 2016-5:28:05 PM
 *
 */
public class AbstractSnmpExecutorService {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected SnmpThreadPoolExecutor service = null;
    // add by bravin@20141230:由于SnmpExecutorService是在Engine上的,所以这里的值存放在配置文件中而不是数据库里
    @Value("${Snmp.snmpSetTimeout:15000}")
    private int snmpSetTimeout;
    @Value("${Snmp.snmpSetRetry:0}")
    private int snmpSetRetry;

    /**
     * @return the service
     */
    public SnmpThreadPoolExecutor getService() {
        return service;
    }

    /**
     * 支持对象的设置
     * 
     * @param <T>
     *            泛型
     * @param snmpParam
     *            Snmp 参数
     * @param data
     *            设置的对象
     * @return 返回设置后的agent返回的结果
     */
    public <T> T setData(SnmpParam snmpParam, T data) {
        snmpParam.setTimeout(snmpSetTimeout);
        snmpParam.setRetry((byte) snmpSetRetry);
        return execute(new SnmpWorker<T>(snmpParam) {
            private static final long serialVersionUID = -727320896339548070L;

            @Override
            protected void exec() {
                snmpUtil.reset(snmpParam);
                result = snmpUtil.set(result);
            }
        }, data);
    }

    /**
     * 根据类名获取对象，通过annotation注释OID来实现
     * 
     * @param <T>
     *            泛型定义
     * @param snmpParam
     *            snmp参数
     * @param clazz
     *            要获取的对象的类名
     * @return 获取的对象
     */
    public <T> T getData(SnmpParam snmpParam, final Class<T> clazz, final List<String> excludeOids) {
        return execute(new SnmpWorker<T>(snmpParam) {
            private static final long serialVersionUID = 5118271769494622783L;

            @Override
            protected void exec() {
                snmpUtil.reset(snmpParam);
                result = snmpUtil.get(clazz, excludeOids);
            }
        }, null);
    }

    /**
     * 根据类名获取对象，通过annotation注释OID来实现
     * 
     * @param <T>
     *            泛型定义
     * @param snmpParam
     *            snmp参数
     * @param clazz
     *            要获取的对象的类名
     * @return 获取的对象
     */
    public <T> T getData(SnmpParam snmpParam, final Class<T> clazz) {
        return execute(new SnmpWorker<T>(snmpParam) {
            private static final long serialVersionUID = 8011500144725239138L;

            @Override
            protected void exec() {
                snmpUtil.reset(snmpParam);
                result = snmpUtil.get(clazz);
            }
        }, null);
    }

    /**
     * 根据类获取一个表，注意要求一个类中定义的表如果是几个MIB表需要表行数一样，如果不一样不能一起获取，需要指定表名单独获取然后合并处理
     * 
     * @param <T>
     *            泛型定义
     * @param snmpParam
     *            snmp参数
     * @param clazz
     *            类的定义
     * @param table
     *            如果需要单独获取clazz中的某个MIB表，需要制定表名
     * @return 对象List
     */
    public <T> List<T> getTable(SnmpParam snmpParam, final Class<T> clazz, String... table) {
        List<T> data = new ArrayList<T>();
        return execute(new SnmpWorker<List<T>>(snmpParam) {
            private static final long serialVersionUID = -727320896339548070L;

            @Override
            protected void exec() {
                snmpUtil.reset(snmpParam);
                if (getParams() == null) {
                    result = snmpUtil.getTable(clazz, true);
                } else {
                    result = snmpUtil.getTable(clazz, (String) getParams()[0]);
                }
            }
        }, data, (Object[]) table);
    }

    /**
     * 根据类获取一个表，注意要求一个类中定义的表如果是几个MIB表需要表行数一样，如果不一样不能一起获取，需要指定表名单独获取然后合并处理
     * 
     * @param <T>
     *            泛型定义
     * @param snmpParam
     *            snmp参数
     * @param clazz
     *            类的定义
     * @param excludeOids
     *            需要过滤的OID列表
     * @return 对象List
     */
    public <T> List<T> getTable(SnmpParam snmpParam, final Class<T> clazz, final List<String> excludeOids) {
        List<T> data = new ArrayList<T>();
        return execute(new SnmpWorker<List<T>>(snmpParam) {
            private static final long serialVersionUID = -727320896339548070L;

            @Override
            protected void exec() {
                snmpUtil.reset(snmpParam);
                result = snmpUtil.getTable(clazz, true, excludeOids);
            }
        }, data);
    }

    /**
     * 获取一个表的一行
     * 
     * @param <T>
     *            泛型定义
     * @param snmpParam
     *            snmp参数
     * @param data
     *            行的index传入
     * @return 返回的行对象
     */
    public <T> T getTableLineByMutiStringIndex(SnmpParam snmpParam, T data) {
        return execute(new SnmpWorker<T>(snmpParam) {
            private static final long serialVersionUID = -727320896339548070L;

            @Override
            protected void exec() {
                snmpUtil.reset(snmpParam);
                result = snmpUtil.getTableLineByMutiStringIndex(result);
            }
        }, data);
    }

    /**
     * 获取一个表的一行
     * 
     * @param <T>
     *            泛型定义
     * @param snmpParam
     *            snmp参数
     * @param data
     *            行的index传入
     * @return 返回的行对象
     */
    public <T> T getTableLine(SnmpParam snmpParam, T data) {
        return execute(new SnmpWorker<T>(snmpParam) {
            private static final long serialVersionUID = -727320896339548070L;

            @Override
            protected void exec() {
                snmpUtil.reset(snmpParam);
                result = snmpUtil.getTableLine(result);
            }
        }, data);
    }

    /**
     * 获取一个表的多行
     * 
     * @param snmpParam
     * @param data
     * @return
     */
    public <T> List<T> getTableLine(SnmpParam snmpParam, List<T> data) {
        return executeList(new SnmpWorker<T>(snmpParam) {
            private static final long serialVersionUID = 2020876966047415700L;

            @Override
            protected void exec() {
                snmpUtil.reset(snmpParam);
                List<T> list = getDataProxy().getDataList();
                list = snmpUtil.getTableLine(list);
                getDataProxy().setDataList(list);
            }
        }, data);
    }

    /**
     * 获取一个表的一行
     * 
     * @param <T>
     *            泛型定义
     * @param snmpParam
     *            snmp参数
     * @param data
     *            行的index传入
     * @param excludeOids
     *            过滤oid
     * @return 返回的行对象
     */
    public <T> T getTableLine(SnmpParam snmpParam, T data, final List<String> excludeOids) {
        return execute(new SnmpWorker<T>(snmpParam) {
            private static final long serialVersionUID = 5755834630239875129L;

            @Override
            protected void exec() {
                snmpUtil.reset(snmpParam);
                result = snmpUtil.getTableLine(result, excludeOids);
            }
        }, data);
    }

    /**
     * 获取一个表按照最后一个索引的范围内所有行
     * 
     * @param snmpParam
     * @param data
     * @param startIndex
     * @param endIndex
     * @return
     */
    public <T> List<T> getTableRangeLine(SnmpParam snmpParam, T data, final long startIndex, final long endIndex) {
        List<T> list = new ArrayList<T>();
        list.add(data);
        return execute(new SnmpWorker<List<T>>(snmpParam) {
            private static final long serialVersionUID = -4183199659718647811L;

            @Override
            protected void exec() {
                snmpUtil.reset(snmpParam);
                result = snmpUtil.getTableRangeLines(result.get(0), startIndex, endIndex);
            }
        }, list);
    }

    /**
     * 对于多个index获取，指定前面几个，后面一个可以不指定，暂时仅支持最后一个index从1开始
     * 
     * 
     * @param <T>
     *            泛型定义
     * @param snmpParam
     *            snmp参数
     * @param data
     *            获取对象实体，需要包括前面几个index值
     * @param firstIndex
     *            没有指定index的第一个值
     * @param length
     *            获取最大的长度，如果不限制则填入Integer.MAX_VALUE
     * @return
     */
    public <T> List<T> getTableLines(SnmpParam snmpParam, T data, final int firstIndex, final int length) {
        List<T> list = new ArrayList<T>();
        list.add(data);
        return execute(new SnmpWorker<List<T>>(snmpParam) {
            private static final long serialVersionUID = -727320896339548070L;

            @Override
            protected void exec() {
                snmpUtil.reset(snmpParam);
                result = snmpUtil.getTableLines(result.get(0), firstIndex, length);
            }
        }, list);
    }

    /**
     * 获取单个节点
     * 
     * @param snmpParam
     *            snmp参数
     * @param oid
     *            节点OID
     * @return oid对应的值
     */
    public String get(SnmpParam snmpParam, final String oid) {
        return execute(new SnmpWorker<String>(snmpParam) {
            private static final long serialVersionUID = -727320896339548070L;

            @Override
            protected void exec() {
                snmpUtil.reset(snmpParam);
                result = snmpUtil.get(oid);
            }
        }, null);
    }
    
    /**
     * 获取单个节点，并记录耗时
     * 
     * @param snmpParam
     *            snmp参数
     * @param oid
     *            节点OID
     * @return oid对应的值
     */
    public String get(SnmpParam snmpParam, final String oid, TimeCostRecord record) {
        return execute(new SnmpWorker<String>(snmpParam) {
            private static final long serialVersionUID = -727320896339548070L;

            @Override
            protected void exec() {
                snmpUtil.reset(snmpParam);
                record.setStartTime(System.currentTimeMillis());
                result = snmpUtil.get(oid);
                record.setEndTime(System.currentTimeMillis());
            }
        }, null);
    }

    /**
     * 获取多个节点
     * 
     * @param snmpParam
     *            snmp参数
     * @param oids
     *            节点OID
     * @return oids对应的值
     */
    public String[] get(SnmpParam snmpParam, final String[] oids) {
        return execute(new SnmpWorker<String[]>(snmpParam) {
            private static final long serialVersionUID = -727320896339548070L;

            @Override
            protected void exec() {
                snmpUtil.reset(snmpParam);
                result = snmpUtil.get(oids);
            }
        }, null);
    }

    /**
     * 获取单个节点的下一个节点
     * 
     * @param snmpParam
     *            snmp参数
     * @param oid
     *            节点OID
     * @return oid下一个节点对应的值
     */
    public String getNext(SnmpParam snmpParam, final String oid) {
        return execute(new SnmpWorker<String>(snmpParam) {
            private static final long serialVersionUID = -727320896339548070L;

            @Override
            protected void exec() {
                snmpUtil.reset(snmpParam);
                result = snmpUtil.getNext(oid);
            }
        }, null);
    }

    /**
     * 获取单个节点的绑定（OID,Name及其值） 返回的Map包含oid , name , value ,realOid
     * 4个键，其中name表示的是oid对应的键名，realOid表示的是Mib中名字对应的oid键 比如 oid 可以为
     * 1.3.6.1.4.1.32285.11.2.3.2.1.1.3.0.1 而realOid 为 1.3.6.1.4.1.32285.11.2.3.2.1.1.3
     * 
     * @param snmpParam
     *            snmp参数
     * @param oid
     *            节点OID
     * @return 返回Map的Key如下： oid , name , value ,realOid
     */
    public Map<String, String> getVB(SnmpParam snmpParam, final String oid) {
        return execute(new SnmpWorker<Map<String, String>>(snmpParam) {
            private static final long serialVersionUID = -727320896339548070L;

            @Override
            protected void exec() {
                snmpUtil.reset(snmpParam);
                VariableBinding bind = snmpUtil.getVB(oid);
                String oid = bind.getOid().toString();
                String name = snmpUtil.getNameByOid(bind.getOid().toString());
                String realOid = snmpUtil.getOidByName(name);
                result.put("oid", oid);
                result.put("name", name);
                result.put("realOid", realOid);
                result.put("value", bind.getVariable().toString());
            }
        }, new HashMap<String, String>());
    }

    /**
     * 获取单个节点的下一个节点的绑定（OID,NAME及其值） 返回的Map包含oid , name , value ,realOid
     * 4个键，其中name表示的是oid对应的键名，realOid表示的是Mib中名字对应的oid键 比如 oid 可以为
     * 1.3.6.1.4.1.32285.11.2.3.2.1.1.3.0.1 而realOid 为 1.3.6.1.4.1.32285.11.2.3.2.1.1.3
     * 
     * @param snmpParam
     *            snmp参数
     * @param oid
     *            节点OID
     * @return 返回Map的Key如下： oid , name , value ,realOid
     * 
     */
    public Map<String, String> getNextVB(SnmpParam snmpParam, final String oid) {
        return execute(new SnmpWorker<Map<String, String>>(snmpParam) {
            private static final long serialVersionUID = -727320896339548070L;

            @Override
            protected void exec() {
                snmpUtil.reset(snmpParam);
                VariableBinding bind = snmpUtil.getNextVB(oid);
                String oid = bind.getOid().toString();
                String name = snmpUtil.getNameByOid(bind.getOid().toString());
                String realOid = snmpUtil.getOidByName(name);
                result.put("oid", oid);
                result.put("name", name);
                result.put("realOid", realOid);
                result.put("value", bind.getVariable().toString());
            }
        }, new HashMap<String, String>());
    }

    /**
     * 
     * @param snmpParam
     * @param oid
     * @param value
     * @return
     */
    public String set(SnmpParam snmpParam, final String oid, final String value) {
        snmpParam.setTimeout(snmpSetTimeout);
        snmpParam.setRetry((byte) snmpSetRetry);
        return execute(new SnmpWorker<String>(snmpParam) {
            private static final long serialVersionUID = -727320896339548070L;

            @Override
            protected void exec() {
                snmpUtil.reset(snmpParam);
                result = snmpUtil.set(oid, value);
            }
        }, null);
    }

    /**
     * 
     * @param <T>
     * @param task
     * @param result
     * @param params
     * @return
     */
    public <T> Future<SnmpDataProxy<T>> submit(SnmpWorker<T> task, T result, Object... params) {
        SnmpDataProxy<T> proxy = new SnmpDataProxy<T>();
        proxy.setData(result);
        proxy.setInvokeStacktrace(Thread.currentThread().getStackTrace());
        if (params != null) {
            proxy.setParams(params);
        }
        task.setDataProxy(proxy);
        return service.submit(task, proxy);
    }

    /**
     * 主要是解决对象传入后重新赋值后不能带出线程池，采用List对象解决。
     * 
     * @param <T>
     *            泛型定义
     * @param task
     *            snmp执行程序
     * @param data
     *            传递的对象
     * @return 执行的结果
     */
    public <T> T execute(SnmpWorker<T> task, T data, Object... params) {
        SnmpDataProxy<T> proxy = new SnmpDataProxy<T>();
        proxy.setData(data);
        proxy.setInvokeStacktrace(Thread.currentThread().getStackTrace());
        if (params != null) {
            proxy.setParams(params);
        }
        task.setDataProxy(proxy);
        Future<SnmpDataProxy<T>> f = service.submit(task, proxy);
        try {
            proxy = f.get();
        } catch (InterruptedException e) {
            throw new EngineThreadException(e);
        } catch (ExecutionException e) {
            throw new EngineThreadException(e);
        }
        if (proxy.getStacktrace() != null) {
            if (proxy.getStacktrace() instanceof RuntimeException) {
                throw (RuntimeException) proxy.getStacktrace();
            } else {
                throw new SnmpException(proxy.getStacktrace());
            }
        }
        if (logger.isDebugEnabled()) {
            if (data instanceof String) {
                logger.debug("print:{}={}", data, proxy.getData());
            } else {
                printObject(proxy.getData());
            }
        }
        return proxy.getData();
    }

    public <T> List<T> executeList(SnmpWorker<T> task, List<T> data, Object... params) {
        SnmpDataProxy<T> proxy = new SnmpDataProxy<T>();
        proxy.setDataList(data);
        proxy.setInvokeStacktrace(Thread.currentThread().getStackTrace());
        if (params != null) {
            proxy.setParams(params);
        }
        task.setDataProxy(proxy);
        Future<SnmpDataProxy<T>> f = service.submit(task, proxy);
        try {
            proxy = f.get();
        } catch (InterruptedException e) {
            throw new EngineThreadException(e);
        } catch (ExecutionException e) {
            throw new EngineThreadException(e);
        }
        if (proxy.getStacktrace() != null) {
            if (proxy.getStacktrace() instanceof RuntimeException) {
                throw (RuntimeException) proxy.getStacktrace();
            } else {
                throw new SnmpException(proxy.getStacktrace());
            }
        }
        return proxy.getDataList();
    }

    /**
     * 打印对象所有字段及其值
     * 
     * @param obj
     *            要打印的对象
     */
    private void printObject(Object obj) {
        StringBuilder data = new StringBuilder();
        Class<?> parent = obj.getClass();
        while (parent != null && !parent.equals(Object.class)) {
            Field[] fs = parent.getDeclaredFields();
            for (int i = 0; fs != null && i < fs.length; i++) {
                fs[i].setAccessible(true);
                try {
                    data.append("\n").append(fs[i].getName()).append("=").append(fs[i].get(obj));
                } catch (Exception e) {
                    logger.debug(fs[i].getName(), e);
                }
            }
            parent = parent.getSuperclass();
        }
        logger.debug("print:{}{}", obj.getClass(), data);
    }

    public int getSnmpSetTimeout() {
        return snmpSetTimeout;
    }

    public void setSnmpSetTimeout(int snmpSetTimeout) {
        this.snmpSetTimeout = snmpSetTimeout;
    }

    public int getSnmpSetRetry() {
        return snmpSetRetry;
    }

    public void setSnmpSetRetry(int snmpSetRetry) {
        this.snmpSetRetry = snmpSetRetry;
    }
}
