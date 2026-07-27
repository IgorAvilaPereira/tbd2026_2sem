# Redis e Spring - Cache

## 1. Dependência

Se você usa **Spring Boot**, basta adicionar no seu `pom.xml` (Maven):

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

Ou no **Gradle**:

```gradle
implementation 'org.springframework.boot:spring-boot-starter-data-redis'
```

---

## 2. Configuração

No `application.properties` (ou `application.yml`), configure a conexão com o Redis:

```properties
spring.redis.host=localhost
spring.redis.port=6379
```

Se tiver senha:

```properties
spring.redis.password=suasenha
```

---

## 3. RedisTemplate

Você pode injetar o `RedisTemplate` ou o `StringRedisTemplate` para manipular dados:

```java
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void salvar(String chave, String valor) {
        redisTemplate.opsForValue().set(chave, valor);
    }

    public String buscar(String chave) {
        return redisTemplate.opsForValue().get(chave);
    }
}
```

---

## 4. Cache com Redis

Se quiser usar como **cache**, habilite caching no projeto:

```java
@EnableCaching
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

E configure o `CacheManager`:

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

@Configuration
public class RedisCacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)); // TTL padrão
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(config).build();
    }
}
```

Depois, basta usar:

```java
@Cacheable("usuarios")
public Usuario buscarUsuario(Long id) {
    // consulta ao banco
}
```

