package com.shandagames.android.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.content.Context;
import android.content.res.Configuration;

/**
 * @file ReflectionSupport.java
 * @create 2012-8-20 下午3:42:23
 * @author selience
 * @description A set of helper methods for best-effort method calls via reflection.
 */
public class ReflectionSupport {

	public static Object tryInvoke(Object target, String methodName, Object... args) {
        Class<?>[] argTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }

        return tryInvoke(target, methodName, argTypes, args);
    }
	
	public static Object tryInvoke(Object target, String methodName, Class<?>[] argTypes,
            Object... args) {
        try {
           // return target.getClass().getMethod(methodName, argTypes).invoke(target, args);
        	Method method = target.getClass().getMethod(methodName, argTypes);
        	return method.invoke(target, args);
        } catch (NoSuchMethodException ignored) {
        	ignored.printStackTrace();
        } catch (IllegalAccessException ignored) {
        	ignored.printStackTrace();
        } catch (InvocationTargetException ignored) {
        	ignored.printStackTrace();
        }

        return null;
    }
	
	@SuppressWarnings("unchecked")
	public static <E> E callWithDefault(Object target, String methodName, E defaultValue) {
        try {
            //noinspection unchecked
            return (E) target.getClass().getMethod(methodName, (Class[]) null).invoke(target);
        } catch (NoSuchMethodException ignored) {
        } catch (IllegalAccessException ignored) {
        } catch (InvocationTargetException ignored) {
        }

        return defaultValue;
    }
	
	
	/** 判断是Android手机还是平板*/
	public static boolean isTabletDevice(Context context) {
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			 // test screen size, use reflection because isLayoutSizeAtLeast is only available since 11
	        Configuration con = context.getResources().getConfiguration();
	        try {
	            Method mIsLayoutSizeAtLeast = con.getClass().getMethod("isLayoutSizeAtLeast", int.class);
	            // Configuration.SCREENLAYOUT_SIZE_XLARGE
	            Boolean r = (Boolean) mIsLayoutSizeAtLeast.invoke(con, 0x00000004); 
	            return r;
	        } catch (Exception x) {
	            return false;
	        }
		}
		return false;
	}
}
