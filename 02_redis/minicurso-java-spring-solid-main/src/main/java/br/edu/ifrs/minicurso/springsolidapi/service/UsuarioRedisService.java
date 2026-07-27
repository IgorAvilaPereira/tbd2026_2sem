package br.edu.ifrs.minicurso.springsolidapi.service;

import br.edu.ifrs.minicurso.springsolidapi.infrastructure.RedisCacheConfig;
import br.edu.ifrs.minicurso.springsolidapi.model.Usuario;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UsuarioRedisService {
    
    private final RedisTemplate<String, Object> redisTemplate;

    public UsuarioRedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String getChave(String id) {
        return "usuario:" + id;
    }

    public void salvar(Usuario usuario) {
        redisTemplate.opsForValue().set(getChave(usuario.getId()), usuario);
        
    }

    public Usuario buscar(String id) {
        try {
            // System.out.println("----------------------");
            // System.out.println(redisTemplate.opsForValue().get(getChave(id)));
            // System.out.println("------------------------");
            Object obj = redisTemplate.opsForValue().get(getChave(id));
            ObjectMapper mapper = new ObjectMapper();
            Usuario u = mapper.convertValue(obj, Usuario.class);
            return u;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void deletar(String id) {
        redisTemplate.delete(getChave(id));
    }

    public List<Usuario> listar() {
        Iterator<String> iterator = redisTemplate.keys("*").iterator();
        List<Usuario> vetUsuario = new ArrayList<>();
        while (iterator.hasNext()) {
            Object obj = redisTemplate.opsForValue().get(iterator.next());
            ObjectMapper mapper = new ObjectMapper();
            Usuario u = mapper.convertValue(obj, Usuario.class);
            vetUsuario.add(u);
        }
        return vetUsuario;
    }

    public void salvarNoCache(int minutos, Usuario usuario) {
        redisTemplate.opsForValue().set(getChave(usuario.getId()), usuario, 10, TimeUnit.SECONDS);

    }
}
