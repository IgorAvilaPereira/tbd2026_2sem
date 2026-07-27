# Integração Redis com Java

O **Redis** é um armazenamento de estrutura de dados em memória, amplamente utilizado como banco de dados de baixa latência, cache distribuído e message broker. Em aplicações Java, a integração garante alta performance e escalabilidade.

---

## 1. Escolha do Cliente Redis (Drivers)

Existem três ecossistemas principais suportados oficialmente pelo Redis para aplicações Java:

*   **Jedis**: Cliente síncrono, leve e direto. Ideal para aplicações tradicionais sem fluxos reativos.
*   **Lettuce**: Cliente assíncrono baseado em Netty. É thread-safe e escalável (padrão do ecossistema Spring).
*   **Redisson**: Framework de alto nível que encapsula as estruturas de dados do Redis em interfaces nativas do Java (como `Map`, `Lock` distribuído e `Set`).

---

## 2. Abordagem 1: Java Nativo (Com Jedis)

Ideal para microsserviços leves ou projetos que não utilizam grandes frameworks de mercado.

### Dependência Maven (`pom.xml`)
```xml
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>5.1.0</version>
</dependency>
```

### Código de Implementação
```java
import redis.clients.jedis.Jedis;

public class RedisExemplo {
    public static void main(String[] args) {
        // Conexão com a instância local do Redis
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            
            // 1. Salvar chave e valor
            jedis.set("usuario:100:nome", "Ana Silva");
            
            // 2. Definir tempo de expiração (TTL) de 60 segundos
            jedis.expire("usuario:100:nome", 60);

            // 3. Recuperar informação
            String nome = jedis.get("usuario:100:nome");
            System.out.println("Usuário recuperado do cache: " + nome);
        }
    }
}
```

---

## 3. Abordagem 2: Spring Boot (Spring Data Redis)

Recomendado para ecossistemas enterprise. O Spring Boot facilita a configuração e traz suporte nativo a pool de conexões com o driver Lettuce.

### Dependência Maven (`pom.xml`)
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

### Configuração (`application.properties`)
```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

### Código: Operações Manuais com `StringRedisTemplate`
```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void salvarNoCache(String chave, String valor) {
        redisTemplate.opsForValue().set(chave, valor);
    }

    public String buscarDoCache(String chave) {
        return redisTemplate.opsForValue().get(chave);
    }
}
```

### Código: Cache Automático Declarativo (`@Cacheable`)
Ative o cache inserindo `@EnableCaching` na sua classe principal da aplicação.

```java
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    // O Spring intercepta a chamada. Se a chave existir no Redis, o método nem é executado.
    @Cacheable(value = "produtos", key = "#id")
    public Produto buscarPorId(Long id) {
        System.out.println("Dado não encontrado no cache. Buscando no Banco Relacional...");
        return produtoRepository.findById(id);
    }
}
```
