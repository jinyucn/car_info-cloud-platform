package redis;

import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.Set;

public class JedisPublish {

    public static void main(String[] args){
        Jedis jedis = new Jedis("192.168.31.112",6379);
        Set<String> keys = jedis.smembers("rdb");
        Iterator<String> it = keys.iterator();
        int i=0;
        while (it.hasNext()){
            String key = it.next();
            jedis.publish("news.share",key);
            i++;
        }
        jedis.publish("news.share",i+" ");
    }
}

