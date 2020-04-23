package cache;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class LRUCache<K, V> extends AbstractCache<K, V> {
	/**
	 * 
	 * @param maxSize cache���������
	 */
	public LRUCache(int maxSize){
		super(maxSize);
		cacheMap = new LinkedHashMap<K, V>();
	}
	/**
	 * 
	 * @param maxSize cache���������
	 * @param initSize cache�ĳ�ʼ����
	 */
	public LRUCache(int maxSize, int initSize){
		super(maxSize);
		cacheMap = new LinkedHashMap<K, V>(initSize);
	}
	/**
	 * 
	 * @param maxSize cache���������
	 * @param initSize cache�ĳ�ʼ����
	 * @param load ��������
	 */
	public LRUCache(int maxSize, int initSize, float load){
		super(maxSize);
		cacheMap = new LinkedHashMap<K, V>(initSize, load);
	}
	@Override
	public V get(K key) {
		V value = cacheMap.remove(key);
		if(! overTimeMap.containsKey(key) || overTimeMap.get(key).getTime() > System.currentTimeMillis()){	
			cacheMap.put(key, value);					//δ����
		}else{
			overTimeMap.remove(key);
		}
		return value;
	}
	@Override
	public V remove(K key) {
		V value = cacheMap.remove(key);
		overTimeMap.remove(key);
		return value;
	}
	@Override
	V out() {
		Iterator<K> it = cacheMap.keySet().iterator();
		K key = it.next();
		overTimeMap.remove(key);
		return cacheMap.remove(key);
	}
	
	

}
