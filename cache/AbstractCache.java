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
	 * ���ص�ǰ�洢Ԫ�ظ���
	 * @return ��ǰ�洢Ԫ�ظ���
	 */
	public int size() {
		return cacheMap.size();
	}
	/**
	 * �洢һ����ֵ��
	 * @param key �����key
	 * @param value �����value
	 */
	public void put(K key, V value) {
		if(cacheMap.size() == MAX_SIZE){
			out();
		}
		cacheMap.put(key, value);
	}
	/**
	 * ���ù���ʱ��
	 * @param key �����ù���ʱ���key
	 * @param overTime ����ʱ��
	 * @return ���óɹ�����true; ��������key����false
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
