package persistencia;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import negocio.Nota;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import util.LocalDateTimeTypeAdapter;

public class NotaDAO {

    private Gson gson;
    private static final String HOST = "localhost";
    private static final int PORT = 6379;
    private static final int INDEX = 0;
    private static final int SEGUNDOS_NO_CACHE = 10;

    public NotaDAO(){
        // this.gson = new Gson();
        this.gson = new GsonBuilder()
.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
    .create();
    }

    public ArrayList<Nota> listar(){
        ArrayList<Nota> vetNota = new ArrayList<Nota>();
         try (JedisPool pool = new JedisPool(HOST, PORT)) {
            try (Jedis jedis = pool.getResource()) {
                jedis.select(INDEX);
                Iterator<String> iterator = jedis.keys("*").iterator();
                while (iterator.hasNext()) {
                    vetNota.add(this.gson.fromJson(jedis.get(iterator.next()), Nota.class));
                }
            }
            return vetNota;
        }
    }

    public void deletar(UUID id) {
        try (JedisPool pool = new JedisPool(HOST, PORT)) {
            try (Jedis jedis = pool.getResource()) {
                jedis.select(INDEX);
                jedis.del(id.toString());
            }
        }
    }

    public void salvar(Nota nota){
        try (JedisPool pool = new JedisPool(HOST, PORT)) {
            try (Jedis jedis = pool.getResource()) {
                jedis.select(INDEX);
                jedis.set(nota.getId().toString(), this.gson.toJson(nota));
            }
        }
    }

    public void deletarTodos() {
        this.listar().forEach(p -> this.deletar(p.getId()));
    }

    public Nota buscar(UUID id) {
        Nota nota = new Nota();
        nota.setId(null);
         try (JedisPool pool = new JedisPool(HOST, PORT)) {
            try (Jedis jedis = pool.getResource()) {
                // tenta cache
                jedis.select(1);
                nota = gson.fromJson(jedis.get(id.toString()), Nota.class);
                // n ta no cache
                if (nota == null){
                    jedis.select(INDEX);
                    nota = this.gson.fromJson(jedis.get(id.toString()), Nota.class);                    
                    if (nota.getId() != null) {                    
                        jedis.select(1);
                        jedis.setex(id.toString(), SEGUNDOS_NO_CACHE, this.gson.toJson(nota));
                    } else {
                        // tentei buscar uma chave que n existe
                        return null;
                    }

                } else {
                    System.out.println("vindo do cache");
                    return nota;
                }
            }
            return nota;
        }
    }

}
