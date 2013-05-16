package com.shandagames.android.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/***
 * An LRU cache which stores recently inserted entries and all entries ever
 * inserted which still has a strong reference elsewhere.
 */
public class LruCache<K, V> {

    private final HashMap<K, V> mLruMap;
    private final HashMap<K, Entry<K, V>> mWeakMap = new HashMap<K, Entry<K, V>>();
    // 记录引用对象已被垃圾回收器回收过的Entry
    private ReferenceQueue<V> mQueue = new ReferenceQueue<V>();

    // 记录引用对象已被垃圾回收器回收过的Entry
    @SuppressWarnings("serial")
	public LruCache(final int capacity) {
        mLruMap = new LinkedHashMap<K, V>(32, 0.75f, true) {
        	// LRU算法最关键的一步，自动清除过期数据,始终保证mLruMap的size<=capacity，即mLruMap内存占用总大小<=单个对象所占内存*capacity
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > capacity;
            }
        };
    }

    private static class Entry<K, V> extends WeakReference<V> {
        K mKey; //保存Key值，以便mWeakMap删除已被垃圾回收器回收过的弱引用对象Entry

        public Entry(K key, V value, ReferenceQueue<V> queue) {
            super(value, queue);
            mKey = key;
        }
    }

    // 清空mWeakMap里已被垃圾回收器回收过的Entry
    // 表明Entry所引用的对象已经被垃圾回收器回收，这时需要清除Entry对象本身
    @SuppressWarnings("unchecked")
    private void cleanUpWeakMap() {
        Entry<K, V> entry = (Entry<K, V>) mQueue.poll();
        while (entry != null) {
        	// 一旦垃圾回收器回收该Entry所引用的对象，就从mWeakMap里删除该Entry
            mWeakMap.remove(entry.mKey);
            entry = (Entry<K, V>) mQueue.poll();
        }
    }

    // 将对象放入缓存，并保存弱引用
    public synchronized V put(K key, V value) {
        cleanUpWeakMap();
        mLruMap.put(key, value);
        Entry<K, V> entry = mWeakMap.put(
                key, new Entry<K, V>(key, value, mQueue));
        return entry == null ? null : entry.get();
    }

    // 取得对象
    public synchronized V get(K key) {
        cleanUpWeakMap();
        V value = mLruMap.get(key);
        if (value != null) return value;
        Entry<K, V> entry = mWeakMap.get(key);
        return entry == null ? null : entry.get();
    }

    // 清空
    public synchronized void clear() {
        mLruMap.clear();
        mWeakMap.clear();
        mQueue = new ReferenceQueue<V>();
    }
}
