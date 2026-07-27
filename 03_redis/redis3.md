# 📂 `09_redis3`

## 📘 Aula 3 – Redis (Uso prático + modelagem real)

---

## 🎯 Objetivo

* Consolidar uso de estruturas do Redis
* Simular cenários reais
* Trabalhar com múltiplos tipos juntos

---

## 🔁 Revisão rápida

```bash
SET chave valor
GET chave

HSET obj campo valor
HGETALL obj

LPUSH lista valor
RPOP lista

SADD set valor
SMEMBERS set

ZADD ranking score valor
ZRANGE ranking 0 -1 WITHSCORES
```

---

# 🧪 Parte 1 – Simulando um sistema real

## 📌 Cenário: sistema de alunos

### 1. Criar alunos

```bash
HSET aluno:1 nome "Ana" curso "ADS" idade 20
HSET aluno:2 nome "Bruno" curso "SI" idade 22
HSET aluno:3 nome "Carlos" curso "ADS" idade 19
```

---

### 2. Listar alunos

```bash
HGETALL aluno:1
HGETALL aluno:2
HGETALL aluno:3
```

---

# 🧪 Parte 2 – Relacionamento (curso → alunos)

## 📌 Usando SET

```bash
SADD curso:ADS "Ana" "Carlos"
SADD curso:SI "Bruno"
```

```bash
SMEMBERS curso:ADS
```

---

# 🧪 Parte 3 – Fila de processamento

## 📌 Simulando tarefas

```bash
LPUSH fila:processamento "job1"
LPUSH fila:processamento "job2"
LPUSH fila:processamento "job3"
```

```bash
LRANGE fila:processamento 0 -1
```

### Processando

```bash
RPOP fila:processamento
```

---

# 🧪 Parte 4 – Ranking de alunos

```bash
ZADD ranking:notas 8 "Ana"
ZADD ranking:notas 6 "Bruno"
ZADD ranking:notas 9 "Carlos"
```

```bash
ZRANGE ranking:notas 0 -1 WITHSCORES
```

---

# 🧪 Parte 5 – Cache com expiração

```bash
SET pagina:home "conteudo cache"
EXPIRE pagina:home 30
TTL pagina:home
```

---

# 🧱 Parte 6 – Padrão do repositório (IMPORTANTE)

## 📌 Nomeação usada

```bash
tipo:identificador
```

### Exemplos do curso:

```bash
aluno:1
curso:ADS
fila:processamento
ranking:notas
pagina:home
```

---

# 🧠 Parte 7 – Integração (DESAFIO GUIADO)

## 🎯 Criar um mini sistema

### Requisitos:

1. Criar 3 alunos (hash)
2. Criar cursos (set)
3. Criar fila de matrícula (list)
4. Criar ranking (sorted set)

---

## 💻 Exemplo esperado:

```bash
# alunos
HSET aluno:10 nome "Maria" curso "ADS"
HSET aluno:11 nome "João" curso "SI"

# cursos
SADD curso:ADS "Maria"
SADD curso:SI "João"

# fila
LPUSH fila:matricula "Maria"
LPUSH fila:matricula "João"

# ranking
ZADD ranking 10 "Maria"
ZADD ranking 8 "João"
```

---

# 🧪 Exercícios (padrão lista)

## 1.

Crie um professor com:

* nome
* disciplina

---

## 2.

Simule uma fila com 5 elementos e processe 3

---

## 3.

Crie um set com alunos sem repetição

---

## 4.

Crie um ranking com 5 elementos

---

## 5.

Use TTL em pelo menos 2 chaves

---

# 💬 Perguntas rápidas

* Qual estrutura usar para fila?
* Qual usar para ranking?
* Quando usar Hash ao invés de String?

---

# 🚀 Observação (estilo do teu repo)

👉 Redis não substitui banco relacional
👉 Ele complementa (cache, fila, performance)

---

