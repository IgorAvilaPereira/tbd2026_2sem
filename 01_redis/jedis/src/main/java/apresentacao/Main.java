package apresentacao;

import java.time.LocalDate;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import negocio.Endereco;
import negocio.Pessoa;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import util.LocalDateTypeAdapter;

public class Main {
    public static void main(String[] args) {
        // Gson gson = new Gson();
        Gson gson = new GsonBuilder()
.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
    .create();

        JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");


        /// Jedis implements Closeable. Hence, the jedis instance will be auto-closed after the last statement.
        try (Jedis jedis = pool.getResource()) {
            jedis.select(1);

            Pessoa igor = new Pessoa();
            igor.setCpf("11111111111");
            igor.setNome("Igor");
            Endereco enderecoDoIgor = new Endereco();
            enderecoDoIgor.setRua("alfredo huch");
            igor.setEndereco(enderecoDoIgor);
            igor.setDataNascimento(LocalDate.of(1987, 01, 20));

            // /// ... do stuff here ... for example
            // jedis.setex(igor.getCpf(), 10, gson.toJson(igor));
            jedis.set(igor.getId(), gson.toJson(igor));
            // jedis.set(igor.getId(), igor.hsetFormat());


            // igor = gson.fromJson(jedis.get("11111111111"), Pessoa.class);
            // System.out.println(igor.getNome());
            
            // String foobar = jedis.get("foo");
            // jedis.zadd("sose", 0, "car"); jedis.zadd("sose", 0, "bike"); 
            // Set<String> sose = jedis.zrange("sose", 0, -1);
        }
        /// ... when closing your application:
        pool.close();
    }
}