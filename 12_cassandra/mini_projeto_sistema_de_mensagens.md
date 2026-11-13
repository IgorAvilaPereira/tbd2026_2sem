## 🚧 Mini-Projeto: Sistema de Mensagens

### Cenário:

Vamos criar um sistema simples em que usuários enviam mensagens uns aos outros. O sistema armazena:

* Quem enviou (`remetente_id`)
* Para quem enviou (`destinatario_id`)
* A mensagem
* A data e hora do envio

---

## 🔨 Etapa 1: Criar Keyspace e Tabela

### 1. Criar o keyspace:

```cql
CREATE KEYSPACE mensageria
WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': 1
};
```

### 2. Usar o keyspace:

```cql
USE mensageria;
```

### 3. Criar a tabela de mensagens:

```cql
CREATE TABLE mensagens (
  remetente_id UUID,
  destinatario_id UUID,
  data_envio TIMESTAMP,
  mensagem TEXT,
  PRIMARY KEY ((remetente_id, destinatario_id), data_envio)
) WITH CLUSTERING ORDER BY (data_envio DESC);
```

🔍 **Explicação da modelagem:**

* `PRIMARY KEY ((remetente_id, destinatario_id), data_envio)`:

  * `remetente_id` e `destinatario_id`: determinam **a partição**.
  * `data_envio`: ordena as mensagens por tempo dentro dessa partição.
* Ideal para consultar mensagens de um usuário para outro, em ordem decrescente.

---

## 🧪 Etapa 2: Inserir alguns dados

```cql
INSERT INTO mensagens (remetente_id, destinatario_id, data_envio, mensagem)
VALUES (uuid(), uuid(), toTimestamp(now()), 'Olá! Tudo bem?');

INSERT INTO mensagens (remetente_id, destinatario_id, data_envio, mensagem)
VALUES (uuid(), uuid(), toTimestamp(now()), 'Você vai na reunião amanhã?');
```

📌 Obs: Para testes manuais, você pode gerar os mesmos UUIDs para remetente/destinatário se quiser controlar melhor os dados.

---

## 🔎 Etapa 3: Consultar mensagens

```cql
-- Substitua pelos UUIDs reais usados na inserção
SELECT * FROM mensagens
WHERE remetente_id = 1111-aaaa-... AND destinatario_id = 2222-bbbb-...
ORDER BY data_envio DESC;
```

---

## 🧠 Etapa 4: Exercícios (com respostas depois)

### 📌 Exercício 1:

Crie uma tabela `usuarios` com os campos:

* `id UUID PRIMARY KEY`
* `nome TEXT`
* `email TEXT`
* `data_cadastro TIMESTAMP`

---

### 📌 Exercício 2:

Baseado na tabela de mensagens:

* Como você faria para consultar **todas as mensagens enviadas por um remetente**, independentemente do destinatário?

📌 *Spoiler: isso exigiria uma modelagem diferente, porque Cassandra não permite buscar só por `remetente_id` quando ele está em uma partição composta.*

---





