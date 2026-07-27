package apresentacao;

import java.time.LocalDateTime;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import negocio.Endereco;
import negocio.Pessoa;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import util.LocalDateTimeTypeAdapter;

public class Main {
    public static void main(String[] args) {
        JedisPool pool = new JedisPool("localhost", 6379);
        // convertendo os objetos em gson sem se preocupar com tipos de data e hora
        // Gson gson = new Gson();

        // para trabalhar com data/hora e continuar convertendo os objetos em gson
        Gson gson = new GsonBuilder()
.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
    .create();

        try (Jedis jedis = pool.getResource()) {
            // deletar
            Pessoa pessoa = new Pessoa(); 
            pessoa.setNome("Igor");
            pessoa.setNroJoelho(1);
            Endereco endereco = new Endereco();
            endereco.setRua("alfredo huch");
            pessoa.setEndereco(endereco);
            pessoa.setDataCirurgia(LocalDateTime.of(2025, 4, 8, 8, 0));
            // pessoa = gson.fromJson(jedis.get("63b58476-d87c-4e2b-8db7-a592da79eda0"), Pessoa.class);
                      
            // salvamento
            // Pessoa pessoa = new Pessoa();
            jedis.set(pessoa.getId(), gson.toJson(pessoa));
            System.out.println(pessoa.getId());

            Iterator<String> iterator = jedis.keys("*").iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
                
            }
        }
        pool.close();
    }
}