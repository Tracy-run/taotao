package com.demo.taotao.redis.batch;

import com.demo.taotao.redis.util.RedisPoolUtil;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Jedis 工具类
 */
public class JedisUtils {


    /**
     * 获取同一前缀的 Key的value
     * @param pre
     * @return
     */
    public static String[]  KeysPre(String pre) {
        Jedis jedis = RedisPoolUtil.getInstance();
        Set<byte[]> keys = jedis.keys((pre+"_*").getBytes());
        Set<String> keystr = new HashSet<>();
        for (byte[] bytes : keys) {
            keystr.add(new String(bytes));
        }
        Object[] objects = keystr.toArray();
//        String[] objecta = (String[])keystr.toArray();
        String str[]= Arrays.copyOf(objects, objects.length, String[].class);

        RedisPoolUtil.closeConn();
        return str;
    }


    /**
     * 批量查询key的值
     * @param key
     * @return
     */
    public static String mget(String... key){
        Jedis jedis = RedisPoolUtil.getInstance();
        List<String> mget = jedis.mget(key);
        RedisPoolUtil.closeConn();
        return mget.toString();
    }


}
