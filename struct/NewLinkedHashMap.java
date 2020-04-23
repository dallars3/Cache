package struct;

import java.util.HashMap;

import javax.security.auth.kerberos.KerberosKey;

public class NewLinkedHashMap<K, V> extends HashMap<K, V> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -731789436910128096L;


	static class Node<K> {
		K key = null;
		Node<K> pre, next;
		Node(){}
		Node(K key){
			this.key = key;
		}
	}
	private HashMap<K, Node<K>> map;
	private Node<K> head =  new Node<K>();
	private Node<K> tail =  new Node<K>();
	{
		head.next = tail;
		tail.pre = head;
	}
	public NewLinkedHashMap(){
		super();
		map = new HashMap<K, Node<K>>();
	}
	public NewLinkedHashMap(int size){
		super(size);
		map = new HashMap<K, Node<K>>(size);
	}
	public NewLinkedHashMap(int size, float load){
		super(size, load);
		map = new HashMap<K, Node<K>>(size, load);
	}
	public K getFirstKey(){
		return head.next.key;
	}
	public K getLastKey(){
		return tail.pre.key;
	}
	public K getPreKey(K key){
		return map.get(key).pre.key;
	}
	public K getNextKey(K key){
		return map.get(key).next.key;
	}
	public synchronized V add(K key, V value){
		if(! containsKey(key)){
			Node<K> node = new Node<K>(key);
			node.next = tail;
			node.pre = tail.pre;
			tail.pre.next = node;
			tail.pre = node;
			map.put(key, node);
			
		}
		return put(key, value);
	}
	public synchronized V addAfterKey(K key, V value, K preKey){
		if(! containsKey(key)){
			Node<K> node = new Node<K>(key);
			Node<K> preNode;
			if(! containsKey(preKey)){
				preNode = head;
			}else{
				preNode = map.get(preKey);
			}
			node.next = preNode.next;
			node.pre = preNode;
			preNode.next.pre = node;
			preNode.next = node;
			map.put(key, node);
			
		}
		
		return put(key, value);
	}
	public synchronized V delete(K key){
		if(! containsKey(key)){
			return null;
		}
		Node<K> node = map.remove(key);
		node.pre.next = node.next;
		node.next.pre = node.pre;
		
		return remove(key);
	}
	
	
	

}
