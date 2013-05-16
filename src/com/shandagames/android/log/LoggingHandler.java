package com.shandagames.android.log;

import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @file JavaLoggingHandler.java
 * @create 2013-3-26 上午11:45:45
 * @author Jacky.Lee
 * @description TODO
 */
public class LoggingHandler extends Handler {

    private static Map<Level, Integer> sLoglevelMap = new HashMap<Level, Integer>();
    
    static {
        sLoglevelMap.put(Level.FINEST, Log.VERBOSE);
        sLoglevelMap.put(Level.FINER, Log.DEBUG);
        sLoglevelMap.put(Level.FINE, Log.DEBUG);
        sLoglevelMap.put(Level.INFO, Log.INFO);
        sLoglevelMap.put(Level.WARNING, Log.WARN);
        sLoglevelMap.put(Level.SEVERE, Log.ERROR);
    }

    @Override
    public void publish(LogRecord record) {
    	System.out.println("###################publish init...");
        Integer level = sLoglevelMap.get(record.getLevel());
        if (level == null) {
            level = Log.VERBOSE;
        }
        Log.println(level, record.getLoggerName(), record.getMessage());
    }

    @Override
    public void close() {
    }

    @Override
    public void flush() {
    }

}
