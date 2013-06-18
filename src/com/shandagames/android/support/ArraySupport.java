package com.shandagames.android.support;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/** 封装常用的数组操作  */
public class ArraySupport {

	private ArraySupport() {
		throw new AssertionError("You are trying to create an instance for this utility class!");
	}
	
	public static <T> List<T> arrayToList(T[] array) {
		if (array == null) {
			return null;
		}
		return Arrays.asList(array);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] listToArray(List<T> list) {
		if (list == null) {
			return null;
		}
		Class<?> type = list.getClass().getComponentType();
		T[] result = (T[])Array.newInstance(type, list.size());
		return list.toArray(result);
	}
	
    @SuppressWarnings("unchecked")
    public static <T> T[] join(T[] head, T[] tail) {
        if (head == null) {
            return tail;
        }
        if (tail == null) {
            return head;
        }
        Class<?> type = head.getClass().getComponentType();
        T[] result = (T[]) Array.newInstance(type, head.length + tail.length);

        System.arraycopy(head, 0, result, 0, head.length);
        System.arraycopy(tail, 0, result, head.length, tail.length);

        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] delete(T[] array, int index) {
        int length = array.length;
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
        }

        T[] result = (T[]) Array.newInstance(array.getClass().getComponentType(), length - 1);
        System.arraycopy(array, 0, result, 0, index);
        if (index < length - 1) {
            System.arraycopy(array, index + 1, result, index, length - index - 1);
        }

        return result;
    }

    /**
     * Attempts to find an object in a potentially unsorted array. Complexity is
     * O(N).
     * 
     * @param <T>
     * @param array
     *        the input array
     * @param object
     *        the object to find
     * @return the index of the object if found, -1 otherwise.
     */
    public static <T> int find(T[] array, T object) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(object)) {
                return i;
            }
        }
        return -1;
    }
    

	public static <T> boolean contains(final T[] array, final T value) {
		if (array == null) return false;
		for (final T item : array) {
			if (item == value) return true;
		}
		return false;
	}

	public static <T> boolean contains(final T[] array, final T... values) {
		if (array == null || values == null) return false;
		for (final T item : array) {
			for (final T value : values) {
				if (item == null || value == null) {
					if (item == value) return true;
					continue;
				}
				if (item.equals(value)) return true;
			}
		}
		return false;
	}

	/** 比较两个数组是否相匹配   */
	public static <T> boolean contentMatch(final T[] array1, final T[] array2) {
		if (array1 == null || array2 == null) return array1 == array2;
		if (array1.length != array2.length) return false;
		final int length = array1.length;
		for (int i = 0; i < length; i++) {
			if (!contains(array2, array1[i])) return false;
		}
		return true;
	}
	
	public static <T> int indexOf(final T[] array, final T value) {
		final int length = array.length;
		for (int i = 0; i < length; i++) {
			if (array[i] == value) return i;
		}
		return -1;
	}

}
