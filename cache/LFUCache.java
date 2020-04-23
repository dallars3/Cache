package cache;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import struct.*;
import java.lang.Math;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class LFUCache<K, V> extends AbstractCache<K, V> {
	NewLinkedHashMap<K, Integer> LFUMap;
	final int BASE;
	Set<K> decreaseSet;
	Random random;
	{
		random = new Random(System.currentTimeMillis());
		LFUMap = new NewLinkedHashMap<K, Integer>();
		
	}
	/**
	 * @param maxSize	cache的最大容量
	 * @param base	与freq升级有关的影响因子
	 */
	public LFUCache(int maxSize, int base){
		super(maxSize);
		cacheMap = new HashMap<K, V>();
		BASE = base;
	}
	/**
	 * @param maxSize	cache的最大容量
	 * @param base	与freq升级有关的影响因子
	 * @param initSize 初始容量
	 */
	public LFUCache(int maxSize, int initSize, int base){
		super(maxSize);
		cacheMap = new HashMap<K, V>(initSize);
		BASE = base;
	}
	/**
	 * @param maxSize	cache的最大容量
	 * @param base	与freq升级有关的影响因子
	 * @param initSize 初始容量
	 * @param load	负载因子
	 */
	public LFUCache(int maxSize, int initSize, float load, int base){
		super(maxSize);
		cacheMap = new HashMap<K, V>(initSize, load);
		BASE = base;
	}
	/**
	 * 启动freq的递减器。每个元素如果在period时间内没有被命中过，其freq - 1
	 * @param delay 启动的延时
	 * @param period	在period时间内没有被命中过，触发递减
	 */
	public void startDecrease(long delay, long period){
		synchronized (LFUMap) {
			decreaseSet = new HashSet<K>(LFUMap.keySet());
		}
		new Timer("decrease").schedule(new TimerTask() {
            @Override
            public void run() {
            	Iterator<K> it = decreaseSet.iterator();
            	while(it.hasNext()){
            		K key = it.next();
            		int freq = LFUMap.get(key);
            		if(freq > 0){
            			freq --;
            		}
            		K nextKey = LFUMap.getNextKey(key);
        			LFUMap.delete(key);
        			while(nextKey  != null){
        				if(LFUMap.get(nextKey) < freq){
        					LFUMap.addAfterKey(key, freq, LFUMap.getPreKey(nextKey));
        				}
        				nextKey = LFUMap.getNextKey(nextKey);
        			}
        			if(nextKey == null){
        				LFUMap.add(key, freq);
        			}
            	}
            	synchronized (LFUMap) {
            		decreaseSet = new HashSet<K>(LFUMap.keySet());
				}
            	
            }
            
        }, delay, period);
	}
	
	@Override
	public V get(K key) {
		if(decreaseSet != null){
			decreaseSet.remove(key);
		}
		V value = cacheMap.get(key);
		if(! overTimeMap.containsKey(key) || overTimeMap.get(key).getTime() > System.currentTimeMillis()){
			increaseFreq(key);
		}else{
			overTimeMap.remove(key);
			cacheMap.remove(key);
		}
		
		return value;
	}
	private void increaseFreq(K key){
		if(! LFUMap.containsKey(key)){
			LFUMap.add(key, 0);
		}
		int freq = LFUMap.get(key);
		int range = BASE * (freq + 1);
		if(random.nextInt(range) == 0){
			freq ++;
			K preKey = LFUMap.getPreKey(key);
			LFUMap.delete(key);
			while(preKey  != null){
				if(LFUMap.get(preKey) > freq){
					LFUMap.addAfterKey(key, freq, preKey);
				}
				preKey = LFUMap.getPreKey(preKey);
			}
			if(preKey == null){
				LFUMap.addAfterKey(key, freq, preKey);
			}
		}
		
	}
	@Override
	public V remove(K key) {
		V value = cacheMap.remove(key);
		overTimeMap.remove(key);
		LFUMap.delete(key);
		return value;
	}
	
	@Override
	V out() {
		K key = LFUMap.getLastKey();
		overTimeMap.remove(key);
		LFUMap.delete(key);
		return cacheMap.remove(key);
	}

	
}
