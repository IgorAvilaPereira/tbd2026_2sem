# Redis

## 1. Configuração (`application.properties`)

```properties
spring.redis.host=localhost
spring.redis.port=6379
```

---

## 2. Serviço usando Redis direto

```java
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Salvar valor simples
    public void salvar(String chave, String valor) {
        redisTemplate.opsForValue().set(chave, valor);
    }

    // Buscar valor
    public String buscar(String chave) {
        return redisTemplate.opsForValue().get(chave);
    }

    // Excluir
    public void deletar(String chave) {
        redisTemplate.delete(chave);
    }
}
```

---

## 3. Usando objetos (não só String)

Se quiser salvar **objetos JSON**, pode configurar um `RedisTemplate<String, Object>` com serializer Jackson:

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

E no serviço:

```java
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UsuarioRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public UsuarioRedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void salvar(String chave, Usuario usuario) {
        redisTemplate.opsForValue().set(chave, usuario);
    }

    public Usuario buscar(String chave) {
        return (Usuario) redisTemplate.opsForValue().get(chave);
    }
}
```

