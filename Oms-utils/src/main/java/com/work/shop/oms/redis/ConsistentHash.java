package com.work.shop.oms.redis;

import java.util.SortedMap;
import java.util.TreeMap;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

/**
 * 
 * 一致性hash算法TREEMAP实现
 * 
 * @author 张瑞雨
 * 
 * @param <T>
 *            节点对象
 */
public final class ConsistentHash<T> {

	private final HashFunction hashFunction;
	private final int numberOfReplicas;
	private final SortedMap<Integer, T> circle = new TreeMap<Integer, T>();

	public static final int DEFAULT_REPLICAS_COUNT = 640;

	/**
	 * 
	 * @param hashFunction
	 *            hash算法提供<code>com.google.common.hash.HashFunction</code>
	 * @param numberOfReplicas
	 *            节点放大倍数
	 * @param nodes
	 *            节点对象
	 */
	public ConsistentHash(HashFunction hashFunction, int numberOfReplicas, T[] nodes, String[] ips, String[] ports) {
		this.hashFunction = hashFunction;
		this.numberOfReplicas = numberOfReplicas;
		// for (T node : nodes) {
		// add(node);
		// }

		for (int i = 0; i < nodes.length; i++) {
			add(nodes[i], ips[i] + "-" + ports[i]);
		}
	}

	// public static <E> ConsistentHash<E> createDefaultMd5HashFunction(int
	// numberOfReplicas, E... nodes) {
	// return new ConsistentHash<E>(Hashing.md5(), numberOfReplicas, nodes);
	// }

	public static <E> ConsistentHash<E> createDefaultMd5HashFunction(E[] nodes, String[] ips, String[] ports) {
		return new ConsistentHash<E>(Hashing.md5(), DEFAULT_REPLICAS_COUNT, nodes, ips, ports);
	}

	public void add(T node, String key) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.put(hashFunction.hashString(key + i).asInt(), node);
		}
	}

	public void remove(String key) {
		for (int i = 0; i < numberOfReplicas; i++) {
			circle.remove(hashFunction.hashString(key + i).asInt());
		}
	}

	public T get(CharSequence key) {
		if (circle.isEmpty()) {
			System.out.println("null");
			return null;
		}
		int hash = hashFunction.hashString(key).asInt();
		if (!circle.containsKey(hash)) {
			SortedMap<Integer, T> tailMap = circle.tailMap(hash);
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		T t = circle.get(hash);
		return t;
	}

	public static void main(String[] args) {
//		ConsistentHash<String> consistentHash = ConsistentHash.createDefaultMd5HashFunction("node1", "node2", "node3");
//
//		HashMap<String, Integer> map = new HashMap<String, Integer>();
//		map.put("node1", 0);
//		map.put("node2", 0);
//		map.put("node3", 0);
//
//		long a = System.currentTimeMillis();
//		for (int i = 0; i < 1000000; i++) {
//			String node = consistentHash.get("user" + i);
//			int re = map.get(node);
//			map.put(node, ++re);
//		}
//		long s = System.currentTimeMillis();
//
//		Set<String> set = map.keySet();
//
//		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
//			String string = iterator.next();
//			System.out.println(string + ":" + map.get(string));
//		}
//
//		System.out.println(s - a);
	}

}
