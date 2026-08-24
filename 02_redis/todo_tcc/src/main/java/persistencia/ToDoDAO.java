package persistencia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import negocio.LocalDateTypeAdapter;
import negocio.ToDo;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class ToDoDAO {
    private Gson gson;
    private static final String HOST = "localhost";
    private static final int PORT = 6379;

    public ToDoDAO(){
    //  -- se for LocalDate
this.gson = new GsonBuilder()
.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
    .create();
    }

     public List<ToDo> listarPorPrioridade(){
        List<ToDo> veToDo = new ArrayList<ToDo>();
        JedisPool pool = new JedisPool(HOST, PORT);
        try (Jedis jedis = pool.getResource()) {
            jedis.select(1);
            List<String> keys = jedis.zrevrange("ranking:prioridade", 0, -1);
            Iterator<String> iterator = keys.iterator();
            jedis.select(0);
            while (iterator.hasNext()) {
                String key = iterator.next();
                if (jedis.exists(key)) {
                    ToDo todo = gson.fromJson(jedis.get(key), ToDo.class);
                    if (todo.getArquivo() != null) todo.setBase64(Base64.getEncoder().encodeToString(todo.getArquivo()));
                    veToDo.add(todo);
                    
    

                }   
            }             
        }
        pool.close();
        return veToDo;

    }

    public List<ToDo> listar(){
        List<ToDo> veToDo = new ArrayList<ToDo>();
        JedisPool pool = new JedisPool(HOST, PORT);
        try (Jedis jedis = pool.getResource()) {
            Set<String> vetUUID = jedis.keys("*");
            Iterator<String> iterator = vetUUID.iterator();
            while (iterator.hasNext()) {
                veToDo.add(gson.fromJson(jedis.get(iterator.next()), ToDo.class));
            }
        }
        pool.close();
        return veToDo;

    }

    public ToDo obter(String uuid) {
        ToDo toDo = new ToDo();
        JedisPool pool = new JedisPool(HOST, PORT);
        try (Jedis jedis = pool.getResource()) {
            try {
                toDo = gson.fromJson(jedis.get(uuid), ToDo.class);
            } catch (Exception e) {
                return null;
            }
        }
        pool.close();
        return toDo;
    }

    public void salvar(ToDo toDo, int segundos) {
        JedisPool pool = new JedisPool(HOST, PORT);
        try (Jedis jedis = pool.getResource()) {
            if (segundos > 0) {
                jedis.setex(toDo.getId().toString(), segundos, gson.toJson(toDo));
            } else {
                this.salvar(toDo);
            }
            jedis.select(1);
            jedis.zadd("ranking:prioridade", Double.parseDouble(String.valueOf(toDo.getPrioridade())), toDo.getId().toString());

        }
        pool.close();

    }

    public void salvar(ToDo toDo){  
        JedisPool pool = new JedisPool(HOST, PORT);
        try (Jedis jedis = pool.getResource()) {
            jedis.set(toDo.getId().toString(), gson.toJson(toDo));
        }
        pool.close();
    }

    public void deletar(String uuid) {
        JedisPool pool = new JedisPool(HOST, PORT);
        try (Jedis jedis = pool.getResource()) {
            jedis.select(0);
            jedis.del(uuid);
            jedis.select(1);
            jedis.del(uuid);
            jedis.select(0);
        }
        pool.close();
    }

}
