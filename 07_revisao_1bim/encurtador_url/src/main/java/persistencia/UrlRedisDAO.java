package persistencia;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class UrlRedisDAO {

    private JedisPool pool;

    public void salvarNoCache(String url, String urlEncurtada) {
        pool = new JedisPool(new JedisPoolConfig(), "localhost");
        try (Jedis jedis = pool.getResource()) {
            jedis.select(0);
            jedis.setex(urlEncurtada, 15, url);
        }

    }

    public String obter(String urlEncurtada) {
        long inicio = System.nanoTime();
        pool = new JedisPool(new JedisPoolConfig(), "localhost");
        String url = null;
        try (Jedis jedis = pool.getResource()) {
            jedis.select(0);
            url = jedis.get(urlEncurtada);
        }
        long fim = System.nanoTime();
        long duracaoNano = fim - inicio;
        long duracaoMillis = duracaoNano / 1_000_000; // Converte para milissegundos
        System.out.println("Tempo de execução: " + duracaoMillis + " ms");
        return url;
    }

}
