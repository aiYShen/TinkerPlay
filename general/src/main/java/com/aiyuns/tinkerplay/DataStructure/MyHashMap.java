package com.aiyuns.tinkerplay.DataStructure;

import java.util.LinkedList;

/**
 * @Author: aiYunS @Date: 2023年8月15日, 下午 2:40:52 @Description: 哈希表数据结构
 */
public class MyHashMap<K, V> {
  private static final int INITIAL_CAPACITY = 16;
  private LinkedList<Entry<K, V>>[] buckets;
  private int size;

  public MyHashMap() {
    buckets = new LinkedList[INITIAL_CAPACITY];
    for (int i = 0; i < INITIAL_CAPACITY; i++) {
      buckets[i] = new LinkedList<>();
    }
    size = 0;
  }

  public void put(K key, V value) {
    int index = getIndex(key);
    LinkedList<Entry<K, V>> bucket = buckets[index];

    for (Entry<K, V> entry : bucket) {
      if (entry.key.equals(key)) {
        entry.value = value;
        return;
      }
    }

    bucket.add(new Entry<>(key, value));
    size++;

    // 扩容
    if ((double) size / buckets.length > 0.75) {
      resize();
    }
  }

  public V get(K key) {
    int index = getIndex(key);
    LinkedList<Entry<K, V>> bucket = buckets[index];

    for (Entry<K, V> entry : bucket) {
      if (entry.key.equals(key)) {
        return entry.value;
      }
    }

    return null;
  }

  public void remove(K key) {
    int index = getIndex(key);
    LinkedList<Entry<K, V>> bucket = buckets[index];

    for (Entry<K, V> entry : bucket) {
      if (entry.key.equals(key)) {
        bucket.remove(entry);
        size--;
        return;
      }
    }
  }

  private int getIndex(K key) {
    return key.hashCode() % buckets.length;
  }

  private void resize() {
    LinkedList<Entry<K, V>>[] oldBuckets = buckets;
    buckets = new LinkedList[buckets.length * 2];

    for (int i = 0; i < buckets.length; i++) {
      buckets[i] = new LinkedList<>();
    }

    for (LinkedList<Entry<K, V>> bucket : oldBuckets) {
      for (Entry<K, V> entry : bucket) {
        int index = getIndex(entry.key);
        buckets[index].add(entry);
      }
    }
  }

  private static class Entry<K, V> {
    K key;
    V value;

    public Entry(K key, V value) {
      this.key = key;
      this.value = value;
    }
  }

  public static void main(String[] args) {
    MyHashMap<String, Integer> hashMap = new MyHashMap<>();

    hashMap.put("one", 1);
    hashMap.put("two", 2);
    hashMap.put("three", 3);

    // Output: 1
    System.out.println(hashMap.get("one"));
    // Output: 2
    System.out.println(hashMap.get("two"));
    // Output: 3
    System.out.println(hashMap.get("three"));

    hashMap.remove("two");
    // Output: null
    System.out.println(hashMap.get("two"));
  }
}
