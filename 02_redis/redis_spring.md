# Redis e Spring

---

## Exemplo completo: Redis persistente com Spring Boot

### 1. Dependência

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

---

### 2. Configuração (`application.properties`)

```properties
spring.redis.host=localhost
spring.redis.port=6379
# se tiver senha
# spring.redis.password=senha123
```

---

### 3. RedisTemplate com JSON

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(new ObjectMapper()));

        return template;
    }
}
```

---

### 4. Entidade simples

```java
import java.io.Serializable;

public class Usuario implements Serializable {
    private String id;
    private String nome;
    private String email;

    // getters e setters
}
```

---

### 5. Serviço para CRUD

```java
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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
        return (Usuario) redisTemplate.opsForValue().get(getChave(id));
    }

    public void deletar(String id) {
        redisTemplate.delete(getChave(id));
    }
}
```

---

### 6. Controller de exemplo

```java
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRedisService usuarioService;

    public UsuarioController(UsuarioRedisService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public void criar(@RequestBody Usuario usuario) {
        usuarioService.salvar(usuario);
    }

    @GetMapping("/{id}")
    public Usuario buscar(@PathVariable String id) {
        return usuarioService.buscar(id);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable String id) {
        usuarioService.deletar(id);
    }
}
```

