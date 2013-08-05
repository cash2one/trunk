package com.shandagames.android.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
}
