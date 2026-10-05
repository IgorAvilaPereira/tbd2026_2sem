# Aplicação: Sistema de Encurtador de URLs

A ideia é os alunos desenvolverem, durante a aula, um **mini sistema de encurtamento de URLs**, semelhante conceitualmente ao Bitly.

### O sistema fará

O usuário informa:

```text
URL original:
https://www.ifrs.edu.br/cursos/tecnologia-em-analise-e-desenvolvimento-de-sistemas
```

O sistema gera:

```text
Código:
abc123
```

E permite acessar:

```text
/abc123
```

que redireciona para a URL original.

---

## Por que essa aplicação é boa para a revisão?

Porque permite utilizar os dois bancos com funções claramente diferentes:

### MongoDB

Será o banco **persistente**:

```text
urls
--------------------------------
_id
codigo
urlOriginal
dataCriacao
quantidadeAcessos
```

Exemplo:

```json
{
    "codigo": "abc123",
    "urlOriginal": "https://www.ifrs.edu.br",
    "dataCriacao": "2026-10-05",
    "quantidadeAcessos": 15
}
```

Aqui os alunos praticam:

* `MongoClient`
* `MongoDatabase`
* `MongoCollection<Document>`
* `Document`
* `Document.parse()`
* `insertOne()`
* `find()`
* `updateOne()`

---

## Redis

Será utilizado como **cache**.

Quando alguém acessar:

```text
/abc123
```

o programa primeiro procura no Redis:

```text
url:abc123
        ↓
https://www.ifrs.edu.br
```

Se encontrar, utiliza a URL diretamente.

Se não encontrar, busca no MongoDB e coloca o resultado no Redis:

```text
MongoDB
   ↓
encontrou URL
   ↓
Redis
   ↓
url:abc123 → https://www.ifrs.edu.br
```

Podemos colocar um TTL, por exemplo:

```java
jedis.setex(
    "url:abc123",
    300,
    "https://www.ifrs.edu.br"
);
```

Assim, o cache fica válido por **5 minutos**.

---

# Desenvolvimento em aula

Eu dividiria em **4 etapas**, cada uma funcionando antes de passar para a próxima.

### Etapa 1 — Cadastrar URL

Criar:

```text
POST /encurtar
```

Recebe:

```text
url=https://www.ifrs.edu.br
```

Gera:

```text
abc123
```

e salva no MongoDB.

---

### Etapa 2 — Consultar URL

Criar:

```text
GET /abc123
```

Primeiramente:

```java
String url = jedis.get("url:abc123");
```

Se encontrar:

```text
CACHE HIT
```

Se não encontrar:

```text
CACHE MISS
```

e consultar o MongoDB.

Essa parte é excelente para explicar **por que usar Redis**.

---

### Etapa 3 — Cache automático

Depois de encontrar no MongoDB:

```java
jedis.setex(
    "url:" + codigo,
    300,
    urlOriginal
);
```

Na próxima consulta, o MongoDB nem precisa ser acessado.

---

### Etapa 4 — Contador de acessos

Aqui entra outra funcionalidade muito boa do Redis.

Sempre que alguém acessar:

```text
/abc123
```

executar:

```java
jedis.incr("acessos:abc123");
```

Assim:

```text
acessos:abc123 → 1
```

depois:

```text
acessos:abc123 → 2
```

etc.

No final, os alunos podem comparar:

```text
MongoDB:
quantidadeAcessos = 25

Redis:
acessos:abc123 = 25
```

E discutir **por que um contador pode ser interessante no Redis**.

---

# Arquitetura final

Eu colocaria no quadro:

```text
              ┌──────────────┐
              │   Aplicação  │
              └───────┬──────┘
                      │
              /encurtar /abc123
                      │
             ┌────────┴────────┐
             │                 │
             ▼                 ▼
        ┌─────────┐       ┌─────────┐
        │ MongoDB │       │  Redis  │
        └─────────┘       └─────────┘
             │                 │
          URLs             Cache URL
       permanentes        TTL 5 min
                           Contador
```

## O desafio final para os alunos

Entregaria apenas este requisito:

> **Desenvolva uma aplicação de encurtamento de URLs utilizando MongoDB para persistência e Redis para cache e contagem de acessos.**
>
> A aplicação deverá permitir cadastrar uma URL, gerar um código curto e acessar a URL através desse código. Ao acessar uma URL encurtada, o sistema deverá consultar primeiro o Redis e, caso não encontre a informação, consultar o MongoDB e atualizar o cache.



