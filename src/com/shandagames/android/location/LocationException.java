/**
 * 
 */
package com.shandagames.android.location;

/**
 * @file LocationException.java
 * @create 2012-9-20 下午1:08:03
 * @author lilong
 * @description TODO
 */
public class LocationException extends Exception {

    public LocationException() {
        super("Unable to determine your location.");
    }

    public LocationException(String message) {
        super(message);
    }

    private static final long serialVersionUID = 1L;
    
}
