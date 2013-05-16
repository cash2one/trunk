package com.shandagames.android.parser;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONHandler {

	/**
	 * 绑定JSON字符串到实体对象
	 * @param content json字符串
	 * @param cls Class<T>
	 * @return
	 */
	public static <T> T bindDataToModel(String content, Class<T> cls) {
		return ObjectMapper.bindDataToModel(content, cls);
	}

	/**
	 * 绑定JSON字符串到List集合
	 * @param content json字符串
	 * @param cls Class<T>
	 * @return
	 */
	public static <T> List<T> bindDataToList(String content, Class<T> cls) {
		return ObjectMapper.bindDataToList(content, cls);
	}

	/**
	 * 绑定JSON字符串到Map集合
	 * @param content json字符串
	 * @param cls Class<T>
	 * @return
	 */
	public static Map<?, ?> bindDataToMap(String content) {
		return ObjectMapper.bindDataToMap(content);
	}

	/**
	 * Model binder class. This class can bind json string to model, to model
	 * list, or to map.
	 */
	public static class ObjectMapper {

		/**
		 * bind json string to model.
		 * 
		 * @param <T> T
		 * @param content json string
		 * @param cls Class<T>
		 * @return <T>
		 */
		private static <T> T bindDataToModel(String content, Class<T> cls) {
			try {
				JSONObject mjson = new JSONObject(content);
				T instance = cls.newInstance();
				Iterator<?> iterator = mjson.keys();
				while (iterator.hasNext()) {
					String propertyName = iterator.next().toString();
					Object propertyValue = mjson.opt(propertyName);

					if(!JSONObject.NULL.equals(propertyValue)) {
						try {
							Class<?> clazz = instance.getClass();
							Field field = clazz.getDeclaredField(propertyName);
							ModelReflector.setField(instance, field, propertyValue.toString());
						} catch (NoSuchFieldException ex) {}
					}
				}
				return instance;
			} catch (JSONException ex) {
				ex.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}

		/**
		 * bind json string to model list.
		 * 
		 * @param <T>  T
		 * @param content json string
		 * @param cls Class<T>
		 * @return <T> List<T>
		 */
		private static <T> List<T> bindDataToList(String content, Class<T> cls) {
			try {
				JSONArray mjson = new JSONArray(content);
				List<T> list = new ArrayList<T>();
				for (int i = 0; i < mjson.length(); i++) {
					T t = bindDataToModel(mjson.get(i).toString(), cls);
					list.add(t);
				}
				return list;
			} catch (JSONException ex) {
				ex.printStackTrace();
			}
			return null;
		}

		/**
		 * bind json string to map object.
		 * 
		 * @param content json string
		 * @return map object.
		 */
		private static Map<?, ?> bindDataToMap(String content) {
			try {
				JSONObject mjson = new JSONObject(content);
				Map<String, Object> map = new HashMap<String, Object>();
				Iterator<?> it = mjson.keys();
				while (it.hasNext()) {
					String entry = it.next().toString();
					map.put(entry, mjson.opt(entry));
				}
				return map;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * Model reflector class.
	 */
	public static class ModelReflector {

		/** 类型是否为泛型集合  */
		private static boolean isCollection(Class<?> clazz) {
			return clazz != null && Collection.class.isAssignableFrom(clazz);
		}
		
		/** 类型是否为数组  */
		private static boolean isArray(Class<?> clazz) {
			return clazz != null && clazz.isArray();
		}

		/** 类型是否为布尔型  */
		private static boolean isBoolean(Class<?> clazz) {
			return (clazz != null)
					&& ((Boolean.TYPE.isAssignableFrom(clazz)) || (Boolean.class
							.isAssignableFrom(clazz)));
		}

		/** 类型是否为数字  */
		private static boolean isNumber(Class<?> clazz) {
			return (clazz != null)
					&& ((Byte.TYPE.isAssignableFrom(clazz))
					|| (Short.TYPE.isAssignableFrom(clazz))
					|| (Integer.TYPE.isAssignableFrom(clazz))
					|| (Long.TYPE.isAssignableFrom(clazz))
					|| (Float.TYPE.isAssignableFrom(clazz))
					|| (Double.TYPE.isAssignableFrom(clazz)) 
					|| (Number.class.isAssignableFrom(clazz)));
		}

		/** 类型是否为字符(串) */
		private static boolean isString(Class<?> clazz) {
			return (clazz != null)
					&& ((String.class.isAssignableFrom(clazz))
							|| (Character.TYPE.isAssignableFrom(clazz)) || (Character.class
								.isAssignableFrom(clazz)));
		}

		/** 类型是否为基本数据类型  */
		private static boolean isSingle(Class<?> clazz) {
			return isBoolean(clazz) || isNumber(clazz) || isString(clazz);
		}
		
		/** 类型是否为引用数据类型(类/接口对象)  */
		private static boolean isObject(Class<?> clazz) {
			return clazz != null && !isSingle(clazz) && !isArray(clazz)
					&& !isCollection(clazz);
		}
		
		/** 判断是否为接口或抽象类*/
		private static boolean isAbstractOrInterface(Class<?> clazz) {
			//modifier of the abstract class is '1025' and interface is '1545'
			return (clazz.getModifiers() == 1025 || clazz.getModifiers() == 1545);
		}
		
		/** 判断json对象是否为空  */
		private static boolean isNull(Object obj) {
			return JSONObject.NULL.equals(obj);
		}

		/** 创建泛型类的实例  */
		private static <T> T createInstance(Class<T> clazz) {
			if (clazz == null || isAbstractOrInterface(clazz))
				return null;
			T obj = null;
			try {
				obj = clazz.newInstance();
			} catch (InstantiationException ex) {
				// notice: the abstract class or interface is not instance
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return obj;
		}
		
		/** 转换json对象为集合  */
		@SuppressWarnings("unchecked")
		private static <T> Collection<T> parseCollection(JSONArray json,
				Class<?> collectionClazz, Class<T> genericType) {
			if (collectionClazz == null || genericType == null || isNull(json)) {
				return null;
			}
			Collection<T> collection = (Collection<T>)createInstance(collectionClazz);
			for (int i = 0; i < json.length(); i++) {
				try {
					if(isSingle(genericType)) {
						T _t = (T)json.get(i);
						collection.add(_t);
					} else if(isObject(genericType)) {
						JSONObject mjson = json.getJSONObject(i);
						T _t = parseObject(mjson, genericType);
						collection.add(_t);
					} else if(isArray(genericType)) {
						JSONArray mjson = json.getJSONArray(i);
						T _t = (T)parseArray(mjson, genericType);
						collection.add(_t);
					} 
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return collection;
		}

		/** 转换json对象为数组  */
		@SuppressWarnings("unchecked")
		private static <T> T[] parseArray(JSONArray mjson, Class<T> clazz) {
			if (clazz == null || isNull(mjson)) {
				return null;
			}
			int len = mjson.length();
			try {
				Object array = Array.newInstance(clazz, len);
				for (int i = 0; i < len; i++) {
					if (isSingle(clazz)) { //基本数据类型数组
						Array.set(array, i, convertWrapClass(mjson.get(i), clazz));
					} else if (isObject(clazz)) { //引用数据类型数组
						JSONObject json = mjson.getJSONObject(i);
						Array.set(array, i, parseObject(json, clazz));
					} 
				}
				return (T[])array;
			} catch(JSONException ex) {
				ex.printStackTrace();
			}
			return null;
		}
		
		/** 转换json对象为object */
		private static <T> T parseObject(JSONObject json, Class<T> clazz) {
			if (clazz == null || isNull(json)) {
				return null;
			}
			T obj = createInstance(clazz);
			if (obj == null) {
				return null;
			}
			for (Field field : clazz.getFields()) {
				setField(obj, field, json.toString());
			}
			return obj;
		}

		/** 获取getter方法  */
		private static String getGetterName(String propertyName) {
			String method = "get" + propertyName.substring(0, 1).toUpperCase()
					+ propertyName.substring(1);
			return method;
		}

		/** 获取setter方法  */
		private static String getSetterName(String propertyName) {
			String method = "set" + propertyName.substring(0, 1).toUpperCase()
					+ propertyName.substring(1);
			return method;
		}
		
		/** 动态执行getter方法  */
		public static Object getProperty(Object bean, String propertyName) {
			Class<?> clazz = bean.getClass();
			try {
				Field field = clazz.getDeclaredField(propertyName);
				Method method = clazz.getDeclaredMethod(
						getGetterName(field.getName()),
						new Class[] { field.getType() });
				return method.invoke(bean);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}
		
		/** 动态执行setter方法  */
		public static Object setProperty(Object bean, String propertyName,
				Object value) {
			Class<?> clazz = bean.getClass();
			try {
				Field field = clazz.getDeclaredField(propertyName);
				Method method = clazz.getDeclaredMethod(
						getSetterName(field.getName()),
						field.getType());
				return method.invoke(bean, convertWrapClass(value, field.getType()));
			} catch (NoSuchFieldException ex) {
				// no property field
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}
		
		/** 转换为对应的基本数据类型 */
		private static Object convertWrapClass(Object value, Class<?> type) {
			if (value==null || value.toString().length()<=0) {
				if(isNumber(type)) return 0;
				else return null;
			} else {
				if ((type == Byte.class) || (type == Byte.TYPE)) return Byte.valueOf(value.toString());
				else if ((type == Short.class) || (type == Short.TYPE)) return Short.valueOf(value.toString());
				else if ((type == Integer.class) || (type == Integer.TYPE)) return Integer.valueOf(value.toString());
				else if ((type == Long.class) || (type == Long.TYPE)) return Long.valueOf(value.toString());
				else if ((type == Float.class) || (type == Float.TYPE)) return Float.valueOf(value.toString());
				else if ((type == Double.class) || (type == Double.TYPE)) return Double.valueOf(value.toString());
				else if ((type == Boolean.class) || (type == Boolean.TYPE)) return Boolean.valueOf(value.toString());
				else if (type == String.class) return value.toString();
				else return value;
			}
		}
		
		/** 根据类型动态反射填充数据  */
		public static void setField(Object bean, Field field, String value) {
			String fieldName = field.getName(); //属性名称
			Class<?> clazz = field.getType(); //属性类型
			try {
				//泛型集合类型
				if (isCollection(clazz)) { 
					Class<?> cls = null;
					Type gType = field.getGenericType();
					if (gType instanceof ParameterizedType) {
						ParameterizedType ptype = (ParameterizedType) gType;
						Type[] targs = ptype.getActualTypeArguments();
						if (targs != null && targs.length > 0) {
							Type t = targs[0];
							cls = (Class<?>) t;
						}
					}
					JSONArray mjson = new JSONArray(value);
					Object obj = parseCollection(mjson, clazz, cls);
					if(obj!=null) {
						setProperty(bean, fieldName, obj);
					}
				} else if (isArray(clazz)) {
					//数组类型
					Class<?> cls= clazz.getComponentType();
					JSONArray mjson = new JSONArray(value);
					Object obj = parseArray(mjson, cls);
					if(obj!=null) {
						setProperty(bean, fieldName, obj);
					}
				} else if (isObject(clazz)) { 
					//类/接口引用数据类型
					JSONObject mjson = new JSONObject(value);
					Object obj = parseObject(mjson, clazz);
					if(obj!=null) {
						setProperty(bean, fieldName, obj);
					}
				} else if (isSingle(clazz)) { 
					// 基本数据类型直接填充
					setProperty(bean, fieldName, value);
				} else {
					throw new RuntimeException("json object is unknow type");
				}
			} catch(JSONException ex) {
				ex.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			} 
		}
	}
}
