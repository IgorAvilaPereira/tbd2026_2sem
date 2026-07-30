# Aplicação: Sistema de Biblioteca Redis (versão aula 1h30)

## Objetivo

Criar uma aplicação Java que gerencia livros utilizando Redis como banco NoSQL.

Os alunos irão implementar:

* cadastro de livros;
* consulta;
* controle de visualizações;
* ranking dos livros mais acessados;
* histórico simples de consultas.

---

# Redis utilizado

| Conceito Redis | Comando        | Uso na aplicação        |
| -------------- | -------------- | ----------------------- |
| String         | SET / GET      | Armazenar dados simples |
| Hash           | HSET / HGETALL | Cadastro dos livros     |
| Expiração      | EXPIRE / TTL   | Cache                   |
| Incremento     | INCR           | Contador de acessos     |
| Sorted Set     | ZADD / ZRANGE  | Ranking                 |
| Lista          | LPUSH / LRANGE | Histórico               |

---

# Estrutura Redis

## Livro (HASH)

Cada livro será armazenado como:

```
livro:{id}
```

Exemplo:

```
livro:1
```

Dados:

```
titulo = Clean Code
autor = Robert Martin
ano = 2008
```

Redis:

```redis
HSET livro:1 titulo "Clean Code"
HSET livro:1 autor "Robert Martin"
HSET livro:1 ano "2008"
```

---

# Funcionalidade 1 - Cadastrar Livro (20 min)

## Java + JEDIS

```java
Jedis jedis = new Jedis("localhost",6379);


jedis.hset(
    "livro:1",
    "titulo",
    "Clean Code"
);


jedis.hset(
    "livro:1",
    "autor",
    "Robert Martin"
);


jedis.hset(
    "livro:1",
    "ano",
    "2008"
);
```

---

# Funcionalidade 2 - Consultar Livro (10 min)

Buscar todos os dados:

Redis:

```redis
HGETALL livro:1
```

Java:

```java
Map<String,String> livro =
    jedis.hgetAll("livro:1");


System.out.println(livro);
```

---

# Funcionalidade 3 - Cache da Consulta (15 min)

Quando consultar um livro:

Primeiro verifica:

```
cache:livro:1
```

Se não existir:

* busca no Redis Hash;
* grava cache.

Criar cache:

```redis
SETEX cache:livro:1 60 "Clean Code"
```

Java:

```java
jedis.setex(
 "cache:livro:1",
 60,
 "Clean Code"
);
```

Consultar tempo:

```redis
TTL cache:livro:1
```

---

# Funcionalidade 4 - Contador de acessos (15 min)

Cada consulta aumenta o contador.

Chave:

```
acessos:livro:1
```

Redis:

```redis
INCR acessos:livro:1
```

Java:

```java
jedis.incr(
 "acessos:livro:1"
);
```

Consultar:

```redis
GET acessos:livro:1
```

---

# Funcionalidade 5 - Ranking de livros (15 min)

Usar Sorted Set.

Chave:

```
ranking_livros
```

Cada acesso aumenta a pontuação:

Redis:

```redis
ZINCRBY ranking_livros 1 livro:1
```

Java:

```java
jedis.zincrby(
 "ranking_livros",
 1,
 "livro:1"
);
```

Mostrar ranking:

```redis
ZREVRANGE ranking_livros 0 5 WITHSCORES
```

Java:

```java
jedis.zrevrangeWithScores(
 "ranking_livros",
 0,
 5
);
```

---

# Funcionalidade 6 - Histórico de consultas (10 min)

Guardar últimos livros acessados.

Lista:

```
historico:usuario:1
```

Adicionar:

```redis
LPUSH historico:usuario:1 livro:1
```

Java:

```java
jedis.lpush(
 "historico:usuario:1",
 "livro:1"
);
```

Consultar:

```redis
LRANGE historico:usuario:1 0 -1
```

---

# Aplicação final

Menu:

```
=========================
 Biblioteca Redis
=========================

1 - Cadastrar livro

2 - Consultar livro

3 - Ver ranking

4 - Ver histórico

5 - Sair


Opção:
```

---

# Distribuição da aula (90 minutos)

| Tempo  | Atividade                       |
| ------ | ------------------------------- |
| 10 min | Apresentação Redis + JEDIS      |
| 20 min | Criar conexão Java/Redis + HASH |
| 15 min | Cadastro e consulta             |
| 15 min | Cache com SETEX                 |
| 15 min | Contador + Ranking ZSET         |
| 10 min | Histórico LIST                  |
| 5 min  | Testes e discussão              |

---

# Comandos Redis trabalhados

```redis
SET
GET

SETEX
TTL

HSET
HGETALL

INCR

ZINCRBY
ZREVRANGE

LPUSH
LRANGE
```

