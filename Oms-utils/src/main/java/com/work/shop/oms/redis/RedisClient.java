package com.work.shop.oms.redis;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.util.SafeEncoder;

/**
 * redis客户端
 * 
 * @author 张瑞雨
 * 
 */
public class RedisClient {

	private String password = null;

	private volatile boolean isInitial = false;

	private ConsistentHash<JedisPool> consistentHash;

	private JedisPool[] jedisPool;

	private String[] ip;

	private String[] port;

	private Integer dbIndex = 7;

	private Logger logger = Logger.getLogger(RedisClient.class);

	public String[] getIp() {
		return ip;
	}

	public String[] getPort() {
		return port;
	}

	public void setIp(String[] ip) {
		this.ip = ip;
	}

	public void setPort(String[] port) {
		this.port = port;
	}

	private static ThreadLocal<JedisPool> threadLocalJedis = new ThreadLocal<JedisPool>();
	private static ThreadLocal<Integer> count = new ThreadLocal<Integer>();

	public synchronized void initial() {
		if (!isInitial) {
			consistentHash = ConsistentHash.createDefaultMd5HashFunction(jedisPool, ip, port);
			isInitial = true;
		}
	}

	public Jedis getJedis(String key) {
		threadLocalJedis.remove();
		initial();
		JedisPool pool = consistentHash.get(key);
		threadLocalJedis.set(pool);
		Jedis jedis = pool.getResource();
		if (jedis == null) {
			Integer i = count.get();
			System.out.println(i);
			i = i == null ? 0 : i;
			if (i > 5)
				return null;
			i++;
			count.set(i);
			jedis = getJedis(key);
		}
		if (!StringUtils.isEmpty(password)) {
			jedis.auth(password);
		}
//		logger.debug("dbIndex" + dbIndex);
		jedis.select(dbIndex);
		return jedis;
	}

	public String setex(String key, int seconds, byte[] value) {
		// Jedis jedis = null;
		// try {
		// jedis = getJedis(key);
		// return jedis.setex(key.getBytes(), seconds, value);
		// } finally {
		// close(jedis);
		// }
		return this.set(key, value);
	}

	public Long del(String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis(key);
			return jedis.del(key.getBytes());
		} finally {
			close(jedis);
		}
	}

	public boolean exists(String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis(key);
			return jedis.exists(key);
		} finally {
			close(jedis);
		}
	}

	public String set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis(key);
			return jedis.set(key, value);
		} finally {
			close(jedis);
		}
	}

	public String set(String key, byte[] value) {
		Jedis jedis = null;
		try {
			jedis = getJedis(key);
			return jedis.set(key.getBytes(), value);
		} finally {
			close(jedis);
		}
	}

	public void close(Jedis jedis) {
		JedisPool pool = threadLocalJedis.get();
		if (pool != null) {
			pool.returnResource(jedis);
		}
	}

	public String get(String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis(key);
			return jedis.get(key);
		} finally {
			close(jedis);
		}
	}

	public byte[] get(byte[] buffer) {
		Jedis jedis = null;
		try {
			jedis = getJedis(new String(buffer));
			return jedis.get(buffer);
		} finally {
			close(jedis);
		}
	}

	public String lpop(String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis(key);
			return jedis.lpop(key);
		} finally {
			close(jedis);
		}
	}

	public Long lpush(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = getJedis(key);
			return jedis.lpush(key, value);
		} finally {
			close(jedis);
		}
	}
	

	public void expire(String key, int i) {
		Jedis jedis = null;
		try {
			jedis = getJedis(key);
			jedis.expire(key, i);
		} finally {
			close(jedis);
		}
	}
	

	public long setnx(String key, String orderOutSn) {
		Jedis jedis = null;
		try {
			jedis = getJedis(key);
			return jedis.setnx(key, orderOutSn);
		} finally {
			close(jedis);
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public JedisPool[] getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool[] jedisPool) {
		this.jedisPool = jedisPool;
	}

	public Integer getDbIndex() {
		return dbIndex;
	}

	public void setDbIndex(Integer dbIndex) {
		this.dbIndex = dbIndex;
	}
	
	public <T extends Serializable> String setPojo(String key, T value) {
		Jedis jedis = null;
		try {
			jedis = getJedis(key);
			return jedis.set(getKeyByte(key), RedisUtil.encode(value));
		} finally {
			if (null != jedis)
				close(jedis);
		}
	}
	
	public <T extends Serializable> T getPojo(String key) {
		Jedis jedis = null;
		try {
			jedis = getJedis(key);
			byte[] value = jedis.get(getKeyByte(key));
			if (null == value) {
				return null;
			}
			return RedisUtil.decode(value);
		} finally {
			if (null != jedis)
				close(jedis);
		}
	}
	
	public <T extends Serializable> T spopPojo(String key)
	{
		Jedis jedis = null;
		try {
			jedis = getJedis(key);
			byte[] result = jedis.spop(getKeyByte(key));
			if ((result != null) && (result.length > 0)) {
				return RedisUtil.decode(result);
			}
			return null;
		} finally {
			if (null != jedis)
				close(jedis);
		}
	}
	
	public <T extends Serializable> String setExPojo(String key, int seconds, T value) {
		Jedis redis = null;
		try {
			redis = getJedis(key);
			return redis.setex(getKeyByte(key), seconds, RedisUtil.encode(value));
		} finally {
			if (null != redis)
				close(redis);
		}
	}
	
	private byte[] getKeyByte(String key)
	{
		return SafeEncoder.encode(key);
	}
	
	public Long delKey(String key) {
		Jedis redis = null;
		try {
			redis = getJedis(key);
			return redis.del(new String[] { key });
		} finally {
			if (null != redis)
				close(redis);
		}
	}

	public Long inc(String key) {
		Jedis redis = null;
		try {
			redis = getJedis(key);
			return redis.incr(key);
		} finally {
			if (null != redis)
				close(redis);
		}
	}

	public <T extends Serializable> String hmset(String key, Map<String, String> hash)
	{
		Jedis jedis = null;
		try {
			jedis = getJedis(key);
			return jedis.hmset(key, hash);
		} finally {
			if (null != jedis)
				close(jedis);
		}
	}
	
	public <T extends Serializable> Map<String, String> hgetAll(String key)
	{
		Jedis jedis = null;
		try {
			jedis = getJedis(key);
			return jedis.hgetAll(key);
		} finally {
			if (null != jedis)
				close(jedis);
		}
	}
}
