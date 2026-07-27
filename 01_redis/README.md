https://youtu.be/xGbqGyix0_0

<!--
* Matéria Nova: REDIS
* Divulgação TRABALHO 1 -> Data e Peso estão no SIGAA => assim fechamos 1 bim.

Semana Quem vem:

* Redis (aula 2) + Atendimento Trabalho

Na volta (2 bim):

* MongoDB
* Neo4j

***-->
<!--[Introdução NoSQL + REDIS](https://github.com/IgorAvilaPereira/tbd2024_2sem/raw/main/slides/introducao_nosql_redis.pdf)

[Código Aula](https://github.com/IgorAvilaPereira/tbd2024_2sem/tree/main/codigos/aula211024/renan)-->

<!--[Código Aula](https://github.com/IgorAvilaPereira/tbd2024_1sem/tree/main/codigos/redis010923)-->

<!--[Instalação](https://github.com/IgorAvilaPereira/tbd2024_2sem/wiki/Setup#redis)-->

**Instalação Ubuntu/Debian/Linux Mint**

```bash
sudo apt-get update 
sudo apt-get install redis-server
```

**Instalação Docker**

```bash
-- 1ª vez
docker run --name redis -p 6379:6379 -d redis:latest
docker exec -it redis redis-cli
-- demais vezes
sudo docker start redis
sudo docker exec -it redis redis-cli
```

**Client**

```
redis-cli
```

<!--[Material Complementar - REDIS](https://github.com/IgorAvilaPereira/tbd2024_2sem/blob/main/redis.md)-->

[Jedis](https://github.com/redis/jedis)

```xml
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>4.3.0</version>
</dependency>
```
```java
-- 1)
JedisPool pool = new JedisPool("localhost", 6379);
try (Jedis jedis = pool.getResource()) {
  jedis.set("clientName", "Jedis");
}
-- 2) 
JedisPooled jedis = new JedisPooled("localhost", 6379);
```

**Links:**

* https://www.tutorialspoint.com/redis/redis_quick_guide.htm#

* https://github.com/uglide/RedisDesktopManager

* https://redis.io/commands/


**GSON:**

**Gson**

```xml
<dependency>
  <groupId>com.google.code.gson</groupId>
  <artifactId>gson</artifactId>
  <version>2.10.1</version>
</dependency>
```

<!--[Código  Aula](https://github.com/IgorAvilaPereira/tbd2024_1sem/tree/main/codigos/RedisAula1)-->

* https://sites.google.com/site/aulasvictormenegusso/programacao-web-2-1-semestre-2017/trabalhando-com-json-com-a-biblioteca-gson

* https://github.com/google/gson

* https://github.com/google/gson/blob/master/UserGuide.md

<!--* [Código - Aula](https://github.com/IgorAvilaPereira/tbd2022_2sem/tree/main/codigos/com.mycompany.maven.jedis_maven-Jedis_jar_1.0-SNAPSHOT)-->

Se usar datas (***LocalDate*** ou ***LocalDateTime***) será preciso ***serializar*** e ***deserializar***:

1) Criar um objeto Gson com GsonBuilder:

```java
-- se for LocalDate
Gson gson = new GsonBuilder()
.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
    .create();
-- se for LocalDateTime
Gson gson = new GsonBuilder()
.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
    .create();
```
2) Construir uma classe concreta que implemente JsonSerializer e JsonDeserializer

* No caso de ser LocalDate:

```java
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.google.gson.*;

public class LocalDateTypeAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  @Override
  public JsonElement serialize(final LocalDate date, final Type typeOfSrc,
      final JsonSerializationContext context) {
    return new JsonPrimitive(date.format(formatter));
  }

  @Override
  public LocalDate deserialize(final JsonElement json, final Type typeOfT,
      final JsonDeserializationContext context) throws JsonParseException {
    return LocalDate.parse(json.getAsString(), formatter);
  }
}
```

* No caso de ser LocalDateTime:

```java
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.google.gson.*;

public class LocalDateTimeTypeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss");

  @Override
  public JsonElement serialize(LocalDateTime localDateTime, Type srcType,
      JsonSerializationContext context) {
    return new JsonPrimitive(formatter.format(localDateTime));
  }

  @Override
  public LocalDateTime deserialize(JsonElement json, Type typeOfT,
      JsonDeserializationContext context) throws JsonParseException {
    return LocalDateTime.parse(json.getAsString(), formatter);
  }
}
```
* **Fonte:** https://howtodoinjava.com/gson/gson-typeadapter-for-inaccessibleobjectexception/
<!--
**Libraries Úteis:**

* https://mvnrepository.com/artifact/com.rometools/rome

```java
<dependency>
    <groupId>com.rometools</groupId>
    <artifactId>rome</artifactId>
    <version>2.1.0</version>
</dependency>
```
-->

* https://howtodoinjava.com/gson/gson-parse-json-array/
```java
Type listType = new TypeToken<ArrayList<ArrayItem>>(){}.getType(); 
ArrayList<ArrayItem> list = gson.fromJson(jsonSource, listType);  
```

**Vídeo Complementar:**

* [Quando NÃO usar SQL?](https://www.youtube.com/watch?v=o8HEfReQ6co)

<!--
[Código](https://github.com/IgorAvilaPereira/tbd2023_2sem/tree/main/codigos/150923/080923)
[Código da aula](https://github.com/IgorAvilaPereira/tbd2024_1sem/tree/main/codigos/1508/redis1)

* https://howtodoinjava.com/gson/gson-parse-json-array/
```java
Type listType = new TypeToken<ArrayList<ArrayItem>>(){}.getType(); 
ArrayList<ArrayItem> list = gson.fromJson(jsonSource, listType);  
```
***

## 08/07 - Recapitulação/Revisão: Aula 1 - Redis

Recapitulação/Revisão: Aula 1 - Redis

Definir - Nova data atividade avaliada  1 -> dia 29/07 tudo: trabalho1 (jpa), trabalho2 (redis) + atividade avaliada presencial

[Código da aula de hoje](https://github.com/IgorAvilaPereira/tbd2024_1sem/tree/main/codigos/080724/redis1)

***

## 15/04 - Redis aula 1

[Última aula antes da suspensão do calendário](https://ifrs.edu.br/riogrande/calendario-academico-estara-suspenso-a-partir-de-16-de-abril/)
-->

**Alternativas open-source:**

* https://youtu.be/RTLkwH-PNcE?si=UKyqqZkjhPdeTphp

* https://docs.keydb.dev/

* [Código Exemplo - Keydb - igual ao Redis](https://github.com/IgorAvilaPereira/tbd2024_2sem/tree/main/codigos/keydb)

[Baixar todo o material da aula](https://download-directory.github.io/?url=http://github.com/IgorAvilaPereira/tbd2026_1sem/tree/main/./07_redis)
