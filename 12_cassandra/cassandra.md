## 🧠 O que é o Cassandra?

**Apache Cassandra** é um banco de dados **NoSQL orientado a colunas** e **distribuído**. Ele é ideal para aplicações que precisam de:

* Alta disponibilidade (sem ponto único de falha)
* Escalabilidade horizontal (você adiciona mais nós, e o sistema se adapta)
* Escritas rápidas em larga escala
* Resiliência em múltiplos datacenters

---

## 📊 Modelo de Dados

Cassandra usa um modelo de **tabela (semelhante ao relacional)**, mas com algumas diferenças importantes.

### Tabela (Column Family)

Cada tabela contém linhas, e cada linha contém colunas. Mas ao contrário dos bancos relacionais:

* Cada linha pode ter colunas diferentes
* Os dados são **denormalizados** (repetição de dados é comum)
* Não há **joins**, e sim **modelagem orientada à consulta**

### Exemplo de tabela:

```cql
CREATE TABLE usuarios (
    id UUID PRIMARY KEY,
    nome TEXT,
    email TEXT,
    idade INT
);
```

---

## 📘 Conceitos Fundamentais

### 1. **CQL (Cassandra Query Language)**

É a linguagem usada para interagir com Cassandra, parecida com SQL.

### 2. **Chave Primária**

* Define a **unicidade** da linha e sua **distribuição no cluster**.
* Composta por:

  * **Partition key** (responsável pela distribuição dos dados)
  * **Clustering columns** (define a ordenação dentro da partição)

Exemplo:

```cql
CREATE TABLE mensagens (
    usuario_id UUID,
    timestamp TIMESTAMP,
    mensagem TEXT,
    PRIMARY KEY (usuario_id, timestamp)
);
```

### 3. **Partitioning & Replication**

* Cada partição é armazenada em um ou mais **nós (nodes)**.
* O número de cópias dos dados é definido pelo **fator de replicação**.

### 4. **Consistência Tunável**

Você pode escolher o nível de consistência:

* `ONE`: rápida, menos consistente
* `QUORUM`: equilíbrio entre consistência e desempenho
* `ALL`: mais consistente, mas mais lenta

---

## ⚙️ Arquitetura

* **Masterless**: Todos os nós são iguais (não existe servidor principal).
* Usa **particionamento por hash** para distribuir os dados.
* **Resiliente a falhas**: Se um nó falha, outros assumem.

---

## 🚀 Exemplos de comandos CQL

```cql
-- Criação de tabela
CREATE TABLE produtos (
    id UUID PRIMARY KEY,
    nome TEXT,
    preco DECIMAL
);

-- Inserção de dados
INSERT INTO produtos (id, nome, preco)
VALUES (uuid(), 'Celular', 1999.99);

-- Consulta
SELECT * FROM produtos WHERE id = 1234;

-- Atualização
UPDATE produtos SET preco = 1899.99 WHERE id = 1234;

-- Exclusão
DELETE FROM produtos WHERE id = 1234;
```

---

## 🛠️ Ferramentas úteis

* **cqlsh**: terminal de linha de comando do Cassandra
* **DataStax Studio**: interface gráfica para trabalhar com Cassandra
* **Docker**: para rodar instâncias locais rapidamente

---

## Como começar?
---

## 🛠️ Etapa 1: Instalando o Cassandra (via Docker — mais fácil e rápido)

Você precisa ter o **Docker** instalado. Se ainda não tem, veja como:

* [Instalar Docker (Windows/Mac/Linux)](https://www.docker.com/products/docker-desktop/)

### 📦 Rodar Cassandra com Docker

Abra o terminal e execute:

```bash
docker run --name cassandra -p 9042:9042 -d cassandra:latest
```

O Cassandra vai subir em segundo plano. A porta `9042` é usada para conexões com o banco via CQL.

> ⚠️ Aguarde 30–60 segundos até ele estar pronto para aceitar conexões.

---

## 🛠️ Etapa 2: Acessando o Cassandra com `cqlsh`

```bash
docker exec -it cassandra cqlsh
```

Você verá um prompt assim:

```
Connected to Test Cluster at 127.0.0.1:9042.
[cqlsh 6.x.x | Cassandra 4.x.x]
cqlsh>
```

---

## 🧪 Etapa 3: Testando com comandos CQL

### 1. Criar um keyspace (como um "banco de dados")

```cql
CREATE KEYSPACE escola
WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': 1
};
```

### 2. Usar esse keyspace

```cql
USE escola;
```

### 3. Criar uma tabela de exemplo

```cql
CREATE TABLE alunos (
  id UUID PRIMARY KEY,
  nome TEXT,
  idade INT
);
```

### 4. Inserir dados

```cql
INSERT INTO alunos (id, nome, idade)
VALUES (uuid(), 'Maria', 21);
```

### 5. Ver os dados

```cql
SELECT * FROM alunos;
```

---



