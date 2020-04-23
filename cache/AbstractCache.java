package cache;

import java.util.Date;
import java.util.HashMap;

public abstract class AbstractCache<K, V> {
	HashMap<K, V> cacheMap;
	HashMap<K, Date> overTimeMap;
	final int MAX_SIZE;
	{
		overTimeMap = new HashMap<K, Date>();
	}
	public AbstractCache(int maxSize){
		MAX_SIZE = maxSize;
	}
	/**
	 * 返回当前存储元素个数
	 * @return 当前存储元素个数
	 */
	public int size() {
		return cacheMap.size();
	}
	/**
	 * 存储一个键值对
	 * @param key 存入的key
	 * @param value 存入的value
	 */
	public void put(K key, V value) {
		if(cacheMap.size() == MAX_SIZE){
			out();
		}
		cacheMap.put(key, value);
	}
	/**
	 * 设置过期时间
	 * @param key 被设置过期时间的key
	 * @param overTime 过期时间
	 * @return 设置成功返回true; 不包含该key返回false
	 */
	public boolean setOverTime(K key, Date overTime) {
		if(! cacheMap.containsKey(key)){
			return false;
		}
		overTimeMap.put(key, overTime);
		return true;
	}
	/**
	 * delete a key-value
	 * @param key	Deleted key
	 * @return Deleted value
	 */
	public abstract V remove(K key);
	/**
	 * get a value of the key
	 * @param key	key to find
	 * @return  value found
	 */
	public abstract V get(K key);
	/**
	 * eliminate a key-value
	 * @return eliminated value
	 */
	abstract V out();
}
