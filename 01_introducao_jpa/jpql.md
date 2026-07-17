# ✅ **JPQL (Java Persistence Query Language)**

O **JPQL** é a linguagem de consulta usada com JPA. Ela é parecida com SQL, mas trabalha com **entidades e atributos Java**, não com tabelas e colunas diretamente.

---

### 📌 Exemplo 1: Buscar todos os clientes

```java
String jpql = "SELECT c FROM Cliente c";
List<Cliente> clientes = em.createQuery(jpql, Cliente.class).getResultList();
```

---

### 📌 Exemplo 2: Buscar pedidos de um cliente específico

```java
String jpql = "SELECT p FROM Pedido p WHERE p.cliente.id = :idCliente";
List<Pedido> pedidos = em.createQuery(jpql, Pedido.class)
    .setParameter("idCliente", 1L)
    .getResultList();
```

---

### 📌 Exemplo 3: Buscar clientes com nome contendo "Carlos"

```java
String jpql = "SELECT c FROM Cliente c WHERE c.nome LIKE :nome";
List<Cliente> clientes = em.createQuery(jpql, Cliente.class)
    .setParameter("nome", "%Carlos%")
    .getResultList();
```

---

### 📌 Exemplo 4: Contar pedidos

```java
String jpql = "SELECT COUNT(p) FROM Pedido p";
Long totalPedidos = em.createQuery(jpql, Long.class).getSingleResult();
```

---

### 📌 Exemplo 5: Ordenar por nome

```java
String jpql = "SELECT c FROM Cliente c ORDER BY c.nome ASC";
List<Cliente> clientes = em.createQuery(jpql, Cliente.class).getResultList();
```

---


## 📌 Exemplo 6: **Buscar todos os pedidos de um cliente com nome específico**

```java
String jpql = "SELECT p FROM Pedido p WHERE p.cliente.nome = :nome";
List<Pedido> pedidos = em.createQuery(jpql, Pedido.class)
    .setParameter("nome", "Carlos")
    .getResultList();
```

---

## 📌 Exemplo 7: **Buscar pedidos com descrição contendo "notebook" (case-insensitive)**

```java
String jpql = "SELECT p FROM Pedido p WHERE LOWER(p.descricao) LIKE LOWER(:desc)";
List<Pedido> pedidos = em.createQuery(jpql, Pedido.class)
    .setParameter("desc", "%notebook%")
    .getResultList();
```

---

## 📌 Exemplo 8: **Buscar cliente com maior número de pedidos (usando GROUP BY)**

```java
String jpql = """
    SELECT p.cliente.nome, COUNT(p)
    FROM Pedido p
    GROUP BY p.cliente.nome
    ORDER BY COUNT(p) DESC
""";

List<Object[]> resultados = em.createQuery(jpql, Object[].class).getResultList();

for (Object[] resultado : resultados) {
    System.out.println("Cliente: " + resultado[0] + ", Total de pedidos: " + resultado[1]);
}
```

---

## 📌 Exemplo 9: **Buscar clientes que ainda não fizeram pedidos (LEFT JOIN com IS EMPTY)**

```java
String jpql = "SELECT c FROM Cliente c WHERE c.pedidos IS EMPTY";
List<Cliente> clientes = em.createQuery(jpql, Cliente.class).getResultList();
```

---

## 📌 Exemplo 10: **Contar quantos pedidos existem por cliente**

```java
String jpql = """
    SELECT c.nome, COUNT(p)
    FROM Cliente c
    LEFT JOIN c.pedidos p
    GROUP BY c.nome
""";

List<Object[]> resultados = em.createQuery(jpql, Object[].class).getResultList();

for (Object[] resultado : resultados) {
    System.out.println("Cliente: " + resultado[0] + ", Pedidos: " + resultado[1]);
}
```

---

## 📌 Exemplo 11: **Buscar pedidos com JOIN FETCH (para evitar N+1)**

```java
String jpql = "SELECT p FROM Pedido p JOIN FETCH p.cliente";
List<Pedido> pedidos = em.createQuery(jpql, Pedido.class).getResultList();
```

> Isso carrega `Pedido` **e o Cliente associado** em uma única consulta SQL — evita múltiplas queries subsequentes.

---

## ✅ 📌 Exemplo 12: **Buscar o nome dos clientes que fizeram pedidos de "Impressora"**

```java
String jpql = """
    SELECT DISTINCT p.cliente.nome
    FROM Pedido p
    WHERE p.descricao = :desc
""";

List<String> nomes = em.createQuery(jpql, String.class)
    .setParameter("desc", "Impressora")
    .getResultList();
```

---

## 📌 Exemplo 13: **Buscar o cliente e total de pedidos acima de 1**

```java
String jpql = """
    SELECT c.nome, COUNT(p)
    FROM Cliente c JOIN c.pedidos p
    GROUP BY c.nome
    HAVING COUNT(p) > 1
""";

List<Object[]> resultados = em.createQuery(jpql, Object[].class).getResultList();

for (Object[] resultado : resultados) {
    System.out.println("Cliente: " + resultado[0] + ", Total: " + resultado[1]);
}
```

---

## 📌 Exemplo 14: **JPQL com parâmetros dinâmicos e ordenação**

```java
String jpql = "SELECT p FROM Pedido p WHERE p.descricao LIKE :desc ORDER BY p.id DESC";
List<Pedido> pedidos = em.createQuery(jpql, Pedido.class)
    .setParameter("desc", "%Notebook%")
    .getResultList();
```

---

## 📌 Exemplo 15: **Buscar pedidos feitos por clientes com nome começando com "M"**

```java
String jpql = "SELECT p FROM Pedido p WHERE p.cliente.nome LIKE :nome";
List<Pedido> pedidos = em.createQuery(jpql, Pedido.class)
    .setParameter("nome", "M%")
    .getResultList();
```

---

# 🧠 Dicas finais sobre JPQL:

* JPQL **trabalha com entidades Java e atributos**, não com nomes de tabelas e colunas do banco.
* Usa **aliases**, `JOIN`, `GROUP BY`, `ORDER BY`, `HAVING`, `COUNT`, `MAX`, `MIN`, `AVG`, etc.
* Pode retornar:

  * Objetos inteiros (`Cliente`, `Pedido`)
  * Campos simples (`String`, `Long`, etc.)
  * Arrays de objetos (`Object[]`), para retornos mistos
  * DTOs customizados (com `new com.meuprojeto.DadosDTO(...)`)

---

## 🚀 O que você pode fazer agora?

* Usar JPQL para criar filtros, buscas e relatórios
