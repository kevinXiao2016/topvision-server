/***********************************************************************
 * $Id: LoggerPusher.java,v1.0 2012-11-25 下午4:14:27 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.logger.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.CheckFacade;
import com.topvision.ems.logger.domain.LoggerMessage;
import com.topvision.ems.logger.service.LoggerService;
import com.topvision.ems.socketserver.domain.SocketRequest;
import com.topvision.ems.socketserver.domain.SocketResponse;
import com.topvision.ems.socketserver.socket.SocketRequestExecutor;
import com.topvision.ems.socketserver.websocket.TopWebSocketHandler;
import com.topvision.platform.domain.EngineServer;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.service.EngineServerService;
import com.topvision.platform.zetaframework.util.ZetaUtil;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * @author Bravin
 * @created @2012-11-25-下午4:14:27
 * 
 */
@Service("loggerConnector")
public class LoggerConnector implements SocketRequestExecutor, TopWebSocketHandler {
    private static Logger logger = LoggerFactory.getLogger(LoggerConnector.class);
    private static BlockingQueue<LoggerMessage> loggerQuene = null;
    private ExecutorService executorService;
    @Autowired
    private LoggerService service;
    private AppenderBase<ILoggingEvent> appender;
    private final Map<String, MySocketResponse> socketResponseMapper = new HashMap<String, MySocketResponse>();
    // Add by Victor@20151212增加engine端日志的修改
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EngineServerService engineServerService;

    /**
     * 销毁该推送器
     */
    @PreDestroy
    public void destroy() {
        if (executorService != null) {
            executorService.shutdownNow();
        }
        loggerQuene = null;
    }

    public static void appendLogger(LoggerMessage log) {
        try {
            loggerQuene.add(log);
        } catch (Exception e) {
            logger.info("", e);
        }
    }

    @PostConstruct
    public void initialize() {
        appender = new LoggerAppender();
        loggerQuene = new ArrayBlockingQueue<LoggerMessage>(10000);
        executorService = Executors.newSingleThreadExecutor();
        // ------开启一个DWR专用线程用于告警推送------//
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("LoggerPusher");
                while (loggerQuene != null) {
                    try {
                        // 获取并移除此告警队列的头部，在元素变得可用之前一直等待（如果有必要）。
                        LoggerMessage $message;
                        $message = loggerQuene.take();
                        Set<Entry<String, MySocketResponse>> sets = socketResponseMapper.entrySet();
                        for (Entry<String, MySocketResponse> $entry : sets) {
                            MySocketResponse $response = $entry.getValue();
                            if (!$response.getSocketResponse().isClosed()) {
                                $response.write($message);
                            } else {
                                synchronized (socketResponseMapper) {
                                    sets.remove($entry);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        logger.error("Send message error:", ex);
                    }
                }
            }
        });
    }

    /**
     * 恢复为出厂设置
     * 
     * @return
     */
    public boolean restoreFactoryConfig() {
        try {
            boolean result = service.restoreFactoryConfig();
            return result;
        } catch (Exception e) {
            logger.error("", e);
            return false;
        }
    }

    /**
     * 记录response，以便于推送数据
     * 
     * modify by fanzidong,之前同一个IP只能有一个推送，无法支持多客户端
     * 
     * @return
     */
    public void registerSocResponse(String uuid, MySocketResponse mySocketResponse) {
        socketResponseMapper.put(uuid, mySocketResponse);
    }

    public void resetSubLevel() {

    }

    class FilterMatch {
        public static final int OPERATE_EQ = 0;// ==
        public static final int OPERATE_BE = 1;// >=
        public static final int OPERATE_BG = 2;// >
        public static final int OPERATE_SE = 3;// <=
        public static final int OPERATE_SM = 4;// <
        public static final int OPERATE_NON = 5;// !=
        public static final int OPERATE_LIKE = 6;// ~
        private Integer level;
        private String thread;
        private String name;
        private int operate;

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public String getThread() {
            return thread;
        }

        public void setThread(String thread) {
            this.thread = thread;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOperate() {
            return operate;
        }

        public void setOperate(int operate) {
            this.operate = operate;
        }

    }

    class MySocketResponse {
        private List<String> includes;
        private List<FilterMatch> filters;
        private final SocketResponse socketResponse;

        public MySocketResponse(SocketResponse socketResponse) {
            this.socketResponse = socketResponse;
        }

        /**
         * 只要满足一个条件就符合
         * 
         * @param name
         * @return
         */
        private boolean nameMatch(String name) {
            if (includes == null) {
                return false;
            }
            for (String $regix : includes) {
                if (name.matches($regix)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * 只要一个条件不符合就抛弃
         * 
         * @param $message
         * @return
         */
        private boolean filterMatch(LoggerMessage $message) {
            if (filters == null) {
                return false;
            }
            int level = $message.getLevel();
            for (FilterMatch $match : filters) {
                Integer $level = $match.getLevel();
                boolean matched = false;
                if ($match.getLevel() != null) {
                    switch ($match.operate) {
                    case FilterMatch.OPERATE_EQ:
                        matched = level == $level;
                        break;
                    case FilterMatch.OPERATE_BE:
                        matched = level >= $level;
                        break;
                    case FilterMatch.OPERATE_BG:
                        matched = level > $level;
                        break;
                    case FilterMatch.OPERATE_SE:
                        matched = level <= $level;
                        break;
                    case FilterMatch.OPERATE_SM:
                        matched = level < $level;
                        break;
                    }
                } else if ($match.getThread() != null) {
                    matched = $match.getThread().equals($message.getThread());
                    switch ($match.operate) {
                    case FilterMatch.OPERATE_EQ:
                        break;
                    case FilterMatch.OPERATE_NON:
                        matched = !matched;
                        break;
                    }
                }
                if (!matched) {
                    return false;
                }
            }
            return true;
        }

        public void write(LoggerMessage $message) {
            String name = $message.getName();
            try {
                if (nameMatch(name) && filterMatch($message)) {
                    socketResponse.write(
                            $message.getData().replaceAll("@", "\\$\\$")/* .replace("\n", "<br>") */);
                    socketResponse.flush();
                }
            } catch (IOException e) {
                logger.error("", e);
            }
        }

        public List<String> getIncludes() {
            return includes;
        }

        public void setIncludes(List<String> includes) {
            this.includes = includes;
        }

        public List<FilterMatch> getFilters() {
            return filters;
        }

        public void setFilters(List<FilterMatch> filters) {
            this.filters = filters;
        }

        public SocketResponse getSocketResponse() {
            return socketResponse;
        }

    }

    @Override
    public boolean execute(SocketRequest socketRequest, SocketResponse socketResponse) {
        String host = socketRequest.getRequestHost();
        // if (!socketResponseMapper.containsKey(host)) {
        registerSocResponse(host, new MySocketResponse(socketResponse));
        // }
        String $query = socketRequest.getString("SOCKET.QUERY");
        try {
            decodeAndExecuteQuery($query, host);
            socketResponse.writeIgnoreExcp(
                    "<p class=greenTxt>" + ZetaUtil.getStaticString("execResultOk", "logger") + "</p>", host, $query);
        } catch (Exception e) {
            socketResponse.writeIgnoreExcp(
                    "<p class=redTxt>" + ZetaUtil.getStaticString("execResultEr", "logger") + "</p>", host, $query);
        }
        return MAINTAIN_SOCKET;
    }

    /**
     * 解析并执行语句
     * 
     * @param $query
     * @param host
     */
    private void decodeAndExecuteQuery(String $query, String socketKey) {
        if ($query == null) {
            return;
        }
        if ($query.startsWith("ENGINE")) {
            String[] data = $query.split(" ");
            EngineServer engine = engineServerService.getEngineServerById(Integer.parseInt(data[1]));
            CheckFacade checkFacade = facadeFactory.getCheckFacade(engine);
            if ("RESET".equals(data[2])) {
                checkFacade.resetLogger();
            }
            return;
        } else if ("RESET".equals($query)) {
            restoreFactoryConfig();
            return;
        } else if ("STOP".equals($query)) {
            service.detachAndStoAppender(appender);
            return;
        }
        int c = $query.indexOf(" ");
        String $head = $query.substring(0, c).toUpperCase();
        if ($head.matches("UPDATE|ACCEPT_THREAD|ACCEPT|ADD_APPENDER|RESET")) {// ^(UPDATE|ACCEPT|ADD
                                                                              // APPENDER)\\s?.?(FILTER|SET)*
            String[] $f, $l;
            List<String> $includes, $actions;
            switch ($head) {
            case "UPDATE":
                // List<String> params = new ArrayList<String>();
                $f = $query.split("UPDATE|SET");
                $l = $f[1].split(" AND ");
                $includes = new ArrayList<String>();
                for (String $s : $l) {
                    $includes.add($s.trim());
                }
                $actions = new ArrayList<String>();
                if ($f.length > 2) {
                    String[] $set = $f[2].split(" AND ");
                    for (String $s : $set) {
                        $actions.add($s.trim());
                    }
                }
                /*
                 * appender.addFilter(new Filter<ILoggingEvent>() {
                 * 
                 * @Override public FilterReply decide(ILoggingEvent event) {
                 * System.out.println(event.getLevel()); return FilterReply.ACCEPT; } });
                 */
                updateLog($includes, $actions);
                break;
            case "ACCEPT_THREAD":
                $f = $query.split("ACCEPT_THREAD");
                break;
            case "ACCEPT":
                $f = $query.split("ACCEPT[\\s]+|[\\s]+FILTER[\\s]+");
                $l = $f[1].split(" AND ");
                $includes = new ArrayList<String>();
                for (String $s : $l) {
                    $includes.add($s.trim());
                }
                $actions = new ArrayList<String>();
                if ($f.length > 2) {
                    String[] $set = $f[2].split(" AND ");
                    for (String $s : $set) {
                        $actions.add($s.trim());
                    }
                }
                startRecvLog($includes, $actions, socketKey);
                break;
            case "ADD":
                break;
            }

        }
    }

    /**
     * 开始接收时过滤后的日志
     * 
     * @param $includes
     * @param $actions
     * @param host
     */
    private void startRecvLog(List<String> $includes, List<String> $actions, String socketKey) {
        List<String> newIncludes = new ArrayList<String>();
        List<FilterMatch> newFilters = new ArrayList<FilterMatch>();
        for (String $include : $includes) {
            $include = $include.replaceAll("\\.", "[.]{1}").replaceAll("\\*", ".*");
            $include = "^" + $include;
            newIncludes.add($include);
        }
        for (String param : $actions) {
            param = param.trim();
            Pattern $patt = Pattern.compile("=|>=|>|<=|<|!=|LIKE");
            String[] $kv = $patt.split(param);
            String key = $kv[0].trim();
            String value = $kv[1].trim();
            FilterMatch fmatch = new FilterMatch();
            String operate = param.replace(key, "").replace(value, "").trim();
            int $opreate = -1;
            switch (operate) {
            case "=":
                $opreate = FilterMatch.OPERATE_EQ;
                break;
            case ">=":
                $opreate = FilterMatch.OPERATE_BE;
                break;
            case ">":
                $opreate = FilterMatch.OPERATE_BG;
                break;
            case "<=":
                $opreate = FilterMatch.OPERATE_SE;
                break;
            case "<":
                $opreate = FilterMatch.OPERATE_SM;
                break;
            case "!=":
                $opreate = FilterMatch.OPERATE_NON;
                break;
            case "~":
                $opreate = FilterMatch.OPERATE_LIKE;
                break;
            }
            fmatch.setOperate($opreate);
            switch (key) {
            case "LEVEL":
                fmatch.setLevel(Level.valueOf(value).toInteger());
                break;
            case "THREAD":
                fmatch.setThread(value);
            }
            newFilters.add(fmatch);
        }
        MySocketResponse mySocketResponse = socketResponseMapper.get(socketKey);
        mySocketResponse.setFilters(newFilters);
        mySocketResponse.setIncludes(newIncludes);
        // useMirrorLogback();
        addAppenderToContext(appender);

    }

    /**
     * 添加appender
     * 
     * @param appender
     */
    private void addAppenderToContext(AppenderBase<ILoggingEvent> appender) {
        service.addAppenderToContext(appender);
    }

    /**
     * 对于符合条件的每一个logger执行操作，不过目前只能配置Level
     * 
     * @param includeLogs
     * @param setParam
     */
    private void updateLog(List<String> includeLogs, List<String> setParam) {
        service.configLogger(includeLogs, setParam);
    }

    public static void main(String[] args) {
        // String s = "com.topvision.ems.epon.*".replaceAll("\\.", "[.]{1}").replaceAll("\\*",
        // ".*");
        // System.out.println("com.topvision.ems.epon.onu.domain.Onu.updateOnuPreType".matches(s));
        System.out.println("abab.XXWWW".matches("abab[.]{1}XX*"));
        // decodeAndExecuteQuery("ACCEPT com.topvision.ems.*.epon.* AND com.topvision.ems.*.cmc.*
        // FILTER LEVEL");
    }

    @Override
    public void onConnected(SocketRequest socketRequest) {
        registerSocResponse(socketRequest.getUuid(), new MySocketResponse(socketRequest.getResponse()));
    }

    @Override
    public void onClose(SocketRequest socketRequest) {
        synchronized (socketResponseMapper) {
            socketResponseMapper.remove(socketRequest.getRequestHost());
        }
    }

    @Override
    public void onMessage(SocketRequest socketRequest) {
        String $query = socketRequest.getString("SOCKET.QUERY");
        String host = socketRequest.getRequestHost();
        SocketResponse socketResponse = socketRequest.getResponse();
        try {
            decodeAndExecuteQuery($query, socketRequest.getUuid());
            socketResponse.writeIgnoreExcp(
                    "<p class=greenTxt>" + ZetaUtil.getStaticString("execResultOk", "logger") + "</p>", host, $query);
        } catch (Exception e) {
            socketResponse.writeIgnoreExcp(
                    "<p class=redTxt>" + ZetaUtil.getStaticString("execResultEr", "logger") + "</p>", host, $query);
        }
    }
}
