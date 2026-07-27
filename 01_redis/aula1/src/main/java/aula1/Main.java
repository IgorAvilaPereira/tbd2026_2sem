package aula1;

import java.util.Iterator;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Main {
    public static void main(String[] args) {
        JedisPool pool = new JedisPool("localhost", 6379);
        try (Jedis jedis = pool.getResource()) {
            jedis.select(0);
                        jedis.setex("clientName2", 5, "Jedis");

            Iterator<String> iterator = jedis.keys("*").iterator();
            while(iterator.hasNext()){
                System.out.println(jedis.get(iterator.next()));
            }
            // System.out.println(jedis.get("clientName"));
        }
    }
}