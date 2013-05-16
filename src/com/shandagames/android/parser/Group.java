/**
 * Copyright 2009 Joe LaPenna
 */

package com.shandagames.android.parser;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @file Group.java
 * @create 2012-9-20 下午1:32:59
 * @author lilong
 * @description TODO
 */
public class Group<T extends ResultType> extends ArrayList<T> implements ResultType {

    private static final long serialVersionUID = 1L;

    private int count;
    
    public Group() {
        super();
    }
    
    public Group(Collection<T> collection) {
        super(collection);
    }

    public void setCount(int size) {
    	count = size;
    }

    public int getCount() {
        return count;
    }
}
