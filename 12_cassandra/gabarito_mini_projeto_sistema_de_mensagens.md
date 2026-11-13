## ✅ **Exercício 1: Criar tabela `usuarios`**

### 📌 Enunciado:

Crie uma tabela `usuarios` com os campos:

* `id UUID PRIMARY KEY`
* `nome TEXT`
* `email TEXT`
* `data_cadastro TIMESTAMP`

### 🧩 Solução:

```cql
CREATE TABLE usuarios (
  id UUID PRIMARY KEY,
  nome TEXT,
  email TEXT,
  data_cadastro TIMESTAMP
);
```

🔍 **Explicação**:

* A chave primária é o `id`, que garante unicidade.
* Não é necessário clustering columns porque você vai buscar geralmente por `id` direto.
* Tabela simples para armazenar perfis de usuários.

---

## ✅ **Exercício 2: Consultar todas as mensagens de um remetente**

### 📌 Enunciado:

Como consultar **todas as mensagens enviadas por um remetente**, independentemente do destinatário?

### 💡 Problema:

Na nossa modelagem original, a chave primária é:

```sql
PRIMARY KEY ((remetente_id, destinatario_id), data_envio)
```

⚠️ **Você não pode consultar só por `remetente_id`**, pois Cassandra exige que você use **toda a partition key** (no caso, `remetente_id` **e** `destinatario_id`).

---

## ✅ Solução alternativa: nova tabela focada no remetente

Se você quer **consultar todas as mensagens enviadas por um remetente**, precisa de uma tabela com outra modelagem:

```cql
CREATE TABLE mensagens_por_remetente (
  remetente_id UUID,
  data_envio TIMESTAMP,
  destinatario_id UUID,
  mensagem TEXT,
  PRIMARY KEY (remetente_id, data_envio)
) WITH CLUSTERING ORDER BY (data_envio DESC);
```

Agora você pode fazer:

```cql
SELECT * FROM mensagens_por_remetente
WHERE remetente_id = 1111-aaaa-...;
```

🔁 Isso é chamado de **modelagem orientada à consulta**: você cria tabelas **específicas para as queries que precisa fazer**.

---

## ✅ Resumo até agora

| Objetivo de Consulta               | Tabela Necessária                            |
| ---------------------------------- | -------------------------------------------- |
| Mensagens de A para B              | `mensagens`                                  |
| Todas as mensagens enviadas por A  | `mensagens_por_remetente`                    |
| Todas as mensagens recebidas por B | (Outra tabela: `mensagens_por_destinatario`) |

---

Se quiser, é possível montar também uma tabela para **mensagens recebidas por um usuário**, ou adicionar funcionalidades como **status da mensagem (lida/não lida)**.
