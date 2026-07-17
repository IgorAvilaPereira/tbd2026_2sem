# **JPQL**, **Criteria API**, e **SQL nativo**

## 🔍 Suponha que temos essa entidade:

```java
@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String cidade;

    // Getters e Setters
}
```

Agora vamos buscar todos os **clientes da cidade de "São Paulo"**, usando **JPQL**, **Criteria API**, e **SQL nativo**.

---

## ✅ 1. Consulta com **JPQL**

> Mais usada, legível, parecida com SQL, mas baseada em entidades.

```java
String jpql = "SELECT c FROM Cliente c WHERE c.cidade = :cidade";
List<Cliente> clientes = em.createQuery(jpql, Cliente.class)
    .setParameter("cidade", "São Paulo")
    .getResultList();
```

---

## ✅ 2. Consulta com **Criteria API**

> Totalmente orientada a objetos. Boa para **consultas dinâmicas**.

```java
CriteriaBuilder cb = em.getCriteriaBuilder();
CriteriaQuery<Cliente> cq = cb.createQuery(Cliente.class);
Root<Cliente> root = cq.from(Cliente.class);

cq.select(root).where(cb.equal(root.get("cidade"), "São Paulo"));

List<Cliente> clientes = em.createQuery(cq).getResultList();
```

---

## ✅ 3. Consulta com **SQL nativo**

> Usa SQL real. Ideal quando JPQL não atende (joins complexos, procedures, views, etc.).

```java
List<Cliente> clientes = em.createNativeQuery(
    "SELECT * FROM cliente WHERE cidade = ?", Cliente.class)
    .setParameter(1, "São Paulo")
    .getResultList();
```

> ⚠️ Note que a tabela aqui é `cliente`, do banco, **não a entidade**.

---

# 📊 Comparativo Rápido

| Tipo           | Baseada em... | Complexidade | Flexível/Dinâmica | Performance |
| -------------- | ------------- | ------------ | ----------------- | ----------- |
| **JPQL**       | Entidades     | Baixa        | Média             | Boa         |
| **Criteria**   | Entidades     | Alta         | Alta              | Boa         |
| **SQL nativo** | Tabelas reais | Média        | Alta              | Alta        |

---

# 🧪 Outros Exemplos

## 📌 Buscar nome dos clientes (JPQL)

```java
String jpql = "SELECT c.nome FROM Cliente c WHERE c.cidade = :cidade";
List<String> nomes = em.createQuery(jpql, String.class)
    .setParameter("cidade", "São Paulo")
    .getResultList();
```

---

## 📌 JPQL com `LIKE`

```java
String jpql = "SELECT c FROM Cliente c WHERE c.nome LIKE :nome";
List<Cliente> clientes = em.createQuery(jpql, Cliente.class)
    .setParameter("nome", "%Carlos%")
    .getResultList();
```

---

## 📌 SQL nativo com DTO

Você pode mapear resultado para DTO com `Object[]`:

```java
List<Object[]> resultados = em.createNativeQuery(
    "SELECT nome, cidade FROM cliente WHERE cidade = ?")
    .setParameter(1, "São Paulo")
    .getResultList();

for (Object[] linha : resultados) {
    System.out.println("Nome: " + linha[0] + ", Cidade: " + linha[1]);
}
```

---

## 📌 Criteria com múltiplas condições

```java
Predicate cond1 = cb.equal(root.get("cidade"), "São Paulo");
Predicate cond2 = cb.like(root.get("nome"), "%a%");

cq.select(root).where(cb.and(cond1, cond2));
```
